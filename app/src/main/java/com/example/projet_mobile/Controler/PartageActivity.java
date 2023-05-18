package com.example.projet_mobile.Controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Amis;
import com.example.projet_mobile.Modele.Notification;
import com.example.projet_mobile.Modele.SMSetMAIL;
import com.example.projet_mobile.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PartageActivity extends AppCompatActivity implements toolbar {
    private static final int PERMISSION_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    public String aller = "true";
    int idAnnonce;
    String role;
    int id;

    Notification notification;
    SMSetMAIL sms;
    Amis amis;

    private TextView affErreur;
    private TextView textTitre;

    Spinner spinnerType;
    String type;

    LinearLayout layoutSMS;
    EditText editTelephoneSMS;

    LinearLayout layoutPersonne;
    EditText editTelephonePers;
    EditText editEmailPers;
    Button buttonEnvoyerPers;

    LinearLayout layoutGroupe;
    EditText editNomGroupe;
    EditText editNomGroupeCreer;
    EditText editTelephoneGroupe;
    EditText editEmailGroupe;
    Button buttonEnvoyerGroupe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partage);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        Intent intent = getIntent();
        idAnnonce = intent.getIntExtra("id", 0);

        getID();
        click();
        changer();
    }

    private void getID(){
        affErreur = findViewById(R.id.affError);
        textTitre = findViewById(R.id.textTitre);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {changer();}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        layoutSMS = findViewById(R.id.layoutSMS);
        editTelephoneSMS = findViewById(R.id.editTelephoneSMS);

        layoutPersonne = findViewById(R.id.layoutPersonne);
        editTelephonePers = findViewById(R.id.editTelephonePers);
        editEmailPers = findViewById(R.id.editEmailPers);
        buttonEnvoyerPers = findViewById(R.id.buttonEnvoyerPers);

        layoutGroupe = findViewById(R.id.layoutGroupe);
        editNomGroupe = findViewById(R.id.editNomGroupe);
        editNomGroupeCreer = findViewById(R.id.editNomGroupeCreer);
        editTelephoneGroupe = findViewById(R.id.editTelephoneGroupe);
        editEmailGroupe = findViewById(R.id.editEmailGroupe);
        buttonEnvoyerGroupe = findViewById(R.id.buttonEnvoyerGroupe);

        ImageView im = findViewById(R.id.imRecherche);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    private void click() {
        buttonEnvoyerPers.setOnClickListener(v -> {
            String telephone = editTelephonePers.getText().toString();
            String email = editEmailPers.getText().toString();
            if (email.isEmpty() && telephone.isEmpty()) {
                Toast.makeText(this, "Il faut renseigne soit l'email, soit le telephne !!", Toast.LENGTH_SHORT).show();
                return;
            }
            notification = new Notification();
            notification.annonce = idAnnonce;
            notification.ajouter(this, email, telephone, new Notification.VolleyCallback() {
                @Override
                public void onSuccess() {Intent intent = new Intent( PartageActivity.this, AccueilActivity.class); startActivity(intent);}
                @Override
                public void onError() {if(email.isEmpty()) envoyerSMS(telephone);else envoyerEmail(email);}
                @Override
                public void onEmpty() {if(email.isEmpty()) envoyerSMS(telephone);else envoyerEmail(email);}
            });
        });

        buttonEnvoyerGroupe.setOnClickListener(v -> {
            String nom = editNomGroupeCreer.getText().toString();
            String telephone = editTelephoneGroupe.getText().toString();
            String email = editEmailGroupe.getText().toString();
            if (nom.isEmpty()) {Toast.makeText(this, "Il faut renseigne soit le nom du groupe !!", Toast.LENGTH_SHORT).show();return;}
            if (email.isEmpty() && telephone.isEmpty()) {Toast.makeText(this, "Il faut renseigne soit l'email, soit le telephne !!", Toast.LENGTH_SHORT).show();return;}
            amis = new Amis(nom, telephone, email);
            amis.ajouter(this, new Amis.VolleyCallback() {
                @Override
                public void onSuccess() {Intent intent = new Intent( PartageActivity.this, AccueilActivity.class); startActivity(intent);}
                @SuppressLint("SetTextI18n")
                @Override
                public void onError() {affErreur.setText("Error. Veillez reesayer !!");}
                @Override
                public void onEmpty() {}
            });
        });
    }
    public void onEnvoyerClickSMS(View view) {envoyerSMS(editTelephoneSMS.getText().toString());}
    public void onEnvoyerClickGroupe(View view) {
        String nom = editNomGroupe.getText().toString();
        amis = new Amis(nom);
        amis.recupDonnes(this, new Amis.VolleyCallback() {
            @Override
            public void onSuccess() throws JsonProcessingException {traiterGroupe();}
            @SuppressLint("SetTextI18n")
            @Override
            public void onError() {affErreur.setText("Le groupe n'existe pas !!");}
            @SuppressLint("SetTextI18n")
            @Override
            public void onEmpty() {affErreur.setText("Le groupe n'existe pas !!");}
        });
    }

    private void changer(){
        type = spinnerType.getSelectedItem().toString();
        if("Partager avec une personne".equals(type)){
            layoutSMS.setVisibility(View.GONE);
            layoutPersonne.setVisibility(View.VISIBLE);
            layoutGroupe.setVisibility(View.GONE);
        }else if("Partager avec un groupe".equals(type)){
            layoutSMS.setVisibility(View.GONE);
            layoutPersonne.setVisibility(View.GONE);
            layoutGroupe.setVisibility(View.VISIBLE);
        }else{
            layoutSMS.setVisibility(View.VISIBLE);
            layoutPersonne.setVisibility(View.GONE);
            layoutGroupe.setVisibility(View.GONE);
        }
        textTitre.setText(type);
    }

    private void envoyerSMS(String tel){
        String text = "Votre pote vous a partage l'annonce avec l'identifiant ["+idAnnonce+"].";
        sms = new SMSetMAIL(this, tel, text, this);
        sms.envoyerSMS();
    }

    private void envoyerEmail(String em){
        String text = "Votre pote vous a partage l'annonce avec l'identifiant ["+idAnnonce+"].";
        sms = new SMSetMAIL(this, em, text, this);
        sms.envoyerEmail();
    }

    private void traiterGroupe() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode emails = om.readTree(amis.email);
        JsonNode telephones = om.readTree(amis.telephone);
        aller = "false";
        for (JsonNode element : emails) {
            String el = element.asText();
            envoyerEmail(el);
        }
        for (JsonNode element : telephones) {
            String el = element.asText();
            envoyerSMS(el);
        }
        Toast.makeText(this, "Succes !!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PartageActivity.this, AccueilActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sms.go();
            } else {
                String t = "L'annonce n'est pas envoye a cause des permission refuses !!";
                affErreur.setText(t);
            }
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}