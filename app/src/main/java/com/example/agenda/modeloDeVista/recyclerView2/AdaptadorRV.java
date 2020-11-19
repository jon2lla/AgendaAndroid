package com.example.agenda.modeloDeVista.recyclerView2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.modelo.Tarea;
import com.example.agenda.modeloDeVista.recyclerView.ContenedorTarea;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder> {

    ArrayList<ContenedorTarea> listaTareasCont;
    private RVClickInterface rvci;

    public AdaptadorRV(ArrayList<ContenedorTarea> listaTareas, RVClickInterface rvci) {
        this.listaTareasCont = listaTareas;
        this.rvci = rvci;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cont_tarea, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContenedorTarea itemActual = listaTareasCont.get(position);

        Currency c = Currency.getInstance(Locale.getDefault());

        holder.lblMonedaCont.setText(c.getSymbol());
        holder.icoPrio.setImageResource(itemActual.getIcoPrio());
        holder.icoBorrar.setImageResource(itemActual.getIcoBorrar());
        holder.lblNomTareaCont.setText(itemActual.getTextNomTarea());
        holder.lblDescripcionCont.setText(itemActual.getTextDescripcion());
        holder.lblFechaCont.setText(itemActual.getTextFecha());
        holder.lblCosteCont.setText(itemActual.getTextCoste());

    }

    @Override
    public int getItemCount() {
        return listaTareasCont.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icoPrio, icoBorrar;
        TextView lblNomTareaCont, lblDescripcionCont, lblCosteCont, lblMonedaCont, lblFechaCont;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            icoPrio = itemView.findViewById(R.id.icoPrio);
            icoBorrar = itemView.findViewById(R.id.icoBorrar);
            lblNomTareaCont = itemView.findViewById(R.id.lblNomTareaCont);
            lblDescripcionCont = itemView.findViewById(R.id.lblDescripcionCont);
            lblCosteCont = itemView.findViewById(R.id.lblCosteCont);
            lblMonedaCont = itemView.findViewById(R.id.lblMonedaCont);
            lblFechaCont = itemView.findViewById(R.id.lblFechaCont);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rvci.onItemClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rvci.onLongItemClick(getAdapterPosition());

                    return true;
                }
            });

        }


    }
}
