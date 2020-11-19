package com.example.agenda.modeloDeVista.presenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agenda.R;
import com.example.agenda.dao.DaoUsuario;
import com.example.agenda.modelo.Usuario;
import com.example.agenda.modeloDeVista.recyclerView2.PantallaListado;

public class PantallaMenuPrincipal extends AppCompatActivity implements View.OnClickListener {

    Button btnConsultartareas, btnCrearTareas;
    Usuario usuario;
    DaoUsuario daoU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        daoU = new DaoUsuario(this);

        Bundle extras = getIntent().getExtras();
        usuario = daoU.getUsuarioById(extras.getInt("idUsuario"));


        btnConsultartareas = (Button) findViewById(R.id.btnConsultarTareas);
        btnConsultartareas.setOnClickListener(this);
        btnCrearTareas = (Button) findViewById(R.id.btnCrearTarea);
        btnCrearTareas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnConsultarTareas:
                lanzarPantallaListadoDeTareas(null);
                break;
            case R.id.btnCrearTarea:
                lanzarPantallaCrearTarea(null);
                break;
        }
    }


    public boolean lanzarPantallaListadoDeTareas(View view){
        Intent i = new Intent(this, PantallaListadoDeTareas.class);
        i.putExtra("idUsuario", usuario.getId());
        startActivity(i);
        return true;
    }

    public boolean lanzarPantallaCrearTarea(View view){
        Intent i2 = new Intent(this, PantallaCrearTarea.class);
        i2.putExtra("idUsuario", usuario.getId());
        i2.putExtra("tipoActividad", 1);
        startActivity(i2);
        return true;
    }
}