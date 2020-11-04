package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agenda.dialogos.DialogoBorrar;
import com.example.agenda.dialogos.DialogoBorrarInterface;

public class PantallaListadoDeTareas extends AppCompatActivity implements DialogoBorrarInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_listado_de_tareas);


        //CONTROLADOR BOTONES
        final Button btnTodas = findViewById(R.id.btnTodas);
        final Button btnHechas = findViewById(R.id.btnHechas);
        final Button btnPendientes = findViewById(R.id.btnPendientes);

        btnTodas.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTodas.setPressed(true);
                btnHechas.setPressed(false);
                btnPendientes.setPressed(false);
                return true;
            }
        });

        btnHechas.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTodas.setPressed(false);
                btnHechas.setPressed(true);
                btnPendientes.setPressed(false);
                return true;
            }
        });

        btnPendientes.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTodas.setPressed(false);
                btnHechas.setPressed(false);
                btnPendientes.setPressed(true);
                return true;
            }
        });
    }

    //ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.acercaDe) {
        }
        if (id==R.id.cambiarContrasenia) {
        }
        if (id==R.id.crearTarea) {
            lanzarPantallaCrearTarea(null);
        }
        if (id==R.id.borrar_tareas) {
            mostrarDialogo(null);
        }
        return super.onOptionsItemSelected(item);

    }

    public void mostrarDialogo(View oView) {
        DialogoBorrar oDialogoBorrar;
        oDialogoBorrar = new DialogoBorrar();
        oDialogoBorrar.show(getSupportFragmentManager(), "Mi Diálogo");
    }

    @Override
    public void onRespuesta(String psRespuesta) {

        Toast.makeText( this, psRespuesta, Toast.LENGTH_LONG).show();
    }

    //CAMBIO DE ACTIVITIES
    public void lanzarPantallaCrearTarea(View view){
        Intent i = new Intent(this, PantallaCrearTarea.class);
        startActivity(i);
    }
}