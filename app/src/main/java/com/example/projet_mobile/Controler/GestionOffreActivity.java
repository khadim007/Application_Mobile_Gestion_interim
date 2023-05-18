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

import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Notification;
import com.example.projet_mobile.R;

public class GestionOffreActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Notification notification;
    Annonce annonce;
    String role;
    int id;

    private ListView listView;
    private TextView affErreur;

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
        if("candidat".equals(role)) getDonnes();
        else getDonnesE();
    }

    private void getID(){
        affErreur = findViewById(R.id.affError);
        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    public void click2(String ident, String nom, String nomAnnonce) {
        if("partager".equals(nom)){
            if(role.equals("candidat")) {
                Intent intent = new Intent( GestionOffreActivity.this, PartageActivity.class);
                intent.putExtra("id", Integer.parseInt(ident));
                startActivity(intent);
            }else{
                Intent intent = new Intent( GestionOffreActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        }else if("consulter".equals(nom)){
            Intent intent = new Intent( GestionOffreActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(ident));
            startActivity(intent);
        }else if("candidater".equals(nom)){
            if(role.equals("candidat")){
                Intent intent = new Intent( GestionOffreActivity.this, CandidatureOffreActivity.class);
                intent.putExtra("id", Integer.parseInt(ident));
                intent.putExtra("nom", nomAnnonce);
                startActivity(intent);
            }else {
                Intent intent = new Intent( GestionOffreActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        }else if("supprimer".equals(nom)){
            if(role.equals("employeur") || role.equals("agence")){
                annonce.id = Integer.parseInt(ident);
                annonce.supp(this, new Annonce.VolleyCallback() {
                    @Override
                    public void onSuccess() {getDonnesE();}
                    @Override
                    public void onError() {affichageError();}
                    @Override
                    public void onEmpty() {}
                });
            }else {
                Intent intent = new Intent( GestionOffreActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        }else if("modifier".equals(nom)){
            if(role.equals("employeur") || role.equals("agence")){
                Intent intent = new Intent( GestionOffreActivity.this, CandidatureSpontaneActivity.class);
                intent.putExtra("idE", Integer.parseInt(ident));
                startActivity(intent);
            }else {
                Intent intent = new Intent( GestionOffreActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        }
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

    public void getDonnesE(){
        annonce = new Annonce();
        if(role.equals("agence")){
            annonce.agence = String.valueOf(id);
            annonce.recupDonnesEmp(this, role, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {affichageEmpty();}
            });
        }else {
            annonce.employeur = String.valueOf(id);
            annonce.recupDonnesEmp(this, role, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {affichage();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {affichageEmpty();}
            });
        }

    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        if("candidat".equals(role)){
            affErreur.setText(notification.id_donnes.length + " resultats");
            listView.setVisibility(View.VISIBLE);
            Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, annonce.donnes);
            listView.setAdapter(annonces);
        }else {
            affErreur.setText(annonce.donnes.length + " resultats");
            listView.setVisibility(View.VISIBLE);
            Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, annonce.donnes);
            listView.setAdapter(annonces);
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    @SuppressLint("SetTextI18n")
    private void affichageEmpty(){affErreur.setText("Aucun notification est trouvée pour vous !!");}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}