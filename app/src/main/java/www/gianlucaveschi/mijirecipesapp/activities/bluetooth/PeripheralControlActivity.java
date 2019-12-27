package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;
import java.util.Timer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.bluetooth.BleAdapterService.LocalBinder;

public class PeripheralControlActivity extends AppCompatActivity {


    private BleAdapterService bluetooth_le_adapter;
    private TemperatureMap temperatureMapper = new TemperatureMap();

    //BindView
    @BindView(R.id.switchMonitTemp)     Switch   temperatureSwitch;
    @BindView(R.id.connectButton)       Button   connectButton;
    @BindView(R.id.rectangle)           LinearLayout rssiRectangle;
    @BindView(R.id.nameTextView)        TextView deviceNameTv;
    @BindView(R.id.temperatureValueTv)  TextView temperatureValueTv;


    //class variables
    private String device_name;
    private String device_address;
    private boolean backBtnWasPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral_control);
        ButterKnife.bind(this);

        // read incoming intent data from the MainActivity
        final Intent intent = getIntent();
        device_name    = intent.getStringExtra(Constants.EXTRA_NAME);
        device_address = intent.getStringExtra(Constants.EXTRA_ID);

        // show the device name
        String concatName = "Device : " + device_name + " ["+ device_address+"]";
        deviceNameTv.setText(concatName);


        //Set Connect Button event Listener
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnect(v);
            }
        });

        // connect to the Bluetooth adapter service
        Intent gattServiceIntent = new Intent(this, BleAdapterService.class);
        bindService(gattServiceIntent, service_connection, BIND_AUTO_CREATE);
        showMsg("READY");

        //Disabled as long as device is not connected
        temperatureSwitch.setEnabled(false);
        temperatureSwitch.setChecked(false);
        temperatureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleTemperatureSwitch(isChecked);
            }
        });

        //Slide back to the Previous Activity
        // TODO: 25/12/2019 :
        //  When sliding left, the bluetooth connection does not get disconnected.
        //  Think about a solution
        //Slidr.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(service_connection);
        bluetooth_le_adapter = null;
    }

    /**If we’re connected to the peripheral device when the user presses the back button,
     * we need to respond by first disconnecting and then allowing the default response
     * to pressing the back button to be taken.
     * This function will be automatically called*/
    @Override
    public void onBackPressed() {
        Log.d(Constants.BT_TAG, "onBackPressed");
        backBtnWasPressed = true;
        if (bluetooth_le_adapter.isConnected()) {
            try {
                bluetooth_le_adapter.disconnect();
            } catch (Exception e) {
                Log.d(Constants.BT_TAG, "onBackPressed: " + e.getMessage());
            }
        } else {
            finish();
        }
    }

    private final ServiceConnection service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetooth_le_adapter = ((LocalBinder) service).getService();
            bluetooth_le_adapter.setActivityHandler(message_handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetooth_le_adapter = null;
        }
    };

    private void onConnect(View v) {
        showMsg("onConnect");
        if (bluetooth_le_adapter != null) {
            if (bluetooth_le_adapter.connect(device_address)) {
                connectButton.setEnabled(false);
            } else {
                showMsg("onConnect: failed to connect");
            }
        } else {
            showMsg("onConnect: bluetooth_le_adapter=null");
        }
    }

    private void showMsg(final String msg) {
        Log.d(Constants.BT_TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.msgTextView)).setText(msg);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler message_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            String characteristic_uuid;
            byte[] b;

            // message handling logic
            switch (msg.what) {
                case BleAdapterService.MESSAGE:
                    bundle = msg.getData();
                    String text = bundle.getString(BleAdapterService.PARCEL_TEXT);
                    showMsg(text);
                    break;
                case BleAdapterService.GATT_CONNECTED:

                    //Update UI
                    connectButton.setEnabled(false);
                    temperatureSwitch.setEnabled(true);
                    rssiRectangle.setVisibility(View.VISIBLE);
                    showMsg("CONNECTED");

                    //Discover Services
                    bluetooth_le_adapter.discoverServices();

                    break;

                case BleAdapterService.GATT_DISCONNECT:
                    //Update UI
                    connectButton.setEnabled(true);
                    temperatureSwitch.setEnabled(false);
                    temperatureSwitch.setChecked(false);
                    showMsg("DISCONNECTED");

                    //takes into account that the user has pressed the back button and
                    // completes the process of exiting the current screen:
                    if (backBtnWasPressed) {
                        PeripheralControlActivity.this.finish();
                    }
                    break;

                case BleAdapterService.GATT_SERVICES_DISCOVERED:
                    //Validate services and if ok...
                    List<BluetoothGattService> services_list = bluetooth_le_adapter.getSupportedGattServices();

                    boolean miji_device_information=false;
                    boolean miji_generic_access=false;
                    boolean miji_generic_attribute=false;
                    boolean miji_service_uuid=false;

                    //LOG SERVICES
                    for (BluetoothGattService svc : services_list) {
                        Log.d(Constants.BT_TAG,
                                "UUID=" + svc.getUuid().toString().toUpperCase()
                                        + " INSTANCE=" + svc.getInstanceId()); //Returns the instance ID for this service if a remote device offers

                        //multiple services with the same UUID
                        if (svc.getUuid().toString().equalsIgnoreCase(Constants.miji_DEVICE_INFORMATION)) {
                            miji_device_information = true;
                            List<BluetoothGattCharacteristic> characteristics_list = svc.getCharacteristics();
                            //DataHelper.logCharacteristics(characteristics_list,"miji_DEVICE_INFORMATION");
                            continue;
                        }

                        if (svc.getUuid().toString().equalsIgnoreCase(Constants.miji_GENERIC_ACCESS)) {
                            miji_generic_access = true;
                            List<BluetoothGattCharacteristic> characteristics_list = svc.getCharacteristics();
                            //DataHelper.logCharacteristics(characteristics_list,"miji_GENERIC_ACCESS");
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(Constants.miji_GENERIC_ATTRIBUTE)) {
                            miji_generic_attribute = true;
                            List<BluetoothGattCharacteristic> characteristics_list = svc.getCharacteristics();
                            //DataHelper.logCharacteristics(characteristics_list,"miji_GENERIC_ATTRIBUTE");
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(Constants.miji_TEMPERATURE_SERVICE)) {
                            miji_service_uuid = true;
                            List<BluetoothGattCharacteristic> characteristics_list = svc.getCharacteristics();
                            DataHelper.logCharacteristics(characteristics_list,"miji_TEMPERATURE_SERVICE");
                            continue;
                        }
                    }
                    if (miji_device_information && miji_generic_access && miji_generic_attribute && miji_service_uuid) {
                        showMsg("Device has expected services");

                        /**After service discovery has completed and we’ve validated the services on the device,
                         * we’ll read the characteristics
                         bluetooth_le_adapter.readCharacteristic(
                         Constants.eLINK_LOSS_SERVICE_UUID,
                         Constants.eALERT_LEVEL_CHARACTERISTIC);*/
                    } else {
                        showMsg("Device does not have expected GATT services");
                    }
                    break;

                case BleAdapterService.NOTIFICATION_OR_INDICATION_RECEIVED:

                    bundle = msg.getData();
                    characteristic_uuid = bundle.getString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID);
                    b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE);
                    showMsg("NOTIFICATION_OR_INDICATION_RECEIVED");
                    temperatureValueTv.setTextSize(60);

                    //Read temperature characteristic
                    if (characteristic_uuid.equalsIgnoreCase((Constants.miji_TEMPERATURE_CHARACTERISTIC))) {
                        Log.d(Constants.BT_TAG, "Handling bundle temp_serv_char_1");
                        String temperatureKey = DataHelper.byteArrayAsHexString(b).trim().toLowerCase();
                        String temperatureValue = temperatureMapper.getValues().get(temperatureKey);
                        Log.d(Constants.BT_TAG, "temperature key: " + temperatureKey + " is " + temperatureValue);
                        showTemperature(temperatureValue);

                    }

                    //what is this characteristic?
                    if (characteristic_uuid.equalsIgnoreCase((Constants.miji_TEMPERATURE_SERVICE_CHAR_2))) {
                        Log.d(Constants.BT_TAG, "Handling bundle temp_serv_char_2");
                        Log.d(Constants.BT_TAG,"bundle as byte array " + b);
                        Log.d(Constants.BT_TAG, "bundle as String " + DataHelper.byteArrayAsHexString(b));
                        Log.d(Constants.BT_TAG, "bundle length:" + b.length);
                    }
                    break;
            }
        }
    };

    /**
     * Temperature Related
     * */
    private void handleTemperatureSwitch(boolean isChecked) {
        if (bluetooth_le_adapter != null && bluetooth_le_adapter.isConnected()) {
            if (!isChecked) {
                //DESubscribe to temperature indications
                showMsg("Switching off temperature monitoring");
                if (bluetooth_le_adapter.setIndicationsState(
                        Constants.miji_TEMPERATURE_SERVICE,
                        Constants.miji_TEMPERATURE_CHARACTERISTIC,
                        false)) {
                    clearTemperature();
                } else {
                    showMsg("Failed to inform temperature monitoring has been disabled");
                }
            } else {
                //Subscribe to temperature indications
                if (bluetooth_le_adapter.setIndicationsState(
                        Constants.miji_TEMPERATURE_SERVICE,
                        Constants.miji_TEMPERATURE_CHARACTERISTIC,
                        true)) {
                    showMsg("Switching on temperature monitoring");
                } else {
                    showMsg("Failed to inform temperature monitoring has been enabled");
                }
            }
        }
    }

    private void showTemperature(final String temperature) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.BT_TAG, "run: " + temperature);
                String temperatureString = temperature + "C°";
                temperatureValueTv.setText(temperatureString);
            }
        });
    }

    private void clearTemperature() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String notAvailable = "not available";
                temperatureValueTv.setText(notAvailable);
            }
        });
    }


}
