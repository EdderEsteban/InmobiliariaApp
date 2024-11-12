package com.abbysoft.inmobiliariaapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.models.Inmueble;
import com.abbysoft.inmobiliariaapp.request.ApiClient;
import com.bumptech.glide.Glide;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    private final List<Inmueble> inmuebles;
    private final Context context;

    // Constructor para recibir la lista de inmuebles
    public InmuebleAdapter(Context context, List<Inmueble> inmuebles) {
        this.context = context;
        this.inmuebles = inmuebles;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inmueble, parent, false);
        return new InmuebleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble inmueble = inmuebles.get(position);

        // Seteamos los datos en la vista
        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvTipo.setText(inmueble.getTipo().getTipo());
        holder.tvPrecio.setText("$" + inmueble.getPrecio_Alquiler());

        // Verificar si el inmueble tiene fotos
        if (inmueble.getFotos() != null && !inmueble.getFotos().isEmpty()) {
            // Obtener la URL de la primera foto
            String fotoUrl = inmueble.getFotos().get(0).getFotoUrl();

            // Cargar la imagen con Glide
            Glide.with(context)
                    .load(ApiClient.URLFOTOS + fotoUrl)  // Construye la URL final sin duplicar barras
                    .placeholder(R.drawable.home_default)  // Imagen de placeholder mientras carga
                    .error(R.drawable.casa_error)        // Imagen en caso de error
                    .into(holder.imgInmueble);
        } else {
            // Si no hay fotos, usar la imagen por defecto
            holder.imgInmueble.setImageResource(R.drawable.home_default);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble", inmueble);
                Navigation.findNavController(v).navigate(R.id.detalleInmuebleFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    // Clase ViewHolder para manejar las vistas de cada tarjeta
    public static class InmuebleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgInmueble;
        private final TextView tvDireccion;
        private final TextView tvTipo;
        private final TextView tvPrecio;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInmueble = itemView.findViewById(R.id.imgInmueble);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}

