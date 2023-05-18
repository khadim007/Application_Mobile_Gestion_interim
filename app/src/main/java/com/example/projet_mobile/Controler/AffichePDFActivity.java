package com.example.projet_mobile.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Candidature;
import com.example.projet_mobile.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AffichePDFActivity extends AppCompatActivity implements toolbar {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    Candidature candidature;
    PdfRenderer pdfRenderer = null;
    PdfRenderer.Page page = null;
    String partie;
    String avoir;
    int id;

    String textL = "Lettre Mv.. : ";
    String textA = "Aucun document n'a été trouvé !!";
    String textC = "Changer le document !!";

    SurfaceView pdf;
    byte[] donnes = null;

    CardView card;
    private TextView affErreur;
    private TextView textTitre;
    private TextView textDocTitre;
    private Button editDoc;
    private TextView textDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche_pdfactivity);
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);

        Intent intent = getIntent();
        partie = intent.getStringExtra("fichier");

        getID();
        click();
        getDonnes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(page != null){
            page.close();
            pdfRenderer.close();
        }
    }

    private void getID(){
        card = findViewById(R.id.card);
        pdf = findViewById(R.id.pdf_surface_view);

        affErreur = findViewById(R.id.textErreur);
        textTitre = findViewById(R.id.textTitre);
        textDocTitre = findViewById(R.id.textDocTitre);
        editDoc = findViewById(R.id.editDoc);
        textDoc = findViewById(R.id.textDoc);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.col2), PorterDuff.Mode.SRC_IN);
    }

    private void click() {
        editDoc.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), FILE_PICKER_REQUEST_CODE);
        });
    }

    private void getDonnes() {
        Intent intent = getIntent();
        int ident = intent.getIntExtra("id", 0);
        if(ident == 0) {
            candidat = new CandidatInscrit(id);
            candidat.getDonnesCVouLettre(this, partie, new CandidatInscrit.VolleyCallback() {
                @Override
                public void onSuccess() {choix();}
                @Override
                public void onError() {Toast.makeText(AffichePDFActivity.this, "Un probleme est survenu. Veillez recommencer !!", Toast.LENGTH_SHORT).show();}
            });
        }else{
            candidature = new Candidature(ident);
            candidature.recupDonnesCVouLet(this, partie, new Candidature.VolleyCallback() {
                @Override
                public void onSuccess() {choixE();}
                @Override
                public void onError() {Toast.makeText(AffichePDFActivity.this, "Un probleme est survenu. Veillez recommencer !!", Toast.LENGTH_SHORT).show();}
                @SuppressLint("SetTextI18n")
                @Override
                public void onEmpty() {affErreur.setText("Le candidat n'a pas renseigne ce document !!");}
            });
        }
    }

    private void choix(){
        if (partie.equals("cv")) avoir = (candidat.cv == null) ? "false" : "true";
        else avoir = (candidat.lettre == null) ? "false" : "true";

        if(avoir.equals("true")) {
            pdf.setVisibility(View.VISIBLE);
            textTitre.setText(textC);
            if (partie.equals("cv")) {
                textDocTitre.setText(R.string.textInsCV);
                donnes = candidat.cv;
            }else {
                textDocTitre.setText(textL);
                donnes = candidat.lettre;
            }
            affichage();
        }else{
            textTitre.setText(textA);
            if (partie.equals("cv")) textDocTitre.setText(R.string.textInsCV);
            else textDocTitre.setText(String.valueOf(textL));
            pdf.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void choixE(){
        card.setVisibility(View.GONE);
        if (partie.equals("cv")) avoir = (candidature.cv == null) ? "false" : "true";
        else avoir = (candidature.lettre == null) ? "false" : "true";

        if(avoir.equals("true")) {
            pdf.setVisibility(View.VISIBLE);
            if (partie.equals("cv")) donnes = candidature.cv;
            else donnes = candidature.lettre;
            affichage();
        }else{
            affErreur.setText("Le candidat n'a pas renseigne ce document !!");
            pdf.setVisibility(View.GONE);
        }
    }

    private void affichage(){
        File file = new File(getFilesDir(), "fichier.pdf");
        ParcelFileDescriptor fileDescriptor = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(donnes);
            outputStream.close();
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        page = pdfRenderer.openPage(0);
        Bitmap bitmap = Bitmap.createBitmap(1083, 1465, Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        SurfaceHolder surfaceHolder = pdf.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (surfaceHolder.getSurface().isValid()) {
                    Canvas canvas = surfaceHolder.lockCanvas();
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }



    private void enregistrer(){
        candidat = new CandidatInscrit(id);
        if (partie.equals("cv")) candidat.cv = donnes;
        else candidat.lettre = donnes;
        candidat.modifierCVouLettre(this, partie, new CandidatInscrit.VolleyCallback() {
            @Override
            public void onSuccess() {Toast.makeText(AffichePDFActivity.this, "Votre document a bien ete enregistre !!", Toast.LENGTH_SHORT).show();}
            @Override
            public void onError() {Toast.makeText(AffichePDFActivity.this, "Un probleme est survenu lors de l'enregistrement !!", Toast.LENGTH_SHORT).show();}
        });
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedFileUri = data.getData();
                String fileName = null;
                if (selectedFileUri != null) {
                    Cursor cursor = getContentResolver().query(selectedFileUri, null, null, null, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                }
                textDoc.setText(fileName);
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }
                    byteArrayOutputStream.close();
                    inputStream.close();
                    donnes = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                avoir = "true";
                enregistrer();
                choix();
            } else {
                Toast.makeText(this, "Veillez verifier si le fichier choisi est bien un pdf valide !!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Veillez verifier si le fichier choisi est bien un pdf valide !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}