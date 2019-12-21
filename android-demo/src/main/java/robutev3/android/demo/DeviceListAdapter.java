package robutev3.android.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Vector;

import robutev3.android.Device;

import static robutev3.android.BluetoothCommunicationInterface.BLUETOOTH_OUI_LEGO;

/**
 * @author Nearchos
 * Created: 11-Aug-18
 */
public class DeviceListAdapter extends ArrayAdapter<Device> {

    DeviceListAdapter(@NonNull Context context, @NonNull final Vector<Device> devices) {
        super(context, android.R.layout.simple_list_item_1, devices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        final Device device = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        // Lookup view for data population
        final ImageView iconImageView = convertView.findViewById(R.id.device_list_item_icon);
        final TextView nameTextView = convertView.findViewById(R.id.device_list_item_name);
        final TextView macAddressTextView = convertView.findViewById(R.id.device_list_item_address);

        // Populate the data into the template view using the data object
        assert device != null;
        iconImageView.setImageResource(getDrawableResourceId(device));
        nameTextView.setText(device.getName());
        final String address = device.getAddress();
        macAddressTextView.setText(address);
        if(device.getType() == Device.Type.TYPE_BLUETOOTH) {
            // if mac address is a LEGO one, show with accent color
            macAddressTextView.setTextColor(address.startsWith(BLUETOOTH_OUI_LEGO) ?
                    getContext().getResources().getColor(R.color.colorAccent) :
                    getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            macAddressTextView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    private int getDrawableResourceId(final Device device) {
        switch (device.getType()) {
            case TYPE_BLUETOOTH:
                return R.drawable.bluetooth;
            case TYPE_USB:
                return R.drawable.usb;
            default:
                return R.drawable.unknown;
        }
    }
}