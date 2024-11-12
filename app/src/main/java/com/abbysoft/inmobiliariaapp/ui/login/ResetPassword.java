package com.abbysoft.inmobiliariaapp.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.databinding.ActivityResetPasswordBinding;

public class ResetPassword extends AppCompatActivity {
    private ResetPasswordViewModel rpvm;
    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rpvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ResetPasswordViewModel.class);

        // Boton Resetear Password
        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rpvm.resetPasswordApi(binding.etResetMail.getText().toString());
            }
        });
    }
}