package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class VoirAnnonceActivity extends AppCompatActivity implements toolbar {

    Annonce annonce;
    Employeur employeur;
    int id;
    SharedPreferences sharedPreferences;

    TextView affErreur;
    TextView textNom;
    TextView textRen;
    TextView textDat;
    TextView textMet;
    TextView textVil;
    TextView textDetails;
    TextView textDescription;
    TextView textMotCles;

    public Button bouttonPartager;
    public Button bouttonCandidater;
    public Button bouttonTraduire;
    public Button bouttonEnregistrer;
    public Button bouttonSimilaires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_annonce);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        getID();
        click();
        affichageDonnes();
    }

    private void getID(){
        affErreur = findViewById(R.id.textErreur);
        textNom = findViewById(R.id.textNom);
        textRen = findViewById(R.id.textRenumeration);
        textDat = findViewById(R.id.textDate);
        textMet = findViewById(R.id.textMetier);
        textVil = findViewById(R.id.textVille);
        textDetails = findViewById(R.id.textDetails);
        textDescription = findViewById(R.id.textDescription);
        textMotCles = findViewById(R.id.textMotCles);

        ImageView im = findViewById(R.id.imRecherche);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);

        bouttonPartager = findViewById(R.id.buttonPartager);
        bouttonCandidater = findViewById(R.id.buttonCandidater);
        bouttonTraduire = findViewById(R.id.buttonTraduire);
        bouttonEnregistrer = findViewById(R.id.buttonEnregistrer);
        bouttonSimilaires = findViewById(R.id.buttonSimilaires);
    }

    private void click() {
        int ident = sharedPreferences.getInt("id", 0);
        bouttonPartager.setOnClickListener(v -> {
            if(ident == 0){
                Intent intent = new Intent( VoirAnnonceActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        });
        bouttonCandidater.setOnClickListener(v -> {
            if(ident == 0){
                Intent intent = new Intent( VoirAnnonceActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        });
        bouttonTraduire.setOnClickListener(v -> {
            textDescription.setText(annonce.donnes[12]);
        });
        bouttonEnregistrer.setOnClickListener(v -> {
            if(ident == 0){
                Intent intent = new Intent( VoirAnnonceActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        });
        bouttonSimilaires.setOnClickListener(v -> {
            Intent intent = new Intent( VoirAnnonceActivity.this, RechercheActivity.class);
            intent.putExtra("type", annonce.donnes[11]);
            intent.putExtra("specialite", annonce.donnes[7]);
            intent.putExtra("lieu", annonce.donnes[8]);
            startActivity(intent);
        });
    }

    private void affichageDonnes() {
        annonce = new Annonce(id);
        annonce.recupDonnes(this, new Annonce.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
        });
    }

    private void affichage(){
        if(annonce.donnes.length != 0) {
            textNom.setText(annonce.donnes[1]+" (H/F)");
            textRen.setText(annonce.donnes[4]+" € par heure");
            textDat.setText(annonce.donnes[5]+" - "+annonce.donnes[9]);
            textMet.setText(annonce.donnes[7]);
            textVil.setText(annonce.donnes[8]);
            textDescription.setText(annonce.donnes[2]);
            textMotCles.setText(annonce.donnes[10]);

            employeur = new Employeur(Integer.parseInt(annonce.donnes[3]));
            employeur.recupDonnes(this, new Employeur.VolleyCallback() {
                @Override
                public void onSuccess() {
                    affichage2();
                }
                @Override
                public void onError() {
                    affichageError();
                }
            });
        }else{
            affErreur.setText("Aucun resultat trouve !!");
        }
    }

    private void affichage2(){
        String s = "Nom : "+employeur.nomContact1+"\nEmail : "+employeur.email1+"\nTelephone : "+employeur.telephone1+"\nNom Entreprise : "+employeur.nomEntreprise+"\nReseaux Sociaux : "+employeur.liens;
        textDetails.setText(s);
    }

    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}

    public void onHomeClick(View view) {
        onHomeClick(this);
    }
    public void onRechercheClick(View view) {
        onRechercheClick(this);
    }
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {
        onCompteClick(this);
    }
}