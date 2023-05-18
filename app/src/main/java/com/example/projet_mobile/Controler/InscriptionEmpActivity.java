package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.R;

public class InscriptionEmpActivity extends AppCompatActivity implements toolbar {
    String role;

    private Button bouttonValider;
    private Button bouttonDeja;

    private String nomEntreprise;
    private String nomService;
    private String nomSousService;
    private String numeroNationale;
    private String nomContact1;
    private String nomContact2;
    private String email1;
    private String email2;
    private String telephone1;
    private String telephone2;
    private String password1;
    private String password2;
    private String adresse;
    private String liens;

    private TextView textNomEntreprise;
    private EditText editNomEntreprise;
    private EditText editNomService;
    private EditText editNomSousService;
    private EditText editNumeroNationale;
    private EditText editNomContact1;
    private EditText editNomContact2;
    private EditText editEmail1;
    private EditText editEmail2;
    private EditText editTelephone1;
    private EditText editTelephone2;
    private EditText editPassword1;
    private EditText editPassword2;
    private EditText editAdresse;
    private EditText editLiens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_emp);
        role = getIntent().getStringExtra("role");

        getID();
        click();
        aff();
    }

    @SuppressLint("SetTextI18n")
    private void getID(){
        textNomEntreprise = findViewById(R.id.textNomEntreprise);
        editNomEntreprise = findViewById(R.id.editNomEntreprise);
        if(role.equals("agence")) {textNomEntreprise.setText("Nom Agence(*) : "); editNomEntreprise.setHint("Nom de Agence");}
        editNomService = findViewById(R.id.editNomService);
        editNomSousService = findViewById(R.id.editNomSousService);
        editNumeroNationale = findViewById(R.id.editNumeroNationale);
        editNomContact1 = findViewById(R.id.editNomContact1);
        editNomContact2 = findViewById(R.id.editNomContact2);
        editEmail1 = findViewById(R.id.editEmail1);
        editEmail2 = findViewById(R.id.editEmail2);
        editTelephone1 = findViewById(R.id.editTéléphone1);
        editTelephone2 = findViewById(R.id.editTéléphone2);
        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);
        editAdresse = findViewById(R.id.editAdresse);
        editLiens = findViewById(R.id.editLiens);

        bouttonValider = findViewById(R.id.buttonValider);
        bouttonDeja = findViewById(R.id.buttonConnecter);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    private void click(){
        bouttonValider.setOnClickListener(v -> {
            nomEntreprise = editNomEntreprise.getText().toString();
            nomService = editNomService.getText().toString();
            nomSousService = editNomSousService.getText().toString();
            numeroNationale = editNumeroNationale.getText().toString();
            nomContact1 = editNomContact1.getText().toString();
            nomContact2 = editNomContact2.getText().toString();
            email1 = editEmail1.getText().toString();
            email2 = editEmail2.getText().toString();
            telephone1 = editTelephone1.getText().toString();
            telephone2 = editTelephone2.getText().toString();
            password1 = editPassword1.getText().toString();
            password2 = editPassword2.getText().toString();
            adresse = editAdresse.getText().toString();
            liens = editLiens.getText().toString();

            exec();
        });
        bouttonDeja.setOnClickListener(v -> {
            Intent intent = new Intent( InscriptionEmpActivity.this, AuthentificationActivity.class);
            startActivity(intent);
        });
    }

    private void exec(){
        if (!(password1.equals(password2))) {
            Toast.makeText(this, "Les deux passwords ne sont pas identiques !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nomEntreprise.isEmpty() || numeroNationale.isEmpty() || email1.isEmpty() || password1.isEmpty() || adresse.isEmpty()) {
            Toast.makeText(this, "Tous les champs obligatoire doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent( InscriptionEmpActivity.this, InsValidationActivity.class);
        if(role.equals("agence")) intent.putExtra("role", "agence");
        else intent.putExtra("role", "employeur");
        intent.putExtra("nomEntreprise", nomEntreprise);
        intent.putExtra("nomService", nomService);
        intent.putExtra("nomSousService", nomSousService);
        intent.putExtra("numeroNationale", numeroNationale);
        intent.putExtra("nomContact1", nomContact1);
        intent.putExtra("nomContact2", nomContact2);
        intent.putExtra("email1", email1);
        intent.putExtra("email2", email2);
        intent.putExtra("telephone1", telephone1);
        intent.putExtra("telephone2", telephone2);
        intent.putExtra("password", password1);
        intent.putExtra("adresse", adresse);
        intent.putExtra("liens", liens);
        startActivity(intent);
    }

    private void aff(){
        Intent intent = getIntent();
        numeroNationale = intent.getStringExtra("numeroNationale");
        if(numeroNationale != null) {
            nomEntreprise = intent.getStringExtra("nomEntreprise");
            nomService = intent.getStringExtra("nomService");
            nomSousService = intent.getStringExtra("nomSousService");
            nomContact1 = intent.getStringExtra("nomContact1");
            nomContact2 = intent.getStringExtra("nomContact2");
            email1 = intent.getStringExtra("email1");
            email2 = intent.getStringExtra("email2");
            telephone1 = intent.getStringExtra("telephone1");
            telephone2 = intent.getStringExtra("telephone2");
            password1 = intent.getStringExtra("password");
            password2 = intent.getStringExtra("password");
            adresse = intent.getStringExtra("adresse");
            liens = intent.getStringExtra("liens");

            editNumeroNationale.setText(numeroNationale);
            editNomEntreprise.setText(nomEntreprise);
            editNomService.setText(nomService);
            editNomSousService.setText(nomSousService);
            editNomContact1.setText(nomContact1);
            editNomContact2.setText(nomContact2);
            editEmail1.setText(email1);
            editEmail2.setText(email2);
            editTelephone1.setText(telephone1);
            editTelephone2.setText(telephone2);
            editPassword1.setText(password1);
            editPassword2.setText(password2);
            editAdresse.setText(adresse);
            editLiens.setText(liens);
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {}
}