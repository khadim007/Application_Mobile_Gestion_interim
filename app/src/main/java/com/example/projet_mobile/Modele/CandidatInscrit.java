package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.io.File;

public class CandidatInscrit {

    SharedPreferences sharedPreferences;
    private static String URL = "candidat_inscrit";
    public static String succes = "Authentification reussie !!";

    private String identifiant;
    public int id;
    public String affiche;

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

    public CandidatInscrit(int id){
        this.id = id;
    }

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


    // ----------------------------- insert ------------------------------
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
                try {
                    id = response.getInt("id");
                    sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", id);
                    editor.putString("role", "candidat");
                    editor.commit();
                    Toast.makeText(context, "Votre compte est cree avec succes !", Toast.LENGTH_SHORT).show();
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
    public void getDonnes(Context context, VolleyCallback callback){
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select return line");
            postData.put("id", id);
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

                        JSONArray jsonArray = null;
                        jsonArray = response.getJSONArray("donnes");
                        JSONObject dataElement = jsonArray.getJSONObject(0);
                        prenom = dataElement.getString("prenom");
                        nom = dataElement.getString("nom");
                        nationalite = dataElement.getString("nationalite");
                        dateNais = dataElement.getString("dateNaissance");
                        telephone = dataElement.getString("telephone");
                        email = dataElement.getString("email");
                        password = dataElement.getString("password");
                        ville = dataElement.getString("ville");
                        cv = new File(dataElement.getString("cv"));
                        int acc = dataElement.getInt("accepte");
                        accepte = acc == 0 ? false  : true;

                        callback.onSuccess();
                    } else {
                        this.affiche = "Probleme lors de la recuperation. Veillez reessayer !!";
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
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void verifier(Context context, VolleyCallback callback){
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select return id");
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
                        id = response.getInt("id");
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
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    // ----------------------------- update ------------------------------


    public interface VolleyCallback {
        void onSuccess();
        void onError();
    }
}
