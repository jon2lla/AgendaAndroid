package com.example.agenda.modeloDeVista.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class AdaptadorContenedor extends RecyclerView.Adapter<AdaptadorContenedor.ContenedorViewHolder> {
    private ArrayList<ContenedorTarea> listaCont;

    private OnItemClickListener mOnItemClickListener;
    private OnLongItemClickListener mOnLongItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public interface OnLongItemClickListener {
        void ItemLongClicked(View v, int position);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.mOnLongItemClickListener = onLongItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public AdaptadorContenedor(ArrayList<ContenedorTarea> listaCont){
        this.listaCont = listaCont;
    }


    @Override
    public ContenedorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cont_tarea, parent, false);
        ContenedorViewHolder cvh = new ContenedorViewHolder(v, this.mOnItemClickListener, this.mOnLongItemClickListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final ContenedorViewHolder holder, final int position) {
        ContenedorTarea itemActual = listaCont.get(position);

        holder.icoPrio.setImageResource(itemActual.getIcoPrio());
        holder.icoBorrar.setImageResource(itemActual.getIcoBorrar());
        holder.lblNomTareaCont.setText(itemActual.getTextNomTarea());
        holder.lblDescripcionCont.setText(itemActual.getTextDescripcion());
        holder.lblFechaCont.setText(itemActual.getTextFecha());
        holder.lblCosteCont.setText(itemActual.getTextCoste());
    }

    @Override
    public int getItemCount() {
        return listaCont.size();
    }

    public class ContenedorViewHolder extends RecyclerView.ViewHolder{

        public ImageView icoPrio, icoBorrar;
        public TextView lblNomTareaCont, lblDescripcionCont, lblFechaCont, lblCosteCont, lblMonedaCont;

        public ContenedorViewHolder(View itemView, final OnItemClickListener clickListener, final OnLongItemClickListener longClickListener) {
            super(itemView);

            Currency c = Currency.getInstance(Locale.getDefault());

            icoPrio = itemView.findViewById(R.id.icoPrio);
            lblNomTareaCont = itemView.findViewById(R.id.lblNomTareaCont);
            lblDescripcionCont = itemView.findViewById(R.id.lblDescripcionCont);
            lblFechaCont = itemView.findViewById(R.id.lblFechaCont);
            lblCosteCont = itemView.findViewById(R.id.lblCosteCont);
            lblMonedaCont = itemView.findViewById(R.id.lblMonedaCont);
            lblMonedaCont.setText(c.getSymbol());
            icoBorrar = itemView.findViewById(R.id.icoBorrar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });

            icoBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onDeleteClick(position);
                        }
                    }
                }
            });

            icoPrio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        longClickListener.ItemLongClicked(v, position);
                    }
                    return true;
                }
            });
        }
    }
}
