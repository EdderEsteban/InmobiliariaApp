package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.adapters.InmuebleAdapter;
import com.abbysoft.inmobiliariaapp.databinding.FragmentGalleryBinding;
import com.abbysoft.inmobiliariaapp.models.Inmueble;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private InmuebleAdapter inmuebleAdapter;
    private GalleryViewModel gvm;
    private List<Inmueble> inmuebleList = new ArrayList<>();
    private FragmentGalleryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout usando binding
        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        // Inicializar el ViewModel
        gvm = new ViewModelProvider(this).get(GalleryViewModel.class);

        // Configurar el RecyclerView
        binding.recyclerInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));
        inmuebleAdapter = new InmuebleAdapter(getContext(), inmuebleList);
        binding.recyclerInmuebles.setAdapter(inmuebleAdapter);

        // Observar el MutableLiveData para obtener los inmuebles
        gvm.getInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                inmuebleList.clear();
                inmuebleList.addAll(inmuebles);
                inmuebleAdapter.notifyDataSetChanged();
            }
        });

        // Configurar el botón "Agregar Inmueble"
        binding.fabAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nuevoInmuebleFragment);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cargar inmuebles cada vez que el usuario vuelve a este fragmento
        gvm.loadInmuebles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
