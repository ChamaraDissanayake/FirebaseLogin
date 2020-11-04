package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    EditText txtName, txtEmail, txtPassword, txtTel;
    Button btnSubmit;
    TextView txtNavigateLogin;
    ProgressBar pgbSignUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtNavigateLogin = findViewById(R.id.txtNavigateLogin);
        pgbSignUp = findViewById(R.id.pgbSignUp);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUp.this,MainActivity.class));
            finish();
        }
    }


    public void btnSubmitOnClick(View view) {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            txtEmail.setError("Email is required");
            return;
        }

        if(TextUtils.isEmpty(password)){
            txtPassword.setError("Password is required");
            return;
        }

        if(password.length()<6){
            txtPassword.setError("Password must include at least 6 characters");
            return;
        }

        pgbSignUp.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUp.this,"Please check your Email to verify your account",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this,"Something went wrong: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                    Toast.makeText(SignUp.this,"User Created",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                } else {
                    Toast.makeText(SignUp.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                pgbSignUp.setVisibility(View.GONE);
            }
        });
    }

    public void txtNavigateLoginOnClick(View view) {
        startActivity(new Intent(SignUp.this,Login.class));
    }
}