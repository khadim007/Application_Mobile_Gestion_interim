package com.example.projet_mobile.Controler;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mobile.Modele.CandidatInscrit;
import com.example.projet_mobile.Modele.Employeur;
import com.example.projet_mobile.R;

import java.io.File;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;

public class InsValidationActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    SmsManager smsManager;
    private CandidatInscrit candidat;
    private Employeur employeur;

    String codeSaisi;
    String codeEnyoye;
    String role;

    private EditText editCode;
    private TextView textErreur;
    private Button bouttonModifier;
    private Button bouttonRenvoyer;

    // Candidat
    private String prenom;
    private String nom;
    private String nationalite;
    private String dateNais;
    private String telephone;
    private String email;
    private String password;
    private String ville;
    private File cv;
    private boolean accepte;

    //Employeur
    private String nomEntreprise;
    private String nomService;
    private String nomSousService;
    private int numeroNationale ;
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

        getID();
        click();

        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        if("employeur".equals(role)){
            nomEntreprise = intent.getStringExtra("nomEntreprise");
            nomService = intent.getStringExtra("nomService");
            nomSousService = intent.getStringExtra("nomSousService");
            numeroNationale = intent.getIntExtra("numeroNationale", 0);
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
            cv = (File) getIntent().getSerializableExtra("cv");
            accepte = getIntent().getBooleanExtra("accepte", false);
            envoyerCand();
        }
    }

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
        int sixDigitNumber = random.nextInt(max - min + 1) + min;
        return sixDigitNumber;
    }

    private void envoyerCodeTel(String telephone) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                System.out.println("Code :"+codeEnyoye+"-----------------------------------------------------");
                Toast.makeText(this, "Le code vous a ete envoye avec succes !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est accordée
                    System.out.println("Code :"+codeEnyoye+"-----------------------------------------------------");
                    Toast.makeText(this, "Le code vous a ete envoye avec succes !!", Toast.LENGTH_SHORT).show();
                } else {
                    // La permission est refusée
                    String t = "Le code n'est pas envoye a cause des permission refuses !!";
                    textErreur.setText(t);
                    Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void envoyerCodeEmail(String email) {
        System.out.println("Code :"+codeEnyoye+"-----------------------------------------------------");
//        String senderEmail = "projet.mobile.kao@gmail.com";
//        String password = "projetmobile";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                char[] passwordArray = password.toCharArray();
//                return new PasswordAuthentication(senderEmail, passwordArray);
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//
//            message.setFrom(new InternetAddress(senderEmail));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
//            message.setSubject("Test email from JavaMail");
//
//            // Set the content of the email message
//            String messageBody = "This is a test email from JavaMail.";
//            message.setText(messageBody);
//
//            Transport.send(message);
//            System.out.println("Email sent successfully.----------");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }

    private void getID(){
        editCode = (EditText) findViewById(R.id.editCode);
        textErreur = (TextView) findViewById(R.id.textErreur);

        bouttonModifier = (Button) findViewById(R.id.buttonModifier);
        bouttonRenvoyer = (Button) findViewById(R.id.buttonRenvoyer);
    }

    private void click(){
        bouttonModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("employeur".equals(role)){
                    Intent intent = new Intent( InsValidationActivity.this, InscriptionEmpActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent( InsValidationActivity.this, InscriptionCandActivity.class);
                    startActivity(intent);
                }
            }
        });
        bouttonRenvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onImageValidationClick(View view) {
        codeSaisi = editCode.getText().toString();
        verifyCode(codeSaisi);
    }

    private void verifyCode(String code) {
        if (codeEnyoye.equals(codeSaisi)) {
            if("employeur".equals(role)) {
                employeur = new Employeur(nomEntreprise, nomService, nomSousService, numeroNationale, nomContact1, nomContact2, email1, email2, telephone1, telephone2, password, adresse, liens);
                employeur.ajouter(this);
            }else{
                candidat = new CandidatInscrit(prenom, nom, nationalite, dateNais, telephone, email, password, ville, cv, accepte);
                candidat.ajouter(this);
            }
            Intent intent = new Intent(InsValidationActivity.this, AccueilActivity.class);
            startActivity(intent);
        } else {
            textErreur.setText("Le code saisi ne correspond pas au code envoye !!");
        }
    }
}
