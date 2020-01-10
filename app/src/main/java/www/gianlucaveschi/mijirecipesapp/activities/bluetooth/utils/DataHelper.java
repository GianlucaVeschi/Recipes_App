package www.gianlucaveschi.mijirecipesapp.activities.bluetooth.utils;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;

import static www.gianlucaveschi.mijirecipesapp.activities.bluetooth.utils.BleConstants.BT_TAG;

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

    public static void logServices(String TAG, List<BluetoothGattService> services_list){
        //LOG SERVICES
        Log.d(TAG, "--------------------- LOG SERVICES ---------------------");
        for (BluetoothGattService svc : services_list) {
            Log.d(TAG,
                    "UUID = "     + svc.getUuid().toString().toUpperCase() + " " +
                         "INSTANCE = " + svc.getInstanceId()); //Returns the instance ID for this service if a remote device offers
        }
        Log.d(TAG, "-------------------- END LOG SERVICES -------------------\n");
    }
    //LOG CHARACTERISTICS
    public static void logCharacteristics(List<BluetoothGattCharacteristic> characteristics, String service_name){
        Log.d(BT_TAG, "logCharacteristics of: " + service_name + "\n");
        for(BluetoothGattCharacteristic characteristic: characteristics){
            Log.d(BT_TAG, "--->  " + characteristic.toString());
            Log.d(BT_TAG, "temp char uuid: " + characteristic.getUuid());
            try{
                Log.d(BT_TAG, "--->  " + DataHelper.byteArrayAsHexString(characteristic.getValue()));
            }
            catch (Exception e){
                Log.d(BT_TAG, "--->  " + "nullPointerException caught");
            }
            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
            DataHelper.logDescriptors(descriptors,characteristic.toString());
        }
    }

    public static void logDescriptors(List<BluetoothGattDescriptor> descriptors, String characteristics_name){
        Log.d(BT_TAG, "logDescriptors of: " + characteristics_name + "\n");
        if(!descriptors.isEmpty()){
            for(BluetoothGattDescriptor descriptor : descriptors){
                try{
                    Log.d(BT_TAG, "--->  " + DataHelper.byteArrayAsHexString(descriptor.getValue()));
                }
                catch (Exception e){
                    Log.d(BT_TAG, "--->  " + "nullPointerException caught");
                }
            }
        }
        else{
            //Log.d(TAG, "NO DESCRIPTORS FOUND :" + descriptors.size());
        }
    }
}
