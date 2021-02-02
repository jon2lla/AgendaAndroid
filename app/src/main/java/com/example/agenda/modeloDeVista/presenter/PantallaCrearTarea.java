package com.example.agenda.modeloDeVista.presenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.R;
import com.example.agenda.dao.DaoTarea;
import com.example.agenda.dao.DaoUsuario;
import com.example.agenda.fragmentos.FragmentoDatePicker;
import com.example.agenda.modelo.manejoDeDatos.FiltroInputDecimal;
import com.example.agenda.modelo.Tarea;
import com.example.agenda.modelo.Usuario;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;

public class PantallaCrearTarea extends AppCompatActivity implements View.OnClickListener{

    Bundle extras;
    Button btnEliminarTarea, btnGuardarTarea, btnSalirTarea;
    EditText tfNomTarea, tfDescripcion, tfFecha, tfCoste;
    TextView lblCabecera, lblMoneda;
    Spinner cmbBxPrioridad;
    CheckBox chkBxTareaRealizada;
    Usuario usuario;
    Tarea tarea;
    DaoUsuario daoU;
    DaoTarea daoT;
    int tipoActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_crear_tarea);

        daoU = new DaoUsuario(this);

        extras = getIntent().getExtras();
        usuario = daoU.getUsuarioById(extras.getInt("idUsuario"));
        tipoActividad = extras.getInt("tipoActividad");

        daoT = new DaoTarea(this, usuario);

        crearSpinner();
        setearBotones();
    }

    public boolean setearBotones(){
        btnEliminarTarea = findViewById(R.id.btnEliminarTarea);
        btnEliminarTarea.setOnClickListener(this);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        btnGuardarTarea.setEnabled(false);
        btnGuardarTarea.setOnClickListener(this);
        btnSalirTarea = findViewById(R.id.btnSalirTarea);
        btnSalirTarea.setOnClickListener(this);

        tfNomTarea = findViewById(R.id.tfNomTarea);
        tfNomTarea.addTextChangedListener(crearTareaTextWatcher);
        tfDescripcion  = findViewById(R.id.tfDescripcion);
        tfDescripcion.addTextChangedListener(crearTareaTextWatcher);
        tfFecha = findViewById(R.id.tfFecha);
        tfFecha.setOnClickListener(this);
        tfFecha.addTextChangedListener(crearTareaTextWatcher);
        tfCoste = findViewById(R.id.tfCoste);


        tfCoste.setFilters(new InputFilter[] {new FiltroInputDecimal(6,2)});



        //        DEPRECATED
        /*TextWatcherMoneda twm = new TextWatcherMoneda();
        tfCoste.addTextChangedListener(twm);*/



        tfCoste.addTextChangedListener(crearTareaTextWatcher);
        tfCoste.setRawInputType(InputType.TYPE_CLASS_NUMBER);


        Currency c = Currency.getInstance(Locale.getDefault());
        lblCabecera = findViewById(R.id.lblCabecera);
        lblMoneda = findViewById(R.id.lblMoneda);
        lblMoneda.setText(c.getSymbol());
        cmbBxPrioridad.findViewById(R.id.cmbBxPrioridad);
        chkBxTareaRealizada = findViewById(R.id.chkBxTareaRealizada);

        switch (tipoActividad){
            case 1:
                chkBxTareaRealizada.setVisibility(View.INVISIBLE);
                btnEliminarTarea.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tfNomTarea.setEnabled(false);
                tfDescripcion.setEnabled(false);
                tfFecha.setEnabled(false);
                tfCoste.setEnabled(false);
                cmbBxPrioridad.setEnabled(false);
                chkBxTareaRealizada.setEnabled(false);

            case 3:
                btnGuardarTarea.setEnabled(true);
                tarea = daoT.getTareaById(extras.getInt("idTarea"));
                lblCabecera.setText(R.string.lblCabecera3);
                tfNomTarea.setText(tarea.getNomTarea());
                tfDescripcion.setText(tarea.getDescripcion());
                tfFecha.setText(tarea.getFecha());
                tfCoste.setText(formatearDecimalSeparator(String.valueOf(tarea.getCoste())));
                cmbBxPrioridad.setSelection(tarea.getPrioridad() - 1);
                if(tarea.getRealizada() == 1){
                    chkBxTareaRealizada.setChecked(true);
                }
                break;
        }
        if(tipoActividad == 2){
            btnEliminarTarea.setVisibility(View.INVISIBLE);
            btnGuardarTarea.setVisibility(View.INVISIBLE);
            lblCabecera.setText(R.string.lblCabecera2);
        }
        return true;
    }

    public String formatearDecimalSeparator(String numeroDecimal){
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols simbolos = format.getDecimalFormatSymbols();
        char sep = simbolos.getDecimalSeparator();
        Log.i("DECIMAL_SEPARATOR", String.valueOf(sep));

        switch (sep){
            case ',':
                numeroDecimal = numeroDecimal.replace(".",",");
                break;
            case '.':
                numeroDecimal = numeroDecimal.replace(",",".");
                break;
        }
        return numeroDecimal;
    }

    private TextWatcher crearTareaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputNomTarea = tfNomTarea.getText().toString().trim();
            String inputDescripcion = tfDescripcion.getText().toString().trim();
            String inputFecha = tfFecha.getText().toString().trim();
            String inputCoste = tfCoste.getText().toString().trim();


            btnGuardarTarea.setEnabled(!inputNomTarea.isEmpty() && !inputDescripcion.isEmpty() && !inputFecha.isEmpty() && !inputCoste.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tfFecha:
                showDatePickerDialog();
                break;
            case R.id.btnEliminarTarea:
                if(borrarTarea()){
                    lanzarPantallaListadoDeTareas(null);
                }
                break;
            case R.id.btnGuardarTarea:
                switch (tipoActividad){
                    case 1:
                        if(crearNuevaTarea()){
                            lanzarPantallaListadoDeTareas(null);
                        };
                        break;
                    case 3:
                        if(modificarTarea())
                            lanzarPantallaListadoDeTareas(null);
                        break;
                }
                break;
            case R.id.btnSalirTarea:
                lanzarPantallaListadoDeTareas(null);
                break;
        }
    }

    public Tarea crearTarea(){
        Tarea t = new Tarea();
        t.setNomTarea(tfNomTarea.getText().toString());
        t.setDescripcion(tfDescripcion.getText().toString());
        String strF = tfFecha.getText().toString();
        char c = strF.charAt(2);
        String s = Character.toString(c);

        t.setFecha(strF);
        Log.i("FECHA TAREA", t.getFecha().toString());


        t.setCoste(Float.parseFloat(formatearDecimalSeparator(tfCoste.getText().toString())));
        t.setPrioridad(cmbBxPrioridad.getSelectedItemPosition() + 1);
        if(chkBxTareaRealizada.isChecked()){
            t.setRealizada(1);
        }
        else{
            t.setRealizada(0);
        }
        return t;
    }

    public boolean crearNuevaTarea(){
        boolean registrado = false;

        Tarea t = crearTarea();
        Log.i("TAREASSSS", t.toString());
        if(t.isNull()){
            Toast.makeText(this, R.string.tstCamposVacios, Toast.LENGTH_SHORT).show();
        }
        else if(daoT.insertTarea(t)){
            Toast.makeText(this, R.string.tstTareaRegistrada, Toast.LENGTH_SHORT).show();
            registrado = true;
        }

        return registrado;
    }

    public boolean borrarTarea(){
        boolean borrado = false;
        if(daoT.deleteTarea(tarea)){
            Toast.makeText(this, R.string.tstTareaBorrada, Toast.LENGTH_SHORT).show();
            borrado = true;
        }
        else {
            Toast.makeText(this, R.string.tstErrorBorrar, Toast.LENGTH_SHORT).show();
        }

        return borrado;
    }

    public boolean modificarTarea(){
        boolean modificado = false;

        this.tarea.setNomTarea(tfNomTarea.getText().toString());
        this.tarea.setDescripcion(tfDescripcion.getText().toString());
        this.tarea.setFecha(tfFecha.getText().toString());
        String coste = tfCoste.getText().toString().replace(",",".");

        this.tarea.setCoste(Float.parseFloat(coste));
        Log.i("TAREA MODIFICADA COSTE", coste);
        this.tarea.setCoste(Float.parseFloat(coste));
        this.tarea.setPrioridad(cmbBxPrioridad.getSelectedItemPosition() + 1);
        if(chkBxTareaRealizada.isChecked()){
            this.tarea.setRealizada(1);
        }
        else{
            this.tarea.setRealizada(0);
        }
        if(daoT.updateTarea(tarea)){
            Toast.makeText(this, R.string.tstTareaMod, Toast.LENGTH_SHORT).show();
            modificado = true;
        }
        else {
            Toast.makeText(this, R.string.tstErrorModTarea, Toast.LENGTH_SHORT).show();
        }

        return modificado;
    }


    private void showDatePickerDialog() {
        FragmentoDatePicker newFragment = FragmentoDatePicker.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month + 1) + "/" + year;
                tfFecha.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public boolean crearSpinner(){
        cmbBxPrioridad = findViewById(R.id.cmbBxPrioridad);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this, R.array.arrayPrioridad, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cmbBxPrioridad.setAdapter(adp3);

        //DESCOMENTAR PARA MOSTRAR EN TOAST LA OPCION SELECCIONADA
        /*
        cmbBxPrioridad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                String ss = cmbBxPrioridad.getSelectedItem().toString();
                Toast.makeText(getBaseContext(), ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        */
        return true;
    }

    public boolean lanzarPantallaListadoDeTareas(View view){
        Intent i = new Intent(this, PantallaListadoDeTareas.class);
        i.putExtra("idUsuario", usuario.getId());
        startActivity(i);
        finish();
        return true;
    }
}