package com.example.projet_mobile.Controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projet_mobile.Modele.Accueil;
import com.example.projet_mobile.Modele.Annonce;
import com.example.projet_mobile.Modele.Annonyme;
import com.example.projet_mobile.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AccueilActivity extends AppCompatActivity implements toolbar {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private Annonyme annonyme;
    private boolean accepte;
    private LocationManager locationManager;
    private LocationListener locationListener;
    SharedPreferences sharedPreferences;

    private Accueil accueil;
    private ListView listView;
    private TextView affErreur;

    private Button bouttonRecherche;
    public Button bouttonPartager;
    public Button bouttonConsulter;
    public Button bouttonCandidater;

    private String type;
    private String specialite;
    private String lieu;

    private Spinner editType;
    private EditText editSpecialite;
    private EditText editLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        localisation(this);
        getID();
        click();
    }

    // ----------------------------------------------------------- Gerer la localisation avec les permissions
    private void localisation(Context c){
        accepte = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                annonyme = new Annonyme(accepte, getAddress(latitude, longitude), c);
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
        editType = findViewById(R.id.editType);
        editSpecialite = findViewById(R.id.editSpecialite);
        editLieu = findViewById(R.id.editLieu);
        affErreur = findViewById(R.id.affError);

        listView = findViewById(R.id.idListView);
        bouttonRecherche = findViewById(R.id.bouttonRecherche);

        ImageView im = findViewById(R.id.imHome);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click() {
        bouttonRecherche.setOnClickListener(v -> {
            type = editType.getSelectedItem().toString();
            specialite = editSpecialite.getText().toString();
            lieu = editLieu.getText().toString();
            exec();
        });
    }

    public void click2(String id, String nom) {
        if("consulter".equals(nom)){
            Intent intent = new Intent( AccueilActivity.this, VoirAnnonceActivity.class);
            intent.putExtra("id", Integer.parseInt(id));
            startActivity(intent);
        }
    }

    private void exec(){
        accueil = new Accueil(type, specialite, lieu);
        accueil.recupDonnes(this, new Accueil.VolleyCallback() {
            @Override
            public void onSuccess() {
                affichage();
            }
            @Override
            public void onError() {
                affichageError();
            }
        });
    }

    private void affichage(){
        if(accueil.donnes.length != 0) {
            affErreur.setText(accueil.donnes.length + " resultats");
            listView.setVisibility(View.VISIBLE);
            Annonce.AnnonceAdapter annonces = new Annonce.AnnonceAdapter(this, this, accueil.donnes);
            listView.setAdapter(annonces);
        }else{
            affErreur.setText("Aucun resultat trouve !!");
        }
    }

    private void affichageError(){
        affErreur.setText("Probleme de connexion. Veillez reesayez !!");
    }

    public void onHomeClick(View view) {}
    public void onRechercheClick(View view) {
        onRechercheClick(this);
    }
    public void onCompteClick(View view) {
        onCompteClick(this);
    }
}