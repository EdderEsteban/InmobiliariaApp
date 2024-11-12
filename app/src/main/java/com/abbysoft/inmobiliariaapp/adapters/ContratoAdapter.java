package com.abbysoft.inmobiliariaapp.adapters;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.models.Contrato;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.ContratoViewHolder> {

    private List<Contrato> contratos;
    private LayoutInflater inflater;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ContratoAdapter(Context context, List<Contrato> contratos) {
        this.contratos = contratos;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContratoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_contrato, parent, false);
        return new ContratoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContratoViewHolder holder, int position) {
        Contrato contrato = contratos.get(position);
        // Log para verificar cada contrato que se enlaza
        Log.d("ContratoAdapter", "Binding contrato en posición " + position + ": ID " + contrato.getId_contrato());

        holder.tvId.setText("N° Contrato: " + contrato.getId_contrato());
        holder.tvMonto.setText("Monto: $" + contrato.getMonto());
        if (contrato.getFecha_inicio() != null && contrato.getFecha_fin() != null) {
            holder.tvFechaInicio.setText("Inicio: " + dateFormat.format(contrato.getFecha_inicio()));
            holder.tvFechaFin.setText("Fin: " + dateFormat.format(contrato.getFecha_fin()));
        } else {
            Log.d("ContratoAdapter", "Fecha de inicio o fin es null para contrato con ID " + contrato.getId_contrato());
        }

        holder.tvVigencia.setText("Vigente: " + (contrato.getVigencia() ? "Sí" : "No"));

        // Configuración del listener para navegar al PagoFragment al hacer clic en el contrato
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configura los argumentos para pasar a PagoFragment
                Bundle args = new Bundle();
                args.putInt("contratoId", contrato.getId_contrato());

                // Navega a PagoFragment
                Navigation.findNavController(v).navigate(R.id.action_contratosFragment_to_pagoFragment, args);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = contratos.size();
        Log.d("ContratoAdapter", "Número de contratos en el adaptador: " + count);
        return count;
    }

    static class ContratoViewHolder extends RecyclerView.ViewHolder {
        TextView tvId,tvMonto, tvFechaInicio, tvFechaFin, tvVigencia;

        public ContratoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId =itemView.findViewById(R.id.tvId);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvFechaInicio = itemView.findViewById(R.id.tvFechaInicio);
            tvFechaFin = itemView.findViewById(R.id.tvFechaFin);
            tvVigencia = itemView.findViewById(R.id.tvVigencia);
        }
    }
}

