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

public class ChooseRadiusFragment extends DialogFragment {

    private GeoMap map;

    private EditText radiusEditText;
    private Spinner unitsDropDownMenu;

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

                if (radiusString == "") {

                } else {
                    double radius = Double.parseDouble(radiusString);
                    if (((String) unitsDropDownMenu.getSelectedItem()).equals("km")) {
                        map.getGeoLocation().setRadius(1000 * radius);
                    } else {
                        map.getGeoLocation().setRadius(radius);
                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}