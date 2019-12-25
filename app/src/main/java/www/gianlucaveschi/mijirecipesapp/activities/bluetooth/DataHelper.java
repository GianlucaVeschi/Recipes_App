package www.gianlucaveschi.mijirecipesapp.activities.bluetooth;

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
}
