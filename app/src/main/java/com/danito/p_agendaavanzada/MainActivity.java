package com.danito.p_agendaavanzada;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.danito.p_agendaavanzada.interfaces.OnAddContact;
import com.danito.p_agendaavanzada.interfaces.OnClickItemListener;
import com.danito.p_agendaavanzada.interfaces.OnEditContact;
import com.danito.p_agendaavanzada.interfaces.OnFabClicked;
import com.danito.p_agendaavanzada.interfaces.OnImageClickListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements
        OnClickItemListener, OnEditContact, OnAddContact, OnFabClicked {

    public ArrayList<Contacto> contactos;
    private int indiceListaPulsado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarDatos();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new VistaContactos(contactos));
        transaction.commit();
    }

    // El parámetro índice, me sirve para saber cuál es el dato que quiero eliminar al pulsar el botón aceptar
    @Override
    public void OnClickItemListener(Contacto contacto, int i) {
        indiceListaPulsado = i;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AccionContacto fragment = new AccionContacto();
        Bundle args = new Bundle();
        args.putParcelable("contacto", contacto);
        fragment.setArguments(args);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnEditContact(Contacto contacto) {
        contactos.set(indiceListaPulsado, contacto);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VistaContactos fragment = new VistaContactos(contactos);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnAddContact(Contacto contacto) {
        contactos.add(contacto);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VistaContactos fragment = new VistaContactos(contactos);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnFabClicked() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new AccionContacto());
        transaction.addToBackStack(null);
        transaction.commit();
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
