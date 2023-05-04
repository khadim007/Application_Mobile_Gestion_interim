package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Notification;
import com.example.projet_mobile.R;

public class OffreActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Notification notification;
    Annonce annonce;
    String role;
    int id;

    private ListView listView;

    private TextView affErreur;
    private TextView textTitre;

    public Button bouttonPartager;
    public Button bouttonConsulter;
    public Button bouttonCandidater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        getID();
        getDonnes();
    }

    private void getID(){
        affErreur = findViewById(R.id.affError);
        textTitre = findViewById(R.id.textTitre);

        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    public void click2(String ident, String nom, String annonce) {
        Intent intent = null;
        if("partager".equals(nom)){
            if(id == 0){
                intent = new Intent( OffreActivity.this, AuthentificationActivity.class);
            }
        }else if("consulter".equals(nom)){
            intent = new Intent( OffreActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(ident));
        }else if("candidater".equals(nom)){
            if(role.equals("candidat")){
                intent = new Intent( OffreActivity.this, CandidatureOffreActivity.class);
                intent.putExtra("id", Integer.parseInt(ident));
                intent.putExtra("nom", annonce);
            }else {
                intent = new Intent( OffreActivity.this, AuthentificationActivity.class);
            }
        }
        startActivity(intent);
    }

    public void getDonnes(){
        notification = new Notification(id);
        notification.recupDonnes(this, new Notification.VolleyCallback() {
            @Override
            public void onSuccess() {getDonnes2();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {affichageEmpty();}
        });
    }

    public void getDonnes2(){
        annonce = new Annonce();
        annonce.recupDonnesTab(this, notification.id_donnes, new Annonce.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {}
        });
    }

    private void affichage(){
        affErreur.setText(notification.id_donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, annonce.donnes);
        listView.setAdapter(annonces);
    }

    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    private void affichageEmpty(){affErreur.setText("Aucun notification est trouvée pour vous !!");}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}