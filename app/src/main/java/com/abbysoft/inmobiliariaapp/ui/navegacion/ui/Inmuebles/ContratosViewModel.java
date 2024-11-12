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

import com.abbysoft.inmobiliariaapp.models.Contrato;
import com.abbysoft.inmobiliariaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel para manejar la lógica de negocio de los contratos.
 */
public class ContratosViewModel extends AndroidViewModel {

    /**
     * LiveData que almacena la lista de contratos.
     */
    private MutableLiveData<List<Contrato>> contratosLiveData = new MutableLiveData<>();

    /**
     * Constructor que inicializa el ViewModel con la aplicación.
     *
     * @param application La aplicación que utiliza el ViewModel.
     */
    public ContratosViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Obtiene el LiveData que almacena la lista de contratos.
     *
     * @return El LiveData que almacena la lista de contratos.
     */
    public LiveData<List<Contrato>> getContratosLiveData() {
        return contratosLiveData;
    }

    public void loadContratos(int inmuebleId) {
        // Obtiene el token de autenticación desde las preferencias compartidas.
        SharedPreferences sp = getApplication().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        Log.d("ContratosViewModel", "Cargando contratos para el inmueble con ID: " + inmuebleId);

        // Crea una instancia del servicio de API.
        ApiClient.InmobiliariaService apiService = ApiClient.getApiInmobiliaria();

        // Crea la llamada a la API para obtener la lista de contratos.
        Call<List<Contrato>> call = apiService.ListadodeContratos("Bearer " + token, inmuebleId);

        // Encola la llamada a la API.
        call.enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful()) {
                    List<Contrato> contratos = response.body();
                    contratosLiveData.setValue(contratos);

                    // Log para ver el contenido de la lista de contratos recibidos
                    Log.d("ContratosViewModel", "Contratos cargados: " + (contratos != null ? contratos.size() : 0));

                    if (contratos != null) {
                        for (Contrato contrato : contratos) {
                            Log.d("ContratoItem", "ID: " + contrato.getId_contrato() + ", Monto: " + contrato.getMonto() +
                                    ", Fecha Inicio: " + contrato.getFecha_inicio() + ", Fecha Fin: " + contrato.getFecha_fin() +
                                    ", Vigencia: " + contrato.getVigencia());
                        }
                    }
                } else {
                    Log.d("ContratosViewModel", "Error al cargar contratos: Código de error " + response.code());
                    contratosLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                Log.d("ContratosViewModel", "Error de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error al cargar contratos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}