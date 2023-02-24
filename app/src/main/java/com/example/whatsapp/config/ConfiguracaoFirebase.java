package com.example.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth auth;
    private static StorageReference firebaseStorage;

    public static DatabaseReference getFirebaseDatabase() {
        if (database == null) {//verifica se tem uma referencia, caso contrario atribui a ela.
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static StorageReference getFirebaseStorage () {
        if (firebaseStorage == null) {//verifica se tem uma referencia, caso contrario atribui a ela.
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return firebaseStorage;
    }

}
