package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.abbysoft.inmobiliariaapp.databinding.FragmentNuevoInmuebleBinding;
import com.abbysoft.inmobiliariaapp.models.Inmueble;
import com.abbysoft.inmobiliariaapp.models.Tipo;
import java.util.ArrayList;
import java.util.List;

public class NuevoInmuebleFragment extends Fragment {

    private NuevoInmuebleViewModel viewModel;
    private FragmentNuevoInmuebleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);

        // Set up Spinner for Uso
        ArrayAdapter<String> usoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Residencial", "Comercial"});
        usoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerUso.setAdapter(usoAdapter);

        // Configurar el adaptador del Spinner para mostrar objetos Tipo
        viewModel.getTiposLiveData().observe(getViewLifecycleOwner(), new Observer<List<Tipo>>() {
            @Override
            public void onChanged(List<Tipo> tipos) {
                ArrayAdapter<Tipo> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tipos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                binding.spinnerTipo.setAdapter(adapter);
            }
        });

        // Observa el LiveData del estado de guardado
        viewModel.getInmuebleGuardadoLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean guardadoExitoso) {
                if (guardadoExitoso) {
                    // Navegar de vuelta a GalleryFragment
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                }
            }
        });

        // Botón para guardar el inmueble
        binding.btnGuardarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarInmueble();
            }
        });

        return binding.getRoot();
    }

    private void guardarInmueble() {
        SharedPreferences sp = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        SharedPreferences spp = requireActivity().getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);

        Inmueble inmueble = new Inmueble();
        inmueble.setDireccion(binding.etDireccion.getText().toString());
        inmueble.setUso(binding.spinnerUso.getSelectedItem().toString());
        inmueble.setCantidad_Ambientes(Integer.parseInt(binding.etCantidadAmbientes.getText().toString()));
        inmueble.setPrecio_Alquiler(Double.parseDouble(binding.etPrecioAlquiler.getText().toString()));
        inmueble.setActivo(binding.switchActivo.isChecked());
        inmueble.setDisponible(binding.switchDisponible.isChecked());
        inmueble.setId_propietario(spp.getInt("id_propietario", -1));
        // Puntos Cardinales Nuleados
        inmueble.setLatitud("0.0");
        inmueble.setLongitud("0.0");

        // Set Tipo seleccionado
        Tipo selectedTipo = (Tipo) binding.spinnerTipo.getSelectedItem();
        // Ver q trae el id_tipo
        Log.d("NuevoInmueble", "ID Tipo seleccionado: " + selectedTipo.getId_tipo());
        if (selectedTipo != null) {
            int tipoId = selectedTipo.getId_tipo();
            inmueble.setId_tipo(tipoId);
            //inmueble.setTipo(selectedTipo);
        }

        // Llamada al ViewModel para guardar el inmueble
        viewModel.guardarInmueble(token, inmueble);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
