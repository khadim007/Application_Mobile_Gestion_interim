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

public class Accueil {

    public String[][] donnes;
    int nbrAttributs = 12; // table AnnonceS

    private final String type;
    private final String specialite;
    private final String lieu;

    public Accueil(String type, String specialite, String lieu){
        this.type = type;
        this.specialite = specialite;
        this.lieu = lieu;
    }

    public void recupDonnes(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url);
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select");
            postData.put("type", type);
            postData.put("specialite", specialite);
            postData.put("lieu", lieu);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
            response -> {
                try {
                    JSONArray jsonArray = response.getJSONArray("donnees");
                    donnes = new String[jsonArray.length()][nbrAttributs];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataElement = jsonArray.getJSONObject(i);
                        donnes[i][0] = dataElement.getString("id_");
                        donnes[i][1] = dataElement.getString("nom");
                        donnes[i][2] = dataElement.getString("description");
                        donnes[i][3] = dataElement.getString("employeur");
                        donnes[i][4] = dataElement.getString("remuneration");
                        donnes[i][5] = dataElement.getString("date_debut");
                        donnes[i][6] = dataElement.getString("date_fin");
                        donnes[i][7] = dataElement.getString("metier");
                        donnes[i][8] = dataElement.getString("ville");
                        donnes[i][9] = dataElement.getString("duree");
                        donnes[i][10] = dataElement.getString("mot_cles");
                        donnes[i][11] = dataElement.getString("type");
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

    public interface VolleyCallback {
        void onSuccess();
        void onError();
    }
}