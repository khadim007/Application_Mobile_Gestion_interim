package com.example.projet_mobile.Controler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projet_mobile.Modele.Accueil;
import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.R;

import java.util.ArrayList;

public class RechercheActivity extends AppCompatActivity implements toolbar {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    SharedPreferences sharedPreferences;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private static int nbr = 1;

    private Accueil accueil;
    private ListView listView;
    private TextView affErreur;

    private Button bouttonRecherche;
    public Button bouttonPartager;
    public Button bouttonConsulter;
    public Button bouttonCandidater;

    private String recherche;
    private String type;
    private String specialite;
    private String lieu;

    private EditText editRecherche;
    private Spinner editType;
    private EditText editSpecialite;
    private EditText editLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
        recherche();
    }

    private void getID(){
        editRecherche = findViewById(R.id.editRecherche);
        editType = findViewById(R.id.editType);
        editSpecialite = findViewById(R.id.editSpecialite);
        editLieu = findViewById(R.id.editLieu);
        affErreur = findViewById(R.id.affError);

        listView = findViewById(R.id.idListView);
        bouttonRecherche = findViewById(R.id.bouttonRecherche);

        ImageView im = findViewById(R.id.imRecherche);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click() {
        bouttonRecherche.setOnClickListener(v -> {
            type = editType.getSelectedItem().toString();
            specialite = editSpecialite.getText().toString();
            lieu = editLieu.getText().toString();
            exec();
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            setupSpeechRecognizer();
        }
    }

    private void recherche() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        specialite = intent.getStringExtra("specialite");
        lieu = intent.getStringExtra("lieu");
        if(type != null && specialite != null && lieu != null)
            exec();
    }

    public void click2(String id, String nom, String annonce) {
        String role = sharedPreferences.getString("role", "");
        Intent intent = null;
        if("partager".equals(nom)){
            if(role.equals("candidat")) {
                intent = new Intent( RechercheActivity.this, PartageActivity.class);
                intent.putExtra("id", Integer.parseInt(id));
            }else{
                intent = new Intent( RechercheActivity.this, AuthentificationActivity.class);
            }
        }else if("consulter".equals(nom)){
            intent = new Intent( RechercheActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
        }else if("candidater".equals(nom)){
            if(role.equals("candidat")){
                intent = new Intent( RechercheActivity.this, CandidatureOffreActivity.class);
                intent.putExtra("id", Integer.parseInt(id));
                intent.putExtra("nom", annonce);
            }else {
                intent = new Intent( RechercheActivity.this, AuthentificationActivity.class);
            }
        }
        startActivity(intent);
    }

    private void exec(){
        accueil = new Accueil(type, specialite, lieu);
        accueil.recupDonnes(this, 1, new Accueil.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
            @Override
            public void onEmpty() {
                affichageEmpty();
            }
        });
    }

    private void affichage(){
        affErreur.setTextSize(26);
        affErreur.setText(accueil.donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, accueil.donnes);
        listView.setAdapter(annonces);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AnnonymeRecherche1", accueil.donnes[0][11]);
        editor.putString("AnnonymeRecherche2", accueil.donnes[0][7]);
        editor.putString("AnnonymeRecherche3", accueil.donnes[0][8]);
        editor.commit();
    }

    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    private void affichageEmpty(){affErreur.setText("Aucune annonce est trouvée !!"); affErreur.setTextSize(16);}

    public void onEnvoyer1Click(View view) {
        recherche = editRecherche.getText().toString();
        if(!recherche.isEmpty()){
            exec2();
        }
    }

    private void exec2(){
        accueil = new Accueil(recherche);
        accueil.recupDonnes(this, 2, new Accueil.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
            @Override
            public void onEmpty() {
                affichageEmpty();
            }
        });
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}





    //--------------------------------------------Vocale-------------------------------
    public void onVocaleClick(View view) {
        if(nbr == 1){
            nbr++;
            startSpeechRecognition();
        }else{
            nbr = 1;
            stopSpeechRecognition();
            recherche = editRecherche.getText().toString();
            if(!recherche.isEmpty()){
                exec2();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupSpeechRecognizer();
            } else {
                Toast.makeText(this, "La permission d'enregistrement audio a été refusée.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (resultList != null && !resultList.isEmpty()) {
                    String transcribedText = resultList.get(0);
                    editRecherche.setText(transcribedText);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {}
            @Override
            public void onEvent(int i, Bundle bundle) {}
            @Override
            public void onReadyForSpeech(Bundle bundle) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float v) {}
            @Override
            public void onBufferReceived(byte[] bytes) {}
            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int errorCode) {
                String errorMessage;
                switch (errorCode) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        errorMessage = "Erreur d'enregistrement audio.";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        errorMessage = "Erreur côté client.";
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        errorMessage = "Permissions insuffisantes.";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        errorMessage = "Erreur de réseau.";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        errorMessage = "Temps d'attente de réseau dépassé.";
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        errorMessage = "Aucun résultat trouvé.";
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        errorMessage = "Le service est occupé.";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        errorMessage = "Erreur côté serveur.";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        errorMessage = "Aucune entrée audio détectée.";
                        break;
                    default:
                        errorMessage = "Erreur inconnue.";
                        break;
                }
                Toast.makeText(RechercheActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    private void startSpeechRecognition() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(recognizerIntent);
            affErreur.setText("Enregistrement audio en cours...");
        }
    }

    private void stopSpeechRecognition() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            affErreur.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}