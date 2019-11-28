package com.danito.p_agendaavanzada;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VistaContactos extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private Adaptador adaptador;
    private OnClickItemListener clickItemListener;
    private OnImageClickListener imageClickListener;
    private ArrayList<Contacto> contactos;
    private SwipeDetector swipeDetector;
    private int indiceListaPulsado;

    private final int COD_ACTIVITY_EDITAR = 1;
    private final int COD_ACTIVITY_ADD = 2;
    private final int COD_ELEGIR_IMAGEN = 3;
    private final int COD_TOMAR_FOTO = 4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.vista_contactos, container, false);


        swipeDetector = new SwipeDetector();
        cargarDatos();
        adaptador = new Adaptador(contactos);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        adaptador.setOnTouchListener(swipeDetector);
        adaptador.setOnClickListener(this);

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Contacto contacto = contactos.get(recyclerView.getChildAdapterPosition(v));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Quieres eliminar el contacto de " + contacto.getNombre() + "?");
                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactos.remove(contacto);
                        recyclerView.setAdapter(adaptador);
                    }
                });
                builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                return false;
            }
        });

        adaptador.setImageClickListener(new OnImageClickListener() {
            @Override
            public void onImageClick(final Contacto contacto) {
                imageClickListener.onImageClick(contacto);
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        indiceListaPulsado = recyclerView.getChildAdapterPosition(v);
        Contacto contacto = contactos.get(recyclerView.getChildAdapterPosition(v));
        if (swipeDetector.swipeDetected()) {
            switch (swipeDetector.getAction()) {
                case LR:
                    llamarContacto(contacto);
                    break;
                case RL:
                    enviarMensaje(contacto);
                    break;
            }
        } else {
            clickItemListener.OnSelectedItemListener(contacto);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            clickItemListener = (OnClickItemListener) context;
            imageClickListener = (OnImageClickListener) context;
        } catch (ClassCastException e) {}
    }

    private void llamarContacto(final Contacto d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Llamar a " + d.getNombre() + "?");
        builder.setPositiveButton("LLAMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (d.getTelefono().isEmpty()) {
                    Toast.makeText(getContext(), "El contacto no tiene teléfono", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + d.getTelefono()));
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void enviarMensaje(final Contacto d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Enviar mensaje a " + d.getNombre() + "?");
        builder.setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (d.getCorreo().isEmpty()) {
                    Toast.makeText(getContext(), "El contacto no tiene correo electrónico", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.fromParts("mailto", d.getCorreo(), null));
                    Intent chooser = Intent.createChooser(intent, "Enviar mensaje...");
                    startActivity(chooser);
                }
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void editarDatos(Contacto d) {
        Intent i = new Intent(getContext(), AccionContacto.class);
        i.putExtra("contacto", d);
        startActivityForResult(i, COD_ACTIVITY_EDITAR);
    }

    private void cargarDatos() {
        contactos = new ArrayList<>();
        contactos.add(new Contacto("Iván", "Gallego", "601245789", "yo@yo.com"));
        contactos.add(new Contacto("Gallego", "Iván", "658984512", "yo@yo.com"));
        contactos.add(new Contacto("Daniel", "Acabado", "", ""));
        contactos.add(new Contacto("Estodorne", "Ideas", "658497415", "yo@yo.com"));
        contactos.add(new Contacto("Carlos", "Apellido", "684974523", ""));
        contactos.add(new Contacto("Juan", "Mastodonte", "", "yo@yo.com"));
        contactos.add(new Contacto("Marcos", "Calatraba", "784569815", ""));
        contactos.add(new Contacto("David", "Muñoz", "745123698", "yo@yo.com"));
        contactos.add(new Contacto("Sandra", "López", "696952356", "yo@yo.com"));
        contactos.add(new Contacto("Andrea", "García", "787878787", "yo@yo.com"));
        contactos.add(new Contacto("Ainhoa", "García", "", "yo@yo.com"));
    }
}
