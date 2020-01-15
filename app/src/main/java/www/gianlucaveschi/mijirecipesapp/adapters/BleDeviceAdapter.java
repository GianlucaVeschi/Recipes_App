package www.gianlucaveschi.mijirecipesapp.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gianlucaveschi.load_json_images_picasso.R;
import java.util.ArrayList;


public class BleDeviceAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> ble_devices;

    public BleDeviceAdapter() {
        super();
        ble_devices = new ArrayList<>();
    }

    static class ViewHolder {
        private TextView deviceName;
        private TextView deviceAddress;
    }

    public void addDevice(BluetoothDevice device) {
        if (!ble_devices.contains(device)) {
            ble_devices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return ble_devices.get(position);
    }
    public void clear() {
        ble_devices.clear();
    }

    @Override
    public int getCount() {
        return ble_devices.size();
    }

    @Override
    public Object getItem(int position) {
        return ble_devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = view.findViewById(R.id.device_name);
            viewHolder.deviceAddress = view.findViewById(R.id.device_mac);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BluetoothDevice device = ble_devices.get(position);
        String deviceName = device.getName();

        //device may or may not include its name in Bluetooth advertising packets
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText("unknown device");
        }
        viewHolder.deviceAddress.setText(device.getAddress());
        return view;
    }
}
