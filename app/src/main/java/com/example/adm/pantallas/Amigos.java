package com.example.adm.pantallas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Amigos extends AppCompatActivity {

    String json_string;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        SharedPreferences sharedPreferences =  getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String usuario= sharedPreferences.getString("usuario","");

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        new BackgroundTaskJSONAmigos(usuario).execute();
    }
    class BackgroundTaskJSONAmigos extends AsyncTask {
        String usuario;

        public BackgroundTaskJSONAmigos(String n) {
            usuario= n;
        }

        String json_url, JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url = "https://run4it.proyectosort.edu.ar/run4it/searchfriends.php";

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


            String IDUsuario, username, nombreapellido, estado;


            /*TextView textView=(TextView)findViewById(R.id.textView6);
            textView.setText((String)o);*/
            json_string = (String) o;

            //-----------------------------------------------------------------
            //ACA TERMINARIA LO DE PEDIR EL JSON.
            //ABAJO EMPIEZA LO DE PARSEARLO
            //-----------------------------------------------------------------
            JSONObject jsonObject;
            JSONArray jsonArray;



            final AdaptadorUsuarios2 adaptadorUsuarios2 = new AdaptadorUsuarios2(getApplicationContext(), R.layout.activity_amigos_listview);

            final ListView listView;
            listView = (ListView) findViewById(R.id.lvAmigos);
            listView.setAdapter(adaptadorUsuarios2);

            try {
                TextView txtnoseencontro= (TextView) findViewById(R.id.txtNoSeEncontro);

                jsonObject = new JSONObject(json_string);

                jsonArray = jsonObject.getJSONArray("server_response");//El nombre con el que tenemos guardado el JSON en el PHP
                Log.d("JSON", jsonArray.length() + "");
                final String[] nombresAutocomplete=new String[jsonArray.length()];
                if (jsonArray.length()<1)
                {
                    txtnoseencontro.setText("Todavía no tienes amigos. Ve a la pantalla de Añadir Amigos.");
                    txtnoseencontro.setVisibility(View.VISIBLE);
                }
                else {
                    for (int count = 0; count < jsonArray.length(); count++) {
                        txtnoseencontro.setVisibility(View.GONE);

                        JSONObject JO = jsonArray.getJSONObject(count);
                        username = JO.getString("Username");
                        nombreapellido = JO.getString("Nombre") + " " + JO.getString("Apellido");
                        estado = JO.getString("Mensaje");
                        IDUsuario = JO.getString("IDUsuario");
                        if (JO.getString("Mensaje").equals("null"))
                            estado = "";

                        nombresAutocomplete[count]=username;// hola granch
                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,nombresAutocomplete);
                        autoCompleteTextView.setAdapter(stringArrayAdapter);

                        UsuariosBuscados usuariosBuscados = new UsuariosBuscados(username, nombreapellido, estado, IDUsuario);
                        adaptadorUsuarios2.add(usuariosBuscados);
                    }
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //TextView tv = (TextView) findViewById(R.id.txtUsername);

                        Toast.makeText(getApplicationContext(), nombresAutocomplete[position], Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);

        }
    }
    public void listviewClickeado(View v){

    }
}
class AdaptadorUsuarios2 extends ArrayAdapter {
    List list = new ArrayList();
    Context ctx;

    public AdaptadorUsuarios2(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        ctx=context;
    }

    public void add(UsuariosBuscados object) {
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
            row = layoutInflater.inflate(R.layout.activity_amigos_listview, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.txUsername= (TextView) row.findViewById(R.id.txtUsername);
            contactHolder.txNombreApellido=(TextView) row.findViewById(R.id.txtNombreApellido);
            contactHolder.txEstado=(TextView) row.findViewById(R.id.txtEstado);

            row.setTag(contactHolder);
        }
        else {
            contactHolder = (ContactHolder) row.getTag();
        }

        final UsuariosBuscados usuariosBuscados= (UsuariosBuscados) this.getItem(position);
        contactHolder.txUsername.setText(usuariosBuscados.getUsername());
        contactHolder.txNombreApellido.setText(usuariosBuscados.getNombreapellido());
        contactHolder.txEstado.setText(usuariosBuscados.getEstado());
        contactHolder.idusuario=usuariosBuscados.getIdusuario();

        return row;
    }

    static class ContactHolder{
        TextView txUsername, txNombreApellido, txEstado;
        String idusuario;
    }
}



