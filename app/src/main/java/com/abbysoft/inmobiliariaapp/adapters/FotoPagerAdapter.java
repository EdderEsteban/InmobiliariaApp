package com.abbysoft.inmobiliariaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbysoft.inmobiliariaapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class FotoPagerAdapter extends RecyclerView.Adapter<FotoPagerAdapter.FotoViewHolder> {
    private final List<String> fotoUrls;
    private final Context context;

    public FotoPagerAdapter(Context context, List<String> fotoUrls) {
        this.context = context;
        this.fotoUrls = fotoUrls;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        if (fotoUrls == null || fotoUrls.isEmpty()) {
            // Mostrar imagen por defecto si no hay fotos
            holder.imgFoto.setImageResource(R.drawable.home_default);
            // Foto por defecto
            holder.tvFotoCounter.setText("1/1");

        } else {
            // Cargar la imagen desde la URL
            String fotoUrl = fotoUrls.get(position);
            Glide.with(context)
                    .load(fotoUrl)
                    .placeholder(R.drawable.home_default)
                    .error(R.drawable.casa_error)
                    .into(holder.imgFoto);
            // Actualiza el contador con la posición y el tamaño total
            holder.tvFotoCounter.setText((position + 1) + "/" + fotoUrls.size());
        }
    }

    @Override
    public int getItemCount() {
        // Si no hay URLs, mostramos una sola "foto" con la imagen por defecto
        return (fotoUrls == null || fotoUrls.isEmpty()) ? 1 : fotoUrls.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        TextView tvFotoCounter;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            tvFotoCounter = itemView.findViewById(R.id.tvFotoCounter);
        }
    }
}
