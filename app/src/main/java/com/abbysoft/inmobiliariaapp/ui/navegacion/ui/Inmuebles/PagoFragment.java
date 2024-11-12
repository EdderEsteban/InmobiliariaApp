package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abbysoft.inmobiliariaapp.adapters.PagoAdapter;
import com.abbysoft.inmobiliariaapp.databinding.FragmentPagoBinding;
import com.abbysoft.inmobiliariaapp.models.Pago;

import java.util.ArrayList;
import java.util.List;

public class PagoFragment extends Fragment {

    private PagoViewModel pagoViewModel;
    private FragmentPagoBinding binding;
    private PagoAdapter pagoAdapter;
    private List<Pago> pagos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPagoBinding.inflate(inflater, container, false);
        pagoViewModel = new ViewModelProvider(this).get(PagoViewModel.class);

        // Configuración del RecyclerView y el adaptador
        binding.recyclerViewPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        pagoAdapter = new PagoAdapter(getContext(), pagos);
        binding.recyclerViewPagos.setAdapter(pagoAdapter);

        // Obtén el ID del contrato desde los argumentos
        int contratoId = getArguments() != null ? getArguments().getInt("contratoId", -1) : -1;

        // Observa el LiveData en el ViewModel
        pagoViewModel.getPagosLiveData().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> newPagos) {
                if (newPagos != null && !newPagos.isEmpty()) {
                    // Si hay pagos, actualiza el adaptador
                    binding.tvNoPagos.setVisibility(View.GONE);
                    binding.recyclerViewPagos.setVisibility(View.VISIBLE);
                    pagos.clear();
                    pagos.addAll(newPagos);
                    pagoAdapter.notifyDataSetChanged();
                } else {
                    // Si no hay pagos, muestra el mensaje de "No hay pagos"
                    binding.tvNoPagos.setVisibility(View.VISIBLE);
                    binding.recyclerViewPagos.setVisibility(View.GONE);
                }
            }
        });

        // Cargar los pagos del contrato especificado si el ID es válido
        if (contratoId != -1) {
            pagoViewModel.loadPagos(contratoId);
        } else {
            Toast.makeText(getContext(), "ID de contrato no válido", Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
