package com.abarrotescasavargas.convencion.Gerencia.Reuniones;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.convencion.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterReuniones extends RecyclerView.Adapter<AdapterReuniones.ViewHolder> {

    private List<ObjEmpresa> reuniones;
    private List<ObjEmpresa> reunionesFiltradas;
    private int parametroFiltrado = -1;
    private LinearLayout fichaFondo;
    private Context context;

    public AdapterReuniones(List<ObjEmpresa> reuniones, Context context) {
        this.reuniones = reuniones;
        this.reunionesFiltradas = new ArrayList<>(reuniones);
        this.context = context;
    }

    public void setParametroFiltrado(int parametroFiltrado) {
        this.parametroFiltrado = parametroFiltrado;
        filtrarReuniones();
    }

    private void filtrarReuniones() {
        reunionesFiltradas.clear();

        for (ObjEmpresa reunion : reuniones) {
            if (parametroFiltrado == -1 || Integer.parseInt(reunion.getStatus()) == parametroFiltrado) {
                reunionesFiltradas.add(reunion);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reuniones_ficha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObjEmpresa reunion = reunionesFiltradas.get(position);

        holder.nombreTextView.setText(reunion.getProveedor());
        holder.diasTextView.setText(reunion.getName());

        // Configura el fondo según el valor de status
        int status = Integer.parseInt(reunion.getStatus());
        Drawable background = getBackgroundForStatus(status);
        holder.fichaFondo.setBackground(background);

        // Inicia la marquesina en las TextView
        holder.nombreTextView.setSelected(true);
        holder.diasTextView.setSelected(true);
    }


    private Drawable getBackgroundForStatus(int status) {
        switch (status) {
            case 0:
                return ContextCompat.getDrawable(context, R.drawable.sin_reunir);
            case 1:
                return ContextCompat.getDrawable(context, R.drawable.reunido);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return reunionesFiltradas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout fichaFondo;
        TextView nombreTextView, diasTextView, existenciaTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.Nombre);
            diasTextView = itemView.findViewById(R.id.dias);
            fichaFondo=itemView.findViewById(R.id.fichaFondo);
        }
    }
}
