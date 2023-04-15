package com.example.projet_mobile.Modele;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.projet_mobile.Controler.AccueilActivity;
import com.example.projet_mobile.R;

import java.util.ArrayList;

public class Annonce {
    private int id;

    private String nom;
    private String description;
    private int employeur;
    private double remuneration;
    private String date_debut;
    private String date_fin;
    private String metier;
    private String ville;
    private String duree;
    private String mot_cles;

    public Annonce(String nom, String description, int employeur, double remuneration, String date_debut, String date_fin, String metier, String ville, String duree, String mot_cles){
        this.nom = nom;
        this.description = description;
        this.employeur = employeur;
        this.remuneration = remuneration;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.metier = metier;
        this.ville = ville;
        this.duree = duree;
        this.mot_cles = mot_cles;
    }




    // -------------------------------------ADAPTER--------------------------------------------
    public static class AnnonceAdapter extends BaseAdapter {
        private Context context;
        private AccueilActivity accueil;
        private LayoutInflater inflater;
        public String donnes[][];

        public AnnonceAdapter(Context context, AccueilActivity accueil,  String donnes[][]) {
            this.context = context;
            this.accueil = accueil;
            this.inflater = LayoutInflater.from(context);
            this.donnes = new String[donnes.length][donnes[0].length];
            for(int i = 0; i < donnes.length; i++){
                for(int j = 0; j < donnes[i].length; j++){
                    this.donnes[i][j] = donnes[i][j];
                }
            }
        }

        @Override
        public int getCount() {
            return this.donnes.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_accueil_annonce, null);

            TextView nom = (TextView) view.findViewById(R.id.textNom);
            TextView ren = (TextView) view.findViewById(R.id.textRenumeration);
            TextView dat = (TextView) view.findViewById(R.id.textDate);
            TextView met = (TextView) view.findViewById(R.id.textMetier);
            TextView vil = (TextView) view.findViewById(R.id.textVille);

            nom.setText(this.donnes[i][1]+" (H/F)");
            ren.setText(this.donnes[i][4]+" € par heure");
            dat.setText(this.donnes[i][5]+" - "+this.donnes[i][9]);
            met.setText(this.donnes[i][7]);
            vil.setText(this.donnes[i][8]);

            String id = this.donnes[i][0];
            accueil.bouttonPartager = (Button) view.findViewById(R.id.buttonPartage);
            accueil.bouttonConsulter = (Button) view.findViewById(R.id.buttonConsuler);
            accueil.bouttonCandidater = (Button) view.findViewById(R.id.buttonCandidater);
            accueil.bouttonPartager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accueil.click2(id, "partager");
                }
            });
            accueil.bouttonConsulter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accueil.click2(id, "consulter");
                }
            });
            accueil.bouttonCandidater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accueil.click2(id, "candidater");
                }
            });

            return view;
        }
    }
}