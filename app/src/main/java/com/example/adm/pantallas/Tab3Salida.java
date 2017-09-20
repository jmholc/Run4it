package com.example.adm.pantallas;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

/**
 * Created by adm on 09/06/2017.
 */

public class Tab3Salida extends Fragment {

    ConstraintLayout constraintLayout;
    Button btnSalida;
    Boolean Clickeado;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_tab3salida, container, false);

        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.LayoutOpciones);
        btnSalida = (Button) rootView.findViewById(R.id.btnSalida);
        return rootView;

    }
    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(Clickeado) {
                constraintLayout.setVisibility(view.GONE);
                btnSalida.setVisibility(view.VISIBLE);
                Clickeado=false;
            }
            else {
                constraintLayout.setVisibility(view.VISIBLE);
                btnSalida.setVisibility(view.GONE);
                Clickeado=true;
            }

        }
    };

}