package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.request.ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubirFotosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<File>> imageFilesLiveData = new MutableLiveData<>();

    public SubirFotosViewModel(@NonNull Application application) {
        super(application);
        imageFilesLiveData.setValue(new ArrayList<>());  // Inicializar la lista vacía
    }

    // Setter para actualizar la lista de archivos seleccionados
    public void setImageFiles(List<File> files) {
        imageFilesLiveData.setValue(files);
    }

    // Getter para observar la lista de archivos seleccionados
    public LiveData<List<File>> getImageFilesLiveData() {
        return imageFilesLiveData;
    }

    // Método para subir las fotos a la API
    public void subirFotos(Context context, String token, int inmuebleId) {

        List<File> files = imageFilesLiveData.getValue();
        if (files == null || files.isEmpty()) {
            Log.d("SubirFotosViewModel", "No hay fotos para subir");
            Toast.makeText(context, "No hay fotos para subir", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("SubirFotosViewModel", "Subiendo fotos para el inmueble con ID: " + inmuebleId);
        Log.d("SubirFotosViewModel", "Cantidad de fotos: " + files.size());
        Log.d("SubirFotosViewModel", "Token: " + token);

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (File file : files) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("fotos", file.getName(), requestFile);
            parts.add(part);
        }

        // Llamada a la API
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<String> call = api.SubirFotos("Bearer " + token, inmuebleId, parts);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Fotos subidas correctamente", Toast.LENGTH_SHORT).show();
                    Log.d("SubirFotosViewModel", "Fotos subidas correctamente");
                } else {
                    Log.e("SubirFotosViewModel", "Error al subir fotos: " + response.message());
                    Toast.makeText(context, "Error al subir fotos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("SubirFotosViewModel", "Error de conexión: " + t.getMessage());
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
