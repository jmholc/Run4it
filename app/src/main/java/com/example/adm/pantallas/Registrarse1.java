package com.example.adm.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registrarse1 extends AppCompatActivity {

    EditText et_nombre,et_apellido,et_mail,et_usuario,et_contrasena,et_repetircontrasena;
    String nombre,apellido,mail,usuario,contrasena,repetircontrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        et_nombre=(EditText)findViewById(R.id.etNombre);
        et_apellido=(EditText)findViewById(R.id.etApellido);
        et_mail=(EditText)findViewById(R.id.etMail);
        et_usuario=(EditText)findViewById(R.id.etUsuario);
        et_contrasena=(EditText)findViewById(R.id.etContrasena);
        et_repetircontrasena=(EditText)findViewById(R.id.etRepetir);
    }

    public void Registrarse(View v)
    {
        nombre=et_nombre.getText().toString();
        nombre=nombre.trim();

        apellido=et_apellido.getText().toString();
        apellido=apellido.trim();

        mail=et_mail.getText().toString();
        mail=mail.trim();

        usuario=et_usuario.getText().toString();
        usuario=usuario.trim();

        contrasena=et_contrasena.getText().toString();
        contrasena=contrasena.trim();

        repetircontrasena=et_repetircontrasena.getText().toString();
        repetircontrasena=repetircontrasena.trim();

        if(nombre.equals("")||apellido.equals("")||mail.equals("")||usuario.equals("")||contrasena.equals("")||repetircontrasena.equals(""))
        {
            Log.i("COSA","LLEGUE");
            Toast.makeText(getApplicationContext(),"Alguno de los campos está vacío",Toast.LENGTH_SHORT).show();
        }
        else if(!isValidEmail(mail))
        {
            Log.i("COSA","LLEGUE mail");
            Toast.makeText(getApplicationContext(),"El formato del mail es incorrecto",Toast.LENGTH_SHORT).show();

        }
        else if (!contrasena.equals(repetircontrasena))
        {
            Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.i("COSA","se está enviando");
            Log.i("COSA",contrasena.toString());
            String method="register1";                                      //COMO VOY A IDENTIFICAR ESTE REGISTRO
            BackgroundTask backgroundTask= new BackgroundTask(this);
            String contrasenaHasheada = getHash(contrasena);
            Log.i("Contrasena Hasheada",contrasenaHasheada);
            backgroundTask.execute(method,nombre,apellido,mail,usuario,contrasenaHasheada);
        }

    }
    public void aRegistrarse2(View v){

        Intent intent=new Intent(Registrarse1.this,Registrarse2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void aLogin(View v){

        Intent intent=new Intent(Registrarse1.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public static boolean isValidEmail(String target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
}
