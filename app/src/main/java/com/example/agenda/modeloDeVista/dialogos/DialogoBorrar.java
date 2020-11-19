package com.example.agenda.modeloDeVista.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.agenda.R;

public class DialogoBorrar extends DialogFragment {
    AlertDialog.Builder oBuilder;
    DialogoBorrarInterface oRespuesta;
    Boolean bElegido = false;
    //---- Creamos el Dialog con sus opciones.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        oBuilder = new AlertDialog.Builder(getActivity());

                //---- Poner títutlo y mensaje al Dialog.
        oBuilder.setTitle(R.string.tituloBorrar);
        oBuilder.setMessage(R.string.mensajeBorrar);
        //---- Respuesta positiva.
        oBuilder.setPositiveButton(R.string.siBorrar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bElegido = true;

                        oRespuesta.onRespuesta(getResources().getString(R.string.mensajeBorrado), 1);
                    } // onClick()
                });
        //---- Respuesta negativa.
        oBuilder.setNegativeButton(R.string.noBorrar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bElegido = true;

                        oRespuesta.onRespuesta(getResources().getString(R.string.mensajeCancelar), 0);
                    } // onClick()
                });

        return oBuilder.create();
    } // onCreateDialog()
    //---- A este metodo se le llama al iniciar el Fragmento.
    @Override
    public void onAttach(Context poContext) {
        super.onAttach(poContext);
        oRespuesta = (DialogoBorrarInterface) poContext;
    } // onAttach()
    //---- A este metodo se le llama si salimos del Dialog sin elegir ninguna opción.
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if ( bElegido == false ) {

            oRespuesta.onRespuesta(getResources().getString(R.string.SinElegir), 0);
        }
    }
}