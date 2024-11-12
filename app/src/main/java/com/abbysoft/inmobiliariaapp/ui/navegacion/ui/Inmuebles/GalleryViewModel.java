package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.models.Inmueble;
import com.abbysoft.inmobiliariaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> mInmuebles;
    private String token;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        token = getToken(application);  // Obtiene el token de SharedPreferences

    }

    public MutableLiveData<List<Inmueble>> getInmuebles() {
        if (mInmuebles == null) {
            mInmuebles = new MutableLiveData<>();
            loadInmuebles();
        }
        return mInmuebles;
    }

    public void loadInmuebles() {
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<List<Inmueble>> call = api.MisInmuebles("Bearer " + token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mInmuebles.setValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al obtener inmuebles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener el token de SharedPreferences desde "login_prefs"
    private String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}
