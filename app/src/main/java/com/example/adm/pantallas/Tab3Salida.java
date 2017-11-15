package com.example.adm.pantallas;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by adm on 09/06/2017.
 */

public class Tab3Salida extends Fragment {


    ConstraintLayout constraintLayout;
    Button btnSalida;
    Boolean Clickeado;
    EditText etDistancia, etTiempo, etVelocidad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_tab3salida, container, false);

        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.LayoutOpciones);
        btnSalida = (Button) rootView.findViewById(R.id.btnSalida);
        etDistancia= (EditText) rootView.findViewById(R.id.etDistancia);
        etTiempo= (EditText) rootView.findViewById(R.id.etTiempo);
        etVelocidad= (EditText) rootView.findViewById(R.id.etVelocidad);
        return rootView;

    }
    

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            int dis = Integer.parseInt(etDistancia.toString());
            int vel = Integer.parseInt(etVelocidad.toString());
            int temp = Integer.parseInt(etTiempo.toString());


            /*if(Clickeado) {
                constraintLayout.setVisibility(view.GONE);
                btnSalida.setVisibility(view.VISIBLE);
                Clickeado=false;
            }
            else {
                constraintLayout.setVisibility(view.VISIBLE);
                btnSalida.setVisibility(view.GONE);
                Clickeado=true;
            }*/

        }
    };

}