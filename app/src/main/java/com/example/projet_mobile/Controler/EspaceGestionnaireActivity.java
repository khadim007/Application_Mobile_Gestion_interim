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

import com.example.projet_mobile.Modele.Gestionnaire;
import com.example.projet_mobile.R;

public class EspaceGestionnaireActivity extends AppCompatActivity implements toolbar {
    SharedPreferences sharedPreferences;
    Gestionnaire gestionnaire;
    String role;
    int id;

    private TextView textErreur;
    private TextView textNom;
    private EditText editNom;
    private EditText editTelephone;
    private EditText editEmail;

    private Button bouttonModifier;
    private Button bouttonPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_gestionnaire);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        textErreur = findViewById(R.id.textErreur);
        textNom = findViewById(R.id.textNom);
        editNom = findViewById(R.id.editNom);
        editTelephone = findViewById(R.id.editTelephone);
        editEmail = findViewById(R.id.editEmail);

        bouttonModifier = findViewById(R.id.buttonModifier);
        bouttonPassword = findViewById(R.id.buttonPassword);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void click(){
        bouttonModifier.setOnClickListener(v -> {
            String nom = editNom.getText().toString();
            String telephone = editTelephone.getText().toString();
            String email = editEmail.getText().toString();
            if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
                Toast.makeText(this, "Tous les champs doivent etre remplis !!", Toast.LENGTH_SHORT).show();
                return;
            }
            gestionnaire = new Gestionnaire(id, nom, telephone, email);
            gestionnaire.modifierInfos(this,  new Gestionnaire.VolleyCallback() {
                @Override
                public void onSuccess() {Toast.makeText(EspaceGestionnaireActivity.this, "Les modifications sont bien enregistres !!", Toast.LENGTH_SHORT).show();}
                @Override
                public void onError() {affichageError("Les modification ne sont pas enregistre. Veillez reesayer !!");}
            });
        });
        bouttonPassword.setOnClickListener(v -> {
            Intent intent = new Intent( EspaceGestionnaireActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void getDonnes(){
        gestionnaire = new Gestionnaire(id);
        gestionnaire.recupDonnes(this, new Gestionnaire.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError("Probleme lors de la recuperation des donnes !!");}
        });
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        textNom.setText(gestionnaire.nom);
        editNom.setText(gestionnaire.nom);
        editTelephone.setText(gestionnaire.telephone);
        editEmail.setText(gestionnaire.email);
    }
    private void affichageError(String s){textErreur.setText(s);}

    public void onCardCandidat(View view) {
        Intent intent = new Intent( EspaceGestionnaireActivity.this, GestionGestionnaireActivity.class);
        intent.putExtra("partie", "candidat");
        startActivity(intent);
    }
    public void onCardEmployeur(View view) {
        Intent intent = new Intent( EspaceGestionnaireActivity.this, GestionGestionnaireActivity.class);
        intent.putExtra("partie", "employeur");
        startActivity(intent);
    }
    public void onCardAgence(View view) {
        Intent intent = new Intent( EspaceGestionnaireActivity.this, GestionGestionnaireActivity.class);
        intent.putExtra("partie", "agence");
        startActivity(intent);
    }
    public void onCardAjouter(View view) {

    }
    public void onCardDeconnexion(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", 0);
        editor.putString("role", "");
        editor.commit();
        Intent intent = new Intent( EspaceGestionnaireActivity.this, AccueilActivity.class);
        startActivity(intent);
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {}
}