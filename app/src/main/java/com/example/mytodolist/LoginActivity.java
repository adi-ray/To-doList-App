  package com.example.mytodolist;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

  public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail,loginPsw;
    private Button loginBtn;
    private TextView loginQues;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;



    private ImageView google;
      GoogleSignInOptions gso;
      GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {







        Toast.makeText(getApplicationContext(),"Login page opened ",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginQues = findViewById(R.id.LoginPageQuestion);
        loginBtn = findViewById(R.id.buttonLogin);
        loginEmail = findViewById(R.id.LoginEmailAddress);
        loginPsw = findViewById(R.id.LoginPassword);
        mAuth = FirebaseAuth.getInstance();
        google = findViewById(R.id.imageView3);
        loader = new ProgressDialog(this);




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.setMessage("Login in progress");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                String email = loginEmail.getText().toString().trim();
                String password = loginPsw.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    loginEmail.setError("Email is required");
                    loader.dismiss();
                    return;
                }
                if(TextUtils.isEmpty("Password is required")){
                    loader.dismiss();
                return;}
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);

                            finish();
                            loader.dismiss();
                        }
                        else{
                            String error = task.getException().toString();
                            Toast.makeText(LoginActivity.this,error,Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }
                    }
                });
            }

        });






        loginQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Registrationactivity.class);
                startActivity(intent);

            }
        });


//        ggogle signup

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this , gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });



    }

      private void SignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,100);

      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          if(requestCode==100){
              Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
              try {
                  task.getResult(ApiException.class);
                  homeActivity();
              } catch (ApiException e) {
                  e.printStackTrace();
                  Toast.makeText(this,"An error occurred",Toast.LENGTH_SHORT).show();
              }

          }
      }

      private void homeActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
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