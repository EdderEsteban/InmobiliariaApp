package com.abbysoft.inmobiliariaapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.abbysoft.inmobiliariaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Response;

public class ResetPasswordViewModel extends AndroidViewModel {

    private Context context;
    public ResetPasswordViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void resetPasswordApi(String email) {
        // Obtener la instancia de la API
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();

        // Realizar la llamada a la API con el email
        Call<String> call = api.ResetPassword(email);

        // Encolar la llamada (asíncrona)
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Si la respuesta es exitosa, mostramos un mensaje
                    Toast.makeText(context, "Se ha enviado un correo con la nueva contraseña", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    // Si la respuesta no es exitosa, mostramos un error
                    Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d("Error de envio", "Código: " + response.code() + ", Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Manejo del error de conexión
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
