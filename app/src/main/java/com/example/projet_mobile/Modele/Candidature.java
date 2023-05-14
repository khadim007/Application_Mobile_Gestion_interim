package com.example.projet_mobile.Modele;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projet_mobile.Controler.GestionCandidatureActivity;
import com.example.projet_mobile.Controler.VoirCandidatureActivity;
import com.example.projet_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Candidature {
    private static final String URL = "candidature";
    public String[][] donnes;
    public int id;

    public int candidat_inscrit;
    public int annonce;
    public String nomAnnonce;
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
    public String etat;

    public Candidature(){}

    public Candidature(int id){this.id = id;}

    public Candidature(int candidat_inscrit, String etat){
        this.candidat_inscrit = candidat_inscrit;
        this.etat = etat;
    }

    public Candidature(int candidat_inscrit, int annonce, String nomAnnonce, String prenom, String nom, String nationalite, String dateNais, byte[] cv, byte[] lettre){
        this.candidat_inscrit = candidat_inscrit;
        this.annonce = annonce;
        this.nomAnnonce = nomAnnonce;
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


    // -------------------------------------ADAPTER--------------------------------------------
    public static class CandidatureAdapter extends BaseAdapter {
        GestionCandidatureActivity candidature;
        VoirCandidatureActivity voir;
        private final LayoutInflater inflater;
        public String[][] donnes;

        public CandidatureAdapter(Context context, GestionCandidatureActivity candidature, String[][] donnes) {
            this.candidature = candidature;
            this.inflater = LayoutInflater.from(context);
            this.donnes = new String[donnes.length][donnes[0].length];
            for(int i = 0; i < donnes.length; i++){
                System.arraycopy(donnes[i], 0, this.donnes[i], 0, donnes[i].length);
            }
        }

        public CandidatureAdapter(Context context, VoirCandidatureActivity voir, String[][] donnes) {
            this.voir = voir;
            this.inflater = LayoutInflater.from(context);
            this.donnes = new String[donnes.length][donnes[0].length];
            for(int i = 0; i < donnes.length; i++){
                System.arraycopy(donnes[i], 0, this.donnes[i], 0, donnes[i].length);
            }
        }

        @Override
        public int getCount() {return this.donnes.length;}
        @Override
        public Object getItem(int i) {return null;}
        @Override
        public long getItemId(int i) {return 0;}

        @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_affiche_candidaturectivity, null);

            if(candidature != null) {
                String id = this.donnes[i][0];
                String annonce = this.donnes[i][1];
                TextView nom = view.findViewById(R.id.textNom);
                nom.setText(this.donnes[i][2] + " (H/F)");

                view.findViewById(R.id.buttonConsulerLet).setVisibility(View.GONE);
                candidature.bouttonConsulter = view.findViewById(R.id.buttonConsuler);
                candidature.bouttonModifier = view.findViewById(R.id.buttonModifier);
                candidature.bouttonSupprimer = view.findViewById(R.id.buttonSupprimer);
                candidature.bouttonContacter = view.findViewById(R.id.buttonContacter);
                if(this.donnes[i][3].equals("en cours acceptees")){
                    candidature.bouttonConsulter.setText("Contact"); candidature.bouttonConsulter.setOnClickListener(v -> candidature.click2(id, annonce, "contacter", this.donnes[i][2]));
                    candidature.bouttonModifier.setText("Accept"); candidature.bouttonModifier.setOnClickListener(v -> candidature.click2(id, annonce, "accepter", this.donnes[i][2]));
                    candidature.bouttonSupprimer.setText("Refus"); candidature.bouttonSupprimer.setOnClickListener(v -> candidature.click2(id, annonce, "refuser", this.donnes[i][2]));
                    candidature.bouttonContacter.setText("+Agenda"); candidature.bouttonContacter.setOnClickListener(v -> candidature.click2(id, annonce, "agenda", this.donnes[i][2]));
                }else if(this.donnes[i][3].equals("en cours non acceptees")){
                    candidature.bouttonConsulter.setOnClickListener(v -> candidature.click2(id, annonce, "consulter", this.donnes[i][2]));
                    candidature.bouttonModifier.setText("Non acceptee"); candidature.bouttonModifier.setBackgroundColor(Color.parseColor("#FFFFFF")); candidature.bouttonModifier.setTextColor(Color.RED);
                    candidature.bouttonSupprimer.setVisibility(View.GONE);
                    candidature.bouttonContacter.setVisibility(View.GONE);
                }else if(this.donnes[i][3].equals("accepter") || this.donnes[i][3].equals("refuser")){
                    candidature.bouttonConsulter.setOnClickListener(v -> candidature.click2(id, annonce, "consulter", this.donnes[i][2]));
                    candidature.bouttonModifier.setText("Vous avez ["+this.donnes[i][3]+"] cette candidature."); candidature.bouttonModifier.setBackgroundColor(Color.parseColor("#FFFFFF")); candidature.bouttonModifier.setTextColor(Color.BLACK);
                    candidature.bouttonSupprimer.setVisibility(View.GONE);
                    candidature.bouttonContacter.setVisibility(View.GONE);
                }else{
                    candidature.bouttonConsulter.setOnClickListener(v -> candidature.click2(id, annonce, "consulter", this.donnes[i][2]));
                    candidature.bouttonModifier.setOnClickListener(v -> candidature.click2(id, annonce, "modifier", this.donnes[i][2]));
                    candidature.bouttonSupprimer.setOnClickListener(v -> candidature.click2(id, annonce, "supprimer", this.donnes[i][2]));
                    candidature.bouttonContacter.setOnClickListener(v -> candidature.click2(id, annonce, "contacter", this.donnes[i][2]));
                }
            }else if(voir != null) {
                String id = this.donnes[i][0];
                String candidat = this.donnes[i][1];
                TextView nom = view.findViewById(R.id.textNom);
                nom.setText("Nom : "+this.donnes[i][3]+" "+this.donnes[i][4]+"\nNationalite : "+this.donnes[i][5]+"\nDate Naiss : "+this.donnes[i][6]); nom.setTextColor(Color.BLACK);

                voir.bouttonConsulter = view.findViewById(R.id.buttonConsuler);
                voir.bouttonConsulterLet = view.findViewById(R.id.buttonConsulerLet);
                voir.bouttonModifier = view.findViewById(R.id.buttonModifier);
                voir.bouttonSupprimer = view.findViewById(R.id.buttonSupprimer);
                voir.bouttonContacter = view.findViewById(R.id.buttonContacter);
                if(this.donnes[i][7].equals("en cours acceptees")){
                    voir.bouttonConsulter.setText("CV"); voir.bouttonConsulter.setOnClickListener(v -> voir.click2(id, candidat, "consulter"));
                    voir.bouttonConsulterLet.setText("Lettre"); voir.bouttonConsulterLet.setOnClickListener(v -> voir.click2(id, candidat, "consulter let"));
                    voir.bouttonModifier.setVisibility(View.GONE);
                    voir.bouttonSupprimer.setText("Vous avez deja acceptée"); voir.bouttonSupprimer.setBackgroundColor(Color.parseColor("#FFFFFF")); voir.bouttonSupprimer.setTextColor(Color.BLACK);
                    voir.bouttonContacter.setText("Contact"); voir.bouttonContacter.setOnClickListener(v -> voir.click2(id, candidat, "repondre"));
                }else if(this.donnes[i][7].equals("en cours non acceptees")){
                    voir.bouttonConsulter.setText("CV"); voir.bouttonConsulter.setOnClickListener(v -> voir.click2(id, candidat, "consulter"));
                    voir.bouttonConsulterLet.setText("Lettre"); voir.bouttonConsulterLet.setOnClickListener(v -> voir.click2(id, candidat, "consulter let"));
                    voir.bouttonModifier.setVisibility(View.GONE);
                    voir.bouttonSupprimer.setText("Vous avez deja refusée"); voir.bouttonSupprimer.setBackgroundColor(Color.parseColor("#FFFFFF")); voir.bouttonSupprimer.setTextColor(Color.BLACK);
                    voir.bouttonContacter.setVisibility(View.GONE);
                }else if(this.donnes[i][7].equals("accepter")){
                    voir.bouttonConsulter.setText("CV"); voir.bouttonConsulter.setOnClickListener(v -> voir.click2(id, candidat, "consulter"));
                    voir.bouttonConsulterLet.setText("Lettre"); voir.bouttonConsulterLet.setOnClickListener(v -> voir.click2(id, candidat, "consulter let"));
                    voir.bouttonModifier.setVisibility(View.GONE);
                    voir.bouttonSupprimer.setText("Le candidat a accepté l'offre"); candidature.bouttonSupprimer.setBackgroundColor(Color.parseColor("#FFFFFF")); candidature.bouttonSupprimer.setTextColor(Color.BLACK);
                    voir.bouttonContacter.setText("Contact"); voir.bouttonContacter.setOnClickListener(v -> voir.click2(id, candidat, "repondre"));
                }else if(this.donnes[i][7].equals("refuser")){
                    voir.bouttonConsulter.setText("CV"); voir.bouttonConsulter.setOnClickListener(v -> voir.click2(id, candidat, "consulter"));
                    voir.bouttonConsulterLet.setText("Lettre"); voir.bouttonConsulterLet.setOnClickListener(v -> voir.click2(id, candidat, "consulter let"));
                    voir.bouttonModifier.setVisibility(View.GONE);
                    voir.bouttonSupprimer.setText("Le candidat a refusé l'offre"); candidature.bouttonSupprimer.setBackgroundColor(Color.parseColor("#FFFFFF")); candidature.bouttonSupprimer.setTextColor(Color.BLACK);
                    voir.bouttonContacter.setVisibility(View.GONE);
                }else{
                    voir.bouttonConsulter.setText("CV"); voir.bouttonConsulter.setOnClickListener(v -> voir.click2(id, candidat, "consulter"));
                    voir.bouttonConsulterLet.setText("Lettre"); voir.bouttonConsulterLet.setOnClickListener(v -> voir.click2(id, candidat, "consulter let"));
                    voir.bouttonModifier.setText("Accept"); voir.bouttonModifier.setOnClickListener(v -> voir.click2(id, candidat, "en cours acceptees"));
                    voir.bouttonSupprimer.setText("Refus"); voir.bouttonSupprimer.setOnClickListener(v -> voir.click2(id, candidat, "en cours non acceptees"));
                    voir.bouttonContacter.setText("Repondre"); voir.bouttonContacter.setOnClickListener(v -> voir.click2(id, candidat, "repondre"));
                }
            }
            return view;
        }
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
            postData.put("nomAnnonce", nomAnnonce);
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


    // ----------------------------- select ------------------------------
    public void recupDonnesPrecis(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select precis");
            postData.put("id", id );
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
                            annonce = Integer.parseInt(dataElement.getString("annonce"));
                            nomAnnonce = dataElement.getString("nomAnnonce");
                            prenom = dataElement.getString("prenom");
                            nom = dataElement.getString("nom");
                            nationalite = dataElement.getString("nationalite");
                            dateNais = dataElement.getString("dateNaissance");
                            if(dataElement.getString("cv").equals("null")){
                                cv = null;
                            }else{
                                String s = dataElement.getString("cv").replace("[", "").replace("]", "");
                                String[] cvStrArr = s.split(", ");
                                cv = new byte[cvStrArr.length];
                                for (int i = 0; i < cvStrArr.length; i++) {
                                    cv[i] = Byte.parseByte(cvStrArr[i]);
                                }
                            }
                            if(dataElement.getString("lettre").equals("null")){
                                lettre = null;
                            }else {
                                String s = dataElement.getString("lettre").replace("[", "").replace("]", "");
                                String[] cvStrArr = s.split(", ");
                                lettre = new byte[cvStrArr.length];
                                for (int i = 0; i < cvStrArr.length; i++) {
                                    lettre[i] = Byte.parseByte(cvStrArr[i]);
                                }
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

    public void recupDonnes(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select");
            postData.put("candidat_inscrit", candidat_inscrit );
            postData.put("etat", etat);
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
                            donnes = new String[jsonArray.length()][4];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataElement = jsonArray.getJSONObject(i);
                                donnes[i][0] = dataElement.getString("id_");
                                donnes[i][1] = dataElement.getString("annonce");
                                donnes[i][2] = dataElement.getString("nomAnnonce");
                                donnes[i][3] = dataElement.getString("etat");
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

    public void recupDonnesCand(Context context, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select cand");
            postData.put("annonce", annonce);
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
                            donnes = new String[jsonArray.length()][9];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataElement = jsonArray.getJSONObject(i);
                                donnes[i][0] = dataElement.getString("id_");
                                donnes[i][1] = dataElement.getString("candidat_inscrit");
                                donnes[i][2] = dataElement.getString("nomAnnonce");
                                donnes[i][3] = dataElement.getString("prenom");
                                donnes[i][4] = dataElement.getString("nom");
                                donnes[i][5] = dataElement.getString("nationalite");
                                donnes[i][6] = dataElement.getString("dateNaissance");
                                donnes[i][7] = dataElement.getString("etat");
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

    public void recupDonnesCVouLet(Context context, String fic, VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "select cv ou lettre");
            postData.put("id", id);
            postData.put("fic", fic);
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

                            if(fic.equals("cv")) {
                                if (dataElement.getString("cv").equals("null")) {
                                    cv = null;
                                } else {
                                    String s = dataElement.getString("cv").replace("[", "").replace("]", "");
                                    String[] cvStrArr = s.split(", ");
                                    cv = new byte[cvStrArr.length];
                                    for (int i = 0; i < cvStrArr.length; i++) {
                                        cv[i] = Byte.parseByte(cvStrArr[i]);
                                    }
                                }
                            }else {
                                if (dataElement.getString("lettre").equals("null")) {
                                    lettre = null;
                                } else {
                                    String s = dataElement.getString("lettre").replace("[", "").replace("]", "");
                                    String[] cvStrArr = s.split(", ");
                                    lettre = new byte[cvStrArr.length];
                                    for (int i = 0; i < cvStrArr.length; i++) {
                                        lettre[i] = Byte.parseByte(cvStrArr[i]);
                                    }
                                }
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

    // ----------------------------- update ------------------------------
    public void modifierOffre(Context context, Candidature.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update offre");
            postData.put("id", id);
            postData.put("type", "offre");
            postData.put("candidat_inscrit", candidat_inscrit);
            postData.put("annonce", annonce);
            postData.put("nomAnnonce", nomAnnonce);
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

    public void modifierEtat(Context context, String et, Candidature.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "update etat");
            postData.put("id", id);
            postData.put("etat", et);
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


    // ----------------------------- delete ------------------------------
    public void supp(Context context, Candidature.VolleyCallback callback) {
        String url = context.getString(R.string.url)+""+URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("choix", "supprimer line");
            postData.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON object", e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if (response.getString("success").equals("true")) {
                            Toast.makeText(context, "Suppression reussie !!", Toast.LENGTH_SHORT).show();
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
        void onEmpty();
    }
}
