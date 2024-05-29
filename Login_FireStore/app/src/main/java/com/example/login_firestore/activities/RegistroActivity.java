package com.example.login_firestore.activities;

import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import android.util.Base64;

import androidx.activity.result.ActivityResultLauncher;

import com.example.login_firestore.databinding.ActivityRegistroBinding;
import com.example.login_firestore.utilities.Constants;
import com.example.login_firestore.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
public class RegistroActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private PreferenceManager preferenceManager;
    private String encodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }
    private void setListeners(){
        binding.txtIniciaSesion.setOnClickListener(v -> onBackPressed());
        binding.btnRegistrate.setOnClickListener(v -> {
            if(ValidarDetallesRegistro()){
                Registrarse();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tomarImagen.launch(intent);
        });
    }
    private void muestraToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }
    private void Registrarse(){
        Cargando(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.etNombre.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.etCorreo.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.etContrasena.getText().toString());
        user.put(Constants.KEY_IMAGE, encodeImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Cargando(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.etNombre.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodeImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    Cargando(false);
                    muestraToast(exception.getMessage());
                });
    }
    private String codificarImagen(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> tomarImagen = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imagenUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imagenUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.FotoPerfil.setImageBitmap(bitmap);
                            encodeImage = codificarImagen(bitmap);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
    private Boolean ValidarDetallesRegistro(){
        if(encodeImage == null){
            muestraToast("Seleciona una imagen de perfil");
            return false;
        } else if (binding.etNombre.getText().toString().trim().isEmpty()) {
            muestraToast("Ingresa tu nombre");
            return false;
        } else if (binding.etCorreo.getText().toString().trim().isEmpty()) {
            muestraToast("Ingresa tu correo electronico");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etCorreo.getText().toString()).matches()) {
            muestraToast("Ingresa un correo electronico valido");
            return false;
        } else if (binding.etContrasena.getText().toString().trim().isEmpty()) {
            muestraToast("Ingresa una contraseña");
            return false;
        } else if (binding.etConfirmaContrasena.getText().toString().trim().isEmpty()) {
            muestraToast("Tu contraseña y la contraseña confirmada debe ser la misma");
            return false;
        } else {
            return true;
        }
    }
    private void Cargando(Boolean isLoading){
        if (isLoading){
            binding.btnRegistrate.setVisibility(View.INVISIBLE);
            binding.BarraProgreso.setVisibility(View.VISIBLE);
        } else {
            binding.btnRegistrate.setVisibility(View.VISIBLE);
            binding.BarraProgreso.setVisibility(View.INVISIBLE);
        }
    }
}