package com.example.chatapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.example.chatapp.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PresenterGoogleAuth {

    private FirebaseUser user;

    private Authorizations view;

    private ActivityResultLauncher<Intent> signInLauncher;

    private boolean result_reg;

    public PresenterGoogleAuth(Authorizations view, ActivityResultLauncher<Intent> signInLauncher) {
        this.view = view;
        this.signInLauncher = signInLauncher;
    }

    public void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == -1) {
            // Successfully signed in
            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
            String info = getInfoUser();
            //Toast.makeText(this, info, Toast.LENGTH_LONG).show();
            view.showMess(info);
            Log.i("MyBase", info);

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            if (response != null) {
                view.showError("createUserWithEmail:failure" + response.getError().toString() );
                Log.w("MyBase", "createUserWithEmail:failure", response.getError());
            } else {
                 view.showError("Error");
            }
        }
    }

    public void signIn() {
          List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    public void signOut(Context ctx){
        AuthUI.getInstance()
                .signOut(ctx)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Toast.makeText(view, "Logged out", Toast.LENGTH_LONG).show();
                            view.getOutGoogle("Logged out");
                        }
                    }
                });
    }

    public boolean getCurUser() {
        result_reg = false;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            result_reg = true;
        }
        return result_reg;
    }
    public String getInfoUser() {
        String result = "user is null!";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            result = user.getEmail();
        }
        return result;
    }

}
