package com.example.adm.pantallas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Desafio extends AppCompatActivity {

    String json_string;
    Spinner spinner;
    TextView tvUnidad, tvDistTime;
    String PorQue;
    String id;
    String usuarioadesafiar;
    String cantidad;
    EditText etUnidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);
        SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");

        Bundle extras = getIntent().getExtras();
        usuarioadesafiar = extras.getString("usuario");

        tvUnidad = (TextView) findViewById(R.id.txtUnidad);
        tvDistTime = (TextView) findViewById(R.id.txtDistTime);
        etUnidad = (EditText) findViewById(R.id.etCantidad);

        cantidad = etUnidad.getText().toString();

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
                switch (position) {
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

    public void onClickComenzar(View v) {
        cantidad = etUnidad.getText().toString();
        if (cantidad.length() > 0) {
            String codePorQue;
            if(PorQue.equals("tiempo"))
                codePorQue="0";
            else
                codePorQue="1";
            new BackgroundTaskDesafio().execute(id,usuarioadesafiar,codePorQue,cantidad);
        }
    }

    public class BackgroundTaskDesafio extends AsyncTask {
        String usuarioretador, usuariodesafiado, objetivodesafio, cantidad;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String urldelphp = null;
            String data = "";
            String respuesta = "";

            usuarioretador = (String) params[0];
            usuariodesafiado = (String) params[1];
            objetivodesafio = (String) params[2];
            cantidad = (String) params[3];
            urldelphp = "https://run4it.proyectosort.edu.ar/run4it/challengenew.php";
            respuesta = "";//"Registration successful,"+usuario+","+contrasena;

            try {
                data =
                        URLEncoder.encode("usuarioretador", "UTF-8") + "=" + URLEncoder.encode(usuarioretador, "UTF-8") + "&" +
                                URLEncoder.encode("usuariodesafiado", "UTF-8") + "=" + URLEncoder.encode(usuariodesafiado, "UTF-8") + "&" +
                                URLEncoder.encode("objetivodesafio", "UTF-8") + "=" + URLEncoder.encode(objetivodesafio, "UTF-8") + "&" +
                                URLEncoder.encode("cantidad", "UTF-8") + "=" + URLEncoder.encode(cantidad, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                URL url = new URL(urldelphp);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);

                OutputStream OS = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    respuesta += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();

                return respuesta;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o.equals("Success")) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("usuariodesafiado", usuariodesafiado);
                mBundle.putString("objetivodesafio", objetivodesafio);
                mBundle.putString("cantidad", cantidad);
                //mBundle.putString("id", info[3]);

                //Log.i("ASDASD", info[3]);
                intent.putExtras(mBundle);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            super.onPostExecute(o);
        }
    }
}