package com.danito.p_agendaavanzada;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.danito.p_agendaavanzada.Util.Layout;
import com.danito.p_agendaavanzada.interfaces.*;
import com.danito.p_agendaavanzada.pojo.Contacto;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        OnClickItemListener, OnEditContact, OnAddContact, OnFabClicked {

    public ArrayList<Contacto> contactos;
    private int indiceListaPulsado;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Layout layout;
    private VistaContactos vistaContactos;
    private OnRecyclerUpdated onRecyclerUpdated;

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout = Layout.GRID;
        } else {
            layout = Layout.LINEAR;
        }

        replaceFragment();

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
                        vistaContactos.adaptador.getFilter().filter("Todo");
                        break;
                }
                onRecyclerUpdated.onRecyclerUpdated(layout);
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        vistaContactos = new VistaContactos(contactos, layout);
        onRecyclerUpdated = vistaContactos;
        transaction.add(R.id.fragment_container, vistaContactos);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // El parámetro índice, me sirve para saber cuál es el dato que quiero eliminar al pulsar el botón aceptar
    @Override
    public void onClickItemListener(Contacto contacto, int i) {
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
    public void onEditContact(Contacto contacto) {
        contactos.set(indiceListaPulsado, contacto);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VistaContactos fragment = new VistaContactos(contactos, layout);
        onRecyclerUpdated = fragment;
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddContact(Contacto contacto) {
        contactos.add(contacto);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VistaContactos fragment = new VistaContactos(contactos, layout);
        onRecyclerUpdated = fragment;
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

        @Override
        public void onFabClicked() {
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
            case R.id.linear_option_menu:
                layout = Layout.LINEAR;
                onRecyclerUpdated.onRecyclerUpdated(Layout.LINEAR);
                break;
            case R.id.grid_option_menu:
                layout = Layout.GRID;
                onRecyclerUpdated.onRecyclerUpdated(Layout.GRID);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }
}
