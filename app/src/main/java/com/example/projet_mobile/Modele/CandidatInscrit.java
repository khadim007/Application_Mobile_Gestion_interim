package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projet_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class CandidatInscrit {

    private static String URL = "candidat_inscrit";
    public static String succes = "Authentification reussie !!";

    private String identifiant;
    private String prenom;
    private String nom;
    private String nationalite;
    private String dateNais;
    private String telephone;
    private String email;
    private String password;
    private String ville;
    private File cv;
    private boolean accepte;
    public String affiche;

    public CandidatInscrit(String identifiant, String password){
        this.identifiant = identifiant;
        this.password = password;
    }

    public CandidatInscrit(String prenom, String nom, String nationalite, String dateNais, String telephone, String email, String password, String ville, File cv, boolean accepte){
        this.prenom = prenom;
        this.nom = nom;
        this.nationalite = nationalite;
        this.dateNais = dateNais;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.ville = ville;
        this.cv = cv;
        this.accepte = accepte;
    }


    public void ajouter(Context context) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "insert");
            postData.put("prenom", prenom);
            postData.put("nom", nom);
            postData.put("nationalite", nationalite);
            postData.put("dateNais", dateNais);
            postData.put("telephone", telephone);
            postData.put("email", email);
            postData.put("password", password);
            postData.put("ville", ville);
            postData.put("cv", cv);
            postData.put("accepte", accepte);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    Toast.makeText(context, "Votre compte est cree avec succes !", Toast.LENGTH_SHORT).show();
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
        queue.add(request);
    }


    public void verifier(Context context, VolleyCallback callback){
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select");
            postData.put("identifiant", identifiant);
            postData.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
            response -> {
                try {
                    if(response.getString("success").equals("true")) {
                        this.affiche = "Authentification reussie !!";
                        Toast.makeText(context, affiche, Toast.LENGTH_SHORT).show();
                        callback.onSuccess();
                    } else {
                        this.affiche = "Identifiant ou mot de passe incorrect !!";
                        Toast.makeText(context, affiche, Toast.LENGTH_SHORT).show();
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
                }else{
                    this.affiche = "Erreur lors de l'enregistrement. Veuillez reesayez !!";

                }
                Toast.makeText(context, affiche, Toast.LENGTH_SHORT).show();
                callback.onError();
            });
        queue.add(request);
    }

    public interface VolleyCallback {
        void onSuccess();
        void onError();
    }
}
