package com.example.mytodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registrationactivity extends AppCompatActivity {
    private EditText ResEmail,ResPsw;
    private Button ResBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registrationactivity);
        ResBtn = findViewById(R.id.buttonRegister);
        ResEmail = findViewById(R.id.RegisterEmailAddress);
        ResPsw = findViewById(R.id.RegisterPassword);
        loader = new ProgressDialog(this);
        ResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.setMessage("Registration in progress");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                String email = ResEmail.getText().toString().trim();
                String password = ResPsw.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    ResEmail.setError("Email is required");
                    loader.dismiss();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    ResPsw.setError("Password is required");
                    loader.dismiss();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Registrationactivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            loader.dismiss();
                        }
                        else{
                            String error = task.getException().toString();
                            Toast.makeText(Registrationactivity.this,error,Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }
                    }

                });
            }
        });
    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .setNegativeButton("No" , null)
                .show();
    }

}