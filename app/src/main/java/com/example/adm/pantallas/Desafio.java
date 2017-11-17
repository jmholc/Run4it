package com.example.adm.pantallas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Desafio extends AppCompatActivity {

    String json_string;
    Spinner spinner;
    TextView tvUnidad, tvDistTime;
    String PorQue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);
        SharedPreferences sharedPreferences =  getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String id= sharedPreferences.getString("id","");

        Bundle extras = getIntent().getExtras();
        String usuarioadesafiar = extras.getString("usuario");

        tvUnidad = (TextView) findViewById(R.id.txtUnidad);
        tvDistTime = (TextView) findViewById(R.id.txtDistTime);

        spinner = (Spinner) findViewById(R.id.spModo);
        ArrayAdapter<String> adapter;
        final List<String> list;

        list = new ArrayList<String>();
        list.add("Tiempo");
        list.add("Distancia");
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position){
                    case 0:
                        tvUnidad.setText("Minutos");
                        tvDistTime.setText("Tiempo");
                        PorQue = "tiempo";
                        break;
                    case 1:
                        tvUnidad.setText("KM");
                        tvDistTime.setText("Distancia");
                        PorQue = "distancia";
                        break;
                }
                Toast.makeText(getApplicationContext(), PorQue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}