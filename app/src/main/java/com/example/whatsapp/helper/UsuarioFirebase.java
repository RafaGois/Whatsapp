package com.example.whatsapp.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static String getIdUser () {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        return Base64Custom.codificarBase64( email );
    }

    public static FirebaseUser getUsuarioAtual () {

        return ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
    }

    public static boolean atualizarFotoUsuario (Uri uri) {

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

            user.updateProfile( profileChangeRequest ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao adicionar foto de perfil");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
