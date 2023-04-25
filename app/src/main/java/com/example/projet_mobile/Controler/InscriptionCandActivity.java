package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.OpenableColumns;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InscriptionCandActivity extends AppCompatActivity implements toolbar {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    private CandidatInscrit candidat;

    private CheckBox checkAccepte;
    private boolean accepte;
    private Button bouttonValider;
    private Button bouttonDeja;

    private String prenom;
    private String nom;
    private String nationalite;
    private String dateNais;
    private String telephone;
    private String email;
    private String password1;
    private String password2;
    private String ville;
    private File cv;

    private EditText editPrenom;
    private EditText editNom;
    private EditText editNationalite;
    private EditText editDateNais;
    private EditText editTelephone;
    private EditText editEmail;
    private EditText editPassword1;
    private EditText editPassword2;
    private EditText editVille;
    private Button editCV;
    private TextView textCV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_cand);

        getID();
        click();
    }

    private void getID(){
        checkAccepte = (CheckBox) findViewById(R.id.checkAccepte);

        editPrenom = (EditText) findViewById(R.id.editPrenom);
        editNom = (EditText) findViewById(R.id.editNom);
        editNationalite = (EditText) findViewById(R.id.editNationalite);
        editDateNais = (EditText) findViewById(R.id.editDateNais);
        editTelephone = (EditText) findViewById(R.id.editTéléphone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword1 = (EditText) findViewById(R.id.editPassword1);
        editPassword2 = (EditText) findViewById(R.id.editPassword2);
        editVille = (EditText) findViewById(R.id.editVille);
        editCV = (Button) findViewById(R.id.editCV);
        textCV = (TextView) findViewById(R.id.textCV);

        bouttonValider = (Button) findViewById(R.id.buttonValider);
        bouttonDeja = (Button) findViewById(R.id.buttonConnecter);
        accepte = false;
    }

    private void click(){
        editCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), FILE_PICKER_REQUEST_CODE);
            }
        });
        bouttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prenom = editPrenom.getText().toString();
                nom = editNom.getText().toString();
                nationalite = editNationalite.getText().toString();
                dateNais = editDateNais.getText().toString();
                telephone = editTelephone.getText().toString();
                email = editEmail.getText().toString();
                password1 = editPassword1.getText().toString();
                password2 = editPassword2.getText().toString();
                ville = editVille.getText().toString();

                if(checkAccepte.isChecked())
                    accepte = true;
                exec();
            }
        });
        bouttonDeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( InscriptionCandActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exec(){
        if (!(password1.equals(password2))) {
            Toast.makeText(this, "Les deux passwords ne sont pas identiques !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (prenom.isEmpty() || nom.isEmpty() || password1.isEmpty()) {
            Toast.makeText(this, "Tous les champs obligatoire doivent etre remplis !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty() && telephone.isEmpty()) {
            Toast.makeText(this, "Il faut renseigne soit l'email, soit le telephne !!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent( InscriptionCandActivity.this, InsValidationActivity.class);
        intent.putExtra("role", "candidat");
        intent.putExtra("prenom", prenom);
        intent.putExtra("nom", nom);
        intent.putExtra("nationalite", nationalite);
        intent.putExtra("dateNais", dateNais);
        intent.putExtra("telephone", telephone);
        intent.putExtra("email", email);
        intent.putExtra("password", password1);
        intent.putExtra("ville", ville);
        intent.putExtra("cv", cv);
        intent.putExtra("accepte", accepte);
        startActivity(intent);
    }

//    public void enregistre(){
//        candidat.ajouter(this);
//    }
// Pour le fichier a choisir
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
                textCV.setText(fileName);

                // recuperer les donnes du fichier
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                    byte[] fileContent = new byte[inputStream.available()];
                    inputStream.read(fileContent);
                    inputStream.close();
                    cv = new File(getCacheDir(), fileName);
                    FileOutputStream outputStream = new FileOutputStream(cv);
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

    public void onImageCompteClick(View view) {
        onImageCompteClick(this);
    }
}