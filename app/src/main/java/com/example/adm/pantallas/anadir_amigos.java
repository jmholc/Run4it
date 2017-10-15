package com.example.adm.pantallas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class anadir_amigos extends AppCompatActivity {

    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_amigos);
    }

    public void buscarUsuario(View v) {

        String usuarioabuscar;
        EditText editText = (EditText) findViewById(R.id.etBuscarUsuarios);
        usuarioabuscar = editText.getText().toString();

        if (usuarioabuscar.length() > 0)
            new BackgroundTaskJSON(usuarioabuscar).execute();
    }

    class BackgroundTaskJSON extends AsyncTask {
        String usuarioabuscar;

        public BackgroundTaskJSON(String n) {
            usuarioabuscar = n;
        }

        String json_url, JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url = "https://run4it.proyectosort.edu.ar/run4it/searchuser.php";

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

                String data = URLEncoder.encode("usuarioabuscar", "UTF-8") + "=" + URLEncoder.encode(usuarioabuscar, "UTF-8");

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
            JSONObject jsonObject;
            JSONArray jsonArray;

            String username, nombreapellido, estado;

            AdaptadorUsuarios adaptadorUsuarios;
            ListView listView;

            /*TextView textView=(TextView)findViewById(R.id.textView6);
            textView.setText((String)o);*/
            json_string = (String) o;

            adaptadorUsuarios = new AdaptadorUsuarios(getApplicationContext(), R.layout.activity_anadir_amigos_listview);
            listView = (ListView) findViewById(R.id.lvUsuarios);
            listView.setAdapter(adaptadorUsuarios);
            try {
                jsonObject = new JSONObject(json_string);

                jsonArray = jsonObject.getJSONArray("server_response");//El nombre con el que tenemos guardado el JSON en el PHP

                for (int count = 0; count < jsonArray.length(); count++) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    username = JO.getString("Username");
                    nombreapellido = JO.getString("Nombre") + " " + JO.getString("Apellido");
                    estado = JO.getString("Estado");
                    UsuariosBuscados usuariosBuscados = new UsuariosBuscados(username, nombreapellido, estado);
                    adaptadorUsuarios.add(usuariosBuscados);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
class AdaptadorUsuarios extends ArrayAdapter {
    List list = new ArrayList();

    public AdaptadorUsuarios(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
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
        ContactHolder contactHolder;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_anadir_amigos_listview,parent,false);
            contactHolder = new ContactHolder();
            contactHolder.txUsername= (TextView) row.findViewById(R.id.txtUsername);
            contactHolder.txNombreApellido=(TextView) row.findViewById(R.id.txtNombreApellido);
            contactHolder.txEstado=(TextView) row.findViewById(R.id.txtEstado);
            row.setTag(contactHolder);
        }
        else {
            contactHolder = (ContactHolder) row.getTag();
        }

        UsuariosBuscados usuariosBuscados= (UsuariosBuscados) this.getItem(position);
        contactHolder.txUsername.setText(usuariosBuscados.getUsername());
        contactHolder.txNombreApellido.setText(usuariosBuscados.getNombreapellido());
        contactHolder.txEstado.setText(usuariosBuscados.getEstado());
        return row;
    }

    static class ContactHolder{
        TextView txUsername, txNombreApellido, txEstado;
    }
}

