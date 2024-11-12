package com.abbysoft.inmobiliariaapp.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.databinding.ActivityMainBinding;
import com.abbysoft.inmobiliariaapp.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel vm;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializamos el ViewModel
        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Observamos el LiveData para iniciar la animación
        vm.getIniciarAnimacion().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean iniciar) {
                if (iniciar) {
                    iniciarAnimaciones();
                } else {
                    navegarALogin();  // Después de la animación, navegar al Login
                }
            }
        });

        // Iniciar la animación en el ViewModel
        vm.iniciarAnimacion();
    }

    // Método para iniciar las animaciones
    private void iniciarAnimaciones() {
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        binding.tvInmo.setAnimation(animacion2);
        binding.tvDotAndroid.setAnimation(animacion2);
        binding.imLogo.setAnimation(animacion1);

        // Después de 3 segundos, navega al LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vm.navegarDespuesDeAnimacion();  // Notificar al ViewModel que la animación terminó
            }
        }, 3000);  // Retraso de 3 segundos
    }

    // Método para navegar al LoginActivity
    private void navegarALogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Finalizar MainActivity para que no vuelva atrás
    }
}
