package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import java.util.List;

public class DataHelper {

    public static String byteArrayAsHexString(byte[] bytes) {
        if (bytes == null) {
            return "[null]";
        }
        int l = bytes.length;
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < l; i++) {
            if ((bytes[i] >= 0) & (bytes[i] < 16))
                hex.append("0");
            hex.append(Integer.toString(bytes[i] & 0xff, 16).toUpperCase());
        }
        return hex.toString();
    }

    //LOG CHARACTERISTICS
    public static void logCharacteristics(List<BluetoothGattCharacteristic> characteristics, String service_name){
        Log.d(BleConstants.BT_TAG, "logCharacteristics of: " + service_name + "\n");
        for(BluetoothGattCharacteristic characteristic: characteristics){
            Log.d(BleConstants.BT_TAG, "--->  " + characteristic.toString());
            Log.d(BleConstants.BT_TAG, "temp char uuid: " + characteristic.getUuid());
            try{
                Log.d(BleConstants.BT_TAG, "--->  " + DataHelper.byteArrayAsHexString(characteristic.getValue()));
            }
            catch (Exception e){
                Log.d(BleConstants.BT_TAG, "--->  " + "nullPointerException caught");
            }
            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
            DataHelper.logDescriptors(descriptors,characteristic.toString());
        }
    }

    public static void logDescriptors(List<BluetoothGattDescriptor> descriptors, String characteristics_name){
        Log.d(BleConstants.BT_TAG, "logDescriptors of: " + characteristics_name + "\n");
        if(!descriptors.isEmpty()){
            for(BluetoothGattDescriptor descriptor : descriptors){
                try{
                    Log.d(BleConstants.BT_TAG, "--->  " + DataHelper.byteArrayAsHexString(descriptor.getValue()));
                }
                catch (Exception e){
                    Log.d(BleConstants.BT_TAG, "--->  " + "nullPointerException caught");
                }
            }
        }
        else{
            //Log.d(TAG, "NO DESCRIPTORS FOUND :" + descriptors.size());
        }
    }
}
