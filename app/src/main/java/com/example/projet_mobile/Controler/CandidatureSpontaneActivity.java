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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.R;

public class CandidatureSpontaneActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Candidature candidature;
    int id;

    private String metier;
    private String employeur;
    private String dateDebut;
    private String ville;

    private EditText editMetier;
    private EditText editEmployeur;
    private EditText editDateDebut;
    private EditText editVille;

    private Button bouttonEnvoyer;

    private TextView affErreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_spontane);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        editMetier = findViewById(R.id.editMetier);
        editEmployeur = findViewById(R.id.editEmployeur);
        editDateDebut = findViewById(R.id.editDateDebut);
        editVille = findViewById(R.id.editVille);

        affErreur = findViewById(R.id.affError);

        bouttonEnvoyer = findViewById(R.id.buttonValider);

        ImageView im = findViewById(R.id.imCandidature);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click(){
        bouttonEnvoyer.setOnClickListener(v -> {
            metier = editMetier.getText().toString();
            employeur = editEmployeur.getText().toString();
            dateDebut = editDateDebut.getText().toString();
            ville = editVille.getText().toString();
            exec();
        });
    }

    private void exec(){
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

    private void getDonnes(){
        candidat = new CandidatInscrit(id);
        candidat.getDonnesCand(this, new CandidatInscrit.VolleyCallback() {
            @Override
            public void onSuccess() {}
            @Override
            public void onError() {affichageError(candidat.affiche);}
        });
    }
    private void affichageError(String s){affErreur.setText(s);}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {}
    public void onCompteClick(View view) {onCompteClick(this);}
}