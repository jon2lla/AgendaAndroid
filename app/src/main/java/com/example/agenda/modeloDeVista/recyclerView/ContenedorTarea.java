package com.example.agenda.modeloDeVista.recyclerView;

import com.example.agenda.R;
import com.example.agenda.modelo.Tarea;

public class ContenedorTarea {

    private int icoPrio, icoBorrar;
    private String textNomTarea, textDescripcion, textCoste, textFecha;
    private int modoIcono = 1; //1. Default 2. Modo Seleccion multiple 3. Seleccionado 4. Borrable
    private Tarea t;
    private boolean seleccionado;

    public ContenedorTarea(Tarea t){

        this.t = t;
        this.textNomTarea = t.getNomTarea();
        this.textDescripcion = t.getDescripcion();
        String coste = String.valueOf(t.getCoste());
        this.textFecha = t.getFecha();
        this.textCoste = coste.replace(".", ",");
        setIcoPrioridad();
        setIcoBorrar();

    }

    public void setIcoBorrar() {
        this.icoBorrar = R.drawable.ic_basura_cerrada;
    }

    public void setIcoPrioridad(){
        switch (this.t.getPrioridad()){
            case 1:
                this.icoPrio = R.drawable.prioridad1;
                break;
            case 2:
                this.icoPrio = R.drawable.prioridad2;
                break;
            case 3:
                this.icoPrio = R.drawable.prioridad3;
                break;
            case 4:
                this.icoPrio = R.drawable.ic_high_priority;
        }
    }

    public void modoSelecMultiple(){
        this.icoBorrar = R.drawable.ic_baseline_check_box_outline_blank_24;
        modoIcono = 2;

    }

    public void modoBorrado(){
        this.icoBorrar = R.drawable.ic_basura_abierta;
        modoIcono = 4;
    }

    public boolean modoSeleccionado(){
        this.icoBorrar = R.drawable.ic_baseline_check_box_24;
        modoIcono = 3;
        return true;
    }
    public int getIcoPrio(){
        return icoPrio;
    }


    public int getIcoBorrar(){
        return icoBorrar;
    }


    public String getTextNomTarea(){
        return textNomTarea;
    }

    public String getTextDescripcion(){
        return textDescripcion;
    }

    public String getTextFecha(){
        return textFecha;
    }
    public String getTextCoste(){
        return textCoste;
    }

    public Tarea getTarea(){
        return t;
    }


    public int getModoIcono() {
        return modoIcono;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }
}
