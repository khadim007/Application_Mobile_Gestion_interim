package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projet_mobile.Controler.AccueilActivity;
import com.example.projet_mobile.Controler.RechercheActivity;
import com.example.projet_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Annonce {
    private static String URL = "annonce";
    private int id;

    public String nom;
    public String description;
    public String employeur;
    public String remuneration;
    public String date_debut;
    public String date_fin;
    public String metier;
    public String ville;
    public String duree;
    public String mot_cles;
    public String type;
    public String descriptionEn;

    public Annonce(String nom, String description, String employeur, String remuneration, String date_debut, String date_fin, String metier, String ville, String duree, String mot_cles){
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

    public Annonce(int id){
        this.id = id;
    }

    // -------------------------------------ADAPTER--------------------------------------------
    public static class AnnonceAdapter extends BaseAdapter {
        private AccueilActivity accueil;
        private RechercheActivity recherche;
        private final LayoutInflater inflater;
        public String[][] donnes;

        public AnnonceAdapter(Context context, AccueilActivity accueil, String[][] donnes) {
            this.accueil = accueil;
            this.inflater = LayoutInflater.from(context);
            this.donnes = new String[donnes.length][donnes[0].length];
            for(int i = 0; i < donnes.length; i++){
                for(int j = 0; j < donnes[i].length; j++){
                    this.donnes[i][j] = donnes[i][j];
                }
            }
        }

        public AnnonceAdapter(Context context, RechercheActivity recherche, String[][] donnes) {
            this.recherche = recherche;
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

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(accueil != null) {
                view = inflater.inflate(R.layout.activity_affiche_annonce, null);

                TextView nom = view.findViewById(R.id.textNom);
                TextView ren = view.findViewById(R.id.textRenumeration);
                TextView dat = view.findViewById(R.id.textDate);
                TextView met = view.findViewById(R.id.textMetier);
                TextView vil = view.findViewById(R.id.textVille);

                nom.setText(this.donnes[i][1] + " (H/F)");
                ren.setText(this.donnes[i][4] + " € par heure");
                dat.setText(this.donnes[i][5] + " - " + this.donnes[i][9]);
                met.setText(this.donnes[i][7]);
                vil.setText(this.donnes[i][8]);

                String id = this.donnes[i][0];
                accueil.bouttonPartager = view.findViewById(R.id.buttonPartage);
                accueil.bouttonConsulter = view.findViewById(R.id.buttonConsuler);
                accueil.bouttonCandidater = view.findViewById(R.id.buttonCandidater);
                accueil.bouttonPartager.setOnClickListener(v -> accueil.click2(id, "partager"));
                accueil.bouttonConsulter.setOnClickListener(v -> accueil.click2(id, "consulter"));
                accueil.bouttonCandidater.setOnClickListener(v -> accueil.click2(id, "candidater"));

                return view;
            }else{
                view = inflater.inflate(R.layout.activity_affiche_annonce, null);

                TextView nom = view.findViewById(R.id.textNom);
                TextView ren = view.findViewById(R.id.textRenumeration);
                TextView dat = view.findViewById(R.id.textDate);
                TextView met = view.findViewById(R.id.textMetier);
                TextView vil = view.findViewById(R.id.textVille);

                nom.setText(this.donnes[i][1] + " (H/F)");
                ren.setText(this.donnes[i][4] + " € par heure");
                dat.setText(this.donnes[i][5] + " - " + this.donnes[i][9]);
                met.setText(this.donnes[i][7]);
                vil.setText(this.donnes[i][8]);

                String id = this.donnes[i][0];
                recherche.bouttonPartager = view.findViewById(R.id.buttonPartage);
                recherche.bouttonConsulter = view.findViewById(R.id.buttonConsuler);
                recherche.bouttonCandidater = view.findViewById(R.id.buttonCandidater);
                recherche.bouttonPartager.setOnClickListener(v -> recherche.click2(id, "partager"));
                recherche.bouttonConsulter.setOnClickListener(v -> recherche.click2(id, "consulter"));
                recherche.bouttonCandidater.setOnClickListener(v -> recherche.click2(id, "candidater"));

                return view;
            }
        }
    }



    // ----------------------------- select ------------------------------
    public void recupDonnes(Context context, Annonce.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select");
            postData.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getString("donnees").equals("false")){
                            callback.onEmpty();
                        }else {
                            JSONArray jsonArray = response.getJSONArray("donnees");
                            JSONObject dataElement = jsonArray.getJSONObject(0);

                            nom = dataElement.getString("nom");
                            description = dataElement.getString("description");
                            employeur = dataElement.getString("employeur");
                            remuneration = dataElement.getString("remuneration");
                            date_debut = dataElement.getString("date_debut");
                            date_fin = dataElement.getString("date_fin");
                            metier = dataElement.getString("metier");
                            ville = dataElement.getString("ville");
                            duree = dataElement.getString("duree");
                            mot_cles = dataElement.getString("mot_cles");
                            type = dataElement.getString("type");
                            descriptionEn = dataElement.getString("descriptionEn");

                            callback.onSuccess();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error instanceof NetworkError) {
                        Toast.makeText(context, "Pas de connexion Internet !", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(context, "Probleme de json !", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Erreur lors de l'enregistrement. Veuillez reesayez !", Toast.LENGTH_SHORT).show();
                    }
                    callback.onError();
                });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public interface VolleyCallback {
        void onSuccess();
        void onError();
        void onEmpty();
    }
}