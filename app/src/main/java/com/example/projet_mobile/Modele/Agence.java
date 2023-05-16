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


public class Agence {

    SharedPreferences sharedPreferences;
    private static final String URL = "agence";
    public static String succes = "good !!";

    private String identifiant;
    public int id;
    public String affiche;

    public String nomAgence;
    public String nomService;
    public String nomSousService;
    public String numeroNationale;
    public String nomContact1;
    public String nomContact2;
    public String email1;
    public String email2;
    public String telephone1;
    public String telephone2;
    public String password;
    public String adresse;
    public String liens;
    public String abonnement;

    public Agence(int id){this.id = id;}

    public Agence(String identifiant, String password){
        this.identifiant = identifiant;
        this.password = password;
    }

    public Agence(int id, String nomAgence, String numeroNationale, String nomService, String nomSousService, String nomContact1, String nomContact2, String telephone1, String email1, String liens, String adresse){
        this.id = id;
        this.nomAgence = nomAgence;
        this.nomService = nomService;
        this.nomSousService = nomSousService;
        this.numeroNationale = numeroNationale;
        this.nomContact1 = nomContact1;
        this.nomContact2 = nomContact2;
        this.email1 = email1;
        this.telephone1 = telephone1;
        this.liens = liens;
        this.adresse = adresse;
    }

    public Agence(String nomAgence, String nomService, String nomSousService, String numeroNationale, String nomContact1, String nomContact2, String email1, String email2, String telephone1, String telephone2, String password, String adresse, String liens){
        this.nomAgence = nomAgence;
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
            postData.put("nomAgence", nomAgence);
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
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    // ----------------------------- select ------------------------------
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

    public void verifierId(Context context, VolleyCallback callback){
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select password");
            postData.put("id", id);
            postData.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getString("success").equals("true")) callback.onSuccess();
                        else callback.onError();
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


    public void recupDonnes(Context context, VolleyCallback callback) {
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
                        JSONArray jsonArray = response.getJSONArray("donnees");
                        JSONObject dataElement = jsonArray.getJSONObject(0);

                        nomAgence = dataElement.getString("nomAgence");
                        nomService = dataElement.getString("nomService");
                        nomSousService = dataElement.getString("nomSousService");
                        numeroNationale = dataElement.getString("numeroNationale");
                        nomContact1 = dataElement.getString("nomContact1");
                        nomContact2 = dataElement.getString("nomContact2");
                        email1 = dataElement.getString("email1");
                        email2 = dataElement.getString("email2");
                        telephone1 = dataElement.getString("telephone1");
                        telephone2 = dataElement.getString("telephone2");
                        password = dataElement.getString("password");
                        adresse = dataElement.getString("adresse");
                        liens = dataElement.getString("liens");
                        abonnement = dataElement.getString("abonnement");
                        affiche = "good !!";

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
                    affiche = "Veillez recommencer !!";
                    callback.onError();
                });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }



    // ----------------------------- update ------------------------------
    public void changeAbonnement(Context context, int abon, VolleyCallback callback) {
        String url = context.getString(R.string.url) + "" + URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update abonnement");
            postData.put("id", id);
            postData.put("abon", abon);
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

    public void modifierInfos(Context context,  VolleyCallback callback) {
        String url = context.getString(R.string.url) + "" + URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update infos");
            postData.put("id", id);
            postData.put("nomAgence", nomAgence);
            postData.put("numeroNationale", numeroNationale);
            postData.put("nomService", nomService);
            postData.put("nomSousService", nomSousService);
            postData.put("nomContact1", nomContact1);
            postData.put("nomContact2", nomContact2);
            postData.put("password", password);
            postData.put("telephone1", telephone1);
            postData.put("email1", email1);
            postData.put("liens", liens);
            postData.put("adresse", adresse);
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

    public void modifierPassword(Context context,  VolleyCallback callback) {
        String url = context.getString(R.string.url) + "" + URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update password");
            postData.put("id", id);
            postData.put("password", password);
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
    }
}
