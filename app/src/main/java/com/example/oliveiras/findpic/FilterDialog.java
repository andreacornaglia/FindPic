package com.example.oliveiras.findpic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.oliveiras.findpic.Activities.MainActivity;
import com.example.oliveiras.findpic.R;

/**
 * Created by oliveiras on 2/28/15.
 */
public class FilterDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(root);
        //relating each spinner with string array
        final Spinner spinnersize = (Spinner) root.findViewById(R.id.size);
        ArrayAdapter<CharSequence> adaptersize = ArrayAdapter.createFromResource(getActivity(),
                R.array.size, android.R.layout.simple_spinner_item);
        adaptersize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersize.setAdapter(adaptersize);

        final Spinner spinnercolor = (Spinner) root.findViewById(R.id.color);
        ArrayAdapter<CharSequence> adaptercolor = ArrayAdapter.createFromResource(getActivity(),
                R.array.color, android.R.layout.simple_spinner_item);
        adaptercolor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercolor.setAdapter(adaptercolor);

        final Spinner spinnertype = (Spinner) root.findViewById(R.id.type);
        ArrayAdapter<CharSequence> adaptertype = ArrayAdapter.createFromResource(getActivity(),
                R.array.type, android.R.layout.simple_spinner_item);
        adaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(adaptertype);

        builder.setMessage("Advanced Image Search Options")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int color = spinnercolor.getSelectedItemPosition();
                        int size = spinnersize.getSelectedItemPosition();
                        int type = spinnertype.getSelectedItemPosition();
                        ((MainActivity) getActivity()).onParametersChanged(color, size, type);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
