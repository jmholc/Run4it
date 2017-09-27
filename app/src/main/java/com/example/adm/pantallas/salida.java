package com.example.adm.pantallas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;

public class salida extends AppCompatActivity {
    MediaPlayer reproductor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        reproductor = MediaPlayer.create(this,R.raw.music);
        //reproductor.setLooping(true);


        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer reproductor) {

                Toast.makeText(getApplicationContext(),"Este audio ha terminado", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void SaludarOnClick(View v) {
        /*
        EditText Cajatexto = (EditText) findViewById(R.id.ET_Nombre);
        String nombre = Cajatexto.getText().toString();
        Toast.makeText(this,"Hola "+ nombre, Toast.LENGTH_LONG).show();*/
        reproductor.start();
        float duration = reproductor.getDuration();
        float getPos = reproductor.getCurrentPosition();




    }
}
