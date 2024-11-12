package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambioPassViewModel extends AndroidViewModel {
    private  MutableLiveData<Boolean> Mvolver;
    private  Context context;

    public CambioPassViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public MutableLiveData<Boolean> getMvolver() {
        if (Mvolver == null) {
            Mvolver = new MutableLiveData<>();
        }
        return Mvolver;
    }

    public void cambiarPassword(String token, String oldPassword, String newPassword) {
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<String> call = api.UpdatePassword("Bearer " + token, oldPassword, newPassword);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                    Mvolver.setValue(true);
                } else {
                    Toast.makeText(context, "Error al actualizar contraseña: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void resetVolver() {
        Mvolver.setValue(false);
    }
}
