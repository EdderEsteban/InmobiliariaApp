package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.databinding.FragmentNavPerfilviewBinding;
import com.abbysoft.inmobiliariaapp.models.Propietario;
import com.abbysoft.inmobiliariaapp.request.ApiClient;
import com.bumptech.glide.Glide;

public class PerfilViewFragment extends Fragment {

    private PerfilViewViewModel pvvm;  // ViewModel
    private FragmentNavPerfilviewBinding binding;  // Binding

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout y configuramos el binding
        binding = FragmentNavPerfilviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializamos el ViewModel
        pvvm = new ViewModelProvider(this).get(PerfilViewViewModel.class);

        // Observamos los datos del propietario
        pvvm.getViewPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {

                    // Pegamos los datos en los EditText
                    binding.tvNombre.setText(propietario.getNombre());
                    binding.tvApellido.setText(propietario.getApellido());
                    binding.tvDni.setText(propietario.getDni());
                    binding.tvDireccion.setText(propietario.getDireccion());
                    binding.tvTelefono.setText(propietario.getTelefono());
                    binding.tvCorreo.setText(propietario.getCorreo());

                    // Mostramos la imagen de avatar
                    String avatarUrl = propietario.getAvatar();
                    if (avatarUrl.equals(ApiClient.URLFOTOS) ||avatarUrl.equals(ApiClient.URLFOTOS+null) || avatarUrl.isEmpty()) {
                        Glide.with(requireActivity())
                            .load(R.drawable.avatar_default)
                            .into(binding.ivAvatarShow);
                    }else {
                        Glide.with(requireActivity())
                            .load(avatarUrl)
                            .into(binding.ivAvatarShow);
                    }
                }
            }
        });

        // Recuperamos el token desde SharedPreferences
        SharedPreferences sp = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sp.getString("token", null);  // Si no existe, devolverá null

        pvvm.pegarPerfilApi(token);

        // Observa el LiveData de navegación
        pvvm.getNavigateEdit().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if (navigate) {
                    // Navegar a PerfilFragment usando NavController
                    Navigation.findNavController(view).navigate(R.id.nav_perfil);
                    pvvm.resetNavigation(); // Resetear el estado de navegación
                }
            }
        });

        // Configurar el botón Editar para activar la navegación en el ViewModel
        binding.btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvvm.editar();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Limpiamos el binding
    }
}
