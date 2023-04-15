package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.R;

public class EspaceCandidatInscritActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;

    private TextView textErreur;


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
    }

    private void click(){

    }

    private void getDonnes(){
        int id = sharedPreferences.getInt("id", 0);
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

    private void affichage(String s){
        if(CandidatInscrit.succes.equals(s)){

        }else{
            affichageError(s);
        }
    }

    private void affichageError(String s){
        textErreur.setText(s);
    }

    public void onImageCompteClick(View view) {} // laisse comm ca
}