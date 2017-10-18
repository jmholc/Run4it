package com.example.adm.pantallas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import static com.example.adm.pantallas.R.raw.duracion;
import com.google.android.gms.maps.model.Polyline;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    MediaPlayer reproductor;
    MediaPlayer bgmusic;
    int maxVolume=100;
    int nextAudio = 10, alertType = 10;
    int nextAudioB = 10;
    int runTime = 100;
    int bTot, spareTime, a;
    boolean alert=false;
    String nextAudioLevel="a_";
    int durationSeg;
    private static final int FILE_SELECT_CODE = 0;
    HashMap<Integer,Integer> durationA = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> durationB = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> durationC = new HashMap<Integer, Integer>();
    private GoogleMap mMap;
    String JSON_URL;
    String json_string;
    String txt_json;
    JSONObject jsonObj;
    double latitude, longitude, altitude;
    private int PROXIMITY_RADIUS = 20;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView t, alt, dir, vel;
    String lugares = "";
    PlaceAutocompleteFragment autocompleteFragment;
    double latFragment, lngFragment;
    ArrayList routePol;
    List<List<LatLng>> route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /*
        Context ctx = getBaseContext();
        reproductor = MediaPlayer.create(this, R.raw.a_10);
        bgmusic = MediaPlayer.create(this, R.raw.bgmusic);
        regVolumen(5);
        bgmusic.start();
        bgmusic.setLooping(true);
        obtenerDuracion();
        reproducirAudio();
        */
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }
        t = (TextView) findViewById(R.id.lblLatlon);
        alt = (TextView) findViewById(R.id.lblAltura);
        dir = (TextView) findViewById(R.id.lblDireccion);
        vel = (TextView) findViewById(R.id.lblVelocidad);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latFragment = place.getLatLng().latitude;
                lngFragment = place.getLatLng().longitude;
                new time().execute();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(place.getLatLng());
                markerOptions.title("Destino");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                Toast.makeText(MapsActivity.this, "Resultados: OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "Resultados: ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        t.setText(latitude + ", " + longitude);
        alt.setText("" + altitude);
    }

    private void buildGoogleApiClient() {
        Log.d("onBuildGoogleApiClient", "corroborando informacion");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        altitude = location.getAltitude();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Comienzo");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        //Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        vel.setText(location.getSpeed() + "KM/H");
        t.setText(latitude + ", " + longitude);
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<Address> list = null;
        try {
            list = gc.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address add = list.get(0);
        dir.setText(add.getAddressLine(0));
        alt.setText("" + location.getAltitude());
        Log.d("onLocationChanged", "Exit");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Parse
    public String parseJSON_Time() {
        while (json_string == null) {
        }
        try {
            jsonObj = new JSONObject(json_string);
            String t = jsonObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
            return t;
        } catch (JSONException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String parseJSON_Dist() {
        while (json_string == null) {
        }
        try {
            jsonObj = new JSONObject(json_string);
            String t = jsonObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
            return t;
        } catch (JSONException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public int parseJSON_dir() {
        while (json_string == null) {
        }
        try {
            routePol = new ArrayList<String>();
            route = new ArrayList<List<LatLng>>();

            jsonObj = new JSONObject(json_string);
            JSONArray cantDirs = jsonObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

            for (int i = 0; i < cantDirs.length(); i++) {
                routePol.add(cantDirs.getJSONObject(i).getJSONObject("polyline").getString("points").toString());
                //https://maps.googleapis.com/maps/api/directions/json?origin=-34.550225,-58.454255&destination=-34.553002,-58.471559&mode=walking&key=AIzaSyBkIzubSeyKqjKRRbZ7RhtGb_UZA84VPU4
            }
            for (int i = 0; i < routePol.size(); i++) {
                route.add(decodePolyLine(routePol.get(i).toString()));
            }
            return cantDirs.length();
        } catch (Exception e) {
            return -1;
        }
    }

    public void parseJSON_places() {
        if (json_string == null)
            Toast.makeText(getApplicationContext(), "Primero descarga el JSON_PLACES", Toast.LENGTH_SHORT).show();
        else {
            try {
                jsonObj = new JSONObject(json_string);
                JSONArray resultArray = jsonObj.getJSONArray("results");
                int count = 0;
                String nombre;
                while (count < resultArray.length()) {
                    JSONObject JO = resultArray.getJSONObject(count);
                    nombre = JO.getString("name");
                    JSONObject geometry = JO.getJSONObject("geometry").getJSONObject("location");
                    latitude = geometry.getDouble("lat");
                    longitude = geometry.getDouble("lng");
                    lugares += nombre + ", ";
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Decodifica polylines
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }
        return decoded;
    }

    //Pedido de JSON
    private class time extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            JSON_URL = getUrl_time(latFragment, lngFragment);
        }

        protected String doInBackground(Void... params) {
            try {
                StringBuilder JSON_DATA = new StringBuilder();
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((json_string = reader.readLine()) != null) {
                    JSON_DATA.append(json_string).append("\n");
                }
                return JSON_DATA.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            txt_json = result;
            json_string = result;

        }
    }

    private class places extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            JSON_URL = getUrl_places(latitude, longitude);
        }

        protected String doInBackground(Void... params) {
            try {
                StringBuilder JSON_DATA = new StringBuilder();
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((json_string = reader.readLine()) != null) {
                    JSON_DATA.append(json_string).append("\n");
                }
                return JSON_DATA.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            txt_json = result;
            json_string = result;

        }
    }

    //URL builders
    public String getUrl_time(double latDest, double lonDest) {
        StringBuilder googlePlacesUrl2 = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googlePlacesUrl2.append("origin=" + latitude + "," + longitude);
        googlePlacesUrl2.append("&destination=" + latDest + ", " + lonDest);
        googlePlacesUrl2.append("&mode=walking");
        googlePlacesUrl2.append("&key=" + "AIzaSyBkIzubSeyKqjKRRbZ7RhtGb_UZA84VPU4");
        return (googlePlacesUrl2.toString());
    }

    private String getUrl_places(double latitude, double longitude) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBkIzubSeyKqjKRRbZ7RhtGb_UZA84VPU4");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////REPRODUCCION DE AUDIOS Y REESTRUCTURACION//////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    */
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
                        regVolumen(5);
                        elegirAudio();
                    }
                }, spareTime/(bTot-10+a+1-10)*1000);   //5 seconds
                regVolumen(50);
            }
        });
    }
    protected void elegirAudio(){
        reproductor.stop();
        reproductor.reset();
        if (alert==false){
            if (runTime>0) {
                try {

                    if ((bTot - 10) > 0 && nextAudioLevel == "a_") {
                        nextAudioLevel = "b_";
                        String filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/" + nextAudioLevel + nextAudioB;
                        reproductor.setDataSource(getBaseContext(), Uri.parse(filename));
                        reproductor.prepare();
                        runTime -= durationB.get(nextAudioB);
                        nextAudioB++;
                        bTot--;
                    } else {
                        nextAudioLevel = "a_";
                        String filename = "android.resource://" + getBaseContext().getPackageName() + "/raw/" + nextAudioLevel + nextAudio;
                        reproductor.setDataSource(getBaseContext(), Uri.parse(filename));
                        reproductor.prepare();
                        runTime -= durationA.get(nextAudio);
                        nextAudio++;
                    }
                    Toast.makeText(getApplicationContext(),"Audio numero " + (nextAudio-10+1), Toast.LENGTH_LONG).show();
                    reproductor.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "NO MORE AUDIOS SIR, "+spareTime, Toast.LENGTH_LONG).show();
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
        reproductor.start();
        nextAudio++;
        float duration = reproductor.getDuration();
        float getPos = reproductor.getCurrentPosition();
    }
    public void Alert(View v){
        alert=true;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent.createChooser(intent, "Selecciona un audio"), FILE_SELECT_CODE);
    }


    public void onClick_places(View v) throws IOException {
        new places().execute();
        parseJSON_places();
        if(lugares!="")Toast.makeText(MapsActivity.this, lugares, Toast.LENGTH_SHORT).show();
        lugares="";
    }

    public void onClick_time(View v) {
        Toast.makeText(getApplicationContext(), parseJSON_Time() + " || " + parseJSON_Dist(), Toast.LENGTH_LONG).show();
        parseJSON_dir();
        for (int i = 0; i < route.size(); i++) {
            for (int j = 0; j < route.get(i).size() - 1; j++) {

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(route.get(i).get(j), route.get(i).get(j + 1))
                        .width(8)
                        .color(Color.RED));
            }
        }
    }

}
