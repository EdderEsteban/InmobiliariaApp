package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abbysoft.inmobiliariaapp.adapters.ContratoAdapter;
import com.abbysoft.inmobiliariaapp.databinding.FragmentContratosBinding;
import com.abbysoft.inmobiliariaapp.models.Contrato;

import java.util.ArrayList;
import java.util.List;



public class ContratosFragment extends Fragment {

    private ContratosViewModel contratosViewModel;
    private FragmentContratosBinding binding;
    private ContratoAdapter contratoAdapter;
    private List<Contrato> contratos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        contratosViewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        // Configurar el RecyclerView y el adaptador
        binding.recyclerViewContratos.setLayoutManager(new LinearLayoutManager(getContext()));
        contratoAdapter = new ContratoAdapter(getContext(), contratos);
        binding.recyclerViewContratos.setAdapter(contratoAdapter);


        // Obtener el ID del inmueble desde los argumentos
        int inmuebleId = getArguments() != null ? getArguments().getInt("inmuebleId", -1) : -1;

        // Observar la lista de contratos en el ViewModel
        contratosViewModel.getContratosLiveData().observe(getViewLifecycleOwner(), new Observer<List<Contrato>>() {
            @Override
            public void onChanged(List<Contrato> newContratos) {
                if (newContratos != null && !newContratos.isEmpty()) {
                    // Ocultar el mensaje de "No hay contratos"
                    binding.tvNoContracts.setVisibility(View.GONE);
                    binding.recyclerViewContratos.setVisibility(View.VISIBLE);
                Log.d("ContratosFragment", "Lista de contratos recibida con tamaño: " + newContratos.size());
                    // Actualizar la lista y notificar cambios al adaptador
                    contratos.clear();
                    contratos.addAll(newContratos);
                    contratoAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ContratosFragment", "Lista de contratos es null");
                    // Mostrar mensaje si no hay contratos
                    binding.tvNoContracts.setVisibility(View.VISIBLE);
                    binding.recyclerViewContratos.setVisibility(View.GONE);
                }
            }
        });


        // Cargar los contratos del inmueble especificado
        if (inmuebleId != -1) {
            contratosViewModel.loadContratos(inmuebleId);
        } else {
            // Mostrar mensaje de error o manejar el caso en que no haya ID de inmueble
            Toast.makeText(getContext(), "ID de inmueble no válido", Toast.LENGTH_SHORT).show();
        }


        return binding.getRoot();
    }

    // Limpiar el binding cuando la vista se destruya
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}