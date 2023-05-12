package com.example.projet_mobile.Controler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

interface toolbar {

    default void onHomeClick(Context context) {
        Intent intent = new Intent( context, AccueilActivity.class);
        context.startActivity(intent);
    }

    default void onRechercheClick(Context context) {
        Intent intent = new Intent( context, RechercheActivity.class);
        context.startActivity(intent);
    }

    default void onCandidatureClick(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        if(id == 0){
            Intent intent = new Intent( context, AuthentificationActivity.class);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent( context, CandidatureSpontaneActivity.class);
            context.startActivity(intent);
        }
    }

    default void onCompteClick(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CandidatInscrit", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        String role = sharedPreferences.getString("role", "");
        if(id == 0){
            Intent intent = new Intent( context, AuthentificationActivity.class);
            context.startActivity(intent);
        }else{
            if("employeur".equals(role)){
                Intent intent = new Intent( context, EspaceEmployeurActivity.class);
                context.startActivity(intent);
            }else{
                Intent intent = new Intent( context, EspaceCandidatInscritActivity.class);
                context.startActivity(intent);
            }
        }
    }
}
