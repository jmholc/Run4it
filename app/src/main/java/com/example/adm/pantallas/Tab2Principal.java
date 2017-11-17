package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        TextView historias=(TextView)rootView.findViewById(R.id.txHistorias);
        historias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getContext(),Historias.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        TextView amigos=(TextView)rootView.findViewById(R.id.txAmigos);
        amigos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getContext(),Amigos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        return rootView;


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txHistorias:


            case R.id.txAmigos:
                Intent intent2=new Intent(getContext(),Amigos.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
        }
    }




}
