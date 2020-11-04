package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnVerify;
    TextView txtVerify;
    FirebaseAuth firebaseAuth;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVerify = findViewById(R.id.btnVerify);
        txtVerify = findViewById(R.id.txtVerify);

//        userId = firebaseAuth.getCurrentUser().getUid();
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();

        if(!firebaseUser.isEmailVerified()){
            txtVerify.setVisibility(View.VISIBLE);
            btnVerify.setVisibility(View.VISIBLE);
        }
    }

    public void btnLogoutOnCreate(View view) {
        firebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,Login.class));
        finish();
    }

    public void btnVerifyOnClick(View view) {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(view.getContext(),"Please check your Email to verify your account",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(),"Something went wrong: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}