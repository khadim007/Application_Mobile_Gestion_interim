package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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


public class Employeur {

    SharedPreferences sharedPreferences;
    private static String URL = "employeur";
    public static String succes = "Authentification reussie !!";

    private String identifiant;
    public int id;
    public String affiche;

    private String nomEntreprise;
    private String nomService;
    private String nomSousService;
    private int numeroNationale;
    private String nomContact1;
    private String nomContact2;
    private String email1;
    private String email2;
    private String telephone1;
    private String telephone2;
    private String password;
    private String adresse;
    private String liens;

    public Employeur(String identifiant, String password){
        this.identifiant = identifiant;
        this.password = password;
    }

    public Employeur(String nomEntreprise, String nomService, String nomSousService, int numeroNationale, String nomContact1, String nomContact2, String email1, String email2, String telephone1, String telephone2, String password, String adresse, String liens){
        this.nomEntreprise = nomEntreprise;
        this.nomService = nomService;
        this.nomSousService = nomSousService;
        this.numeroNationale = numeroNationale;
        this.nomContact1 = nomContact1;
        this.nomContact2 = nomContact2;
        this.email1 = email1;
        this.email2 = email2;
        this.telephone1 = telephone1;
        this.telephone2 = telephone2;
        this.password = password;
        this.adresse = adresse;
        this.liens = liens;
    }


    // ----------------------------- insert ------------------------------
    public void ajouter(Context context) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "insert");
            postData.put("nomEntreprise", nomEntreprise);
            postData.put("nomService", nomService);
            postData.put("nomSousService", nomSousService);
            postData.put("numeroNationale", numeroNationale);
            postData.put("nomContact1", nomContact1);
            postData.put("nomContact2", nomContact2);
            postData.put("email1", email1);
            postData.put("email2", email2);
            postData.put("telephone1", telephone1);
            postData.put("telephone2", telephone2);
            postData.put("password", password);
            postData.put("adresse", adresse);
            postData.put("liens", liens);
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
                    editor.putString("role", "employeur");
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
        queue.add(request);
    }


    // ----------------------------- select ------------------------------
    public void verifier(Context context, Employeur.VolleyCallback callback){
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
        queue.add(request);
    }

    public interface VolleyCallback {
        void onSuccess();
        void onError();
    }
}
