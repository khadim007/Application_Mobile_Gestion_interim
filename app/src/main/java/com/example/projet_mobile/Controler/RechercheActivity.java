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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Accueil;
import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.R;

public class RechercheActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;

    private Accueil accueil;
    private ListView listView;
    private TextView affErreur;

    private Button bouttonRecherche;
    public Button bouttonPartager;
    public Button bouttonConsulter;
    public Button bouttonCandidater;

    private String type;
    private String specialite;
    private String lieu;

    private Spinner editType;
    private EditText editSpecialite;
    private EditText editLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
        recherche();
    }

    private void getID(){
        editType = findViewById(R.id.editType);
        editSpecialite = findViewById(R.id.editSpecialite);
        editLieu = findViewById(R.id.editLieu);
        affErreur = findViewById(R.id.affError);

        listView = findViewById(R.id.idListView);
        bouttonRecherche = findViewById(R.id.bouttonRecherche);

        ImageView im = findViewById(R.id.imRecherche);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click() {
        bouttonRecherche.setOnClickListener(v -> {
            type = editType.getSelectedItem().toString();
            specialite = editSpecialite.getText().toString();
            lieu = editLieu.getText().toString();
            exec();
        });
    }

    private void recherche() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        specialite = intent.getStringExtra("specialite");
        lieu = intent.getStringExtra("lieu");
        if(type != null && specialite != null && lieu != null)
            exec();
    }

    public void click2(String id, String nom) {
        int ident = sharedPreferences.getInt("id", 0);
        if("partager".equals(nom)){
            if(ident == 0){
                Intent intent = new Intent( RechercheActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        }else if("consulter".equals(nom)){
            Intent intent = new Intent( RechercheActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
            startActivity(intent);
        }else if("candidater".equals(nom)){
            if(ident == 0){
                Intent intent = new Intent( RechercheActivity.this, AuthentificationActivity.class);
                startActivity(intent);
            }
        }
    }

    private void exec(){
        accueil = new Accueil(type, specialite, lieu);
        accueil.recupDonnes(this, new Accueil.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
            @Override
            public void onEmpty() {
                affichageEmpty();
            }
        });
    }

    private void affichage(){
        affErreur.setTextSize(26);
        affErreur.setText(accueil.donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, accueil.donnes);
        listView.setAdapter(annonces);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AnnonymeRecherche1", type);
        editor.putString("AnnonymeRecherche2", specialite);
        editor.putString("AnnonymeRecherche3", lieu);
        editor.commit();
    }

    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    private void affichageEmpty(){affErreur.setText("Aucune annonce est trouvée !!"); affErreur.setTextSize(16);}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}