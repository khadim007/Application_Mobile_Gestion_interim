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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Agence;
import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.Modele.Gestionnaire;
import com.example.projet_mobile.R;

public class AuthentificationActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    private Gestionnaire gestionnaire;
    private CandidatInscrit candidat;
    private Employeur employeur;
    private Agence agence;

    private String role;
    private String identifiant;
    private String password;

    private TextView textErreur;

    private Spinner editRole;
    private EditText editIdentifiant;
    private EditText editPassword;

    private Button bouttonValider;
    private Button bouttonCreer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
    }

    private void getID(){
        editRole = findViewById(R.id.editRole);
        editRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(editRole.getSelectedItem().toString().equals("gestionnaire")) bouttonCreer.setVisibility(View.GONE);
                else bouttonCreer.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        editIdentifiant = findViewById(R.id.editIdentifiant);
        editPassword = findViewById(R.id.editPassword);

        textErreur = findViewById(R.id.textErreur);

        bouttonValider = findViewById(R.id.buttonValider);
        bouttonCreer = findViewById(R.id.buttoncreer);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click(){
        bouttonValider.setOnClickListener(v -> {
            role = editRole.getSelectedItem().toString();
            identifiant = editIdentifiant.getText().toString();
            password = editPassword.getText().toString();
            if(role.equals("employeur")) execEmp();
            else if(role.equals("agence")) execAgen();
            else if(role.equals("gestionnaire")) execGes();
            else execCand();
        });
        bouttonCreer.setOnClickListener(v -> {
            role = editRole.getSelectedItem().toString();
            if(role.equals("employeur") || role.equals("agence")) {
                Intent intent = new Intent(AuthentificationActivity.this, InscriptionEmpActivity.class);
                intent.putExtra("role", role);
                startActivity(intent);
            }else{
                Intent intent = new Intent(AuthentificationActivity.this, InscriptionCandActivity.class);
                startActivity(intent);
            }
        });
    }

    private void execCand(){
        if (identifiant.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        candidat = new CandidatInscrit(identifiant, password);
        candidat.verifier(this, new CandidatInscrit.VolleyCallback() {
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

    private void execEmp(){
        if (identifiant.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        employeur = new Employeur(identifiant, password);
        employeur.verifier(this, new Employeur.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
        });
    }

    private void execAgen(){
        if (identifiant.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        agence = new Agence(identifiant, password);
        agence.verifier(this, new Agence.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
        });
    }

    private void execGes(){
        if (identifiant.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        gestionnaire = new Gestionnaire(identifiant, password);
        gestionnaire.verifier(this, new Gestionnaire.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
        });
    }

    private void affichage(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if("employeur".equals(role)) editor.putInt("id", employeur.id);
        else if("agence".equals(role)) editor.putInt("id", agence.id);
        else if("gestionnaire".equals(role)) editor.putInt("id", gestionnaire.id);
        else editor.putInt("id", candidat.id);
        editor.putString("role", role);
        editor.commit();
        Intent intent = new Intent( AuthentificationActivity.this, AccueilActivity.class);
        startActivity(intent);
    }
    @SuppressLint("SetTextI18n")
    private void affichageError(){textErreur.setText("Identifiant ou mot de passe incorrect !!");}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {} //laisse comme ca
}