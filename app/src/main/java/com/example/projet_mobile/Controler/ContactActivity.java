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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.SMSetMAIL;
import com.example.projet_mobile.R;

public class ContactActivity extends AppCompatActivity implements toolbar {
    private static final int PERMISSION_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    String text;
    String role;
    int id;

    public SMSetMAIL smsMail;
    public String partie;

    TextView affErreur;
    EditText editSaisi;
    Button buttonEnvoyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        Intent intent = getIntent();
        partie = intent.getStringExtra("partie");

        getID();
        click();
    }

    private void getID(){
        affErreur = findViewById(R.id.affError);
        editSaisi = findViewById(R.id.editSaisi);
        buttonEnvoyer = findViewById(R.id.buttonEnvoyer);

        ImageView im = findViewById(R.id.imRecherche);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click() {
        buttonEnvoyer.setOnClickListener(v -> {
            if(partie.equals("voirCandidature")){
                Intent intent = getIntent();
                int candidature = intent.getIntExtra("id", 0);
                int candidat = intent.getIntExtra("candidat", 0);
                text = "Pour votre candidature avec l'identifiant ["+candidature+"], l'"+role+" vous envoie le message suivant : "+editSaisi.getText().toString();
                CandidatInscrit c = new CandidatInscrit(candidat);
                c.getDonnes(this, new CandidatInscrit.VolleyCallback() {
                    @Override
                    public void onSuccess(){
                        if(c.email.equals(""))envoyerSMS(c.telephone);
                        else envoyerEmail(c.email);
                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onError() {affErreur.setText("Error !!");}
                });
            }else if(partie.equals("gestGestionnaire")){
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");
                text = "Le gestionnaire de l'application Kao Interim vous envoie le message suivant : "+editSaisi.getText().toString();
                envoyerEmail(email);
            }
        });
    }

    private void envoyerSMS(String tel){
        smsMail = new SMSetMAIL(this, tel, text, this);
        smsMail.envoyerSMS();
    }

    private void envoyerEmail(String em){
        smsMail = new SMSetMAIL(this, em, text, this);
        smsMail.envoyerEmail();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                smsMail.go();
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