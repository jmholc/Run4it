package com.example.adm.pantallas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class VerificarMail extends AppCompatActivity {

    EditText et_codigo;
    String codigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_mail);
        et_codigo=(EditText)findViewById(R.id.et_Verificar);

    }
    public void checkCodigo(View v)
    {
        Bundle extras = getIntent().getExtras();
        String usuario="", contrasena="";

        if (extras != null) {
            usuario = extras.getString("usuario");
            contrasena = extras.getString("contrasena");
        }

        codigo=et_codigo.getText().toString();
        codigo=codigo.trim();
        if (codigo.length()!=6)
        {
            Toast.makeText(getApplicationContext(),"El codigo debe ser de 6 dígitos",Toast.LENGTH_SHORT).show();
        }
        else {
            String method="codigomail";
            BackgroundTask backgroundTask= new BackgroundTask(this);
            backgroundTask.execute(method,codigo, usuario, contrasena);
        }
    }
}
