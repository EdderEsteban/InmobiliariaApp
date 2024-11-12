package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class HomeViewModel extends AndroidViewModel {
    private Context context;
    private FusedLocationProviderClient fused;
    private MutableLiveData<MapaActual> mMapa;
    private LatLng localizacion;

    private static final LatLng Inmobiliaria =new LatLng(-33.29509, -66.31905);


    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        fused = LocationServices.getFusedLocationProviderClient(context);
    }

    public LiveData<MapaActual> getMMapa(){
        if(mMapa==null){
            mMapa=new MutableLiveData<>();
        }
        return mMapa;
    }


    public void obtenerUltimaUbicacion() {

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> tarea = fused.getLastLocation();
        tarea.addOnSuccessListener(getApplication().getMainExecutor(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                localizacion = new LatLng(location.getLatitude(), location.getLongitude());
                obtenerMapa();
            }
        });

    }

    public class MapaActual implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            if(localizacion != null) {
                googleMap.addMarker(new MarkerOptions().position(localizacion).title("Yo"));
                googleMap.addMarker(new MarkerOptions().position(Inmobiliaria).title("Inmobiliaria Real State"));

                CameraPosition camPos = new CameraPosition.Builder()
                        .target(localizacion)
                        .zoom(10)
                        .build();
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(camPos);
                googleMap.animateCamera(update);
            }else{
                Toast.makeText(context, "Localizacion nula", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void obtenerMapa(){
        MapaActual ma=new MapaActual();
        mMapa.setValue(ma);
    }


}