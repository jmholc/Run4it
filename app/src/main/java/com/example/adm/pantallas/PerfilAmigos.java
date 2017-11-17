package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
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

public class PerfilAmigos extends AppCompatActivity {

    String json_string;
    TextView txtUsername, txtNombre, txtApellido, txtMessage;
    String IDUsuario, username, nombre, apellido, estado;
    Button btnDesafiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigos);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtApellido = (TextView) findViewById(R.id.txtApellido);
        txtMessage = (TextView) findViewById(R.id.txtMensaje);
        btnDesafiar= (Button) findViewById(R.id.btnComenzar);

        new BackgroundTaskJSONUser(id).execute();

    }
    public void aDesafio(View v){
        Intent intent=new Intent(PerfilAmigos.this,Desafio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle mBundle = new Bundle();
        mBundle.putString("usuario", username);
        Log.i("ASDASD", username);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
    class BackgroundTaskJSONUser extends AsyncTask {
        String usuario;

        public BackgroundTaskJSONUser(String n) {
            usuario= n;
        }

        String json_url, JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url = "https://run4it.proyectosort.edu.ar/run4it/fetchuser.php";
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
            json_string = (String) o;

            //-----------------------------------------------------------------
            //ACA TERMINARIA LO DE PEDIR EL JSON.
            //ABAJO EMPIEZA LO DE PARSEARLO
            //-----------------------------------------------------------------
            JSONObject jsonObject;
            JSONArray jsonArray;

            try {
                jsonObject = new JSONObject(json_string);

                jsonArray = jsonObject.getJSONArray("server_response");//El nombre con el que tenemos guardado el JSON en el PHP
                Log.d("JSON", jsonArray.length() + "");
                final String[] nombresAutocomplete=new String[jsonArray.length()];

                JSONObject JO = jsonArray.getJSONObject(0);
                username = JO.getString("Username");
                nombre = JO.getString("Nombre");
                apellido = JO.getString("Apellido");
                estado = JO.getString("Mensaje");
                if (JO.getString("Mensaje").equals("null"))
                    estado = "";

                Log.d("Coso", username);
                Log.d("Coso", estado);
                Log.d("Coso", nombre);
                Log.d("Coso", apellido);



                txtUsername.setText(txtUsername.getText()+" "+username);
                txtNombre.setText(txtNombre.getText()+" "+nombre);
                txtApellido.setText(txtApellido.getText()+" "+apellido);
                txtMessage.setText(/*txtMessage.getText()+" "+*/estado);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);

        }
    }
}
