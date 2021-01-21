package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Class to map "TypeDenree" objects that come from remote API.
 */
public class APITypeDenree implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("espece")
    @Expose
    private String espece;
    @SerializedName("animal")
    @Expose
    private String animal;

    public APITypeDenree(Long id, String nom, String espece, String animal) {
        this.id = id;
        this.nom = nom;
        this.espece = espece;
        this.animal = animal;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getEspece() {
        return espece;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }
}
