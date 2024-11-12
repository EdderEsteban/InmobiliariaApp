package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.models.Inmueble;
import com.abbysoft.inmobiliariaapp.request.ApiClient;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private final MutableLiveData<Inmueble> inmuebleLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> fotoUrlsLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> EstadoActivo = new MutableLiveData<>();


    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public void setInmueble(Inmueble inmueble) {
        inmuebleLiveData.setValue(inmueble);

        List<String> fotoUrls = new ArrayList<>();
        if (inmueble.getFotos() != null && !inmueble.getFotos().isEmpty()) {
            for (int i = 0; i < inmueble.getFotos().size(); i++) {
                fotoUrls.add(ApiClient.URLFOTOS + inmueble.getFotos().get(i).getFotoUrl());
            }
        }
        fotoUrlsLiveData.setValue(fotoUrls);
    }

    public LiveData<Inmueble> getInmuebleLiveData() {
        return inmuebleLiveData;
    }

    public LiveData<List<String>> getFotoUrlsLiveData() {
        return fotoUrlsLiveData;
    }



    public void actualizarEstadoInmueble(String token, int inmuebleId, boolean nuevoEstado, Context context) {
        ApiClient.InmobiliariaService apiService = ApiClient.getApiInmobiliaria();
        Call<Void> call = apiService.actualizarEstadoActivo("Bearer " + token, inmuebleId, nuevoEstado);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    EstadoActivo.setValue(nuevoEstado);

                    Toast.makeText(context, "Estado actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Detalles", "Código: " + response.code() + ", Error: " + response.message());
                    Toast.makeText(context, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
