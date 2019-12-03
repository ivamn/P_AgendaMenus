package com.danito.p_agendaavanzada;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danito.p_agendaavanzada.interfaces.OnImageClickListener;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter
        implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, Filterable {
    private ArrayList<Contacto> contactos, contactosCompletos;
    private View.OnClickListener clickListener;
    private OnImageClickListener imageClickListener;
    private View.OnLongClickListener longClickListener;
    private View.OnTouchListener touchListener;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence seleccion) {
            ArrayList<Contacto> datosFiltrados = new ArrayList<>();
            if (seleccion == null || seleccion.length() == 0) {
                datosFiltrados.addAll(contactosCompletos);
            } else if (seleccion.equals("Todo")) {
                datosFiltrados.addAll(contactosCompletos);
            } else {
                for (Contacto c : contactos) {
                    if (seleccion.equals("Amigo") && c.isAmigo()) {
                        datosFiltrados.add(c);
                    } else if (seleccion.equals("Trabajo") && c.isTrabajo()) {
                        datosFiltrados.add(c);
                    } else if (seleccion.equals("Familia") && c.isFamilia()) {
                        datosFiltrados.add(c);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = datosFiltrados;
            results.count = datosFiltrados.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactos.clear();
            contactos.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public Adaptador(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
        contactosCompletos = new ArrayList<>(contactos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entrada_agenda, parent, false);
        v.setOnLongClickListener(this);
        v.setOnClickListener(this);
        v.setOnTouchListener(this);
        Holder h = new Holder(v);
        h.setImageClickListener(new OnImageClickListener() {
            @Override
            public void onImageClick(Contacto contacto, View view) {
                imageClickListener.onImageClick(contacto, v);
            }
        });
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Holder) holder).bind(contactos.get(position));
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        if (listener != null) {
            this.clickListener = listener;
        }
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        if (listener != null) {
            this.longClickListener = listener;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null) {
            longClickListener.onLongClick(v);
        }
        return false;
    }

    public void setOnTouchListener(View.OnTouchListener listener) {
        if (listener != null) {
            this.touchListener = listener;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (touchListener != null) {
            touchListener.onTouch(v, event);
        }
        return false;
    }

    public void setImageClickListener(OnImageClickListener listener) {
        if (listener != null) {
            imageClickListener = listener;
        }
    }
}
