package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.abbysoft.inmobiliariaapp.databinding.FragmentCambioPassBinding;

public class CambioPassFragment extends Fragment {
    private CambioPassViewModel cpvm;
    private FragmentCambioPassBinding binding;
    private String token;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCambioPassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cpvm = new ViewModelProvider(this).get(CambioPassViewModel.class);

        // Obtener el token desde SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Observa el clic en el botón Guardar
        binding.btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = binding.etOldPass.getText().toString();
                String newPass = binding.etNewPass.getText().toString();

                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    cpvm.cambiarPassword(token, oldPass, newPass);
                }
            }
        });

        cpvm.getMvolver().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean Mvolver) {
                if (Mvolver) {
                    Navigation.findNavController(view).popBackStack(); // Regresar al fragment anterior
                    cpvm.resetVolver(); // Resetear el estado de navegación
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
