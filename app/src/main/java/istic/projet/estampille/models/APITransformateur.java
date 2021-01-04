package istic.projet.estampille.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APITransformateur implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("num_agrement")
    @Expose
    private String numAgrement;
    @SerializedName("siret")
    @Expose
    private String siret;
    @SerializedName("raison_sociale")
    @Expose
    private String raisonSociale;
    @SerializedName("adresse")
    @Expose
    private String adresse;
    @SerializedName("code_postal")
    @Expose
    private String codePostal;
    @SerializedName("commune")
    @Expose
    private String commune;
    @SerializedName("categorie")
    @Expose
    private String categorie;
    @SerializedName("act_associees")
    @Expose
    private String actAssociees;
    @SerializedName("espece")
    @Expose
    private String espece;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    /**
     * No args constructor for use in serialization
     *
     */
    public APITransformateur() {
    }

    /**
     *
     * @param categorie
     * @param raisonSociale
     * @param commune
     * @param actAssociees
     * @param espece
     * @param latitude
     * @param adresse
     * @param id
     * @param codePostal
     * @param numAgrement
     * @param siret
     * @param longitude
     */
    public APITransformateur(Integer id, String numAgrement, String siret, String raisonSociale, String adresse, String codePostal, String commune, String categorie, String actAssociees, String espece, String latitude, String longitude) {
        super();
        this.id = id;
        this.numAgrement = numAgrement;
        this.siret = siret;
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.commune = commune;
        this.categorie = categorie;
        this.actAssociees = actAssociees;
        this.espece = espece;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumAgrement() {
        return numAgrement;
    }

    public void setNumAgrement(String numAgrement) {
        this.numAgrement = numAgrement;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getActAssociees() {
        return actAssociees;
    }

    public void setActAssociees(String actAssociees) {
        this.actAssociees = actAssociees;
    }

    public String getEspece() {
        return espece;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("id", id).append("numAgrement", numAgrement).append("siret", siret).append("raisonSociale", raisonSociale).append("adresse", adresse).append("codePostal", codePostal).append("commune", commune).append("categorie", categorie).append("actAssociees", actAssociees).append("espece", espece).append("latitude", latitude).append("longitude", longitude).toString();
//    }

}

