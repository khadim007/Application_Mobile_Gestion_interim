package com.example.projet_mobile.Controler;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CandidatureSpontaneActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Candidature candidature;
    Annonce annonce;
    String json;
    String role;
    int id;

    private TextView affErreur;
    //------------------------------
    CardView card;
    private EditText editMetier;
    private EditText editEmployeur;
    private EditText editDateDebut;
    private EditText editVille;
    private Button bouttonEnvoyer;
    //------------------------------
    LinearLayout layoutAE;
    CardView cardA;
    private Button bouttonFichier;
    private TextView textFichier;
    CardView cardA2;
    private EditText editLien;
    //------------------------------
    CardView cardE;
    private EditText editNomE;
    private EditText editDescriptionE;
    private EditText editDescriptionEEn;
    private EditText editRenumerationE;
    private EditText editDateDebutE;
    private EditText editMetierE;
    private EditText editVilleE;
    private EditText editDureeE;
    private EditText editMots_clesE;
    private EditText editCategorieE;
    private Button bouttonEnvoyerE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_spontane);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        getID();
        aff();
        click();
        getDonnes();
    }

    private void getID(){
        affErreur = findViewById(R.id.affError);
        //------------------------------------------------------
        card = findViewById(R.id.card);
        editMetier = findViewById(R.id.editMetier);
        editEmployeur = findViewById(R.id.editEmployeur);
        editDateDebut = findViewById(R.id.editDateDebut);
        editVille = findViewById(R.id.editVille);
        bouttonEnvoyer = findViewById(R.id.buttonValider);
        //------------------------------------------------------
        layoutAE = findViewById(R.id.layoutE);
        cardA = findViewById(R.id.cardA);
        bouttonFichier = findViewById(R.id.buttonFichier);
        textFichier = findViewById(R.id.textFichier);
        cardA2 = findViewById(R.id.cardA2);
        editLien = findViewById(R.id.editLien);
        //------------------------------------------------------
        cardE = findViewById(R.id.cardE);
        editNomE = findViewById(R.id.editNomE);
        editDescriptionE = findViewById(R.id.editDescriptionE);
        editDescriptionEEn = findViewById(R.id.editDescriptionEEn);
        editRenumerationE = findViewById(R.id.editRenumerationE);
        editDateDebutE = findViewById(R.id.editDateDebutE);
        editMetierE = findViewById(R.id.editMetierE);
        editVilleE = findViewById(R.id.editVilleE);
        editDureeE = findViewById(R.id.editDureeE);
        editMots_clesE = findViewById(R.id.editMot_clesE);
        editCategorieE = findViewById(R.id.editCategorieE);
        bouttonEnvoyerE = findViewById(R.id.buttonValiderE);

        ImageView im = findViewById(R.id.imCandidature);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void aff(){
        if("employeur".equals(role)) {card.setVisibility(View.GONE); cardA.setVisibility(View.GONE); cardA2.setVisibility(View.GONE);}
        else if("candidat".equals(role)) layoutAE.setVisibility(View.GONE);
        else {card.setVisibility(View.GONE);}
    }

    private void click(){
        bouttonEnvoyer.setOnClickListener(v -> exec());
        bouttonEnvoyerE.setOnClickListener(v -> execE());
        bouttonFichier.setOnClickListener(v -> fichier());
    }

    private void exec(){
        String metier = editMetier.getText().toString();
        String employeur = editEmployeur.getText().toString();
        String dateDebut = editDateDebut.getText().toString();
        String ville = editVille.getText().toString();
        if (metier.isEmpty() && employeur.isEmpty() && dateDebut.isEmpty() && ville.isEmpty()) {
            Toast.makeText(this, "Au mois un champs doit etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        candidature = new Candidature(id, candidat.prenom, candidat.nom, candidat.nationalite, candidat.dateNais, metier, employeur, dateDebut, ville);
        candidature.ajouterSpontane(this, new Candidature.VolleyCallback() {
            @Override
            public void onSuccess() {Intent intent = new Intent( CandidatureSpontaneActivity.this, RechercheActivity.class);startActivity(intent);}
            @Override
            public void onError() {}
            @Override
            public void onEmpty() {}
        });
    }

    private void execE(){
        String nomE = editNomE.getText().toString();
        String descriptionE = editDescriptionE.getText().toString();
        String descriptionEEn = editDescriptionEEn.getText().toString();
        String renumerationE = editRenumerationE.getText().toString();
        String dateDebutE = editDateDebutE.getText().toString();
        String metierE = editMetierE.getText().toString();
        String villeE = editVilleE.getText().toString();
        String dureeE = editDureeE.getText().toString();
        String mots_clesE = editMots_clesE.getText().toString();
        String categorieE = editCategorieE.getText().toString();

        if (nomE.isEmpty() || descriptionE.isEmpty() || descriptionEEn.isEmpty() || renumerationE.isEmpty() || dateDebutE.isEmpty() || metierE.isEmpty() || villeE.isEmpty() || dureeE.isEmpty() || mots_clesE.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent obligatoirement etre remplis sauf categorie !!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        int ident = intent.getIntExtra("idE", 0);
        if(role.equals("employeur")) annonce = new Annonce(nomE, descriptionE, descriptionEEn, String.valueOf(id), renumerationE, dateDebutE, metierE, villeE, dureeE, mots_clesE, categorieE, null);
        else annonce = new Annonce(nomE, descriptionE, descriptionEEn, null, renumerationE, dateDebutE, metierE, villeE, dureeE, mots_clesE, categorieE, String.valueOf(id));
        if(ident != 0){
            annonce.id = ident;
            annonce.modifier(this, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {Intent intent = new Intent( CandidatureSpontaneActivity.this, GestionOffreActivity.class); startActivity(intent);}
                @Override
                public void onError() {}
                @Override
                public void onEmpty() {}
            });
        }else {
            annonce.ajouter(this, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(CandidatureSpontaneActivity.this, AccueilActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onError() {}
                @Override
                public void onEmpty() {}
            });
        }
    }

    public void onEnvoyerFichierClick(View view) {
        if(json == null)Toast.makeText(this, "Veillez choisir un fichier !!", Toast.LENGTH_SHORT).show();
        else sendJson(json);
    }
    // via un web service REST
    public void onEnvoyerLienClick(View view) {
        String lien = editLien.getText().toString();
        if(lien.isEmpty()){
            Toast.makeText(this, "Veillez saisir le lien !!", Toast.LENGTH_SHORT).show();
        }else {
            new GetOffersAsyncTask().execute();
        }
    }

    private void sendJson(String json){
        annonce = new Annonce();
        annonce.agence = String.valueOf(id);
        annonce.ajouterAgence(this, json, new Annonce.VolleyCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(CandidatureSpontaneActivity.this, AccueilActivity.class);
                startActivity(intent);
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onError() {affErreur.setText("Le fichier choisi n'est pas bien structure !!");}
            @Override
            public void onEmpty() {}
        });
    }

    private void fichier(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Sélectionnez un fichier JSON"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Erreur. Veillez reesayer !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDonnes(){
        Intent intent = getIntent();
        int ident = intent.getIntExtra("idE", 0);
        if(ident != 0){
            annonce = new Annonce(ident);
            annonce.recupDonnes(this, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError("Error. Veillez reesayer !!");}
                @Override
                public void onEmpty() {}
            });
        }
        if("candidat".equals(role)){
            candidat = new CandidatInscrit(id);
            candidat.getDonnesCand(this, new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {}
                @Override
                public void onError() {affichageError(candidat.affiche);}
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        editNomE.setText(annonce.nom);
        editDescriptionE.setText(annonce.description);
        editDescriptionEEn.setText(annonce.descriptionEn);
        editRenumerationE.setText(annonce.remuneration);
        editDateDebutE.setText(annonce.date_debut);
        editMetierE.setText(annonce.metier);
        editVilleE.setText(annonce.ville);
        editDureeE.setText(annonce.duree);
        editMots_clesE.setText(annonce.mot_cles);
    }
    private void affichageError(String s){affErreur.setText(s);}

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String fileName = null;
            if (uri != null) {
                try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
            }
            textFichier.setText(fileName);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                json = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetOffersAsyncTask extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @Override
        protected Void doInBackground(Void... voids) {
            String endpointUrl = editLien.getText().toString();

            try {
                URL url = new URL(endpointUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();

                    json = stringBuilder.toString();
                    sendJson(json);
                    Log.d(TAG, "Response: " + json);
                } else {
                    Log.e(TAG, "Error: " + responseCode);
                }
                connection.disconnect();
            } catch (IOException e) {
                affichageError("Lien ou fichier incorrect !!");
                e.printStackTrace();
            }
            return null;
        }
        private void affichageError(String errorMessage) {
            runOnUiThread(() -> affErreur.setText(errorMessage));
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {}
    public void onCompteClick(View view) {onCompteClick(this);}
}