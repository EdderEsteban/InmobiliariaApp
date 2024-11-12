package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.perfil;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.models.Propietario;
import com.abbysoft.inmobiliariaapp.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final Context context;
    private final MutableLiveData<Boolean> mVolver = new MutableLiveData<>();
    private final MutableLiveData<Uri> openCameraEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openGalleryEvent = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<Boolean> getMVolver() {
        return mVolver;
    }

    public MutableLiveData<Uri> getOpenCameraEvent() {
        return openCameraEvent;
    }

    public MutableLiveData<Boolean> getOpenGalleryEvent() {
        return openGalleryEvent;
    }

    public void elejirFoto(Context activityContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Seleccionar imagen de perfil")
                .setItems(new CharSequence[]{"Tomar Foto", "Elegir de Galería"}, (dialog, which) -> {
                    if (which == 0) openCamera();
                    else openGallery();
                }).show();
    }

    private void openCamera() {
        try {
            File photoFile = createImageFile();
            Uri uri = FileProvider.getUriForFile(context, "com.abbysoft.inmobiliariaapp.fileprovider", photoFile);
            openCameraEvent.setValue(uri);
        } catch (IOException ex) {
            Log.e("PerfilViewModel", "Error al crear archivo para la imagen", ex);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = context.getExternalFilesDir(null);
        return File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
    }

    private void openGallery() {
        openGalleryEvent.setValue(true);
    }

    public void resetVolver() {
        mVolver.setValue(false);
    }

    public void resetOpenCameraEvent() {
        openCameraEvent.setValue(null);
    }

    public void resetOpenGalleryEvent() {
        openGalleryEvent.setValue(false);
    }

    public void saveImageToSharedPreferences(Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = readBytes(inputStream);
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            SharedPreferences sharedPreferences = context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("avatarBase64", encodedImage);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void enviarImagenDesdeSharedPreferences(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString("avatarBase64", null);

        if (encodedImage != null) {
            byte[] imageData = Base64.decode(encodedImage, Base64.DEFAULT);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageData);
            MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatarFile", "avatar.jpg", requestFile);

            ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
            Call<String> call = api.ActualizarFoto("Bearer " + token, avatarPart);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("avatarBase", response.body());
                        editor.apply();

                        Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al actualizar la foto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, "No hay imagen en el perfil", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificarPerfil(String token, @NonNull Propietario propietario) {
        //Seteo el avatar
        SharedPreferences sharedPreferences = context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
        String sharedAvatar = sharedPreferences.getString("avatarBase", null);
        propietario.setAvatar(sharedAvatar);

        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<Propietario> call = api.UpdatePropietario("Bearer " + token, propietario);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    mVolver.setValue(true);
                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
