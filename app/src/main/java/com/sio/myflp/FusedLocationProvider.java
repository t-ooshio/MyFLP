package com.sio.myflp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class FusedLocationProvider extends FragmentActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final String TAG = "FusedLocationProvider";

    private TextView textView;
    private String textlog = "start\n";

    //LocationClientの代わりにGoogleAPIClientをしよう
    GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    private FusedLocationProviderApi fusedLocationProviderApi;

    private LocationRequest locationRequest;
    private Location location;
    private long lastLocationTIme = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_location_provider);

        Log.d(TAG, "onCreate");

        textView = (TextView) findViewById(R.id.textView);

        //LocationRequestを生成して精度・インターバルを設定
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(16);

        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Log.d(TAG, "mGoogleApiClient");

        textlog += "onCreate\n";
        textView.setText(textlog);
    }


    protected void onStart(){
        super.onStart();

        Log.d(TAG,"onStart");

        if(!mResolvingError){
            mGoogleApiClient.connect();

            textlog += "onStart,connect()\n";
            textView.setText(textlog);
        }else{
            textlog += "onStart(),mResolvingError\n";
            textView.setText(textlog);
        }
    }

    protected void onStop(){
        mGoogleApiClient.disconnect();
        textlog += "onStop\n";
        textView.setText(textlog);

        super.onStop();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");

        textlog += "onCennected()\n";
        textView.setText(textlog);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location currentLocation = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG,String.valueOf(currentLocation.getTime()));
        if(currentLocation != null){
            location = currentLocation;

            textlog += "-----onLocationChanged\n";
            textlog += "Lattitude=" + String.valueOf(location.getLatitude()) + "\n";
            textlog += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
            textlog += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
            textlog += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
            textlog += "Time=" + String.valueOf(location.getTime()) + "\n";
            textlog += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
            textlog += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
            textlog += "time" + String.valueOf(lastLocationTIme) + "\n";
            textlog += "Provider=" + String.valueOf(location.getProvider()) + "\n";
            textView.setText(textlog);

            Log.d(TAG,"debug");

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocationTIme = location.getTime() - lastLocationTIme;

        textlog += "-----onLocationChanged\n";
        textlog += "Lattitude=" + String.valueOf(location.getLatitude()) + "\n";
        textlog += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
        textlog += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
        textlog += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
        textlog += "Time=" + String.valueOf(location.getTime()) + "\n";
        textlog += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
        textlog += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
        textlog += "time" + String.valueOf(lastLocationTIme) + "\n";
        textlog += "Provider=" + String.valueOf(location.getProvider()) + "\n";
        textView.setText(textlog);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed");
    }
}
