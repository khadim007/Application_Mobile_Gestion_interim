package com.example.projet_mobile.Controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Accueil;
import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Annonyme;
import com.example.projet_mobile.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AccueilActivity extends AppCompatActivity implements toolbar {
    static final int REQUEST_LOCATION_PERMISSION = 1;
    SharedPreferences sharedPreferences;
    LocationListener locationListener;
    LocationManager locationManager;
    Annonyme annonyme;
    boolean accepte;
    String role;

    private Accueil accueil;
    private ListView listView;

    private TextView affErreur;
    private TextView textTitre;

    public Button bouttonPartager;
    public Button bouttonConsulter;
    public Button bouttonCandidater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");

        localisation(this);
        getID();
    }

    // ----------------------------------------------------------- Gerer la localisation avec les permissions
    private void localisation(Context c){
        accepte = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                accepte = true;
                annonyme = new Annonyme(accepte, getAddress(latitude, longitude), c);
                exec();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
                    accepte = true;
                }
            }else{
                annonyme = new Annonyme(this);
                exec();
            }
        }
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    builder.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                address = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }




    // ----------------------------------------------------------- Gerer l'affichage des annonces
    private void getID(){
        affErreur = findViewById(R.id.affError);
        textTitre = findViewById(R.id.textTitre);

        listView = findViewById(R.id.idListView);

        ImageView im = findViewById(R.id.imHome);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    public void click2(String id, String nom, String annonce) {
        Intent intent = null;
        if("partager".equals(nom)){
            if(role.equals("candidat")) {
                intent = new Intent( AccueilActivity.this, PartageActivity.class);
                intent.putExtra("id", Integer.parseInt(id));
            }else{
                intent = new Intent( AccueilActivity.this, AuthentificationActivity.class);
            }
        }else if("consulter".equals(nom)){
            intent = new Intent( AccueilActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
        }else if("candidater".equals(nom)){
            if(role.equals("candidat")){
                intent = new Intent( AccueilActivity.this, CandidatureOffreActivity.class);
                intent.putExtra("id", Integer.parseInt(id));
                intent.putExtra("nom", annonce);
            }else {
                intent = new Intent( AccueilActivity.this, AuthentificationActivity.class);
            }
        }
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void exec(){
        String specialite = "";
        String lieu = "";
        if(accepte){
            textTitre.setText("Affichage d'annonces pres de vous ou pertinants");
            specialite = Annonyme.getRecherche(this);
            lieu = Annonyme.getLieu(this);
        }else{
            textTitre.setText("Affichage d'annonces interressantes");
        }
        accueil = new Accueil(specialite, lieu);
        accueil.recupDonnes(this, 1, new Accueil.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
            @Override
            public void onEmpty() {
                String s2 = sharedPreferences.getString("AnnonymeRecherche2", "");
                String s3 = sharedPreferences.getString("AnnonymeRecherche3", "");
                if(s2.equals("") && s3.equals("")){
                    affichageEmpty();
                }else{
                    exec2(s2, s3);
                }
            }
        });
    }

    public void exec2(String s2, String s3){
        accueil = new Accueil(s2, s3);
        accueil.recupDonnes(this, 1, new Accueil.VolleyCallback() {
            @Override
            public void onSuccess() {affichage();}
            @Override
            public void onError() {affichageError();}
            @Override
            public void onEmpty() {affichageEmpty();}
        });
    }

    @SuppressLint("SetTextI18n")
    private void affichage(){
        affErreur.setText(accueil.donnes.length + " resultats");
        listView.setVisibility(View.VISIBLE);
        Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, accueil.donnes);
        listView.setAdapter(annonces);
    }

    @SuppressLint("SetTextI18n")
    private void affichageError(){affErreur.setText("Probleme de connexion. Veillez reesayez !!");}
    @SuppressLint("SetTextI18n")
    private void affichageEmpty(){affErreur.setText("Aucune annonce est trouvée pres de vous !!");}

    public void onHomeClick(View view) {}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}