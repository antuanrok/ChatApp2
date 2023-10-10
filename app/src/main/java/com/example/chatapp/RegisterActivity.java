package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.presenter.Authorizations;
import com.example.chatapp.presenter.PresenterAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements Authorizations{

    //private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewLog;
    private PresenterAuth presenterAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reg_google, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_signin_google) {
            regGoogle();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.editTextRegEmail);
        editTextPass = findViewById(R.id.editTextRegPassw);
        textViewLog = findViewById(R.id.textViewHaveAkk);
        presenterAuth = new PresenterAuth(this);
        textViewLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickReg(View view) {
        String email= editTextEmail.getText().toString().trim();
        String passw= editTextPass.getText().toString().trim();
        if (!email.isEmpty() && !passw.isEmpty()) {
              presenterAuth.registerNewUser(email,passw);
        }
    }


    public void showActivity(boolean res) {
        if (res) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

        }
    }

    public void regGoogle()
    {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("regGoogle",true);
        startActivity(intent);
    }

    @Override
    public void getOutGoogle(String text) {

    }

    @Override
    public void showError(String mess) {

    }

    @Override
    public void showMess(String mess) {

    }

}