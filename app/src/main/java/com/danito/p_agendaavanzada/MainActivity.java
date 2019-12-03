package com.danito.p_agendaavanzada;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.danito.p_agendaavanzada.interfaces.OnAddContact;
import com.danito.p_agendaavanzada.interfaces.OnClickItemListener;
import com.danito.p_agendaavanzada.interfaces.OnEditContact;
import com.danito.p_agendaavanzada.interfaces.OnFabClicked;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        OnClickItemListener, OnEditContact, OnAddContact, OnFabClicked {

    public ArrayList<Contacto> contactos;
    private int indiceListaPulsado;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarDatos();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        final VistaContactos vistaContactos = new VistaContactos(contactos);
        transaction.add(R.id.fragment_container, vistaContactos);
        transaction.commit();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.familia_menu_option:
                        vistaContactos.adaptador.getFilter().filter("Familia");
                        break;
                    case R.id.amigos_menu_option:
                        vistaContactos.adaptador.getFilter().filter("Amigo");
                        break;
                    case R.id.trabajo_menu_option:
                        vistaContactos.adaptador.getFilter().filter("Trabajo");
                        break;
                    case R.id.todos_menu_option:
                        vistaContactos.adaptador.getFilter().filter(null);
                        break;
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
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
