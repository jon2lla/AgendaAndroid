package com.example.agenda.modeloDeVista.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.agenda.R;

public class DialogoAcercaDe extends DialogFragment {
    AlertDialog.Builder oBuilder;
    DialogoBorrarInterface oRespuesta;
    Boolean bElegido = false;
    //---- Creamos el Dialog con sus opciones.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        oBuilder = new AlertDialog.Builder(getActivity());
        //---- Poner títutlo y mensaje al Dialog.
        oBuilder.setTitle(R.string.tituloAcercaDe);
        oBuilder.setMessage(R.string.mensajeAcercaDe);
        //---- Respuesta positiva.

        return oBuilder.create();
    } // onCreateDialog()
    //---- A este metodo se le llama al iniciar el Fragmento.
    @Override
    public void onAttach(Context poContext) {
        super.onAttach(poContext);
        oRespuesta = (DialogoBorrarInterface) poContext;
    }

}