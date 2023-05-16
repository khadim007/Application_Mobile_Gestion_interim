package com.example.projet_mobile.Controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.Agence;
import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class InsValidationActivity extends AppCompatActivity implements toolbar {
    private static final int PERMISSION_REQUEST_CODE = 1;
    SharedPreferences sharedPreferences;
    CandidatInscrit candidat;
    SmsManager smsManager;
    Employeur employeur;
    Agence agence;

    String codeSaisi;
    String codeEnyoye;
    String role;

    private EditText editCode;
    private TextView textErreur;
    private Button bouttonModifier;
    private Button bouttonRenvoyer;

    //--------------------------------- Candidat
    private String prenom;
    private String nom;
    private String nationalite;
    private String dateNais;
    private String telephone;
    private String email;
    private String password;
    private String ville;
    private byte[] cv;
    private boolean accepte;

    //-----------------------------------Employeur ou agence
    private String nomEntreprise;
    private String nomService;
    private String nomSousService;
    private String numeroNationale ;
    private String nomContact1;
    private String nomContact2;
    private String email1;
    private String email2;
    private String telephone1;
    private String telephone2;
    private String adresse;
    private String liens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_validation);
        smsManager = SmsManager.getDefault();
        sharedPreferences = getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);

        getID();
        click();

        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        if("employeur".equals(role) || "agence".equals(role)){
            nomEntreprise = intent.getStringExtra("nomEntreprise");
            nomService = intent.getStringExtra("nomService");
            nomSousService = intent.getStringExtra("nomSousService");
            numeroNationale = intent.getStringExtra("numeroNationale");
            nomContact1 = intent.getStringExtra("nomContact1");
            nomContact2 = intent.getStringExtra("nomContact2");
            email1 = intent.getStringExtra("email1");
            email2 = intent.getStringExtra("email2");
            telephone1 = intent.getStringExtra("telephone1");
            telephone2 = intent.getStringExtra("telephone2");
            password = intent.getStringExtra("password");
            adresse = intent.getStringExtra("adresse");
            liens = intent.getStringExtra("liens");
            envoyerEmp();
        }else{
            prenom = intent.getStringExtra("prenom");
            nom = intent.getStringExtra("nom");
            nationalite = intent.getStringExtra("nationalite");
            dateNais = intent.getStringExtra("dateNais");
            telephone = intent.getStringExtra("telephone");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            ville = intent.getStringExtra("ville");
            cv = intent.getByteArrayExtra("cv");
            accepte = getIntent().getBooleanExtra("accepte", false);
            envoyerCand();
        }
    }

    private void getID(){
        editCode = findViewById(R.id.editCode);
        textErreur = findViewById(R.id.textErreur);

        bouttonModifier = findViewById(R.id.buttonModifier);
        bouttonRenvoyer = findViewById(R.id.buttonRenvoyer);

        ImageView im = findViewById(R.id.imCompte);
        im.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    private void click(){
        bouttonModifier.setOnClickListener(v -> {
            if("employeur".equals(role) || "agence".equals(role)){
                Intent intent = new Intent( InsValidationActivity.this, InscriptionEmpActivity.class);
                if(role.equals("agence")) intent.putExtra("role", "agence");
                else intent.putExtra("role", "employeur");
                intent.putExtra("nomEntreprise", nomEntreprise);
                intent.putExtra("nomService", nomService);
                intent.putExtra("nomSousService", nomSousService);
                intent.putExtra("numeroNationale", numeroNationale);
                intent.putExtra("nomContact1", nomContact1);
                intent.putExtra("nomContact2", nomContact2);
                intent.putExtra("email1", email1);
                intent.putExtra("email2", email2);
                intent.putExtra("telephone1", telephone1);
                intent.putExtra("telephone2", telephone2);
                intent.putExtra("password", password);
                intent.putExtra("adresse", adresse);
                intent.putExtra("liens", liens);
                startActivity(intent);
            }else{
                Intent intent = new Intent( InsValidationActivity.this, InscriptionCandActivity.class);
                intent.putExtra("prenom", prenom);
                intent.putExtra("nom", nom);
                intent.putExtra("nationalite", nationalite);
                intent.putExtra("dateNais", dateNais);
                intent.putExtra("telephone", telephone);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("ville", ville);
                intent.putExtra("cv", cv);
                intent.putExtra("accepte", accepte);
                startActivity(intent);
            }
        });
        bouttonRenvoyer.setOnClickListener(v -> {
            if("employeur".equals(role) || "agence".equals(role)) envoyerEmp();
            else envoyerCand();
        });
    }

    public void onImageValidationClick(View view) {
        codeSaisi = editCode.getText().toString();
        verifyCode();
    }


    //-----------------------------------Gestion du code et a qui-------------------
    private void envoyerCand() {
        codeEnyoye = String.valueOf(generateSixDigitNumber());
        if(!("".equals(telephone))) {
            envoyerCodeTel(telephone);
        }else {
            envoyerCodeEmail(email);
        }
    }

    private void envoyerEmp() {
        codeEnyoye = String.valueOf(generateSixDigitNumber());
        envoyerCodeEmail(email1);
    }

    public static int generateSixDigitNumber() {
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    @SuppressLint("SetTextI18n")
    private void verifyCode() {
        if (codeEnyoye.equals(codeSaisi)) {
            if("employeur".equals(role)) {
                employeur = new Employeur(nomEntreprise, nomService, nomSousService, numeroNationale, nomContact1, nomContact2, email1, email2, telephone1, telephone2, password, adresse, liens);
                employeur.ajouter(this);
                Intent intent = new Intent(InsValidationActivity.this, AbonnementActivity.class);
                intent.putExtra("role", role);
                startActivity(intent);
            }else if("agence".equals(role)) {
                agence = new Agence(nomEntreprise, nomService, nomSousService, numeroNationale, nomContact1, nomContact2, email1, email2, telephone1, telephone2, password, adresse, liens);
                agence.ajouter(this);
                Intent intent = new Intent(InsValidationActivity.this, AbonnementActivity.class);
                intent.putExtra("role", role);
                startActivity(intent);
            }else{
                candidat = new CandidatInscrit(prenom, nom, nationalite, dateNais, telephone, email, password, ville, cv, accepte);
                candidat.ajouter(this);
                Intent intent = new Intent(InsValidationActivity.this, AccueilActivity.class);
                startActivity(intent);
            }
        } else {
            textErreur.setText("Le code saisi ne correspond pas au code envoye !!");
        }
    }


    //-------------------------------- Envoie par SMS------------------------
    private void envoyerCodeTel(String telephone) {
        this.telephone = telephone;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                String message = "Code : "+codeEnyoye+", envoye par KAO Interim.";
                smsManager.sendTextMessage(telephone, null, message, null, null);
                Toast.makeText(this, "Le code vous a ete envoye avec succes !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String message = "Code : "+codeEnyoye+", envoye par KAO Interim.";
                smsManager.sendTextMessage(telephone, null, message, null, null);
                Toast.makeText(this, "Le code vous a ete envoye avec succes !!", Toast.LENGTH_SHORT).show();
            } else {
                String t = "Le code n'est pas envoye a cause des permission refuses !!";
                textErreur.setText(t);
                Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
            }
        }
    }


    //-----------------------------------Envoie par Mail-----------------------------
    private void envoyerCodeEmail(String email) {
        String senderEmail = "kao.projet.mobile@gmail.com";
        String password = "pjhlyxteosgzricv";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Code de validation envoye par KAO Interim");

            String messageBody = "Code : "+codeEnyoye;
            message.setText(messageBody);

            SendMailTask sendMailTask = new SendMailTask();
            sendMailTask.execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(InsValidationActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void onHomeClick(View view) {onHomeClick(this);}
    public void onRechercheClick(View view) {onRechercheClick(this);}
    public void onCandidatureClick(View view) {onCandidatureClick(this);}
    public void onCompteClick(View view) {onCompteClick(this);}
}
