package com.abbysoft.inmobiliariaapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.models.Pago;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoViewHolder> {

    private List<Pago> pagos;
    private LayoutInflater inflater;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public PagoAdapter(Context context, List<Pago> pagos) {
        this.pagos = pagos;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_pago, parent, false);
        return new PagoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pago pago = pagos.get(position);

        holder.tvNPago.setText("N° Pago: " + pago.getId_Pago());
        holder.tvNContrato.setText("N° Contrato: " + pago.getId_Contrato());

        // Control de null en fechas
        if (pago.getFecha_Pago() != null) {
            holder.tvFecha_pago.setText("Fecha de Pago: " + dateFormat.format(pago.getFecha_Pago()));
        } else {
            holder.tvFecha_pago.setText("Fecha de Pago: No disponible");
        }

        if (pago.getPeriodo() != null) {
            holder.tvPeriodo.setText("Periodo: " + dateFormat.format(pago.getPeriodo()));
        } else {
            holder.tvPeriodo.setText("Periodo: No disponible");
        }

        holder.tvMonto.setText("Monto: $" + pago.getMonto());

        // Log para depurar cada elemento Pago
        Log.d("PagoAdapter", "Binding pago en posición " + position + ": ID " + pago.getId_Pago());
    }

    @Override
    public int getItemCount() {
        int count = pagos.size();
        Log.d("PagoAdapter", "Número de pagos en el adaptador: " + count);
        return count;
    }

    static class PagoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNPago, tvNContrato, tvFecha_pago, tvMonto, tvPeriodo;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNPago = itemView.findViewById(R.id.tvNPago);
            tvNContrato = itemView.findViewById(R.id.tvNContrato);
            tvFecha_pago = itemView.findViewById(R.id.tvFecha_pago);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvPeriodo = itemView.findViewById(R.id.tvPeriodo);
        }
    }
}
