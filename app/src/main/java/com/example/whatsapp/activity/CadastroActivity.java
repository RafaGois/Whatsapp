package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Base64;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.inputNomeCadastro);
        campoEmail = findViewById(R.id.inputEmailCadastro);
        campoSenha = findViewById(R.id.inputSenhaCadastro);
    }

    public void cadastrarUsuarioFirebase(Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Conta salva com sucesso!", Toast.LENGTH_SHORT).show();

                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                    finish();

                    try {

                        String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                        usuario.setUid(identificadorUsuario);
                        usuario.salvar();

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {

                    String execucao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        execucao = "Digite uma senha mais forte.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        execucao = "Por favor, difgite um email válido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        execucao = "Conta já cadastrada.";
                    } catch (Exception e) {
                        execucao = "Erro ao cadastrar usuário." + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, execucao, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void validarCadastroUsuario(View view) {

        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if (!nome.isEmpty()) {

            if (!email.isEmpty()) {

                if (!senha.isEmpty()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    cadastrarUsuarioFirebase(usuario);
                } else {
                    Toast.makeText(this, "Informe a senha.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Informe o email.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe o nome.", Toast.LENGTH_SHORT).show();
        }
    }

    public void volta (View view) {
        finishAndRemoveTask();
    }
}