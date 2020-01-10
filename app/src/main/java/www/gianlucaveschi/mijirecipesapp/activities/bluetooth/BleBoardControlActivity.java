package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

import android.annotation.SuppressLint;
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

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;
import www.gianlucaveschi.mijirecipesapp.activities.bluetooth.services.BleAdapterService;
import www.gianlucaveschi.mijirecipesapp.activities.bluetooth.services.BleAdapterService.LocalBinder;
import www.gianlucaveschi.mijirecipesapp.activities.bluetooth.utils.BleConstants;
import www.gianlucaveschi.mijirecipesapp.activities.bluetooth.utils.DataHelper;

public class BleBoardControlActivity extends AppCompatActivity {

    private static final String TAG = "BleBoardControlActivity";
    
    private BleAdapterService bluetooth_le_adapter;

    //BindView
    @BindView(R.id.nameTextView)        TextView deviceNameTv;
    @BindView(R.id.connectionState)     TextView connectionStateTv;
    @BindView(R.id.connectButton)       Button   connectButton;
    @BindView(R.id.readChar)            Button   readCharButton;
    @BindView(R.id.writeChar)           Button   WriteCharButton;


    //class variables
    private String device_name;
    private String device_address;
    private boolean backBtnWasPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_board_control);
        ButterKnife.bind(this);

        // read incoming intent data from the MainActivity
        final Intent intent = getIntent();
        device_name    = intent.getStringExtra(BleConstants.EXTRA_NAME);
        device_address = intent.getStringExtra(BleConstants.EXTRA_ID);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(service_connection);
        bluetooth_le_adapter = null;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        backBtnWasPressed = true;
        if (bluetooth_le_adapter.isConnected()) {
            try {
                bluetooth_le_adapter.disconnect();
            } catch (Exception e) {
                Log.d(TAG, "onBackPressed: " + e.getMessage());
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
        Log.d(TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectionStateTv.setText(msg);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler message_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;

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
                    showMsg("CONNECTED");

                    //Discover Services
                    bluetooth_le_adapter.discoverServices();

                    break;

                case BleAdapterService.GATT_DISCONNECT:
                    //Update UI
                    connectButton.setEnabled(true);
                    showMsg("DISCONNECTED");

                    //takes into account that the user has pressed the back button and
                    // completes the process of exiting the current screen:
                    if (backBtnWasPressed) {
                        BleBoardControlActivity.this.finish();
                    }
                    break;

                case BleAdapterService.GATT_SERVICES_DISCOVERED:
                    //Validate services and if ok...
                    List<BluetoothGattService> services_list = bluetooth_le_adapter.getSupportedGattServices();

                    //LOG SERVICES
                    DataHelper.logServices(TAG,services_list);

                    break;

                case BleAdapterService.NOTIFICATION_OR_INDICATION_RECEIVED:

                    showMsg("NOTIFICATION_OR_INDICATION_RECEIVED");
                    
            }
        }
    };
}
