package com.example.projet_mobile.Modele;

public class Annonyme {

    private String identifiant;
    private boolean accepte;
    private String lieu;
    private String recherche;

    public Annonyme(boolean acc, String addr){
        accepte = acc;
        System.out.println("============="+accepte);
        if(acc) {
            String adress[] = sep(addr);
            lieu = adress[0] + " " + adress[1] + " " + adress[2] + " " + adress[3] + " " + adress[4];
        }else {
            lieu = "";
        }
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public void setAccepte(boolean accepte) {
        this.accepte = accepte;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setRecherche(String recherche) {
        this.recherche = recherche;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public boolean isAccepte() {
        return accepte;
    }

    public String getLieu() {
        return lieu;
    }

    public String getRecherche() {
        return recherche;
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
