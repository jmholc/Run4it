package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Amigos_solicitudRecibida extends AppCompatActivity {

    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos_solicitud_recibida);
        SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario", "");

        new BackgroundTaskJSONAmigos(usuario).execute();
    }

    class BackgroundTaskJSONAmigos extends AsyncTask {
        String usuario;
        String json_url, JSON_STRING;

        public BackgroundTaskJSONAmigos(String n) {
            usuario = n;
        }

        @Override
        protected void onPreExecute() {
            json_url = "https://run4it.proyectosort.edu.ar/run4it/searchreceived.php";
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
                        URLEncoder.encode("usuario", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8");

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


            final AdaptadorUsuarios4 adaptadorUsuarios4
                    = new AdaptadorUsuarios4(getApplicationContext(), R.layout.activity_amigos_solicitud_recibida_listview);

            final ListView listView;
            listView = (ListView) findViewById(R.id.lvUsuarios);
            listView.setAdapter(adaptadorUsuarios4);

            try {
                TextView txtnoseencontro = (TextView) findViewById(R.id.txtNoSeEncontro);


                jsonObject = new JSONObject(json_string);

                jsonArray = jsonObject.getJSONArray("server_response");//El nombre con el que tenemos guardado el JSON en el PHP

                final String[] idusers=new String[jsonArray.length()];

                Log.d("JSON", jsonArray.length() + "");
                if (jsonArray.length() < 1) {
                    txtnoseencontro.setText("No tienes solicitudes pendientes.");
                    txtnoseencontro.setVisibility(View.VISIBLE);
                } else {
                    for (int count = 0; count < jsonArray.length(); count++) {
                        txtnoseencontro.setVisibility(View.GONE);

                        JSONObject JO = jsonArray.getJSONObject(count);
                        username = JO.getString("Username");
                        nombreapellido = JO.getString("Nombre") + " " + JO.getString("Apellido");
                        estado = "";
                        IDUsuario = JO.getString("IDUsuario");

                        idusers[count]=IDUsuario;// hola granch


                        UsuariosBuscados usuariosBuscados = new UsuariosBuscados(username, nombreapellido, estado, IDUsuario);
                        adaptadorUsuarios4
                                .add(usuariosBuscados);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);
        }
    }
}

class AdaptadorUsuarios4 extends ArrayAdapter {
    List list = new ArrayList();
    Context ctx;

    public AdaptadorUsuarios4(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        ctx = context;
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
        row = convertView;
        final ContactHolder contactHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_amigos_solicitud_recibida_listview, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.txUsername = (TextView) row.findViewById(R.id.txtUsername);
            contactHolder.txNombreApellido = (TextView) row.findViewById(R.id.txtNombreApellido);
            contactHolder.btnAceptar = (Button) row.findViewById(R.id.btnAceptar);
            contactHolder.btnDeclinar = (Button) row.findViewById(R.id.btnDeclinar);

            row.setTag(contactHolder);
        } else {
            contactHolder = (ContactHolder) row.getTag();
        }

        final UsuariosBuscados usuariosBuscados = (UsuariosBuscados) this.getItem(position);
        contactHolder.txUsername.setText(usuariosBuscados.getUsername());
        contactHolder.txNombreApellido.setText(usuariosBuscados.getNombreapellido());
        contactHolder.idusuario = usuariosBuscados.getIdusuario();

        contactHolder.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, contactHolder.txNombreApellido.getText(), Toast.LENGTH_SHORT).show();
                ejecutarBackgroundTask("aceptar", usuariosBuscados.getIdusuario());
            }
        });
        contactHolder.btnDeclinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, contactHolder.txNombreApellido.getText(), Toast.LENGTH_SHORT).show();
                ejecutarBackgroundTask("declinar", usuariosBuscados.getIdusuario());
            }
        });

        return row;
    }

    public void ejecutarBackgroundTask(String tipo, String usuariosBuscados) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario", "");

        BackgroundTaskRespuesta backgroundTaskRespuesta = new BackgroundTaskRespuesta(ctx);
        backgroundTaskRespuesta.execute(tipo, usuario, usuariosBuscados);
    }

    static class ContactHolder {
        TextView txUsername, txNombreApellido;
        Button btnAceptar, btnDeclinar;
        String idusuario;
    }

    class BackgroundTaskRespuesta extends AsyncTask {
        Context ctx;

        BackgroundTaskRespuesta(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String urldelphp = null;
            String data = "";
            String respuesta = "";


            String tipo = (String) params[0];
            String usuario = (String) params[1];
            String usuarioaenviar = (String) params[2];
            Log.d("Lo que se esta mandando", tipo + " " + usuario+ " " +usuarioaenviar);
            urldelphp = "https://run4it.proyectosort.edu.ar/run4it/confirmrequest.php";
            try {
                data =
                        URLEncoder.encode("usuario", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8") + "&" +
                        URLEncoder.encode("tipo", "UTF-8") + "=" + URLEncoder.encode(tipo, "UTF-8") + "&" +
                        URLEncoder.encode("usuarioaenviar", "UTF-8") + "=" + URLEncoder.encode(usuarioaenviar, "UTF-8");
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
            Toast.makeText(ctx, (String) o, Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(ctx,Amigos_solicitudRecibida.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            ctx.startActivity(intent);
            super.onPostExecute(o);
        }
    }
}

