package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Class to map "UrlVideo" objects that come from remote API.
 */
public class APIVideo implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("titre")
    @Expose
    private String titre;

    public APIVideo() {
    }

    public APIVideo(Long id, String libelle, String titre) {
        this.id = id;
        this.libelle = libelle;
        this.titre = titre;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
