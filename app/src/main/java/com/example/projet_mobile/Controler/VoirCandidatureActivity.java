package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.R;

public class VoirCandidatureActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Candidature candidature;
    Intent intent;
    String role;
    int id;

    public Button bouttonConsulterLet;
    public Button bouttonConsulter;
    public Button bouttonModifier;
    public Button bouttonSupprimer;
    public Button bouttonContacter;

    private ListView listView;
    private TextView affErreur;
    private TextView textTitre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_candidature);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");
        intent = getIntent();

        getID();
        getDonnes();
    }

    private void getID(){
        affErreur = findViewById(R.id.textErreur);
        textTitre = findViewById(R.id.textTitre);
        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void getDonnes(){
        int a = intent.getIntExtra("annonce", 0);
        candidature = new Candidature();
        candidature.annonce = a;
        candidature.recupDonnesCand(this, new Candidature.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {affichageEmpty();}
        });
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        String n = intent.getStringExtra("nomAnnonce");
        textTitre.setText(n);
        affErreur.setText(candidature.donnes.length + " candidatures");
        listView.setVisibility(View.VISIBLE);
        Candidature.CandidatureAdapter candidatures = new Candidature.CandidatureAdapter(this, this, candidature.donnes);
        listView.setAdapter(candidatures);
    }

    @SuppressLint("SetTextI18n")
    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    @SuppressLint("SetTextI18n")
    private void affichageEmpty(){affErreur.setText("Aucune candidature est trouvée !!"); affErreur.setTextSize(16); listView.setVisibility(View.GONE);}

    public void click2(String id, String candidat, String nom) {
        Intent intent;
        if("consulter".equals(nom)){
            intent = new Intent( VoirCandidatureActivity.this, AffichePDFActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
            intent.putExtra("fichier", "cv");
            startActivity(intent);
        }else if("consulter let".equals(nom)){
            intent = new Intent( VoirCandidatureActivity.this, AffichePDFActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
            intent.putExtra("fichier", "lettre");
            startActivity(intent);
        }else if("en cours acceptees".equals(nom) || "en cours non acceptees".equals(nom)){
            candidature.id = Integer.parseInt(id);
            candidature.modifierEtat(this, nom, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {getDonnes();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {}
            });
        }else if("repondre".equals(nom)){
            intent = new Intent( VoirCandidatureActivity.this, ContactActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
            intent.putExtra("candidat", candidat);
            intent.putExtra("partie", "voirCandidature");
            startActivity(intent);
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}