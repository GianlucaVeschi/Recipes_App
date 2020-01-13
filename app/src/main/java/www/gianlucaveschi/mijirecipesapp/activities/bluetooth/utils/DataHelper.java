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

    //LOG SERVICES
    public static void logServices(String TAG, List<BluetoothGattService> services_list){
        Log.d(TAG, "--------------------- LOG SERVICES ---------------------");
        for (BluetoothGattService svc : services_list) {
            Log.d(TAG,
                    "UUID = "     + svc.getUuid().toString().toUpperCase() + " " +
                         "INSTANCE = " + svc.getInstanceId()); //Returns the instance ID for this service if a remote device offers
        }
        Log.d(TAG, "-------------------- END LOG SERVICES -------------------\n");

        //LOG CHARACTERISTICS OF THE SERVICE
        for(BluetoothGattService svc : services_list){
                //DataHelper.logCharacteristics(TAG,svc.getCharacteristics());
        }
    }

    //LOG CHARACTERISTICS
    private static void logCharacteristics( String TAG,List<BluetoothGattCharacteristic> characteristics){
        Log.d(TAG, "--------------------- LOG CHARACTERISTICS ---------------------");
        for(BluetoothGattCharacteristic characteristic: characteristics){
            Log.d(BT_TAG, "--->  " + characteristic.toString());
            Log.d(BT_TAG, "char uuid: " + characteristic.getUuid());

            //LOG DESCRIPTORS OF THE CHARACTERISTIC
            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
            DataHelper.logDescriptors(descriptors,characteristic.toString());
        }
        Log.d(TAG, "-------------------- END LOG CHARACTERISTICS -------------------\n");
    }

    //LOG DESCRIPTORS
    private static void logDescriptors(List<BluetoothGattDescriptor> descriptors, String characteristics_name){
        Log.d(BT_TAG, "--------------------- LOG DESCRIPTORS OF " + characteristics_name + " ---------------------");
        if(!descriptors.isEmpty()){
            for(BluetoothGattDescriptor descriptor : descriptors){
                    Log.d(BT_TAG, "--->  " + DataHelper.byteArrayAsHexString(descriptor.getValue()));
            }
            Log.d(BT_TAG, "-------------------- END LOG DESCRIPTORS -------------------\n");
        }
        else{
            Log.d(BT_TAG, "NO DESCRIPTORS FOUND :" + descriptors.size());
        }
    }
}
