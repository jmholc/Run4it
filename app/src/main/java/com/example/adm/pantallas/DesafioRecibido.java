package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class DesafioRecibido extends AppCompatActivity {

    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio_recibido);
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
            json_url = "https://run4it.proyectosort.edu.ar/run4it/challengereceived.php";
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

            String IDUsuario, username, objetivodesafio, cantidad, IDDesafio;
            /*TextView textView=(TextView)findViewById(R.id.textView6);
            textView.setText((String)o);*/
            json_string = (String) o;
            //-----------------------------------------------------------------
            //ACA TERMINARIA LO DE PEDIR EL JSON.
            //ABAJO EMPIEZA LO DE PARSEARLO
            //-----------------------------------------------------------------
            JSONObject jsonObject;
            JSONArray jsonArray;

            final AdaptadorDesafiosRec adaptadorDesafios = new AdaptadorDesafiosRec(getApplicationContext(), R.layout.activity_desafio_recibido);

            final ListView listView;
            listView = (ListView) findViewById(R.id.lvDesafioRecibido);
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
                        IDDesafio=JO.getString("IDDesafio");
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

                        DesafioPendienteManager desafioPendienteManager= new DesafioPendienteManager(IDUsuario, username, objetivodesafio, cantidad, IDDesafio);
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
class AdaptadorDesafiosRec extends ArrayAdapter {
    List list = new ArrayList();
    Context ctx;

    public AdaptadorDesafiosRec(@NonNull Context context, @LayoutRes int resource) {
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
            row = layoutInflater.inflate(R.layout.desafio_recibido_listview_item, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.txUsername= (TextView) row.findViewById(R.id.txUsernameRec);
            contactHolder.txTipo=(TextView) row.findViewById(R.id.txTipoRec);
            contactHolder.txCantidad=(TextView) row.findViewById(R.id.txCantidadRec);
            contactHolder.aceptardesafio= (Button) row.findViewById(R.id.btnAceptarDesafio);

            row.setTag(contactHolder);
        }
        else {
            contactHolder = (ContactHolder) row.getTag();
        }

        final DesafioPendienteManager d= (DesafioPendienteManager) this.getItem(position);
        contactHolder.txUsername.setText(d.getUsername());
        contactHolder.txTipo.setText(d.getObjetivodesafio());
        contactHolder.txCantidad.setText(d.getCantidad());
        contactHolder.idusuario=d.getIdUsuario();
        contactHolder.iddesafio=d.getIddesafio();
        contactHolder.aceptardesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ctx, contactHolder.idusuario, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), MapsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("usuariodesafiado", d.getUsername());
                mBundle.putString("objetivodesafio", d.getIddesafio());
                mBundle.putString("cantidad", d.getCantidad());
                //mBundle.putString("id", info[3]);

                //Log.i("ASDASD", info[3]);
                intent.putExtras(mBundle);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);

                //ejecutarBackgroundTask("aceptar", usuariosBuscados.getIdusuario());
            }
        });
        //contactHolder.aceptardesafio;

        return row;
    }

    static class ContactHolder{
        TextView txUsername, txTipo, txCantidad;
        String idusuario, iddesafio;
        Button aceptardesafio;
    }
}