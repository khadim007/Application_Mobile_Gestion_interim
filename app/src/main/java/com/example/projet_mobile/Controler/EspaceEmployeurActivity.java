package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Abonnement;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class EspaceEmployeurActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Employeur employeur;
    Abonnement abonnement;
    int id;

    private TextView textErreur;
    private TextView textNomEntre;
    private TextView textPrix;
    private TextView textAb;
    private TextView textCondition;

    private EditText editNomEntre;
    private EditText editNumero;
    private EditText editServ1;
    private EditText editServ2;
    private EditText editCont1;
    private EditText editCont2;
    private EditText editTelephone;
    private EditText editAdresse;
    private EditText editEmail;
    private EditText editPassword;

    private Button bouttonModifier;
    private Button bouttonChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_employeur);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        textErreur = findViewById(R.id.textErreur);
        textNomEntre = findViewById(R.id.textNomEntre);
        textAb = findViewById(R.id.textAb);
        textPrix = findViewById(R.id.textprix);
        textCondition = findViewById(R.id.textCondition);

        editNomEntre = findViewById(R.id.editNomEntre);
        editNumero = findViewById(R.id.editNumero);
        editServ1 = findViewById(R.id.editServ1);
        editServ2 = findViewById(R.id.editServ2);
        editCont1 = findViewById(R.id.editCont1);
        editCont2 = findViewById(R.id.editCont2);
        editTelephone = findViewById(R.id.editTelephone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editAdresse = findViewById(R.id.editAdresse);

        bouttonModifier = findViewById(R.id.buttonModifier);
        bouttonChanger = findViewById(R.id.buttonChager);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void click(){
        bouttonModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomEntre = editNomEntre.getText().toString();
                String numero = editNumero.getText().toString();
                String serv1 = editServ1.getText().toString();
                String serv2 = editServ2.getText().toString();
                String cont1 = editCont1.getText().toString();
                String cont2 = editCont2.getText().toString();
                String telephone = editTelephone.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String adresse = editAdresse.getText().toString();

                employeur = new Employeur(id, nomEntre, numero, serv1, serv2, cont1, cont2, telephone, email, password, adresse);
                employeur.modifierInfos(EspaceEmployeurActivity.this,  new Employeur.VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        affichageError("");
                        Toast.makeText(EspaceEmployeurActivity.this, "Les modifications sont bien enregistres !!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError() {
                        String s = "Les modification ne sont pas enregistre. Veillez reesayer !!";
                        affichageError(s);
                    }
                });
            }
        });

        bouttonChanger.setOnClickListener(v -> {
            Intent intent = new Intent( EspaceEmployeurActivity.this, AbonnementActivity.class);
            startActivity(intent);
        });
    }

    private void getDonnes(){
        id = sharedPreferences.getInt("id", 0);
        employeur= new Employeur(id);
        employeur.recupDonnes(this, new Employeur.VolleyCallback() {
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

    @SuppressLint("SetTextI18n")
    private void affichage(String s){
        if(Employeur.succes.equals(s)){
            textNomEntre.setText(employeur.nomEntreprise);
            editNomEntre.setText(employeur.nomEntreprise);
            editNumero.setText(employeur.numeroNationale);
            editServ1.setText(employeur.nomService);
            editServ2.setText(employeur.nomSousService);
            editCont1.setText(employeur.nomContact1);
            editCont2.setText(employeur.nomContact2);
            editTelephone.setText(employeur.telephone1);
            editEmail.setText(employeur.email1);
            editPassword.setText(employeur.password);
            editAdresse.setText(employeur.adresse);

            abonnement = new Abonnement(Integer.parseInt(employeur.abonnement));
            abonnement.getDonnes(this, new Abonnement.VolleyCallback() {
                @Override
                public void onSuccess() {
                    affichage2();
                }
                @Override
                public void onError() {
                    affichageError(s);
                }
            });
        }else{
            affichageError(s);
        }
    }

    private void affichage2(){
        textAb.setText(abonnement.nom);
        textPrix.setText(abonnement.prix);
        textCondition.setText(abonnement.condition);
    }

    private void affichageError(String s){
        textErreur.setText(s);
    }
}