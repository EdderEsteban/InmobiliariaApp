package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.perfil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.databinding.FragmentPerfilBinding;
import com.abbysoft.inmobiliariaapp.models.Propietario;
import com.abbysoft.inmobiliariaapp.request.ApiClient;
import com.bumptech.glide.Glide;

public class PerfilFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    private PerfilViewModel pvm;
    private FragmentPerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pvm = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Cargar datos de SharedPreferences y mostrar en el perfil
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
        binding.etNombre.setText(sharedPreferences.getString("nombre", ""));
        binding.etApellido.setText(sharedPreferences.getString("apellido", ""));
        binding.etDni.setText(sharedPreferences.getString("dni", ""));
        binding.etDireccion.setText(sharedPreferences.getString("direccion", ""));
        binding.etTelefono.setText(sharedPreferences.getString("telefono", ""));

        // Cargar avatar o imagen por defecto
        String avatarUrl = sharedPreferences.getString("avatar", "");
        if (avatarUrl.equals(ApiClient.URLFOTOS) || avatarUrl.equals(ApiClient.URLFOTOS + null) || avatarUrl.isEmpty()) {
            Glide.with(requireActivity())
                    .load(R.drawable.avatar_default)
                    .into(binding.ivAvatar);
        } else {
            Glide.with(requireActivity())
                    .load(avatarUrl)
                    .into(binding.ivAvatar);
        }

        // Observador para abrir la cámara
        pvm.getOpenCameraEvent().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        openCameraWithUri(uri);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }
            }
        });

        // Observador para abrir la galería
        pvm.getOpenGalleryEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean open) {
                if (open) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE_PICK);
                    pvm.resetOpenGalleryEvent();
                }
            }
        });

        // Botón para elegir la foto de perfil
        binding.btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvm.elejirFoto(requireActivity());
            }
        });

        // Botón para guardar perfil
        binding.btnSavePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Propietario propietario = new Propietario();
                propietario.setNombre(binding.etNombre.getText().toString());
                propietario.setApellido(binding.etApellido.getText().toString());
                propietario.setDni(binding.etDni.getText().toString());
                propietario.setDireccion(binding.etDireccion.getText().toString());
                propietario.setTelefono(binding.etTelefono.getText().toString());

                // Obtener correo y ID desde SharedPreferences
                SharedPreferences sp = requireActivity().getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
                String correo = sp.getString("correo", "");
                int id = sp.getInt("id_propietario", 0);
                propietario.setId_Propietario(id);
                propietario.setCorreo(correo);

                // Obtener token y actualizar perfil
                SharedPreferences spt = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                String token = spt.getString("token", "");
                pvm.modificarPerfil(token, propietario);

                // Observar el cambio de LiveData para volver
                pvm.getMVolver().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean mVolver) {
                        if (mVolver) {
                            Navigation.findNavController(view).popBackStack();
                            pvm.resetVolver();  // Resetear el evento de volver
                        }
                    }
                });
            }
        });

        // Botón para subir la imagen guardada en SharedPreferences a la API
        binding.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spt = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                String token = spt.getString("token", "");
                pvm.enviarImagenDesdeSharedPreferences(token);
            }
        });

        // Boton para cambiar de password
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_cambioPass);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageUri = pvm.getOpenCameraEvent().getValue();
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imageUri = data.getData();
            }

            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(binding.ivAvatar);
                pvm.saveImageToSharedPreferences(imageUri);
            } else {
                Toast.makeText(requireContext(), "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCameraWithUri(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Uri uri = pvm.getOpenCameraEvent().getValue();
                if (uri != null) {
                    openCameraWithUri(uri);
                }
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
