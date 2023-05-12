package com.example.projet_mobile.Modele;

import android.content.Context;
import android.content.SharedPreferences;

public class Annonyme {
    static SharedPreferences sharedPreferences;

    public boolean accepte;
    public String lieu = "";

    public Annonyme(Context context){
        accepte = false;
        sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("AnnonymeAccepte", accepte);
        editor.commit();
    }

    public Annonyme(boolean acc, String addr, Context context){
        accepte = acc;
        if(acc){
            String[] adress = sep(addr);
            lieu = adress[2]; // jusqu'a 4. 2 c'est la ville
            lieu = lieu.substring(1);
        }
        sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AnnonymeLieu", lieu);
        editor.putBoolean("AnnonymeAccpete", accepte);
        editor.commit();
    }

    public static Boolean getAccepte(Context context) {
        sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("AnnonymeAccpete",false);
    }

    public static String getLieu(Context context) {
        sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        return sharedPreferences.getString("AnnonymeLieu","");
    }

    public static String getRecherche(Context context) {
        sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        return sharedPreferences.getString("AnnonymeRecherche","");
    }

    private String[] sep(String s){
        String[] e1 = s.split(",");
        String[] e2 = e1[0].split(" ");
        String[] e = new String[5];
        e[0] = e2[0];
        s = "";
        for(int i = 1; i < e2.length; i++)
            s = s+" "+e2[i];
        e[1] = s;
        e[2] = e1[1];
        e[3] = e1[2];
        //e[4] = e1[3];
        return e;
    }
}
