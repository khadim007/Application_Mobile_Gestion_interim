package com.example.projet_mobile.Modele;

import android.content.Context;
import android.content.SharedPreferences;

public class Annonyme {

    SharedPreferences sharedPreferences;

    private String identifiant;
    private boolean accepte;
    private String lieu;
    private String recherche;

    public Annonyme(boolean acc, String addr, Context context){
        accepte = acc;
        if(acc) {
            String adress[] = sep(addr);
            lieu = adress[0] + " " + adress[1] + " " + adress[2] + " " + adress[3] + " " + adress[4];
        }else {
            lieu = "";
        }
        sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AnnonymeLieu", lieu);
        editor.commit();
    }

    private String[] sep(String s){
        String e1[] = s.split(",");
        String e2[] = e1[0].split(" ");
        String e[] = new String[5];
        e[0] = e2[0];
        s = "";
        for(int i = 1; i < e2.length; i++)
            s = s+" "+e2[i];
        e[1] = s;
        e[2] = e1[1];
        e[3] = e1[2];
        e[4] = e1[3];
        return e;
    }
}
