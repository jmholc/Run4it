package com.example.adm.pantallas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DesafioPendiente extends AppCompatActivity {

    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio_pendiente);

        SharedPreferences sharedPreferences =  getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String id= sharedPreferences.getString("id","");

        new BackgroundTaskJSONDesafioPendiente(id).execute();
    }

        class BackgroundTaskJSONDesafioPendiente extends AsyncTask {
            String usuario;

            public BackgroundTaskJSONDesafioPendiente(String n) {
                usuario= n;
            }

            String json_url, JSON_STRING;

            @Override
            protected void onPreExecute() {
                json_url = "https://run4it.proyectosort.edu.ar/run4it/challengepending.php";
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
                            URLEncoder.encode("usuario","UTF-8")   +"="+URLEncoder.encode(usuario,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpsURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                /*URL url = new URL(json_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));*/

                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_STRING + "\n");

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpsURLConnection.disconnect();

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

                String IDUsuario, username, objetivodesafio, cantidad;
            /*TextView textView=(TextView)findViewById(R.id.textView6);
            textView.setText((String)o);*/
                json_string = (String) o;
                //-----------------------------------------------------------------
                //ACA TERMINARIA LO DE PEDIR EL JSON.
                //ABAJO EMPIEZA LO DE PARSEARLO
                //-----------------------------------------------------------------
                JSONObject jsonObject;
                JSONArray jsonArray;

                final AdaptadorDesafios adaptadorDesafios = new AdaptadorDesafios(getApplicationContext(), R.layout.activity_desafio_pendiente);

                final ListView listView;
                listView = (ListView) findViewById(R.id.lvDesafioPendiente);
                listView.setAdapter(adaptadorDesafios);

                try {
                    TextView txtnoseencontro= (TextView) findViewById(R.id.txtNoSeEncontro);


                    jsonObject = new JSONObject(json_string);

                    jsonArray = jsonObject.getJSONArray("server_response");//El nombre con el que tenemos guardado el JSON en el PHP
                    Log.d("JSON", jsonArray.length() + "");
                    if (jsonArray.length()<1)
                    {
                        txtnoseencontro.setText("No tienes solicitudes pendientes.");
                        txtnoseencontro.setVisibility(View.VISIBLE);
                    }
                    else {
                        for (int count = 0; count < jsonArray.length(); count++) {
                            txtnoseencontro.setVisibility(View.GONE);

                            JSONObject JO = jsonArray.getJSONObject(count);
                            IDUsuario = JO.getString("IDUsuario");
                            username = JO.getString("Username");
                            objetivodesafio = JO.getString("ObjetivoDesafio");
                            cantidad = JO.getString("Cantidad");
                            if(objetivodesafio.equals("1"))
                            {
                                cantidad+= " Minutos";
                                objetivodesafio="Por Tiempo";
                            }
                            else {
                                cantidad += " KM";
                                objetivodesafio = "Por Distancia";
                            }
                            Log.d("JSON", IDUsuario);
                            Log.d("JSON", username);
                            Log.d("JSON", objetivodesafio);
                            Log.d("JSON", cantidad);

                            DesafioPendienteManager desafioPendienteManager= new DesafioPendienteManager(IDUsuario, username, objetivodesafio, cantidad);
                            adaptadorDesafios.add(desafioPendienteManager);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(o);

            }
        }
    }
    class AdaptadorDesafios extends ArrayAdapter {
        List list = new ArrayList();
        Context ctx;

        public AdaptadorDesafios(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            ctx=context;
        }

        public void add(DesafioPendienteManager object) {
            super.add(object);
            list.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row;
            row=convertView;
            final ContactHolder contactHolder;
            if(row==null)
            {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.desafio_pendiente_listview_item, parent, false);
                contactHolder = new ContactHolder();
                contactHolder.txUsername= (TextView) row.findViewById(R.id.txUsername);
                contactHolder.txTipo=(TextView) row.findViewById(R.id.txTipo);
                contactHolder.txCantidad=(TextView) row.findViewById(R.id.txCantidad);

                row.setTag(contactHolder);
            }
            else {
                contactHolder = (com.example.adm.pantallas.AdaptadorDesafios.ContactHolder) row.getTag();
            }

            final DesafioPendienteManager d= (DesafioPendienteManager) this.getItem(position);
            contactHolder.txUsername.setText(d.getUsername());
            contactHolder.txTipo.setText(d.getObjetivodesafio());
            contactHolder.txCantidad.setText(d.getCantidad());
            contactHolder.idusuario=d.getIdUsuario();

            return row;
        }

        static class ContactHolder{
            TextView txUsername, txTipo, txCantidad;
            String idusuario;
        }
    }