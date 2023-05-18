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

import com.example.projet_mobile.Modele.Agence;
import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.Modele.Gestionnaire;
import com.example.projet_mobile.R;

public class ChangePasswordActivity extends AppCompatActivity implements toolbar {
    SharedPreferences sharedPreferences;
    Gestionnaire gestionnaire;
    CandidatInscrit candidat;
    Employeur employeur;
    Agence agence;
    String role;
    int id;

    String ancienPassword;
    String password;

    private TextView textErreur;
    private EditText editAncienPassword;
    private EditText editPassword1;
    private EditText editPassword2;
    private Button bouttonModifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        getID();
        click();
    }

    private void getID(){
        textErreur = findViewById(R.id.textErreur);
        editAncienPassword = findViewById(R.id.editAncienPassword);
        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);
        bouttonModifier = findViewById(R.id.buttonModifier);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("SetTextI18n")
    private void click(){
        bouttonModifier.setOnClickListener(v -> {
            String anc = editAncienPassword.getText().toString();
            String p1 = editPassword1.getText().toString();
            String p2 = editPassword2.getText().toString();
            if(anc.isEmpty() || p1.isEmpty() || p2.isEmpty()){
                textErreur.setText("Tous les champs doivent etre remplis !!");
                return;
            }
            if(!p1.equals(p2)){
                textErreur.setText("Les nouveaux password saisis sont differents!!");
                return;
            }
            ancienPassword = anc;
            password = p1;
            exec1();
        });
    }

    private void exec1(){
        if("employeur".equals(role)){
            employeur = new Employeur(id); employeur.password = ancienPassword;
            employeur.verifierID(this, new Employeur.VolleyCallback() {
                @Override
                public void onSuccess() {exec2();}
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("L'ancien mot de passe saisi est incorrct !!");}
            });
        }else if("agence".equals(role)){
            agence = new Agence(id); agence.password = ancienPassword;
            agence.verifierId(this, new Agence.VolleyCallback() {
                @Override
                public void onSuccess() {exec2();}
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("L'ancien mot de passe saisi est incorrct !!");}
            });
        }else if("gestionnaire".equals(role)){
            gestionnaire = new Gestionnaire(id); gestionnaire.password = ancienPassword;
            gestionnaire.verifierId(this, new Gestionnaire.VolleyCallback() {
                @Override
                public void onSuccess() {exec2();}
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("L'ancien mot de passe saisi est incorrct !!");}
            });
        }else{
            candidat = new CandidatInscrit(id); candidat.password = ancienPassword;
            candidat.verifierID(this, new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {exec2();}
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("L'ancien mot de passe saisi est incorrct !!");}
            });
        }
    }

    private void exec2(){
        if("employeur".equals(role)){
            employeur.password = password;
            employeur.modifierPassword(this, new Employeur.VolleyCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(ChangePasswordActivity.this, EspaceEmployeurActivity.class);
                    startActivity(intent);
                }
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("Error. Veillez reconmmencer !!");}
            });
        }else if("agence".equals(role)){
            agence.password = password;
            agence.modifierPassword(this, new Agence.VolleyCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(ChangePasswordActivity.this, EspaceEmployeurActivity.class);
                    startActivity(intent);
                }
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("Error. Veillez reconmmencer !!");}
            });
        }else if("gestionnaire".equals(role)){
            gestionnaire.password = password;
            gestionnaire.modifierPassword(this, new Gestionnaire.VolleyCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(ChangePasswordActivity.this, EspaceGestionnaireActivity.class);
                    startActivity(intent);
                }
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("Error. Veillez reconmmencer !!");}
            });
        }else{
            candidat.password = password;
            candidat.modifierPassword(this, new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(ChangePasswordActivity.this, EspaceCandidatInscritActivity.class);
                    startActivity(intent);
                }
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {textErreur.setText("Error. Veillez reconmmencer  !!");}
            });
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}
