package com.example.projet_mobile.Controler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.Modele.Emplois;
import com.example.projet_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class GestionCandidatureActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Candidature candidature;
    Annonce annonce;
    String role;
    int id;

    public Button bouttonConsulter;
    public Button bouttonModifier;
    public Button bouttonSupprimer;
    public Button bouttonContacter;
    public Button bouttonPartager;
    public Button bouttonCandidater;

    private Spinner spinnerType;
    private TextView textTitre;
    private TextView affErreur;

    private LinearLayout layoutRecherche;
    private EditText editOffre;
    private EditText editDate;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_candidature);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        role = sharedPreferences.getString("role", "");

        getID();
        aff();
    }

    private void getID(){
        spinnerType = findViewById(R.id.spinnerType);
        textTitre = findViewById(R.id.textTitre);
        affErreur = findViewById(R.id.affError);

        layoutRecherche = findViewById(R.id.layoutRecherche);
        editOffre = findViewById(R.id.editOffre);
        editDate = findViewById(R.id.editDate);

        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    private void aff() {
        if("employeur".equals(role) || "agence".equals(role)){
            List<String> listeValeurs = new ArrayList<>();
            listeValeurs.add("Consulter les candidatures");
            listeValeurs.add("Chercher une candidature");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listeValeurs);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(adapter);
            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {getDonnesE();}
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }else {
            layoutRecherche.setVisibility(View.GONE);
            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {getDonnes();}
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            getDonnes();
        }
    }

    public void click2(String id, String annonce, String nom, String nomAnnonce) {
        Intent intent;
        if("consulter".equals(nom) || "contacter".equals(nom)){
            intent = new Intent( GestionCandidatureActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(annonce));
            startActivity(intent);
        }else if("modifier".equals(nom)){
            intent = new Intent( GestionCandidatureActivity.this, CandidatureOffreActivity.class);
            intent.putExtra("id", Integer.parseInt(annonce));
            intent.putExtra("nom", nomAnnonce);
            intent.putExtra("id_candidature", Integer.parseInt(id));
            startActivity(intent);
        }else if("supprimer".equals(nom)){
            candidature.id = Integer.parseInt(id);
            candidature.supp(this, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {getDonnes();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {}
            });
        }else if("accepter".equals(nom) || "refuser".equals(nom)){
            candidature.id = Integer.parseInt(id);
            candidature.modifierEtat(this, nom, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {getDonnes();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {}
            });

            int idd = sharedPreferences.getInt("id", 0);
            if("accepter".equals(nom)){
                Emplois emplois = new Emplois(Integer.parseInt(id), idd, Integer.parseInt(annonce), nomAnnonce);
                emplois.ajouter(this, new Emplois.VolleyCallback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onError() {}
                    @Override
                    public void onEmpty() {}
                });
            }
        }else if("consulter emp".equals(nom)){
            intent = new Intent( GestionCandidatureActivity.this, VoirCandidatureActivity.class);
            intent.putExtra("annonce", Integer.parseInt(annonce));
            intent.putExtra("nomAnnonce", nomAnnonce);
            startActivity(intent);
        }
    }

    public void onEnvoyerClick(View view) {
        String offre = editOffre.getText().toString();
        String date = editDate.getText().toString();
        if(offre.isEmpty() && date.isEmpty()){Toast.makeText(this, "Au moins un des champs doit etre remplis !!", Toast.LENGTH_SHORT).show(); return; }
        if(offre.isEmpty()) offre = "--------"; if(date.isEmpty()) date = "--------";

        annonce = new Annonce();
        if(role.equals("agence")){
            annonce.agence = String.valueOf(id); annonce.nom = offre; annonce.date_debut = date;
            annonce.recupDonnesEmpRech(this, role, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {affichageE();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {affichageEmpty();}
            });
        }else{
            annonce.employeur = String.valueOf(id); annonce.nom = offre; annonce.date_debut = date;
            annonce.recupDonnesEmpRech(this, role, new Annonce.VolleyCallback() {
                @Override
                public void onSuccess() {affichageE();}
                @Override
                public void onError() {affichageError();}
                @Override
                public void onEmpty() {affichageEmpty();}
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void getDonnes(){
        String type = spinnerType.getSelectedItem().toString();
        switch (type) {
            case "Cand. en cours acceptees":
                textTitre.setText("Vos Cand. en cours acceptees");
                candidature = new Candidature(id, "en cours acceptees");
                break;
            case "Cand. en cours non acceptees":
                textTitre.setText("Vos Cand. en cours non acceptees");
                candidature = new Candidature(id, "en cours non acceptees");
                break;
            case "Cand. passees":
                textTitre.setText("Vos Cand. passees");
                candidature = new Candidature(id, "passees");
                break;
            case "Cand. potentielles":
                Intent intent = new Intent( GestionCandidatureActivity.this, GestionOffreActivity.class);
                startActivity(intent);
                return;
            default:
                textTitre.setText("Vos Cand. en cours et sans reponse");
                candidature = new Candidature(id, "en cours et sans reponse");
                break;
        }
        candidature.recupDonnes(this, new Candidature.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {affichageEmpty();}
        });
    }

    @SuppressLint("SetTextI18n")
    private void getDonnesE(){
        String type = spinnerType.getSelectedItem().toString();
        if("Chercher une candidature".equals(type)){
            layoutRecherche.setVisibility(View.VISIBLE);
            textTitre.setText("Chercher une candidature");
            listView.setVisibility(View.GONE);
        }else{
            layoutRecherche.setVisibility(View.GONE);
            textTitre.setText("Consulter les candidatures");
            annonce = new Annonce();
            if(role.equals("agence")){
                annonce.agence = String.valueOf(id);
                annonce.recupDonnesEmp(this, role, new Annonce.VolleyCallback() {
                    @Override
                    public void onSuccess() {affichageE();}
                    @Override
                    public void onError() {affichageError();}
                    @Override
                    public void onEmpty() {affichageEmpty();}
                });
            }else{
                annonce.employeur = String.valueOf(id);
                annonce.recupDonnesEmp(this, role, new Annonce.VolleyCallback() {
                    @Override
                    public void onSuccess() {affichageE();}
                    @Override
                    public void onError() {affichageError();}
                    @Override
                    public void onEmpty() {affichageEmpty();}
                });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        affErreur.setText(candidature.donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Candidature.CandidatureAdapter candidatures = new Candidature.CandidatureAdapter(this, this, candidature.donnes);
        listView.setAdapter(candidatures);
    }
    @SuppressLint("SetTextI18n")
    private void affichageE(){
        affErreur.setText(annonce.donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, annonce.donnes);
        listView.setAdapter(annonces);
    }

    @SuppressLint("SetTextI18n")
    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    @SuppressLint("SetTextI18n")
    private void affichageEmpty(){affErreur.setText("Aucune candidature est trouvée !!"); affErreur.setTextSize(16); listView.setVisibility(View.GONE);}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}