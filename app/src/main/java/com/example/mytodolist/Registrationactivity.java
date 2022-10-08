package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registrationactivity extends AppCompatActivity {
    private EditText ResEmail,ResPsw;
    private Button ResBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        ResBtn = findViewById(R.id.buttonRegister);
        ResEmail = findViewById(R.id.RegisterEmailAddress);
        ResPsw = findViewById(R.id.RegisterPassword);

    }
}