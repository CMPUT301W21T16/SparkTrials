package com.example.sparktrials.main_ui.publish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sparktrials.R;
import com.example.sparktrials.models.GeoMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * This represents the Fragment (to be displayed in MapsActivity) that allows the user to set the
 * radius of their region.
 */
public class ChooseRadiusFragment extends DialogFragment {

    private GeoMap map;

    private EditText radiusEditText;
    private Spinner unitsDropDownMenu;

    /**
     * Constructor for ChooseRadiusFragment.
     * @param map
     *      The map that the region was selected on.
     */
    public ChooseRadiusFragment(GeoMap map) {
        this.map = map;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choose_radius, null);

        radiusEditText = view.findViewById(R.id.radius_edit_text);
        unitsDropDownMenu = view.findViewById(R.id.unit_drop_down_menu);

        String[] units = {"m", "km"};
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, units);
        unitsDropDownMenu.setAdapter(unitsAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setPositiveButton("Confirm Radius", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String radiusString = radiusEditText.getText().toString().trim();

                if (radiusString.equals("")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("ERROR")
                            .setMessage("Radius cannot be empty!")
                            .setPositiveButton("OK",null)
                            .show();
                } else {
                    double radius = Double.parseDouble(radiusString);
                    if (unitsDropDownMenu.getSelectedItem().equals("km")) {
                        // If the radius is set in kilometers, convert it to meters
                        map.getGeoLocation().setRadius(1000 * radius);
                    } else {
                        map.getGeoLocation().setRadius(radius);
                    }
                    LatLng center = new LatLng(map.getGeoLocation().getLat(), map.getGeoLocation().getLon());
                    map.displayCircle(center, map.getGeoLocation().getRadius());
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}