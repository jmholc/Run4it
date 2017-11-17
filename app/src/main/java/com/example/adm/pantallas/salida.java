package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import android.nfc.Tag;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import static com.example.adm.pantallas.R.raw.duracion;

public class salida extends AppCompatActivity {
    MediaPlayer reproductor;
    MediaPlayer bgmusic;
    int maxVolume=100;
    int nextAudio = 10, alertType = 10;
    int nextAudioB = 10;
    int runTime = 200;
    int vol=0;
    int fix = 0, fixb = 0;
    String filename, op = "";
    int bTot, spareTime, a;
    boolean alert=false;
    String nextAudioLevel="a_";
    int durationSeg;
    private static final int FILE_SELECT_CODE = 0;
    HashMap<Integer,Integer> durationA = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> durationB = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> durationC = new HashMap<Integer, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        Context ctx = getBaseContext();
        reproductor = MediaPlayer.create(this, R.raw.a_10);
        bgmusic = MediaPlayer.create(this, R.raw.bgmusic);
        regVolumen(5);
        bgmusic.start();
        bgmusic.setLooping(true);
        obtenerDuracion();
        reproducirAudio();
    }
    protected void regVolumen(int vol){
        float log1 = (float) (Math.log(maxVolume - vol) / Math.log(maxVolume));
        bgmusic.setVolume(1 - log1, 1 - log1);
    }
    protected void reproducirAudio(){
        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer reproductor) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        fadeOut();
                        elegirAudio();
                    }
                }, spareTime/(bTot-10+a+1-10)*1000);   //5 seconds
                fadeIn();
            }
        });
    }
    protected void fadeIn(){
        new CountDownTimer(1000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (vol<50) regVolumen(vol++);

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    protected void fadeOut(){
        new CountDownTimer(1000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (vol>5) regVolumen(vol--);

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    protected void elegirAudio(){
        reproductor.stop();
        reproductor.reset();
        if (alert==false){
            if (runTime>0) {
                    try {

                        if ((bTot - 10) > 0 && nextAudioLevel == "a_") {
                            nextAudioLevel = "b_";
                            filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/" + nextAudioLevel + (nextAudioB-fixb)+op;
                            reproductor.setDataSource(getBaseContext(), Uri.parse(filename));
                            reproductor.prepare();
                            runTime -= durationB.get(nextAudioB);
                            nextAudioB++;
                            bTot--;
                        } else {

                            nextAudioLevel = "a_";
                            filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/" + nextAudioLevel + (nextAudio-fix)+op;
                            reproductor.setDataSource(getBaseContext(), Uri.parse(filename));
                            reproductor.prepare();
                            runTime -= durationA.get(nextAudio);
                            nextAudio++;
                        }
                        Toast.makeText(getApplicationContext(), "Audio numero " + (nextAudio - 10), Toast.LENGTH_LONG).show();
                        reproductor.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "NO MORE AUDIOS SIR, " + spareTime, Toast.LENGTH_LONG).show();
                        reproductor.stop();
                        reproductor.release();
                    }

            }else{
                Toast.makeText(getApplicationContext(),"YOU FINISHED", Toast.LENGTH_LONG).show();
            }

        }else{
            try {
                alert=false;
                String filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/c_" + alertType;
                reproductor.setDataSource(getBaseContext(), Uri.parse(filename));
                reproductor.prepare();
                reproductor.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    protected void obtenerDuracion(){
        Context ctx = getBaseContext();
        InputStream is = ctx.getResources().openRawResource(duracion);
        Scanner sc = new Scanner(is);
        String linea = sc.next();
        durationSeg = sc.nextInt();
        String level="";
        int durA=0, durB=0, durC=0, aDurTotal=0, bDurTotal=0, cDurTotal=0, c = 10, b = 10, durRest=0;
        a = 10;
        while (sc.hasNext()){
            level = sc.next();
            switch (level.charAt(0)){
                case 'a':                           //29 seg
                    durA = sc.nextInt();
                    aDurTotal+=durA;
                    durationA.put(a,durA);
                    a++;
                    break;
                case 'b':
                    durB=sc.nextInt();
                    bDurTotal+=durB;
                    durationB.put(b,durB);
                    b++;
                    break;
                case 'c':
                    durC=sc.nextInt();
                    cDurTotal+=durC;
                    durationC.put(c,durC);
                    c++;
                    break;
                default:
                    break;
            }
        }
        sc.close();
        //Toast.makeText(getApplicationContext(),String.valueOf(aDurTotal), Toast.LENGTH_LONG).show();
        durRest=runTime-aDurTotal;
        bTot=10;

        for (int i = 10; durRest>0; i++){
            if (durationB.containsKey(i)) {
                durRest -= durationB.get(i);
                bTot++;
            }else{
                spareTime++;
                durRest--;
            }
        }

        // bTot-=1;
        Toast.makeText(getApplicationContext(),"Entran los "+(bTot-10)+" primeros audios de la categoria B :)", Toast.LENGTH_LONG).show();
    }

    //Clicks
    public void Comenzar(View v) {
        /*
        EditText Cajatexto = (EditText) findViewById(R.id.ET_Nombre);
        String nombre = Cajatexto.getText().toString();
        Toast.makeText(this,"Hola "+ nombre, Toast.LENGTH_LONG).show();*/
        fadeOut();
        reproductor.start();
        nextAudio++;
        float duration = reproductor.getDuration();
        float getPos = reproductor.getCurrentPosition();
    }
    public void Alert(View v){
        alert=true;
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("*/*");
        //startActivityForResult(intent.createChooser(intent, "Selecciona un audio"), FILE_SELECT_CODE);
    }
    public void Left(View v){
        op="_l";
        fixb = nextAudioB-10;
        fix = nextAudio-10;
    }
    public void Right(View v){
        op="_r";
        fixb = nextAudioB-10;
        fix = nextAudio-10;
    }


}
