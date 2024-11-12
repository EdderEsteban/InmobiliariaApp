package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.models.Propietario;
import com.abbysoft.inmobiliariaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<Boolean> navigateEdit;
    private Context context;

    public PerfilViewViewModel(@NonNull Application application) {

        super(application);
        context = application.getApplicationContext();
    }

    MutableLiveData<Propietario> getVPropietario() {
        if (mPropietario == null) {
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    MutableLiveData<Boolean> getNavigateEdit() {
        if (navigateEdit == null) {
            navigateEdit = new MutableLiveData<>();
        }
        return navigateEdit;
    }
    // Método para exponer los datos del propietario como LiveData
    public LiveData<Propietario> getViewPropietario() {
        if (mPropietario == null) {
            mPropietario = new MutableLiveData<>();
        }
        return mPropietario;
    }

    // Método para pegar los datos del perfil desde la API
    public void pegarPerfilApi(String token) {
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();

        Call<Propietario> call = api.MyPropietario("Bearer " + token);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    Propietario propietario = response.body();
                    // Capturamos la URL de la BD para usarla en PerfilFragment por si no queremos cambiar de avatar
                    String avatarBase = propietario.getAvatar();
                    // Creamos la URL completa de la imagen de avatar
                    String avatarUrl = ApiClient.URLFOTOS + propietario.getAvatar();

                    propietario.setAvatar(avatarUrl);
                    // Guardamos en SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id_propietario", propietario.getId_Propietario());
                    editor.putString("nombre", propietario.getNombre());
                    editor.putString("apellido", propietario.getApellido());
                    editor.putString("dni", propietario.getDni());
                    editor.putString("direccion", propietario.getDireccion());
                    editor.putString("telefono", propietario.getTelefono());
                    editor.putString("correo", propietario.getCorreo());
                    editor.putString("avatar", avatarUrl);
                    editor.putString("avatarBase", avatarBase);
                    editor.apply();
                    // Actualizamos el LiveData
                    mPropietario.setValue(propietario);
                } else {
                    Log.e("Perfil", "Error de respuesta: Código: " + response.code() + ", Mensaje: " + response.message());
                    Toast.makeText(context, "Error al cargar perfil: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("Perfil", "Error de conexión: " + t.getMessage());
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editar() {
        navigateEdit.setValue(true); // Activa la navegación
    }

    public void resetNavigation() {
        navigateEdit.setValue(false); // Resetea la navegación después de la acción
    }
}
