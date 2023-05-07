package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Emplois;
import com.example.projet_mobile.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class GestionEmploisActivity extends AppCompatActivity implements toolbar {

    SharedPreferences sharedPreferences;
    Emplois emplois;
    ListView listView;
    Calendar calendar;
    int id;

    String[][] titres;
    String[] joursL;
    int[] jours;

    TextView textSemaine;
    Button button1;
    Button button2;
    Button button3;
    Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_emplois);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();
        getDonnes();
    }

    private void getID(){
        listView = findViewById(R.id.idListView);
        textSemaine = findViewById(R.id.textSemaine);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        titres = new String[7][0];
    }

    private void click(){
        button1.setOnClickListener(v -> aff());
        button3.setOnClickListener(v -> aff());
        button4.setOnClickListener(v -> aff());
    }

    public void click2(int ii){
        int ident = 0;
        for (int i = 0; i < emplois.donnes.length; i ++){
            String d = emplois.donnes[i][2];
            String[] d2 = d.split("/");
            d = d2[0];

            if(d.equals(String.valueOf(jours[0]))){
                ident++;
            }else if(d.equals(String.valueOf(jours[1]))){
                ident++;
            }else if(d.equals(String.valueOf(jours[2]))){
                ident++;
            }else if(d.equals(String.valueOf(jours[3]))){
                ident++;
            }else if(d.equals(String.valueOf(jours[4]))){
                ident++;
            }else if(d.equals(String.valueOf(jours[5]))){
                ident++;
            }else if(d.equals(String.valueOf(jours[6]))){
                ident++;
            }
            if(ii == i) break;
        }

        Intent intent = new Intent( GestionEmploisActivity.this, VoirAnnonceActivity.class);
        intent.putExtra("id", Integer.parseInt(emplois.donnes[ident-1][0]));
        startActivity(intent);
    }

    private void aff(){Toast.makeText(this, R.string.textNon, Toast.LENGTH_SHORT).show();}


    private void getDonnes(){
        jours = new int[7];
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 7; i++) {
            jours[i] = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DATE, 1);
        }
        joursL = new String[]{"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};

        id = sharedPreferences.getInt("id", 0);
        emplois = new Emplois(id);
        emplois.recupDonnesPrecis(this, new Emplois.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {}
        });
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        textSemaine.setText(monthName+" "+jours[0]+"-"+jours[6]);
        button2.setText(jours[0]+"-"+jours[6]);

        for (int i = 0; i < emplois.donnes.length; i ++){
            String d = emplois.donnes[i][2];
            String[] d2 = d.split("-");
            d = d2[2];
            if(Integer.parseInt(d) == jours[0]){
                titres[0] = Arrays.copyOf(titres[0], titres[0].length + 1);
                titres[0][titres[0].length - 1] = emplois.donnes[i][1];
            }else if(Integer.parseInt(d) == jours[1]){
                titres[1] = Arrays.copyOf(titres[1], titres[1].length + 1);
                titres[1][titres[1].length - 1] = emplois.donnes[i][1];
            }else if(Integer.parseInt(d) == jours[2]){
                titres[2] = Arrays.copyOf(titres[2], titres[2].length + 1);
                titres[2][titres[2].length - 1] = emplois.donnes[i][1];
            }else if(Integer.parseInt(d) == jours[3]){
                titres[3] = Arrays.copyOf(titres[3], titres[3].length + 1);
                titres[3][titres[3].length - 1] = emplois.donnes[i][1];
            }else if(Integer.parseInt(d) == jours[4]){
                titres[4] = Arrays.copyOf(titres[4], titres[4].length + 1);
                titres[4][titres[4].length - 1] = emplois.donnes[i][1];
            }else if(Integer.parseInt(d) == jours[5]){
                titres[5] = Arrays.copyOf(titres[5], titres[5].length + 1);
                titres[5][titres[5].length - 1] = emplois.donnes[i][1];
            }else if(Integer.parseInt(d) == jours[6]){
                titres[6] = Arrays.copyOf(titres[6], titres[6].length + 1);
                titres[6][titres[6].length - 1] = emplois.donnes[i][1];
            }
        }
        for (int i = 0; i < 7; i ++){
            if(titres[i].length == 0){
                titres[i] = Arrays.copyOf(titres[i], titres[i].length + 1);
                titres[i][titres[i].length - 1] = " ";
            }
        }

        Emplois.EmploisAdapter horaires = new Emplois.EmploisAdapter(this, joursL, jours, titres, this);
        listView.setAdapter(horaires);
    }
    private void affichageError(){Toast.makeText(this, "Aucun emplois est trouve !!", Toast.LENGTH_SHORT).show();}

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}