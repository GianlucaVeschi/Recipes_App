package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

public class BleConstants {
    public static final String BT_TAG = "BLUETOOTH_TAG";
    public static final String FIND = "Find MIJI Device";
    public static final String STOP_SCANNING = "Stop Scanning";
    public static final String SCANNING = "Scanning";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ID = "id";

    // service characteristics
    public static String ALERT_LEVEL_CHARACTERISTIC = "00002A06-0000-1000-8000-00805F9B34FB";
    public static String CLIENT_PROXIMITY_CHARACTERISTIC = "3E099911-293F-11E4-93BD-AFD0FE6D1DFD";
    public static String TEMPERATURE_MEASUREMENT_CHARACTERISTIC = "00002A1C-0000-1000-8000-00805F9B34FB";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    //MIJI Services
    public static final String miji_GENERIC_ACCESS     = "00001800-0000-1000-8000-00805F9B34FB";
    public static final String miji_GENERIC_ATTRIBUTE  = "00001801-0000-1000-8000-00805F9B34FB";
    public static final String miji_DEVICE_INFORMATION = "0000180A-0000-1000-8000-00805F9B34FB";
    public static final String miji_TEMPERATURE_SERVICE   = "0000FFE5-0000-1000-8000-00805F9B34FB";

    // TODO: 05/11/2019 : Understand what these characteristics represent
    //MIJI services characteristics
    public static final String miji_TEMPERATURE_CHARACTERISTIC = "0000ffe9-0000-1000-8000-00805f9b34fb";
    public static final String miji_TEMPERATURE_SERVICE_CHAR_2 = "0000ffe0-0000-1000-8000-00805f9b34fb";

    public static final String miji_DEVICE_NAME = "Miji_Bluetooth01";

}
