package com.example.agenda.modeloDeVista.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agenda.R;
import com.example.agenda.dao.DaoUsuario;
import com.example.agenda.modelo.manejoDeDatos.Encriptador;
import com.example.agenda.modelo.Usuario;

public class PantallaLogin extends AppCompatActivity implements View.OnClickListener {

    EditText tfUsuario, tfPasswd;
    Button btnIniciarSesion, btnRegistrarse;
    CheckBox chckBxRecordar;
    DaoUsuario daoU;
    Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_login);

        SharedPreferences prefs = Encriptador.getSPEncriptado(this);

        tfUsuario = findViewById(R.id.tfUsuario);
        Log.i("PREFERENCES_USUARIO", prefs.getString("usuario", "DEFAULT"));
        tfUsuario.setText("");
        tfUsuario.setText(prefs.getString("usuario", ""));
        tfUsuario.addTextChangedListener(loginTextWatcher);

        Log.i("PREFERENCES_PASSWD", prefs.getString("passwd", "DEFAULT"));
        tfPasswd = findViewById(R.id.tfPasswd);
        tfPasswd.setText("");
        tfPasswd.setText(prefs.getString("passwd", ""));
        tfPasswd.addTextChangedListener(loginTextWatcher);


        chckBxRecordar = findViewById(R.id.chkBxRecordar);
        daoU = new DaoUsuario(this);



        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setEnabled(false);
        btnIniciarSesion.setOnClickListener(this);

        if(prefs.getBoolean("recordar", true)){
            if(comprobarUsuario()){
                chckBxRecordar.setChecked(true);

                lanzarMenuPrincipal(null);

            }
        }

        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this);


    }


    public void mostrarPasswd(View view) {

        if(view.getId() == R.id.btnMostrarPasswd){
            if(tfPasswd.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                tfPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                //Hide Password
                tfPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputUsuario = tfUsuario.getText().toString().trim();
            String inputPasswd = tfPasswd.getText().toString().trim();

            btnIniciarSesion.setEnabled(!inputUsuario.isEmpty() && !inputPasswd.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnIniciarSesion:
                if(comprobarUsuario()){
                    Toast.makeText(this, "Sesion iniciada", Toast.LENGTH_SHORT).show();

                    lanzarMenuPrincipal(null);
                }
                else{
                    Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.btnRegistrarse:
                lanzarRegistro(null);
                break;

        }
    }


    public boolean comprobarUsuario(){
        boolean logeado = false;
        String u = tfUsuario.getText().toString();
        String p = tfPasswd.getText().toString();


        if(daoU.login(u, p) == 1){
            usuario = daoU.getUsuario(u, p);
            logeado = true;
        }

        return logeado;
    }


    public boolean lanzarMenuPrincipal(View view){
        SharedPreferences prefs = Encriptador.getSPEncriptado(this);
        SharedPreferences.Editor editor = prefs.edit();

        if(chckBxRecordar.isChecked()){
            Log.i("PREFERENCES VALUE2", prefs.getString("usuario", "DEFAULT2"));

            editor.putString("usuario", usuario.getUsuario());
            editor.putString("passwd", usuario.getPasswd());
            editor.putBoolean("recordar", true);
        }
        else{
            editor.remove("usuario");
            editor.remove("passwd");
            editor.putBoolean("recordar", false);


        }
        editor.commit();

        Intent i = new Intent(this, PantallaMenuPrincipal.class);
        i.putExtra("idUsuario", usuario.getId());

        startActivity(i);
        finish();
        return true;
    }

    public boolean lanzarRegistro(View view){
        Intent i = new Intent(this, PantallaRegistrarUsuario.class);
        i.putExtra("tipoActividad", 1);
        startActivity(i);
        return true;
    }


}