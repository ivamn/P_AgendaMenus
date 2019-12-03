package com.danito.p_agendaavanzada;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danito.p_agendaavanzada.interfaces.OnAddContact;
import com.danito.p_agendaavanzada.interfaces.OnEditContact;
import com.danito.p_agendaavanzada.pojo.Contacto;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AccionContacto extends Fragment implements View.OnClickListener {

    private OnEditContact editContact;
    private OnAddContact addContact;
    private EditText editNombre, editApellido, editTelefono, editCorreo;
    private Button aceptarButton;
    private Bitmap bitmap;
    private Contacto contacto;
    private ImageView imageView;
    private RadioButton radioButtonFamilia, radioButtonTrabajo, radioButtonAmigos;
    private final int EDITAR = 0;
    private final int ADD = 1;
    private final int COD_ELEGIR_IMAGEN = 2;
    private final int COD_TOMAR_FOTO = 3;
    private int proposito;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.accion_contacto, container, false);
        view = v;
        Bundle args = getArguments();

        if (args != null) {
            contacto = args.getParcelable("contacto");
        }
        proposito = contacto == null ? ADD : EDITAR;

        imageView = v.findViewById(R.id.profile_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getContext(), v);
                pop.getMenuInflater().inflate(R.menu.menu_foto, pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.camera:
                                tomarFoto();
                                break;
                            case R.id.galeria:
                                elegirImagen();
                                break;
                            case R.id.borrar:
                                contacto.setImagen(null);
                                break;
                        }
                        return true;
                    }
                });
                pop.show();
            }
        });

        radioButtonAmigos = v.findViewById(R.id.radio_amigo);
        radioButtonFamilia = v.findViewById(R.id.radio_familia);
        radioButtonTrabajo = v.findViewById(R.id.radio_trabajo);
        editNombre = v.findViewById(R.id.editNombre);
        editApellido = v.findViewById(R.id.editApellido);
        editTelefono = v.findViewById(R.id.editTelefono);
        editCorreo = v.findViewById(R.id.editCorreo);
        aceptarButton = v.findViewById(R.id.aceptar);
        aceptarButton.setOnClickListener(this);

        if (proposito == EDITAR) {
            if (contacto.getImagen() != null){
                imageView.setImageBitmap(contacto.getImagen());
            }
            editNombre.setText(contacto.getNombre());
            editApellido.setText(contacto.getApellido());
            editTelefono.setText(contacto.getTelefono());
            editCorreo.setText(contacto.getCorreo());
            if (contacto.isTrabajo()) radioButtonTrabajo.setChecked(true);
            else if (contacto.isAmigo()) radioButtonAmigos.setChecked(true);
            else if (contacto.isFamilia()) radioButtonFamilia.setChecked(true);
        }

        return v;
    }

    public Contacto generarNuevoContacto() {
        Contacto d = new Contacto();
        d.setAmigo(radioButtonAmigos.isChecked());
        d.setTrabajo(radioButtonTrabajo.isChecked());
        d.setFamilia(radioButtonFamilia.isChecked());
        d.setImagen(bitmap);
        d.setNombre(editNombre.getText().toString());
        d.setApellido(editApellido.getText().toString());
        d.setTelefono(editTelefono.getText().toString());
        d.setCorreo(editCorreo.getText().toString());
        return d;
    }

    public void editarContacto() {
        contacto.setImagen(bitmap);
        contacto.setAmigo(radioButtonAmigos.isChecked());
        contacto.setTrabajo(radioButtonTrabajo.isChecked());
        contacto.setFamilia(radioButtonFamilia.isChecked());
        contacto.setNombre(editNombre.getText().toString());
        contacto.setApellido(editApellido.getText().toString());
        contacto.setTelefono(editTelefono.getText().toString());
        contacto.setCorreo(editCorreo.getText().toString());
    }

    @Override
    public void onClick(View v) {
        if (nombreCorrecto(editNombre.getText().toString())) {
            Intent intent = new Intent();
            if (proposito == EDITAR) {
                editarContacto();
                editContact.OnEditContact(contacto);
            } else {
                Contacto nuevoContacto = generarNuevoContacto();
                intent.putExtra("contacto", nuevoContacto);
                addContact.OnAddContact(nuevoContacto);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            editContact = (OnEditContact) context;
            addContact = (OnAddContact) context;
        } catch (ClassCastException e) {
        }
    }

    private boolean nombreCorrecto(String nombre) {
        if (nombre.isEmpty()) {
            editNombre.setError("El nombre no debe estar vacío.");
            return false;
        }
        return true;
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, COD_TOMAR_FOTO);
        }
    }

    private void elegirImagen() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, COD_ELEGIR_IMAGEN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COD_ELEGIR_IMAGEN && resultCode == RESULT_OK && data != null) {
            bitmap = bitmapFromUri(data.getData());
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == COD_TOMAR_FOTO && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getContext(), "Se ha cancelado la operación", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap bitmapFromUri(Uri uri) {
        ImageView imageViewTemp = new ImageView(getContext());
        imageViewTemp.setImageURI(uri);
        BitmapDrawable d = (BitmapDrawable) imageViewTemp.getDrawable();
        return d.getBitmap();
    }
}
