package com.danito.p_agendaavanzada;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AccionContacto extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

    private OnEditContact editContact;
    private TextInputEditText editNombre, editApellido, editTelefono, editCorreo;
    private TextInputLayout editNombreContenedor;
    private Button aceptarButton;
    private Contacto contactoRecibido;
    private final int EDITAR = 0;
    private final int ADD = 1;
    private int proposito;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.accion_contacto, container, false);
        view = v;
        Bundle args = getArguments();

        contactoRecibido = (Contacto) args.get("contacto");
        resetTint();
        proposito = contactoRecibido == null ? ADD : EDITAR;

        editNombre = v.findViewById(R.id.editNombre);
        editApellido = v.findViewById(R.id.editApellido);
        editTelefono = v.findViewById(R.id.editTelefono);
        editCorreo = v.findViewById(R.id.editCorreo);
        editNombreContenedor = v.findViewById(R.id.editNombreContenedor);
        aceptarButton = v.findViewById(R.id.aceptar);
        aceptarButton.setOnClickListener(this);
        editNombre.setOnFocusChangeListener(this);
        editApellido.setOnFocusChangeListener(this);
        editTelefono.setOnFocusChangeListener(this);
        editCorreo.setOnFocusChangeListener(this);

        if (proposito == EDITAR) {
            editNombre.setText(contactoRecibido.getNombre());
            editApellido.setText(contactoRecibido.getApellido());
            editTelefono.setText(contactoRecibido.getTelefono());
            editCorreo.setText(contactoRecibido.getCorreo());
        }

        return v;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Drawable d = null;
        ImageView icono = null;
        switch (v.getId()) {
            case R.id.editNombre:
                icono = view.findViewById(R.id.imagenNombre);
                d = icono.getDrawable();
                break;
            case R.id.editApellido:
                icono = view.findViewById(R.id.imagenApellido);
                d = icono.getDrawable();
                break;
            case R.id.editTelefono:
                icono = view.findViewById(R.id.imagenTelefono);
                d = icono.getDrawable();
                break;
            case R.id.editCorreo:
                icono = view.findViewById(R.id.imagenCorreo);
                d = icono.getDrawable();
                break;
        }
        if (d != null) {
            DrawableCompat.wrap(d);
            if (hasFocus) {
                DrawableCompat.setTint(d, ContextCompat.getColor(getContext(), R.color.colorAccent));
            } else {
                DrawableCompat.setTint(d, ContextCompat.getColor(getContext(), R.color.colorBaseIconos));
            }
        }
    }

    public Contacto generarNuevoContacto() {
        Contacto d = new Contacto();
        d.setNombre(editNombre.getText().toString());
        d.setApellido(editApellido.getText().toString());
        d.setTelefono(editTelefono.getText().toString());
        d.setCorreo(editCorreo.getText().toString());
        return d;
    }

    public void editarContacto() {
        contactoRecibido.setNombre(editNombre.getText().toString());
        contactoRecibido.setApellido(editApellido.getText().toString());
        contactoRecibido.setTelefono(editTelefono.getText().toString());
        contactoRecibido.setCorreo(editCorreo.getText().toString());
    }

    @Override
    public void onClick(View v) {
        if (nombreCorrecto(editNombre.getText().toString())) {
            Intent intent = new Intent();
            if (proposito == EDITAR) {
                editarContacto();
                editContact.OnEditContact(contactoRecibido);
            } else {
                Contacto nuevoContacto = generarNuevoContacto();
                intent.putExtra("contacto", nuevoContacto);
                editContact.OnEditContact(nuevoContacto);
            }
            resetTint();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            editContact = (OnEditContact) context;
        } catch (ClassCastException e){}
    }

    private boolean nombreCorrecto(String nombre) {
        if (nombre.isEmpty()) {
            editNombreContenedor.setError("El nombre no debe estar vacío.");
            return false;
        }
        return true;
    }

    private void resetTint() {
        ImageView iconoNombre = view.findViewById(R.id.imagenNombre);
        ImageView iconoApellido = view.findViewById(R.id.imagenApellido);
        ImageView iconoTelefono = view.findViewById(R.id.imagenTelefono);
        ImageView iconoCorreo = view.findViewById(R.id.imagenCorreo);
        ArrayList<Drawable> drawables = new ArrayList<>();
        drawables.add(iconoNombre.getDrawable());
        drawables.add(iconoApellido.getDrawable());
        drawables.add(iconoTelefono.getDrawable());
        drawables.add(iconoCorreo.getDrawable());
        for (Drawable d : drawables) {
            DrawableCompat.wrap(d);
            DrawableCompat.setTint(d, ContextCompat.getColor(getContext(), R.color.colorBaseIconos));
        }
    }


}
