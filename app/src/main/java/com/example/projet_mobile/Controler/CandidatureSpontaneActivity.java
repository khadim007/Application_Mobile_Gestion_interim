package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.R;

public class CandidatureSpontaneActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Candidature candidature;
    Annonce annonce;
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
        if("employeur".equals(role)) card.setVisibility(View.GONE);
        else cardE.setVisibility(View.GONE);
    }

    private void click(){
        bouttonEnvoyer.setOnClickListener(v -> exec());
        bouttonEnvoyerE.setOnClickListener(v -> execE());
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
        if(ident != 0){
            annonce = new Annonce(nomE, descriptionE, descriptionEEn, String.valueOf(id), renumerationE, dateDebutE, metierE, villeE, dureeE, mots_clesE, categorieE);
            annonce.id = ident;
            annonce.modifier(this, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {Intent intent = new Intent( CandidatureSpontaneActivity.this, OffreActivity.class); startActivity(intent);}
                @Override
                public void onError() {}
                @Override
                public void onEmpty() {}
            });
        }else {
            annonce = new Annonce(nomE, descriptionE, descriptionEEn, String.valueOf(id), renumerationE, dateDebutE, metierE, villeE, dureeE, mots_clesE, categorieE);
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

    private void getDonnes(){
        Intent intent = getIntent();
        int ident = intent.getIntExtra("idE", 0);
        if(ident != 0){
            annonce = new Annonce(ident);
            annonce.recupDonnes(this, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError(candidat.affiche);}
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

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {}
    public void onCompteClick(View view) {onCompteClick(this);}
}