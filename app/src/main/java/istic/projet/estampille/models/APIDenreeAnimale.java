package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APIDenreeAnimale implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("origine")
    @Expose
    private String origine;

    public APIDenreeAnimale() {
    }

    public APIDenreeAnimale(Long id, String nom, String origine) {
        this.id = id;
        this.nom = nom;
        this.origine = origine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }
}
