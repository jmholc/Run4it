package com.example.adm.pantallas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Historias extends AppCompatActivity {

    String[] titles={"Historia 1", "Historia 2"};
    String[] descriptions={"Descripcion 1", "Descripcion 2"};
    int[] images={R.drawable.ic_historia,R.drawable.ic_historia};


    private Button button;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> files_on_server = new ArrayList<>();
    private Handler handler;
    private String selected_file;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historias);

        handler = new Handler();

        permission_check();
/*        listView= (ListView) findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(getApplicationContext(),titles,descriptions,images);
        listView.setOnClickListener(onItemClick);
        listView.setAdapter(adapter);*/
        Log.d("Descargas", "TERMINO EL ADAPTER");




    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(titles[position].equals("Historia 1")){
                Log.d("Descargas", position + " Historia 1");
            }
            else if (titles[position].equals("Historia 1")){
                Log.d("Descargas", position + " Historia 2");
            }
            else {
                Log.d("Descargas", position + " NINGUNA ");
            }
        }
    };

    private void permission_check() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
        }

        initialize();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initialize();
        }else {
            permission_check();
        }
    }

    private void initialize() {

        listView= (ListView) findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(getApplicationContext(),titles,descriptions,images);
        //listView.setOnClickListener((View.OnClickListener) onItemClick);
        listView.setAdapter(adapter);

        /*listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,files_on_server);
        listView.setAdapter(arrayAdapter);*/

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Descargando...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.d("Descargas", i+titles[i]);

                String url="";

                //selected_file = ((TextView)view).getText().toString();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (titles[i].equals("Historia 1")) {
                            Log.d("Descargas", i + " Historia 1");
                            selected_file = "Historia 1.zip";
                        } else if (titles[i].equals("Historia 2")) {
                            Log.d("Descargas", i + " Historia 2");
                            selected_file = "Historia 2.zip";
                        } else {
                            Log.d("Descargas", i + " NINGUNA ");
                        }
                        File FCheck = new File(Environment.getExternalStorageDirectory() + "/" + "Run4It/", selected_file.substring(0, selected_file.length() - 4));
                        File[] contents = FCheck.listFiles();
                        // the directory file is not really a directory..
                        if (contents == null || contents.length == 0) {

                            //======================CREO DIRECTORIOS==============================
                            File f = new File(Environment.getExternalStorageDirectory(), "Run4It");
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                            File f1 = new File(Environment.getExternalStorageDirectory() + "/" + "Run4It", selected_file.substring(0, selected_file.length() - 4));
                            if (!f1.exists()) {
                                f1.mkdirs();
                            }
                            //======================CREO DIRECTORIOS==============================

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url("http://run4it.proyectosort.edu.ar/run4it/Audios/" + selected_file).build();

                            Response response = null;
                            try {

                                response = client.newCall(request).execute();
                                float file_size = response.body().contentLength();

                                BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                                OutputStream stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Run4It/" + selected_file.substring(0, selected_file.length() - 4) +"/"+ selected_file);

                                byte[] data = new byte[8192];
                                float total = 0;
                                int read_bytes = 0;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.show();
                                    }
                                });


                                while ((read_bytes = inputStream.read(data)) != -1) {

                                    total = total + read_bytes;
                                    stream.write(data, 0, read_bytes);
                                    progressDialog.setProgress((int) ((total / file_size) * 100));

                                }

                                progressDialog.dismiss();
                                Log.d("Historias", "HISTORIA DESCARGADA EXITOSAMENTE");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"HISTORIA DESCARGADA EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                stream.flush();
                                stream.close();
                                response.body().close();
                                /*Decompress decompress = new Decompress(Environment.getExternalStorageDirectory() + "/Run4It/" + selected_file.substring(0, selected_file.length() - 4)+"/"+selected_file ,
                                        Environment.getExternalStorageDirectory() + "/Run4It/" + selected_file.substring(0, selected_file.length() - 4));
                                decompress.unzip();*/
                                unpackZip(Environment.getExternalStorageDirectory() + "/Run4It/" + selected_file.substring(0, selected_file.length() - 4)+"/",selected_file);
                                File file = new File(Environment.getExternalStorageDirectory() + "/Run4It/" + selected_file.substring(0, selected_file.length() - 4)+"/"+selected_file);
                                boolean deleted = file.delete();
                                Log.d("Historias", "HISTORIA DESCOMPRIMIDA EXITOSAMENTE");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Log.d("Historias", "La historia ya esta descargada");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"La historia ya esta descargada", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                t.start();
            }
        });
    }
    private boolean unpackZip(String path, String zipname){
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
class MyAdapter extends ArrayAdapter{
    int[] imageArray;
    String[] titleArray;
    String[] descArray;
    public MyAdapter(Context context, String[] titles1, String[] descriptions1, int[] img1){
        super(context,R.layout.custom_listview,R.id.idTitle,titles1);
        this.imageArray=img1;
        this.titleArray=titles1;
        this.descArray=descriptions1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview,parent,false);

        ImageView myImage= (ImageView) row.findViewById(R.id.idPic);
        TextView myTitle = (TextView) row.findViewById(R.id.idTitle);
        TextView myDescription = (TextView) row.findViewById(R.id.idDescription);

        myImage.setImageResource(imageArray[position]);
        myTitle.setText(titleArray[position]);
        myDescription.setText(descArray[position]);
        return row;
    }



}