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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static com.example.adm.pantallas.R.raw.duracion;

public class salida extends AppCompatActivity {
    MediaPlayer reproductor;
    MediaPlayer bgmusic;
    int maxVolume=100;
    int volume = 50;
    int cont = 10;
    int runTime = 30;
    int durationSeg;
    HashMap<Integer,Integer> durationA = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> durationB = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> durationC = new HashMap<Integer, Integer>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        Context ctx = getBaseContext();
        InputStream is = ctx.getResources().openRawResource(duracion);
        //String filename2 = "android.resource://" + getBaseContext().getPackageName() + "/raw/prueba.txt";
        Scanner sc = new Scanner(is);
        String linea = sc.next();


        reproductor = MediaPlayer.create(this, R.raw.a_10);
        bgmusic = MediaPlayer.create(this, R.raw.bgmusic);
        float log1 = (float) (Math.log(maxVolume - volume) / Math.log(maxVolume));
        bgmusic.setVolume(1 - log1, 1 - log1);
        bgmusic.start();
        bgmusic.isLooping();

        //reproductor.setLooping(true);
        durationSeg = sc.nextInt();
        Toast.makeText(getApplicationContext(), durationSeg, Toast.LENGTH_LONG).show();
        String level;
        int a = 10,b = 10,c = 10;
        while (sc.hasNext()){
            level = sc.next();
            switch (level.charAt(0)){
                case 'a':
                    durationA.put(a,sc.nextInt());
                    a++;
                    break;
                case 'b':
                    durationB.put(b,sc.nextInt());
                    b++;
                    break;
                case 'c':
                    durationC.put(c,sc.nextInt());
                    c++;
                    break;
                default:
                    break;
            }
        }

        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer reproductor) {

                Toast.makeText(getApplicationContext(),"Audio numero " + (cont+1), Toast.LENGTH_LONG).show();
                cont++;
                reproductor.stop();
                reproductor.reset();

                try {
                    String filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/a_" + cont;
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
