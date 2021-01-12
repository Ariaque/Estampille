package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APILabel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("libelle")
    @Expose
    private String libelle;

    public APILabel() {

    }

    public APILabel(Integer id, String libelle) {
        super();
        this.id = id;
        this.libelle = libelle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
