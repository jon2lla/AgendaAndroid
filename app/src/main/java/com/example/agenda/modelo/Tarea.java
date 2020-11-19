package com.example.agenda.modelo;

import com.example.agenda.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;
import com.example.agenda.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.core.content.res.TypedArrayUtils.getString;

public class Tarea {

    private int id, prioridad;
    private String nomTarea, descripcion, fecha;
    private float coste;
    private int realizada = 0;

    public Tarea() {

    }

    public Tarea(String nomTarea, String descripcion, String fecha, float coste, int prioridad, int realizada) {
        this.nomTarea = nomTarea;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.coste = coste;
        this.prioridad = prioridad;
        this.realizada = realizada;
    }

    public boolean isNull(){
        if(nomTarea.equals("") || descripcion.equals("") || fecha.equals("") || String.valueOf(coste).equals("") || prioridad == 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString(){
        return "idTarea: " + id + " - Nombre: " + nomTarea + " - Descripcion: " + descripcion + " - Fecha: " + fecha + " - Coste: " + coste + " - Prioridad: " + prioridad + " - Realizada: " + realizada;
    }

    public String toStringFormateado(Context c){
        Resources res = c.getResources();
        Currency divisa = Currency.getInstance(Locale.getDefault());

        String costeF = String.valueOf(coste).replace(".", ",") + divisa.getSymbol();
        String str = " - " + res.getString(R.string.tarea) + " " + nomTarea
                + " - " + res.getString(R.string.lblDescripcion) + " " + descripcion
                + " - " + res.getString(R.string.lblFecha) + " " + fecha
                + " - " + res.getString(R.string.lblCoste) + " " + costeF
                + " - " + res.getString(R.string.lblPrioridad) + " ";
        switch (prioridad){
            case 1:
                str = str + res.getString(R.string.spinBaja);
                break;
            case 2:
                str = str + res.getString(R.string.spinMedia);
                break;
            case 3:
                str = str + res.getString(R.string.spinAlta);
                break;
            case 4:
                str = str + res.getString(R.string.spinUrgente);
                break;
        }

        str = str + res.getString(R.string.lblRealizada) + " ";

        switch (realizada){
            case 0:
                str = str + res.getString(R.string.noBorrar);
                break;
            case 1:
                str = str + res.getString(R.string.siBorrar);
                break;
        }

        return str;
    }

    //GETTERS & SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomTarea() {
        return nomTarea;
    }

    public void setNomTarea(String nomTarea) {
        this.nomTarea = nomTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getCoste() {
        return coste;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getRealizada() {
        return realizada;
    }

    public void setRealizada(int realizada) {
        this.realizada = realizada;
    }

}
