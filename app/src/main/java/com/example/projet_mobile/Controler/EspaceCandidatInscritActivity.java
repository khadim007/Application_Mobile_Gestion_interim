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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Preference;
import com.example.projet_mobile.R;

public class EspaceCandidatInscritActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Preference preference;
    int id;

    private TextView textErreur;
    private TextView textNom;

    private EditText editTelephone;
    private EditText editNationalite;
    private EditText editDateNais;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editVille;

    private EditText editEmployeur;
    private EditText editDateDebut;
    private EditText editMetier;
    private EditText editVille2;

    private Button bouttonModifier;
    private Button bouttonModifier2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_candidat_inscrit);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        textErreur = findViewById(R.id.textErreur);
        textNom = findViewById(R.id.textNom);

        editNationalite = findViewById(R.id.editNationalite);
        editDateNais = findViewById(R.id.editDateNais);
        editTelephone = findViewById(R.id.editTelephone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editVille = findViewById(R.id.editVille);

        editEmployeur = findViewById(R.id.editEmployeur);
        editDateDebut = findViewById(R.id.editDateDebut);
        editMetier = findViewById(R.id.editMetier);
        editVille2 = findViewById(R.id.editVille2);

        bouttonModifier = findViewById(R.id.buttonModifier);
        bouttonModifier2 = findViewById(R.id.buttonModifier2);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void click(){
        bouttonModifier.setOnClickListener(v -> {
            String nationalite = editNationalite.getText().toString();
            String dateNais = editDateNais.getText().toString();
            String telephone = editTelephone.getText().toString();
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            String ville = editVille.getText().toString();

            if (password.isEmpty()) {
                Toast.makeText(this, "Password doit pas etre vide !!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty() && telephone.isEmpty()) {
                Toast.makeText(this, "Il faut renseigne soit l'email, soit le telephne !!", Toast.LENGTH_SHORT).show();
                return;
            }
            candidat = new CandidatInscrit(id, nationalite, dateNais, telephone, email, password, ville);
            candidat.modifierInfos(EspaceCandidatInscritActivity.this,  new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {
                    affichageError("");
                    Toast.makeText(EspaceCandidatInscritActivity.this, "Les modifications sont bien enregistres !!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError() {
                    String s = "Les modification ne sont pas enregistre. Veillez reesayer !!";
                    affichageError(s);
                }
            });
        });

        bouttonModifier2.setOnClickListener(v -> {
            String employeur = editEmployeur.getText().toString();
            String dateDebut = editDateDebut.getText().toString();
            String metier = editMetier.getText().toString();
            String ville2 = editVille2.getText().toString();
            if (metier.isEmpty() && employeur.isEmpty() && dateDebut.isEmpty() && ville2.isEmpty()) {
                Toast.makeText(this, "Au mois un champs doit etre remplis !!", Toast.LENGTH_SHORT).show();
                return;
            }
            preference = new Preference(id, employeur, dateDebut, metier, ville2);
            preference.modifierInfos(EspaceCandidatInscritActivity.this,  new Preference.VolleyCallback() {
                @Override
                public void onSuccess() {affichageError("");Toast.makeText(EspaceCandidatInscritActivity.this, "Les modifications sont bien enregistres !!", Toast.LENGTH_SHORT).show();}
                @Override
                public void onError() {affichageError("Les modification ne sont pas enregistre. Veillez reesayer !!");}
                @Override
                public void onEmpty() {}
            });
        });
    }

    private void getDonnes(){
        id = sharedPreferences.getInt("id", 0);
        candidat = new CandidatInscrit(id);
        candidat.getDonnes(this, new CandidatInscrit.VolleyCallback() {
            @Override
            public void onSuccess() {affichage(candidat.affiche);}
            @Override
            public void onError() {affichageError(candidat.affiche);}
        });

        preference = new Preference(id);
        preference.getDonnes(this, new Preference.VolleyCallback() {
            @Override
            public void onSuccess() {affichage2();}
            @Override
            public void onError() {affichageError(preference.affiche);}
            @Override
            public void onEmpty() {}
        });
    }

    @SuppressLint("SetTextI18n")
    private void affichage(String s){
        if(CandidatInscrit.succes.equals(s)){
            textNom.setText(candidat.prenom+" "+candidat.nom);
            editNationalite.setText(candidat.nationalite);
            editDateNais.setText(candidat.dateNais);
            editTelephone.setText(candidat.telephone);
            editEmail.setText(candidat.email);
            editPassword.setText(candidat.password);
            editVille.setText(candidat.ville);
        }else{
            affichageError(s);
        }
    }
    @SuppressLint("SetTextI18n")
    private void affichage2(){
        editEmployeur.setText(preference.employeur);
        editDateDebut.setText(preference.dateDebut);
        editMetier.setText(preference.metier);
        editVille2.setText(preference.ville);
    }
    private void affichageError(String s){textErreur.setText(s);}

    public void onCardCV(View view) {
        Intent intent = new Intent( EspaceCandidatInscritActivity.this, AffichePDFActivity.class);
        intent.putExtra("fichier", "cv");
        startActivity(intent);
    }
    public void onCardLettre(View view) {
        Intent intent = new Intent( EspaceCandidatInscritActivity.this, AffichePDFActivity.class);
        intent.putExtra("fichier", "lettre");
        startActivity(intent);
    }
    public void onCardCandidatures(View view) {
        Intent intent = new Intent( EspaceCandidatInscritActivity.this, GestionCandidatureActivity.class);
        startActivity(intent);
    }
    public void onCardOffres(View view) {
        Intent intent = new Intent( EspaceCandidatInscritActivity.this, OffreActivity.class);
        startActivity(intent);
    }

    public void onCardEmplois(View view) {
        System.out.println("==========================E");
    }

    public void onCardDeconnexion(View view) {
        System.out.println("==========================E");
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {} // laisse comm ca
}