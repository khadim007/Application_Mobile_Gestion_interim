package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Abonnement;
import com.example.projet_mobile.Modele.Accueil;
import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

public class AbonnementActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Abonnement abonnement;
    private ListView listView;
    public Button bouttonSouscrire;

    private TextView affErreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
        exec();
    }

    private void getID(){
        listView = findViewById(R.id.idListView);
        affErreur = (TextView) findViewById(R.id.affError);
    }

    private void click() {

    }

    public void click2(int i, String type) { // dans Abonnement pour le click du button dans listView
        if("souscrire".equals(type)){
            sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("id", 0);
            if(id == 0){
                System.out.println("Il faut qu'il se reconnecte. Code pas encore gerer.");
            }else{
                Employeur e = new Employeur(id);
                e.changeAbonnement(this, i, new Employeur.VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent( AbonnementActivity.this, AccueilActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onError() {
                        affichageError();
                    }
                });
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

    private void affichageError(){
        affErreur.setText("Probleme de connexion. Veillez reesayez !!");
    }

    public void onImageCompteClick(View view) {
        onImageCompteClick(this);
    }
}