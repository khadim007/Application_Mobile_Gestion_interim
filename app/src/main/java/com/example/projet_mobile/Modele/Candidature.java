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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Candidature {
    private static final String URL = "candidature";

    public int id;
    public String affiche;

    public int candidat_inscrit;
    public int annonce;
    public String prenom;
    public String nom;
    public String nationalite;
    public String dateNais;
    public byte[] cv;
    public byte[] lettre;
    public String employeur;
    public String date_debut;
    public String metier;
    public String ville;

    public Candidature(int candidat_inscrit, int annonce, String prenom, String nom, String nationalite, String dateNais, byte[] cv, byte[] lettre){
        this.candidat_inscrit = candidat_inscrit;
        this.annonce = annonce;
        this.prenom = prenom;
        this.nom = nom;
        this.nationalite = nationalite;
        this.dateNais = dateNais;
        this.cv = cv;
        this.lettre = lettre;
    }

    public Candidature(int candidat_inscrit, String prenom, String nom, String nationalite, String dateNais, String metier, String employeur, String date_debut, String ville){
        this.candidat_inscrit = candidat_inscrit;
        this.prenom = prenom;
        this.nom = nom;
        this.nationalite = nationalite;
        this.dateNais = dateNais;
        this.metier = metier;
        this.employeur = employeur;
        this.date_debut = date_debut;
        this.ville = ville;
    }

    // ----------------------------- insert ------------------------------
    public void ajouterOffre(Context context, Candidature.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "insert offre");
            postData.put("type", "offre");
            postData.put("candidat_inscrit", candidat_inscrit);
            postData.put("annonce", annonce);
            postData.put("prenom", prenom);
            postData.put("nom", nom);
            postData.put("nationalite", nationalite);
            postData.put("dateNais", dateNais);
            postData.put("cv", Arrays.toString(cv));
            postData.put("lettre", Arrays.toString(lettre));
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
            response -> {
                try {
                    if (response.getString("success").equals("true")) {
                        Toast.makeText(context, "Votre candidature est enregistree avec succes !!", Toast.LENGTH_SHORT).show();
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

    public void ajouterSpontane(Context context, Candidature.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "insert spontane");
            postData.put("type", "spontane");
            postData.put("candidat_inscrit", candidat_inscrit);
            postData.put("prenom", prenom);
            postData.put("nom", nom);
            postData.put("nationalite", nationalite);
            postData.put("dateNais", dateNais);
            postData.put("metier", metier);
            postData.put("employeur", employeur);
            postData.put("date_debut", date_debut);
            postData.put("ville", ville);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if (response.getString("success").equals("true")) {
                            Toast.makeText(context, "Votre candidature est enregistree avec succes !!", Toast.LENGTH_SHORT).show();
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



    public interface VolleyCallback {
        void onSuccess();
        void onError();
    }
}
