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

import com.example.projet_mobile.Modele.Agence;
import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class GestionGestionnaireActivity extends AppCompatActivity implements toolbar {
    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Employeur employeur;
    Agence agence;
    String partie;
    String role;
    int id;

    private ListView listView;
    private TextView affTitre;
    private TextView affErreur;

    public Button bouttonRefuser;
    public Button bouttonContacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_gestionnaire);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");
        partie = getIntent().getStringExtra("partie");

        getID();
        getDonnes();
    }

    private void getID(){
        affTitre = findViewById(R.id.textTitre);
        affErreur = findViewById(R.id.affError);
        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    public void click2(String ident, String nom, String em) {
        if("refuser".equals(nom)){
            if(partie.equals("agence")) {
                agence.id = Integer.parseInt(ident);
                agence.supp(this, new Agence.VolleyCallback() {
                    @Override
                    public void onSuccess() {getDonnes();}
                    @Override
                    public void onError() {affichageError();}
                });
            }else{
                employeur.id =  Integer.parseInt(ident);
                employeur.supp(this, new Employeur.VolleyCallback() {
                    @Override
                    public void onSuccess() {getDonnes();}
                    @Override
                    public void onError() {affichageError();}
                });
            }
        }else if("contacter".equals(nom)){
            Intent intent = new Intent( GestionGestionnaireActivity.this, ContactActivity.class);
            intent.putExtra("id", Integer.parseInt(ident));
            intent.putExtra("partie", "gestGestionnaire");
            intent.putExtra("email", em);
            startActivity(intent);
        }else if("cv".equals(nom)){
            Intent intent = new Intent( GestionGestionnaireActivity.this, AffichePDFActivity.class);
            intent.putExtra("id", Integer.parseInt(ident));
            intent.putExtra("fichier", "cv");
            startActivity(intent);
        }else if("lettre".equals(nom)){
            Intent intent = new Intent( GestionGestionnaireActivity.this, AffichePDFActivity.class);
            intent.putExtra("id", Integer.parseInt(ident));
            intent.putExtra("fichier", "lettre");
            startActivity(intent);
        }
    }

    public void getDonnes(){
        affTitre.setText(partie);
        if(partie.equals("agence")){
            agence = new Agence();
            agence.recupAll(this, new Agence.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError();}
            });
        }else if(partie.equals("candidat")){
            candidat = new CandidatInscrit();
            candidat.recupAll(this, new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichage();}
            });
        }else{
            employeur = new Employeur();
            employeur.recupAll(this, new Employeur.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError();}
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        if(partie.equals("agence")){
            affErreur.setText(agence.donnes.length + " resultats");
            listView.setVisibility(View.VISIBLE);
            Agence.AgenceAdapter agences = new Agence.AgenceAdapter(this, this, agence.donnes, partie);
            listView.setAdapter(agences);
        }else if(partie.equals("candidat")){
            affErreur.setText(candidat.donnes.length + " resultats");
            listView.setVisibility(View.VISIBLE);
            Agence.AgenceAdapter agences = new Agence.AgenceAdapter(this, this, candidat.donnes, partie);
            listView.setAdapter(agences);
        }else {
            affErreur.setText(employeur.donnes.length + " resultats");
            listView.setVisibility(View.VISIBLE);
            Agence.AgenceAdapter agences = new Agence.AgenceAdapter(this, this, employeur.donnes, partie);
            listView.setAdapter(agences);
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}