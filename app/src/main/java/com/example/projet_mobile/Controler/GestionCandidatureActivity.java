package com.example.projet_mobile.Controler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.Modele.Emplois;
import com.example.projet_mobile.R;

public class GestionCandidatureActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Candidature candidature;
    String role;
    int id;

    public Button bouttonConsulter;
    public Button bouttonModifier;
    public Button bouttonSupprimer;
    public Button bouttonContacter;

    private ListView listView;
    private TextView affErreur;
    private TextView textTitre;
    private String type;
    private Spinner spinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_candidature);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        spinnerType = findViewById(R.id.spinnerType);
        affErreur = findViewById(R.id.affError);
        textTitre = findViewById(R.id.textTitre);
        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click() {}

    public void click2(String id, String annonce, String nom, String nomAnnonce) {
        Intent intent;
        if("consulter".equals(nom) || "contacter".equals(nom)){
            intent = new Intent( GestionCandidatureActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(annonce));
            startActivity(intent);
        }else if("modifier".equals(nom)){
            intent = new Intent( GestionCandidatureActivity.this, CandidatureOffreActivity.class);
            intent.putExtra("id", Integer.parseInt(annonce));
            intent.putExtra("nom", nomAnnonce);
            intent.putExtra("id_candidature", Integer.parseInt(id));
            startActivity(intent);
        }else if("supprimer".equals(nom)){
            candidature.id = Integer.parseInt(id);
            candidature.supp(this, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {getDonnes();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {}
            });
        }else if("accepter".equals(nom) || "refuser".equals(nom)){
            candidature.id = Integer.parseInt(id);
            candidature.modifierEtat(this, nom, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {getDonnes();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {}
            });

            int idd = sharedPreferences.getInt("id", 0);
            if("accepter".equals(nom)){
                Emplois emplois = new Emplois(Integer.parseInt(id), idd, Integer.parseInt(annonce), nomAnnonce);
                emplois.ajouter(this, new Emplois.VolleyCallback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onError() {}
                    @Override
                    public void onEmpty() {}
                });
            }
        }
    }

    public void onEnvoyerClick(View view) {getDonnes();}

    private void getDonnes(){
        type = spinnerType.getSelectedItem().toString();
        switch (type) {
            case "Cand. en cours acceptees":
                textTitre.setText("Vos Cand. en cours acceptees");
                candidature = new Candidature(id, "en cours acceptees");
                break;
            case "Cand. en cours non acceptees":
                textTitre.setText("Vos Cand. en cours non acceptees");
                candidature = new Candidature(id, "en cours non acceptees");
                break;
            case "Cand. passees":
                textTitre.setText("Vos Cand. passees");
                candidature = new Candidature(id, "passees");
                break;
            case "Cand. potentielles":
                Intent intent = new Intent( GestionCandidatureActivity.this, OffreActivity.class);
                startActivity(intent);
                return;
            default:
                textTitre.setText("Vos Cand. en cours et sans reponse");
                candidature = new Candidature(id, "en cours et sans reponse");
                break;
        }
        candidature.recupDonnes(this, new Candidature.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {affichageEmpty();}
        });
    }

    private void affichage(){
        affErreur.setText(candidature.donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Candidature.CandidatureAdapter candidatures = new Candidature.CandidatureAdapter(this, this, candidature.donnes);
        listView.setAdapter(candidatures);
    }

    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    private void affichageEmpty(){affErreur.setText("Aucune candidature est trouvée !!"); affErreur.setTextSize(16); listView.setVisibility(View.GONE);}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}