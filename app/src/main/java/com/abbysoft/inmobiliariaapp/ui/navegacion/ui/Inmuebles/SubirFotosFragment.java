package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.abbysoft.inmobiliariaapp.databinding.FragmentSubirFotosBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SubirFotosFragment extends Fragment {

    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private SubirFotosViewModel viewModel;
    private FragmentSubirFotosBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubirFotosBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SubirFotosViewModel.class);

        // Observa los archivos de imagen seleccionados y habilita el botón "Guardar Cambios" si hay imágenes
        viewModel.getImageFilesLiveData().observe(getViewLifecycleOwner(), new Observer<List<File>>() {
            @Override
            public void onChanged(List<File> imageFiles) {
                binding.btnSubirFotos.setEnabled(!imageFiles.isEmpty());
            }
        });

        // Configura el botón para abrir la galería y seleccionar imágenes
        binding.btnSeleccionarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    abrirGaleria();
                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                }
            }
        });

        // Configura el botón para subir las imágenes seleccionadas a la API
        binding.btnSubirFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                int inmuebleId = obtenerInmuebleId();
                viewModel.subirFotos(requireContext(), token, inmuebleId);
            }
        });

        return binding.getRoot();
    }

    // Lanzador de galería para seleccionar múltiples imágenes
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    List<File> selectedImageFiles = new ArrayList<>();
                    Intent data = result.getData();

                    // Maneja selección múltiple de imágenes
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            File file = guardarImagenEnCache(imageUri);
                            if (file != null) selectedImageFiles.add(file);
                        }
                    } else if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        File file = guardarImagenEnCache(imageUri);
                        if (file != null) selectedImageFiles.add(file);
                    }

                    viewModel.setImageFiles(selectedImageFiles); // Actualiza los archivos en el ViewModel
                }
            }
    );

    // Abre la galería para seleccionar imágenes
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Permite seleccionar múltiples imágenes
        galleryLauncher.launch(intent);
    }

    // Guarda la imagen en el almacenamiento de caché para procesarla más tarde
    private File guardarImagenEnCache(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            File tempFile = new File(requireContext().getCacheDir(), System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            Log.d("SubirFotosFragment", "Imagen guardada en caché en: " + tempFile.getAbsolutePath());
            return tempFile;

        } catch (Exception e) {
            Log.e("SubirFotosFragment", "Error al guardar la imagen en caché", e);
            return null;
        }
    }

    // Manejo de los permisos de almacenamiento
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirGaleria();
            } else {
                Toast.makeText(requireContext(), "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Obtener el ID del inmueble desde los argumentos
    private int obtenerInmuebleId() {
        return getArguments() != null ? getArguments().getInt("inmuebleId", -1) : -1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
