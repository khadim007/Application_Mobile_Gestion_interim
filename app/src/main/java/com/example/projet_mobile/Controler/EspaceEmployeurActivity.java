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

import com.example.projet_mobile.Modele.Abonnement;
import com.example.projet_mobile.Modele.Agence;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class EspaceEmployeurActivity extends AppCompatActivity implements toolbar {
    SharedPreferences sharedPreferences;
    Abonnement abonnement;
    Employeur employeur;
    Agence agence;
    String role;
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
    private EditText editLiens;

    private Button bouttonModifier;
    private Button bouttonChanger;
    private Button bouttonPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_employeur);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

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
        editLiens = findViewById(R.id.editLiens);
        editAdresse = findViewById(R.id.editAdresse);

        bouttonModifier = findViewById(R.id.buttonModifier);
        bouttonChanger = findViewById(R.id.buttonChager);
        bouttonPassword = findViewById(R.id.buttonPassword);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void click(){
        bouttonModifier.setOnClickListener(v -> {
            String nomEntre = editNomEntre.getText().toString();
            String numero = editNumero.getText().toString();
            String serv1 = editServ1.getText().toString();
            String serv2 = editServ2.getText().toString();
            String cont1 = editCont1.getText().toString();
            String cont2 = editCont2.getText().toString();
            String telephone = editTelephone.getText().toString();
            String email = editEmail.getText().toString();
            String liens = editLiens.getText().toString();
            String adresse = editAdresse.getText().toString();
            if (nomEntre.isEmpty() || serv1.isEmpty() || numero.isEmpty() || email.isEmpty() || adresse.isEmpty()) {
                Toast.makeText(this, "Il y a des champs qui doivent pas etre vide!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(role.equals("agence")){
                agence = new Agence(id, nomEntre, numero, serv1, serv2, cont1, cont2, telephone, email, liens, adresse);
                agence.modifierInfos(EspaceEmployeurActivity.this,  new Agence.VolleyCallback() {
                    @Override
                    public void onSuccess() {affichageError(""); Toast.makeText(EspaceEmployeurActivity.this, "Les modifications sont bien enregistres !!", Toast.LENGTH_SHORT).show();}
                    @Override
                    public void onError() {affichageError("Les modification ne sont pas enregistre. Veillez reesayer !!");}
                });
            }else{
                employeur = new Employeur(id, nomEntre, numero, serv1, serv2, cont1, cont2, telephone, email, liens, adresse);
                employeur.modifierInfos(EspaceEmployeurActivity.this,  new Employeur.VolleyCallback() {
                    @Override
                    public void onSuccess() {affichageError(""); Toast.makeText(EspaceEmployeurActivity.this, "Les modifications sont bien enregistres !!", Toast.LENGTH_SHORT).show();}
                    @Override
                    public void onError() {affichageError("Les modification ne sont pas enregistre. Veillez reesayer !!");}
                });
            }
        });
        bouttonChanger.setOnClickListener(v -> {
            Intent intent = new Intent( EspaceEmployeurActivity.this, AbonnementActivity.class);
            startActivity(intent);
        });
        bouttonPassword.setOnClickListener(v -> {
            Intent intent = new Intent( EspaceEmployeurActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void getDonnes(){
        if(role.equals("agence")) {
            agence = new Agence(id);
            agence.recupDonnes(this, new Agence.VolleyCallback() {
                @Override
                public void onSuccess() {affichage(agence.affiche);}
                @Override
                public void onError() {affichageError(agence.affiche);}
            });
        }else{
            employeur= new Employeur(id);
            employeur.recupDonnes(this, new Employeur.VolleyCallback() {
                @Override
                public void onSuccess() {affichage(employeur.affiche);}
                @Override
                public void onError() {affichageError(employeur.affiche);}
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichage(String s){
        if(role.equals("agence")) {
            if(Agence.succes.equals(s)){
                textNomEntre.setText(agence.nomAgence);
                editNomEntre.setText(agence.nomAgence);
                editNumero.setText(agence.numeroNationale);
                editServ1.setText(agence.nomService);
                editServ2.setText(agence.nomSousService);
                editCont1.setText(agence.nomContact1);
                editCont2.setText(agence.nomContact2);
                editTelephone.setText(agence.telephone1);
                editEmail.setText(agence.email1);
                editLiens.setText(agence.liens);
                editAdresse.setText(agence.adresse);

                abonnement = new Abonnement(Integer.parseInt(agence.abonnement));
                abonnement.getDonnes(this, new Abonnement.VolleyCallback() {
                    @Override
                    public void onSuccess() {affichage2();}
                    @Override
                    public void onError() {affichageError(s);}
                });
            }else{
                affichageError(s);
            }
        }else{
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
                editLiens.setText(employeur.liens);
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
    }

    private void affichage2(){
        textAb.setText(abonnement.nom);
        textPrix.setText(abonnement.prix);
        textCondition.setText(abonnement.condition);
    }
    private void affichageError(String s){textErreur.setText(s);}

    public void onCardCandidatures(View view) {
        Intent intent = new Intent( EspaceEmployeurActivity.this, GestionCandidatureActivity.class);
        startActivity(intent);
    }
    public void onCardOffres(View view) {
        Intent intent = new Intent( EspaceEmployeurActivity.this, GestionOffreActivity.class);
        startActivity(intent);
    }
    public void onCardDeconnexion(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", 0);
        editor.putString("role", "");
        editor.commit();
        Intent intent = new Intent( EspaceEmployeurActivity.this, AccueilActivity.class);
        startActivity(intent);
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {}
}