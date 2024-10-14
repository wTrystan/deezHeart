package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.example.deezheart.R;

public class GameActivity extends AppCompatActivity {

    private Button mConnexion;
    private String applicationID = "426042";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mConnexion = (Button) findViewById(R.id.activity_connexion);
        final DeezerConnect deezerConnect = new DeezerConnect(this, applicationID);

        // Vérification si la personne n'est pas déjà identifié
        final SessionStore sessionStore = new SessionStore();

        //final String test = deezerConnect.getCurrentUser().toString();

        if (sessionStore.restore(deezerConnect, this)) {
            Toast.makeText(this, "Tu es déja connecté", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Pas encore connecté", Toast.LENGTH_LONG).show();
    }

        // The set of Deezer Permissions needed by the app
        final String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.DELETE_LIBRARY,
                Permissions.LISTENING_HISTORY,
                Permissions.OFFLINE_ACCESS};

// The listener for authentication events
        final DialogListener listener = new DialogListener() {
            @Override
            public void onComplete(final Bundle values) {
                // store the current authentication info
                Toast.makeText(GameActivity.this, "oui", Toast.LENGTH_LONG).show();
                //sessionStore.save(deezerConnect, GameActivity.this);

                // Launch the Home activity
                //Intent intent = new Intent(GameActivity.this, MainActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onException(final Exception exception) {
                Toast.makeText(GameActivity.this, "Deezer error",
                       Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(GameActivity.this, "Annulation de la connexion",
                        Toast.LENGTH_LONG).show();
            }
        };

// Launches the authentication process
        mConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deezerConnect.authorize(GameActivity.this, permissions, listener);
            }
        });
    }
}