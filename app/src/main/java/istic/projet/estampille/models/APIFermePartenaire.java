package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APIFermePartenaire implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("description")
    @Expose
    private String description;

    public APIFermePartenaire() {

    }

    public APIFermePartenaire(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
