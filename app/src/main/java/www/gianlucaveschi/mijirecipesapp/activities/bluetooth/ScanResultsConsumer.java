package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * MainBluetoothActivity.java needs to implement this interface so that it can receive
 * and process data produced by the Bluetooth scanning process.
 * */
public interface ScanResultsConsumer {

    public void candidateBleDevice(BluetoothDevice device, byte[] scan_record, int rssi);
    public void scanningStarted();
    public void scanningStopped();
}
