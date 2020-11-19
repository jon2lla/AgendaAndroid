package com.example.agenda.modeloDeVista.presenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.R;
import com.example.agenda.dao.DaoUsuario;
import com.example.agenda.modelo.Usuario;

public class PantallaRegistrarUsuario extends AppCompatActivity implements View.OnClickListener{

    Button btnGuardarRegistro, btnCancelarRegistro;
    EditText tfNombre, tfPasswdAntigua, tfPasswdNueva, tfPasswdNueva2;
    TextView lblPasswdAntigua, lblPasswdNueva;
    Usuario usuario;
    DaoUsuario daoU;
    int tipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_registrar_usuario);

        Bundle extras = getIntent().getExtras();
        tipo = extras.getInt("tipoActividad");

        btnGuardarRegistro = (Button) findViewById(R.id.btnGuardarRegistro);
        btnGuardarRegistro.setOnClickListener(this);
        btnCancelarRegistro = (Button) findViewById(R.id.btnCancelarRegistro);
        btnCancelarRegistro.setOnClickListener(this);
        tfNombre = (EditText) findViewById(R.id.tfNombre);
        tfPasswdAntigua = (EditText) findViewById(R.id.tfPasswdAntigua);
        tfPasswdNueva = (EditText) findViewById(R.id.tfPasswdNueva);
        tfPasswdNueva2 = (EditText) findViewById(R.id.tfPasswdNueva2);
        lblPasswdAntigua = (TextView) findViewById(R.id.lblPasswdAntigua);
        lblPasswdNueva = (TextView) findViewById(R.id.lblPasswdNueva);
        daoU = new DaoUsuario(this);

        switch(tipo){
            case 1:
                btnGuardarRegistro.setText(R.string.btnRegistrar);
                lblPasswdAntigua.setVisibility(View.GONE);
                tfPasswdAntigua.setVisibility(View.GONE);
                lblPasswdNueva.setText(R.string.lblPasswd);
                tfPasswdNueva.setHint(R.string.hintPasswd);
                tfPasswdNueva2.setHint(R.string.hintPasswd2);
                break;

            case 2:
                usuario = daoU.getUsuarioById(extras.getInt("idUsuario"));
                tfNombre.setText(usuario.getUsuario());
                btnGuardarRegistro.setText(R.string.btnGuardarRegistro);
                tfNombre.setEnabled(false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnGuardarRegistro:
                switch(tipo){
                    case 1:
                        if(registrarUsuario()){
                            lanzarPantallaLogin(null);
                        }
                        break;
                    case 2:
                        if(modificarUsuario()){
                            lanzarPantallaListadoDeTareas(null);
                        }
                        break;
                }
                break;
            case R.id.btnCancelarRegistro:
                switch(tipo){
                    case 1:
                        lanzarPantallaLogin(null);
                        break;
                    case 2:
                        lanzarPantallaListadoDeTareas(null);
                        break;
                }
                break;
        }
    }

    public boolean registrarUsuario(){

        boolean registrado = false;

        if(tfPasswdNueva.getText().toString().equals(tfPasswdNueva2.getText().toString())){
            Usuario u = new Usuario();
            u.setUsuario(tfNombre.getText().toString());
            u.setPasswd(tfPasswdNueva.getText().toString());
            if(u.isNull()){
                Toast.makeText(this, "ERROR. Campos vacios", Toast.LENGTH_SHORT).show();
            }
            else if(daoU.insertUsuario(u)){
                Toast.makeText(this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                registrado = true;
            }
            else{
                Toast.makeText(this, "Usuario ya registrado", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }


        return registrado;
    }

    public boolean modificarUsuario(){
        boolean modificado = false;
        if(tfPasswdAntigua.getText().toString().equals(usuario.getPasswd())) {
            if (tfPasswdNueva.getText().toString().equals(tfPasswdNueva2.getText().toString())) {
                if (tfPasswdAntigua.getText().toString().equals("") || tfPasswdNueva.getText().toString().equals("") || tfPasswdNueva2.getText().toString().equals("")) {
                    Toast.makeText(this, "Campo(s) vacio(s)", Toast.LENGTH_LONG).show();
                } else if (daoU.updatePasswd(usuario, tfPasswdNueva.getText().toString())) {

                    Toast.makeText(this, "Contraseña modificada con exito", Toast.LENGTH_SHORT).show();

                    modificado = true;
                } else {
                    Toast.makeText(this, "ERROR al actualizar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this, "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
        }
        return modificado;
    }

    public boolean lanzarPantallaLogin(View view){
        Intent i = new Intent(PantallaRegistrarUsuario.this, PantallaLogin.class);
        startActivity(i);
        finish();
        return true;
    }

    public boolean lanzarPantallaListadoDeTareas(View view){
        Intent i = new Intent(this, PantallaListadoDeTareas.class);
        i.putExtra("idUsuario", usuario.getId());
        startActivity(i);
        return true;
    }
}