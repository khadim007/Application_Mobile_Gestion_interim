package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.projet_mobile.R;

public class EspaceCandidatInscritActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    int id;

    private TextView textErreur;
    private TextView textNom;

    private EditText editTelephone;
    private EditText editNationalite;
    private EditText editDateNais;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editVille;

    private Button bouttonModifier;


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
        textErreur = (TextView) findViewById(R.id.textErreur);
        textNom = (TextView) findViewById(R.id.textNom);

        editNationalite = (EditText) findViewById(R.id.editNationalite);
        editDateNais = (EditText) findViewById(R.id.editDateNais);
        editTelephone = (EditText) findViewById(R.id.editTelephone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editVille = (EditText) findViewById(R.id.editVille);

        bouttonModifier = (Button) findViewById(R.id.buttonModifier);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void click(){
        bouttonModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nationalite;
                String dateNais;
                String telephone;
                String email;
                String password;
                String ville;

                nationalite = editNationalite.getText().toString();
                dateNais = editDateNais.getText().toString();
                telephone = editTelephone.getText().toString();
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();
                ville = editVille.getText().toString();

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
            }
        });
    }

    private void getDonnes(){
        id = sharedPreferences.getInt("id", 0);
        candidat = new CandidatInscrit(id);
        candidat.getDonnes(this, new CandidatInscrit.VolleyCallback() {
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

    private void affichageError(String s){
        textErreur.setText(s);
    }

    public void onHomeClick(View view) {
        onHomeClick(this);
    }
    public void onRechercheClick(View view) {
        onRechercheClick(this);
    }
    public void onCompteClick(View view) {} // laisse comm ca

    public void onCardCV(View view) {
        System.out.println("==========================cv");
    }

    public void onCardLettre(View view) {
        System.out.println("==========================L");
    }

    public void onCardCandidatures(View view) {
        System.out.println("==========================C");
    }

    public void onCardOffres(View view) {
        System.out.println("==========================O");
    }

    public void onCardEmplois(View view) {
        System.out.println("==========================E");
    }

    public void onCardDeconnexion(View view) {
        System.out.println("==========================E");
    }
}