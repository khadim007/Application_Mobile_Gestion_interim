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
import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Amis {

    private static final String URL = "amis";
    public int id;

    public String nom;
    public String telephone;
    public String email;

    public Amis(String nom){this.nom = nom;}

    public Amis(String nom, String telephone, String email){
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
    }


    // ----------------------------- insert ------------------------------
    public void ajouter(Context context, Amis.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            JSONArray e = new JSONArray(email.split(" "));
            JSONArray t = new JSONArray(telephone.split(" "));
            postData.put("choix", "insert");
            postData.put("email", ""+e+"");
            postData.put("telephone", ""+t+"");
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
                            callback.onError();
                        }
                    } catch (JSONException | JsonProcessingException e) {
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
    public void recupDonnes(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select");
            postData.put("nom", nom);
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
                            email = dataElement.getString("email");
                            telephone = dataElement.getString("telephone");
                            callback.onSuccess();
                        }
                    } catch (JSONException | JsonProcessingException e) {
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
        void onSuccess() throws JsonProcessingException;
        void onError();
        void onEmpty();
    }
}
