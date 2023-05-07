package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projet_mobile.Controler.GestionEmploisActivity;
import com.example.projet_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Emplois {
    private static final String URL = "emplois";
    public String[][] donnes;
    public int id;

    public int candidat;
    public int annonce;
    public String nomAnnonce;

    public Emplois(int candidat){this.candidat = candidat;}

    public Emplois(int id, int candidat, int annonce, String nomAnnonce){
        this.id = id;
        this.candidat = candidat;
        this.annonce = annonce;
        this.nomAnnonce = nomAnnonce;
    }

    // -------------------------------------ADAPTER--------------------------------------------
    public static class EmploisAdapter extends BaseAdapter {

        Context context;
        private final String[] joursL;
        private final int[] jours;
        private final String[][] titres;
        GestionEmploisActivity gestion;
        LayoutInflater inflater;

        public EmploisAdapter(Context context, String[] joursL, int[] jours, String[][] titres, GestionEmploisActivity gestion) {
            this.context = context;
            this.joursL = joursL;
            this.jours = jours;
            this.titres = titres;
            this.inflater = LayoutInflater.from(context);
            this.gestion = gestion;
        }

        @Override
        public int getCount() {return jours.length;}
        @Override
        public Object getItem(int i) {return null;}
        @Override
        public long getItemId(int i) {return 0;}

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_affichage_emplois, null);

            LinearLayout layoutEvents = view.findViewById(R.id.layoutEvent);

            TextView joursTVL = view.findViewById(R.id.idJoursL);
            String s = String.valueOf(joursL[i]);
            joursTVL.setText(s);

            TextView joursTV = view.findViewById(R.id.idJours);
            s = String.valueOf(jours[i]);
            joursTV.setText(s);

            String[] titre = titres[i];

            for (int j = 0; j < titre.length; j++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout layoutEvent = new LinearLayout(context);
                layoutEvent.setOrientation(LinearLayout.VERTICAL);
                layoutEvent.setPadding(10, 0, 10, 0);
                layoutEvent.setLayoutParams(layoutParams);
                layoutEvent.setOnClickListener(v -> gestion.click2(i));

                if (!titre[j].equals(" "))
                    switch (j) {
                        case 0:
                            layoutEvent.setBackgroundColor(context.getResources().getColor(R.color.col1));
                            break;
                        case 1:
                            layoutEvent.setBackgroundColor(context.getResources().getColor(R.color.col22));
                            break;
                        case 2:
                            layoutEvent.setBackgroundColor(context.getResources().getColor(R.color.col3));
                            break;
                        case 3:
                            layoutEvent.setBackgroundColor(context.getResources().getColor(R.color.col4));
                            break;
                        case 4:
                            layoutEvent.setBackgroundColor(context.getResources().getColor(R.color.col5));
                            break;
                    }

                TextView textTitre = new TextView(context);
                textTitre.setText(titre[j]);
                textTitre.setTextSize(16);
                textTitre.setTypeface(null, Typeface.BOLD);
                layoutEvent.addView(textTitre);

                TextView textHeureDeb = new TextView(context);
                textHeureDeb.setTextSize(14);
                layoutEvent.addView(textHeureDeb);

                TextView textHeureFin = new TextView(context);
                textHeureFin.setTextSize(14);
                layoutEvent.addView(textHeureFin);

                layoutEvents.addView(layoutEvent);
            }
            return view;
        }
    }


    // ----------------------------- insert ------------------------------
    public void ajouter(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "insert");
            postData.put("id", id);
            postData.put("candidat_inscrit", candidat);
            postData.put("annonce", annonce);
            postData.put("nomAnnonce", nomAnnonce);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if (response.getString("success").equals("true")) {
                            Toast.makeText(context, "succes !!", Toast.LENGTH_SHORT).show();
                            callback.onSuccess();
                        } else {
                            Toast.makeText(context, "Un probleme est survenu. Veillez reessayer !!", Toast.LENGTH_SHORT).show();
                            callback.onError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error instanceof NetworkError) {
                        Toast.makeText(context, "Pas de connexion Internet !", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(context, "Probleme lors de la creation !", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Erreur lors de l'enregistrement. Veuillez reesayez !", Toast.LENGTH_SHORT).show();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    // ----------------------------- select ------------------------------
    public void recupDonnesPrecis(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select precis");
            postData.put("candidat_inscrit", candidat);
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
                            donnes = new String[jsonArray.length()][3];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataElement = jsonArray.getJSONObject(i);
                                donnes[i][0] = dataElement.getString("annonce");
                                donnes[i][1] = dataElement.getString("nomAnnonce");
                                donnes[i][2] = dataElement.getString("date_debut");
                            }
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
