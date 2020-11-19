package com.example.agenda.modelo.manejoDeDatos;


import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiltroInputDecimal implements InputFilter {

    Pattern patron;

    public FiltroInputDecimal(int digitosAntesDeLaComa, int digitosDespuesDeLaComa) {

        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols simbolos = format.getDecimalFormatSymbols();
        char sep = simbolos.getDecimalSeparator();
        Log.i("DECIMAL_SEPARATOR", String.valueOf(sep));

        switch (sep){
            case ',':
                patron = Pattern.compile("(([1-9]{1}[0-9]{0," + (digitosAntesDeLaComa - 1) + "})?||[0]{1})((\\,[0-9]{0," + digitosDespuesDeLaComa + "})?)||(\\.)?");
                break;
            case '.':
                patron = Pattern.compile("(([1-9]{1}[0-9]{0," + (digitosAntesDeLaComa - 1) + "})?||[0]{1})((\\.[0-9]{0," + digitosDespuesDeLaComa + "})?)||(\\.)?");
                break;
        }


        //Patron para controlar el numero total de digitos introducidos y la precision de la parte decimal
        //patron = Pattern.compile("[0-9]{0," + (digitosTotales - 1) + "}+((\\,[0-9]{0," + (digitosDespuesDeLaComa - 1) + "})?)||(\\,)?");

    }


    @Override public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd) {
        String newString = destination.toString().substring(0, destinationStart) + destination.toString().substring(destinationEnd, destination.toString().length());

        newString = newString.substring(0, destinationStart) + source.toString() + newString.substring(destinationStart, newString.length());

        Matcher matcher = patron.matcher(newString);

        if(matcher.matches())
        {
            // Input valido
            return null;
        }

        // Input invalido
        return "";
    }

}
