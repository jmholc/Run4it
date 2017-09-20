package com.example.adm.pantallas;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by adm on 16/08/2017.
 */

public class MyAlertDialog extends DialogFragment {
    LayoutInflater inflater;
    View v;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.my_dialog, null);
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
