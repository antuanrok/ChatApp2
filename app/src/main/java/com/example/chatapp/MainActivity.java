package com.example.chatapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.adapters.MessageAdapters;
import com.example.chatapp.pojo.Message;
import com.example.chatapp.presenter.Authorizations;
import com.example.chatapp.presenter.MessageListView;
import com.example.chatapp.presenter.Presenter;
import com.example.chatapp.presenter.PresenterAuth;
import com.example.chatapp.presenter.PresenterGoogleAuth;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MessageListView, Authorizations {

    private RecyclerView recyclerViewMess;
    private MessageAdapters adapter;

    private EditText editTextMess;

    private ImageView imageViewSendMess;
    private String author;

    // private FirebaseAuth mAuth;

    private Presenter presenter;

    private PresenterAuth presenterAuth;

    private PresenterGoogleAuth presenterGoogleAuth;

    private ActivityResultLauncher<Intent> signInLauncher;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_sign_out) {
          //  mAuth.signOut();
            if (presenterAuth.getCurUser()) {
                presenterAuth.signOut();
          //  } else if (presenterGoogleAuth.getCurUser()) {
       //         presenterGoogleAuth.signOut(this);
            }
            getOut();
         //  signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMess = findViewById(R.id.editTextMessage);
        imageViewSendMess = findViewById(R.id.imageViewSendMess);
        recyclerViewMess = findViewById(R.id.recyclerViewChat);

        //mAuth = FirebaseAuth.getInstance();

        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        Toast.makeText(MainActivity.this,"bebebe", Toast.LENGTH_SHORT).show();
                        presenterGoogleAuth.onSignInResult(result);
                    }
                }
        );

        presenter = new Presenter(this);
        presenterAuth = new PresenterAuth(this);
        presenterGoogleAuth = new PresenterGoogleAuth(this, signInLauncher);

        adapter = new MessageAdapters();
        recyclerViewMess.setLayoutManager(new LinearLayoutManager(this));
        adapter.setMessages(new ArrayList<>());
        recyclerViewMess.setAdapter(adapter);
        author = "Andrey";

        recyclerViewMess.scrollToPosition(adapter.getItemCount() - 1);

        imageViewSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepMessToSend();
            }
        });

        if (presenterAuth.getCurUser()) {
            Toast.makeText(this, "Logged", Toast.LENGTH_SHORT).show();
        } else {
            Bundle extras=getIntent().getExtras();
            if (extras != null) {
                if (extras.getBoolean("regGoogle")) {
                    regGoogle();
                    return;
                }
            }
            getOut();
        }
    }

    private void prepMessToSend() {
        String mess = editTextMess.getText().toString().trim();
        long date = System.currentTimeMillis();
        if (!mess.isEmpty()) {
            presenter.sendMessage(author, mess, date);
            recyclerViewMess.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void showData(List<Message> messages) {
        adapter.setMessages(messages);
    }

    @Override
    public void showError(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void completeLoad(String text) {
        editTextMess.setText("");
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getOut() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    @Override
    public void showActivity(boolean res) {
       // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(intent);
    }


    public void regGoogle() {
        Toast.makeText(this,"Выбрана регистрация Гугл", Toast.LENGTH_SHORT).show();
       // presenterGoogleAuth.signIn();
    }

    @Override
    public void getOutGoogle(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMess(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }
}