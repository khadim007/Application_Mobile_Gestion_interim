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
import com.example.projet_mobile.Controler.AbonnementActivity;
import com.example.projet_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Abonnement {
    private static final String URL = "abonnement";
    String[][] donnes;
    int id;

    public String nom;
    public String prix;
    public String condition;

    public Abonnement(){}

    public Abonnement(int id){this.id = id;}

    public Abonnement(String nom, String prix, String condition){
        this.nom = nom;
        this.prix = prix;
        this.condition = condition;
    }


    // -------------------------------------ADAPTER--------------------------------------------
    public static class AbonnementAdapter extends BaseAdapter {
        private int nbr = 6;
        private Context context;
        private final LayoutInflater inflater;
        private final String[] titres;
        private final String[] prix;
        private final String[] periodes = {"par annonce", "par mois", "par trimestre", "par semestre", "par annee", "a vie"};
        private final String[] conditions;
        private final AbonnementActivity abonnement;

        public AbonnementAdapter(Context context, AbonnementActivity abonnement, Abonnement a){
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.abonnement = abonnement;
            nbr = a.donnes.length;
            titres = new String[nbr];
            conditions = new String[nbr];
            prix = new String[nbr];
            for(int i = 0; i < a.donnes.length; i++){
                titres[i] = a.donnes[i][1];
                prix[i] = a.donnes[i][2];
                conditions[i] = a.donnes[i][3];
            }
        }

        @Override
        public int getCount() {return nbr;}
        @Override
        public Object getItem(int i) {return null;}
        @Override
        public long getItemId(int i) {return 0;}

        @SuppressLint({"ViewHolder", "SetTextI18n"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_abonnement_type, null);

            TextView titre = view.findViewById(R.id.textTitre);
            TextView pr = view.findViewById(R.id.textprix);
            TextView condition = view.findViewById(R.id.textCondition);

            titre.setText(titres[i]);
            pr.setText(prix[i]+" "+periodes[i]);
            condition.setText(conditions[i]);

            abonnement.bouttonSouscrire = view.findViewById(R.id.buttonSouscrire);
            abonnement.bouttonSouscrire.setOnClickListener(v -> abonnement.click2((i+1), "souscrire"));

            return view;
        }
    }


    // ----------------------------- select ------------------------------
    public void recupDonnes(Context context, Abonnement.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select return all");
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
            response -> {
                try {
                    JSONArray jsonArray = response.getJSONArray("donnes");
                    donnes = new String[jsonArray.length()][4];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataElement = jsonArray.getJSONObject(i);
                        donnes[i][0] = dataElement.getString("id_");
                        donnes[i][1] = dataElement.getString("nom");
                        donnes[i][2] = dataElement.getString("prix");
                        donnes[i][3] = dataElement.getString("condition_");

                    }
                    callback.onSuccess();
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



    public void getDonnes(Context context, Abonnement.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select return one");
            postData.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
            response -> {
                try {
                    JSONArray jsonArray = response.getJSONArray("donnes");
                    JSONObject dataElement = jsonArray.getJSONObject(0);
                    nom = dataElement.getString("nom");
                    prix = dataElement.getString("prix");
                    condition = dataElement.getString("condition_");
                    callback.onSuccess();
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
    }
}
