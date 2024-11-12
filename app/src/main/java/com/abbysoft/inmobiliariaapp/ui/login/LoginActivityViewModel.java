package com.abbysoft.inmobiliariaapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.abbysoft.inmobiliariaapp.request.ApiClient;
import com.abbysoft.inmobiliariaapp.ui.navegacion.NavMainActivity;
import com.abbysoft.inmobiliariaapp.ui.navegacion.ui.home.HomeFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private Context context;
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }

    public void logInApi (String email, String password){
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<String> call = api.apilogin(email, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    // Login exitoso
                    String token = response.body();
                    Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show();

                    // Guardar el token en SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);  // Guardar el token
                    editor.putString("email", email); // Guardar el email
                    editor.apply();  // Aplicar los cambios

                    // Redirigir a Home del Navegador
                    Intent intent = new Intent(context, NavMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    // Error de Login
                    Log.d("Error de Login", "Código: " + response.code() + ", Error: " + response.message());
                    Toast.makeText(context, "Correo o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                } 
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.e("Error de Conexión", throwable.getMessage());
                Toast.makeText(context, "Error de Conexión: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToRegisterActivity(){
        Intent intent = new Intent(context, ResetPassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
