package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText txtEmailLogin, txtPasswordLogin;
    Button btnLogin;
    ProgressBar pgbLogin;
    TextView txtNavigateSignUp, txtReset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        pgbLogin = findViewById(R.id.pgbLogin);
        txtNavigateSignUp = findViewById(R.id.txtNavigateSignUp);
        txtReset = findViewById(R.id.txtReset);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void txtNavigateSignUpOnClick(View view) {
        startActivity(new Intent(Login.this, SignUp.class));
    }

    public void btnLoginOnClick(View view) {
        String email = txtEmailLogin.getText().toString().trim();
        String password = txtPasswordLogin.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            txtEmailLogin.setError("Email is required");
            return;
        }

        if(TextUtils.isEmpty(password)){
            txtPasswordLogin.setError("Password is required");
            return;
        }

        if(password.length()<6){
            txtPasswordLogin.setError("Password must include at least 6 characters");
            return;
        }

        pgbLogin.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this,"Login Success",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this,MainActivity.class));
                } else {
                    Toast.makeText(Login.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                pgbLogin.setVisibility(View.GONE);
            }
        });
    }

    public void txtResetOnClick(View view) {
        EditText resetMail = new EditText(Login.this);
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(Login.this);
        passwordResetDialog.setTitle("Reset Password?");
        passwordResetDialog.setMessage("Enter your email to receive reset link");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail = resetMail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Login.this, "Reset link send to your email",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error! Reset link not sent" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        passwordResetDialog.create().show();
    }

    public void btnChooseOnClick(View view) {
        startActivity(new Intent(view.getContext(),GoogleLogin.class));
    }
}