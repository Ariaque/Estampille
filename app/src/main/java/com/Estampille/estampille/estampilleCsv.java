package com.Estampille.estampille;

public class estampilleCsv {

    private int dep;
    private String code;
    private int siret;
    private String nom;
    private String adresse;
    private int codePostal;
    private String commune;
    private String categorie;
    private String activite;
    private String espece;



    //Getter
    public int getDep() {
        return dep;
    }

    public String getCode() {
        return code;
    }

    public int getSiret() {
        return siret;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public String getCommune() {
        return commune;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getActivite() {
        return activite;
    }

    public String getEspece() {
        return espece;
    }


    //Setter
    public void setDep(int dep) {
        this.dep = dep;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSiret(int siret) {
        this.siret = siret;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }


    @Override
    public String toString() {
        return "estampilleCsv{" +
                "dep=" + dep +
                ", code='" + code + '\'' +
                ", siret=" + siret +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", codePostal=" + codePostal +
                ", commune='" + commune + '\'' +
                ", categorie='" + categorie + '\'' +
                ", activite='" + activite + '\'' +
                ", espece='" + espece + '\'' +
                '}';
    }
}
