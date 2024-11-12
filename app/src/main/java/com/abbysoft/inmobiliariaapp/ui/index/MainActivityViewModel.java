package com.abbysoft.inmobiliariaapp.ui.index;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> iniciarAnimacion;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        iniciarAnimacion = new MutableLiveData<>();
    }

    // Método para exponer LiveData de animación
    public LiveData<Boolean> getIniciarAnimacion() {
        return iniciarAnimacion;
    }

    // Método para iniciar la animación
    public void iniciarAnimacion() {
        iniciarAnimacion.setValue(true);  // Señal para iniciar la animación
    }

    // Método para notificar cuando la animación ha terminado y hay que navegar
    public void navegarDespuesDeAnimacion() {
        iniciarAnimacion.setValue(false);  // Señal para que la Activity navegue a la siguiente pantalla
    }
}
