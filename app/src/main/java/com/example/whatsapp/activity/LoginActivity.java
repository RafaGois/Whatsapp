package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null) {
            abrirTelaPrincipal();
        }
    }

    public void logarUsuario (Usuario usuario) {

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    abrirTelaPrincipal();
                } else {

                    String execucao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        execucao = "Usuário não está cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        execucao = "Email ou senha não correspondem a um usuário cadastrado.";
                    } catch (Exception e) {
                        execucao = "Erro ao cadastrar usuário." + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, execucao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void validarAutenticacaoUsuario(View view) {

        String email = inputEmail.getText().toString();
        String senha = inputSenha.getText().toString();

        if (!email.isEmpty()) {

            if (!senha.isEmpty()) {

                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                logarUsuario(usuario);

            } else {
                Toast.makeText(this, "Informe a senha.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe o email.", Toast.LENGTH_SHORT).show();
        }

    }

    public void clicouCadastro (View view) {
        Intent intent = new Intent(this,CadastroActivity.class);
        startActivity(intent);
    }

    public void abrirTelaPrincipal ( ) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}