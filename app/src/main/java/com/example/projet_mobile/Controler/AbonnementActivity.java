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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Abonnement;
import com.example.projet_mobile.Modele.Agence;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class AbonnementActivity extends AppCompatActivity implements toolbar {
    SharedPreferences sharedPreferences;
    Abonnement abonnement;
    String role;
    int id;

    private TextView affErreur;
    private ListView listView;
    public Button bouttonSouscrire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        id = sharedPreferences.getInt("id", 0);

        getID();
        click();
        exec();
    }

    private void getID(){
        affErreur = findViewById(R.id.affError);
        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click() {}

    public void click2(int i, String type) {
        if("souscrire".equals(type)){
            sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
            if(id == 0){
                Intent intent = new Intent( AbonnementActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }else{
                if(role.equals("agence")){
                    Agence a = new Agence(id);
                    a.changeAbonnement(this, i, new Agence.VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent( AbonnementActivity.this, AccueilActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onError() {affichageError();}
                    });
                }else{
                    Employeur e = new Employeur(id);
                    e.changeAbonnement(this, i, new Employeur.VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent( AbonnementActivity.this, AccueilActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onError() {affichageError();}
                    });
                }
            }
        }
    }

    private void exec(){
        abonnement = new Abonnement();
        abonnement.recupDonnes(this, new Abonnement.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
        });
    }

    private void affichage(){
        Abonnement.AbonnementAdapter aboAdap = new Abonnement.AbonnementAdapter(this, this, abonnement);
        listView.setAdapter(aboAdap);
    }
    @SuppressLint("SetTextI18n")
    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {}
}