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
import com.abbysoft.inmobiliariaapp.models.Tipo;
import com.abbysoft.inmobiliariaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<List<Tipo>> tiposLiveData;
    private MutableLiveData<Boolean> inmuebleGuardadoLiveData = new MutableLiveData<>();


    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
        tiposLiveData = new MutableLiveData<>();
        cargarTipos();
    }
    public MutableLiveData<Boolean> getInmuebleGuardadoLiveData() {
        return inmuebleGuardadoLiveData;
    }

    public MutableLiveData<List<Tipo>> getTiposLiveData() {
        return tiposLiveData;
    }

    private void cargarTipos() {
        String token = "Bearer " + obtenerToken();  // Obtiene el token de SharedPreferences

        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<List<Tipo>> call = api.ListadodeTipos(token);

        call.enqueue(new Callback<List<Tipo>>() {
            @Override
            public void onResponse(Call<List<Tipo>> call, Response<List<Tipo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tiposLiveData.setValue(response.body());
                    Log.d("NuevoInmueble", "Tipos de inmueble cargados: " + response.body());
                } else {
                    Log.d("NuevoInmueble", "Código Error en Tipos: " + response.code());
                    Toast.makeText(getApplication(), "Error al obtener tipos de inmueble", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tipo>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardarInmueble(String token, Inmueble inmueble) {
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Log.d("NuevoInmueble", "Guardando inmueble: " + inmueble.getDireccion()+" "+ inmueble.getUso()
                +" "+ inmueble.getCantidad_Ambientes()+" "+ inmueble.getPrecio_Alquiler()+" "+ inmueble.getTipo() + " " + inmueble.getId_tipo()
                + " "+ inmueble.isActivo() + " "+ inmueble.isDisponible() + " " + inmueble.getLatitud() + " " + inmueble.getLongitud()
        + " "+ inmueble.getId_propietario());

        api.AltaInmueble("Bearer " + token, inmueble).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    inmuebleGuardadoLiveData.setValue(true); // Notificar guardado exitoso
                    Toast.makeText(getApplication(), "Inmueble guardado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("NuevoInmueble", "Código Error en GuardarInmueble: " + response.code());
                    inmuebleGuardadoLiveData.setValue(false); // Notificar error
                    Toast.makeText(getApplication(), "Error al guardar el inmueble", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.d("NuevoInmueble", "Error de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obtenerToken() {
        return getApplication().getSharedPreferences("login_prefs", Context.MODE_PRIVATE).getString("token", "");
    }
}
