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


public class Gestionnaire {
    private static final String URL = "gestionnaire";
    SharedPreferences sharedPreferences;

    private String identifiant;
    public int id;

    public String nom;
    public String email;
    public String telephone;
    public String password;

    public Gestionnaire(int id){this.id = id;}

    public Gestionnaire(String identifiant, String password){
        this.identifiant = identifiant;
        this.password = password;
    }

    public Gestionnaire(int id, String nom, String telephone, String email){
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
    }

    public Gestionnaire(String nom, String telephone, String email, String password){
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }


    // ----------------------------- insert ------------------------------
    public void ajouter(Context context) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "insert");
            postData.put("nom", nom);
            postData.put("email", email);
            postData.put("telephone", telephone);
            postData.put("password", password);
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
                        editor.putString("role", "gestionnaire");
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
    public void verifier(Context context, Gestionnaire.VolleyCallback callback){
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select verif");
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
                        id = response.getInt("id");
                        Toast.makeText(context, "Authentification reussie !!", Toast.LENGTH_SHORT).show();
                        callback.onSuccess();
                    } else {
                        Toast.makeText(context, "Identifiant ou mot de passe incorrect !!", Toast.LENGTH_SHORT).show();
                        callback.onError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            error -> {
                if (error instanceof NetworkError) {
                    Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Probleme lors de la verification !!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                }
                callback.onError();
            });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void verifierId(Context context, Gestionnaire.VolleyCallback callback){
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
                    Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Probleme lors de la verification !!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                }
                callback.onError();
            });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void recupDonnes(Context context, Gestionnaire.VolleyCallback callback) {
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
                        if (response.getString("donnees").equals("false")){
                            Toast.makeText(context, "Identifiant ou mot de passe incorrect !!", Toast.LENGTH_SHORT).show();
                            callback.onError();
                        }else{
                            JSONArray jsonArray = response.getJSONArray("donnees");
                            JSONObject dataElement = jsonArray.getJSONObject(0);
                            nom = dataElement.getString("nom");
                            email = dataElement.getString("email");
                            telephone = dataElement.getString("telephone");
                            password = dataElement.getString("password");
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



    // ----------------------------- update ------------------------------
    public void modifierInfos(Context context,  Gestionnaire.VolleyCallback callback) {
        String url = context.getString(R.string.url) + "" + URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update infos");
            postData.put("id", id);
            postData.put("nom", nom);
            postData.put("password", password);
            postData.put("telephone", telephone);
            postData.put("email", email);
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
                    Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Probleme lors de la verification !!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                }
                callback.onError();
            });
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void modifierPassword(Context context,  Gestionnaire.VolleyCallback callback) {
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
                        Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(context, "Probleme lors de la verification !!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Pas de connexion Internet !!", Toast.LENGTH_SHORT).show();
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
