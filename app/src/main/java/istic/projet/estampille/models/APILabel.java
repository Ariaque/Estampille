package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APILabel implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("libelle")
    @Expose
    private String libelle;

    public APILabel() {

    }

    public APILabel(Long id, String libelle) {
        super();
        this.id = id;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
