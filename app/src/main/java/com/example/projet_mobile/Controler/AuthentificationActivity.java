package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class AuthentificationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private CandidatInscrit candidat;
    private Employeur employeur;

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
        editRole = (Spinner) findViewById(R.id.editRole);
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
                role = editRole.getSelectedItem().toString();
                identifiant = editIdentifiant.getText().toString();
                password = editPassword.getText().toString();
                if(role.equals("employeur")) {
                    execEmp();
                }else{
                    execCand();
                }
            }
        });

        bouttonCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = editRole.getSelectedItem().toString();
                if(role.equals("employeur")) {
                    Intent intent = new Intent(AuthentificationActivity.this, InscriptionEmpActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(AuthentificationActivity.this, InscriptionCandActivity.class);
                    startActivity(intent);
                }
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
                affichage(candidat.affiche);
            }
            @Override
            public void onError() {
                affichageError(candidat.affiche);
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
            public void onSuccess() {
                affichage(employeur.affiche);
            }
            @Override
            public void onError() {
                affichageError(employeur.affiche);
            }
        });
    }

    private void affichage(String s){
        if(CandidatInscrit.succes.equals(s)){
            if("employeur".equals(role)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", employeur.id);
                editor.putString("role", role);
                editor.commit();
                Intent intent = new Intent( AuthentificationActivity.this, AccueilActivity.class);
                startActivity(intent);
            }else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", candidat.id);
                editor.putString("role", role);
                editor.commit();
                Intent intent = new Intent( AuthentificationActivity.this, AccueilActivity.class);
                startActivity(intent);
            }
        }else{
            affichageError(s);
        }
    }

    private void affichageError(String s){
        textErreur.setText(candidat.affiche);
    }

    public void onImageCompteClick(View view) {} //laisse comme ca
}