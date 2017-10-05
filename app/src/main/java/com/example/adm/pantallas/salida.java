package com.example.adm.pantallas;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.example.adm.pantallas.R.raw.prueba;

public class salida extends AppCompatActivity {
    MediaPlayer reproductor;
    MediaPlayer bgmusic;
    int maxVolume=100;
    int volume = 0;
    int cont = 1;
    int runTime = 30;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        Context ctx = getBaseContext();
        InputStream is = ctx.getResources().openRawResource(prueba);
        //String filename2 = "android.resource://" + getBaseContext().getPackageName() + "/raw/prueba.txt";
        Scanner sc = new Scanner(is);
        String linea = sc.next();
        Toast.makeText(getApplicationContext(), linea, Toast.LENGTH_LONG).show();
        sc.close();
        reproductor = MediaPlayer.create(this, R.raw.audio1);
        bgmusic = MediaPlayer.create(this, R.raw.bgmusic);
        float log1 = (float) (Math.log(maxVolume - volume) / Math.log(maxVolume));
        bgmusic.setVolume(1 - log1, 1 - log1);
        bgmusic.start();

        //reproductor.setLooping(true);


        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer reproductor) {

                Toast.makeText(getApplicationContext(),"Audio numero " + (cont+1), Toast.LENGTH_LONG).show();
                cont++;
                reproductor.stop();
                reproductor.reset();

                try {
                    String filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/audio" + cont;
                    reproductor.setDataSource(getBaseContext(), Uri.parse(filename));
                    reproductor.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "algo extraño", Toast.LENGTH_LONG).show();
                }

                reproductor.start();

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
