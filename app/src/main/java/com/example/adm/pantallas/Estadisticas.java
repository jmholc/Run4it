package com.example.adm.pantallas;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Estadisticas extends AppCompatActivity {


    String json_string;
    ListView lvstats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        lvstats= (ListView) findViewById(R.id.lvstats);

        new BackgroundTaskJSONUser(id).execute();

    }
    class BackgroundTaskJSONUser extends AsyncTask {
        String usuario;
        String json_url, JSON_STRING;

        public BackgroundTaskJSONUser(String n) {
            usuario= n;
        }

        @Override
        protected void onPreExecute() {
            json_url = "https://run4it.proyectosort.edu.ar/run4it/userstats.php";
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {

                URL url = new URL(json_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);

                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data =
                        URLEncoder.encode("usuarioabuscar","UTF-8")   +"="+URLEncoder.encode(usuario,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();

                Log.d("Coso", "doInBackground: ");

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {




            /*TextView textView=(TextView)findViewById(R.id.textView6);
            textView.setText((String)o);*/

            //-----------------------------------------------------------------
            //ACA TERMINARIA LO DE PEDIR EL JSON.
            //ABAJO EMPIEZA LO DE PARSEARLO
            //-----------------------------------------------------------------
            String cantidadpasos, velPromedio, elevacion, totaldistancia, velmax, distmax, calorias, duracion;
            json_string = (String) o;

            JSONObject jsonObject;
            JSONArray jsonArray;

            try {
                jsonObject = new JSONObject(json_string);

                jsonArray = jsonObject.getJSONArray("server_response");//El nombre con el que tenemos guardado el JSON en el PHP
                Log.d("JSON", jsonArray.length() + "");
                final String[] nombresAutocomplete=new String[jsonArray.length()];

                JSONObject JO = jsonArray.getJSONObject(0);

                cantidadpasos = JO.getString("CantidadPasos ");
                velPromedio = JO.getString("VelPromedio");
                elevacion = JO.getString("Elevacion");
                totaldistancia = JO.getString("TotalDistancia");
                velmax = JO.getString("VelMax");
                distmax = JO.getString("DistMax");
                calorias = JO.getString("Calorias");
                duracion = JO.getString("Duracion");

                
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);

        }
    }
}