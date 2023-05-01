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
import com.example.projet_mobile.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CandidatureActivity extends AppCompatActivity implements toolbar {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    int partie;
    int id;

    int idAnnonce;
    String nomAnnonce;

    private String prenom;
    private String nom;
    private String nationalite;
    private String dateNais;
    private File cv;
    private File lettre;

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
        setContentView(R.layout.activity_candidature);
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
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click(){
        editCV.setOnClickListener(v -> {
            partie = 1;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), FILE_PICKER_REQUEST_CODE);
        });
        editLettre.setOnClickListener(v -> {
            partie = 2;
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
        if (prenom.isEmpty() || nom.isEmpty() || nationalite.isEmpty() || dateNais.isEmpty() || cv.length() ==0) {
            Toast.makeText(this, "Tous les champs obligatoire doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent( CandidatureActivity.this, AccueilActivity.class);
        startActivity(intent);
    }

    private void getDonnes(){
        candidat = new CandidatInscrit(id);
        candidat.getDonnes(this, new CandidatInscrit.VolleyCallback() {
            @Override
            public void onSuccess() {affichage(candidat.affiche);}
            @Override
            public void onError() {affichageError(candidat.affiche);}
        });
    }

    @SuppressLint("SetTextI18n")
    private void affichage(String s){
        if(CandidatInscrit.succes.equals(s)){
            Intent intent = getIntent();
            idAnnonce = intent.getIntExtra("id", 0);
            nomAnnonce = intent.getStringExtra("nom");

            textAnnonce.setText(nomAnnonce);
            editPrenom.setText(candidat.prenom);
            editNom.setText(candidat.nom);
            editNationalite.setText(candidat.nationalite);
            editDateNais.setText(candidat.dateNais);
        }else{
            affichageError(s);
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

                // recuperer le nom du fichier
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
                if(partie == 1)
                    textCV.setText(fileName);
                else
                    textLettre.setText(fileName);

                // recuperer les donnes du fichier
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                    byte[] fileContent = new byte[inputStream.available()];
                    inputStream.read(fileContent);
                    inputStream.close();
                    FileOutputStream outputStream;
                    if(partie == 1) {
                        cv = new File(getCacheDir(), fileName);
                        outputStream = new FileOutputStream(cv);
                    }else {
                        lettre = new File(getCacheDir(), fileName);
                        outputStream = new FileOutputStream(lettre);
                    }
                    outputStream.write(fileContent);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, "Veillez verifier si le fichier choisi est bien un pdf valide !!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Veillez verifier si le fichier choisi est bien un pdf valide !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {}
    public void onCompteClick(View view) {onCompteClick(this);}
}