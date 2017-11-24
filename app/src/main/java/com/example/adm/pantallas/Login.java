package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    EditText et_usuario, et_contrasena;
    String usuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_contrasena = (EditText) findViewById(R.id.etContrasena);
        et_usuario = (EditText) findViewById(R.id.etUsuario);
        iniciarSesionSharedPref();
    }

    public void iniciarSesion(View v) {
        usuario=et_usuario.getText().toString();
        contrasena=et_contrasena.getText().toString();

        String method="login";
        BackgroundTask backgroundTask=new BackgroundTask(this);
        backgroundTask.execute(method,usuario,getHash(contrasena  ));
    }

    public void iniciarSesionSharedPref()
    {
        SharedPreferences sharedPreferences =  getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        String usuario= sharedPreferences.getString("usuario","");
        String contrasena=sharedPreferences.getString("contrasena","");
        if (!(usuario.isEmpty())&&!(contrasena.isEmpty()))
        {//Q haga el Login Con el SHaredPreferences
            Log.i("DSADSA", "LLego aca");
            Log.i("DSADSA",usuario);
            Log.i("DSADSA",contrasena);

            String method="login";
            BackgroundTask backgroundTask=new BackgroundTask(this);
            backgroundTask.execute(method,usuario,contrasena);
        }
    }

    static String getHash(String text){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] array=md.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<array.length;i++)
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            return sb.toString();

        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;

    }


    public void aMenuPrincipal(){

        Intent intent=new Intent(Login.this,Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void aRegistrarse(View v){

        Intent intent=new Intent(Login.this,Registrarse1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
