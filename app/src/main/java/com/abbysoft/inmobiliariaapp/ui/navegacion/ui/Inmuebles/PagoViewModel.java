package com.abbysoft.inmobiliariaapp.ui.navegacion.ui.Inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abbysoft.inmobiliariaapp.models.Pago;
import com.abbysoft.inmobiliariaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PagoViewModel extends AndroidViewModel {

    // LiveData para almacenar la lista de pagos
    private MutableLiveData<List<Pago>> pagosLiveData = new MutableLiveData<>();

    public PagoViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<Pago>> getPagosLiveData() {
        return pagosLiveData;
    }

    public void loadPagos(int contratoId) {
        // Obtiene el token de autenticación desde SharedPreferences.
        SharedPreferences sp = getApplication().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        Log.d("PagoViewModel", "Cargando pagos para el contrato con ID: " + contratoId);

        // Obtiene una instancia del servicio de API.
        ApiClient.InmobiliariaService apiService = ApiClient.getApiInmobiliaria();

        // Crea la llamada a la API para obtener la lista de pagos.
        Call<List<Pago>> call = apiService.ListadodePagos("Bearer " + token, contratoId);

        // Encola la llamada a la API.
        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                // Verifica si la respuesta es exitosa
                if (response.isSuccessful()) {
                    List<Pago> pagos = response.body();
                    pagosLiveData.setValue(pagos); // Actualiza el LiveData con la lista de pagos
                    Log.d("PagoViewModel", "Pagos cargados: " + (pagos != null ? pagos.size() : 0));

                    if (pagos != null) {
                        for (Pago pago : pagos) {
                            Log.d("PagoItem", "ID: " + pago.getId_Pago() + ", Monto: " + pago.getMonto() +
                                    ", Fecha Pago: " + pago.getFecha_Pago() + ", Periodo: " + pago.getPeriodo());
                        }
                    }
                } else {
                    Log.d("PagoViewModel", "Error al cargar pagos: Códig de error " + response.code());
                    pagosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                // Log para fallas de conexión
                Log.d("PagoViewModel", "Error de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error al cargar pagos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
