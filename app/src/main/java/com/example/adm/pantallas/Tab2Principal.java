package com.example.adm.pantallas;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by adm on 09/06/2017.
 */

public class Tab2Principal extends Fragment {
    TextView txtNombre;
    String Nombre;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_tab2principal, container, false);

        SharedPreferences sharedPreferences =  this.getActivity().getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String usuario= sharedPreferences.getString("usuario","");
        String contrasena=sharedPreferences.getString("contrasena","");

        txtNombre= (TextView) rootView.findViewById(R.id.txtNombre);
        txtNombre.setText(usuario);




        return rootView;
    }

}
