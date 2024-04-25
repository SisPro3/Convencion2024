package com.abarrotescasavargas.convencion.Gerencia.Sugeridos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abarrotescasavargas.convencion.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSugeridos extends RecyclerView.Adapter<AdapterSugeridos.ViewHolder> {
    private Context context;
    private List<Productos> listaProductos;
    private List<Productos> listaProductosFiltrados;

    public AdapterSugeridos(Context context, List<Productos> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.listaProductosFiltrados = new ArrayList<>(listaProductos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sugeridos_ficha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Productos producto = listaProductosFiltrados.get(position);

        holder.mes.setText(producto.getMes());
        holder.claveSugeridos.setText(producto.getClave());
        holder.existenciaSugeridos.setText(String.valueOf(producto.getExistencia()));
        holder.sugeridoSugeridos.setText(String.valueOf(producto.getSugerido()));
        holder.comentario.setText(String.valueOf(producto.getComentario()));
        setTextWithMarquee(holder.nombreSugeridos, producto.getNombre());

        if (producto.getMes().equalsIgnoreCase("MARZO")) {
            holder.fichaFondoSugeridos.setBackgroundResource(R.drawable.reunido);
        } else if (producto.getMes().equalsIgnoreCase("ABRIL")) {
            holder.fichaFondoSugeridos.setBackgroundResource(R.drawable.sin_reunir);
        }
    }

    private void setTextWithMarquee(TextView textView, String text) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setSelected(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
        }
    }
    public void filterBySucursal(String selectedSucursal) {
        listaProductosFiltrados.clear();

        if (selectedSucursal.isEmpty()) {
            listaProductosFiltrados.addAll(listaProductos);
        } else {
            for (Productos producto : listaProductos) {
                if (producto.getNombre().toLowerCase().contains(selectedSucursal.toLowerCase()) || producto.getClave().toLowerCase().contains(selectedSucursal.toLowerCase())) {
                    listaProductosFiltrados.add(producto);
                }
            }
        }

        notifyDataSetChanged();
    }



    public void filtrarPorMes(String mes) {
        listaProductosFiltrados.clear();
        if (TextUtils.isEmpty(mes)) {
            listaProductosFiltrados.addAll(listaProductos);
        } else {
            for (Productos producto : listaProductos) {
                if (producto.getMes().equalsIgnoreCase(mes)) {
                    listaProductosFiltrados.add(producto);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaProductosFiltrados.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView claveSugeridos, nombreSugeridos, existenciaSugeridos, sugeridoSugeridos,mes,comentario;
        LinearLayout fichaFondoSugeridos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fichaFondoSugeridos = itemView.findViewById(R.id.fichaFondoSugeridos);
            claveSugeridos = itemView.findViewById(R.id.claveSugeridos);
            mes = itemView.findViewById(R.id.mes);
            nombreSugeridos = itemView.findViewById(R.id.nombreSugeridos);
            existenciaSugeridos = itemView.findViewById(R.id.existenciaSugeridos);
            sugeridoSugeridos = itemView.findViewById(R.id.sugeridoSugeridos);
            comentario=itemView.findViewById((R.id.comentariosSugeridos));
        }
    }
}
