  package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

  public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail,loginPsw;
    private Button loginBtn;
    private TextView loginQues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getApplicationContext(),"Login page opened ",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginQues = findViewById(R.id.LoginPageQuestion);
        loginBtn = findViewById(R.id.buttonLogin);
        loginEmail = findViewById(R.id.LoginEmailAddress);
        loginPsw = findViewById(R.id.LoginPassword);


        loginQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Registrationactivity.class);
                startActivity(intent);
            }
        });
    }
}