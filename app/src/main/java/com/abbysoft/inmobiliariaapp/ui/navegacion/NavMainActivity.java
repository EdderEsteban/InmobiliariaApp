package com.abbysoft.inmobiliariaapp.ui.navegacion;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.abbysoft.inmobiliariaapp.R;
import com.abbysoft.inmobiliariaapp.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.abbysoft.inmobiliariaapp.databinding.ActivityNavMainBinding;

public class NavMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permisoLoc();

        setSupportActionBar(binding.appBarNavMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_perfilview, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configuración del header en NavigationView
        View headerView = navigationView.getHeaderView(0);
        // ImageView headerImagen = headerView.findViewById(R.id.imageViewPerfil);
        TextView headerTitle = headerView.findViewById(R.id.textviewName);
        TextView headerSubtitle = headerView.findViewById(R.id.textViewMail);

        SharedPreferences spt = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        headerTitle.setText("Bienvenido");
        headerSubtitle.setText(spt.getString("email", "correo@ejemplo.com"));

        // Listener para cerrar el drawer al cambiar de destino
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            drawer.closeDrawer(GravityCompat.START);
        });




        // Añadir funcionalidad al menú de cerrar sesión
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            logout();
            return true;
        });
    }

    // Método de logout
    private void logout() {
        SharedPreferences spt = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        spt.edit()
                .clear()
                .apply();
        SharedPreferences spl = getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE);
        spl.edit()
                .clear()
                .apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Permiso de localizacion
    public void permisoLoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1000);
        }
    }

}