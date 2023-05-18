package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CandidatureOffreActivity extends AppCompatActivity implements toolbar {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Candidature candidature;
    String partie;
    int id;

    int idAnnonce;
    String nomAnnonce;

    private String prenom;
    private String nom;
    private String nationalite;
    private String dateNais;
    private byte[] cv;
    private byte[] lettre;

    private EditText editPrenom;
    private EditText editNom;
    private EditText editNationalite;
    private EditText editDateNais;

    private Button editCV;
    private Button editLettre;
    private Button bouttonEnvoyer;

    private TextView affErreur;
    private TextView textAnnonce;
    private TextView textCV;
    private TextView textLettre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_offre);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        editPrenom = findViewById(R.id.editPrenom);
        editNom = findViewById(R.id.editNom);
        editNationalite = findViewById(R.id.editNationalite);
        editDateNais = findViewById(R.id.editDateNais);
        editCV = findViewById(R.id.editCV);
        editLettre = findViewById(R.id.editLettre);

        affErreur = findViewById(R.id.affError);
        textAnnonce = findViewById(R.id.textAnnonce);
        textCV = findViewById(R.id.textCV);
        textLettre = findViewById(R.id.textLettre);

        bouttonEnvoyer = findViewById(R.id.buttonValider);

        ImageView im = findViewById(R.id.imCandidature);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    private void click(){
        editCV.setOnClickListener(v -> {
            partie = "cv";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), FILE_PICKER_REQUEST_CODE);
        });
        editLettre.setOnClickListener(v -> {
            partie = "lettre";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), FILE_PICKER_REQUEST_CODE);
        });
        bouttonEnvoyer.setOnClickListener(v -> {
            prenom = editPrenom.getText().toString();
            nom = editNom.getText().toString();
            nationalite = editNationalite.getText().toString();
            dateNais = editDateNais.getText().toString();
            exec();
        });
    }

    private void exec(){
        if (prenom.isEmpty() || nom.isEmpty() || nationalite.isEmpty() || dateNais.isEmpty() || cv == null) {
            Toast.makeText(this, "Tous les champs obligatoire doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        int id_candidature = intent.getIntExtra("id_candidature", 0);
        if(id_candidature != 0){
            candidature = new Candidature(id, idAnnonce, nomAnnonce, prenom, nom, nationalite, dateNais, cv, lettre);
            candidature.id = id_candidature;
            candidature.modifierOffre(this, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {Intent intent = new Intent( CandidatureOffreActivity.this, GestionCandidatureActivity.class);startActivity(intent);}
                @Override
                public void onError() {}
                @Override
                public void onEmpty() {}
            });
        }else {
            candidature = new Candidature(id, idAnnonce, nomAnnonce, prenom, nom, nationalite, dateNais, cv, lettre);
            candidature.ajouterOffre(this, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {Intent intent = new Intent( CandidatureOffreActivity.this, RechercheActivity.class);startActivity(intent);}
                @Override
                public void onError() {}
                @Override
                public void onEmpty() {}
            });
        }
    }

    private void getDonnes(){
        Intent intent = getIntent();
        int id_candidature = intent.getIntExtra("id_candidature", 0);
        if(id_candidature != 0){
            candidature = new Candidature(id_candidature);
            candidature.recupDonnesPrecis(this, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError(candidat.affiche);}
                @Override
                public void onEmpty() {}
            });
        }else{
            candidat = new CandidatInscrit(id);
            candidat.getDonnesCand(this, new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError("Error");}
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        Intent intent = getIntent();
        idAnnonce = intent.getIntExtra("id", 0);
        nomAnnonce = intent.getStringExtra("nom");
        int id_candidature = intent.getIntExtra("id_candidature", 0);
        if(id_candidature != 0){
            textAnnonce.setText(nomAnnonce);
            editPrenom.setText(candidature.prenom);
            editNom.setText(candidature.nom);
            editNationalite.setText(candidature.nationalite);
            editDateNais.setText(candidature.dateNais);
            if(candidature.cv != null) textCV.setText("Deja un CV !!");
            if(candidature.lettre != null) textLettre.setText("Deja une Lettre !!");
            cv = candidature.cv;
            lettre = candidature.lettre;
        }else{
            textAnnonce.setText(nomAnnonce);
            editPrenom.setText(candidat.prenom);
            editNom.setText(candidat.nom);
            editNationalite.setText(candidat.nationalite);
            editDateNais.setText(candidat.dateNais);
            if(candidat.cv != null) textCV.setText("Deja un CV !!");
            if(candidat.lettre != null) textLettre.setText("Deja une Lettre !!");
            cv = candidat.cv;
            lettre = candidat.lettre;
        }
    }

    private void affichageError(String s){affErreur.setText(s);}

    // -------------------------------------------------------------------------Pour le fichier a choisir
    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedFileUri = data.getData();
                String fileName = null;
                if (selectedFileUri != null) {
                    Cursor cursor = getContentResolver().query(selectedFileUri, null, null, null, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                }
                if(partie.equals("cv")) textCV.setText(fileName);
                else textLettre.setText(fileName);
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }
                    byteArrayOutputStream.close();
                    inputStream.close();
                    if(partie.equals("cv")) cv = byteArrayOutputStream.toByteArray();
                    else lettre = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Veillez verifier si le fichier choisi est bien un pdf valide !!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Veillez verifier si le fichier choisi est bien un pdf valide !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}