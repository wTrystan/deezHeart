package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.deezheart.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;


public class WaitingScreen extends AppCompatActivity {

    private static final String CLIENT_ID = "f4583493c2e94ea1902a550d79ef8451";
    private static final String REDIRECT_URI = "com.example.deezheart://callback";
    private static final String TAG = "Spotify" + WaitingScreen.class.getSimpleName();
    private static final int REQUEST_CODE = 1337;
    public static String AUTH_TOKEN = "AUTH_TOKEN";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "";
    private String jsontest;

    private SpotifyAppRemote mSpotifyAppRemote;
    private Button mChooseTracks;
    private Button mConnexionSpotify;
    private String mNameUser;
    private ImageView mCover;
    private EditText mBPMValue;
    private TextView mBPMAffiche;
    private Button mPause;
    private Button mPreviousTrack;
    private Button mNextTrack;
    private TextView mTestValue;
    private TextView mAffichageQueue;
    private Button mPlaylistCalme;
    private Button mPlaylistSport;

    private String mUserID;
    public String mPlaylistID = "";
    private ArrayList mListePlaylistID;
    private String identifiantFinal;
    private String playlistID;
    private String IDFinal;
    private ArrayList mListeIDTrackFinale;
    private Integer mBPMValueMIN;
    private Integer mBPMValueMAX;
    Integer nbChansons;
    private SeekBar mSeekBar;
    private TextView mSeekBarValue;
    private Integer valueSeekBar;
    Integer nombreQueue = 0;
    TextView mListeQueue;
    ArrayList mListeIDTrack;
    ArrayList mListeFinale;
    ArrayList mListeGlobaleID;
    ArrayList mListeGlobaleNom;
    Integer nombrePlaylist;
    Integer bouclePlaylist;
    Integer nombreChansons;
    Integer testBoucleJ;
    Integer connecter;
    Integer methode;
    pl.droidsonroids.gif.GifImageView mGif;
    ProgressBar mLoading;
    TextView mTexteWaiting;
    Boolean stopProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);
        getSupportActionBar().hide();

        //Récupération des BPM max et min
        Integer BPMmin = 0;
        Integer BPMmax = 0;
        Integer mMethode = 0;
        Intent accesWaiting = getIntent();

        if (accesWaiting != null) {
            if (accesWaiting.hasExtra("BPMmin")) {
                BPMmin = accesWaiting.getIntExtra("BPMmin", 0);
            }
            if (accesWaiting.hasExtra("BPMmax")) {
                BPMmax = accesWaiting.getIntExtra("BPMmax", 0);
            }
            if (accesWaiting.hasExtra("Methode")) {
                mMethode = accesWaiting.getIntExtra("Methode",1);
            }
        }

        // Récupération faite !!

        mBPMValueMIN = BPMmin;
        mBPMValueMAX = BPMmax;
        methode = mMethode;

        setContentView(R.layout.activity_waiting_screen);
        mGif = findViewById(R.id.gifChargement);
        mLoading = findViewById(R.id.my_progressBar);
        mTexteWaiting = findViewById(R.id.texteWaiting);
        getSupportActionBar().hide();
        String Separator = System.getProperty("line.separator");
        stopProgramme = false;


       // Détermination du loading Screen!
        if (methode==0){
            mGif.setImageResource(R.drawable.loadingparty);
        }
        if (methode==1){
            mGif.setImageResource(R.drawable.loadingzen);
        }
        if (methode==2){
            mGif.setImageResource(R.drawable.loadingsport);
        }
        if (methode==3){
            mGif.setImageResource(R.drawable.loadingparty);
        }

    }

    // Connexion avec Spotify App Remote
    @Override
    protected void onStart() {
        super.onStart();
        //if (!AUTH_TOKEN.equals("AUTH_TOKEN")) {
        openLoginWindow();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                        Log.d("connected","true");
                        Intent SpotifyConnect = new Intent(WaitingScreen.this, SpotifyConnect.class);
                        startActivity(SpotifyConnect);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
        //}
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
       //((TextView) findViewById(R.id.nameUser)).setText("Disconnect");
    }

    private void connected() {

        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(AUTH_TOKEN);
        SpotifyService spotify = api.getService();
        nombreQueue = 0;
        nombrePlaylist = 0;
        bouclePlaylist = 0;
        nombreChansons = 0;
        ArrayList mListeGlobaleID = new ArrayList();
        ArrayList mListeGlobaleNom = new ArrayList();
        ArrayList mListeFinale = new ArrayList();

        //Toast.makeText(WaitingScreen.this, "Le choix des chansons se fera entre "+Math.round(mBPMValueMIN)+ " et " +Math.round(mBPMValueMAX), Toast.LENGTH_LONG).show();

        // Récupère le nom d'utilisateur et son id dans la variable nomUtilisateur
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate UserPrivate, retrofit.client.Response response) {
                getPlaylistUtilisateurs();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });

    }

// Connexion avec Spotify Auth SDK

    private void openLoginWindow() {

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "streaming", "user-top-read", "user-read-recently-played", "user-library-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:

                    AUTH_TOKEN = response.getAccessToken();
                    //mSeekBarValue.setText(response.getAccessToken());

                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e(TAG, "Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d(TAG, "Auth result: " + response.getType());
            }
        }
    }

    // Formules pour récupérer les musiques

    private void tri(ArrayList ListeID, ArrayList ListeNom) {
        //on va tenter d'enlever les doublons

        ArrayList ListeBoucle = new ArrayList();

        for (int i=0; i<ListeID.size();i++) {
            ListeBoucle.add(ListeID.get(i).toString() + ListeNom.get(i).toString());
        }

        Set set = new HashSet();
        set.addAll(ListeBoucle);
        ArrayList nouvelleBoucle = new ArrayList(set);

        Collections.shuffle(nouvelleBoucle);
        getInformations(nouvelleBoucle);
    }

    protected void ajoutQueue(String ajoutID) {
        // Permet de rajouter la musique dans la queue
        //findViewById(R.id.gifChargement).setVisibility(View.INVISIBLE);
        Integer mTemp;
        mTemp = 0;
        if (mLoading.getProgress() == 100){
            mTemp = 1;
            stopProgramme = true;
            Log.d("ON","STOP TOUT");
        }
        mSpotifyAppRemote.getPlayerApi().queue(ajoutID);
        mLoading.setProgress(mLoading.getProgress()+10);
        mTexteWaiting.setText(mLoading.getProgress()/10+"/10 chansons trouvées");

        if (mLoading.getProgress() == 100 && mTemp == 0) {
            Intent accesPlayer = new Intent(WaitingScreen.this, SpotifyPlayer.class);
            accesPlayer.putExtra("Methode", 1);
            accesPlayer.putExtra("token", AUTH_TOKEN);
            startActivity(accesPlayer);}
        }

    private void GetUtilisateur() {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(AUTH_TOKEN);
        SpotifyService spotify = api.getService();
        nombreQueue = 0;
        nombrePlaylist = 0;
        bouclePlaylist = 0;
        nombreChansons = 0;
        ArrayList mListeGlobaleID = new ArrayList();
        ArrayList mListeGlobaleNom = new ArrayList();
        ArrayList mListeFinale = new ArrayList();


        // Récupère le nom d'utilisateur et son id dans la variable nomUtilisateur
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate UserPrivate, retrofit.client.Response response) {
                getPlaylistUtilisateurs();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });
    }

    private void getPlaylistUtilisateurs() {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(AUTH_TOKEN);
        SpotifyService spotify = api.getService();
        ArrayList<String> mListePlaylistID = new ArrayList();
        ArrayList<String> mListeGlobaleID = new ArrayList();
        ArrayList<String> mListeGlobaleNom = new ArrayList();
        ArrayList<String> mListeFinale = new ArrayList();
        final Integer[] nombreBoucle = {0};

        // Récupère les playlists de l'utilisateur dans la liste listeIDPlaylist
        spotify.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> listePlaylist, retrofit.client.Response response) {
                //récupération des playlists

                for (int i = 0; i < listePlaylist.items.size(); i++) {
                    mListePlaylistID.add(listePlaylist.items.get(i).id);
                    Log.d("playlist", listePlaylist.items.get(i).id);
                }

                if (methode==3) {
// Ajout de la playlist TOP 50 Mondial
                    mListePlaylistID.add("37i9dQZEVXbMDoHDwVN2tF");
                    // Ajout de la playlist TOP 50 FR
                    mListePlaylistID.add("37i9dQZEVXbIPWwFssbupI");
                }

                nombrePlaylist = listePlaylist.items.size();

                try {
                    getTrackInformations(mListePlaylistID, mListeGlobaleID, mListeGlobaleNom);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            };
            //mSpotifyAppRemote.getPlayerApi().play(mListeIDTrackFinale.get(0).toString());

            //Intent qui permet d'accéder à l'écran de chargement
            //Intent SpotifyWaitingScreen = new Intent(SpotifyConnect.this, WaitingScreen.class);
            //startActivity(SpotifyWaitingScreen);

            //Intent qui permet d'accéder au player
            //Intent spotifyPlayerIntent = new Intent(SpotifyConnect.this, SpotifyPlayer.class);
            //startActivity(spotifyPlayerIntent);
        });
    }

    private void getTrackInformations(ArrayList<String> ListeplaylistID, ArrayList<String> GigalisteglobaleID, ArrayList<String> GigalisteglobaleNom) throws InterruptedException {
        Integer bouclej = 0;
        Integer testboucle=0;
        testBoucleJ=0;

        for (int j = 0; j < ListeplaylistID.size(); j++) {
            bouclej++;
            // Récupère les chansons de la playlist
            OkHttpClient client = new OkHttpClient();
            Request myGetRequest = new Request.Builder()
                    .url("https://api.spotify.com/v1/playlists/" + ListeplaylistID.get(j) + "/tracks?market=FR&fields=items(track(id,name))&limit=100&offset=1")
                    .header("Authorization", "Bearer " + AUTH_TOKEN)
                    .build();

            //Integer finalBouclej = bouclej;
            Integer finalTaillePlaylist = ListeplaylistID.size();
            client.newCall(myGetRequest).enqueue(new com.squareup.okhttp.Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("Infos track", "Error");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    //le retour est effectué dans un thread différent

                    final String text = response.body().string();
                    //ArrayList mListeIDTrack = new ArrayList();

                    runOnUiThread(new Runnable() {
                        // truc naze où t'es obligé d'avoir l'API 19
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            try {
                                Integer boucleChiante;
                                String temp;
                                testBoucleJ++;
                                boucleChiante = testBoucleJ;
                                //Création du premier JSONObject
                                JSONObject obj = new JSONObject(text);
                                JSONArray objObject = obj.getJSONArray("items");

                                for (int i = 0; i < objObject.length(); i++) {
                                    GigalisteglobaleID.add(objObject.get(i).toString().substring(16, 38));
                                    temp = objObject.get(i).toString().substring(48);
                                    temp = temp.substring(0, temp.length()-3);
                                    GigalisteglobaleNom.add(temp);
                                    ((TextView) findViewById(R.id.affichageNombre)).setText("Recherche parmi les "+GigalisteglobaleID.size()+" titres");
                                }

                                if (boucleChiante == (finalTaillePlaylist)) {
                                    //Log.d("Taille liste", String.valueOf(Gigalisteglobale.size()));
                                    tri(GigalisteglobaleID,GigalisteglobaleNom);
                                }

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + text + "\"");
                            }
                        }
                    });

                }
            });
        }
    }

    private void getInformations(ArrayList<String> ListeTout) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(AUTH_TOKEN);
        SpotifyService spotify = api.getService();
        ArrayList mListeIDTrackFinale = new ArrayList();
        nombreQueue = 0;

        for (int i = 0; i < (ListeTout.size()); i++) {

            String titre = ListeTout.get(i).substring(22);
            // récupère les informations de la chanson ainsi que son BPM
            final boolean reponse;
            reponse = stopProgramme;

            if (reponse) {
                i = ListeTout.size();
            }

                spotify.getTrackAudioFeatures(ListeTout.get(i).substring(0, 22), new Callback<AudioFeaturesTrack>() {
                    @Override
                    public void success(AudioFeaturesTrack audioFeaturesTrack, retrofit.client.Response response) {
                        Float value = audioFeaturesTrack.tempo;
                        String nom = audioFeaturesTrack.uri;
                        Float dance = audioFeaturesTrack.danceability;
                        Float energy = audioFeaturesTrack.energy;
                        Float valence = audioFeaturesTrack.valence;
                        String valueString = value.toString();


                        // CHOIX BPM
                        if (methode == 0 && value <= mBPMValueMAX && value >= mBPMValueMIN && nombreQueue < 11) {
                            nombreQueue++;
                            String Separator = System.getProperty("line.separator");
                            // mAffichageQueue.setText(mAffichageQueue.getText() + Separator + Separator + titre + " - " + Math.round(value) + " BPM");
                            ajoutQueue(nom);
                            //Log.d("Infos", titre + ' ' + Math.round(value));
                        }
                        // CHOIX CALME
                        if (methode == 1 && energy < 0.4 && nombreQueue < 11) {
                            nombreQueue++;
                            String Separator = System.getProperty("line.separator");
                            //mAffichageQueue.setText(mAffichageQueue.getText() + Separator + Separator + titre + " - " + Math.round(value) + " BPM");
                            ajoutQueue(nom);
                            Log.d("Infos", titre + ' ' + Math.round(value));
                        }
                        // CHOIX SPORT
                        if (methode == 2 && energy > 0.8 && nombreQueue < 11) {
                            nombreQueue++;
                            String Separator = System.getProperty("line.separator");
                            //mAffichageQueue.setText(mAffichageQueue.getText() + Separator + Separator + titre + " - " + Math.round(value) + " BPM");
                            ajoutQueue(nom);
                            Log.d("Infos", titre + ' ' + Math.round(value));
                        }
                        // CHOIX SOIREE
                        if (methode == 3 && dance > 0.7 && energy > 0.6 && valence > 0.5 && nombreQueue < 11) {
                            nombreQueue++;
                            String Separator = System.getProperty("line.separator");
                            //mAffichageQueue.setText(mAffichageQueue.getText() + Separator + Separator + titre + " - " + Math.round(value) + " BPM");
                            ajoutQueue(nom);
                            Log.d("Infos", titre + ' ' + Math.round(value));
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
        }
    }
}

