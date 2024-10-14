package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.example.deezheart.R;

public class ChooseType extends AppCompatActivity {

    private Button mDecouverte;
    private Button mHabituel;
    private Integer mBPMValueMIN;
    private Integer mBPMValueMAX;
    private String applicationID = "426042";



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_type);
        mDecouverte = findViewById(R.id.Decouverte);
        mHabituel = findViewById(R.id.Habituel);
        getSupportActionBar().hide();


        //Récupération des BPM max et min
        Integer BPMmin = 0;
        Integer BPMmax = 0;
        Intent accesWaiting = getIntent();

        if (accesWaiting != null) {
            if (accesWaiting.hasExtra("BPMmin")) {
                BPMmin = accesWaiting.getIntExtra("BPMmin", 0);
            }
            if (accesWaiting.hasExtra("BPMmax")) {
                BPMmax = accesWaiting.getIntExtra("BPMmax", 0);
            }
        }

        // Récupération faite !!

        mBPMValueMIN = BPMmin;
        mBPMValueMAX = BPMmax;

        final com.deezer.sdk.network.connect.DeezerConnect deezerConnect = new DeezerConnect(this, applicationID);

        // Vérification si la personne n'est pas déjà identifié
        final SessionStore sessionStore = new SessionStore();

        //final String test = deezerConnect.getCurrentUser().toString();

        if (sessionStore.restore(deezerConnect, this)) {
            Toast.makeText(this, "Tu es déja connecté", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ChooseType.this, "oui", Toast.LENGTH_LONG).show();
                //sessionStore.save(deezerConnect, GameActivity.this);

                // Launch the Home activity
                //Intent intent = new Intent(GameActivity.this, MainActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onException(final Exception exception) {
                Toast.makeText(ChooseType.this, "Deezer error",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(ChooseType.this, "Annulation de la connexion",
                        Toast.LENGTH_LONG).show();
            }
        };















        mDecouverte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                deezerConnect.authorize(ChooseType.this, permissions, listener);

                    //choixMusique("Decouverte");
            }
        });

        mHabituel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale
                choixMusique("Habituel");
            }
        });
    }


    private void choixMusique(String methode) {

        // Redirection
        if (methode == "Decouverte") {
            recuperationFlow(mBPMValueMIN, mBPMValueMAX);
        }

        if (methode == "Habituel") {
            //RecuperationFlow(mBPMValueMIN, mBPMValueMAX);
        }
    }

    private void recuperationFlow(Integer min, Integer max) {



    }
}
