package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;


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
import com.abbysoft.inmobiliariaapp.adapters.FotoPagerAdapter;
import com.abbysoft.inmobiliariaapp.databinding.FragmentDetalleInmuebleBinding;
import com.abbysoft.inmobiliariaapp.models.Inmueble;


import java.util.List;

public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel divm;
    private FragmentDetalleInmuebleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        divm = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            divm.setInmueble(inmueble);

        }

        divm.getFotoUrlsLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> fotoUrls) {
                FotoPagerAdapter adapter = new FotoPagerAdapter(requireContext(), fotoUrls);
                binding.viewPagerFotos.setAdapter(adapter);
            }
        });

        // Observar cambios en el estado del inmueble
        divm.getInmuebleLiveData().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                binding.tvCodigoDetalle.setText(String.valueOf(inmueble.getId_inmueble()));
                binding.tvDireccionDetalle.setText(inmueble.getDireccion());
                binding.tvTipoDetalle.setText(inmueble.getTipo().getTipo());
                binding.tvUsoDetalle.setText(inmueble.getUso());
                binding.tvAmbientesDetalle.setText(String.valueOf(inmueble.getCantidad_Ambientes()));
                binding.tvPrecioDetalle.setText("$" + inmueble.getPrecio_Alquiler());
                binding.swActivo.setChecked(inmueble.isActivo());
            }
        });

        // Boton para actualizar el estado del inmueble
        binding.btnActualizarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");

                int inmuebleId = divm.getInmuebleLiveData().getValue().getId_inmueble();
                boolean nuevoEstado = binding.swActivo.isChecked();
                divm.actualizarEstadoInmueble(token, inmuebleId, nuevoEstado, getContext());

            }
        });

        // Botón para ir al fragment de fotos
        binding.btnAddFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el ID del inmueble desde el ViewModel o el objeto actual
                int inmuebleId = divm.getInmuebleLiveData().getValue().getId_inmueble();

                // Crear el Bundle con el ID del inmueble
                Bundle args = new Bundle();
                args.putInt("inmuebleId", inmuebleId);
                Log.d("Detalles", "ID del inmueble ARGS: " + args.getInt("inmuebleId"));
                // Navegar al fragment de subir fotos
                Navigation.findNavController(view).navigate(R.id.action_detalleInmueble_to_subirFotos, args);

            }
        });

        // Boton para ir a los contratos
        binding.btnContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inmuebleId = divm.getInmuebleLiveData().getValue().getId_inmueble();
                Bundle args = new Bundle();
                args.putInt("inmuebleId", inmuebleId);
                Navigation.findNavController(view).navigate(R.id.action_detalleInmueble_to_contratosFragment, args);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
