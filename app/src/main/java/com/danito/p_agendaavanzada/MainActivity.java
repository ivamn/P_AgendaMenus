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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements
        OnClickItemListener, OnImageClickListener, OnEditContact, OnAddContact {

    public ArrayList<Contacto> contactos;
    private int indiceListaPulsado;
    private RecyclerView recyclerView;
    private Adaptador adaptador;
    private SwipeDetector swipeDetector;
    private Contacto contactoTemp;

    private final int COD_ACTIVITY_EDITAR = 1;
    private final int COD_ACTIVITY_ADD = 2;
    private final int COD_ELEGIR_IMAGEN = 3;
    private final int COD_TOMAR_FOTO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarDatos();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new VistaContactos(contactos));
        transaction.commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContacto();
            }
        });
    }

    private void mostrarPerfil(final Contacto contacto) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View vista = inflater.inflate(R.layout.perfil_contacto, null);
        TextView nombrePerfil = vista.findViewById(R.id.nombrePerfil);
        ImageView imagenPerfil = vista.findViewById(R.id.imagenPerfil);
        if (contacto.getImagen() != null) {
            imagenPerfil.setImageBitmap(contacto.getImagen());
        } else {
            imagenPerfil.setImageResource(R.drawable.ic_default);
        }

        nombrePerfil.setText(contacto.getNombre());
        builder.setView(vista);
        final AlertDialog dialog = builder.create();
        ImageView imagenCamara = vista.findViewById(R.id.botonCamara);
        imagenCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto(contacto, dialog);
            }
        });
        ImageView imagenGaleria = vista.findViewById(R.id.botonGaleria);
        imagenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImagen(contacto, dialog);
            }
        });
        dialog.show();
    }

    private void elegirImagen(Contacto contacto, AlertDialog dialog) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, COD_ELEGIR_IMAGEN);
            contactoTemp = contacto;
        }
        dialog.cancel();
    }

    private void tomarFoto(Contacto contacto, AlertDialog dialog) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, COD_TOMAR_FOTO);
            contactoTemp = contacto;
        }
        dialog.cancel();
    }

    private void llamarContacto(final Contacto d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Llamar a " + d.getNombre() + "?");
        builder.setPositiveButton("LLAMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (d.getTelefono().isEmpty()) {
                    Toast.makeText(MainActivity.this, "El contacto no tiene teléfono", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + d.getTelefono()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Enviar mensaje a " + d.getNombre() + "?");
        builder.setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (d.getCorreo().isEmpty()) {
                    Toast.makeText(MainActivity.this, "El contacto no tiene correo electrónico", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(this, AccionContacto.class);
        i.putExtra("contacto", d);
        startActivityForResult(i, COD_ACTIVITY_EDITAR);
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
    public void onImageClick(Contacto contacto) {
        mostrarPerfil(contacto);
    }

    private void addContacto() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new AccionContacto());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COD_ACTIVITY_EDITAR &&
                resultCode == RESULT_OK && data != null) {
            Contacto d = data.getParcelableExtra("contacto");
            contactos.set(indiceListaPulsado, d);
        } else if (requestCode == COD_ACTIVITY_ADD &&
                resultCode == RESULT_OK && data != null) {
            Contacto d = data.getParcelableExtra("contacto");
            contactos.add(d);
        } else if (requestCode == COD_ELEGIR_IMAGEN && resultCode == RESULT_OK && data != null) {
            Uri rutaImagen = data.getData();
            contactoTemp.setImagen(bitmapFromUri(rutaImagen));
        } else if (requestCode == COD_TOMAR_FOTO && resultCode == RESULT_OK && data != null) {
            contactoTemp.setImagen((Bitmap) data.getExtras().get("data"));
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Se ha cancelado la operación", Toast.LENGTH_LONG).show();
        }
        recyclerView.setAdapter(adaptador);
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

    private Bitmap bitmapFromUri(Uri uri) {
        ImageView imageViewTemp = new ImageView(this);
        imageViewTemp.setImageURI(uri);
        BitmapDrawable d = (BitmapDrawable) imageViewTemp.getDrawable();
        return d.getBitmap();
    }
}
