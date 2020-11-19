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

    private OnItemClickListener listener;
    private onLongItemClickListener mOnLongItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public interface onLongItemClickListener {
        void ItemLongClicked(View v, int position);
    }

    public void setOnLongItemClickListener(onLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public static class ContenedorViewHolder extends RecyclerView.ViewHolder{

        public ImageView icoPrio, icoBorrar;
        public TextView lblNomTareaCont, lblDescripcionCont, lblFechaCont, lblCosteCont, lblMonedaCont;

        public ContenedorViewHolder(View itemView, final OnItemClickListener listener) {
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
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });


            icoBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdaptadorContenedor(ArrayList<ContenedorTarea> listaCont){
        this.listaCont = listaCont;
    }

    @Override
    public ContenedorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cont_tarea, parent, false);
        ContenedorViewHolder cvh = new ContenedorViewHolder(v, this.listener);
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
        holder.icoPrio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.ItemLongClicked(v, position);
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCont.size();
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



        }


    }
}
