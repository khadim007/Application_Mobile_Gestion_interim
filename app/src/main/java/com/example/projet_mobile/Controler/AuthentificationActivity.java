package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.R;

public class AuthentificationActivity extends AppCompatActivity {

    private CandidatInscrit candidat;

    private String identifiant;
    private String password;

    private TextView textErreur;

    private EditText editIdentifiant;
    private EditText editPassword;

    private Button bouttonValider;
    private Button bouttonCreer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        getID();
        click();
    }

    private void getID(){
        editIdentifiant = (EditText) findViewById(R.id.editIdentifiant);
        editPassword = (EditText) findViewById(R.id.editPassword);

        textErreur = (TextView) findViewById(R.id.textErreur);

        bouttonValider = (Button) findViewById(R.id.buttonValider);
        bouttonCreer = (Button) findViewById(R.id.buttoncreer);
    }

    private void click(){
        bouttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifiant = editIdentifiant.getText().toString();
                password = editPassword.getText().toString();
                exec();
            }
        });

        bouttonCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( AuthentificationActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exec(){
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

    private void affichage(){
        if(CandidatInscrit.succes.equals(candidat.affiche)){
            Intent intent = new Intent( AuthentificationActivity.this, AccueilActivity.class);
            startActivity(intent);
        }else{
            affichageError();
        }
    }

    private void affichageError(){
        textErreur.setText(candidat.affiche);
    }
}