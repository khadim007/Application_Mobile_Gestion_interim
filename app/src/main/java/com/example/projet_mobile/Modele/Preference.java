package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projet_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Preference {

    private static final String URL = "preference";
    public int id;
    public String affiche;

    public String employeur;
    public String dateDebut;
    public String metier;
    public String ville;

    public Preference(int id){this.id = id;}

    public Preference(int id, String employeur, String dateDebut, String metier, String ville){
        this.id = id;
        this.employeur = employeur;
        this.dateDebut = dateDebut;
        this.metier = metier;
        this.ville = ville;
    }

    // ----------------------------- select ------------------------------
    public void getDonnes(Context context, VolleyCallback callback){
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select return line");
            postData.put("candidat", id);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getString("success").equals("true")) {
                            JSONArray jsonArray = response.getJSONArray("donnes");
                            JSONObject dataElement = jsonArray.getJSONObject(0);

                            employeur = dataElement.getString("employeur");
                            dateDebut = dataElement.getString("date_debut");
                            metier = dataElement.getString("metier");
                            ville = dataElement.getString("ville");

                            callback.onSuccess();
                        } else {
                            callback.onEmpty();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error instanceof NetworkError) {
                        this.affiche = "Pas de connexion Internet !!";
                    } else if (error instanceof ParseError) {
                        this.affiche = "Probleme lors de la verification !!";
                    }else{
                        this.affiche = "Erreur lors de l'enregistrement. Veuillez reesayez !!";
                    }
                    Toast.makeText(context, affiche, Toast.LENGTH_SHORT).show();
                    callback.onError();
                });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    // ----------------------------- update ------------------------------
    public void modifierInfos(Context context,  VolleyCallback callback) {
        String url = context.getString(R.string.url) + "" + URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update infos");
            postData.put("candidat", id);
            postData.put("employeur", employeur);
            postData.put("dateDebut", dateDebut);
            postData.put("metier", metier);
            postData.put("ville", ville);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if (response.getString("success").equals("true")) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error instanceof NetworkError) {
                        this.affiche = "Pas de connexion Internet !!";
                    } else if (error instanceof ParseError) {
                        this.affiche = "Probleme lors de la verification !!";
                    } else {
                        this.affiche = "Erreur lors de l'enregistrement. Veuillez reesayez !!";
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