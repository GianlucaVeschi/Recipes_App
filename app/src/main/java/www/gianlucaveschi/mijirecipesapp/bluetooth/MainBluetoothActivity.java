package www.gianlucaveschi.mijirecipesapp.bluetooth;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.bluetooth.services.BleScanner;
import www.gianlucaveschi.mijirecipesapp.bluetooth.services.ScanResultsConsumer;
import www.gianlucaveschi.mijirecipesapp.bluetooth.utils.BleConstants;
import www.gianlucaveschi.mijirecipesapp.adapters.BleDeviceAdapter;

public class MainBluetoothActivity extends AppCompatActivity implements ScanResultsConsumer {

    @BindView(R.id.deviceList)      ListView deviceList;
    @BindView(R.id.scanButton)      Button scanButton;

    private boolean ble_scanning = false;
    private BleDeviceAdapter ble_device_list_adapter;
    private BleScanner ble_scanner;
    private static final long SCAN_TIMEOUT = 5000;
    private static final int REQUEST_LOCATION = 0;
    private boolean permissions_granted=false;
    private int device_count=0;
    private Toast toast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bluetooth);
        ButterKnife.bind(this);

        //Set Scanner
        ble_scanner = new BleScanner(this.getApplicationContext());


        //Set Adapter
        ble_device_list_adapter = new BleDeviceAdapter();
        deviceList.setAdapter(ble_device_list_adapter);

        //Set ScanButton
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScan(v);
            }
        });

        //Set Interactive Device List
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ble_scanning) {
                    setScanState(false);
                    ble_scanner.stopScanning();
                }
                BluetoothDevice device = ble_device_list_adapter.getDevice(position);
                if (toast != null) { toast.cancel(); }

                //Access Device According to its name
                if(device.getName() != null){
                    switch(device.getName()){
                        case BleConstants.miji_DEVICE_NAME:
                            Intent mijiThermometerIntent = new Intent(MainBluetoothActivity.this, MijiTemperatureControlActivity.class);
                            mijiThermometerIntent.putExtra(BleConstants.EXTRA_NAME, device.getName());
                            mijiThermometerIntent.putExtra(BleConstants.EXTRA_ID, device.getAddress());
                            startActivity(mijiThermometerIntent);
                            break;
                        case BleConstants.BT_BOARD:
                            Intent BtBoardIntent = new Intent(MainBluetoothActivity.this, BleBoardControlActivity.class);
                            BtBoardIntent.putExtra(BleConstants.EXTRA_NAME, device.getName());
                            BtBoardIntent.putExtra(BleConstants.EXTRA_ID, device.getAddress());
                            startActivity(BtBoardIntent);
                            break;
                        default:
                            Toast.makeText(MainBluetoothActivity.this, "UNKNOWN DEVICE", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainBluetoothActivity.this, "UNKNOWN DEVICE", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    //Respond to the Find Miji Device Button
    public void onScan(View view) {
        if (!ble_scanner.isScanning()) {
            device_count = 0;

            //This is only an issue if running on a device with Android 6 or a later version.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissions_granted = false;
                    requestLocationPermission();
                } else {
                    Log.i(BleConstants.BT_TAG, "Location permission has already been granted. Starting scanning.");
                    permissions_granted = true;
                }
            } else {
                // the ACCESS_COARSE_LOCATION permission did not exist before M so....
                permissions_granted = true;
            }

            startScanning();
        } else {
            ble_scanner.stopScanning();
        }
    }

    private void startScanning() {
        if (permissions_granted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ble_device_list_adapter.clear();
                    ble_device_list_adapter.notifyDataSetChanged();
                }
            });
            simpleToast(BleConstants.SCANNING, 2000);
            ble_scanner.startScanning(this, SCAN_TIMEOUT);
        } else {
            Log.i(BleConstants.BT_TAG, "Permission to perform Bluetooth scanning was not yet granted");
        }
    }


    private void setScanState(boolean value) {
        ble_scanning = value;
        scanButton.setText(value ? BleConstants.STOP_SCANNING : BleConstants.FIND);
    }

    @Override
    public void candidateBleDevice(final BluetoothDevice device, byte[] scan_record, int rssi) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ble_device_list_adapter.addDevice(device);
                ble_device_list_adapter.notifyDataSetChanged();
                device_count++;
            }
        });
    }

    @Override
    public void scanningStarted() {
        setScanState(true);
    }

    @Override
    public void scanningStopped() {
        if (toast != null) {
            toast.cancel();
        }
        setScanState(false);
    }

    private void simpleToast(String message, int duration) {
        toast = Toast.makeText(this, message, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * HANDLE PERMISSION METHODS
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            Log.i(BleConstants.BT_TAG, "Received response for location permission request.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted
                Log.i(BleConstants.BT_TAG, "Location permission has now been granted. Scanning.....");
                permissions_granted = true;
                if (ble_scanner.isScanning()) {
                    startScanning();
                }
            } else {
                Log.i(BleConstants.BT_TAG, "Location permission was NOT granted.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void requestLocationPermission() {
        Log.i(BleConstants.BT_TAG, "Location permission has NOT yet been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.i(BleConstants.BT_TAG, "Displaying location permission rationale to provide additional context.");
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission Required");
            builder.setMessage("Please grant Location access so this application can perform Bluetooth scanning");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Log.d(BleConstants.BT_TAG, "Requesting permissions after explanation");
                    ActivityCompat.requestPermissions(MainBluetoothActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                }
            });
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
    }
}
