package com.example.adm.pantallas;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by adm on 09/06/2017.
 */

public class Tab3Salida extends Fragment{


    ConstraintLayout constraintLayout;
    Button btnSalida;
    Boolean Clickeado;
    EditText etDistancia, etTiempo, etVelocidad;
    NumberFormat formatter = new DecimalFormat("#0.0");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_tab3salida, container, false);
        Button button = (Button) rootView.findViewById(R.id.btnSalida);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")) {
                    if((Double.valueOf(etVelocidad.getText().toString())*Double.valueOf(etTiempo.getText().toString()))/60==Double.valueOf(etDistancia.getText().toString()))
                    {
                        float dis = Float.valueOf(etDistancia.getText().toString());
                        float vel = Float.valueOf(etVelocidad.getText().toString());
                        float temp = Float.valueOf(etTiempo.getText().toString());
                        Global Dis = new Global();
                        Global Vel = new Global();
                        Global Temp = new Global();
                        Dis.setDis(dis);
                        Vel.setDis(vel);
                        Temp.setDis(temp);
                        Intent k = new Intent(getContext(), salida.class);
                        startActivity(k);
                    }
                }

            }
        });
        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.LayoutOpciones);
        btnSalida = (Button) rootView.findViewById(R.id.btnSalida);
        etDistancia= (EditText) rootView.findViewById(R.id.etDistancia);
        etTiempo= (EditText) rootView.findViewById(R.id.etTiempo);
        etVelocidad= (EditText) rootView.findViewById(R.id.etVelocidad);
        etTiempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etDistancia.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                    etVelocidad.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString())/Double.valueOf(etTiempo.getText().toString()))*60));
                }else if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                    etDistancia.setText(""+formatter.format((Double.valueOf(etVelocidad.getText().toString())*Double.valueOf(etTiempo.getText().toString()))/60));
                }else if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")){
                    etVelocidad.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString())/Double.valueOf(etTiempo.getText().toString()))*60));
                }
                //if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                //    etDistancia.setText(""+(Integer.parseInt(etDistancia.getText().toString())/Integer.parseInt(etTiempo.getText().toString())));
              //  }
            }
        });
        etTiempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                //if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                //    etDistancia.setText(""+(Integer.parseInt(etDistancia.getText().toString())/Integer.parseInt(etTiempo.getText().toString())));
                //  }
            }
        });
        etTiempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!etDistancia.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                        etVelocidad.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString())/Double.valueOf(etTiempo.getText().toString()))*60));
                    }else if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                        etDistancia.setText(""+formatter.format((Double.valueOf(etVelocidad.getText().toString())*Double.valueOf(etTiempo.getText().toString()))/60));
                    }else if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")){
                        etVelocidad.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString())/Double.valueOf(etTiempo.getText().toString()))*60));
                    }
                }
            }
        });
        etDistancia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!etDistancia.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                        etVelocidad.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString())/Double.valueOf(etTiempo.getText().toString()))*60));
                    }else if (!etVelocidad.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")) {
                        etTiempo.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString()))*60/(Double.valueOf(etVelocidad.getText().toString()))));
                    }else if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")){
                        etVelocidad.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString())/Double.valueOf(etTiempo.getText().toString()))*60));
                    }
                }
            }
        });
        etVelocidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")) {
                        etDistancia.setText(""+formatter.format((Double.valueOf(etVelocidad.getText().toString())*Double.valueOf(etTiempo.getText().toString()))/60));
                    }else if (!etVelocidad.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")) {
                        etTiempo.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString()))*60/(Double.valueOf(etVelocidad.getText().toString()))));
                    }else if (!etVelocidad.getText().toString().equals("")&&!etTiempo.getText().toString().equals("")&&!etDistancia.getText().toString().equals("")){
                        etTiempo.setText(""+formatter.format((Double.valueOf(etDistancia.getText().toString()))*60/(Double.valueOf(etVelocidad.getText().toString()))));
                    }
                }
            }
        });

        return rootView;

    }

    public void chau (View view){


    }

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


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