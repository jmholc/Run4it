package com.example.adm.pantallas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by juanholcman on 25/7/2017.
 */

public class BackgroundTask extends AsyncTask{
    Context ctx;
    AlertDialog alertDialog;

    private MyAlertDialog myAlertDialog;
    BackgroundTask(MyAlertDialog myAlertDialog){
        this.myAlertDialog=myAlertDialog;
    }

    BackgroundTask(Context ctx)
    {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Cargando...");
        alertDialog.show();
    }


    @Override
    protected Object doInBackground(Object[] params) {
        //String reg_url="https://run4it.proyectosort.edu.ar/run4it/register.php";
        //String login_url="https://run4it.proyectosort.edu.ar/run4it/login.php";
        String urldelphp = null;
        String data="";
        String respuesta="";

        String method = (String) params[0];
        if (method.equals("register1"))
        {
            String nombre= (String) params[1];
            String apellido=(String) params[2];
            String mail=(String) params[3];
            String usuario=(String) params[4];
            String contrasena=(String) params[5];
            urldelphp = "https://run4it.proyectosort.edu.ar/run4it/register.php";
            respuesta="";//"Registration successful,"+usuario+","+contrasena;

            try {
                data=
                                URLEncoder.encode("nombre","UTF-8")     +"="+URLEncoder.encode(nombre,"UTF-8")+"&"+
                                URLEncoder.encode("apellido","UTF-8")   +"="+URLEncoder.encode(apellido,"UTF-8")+"&"+
                                URLEncoder.encode("mail","UTF-8")       +"="+URLEncoder.encode(mail,"UTF-8")+"&"+
                                URLEncoder.encode("usuario","UTF-8")    +"="+URLEncoder.encode(usuario,"UTF-8")+"&"+
                                URLEncoder.encode("contrasena","UTF-8") +"="+URLEncoder.encode(contrasena,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if (method.equals("login"))
        {
            urldelphp = "https://run4it.proyectosort.edu.ar/run4it/login.php";
            String usuario=(String) params[1];
            String contrasena=(String) params[2];

            try {
                data=
                        URLEncoder.encode("usuario","UTF-8")    +"="+URLEncoder.encode(usuario,"UTF-8")+"&"+
                        URLEncoder.encode("contrasena","UTF-8") +"="+URLEncoder.encode(contrasena,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (method.equals("codigomail"))
        {
            String codigo=(String) params[1];
            String usuario= (String) params[2];
            urldelphp = "https://run4it.proyectosort.edu.ar/run4it/verify.php";
            try {
                data=
                        URLEncoder.encode("usuario","UTF-8")   +"="+URLEncoder.encode(usuario,"UTF-8")+"&"+
                        URLEncoder.encode("codigo","UTF-8")    +"="+URLEncoder.encode(codigo,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if (method.equals("enviarsolicitud")){
            String usuario=(String) params[1];
            String usuarioaenviar= (String) params[2];
            String tipo=(String) params[3];
            urldelphp = "https://run4it.proyectosort.edu.ar/run4it/friendrequest.php";
            try {
                data=
                        URLEncoder.encode("usuario","UTF-8")   +"="+URLEncoder.encode(usuario,"UTF-8")+"&"+
                                URLEncoder.encode("tipo","UTF-8")   +"="+URLEncoder.encode(usuario,"UTF-8")+"&"+
                                URLEncoder.encode("usuarioaenviar","UTF-8")    +"="+URLEncoder.encode(usuarioaenviar,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if (method.equals("register2")){

        }
        try {
            URL url=new URL(urldelphp);
            HttpsURLConnection httpsURLConnection=(HttpsURLConnection)url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);

            OutputStream OS=httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));


            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String line = "";
            while ((line = bufferedReader.readLine())!=null)
            {
                respuesta+= line;
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
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) { //CAMBIAR el POST
        alertDialog.dismiss();
        Log.i("ASDASD", (String) o);

        //Reg 1
        if (((String) o).contains("Registration successful"))//
        {
            String rta="Le hemos enviado un mail a su correo electrónico. Ingresa el código de verificación la próxima vez que inicies sesión en la app";
            Toast.makeText(ctx, rta,Toast.LENGTH_LONG).show();

            //Hacer el SharedPreferences
            String[] info = ((String)o).split(",");
            Log.i("ASDASD", (String) o);


            Intent intent=new Intent(ctx,VerificarMail.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("usuario", info[1]);
            mBundle.putString("contrasena", info[2]);
            //mBundle.putString("id", info[3]);

            Log.i("ASDASD", info[3]);
            intent.putExtras(mBundle);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }
        else if ((((String) o).contains("Username Taken")))
        {
            Toast.makeText(ctx, "El nombre de usuario ya existe",Toast.LENGTH_LONG).show();
        }

        //LOGIN                                                Y aca Verificar Mail
        else if ((((String) o).contains("Confirmation Missing")))
        {
            String[] info = ((String)o).split(",");
            Log.i("ASDASD", (String) o);


            Intent intent=new Intent(ctx,VerificarMail.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("usuario", info[1]);
            mBundle.putString("contrasena", info[2]);
            mBundle.putString("id", info[3]);
            Log.i("ASDASD", info[3]);

            intent.putExtras(mBundle);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();

            Toast.makeText(ctx, "Falta Confirmación",Toast.LENGTH_SHORT).show();
        }
        else if ((((String) o).contains("Login Failed")))
        {
            Toast.makeText(ctx, "El usuario no existe o la contraseña es incorrecta",Toast.LENGTH_SHORT).show();
        }

        else if ((((String) o).contains("Login Successful"))||(((String) o).contains("Codigo correcto")))
        {
            String[] info = ((String)o).split(",");

            SharedPreferences sharedPreferences =  ctx.getSharedPreferences("infoUsuario",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usuario",info[1]);
            editor.putString("contrasena",info[2]);
            editor.putString("id",info[3]);
            Log.i("ASDASD", info[3]);
            //editor.putString("nombre",info[3]);
            //editor.putString("apellido",info[4]);

            editor.apply();

            Intent intent=new Intent(ctx,Principal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();

            //Toast.makeText(ctx, (String) o,Toast.LENGTH_SHORT).show();
        }
        //CONFIRMACION MAIL
        else if ((((String) o).contains("Codigo incorrecto")))
        {
            Toast.makeText(ctx, "El código ingresado es incorrecto",Toast.LENGTH_SHORT).show();
        }

        else {
            Log.i("QWEQWE","Hizo esto...");
            Toast.makeText(ctx, (String) o,Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(o);

    }

}
