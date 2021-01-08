package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class APIInfosTransformateur implements Serializable {

    private static final long serialVersionUID = 2L;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transformateur")
    @Expose
    private APITransformateur transformateur;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("nombre_employes")
    @Expose
    private String nombreEmployes;
    @SerializedName("url_site")
    @Expose
    private String urlSite;
    @SerializedName("url_facebook")
    @Expose
    private String urlFacebook;
    @SerializedName("url_twitter")
    @Expose
    private String urlTwitter;
    @SerializedName("url_instagram")
    @Expose
    private Object urlInstagram;
    @SerializedName("appartient_groupe")
    @Expose
    private Boolean appartientGroupe;
    @SerializedName("groupe")
    @Expose
    private Object groupe;
    @SerializedName("labels")
    @Expose
    private List<Object> labels = null;
    @SerializedName("certifications")
    @Expose
    private List<Object> certifications = null;
    @SerializedName("urls")
    @Expose
    private List<Object> urls = null;

    /**
     * No args constructor for use in serialization
     */
    public APIInfosTransformateur() {
    }

    /**
     * @param urlFacebook
     * @param urlTwitter
     * @param description
     * @param appartientGroupe
     * @param urlSite
     * @param urlInstagram
     * @param certifications
     * @param labels
     * @param urls
     * @param nombreEmployes
     * @param id
     * @param groupe
     */
    public APIInfosTransformateur(Integer id, APITransformateur transformateur, String description, String nombreEmployes, String urlSite, String urlFacebook, String urlTwitter, Object urlInstagram, Boolean appartientGroupe, Object groupe, List<Object> labels, List<Object> certifications, List<Object> urls) {
        super();
        this.id = id;
        this.transformateur = transformateur;
        this.description = description;
        this.nombreEmployes = nombreEmployes;
        this.urlSite = urlSite;
        this.urlFacebook = urlFacebook;
        this.urlTwitter = urlTwitter;
        this.urlInstagram = urlInstagram;
        this.appartientGroupe = appartientGroupe;
        this.groupe = groupe;
        this.labels = labels;
        this.certifications = certifications;
        this.urls = urls;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public APITransformateur getTransformateur() {
        return transformateur;
    }

    public void setTransformateur(APITransformateur transformateur) {
        this.transformateur = transformateur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNombreEmployes() {
        return nombreEmployes;
    }

    public void setNombreEmployes(String nombreEmployes) {
        this.nombreEmployes = nombreEmployes;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    public String getUrlFacebook() {
        return urlFacebook;
    }

    public void setUrlFacebook(String urlFacebook) {
        this.urlFacebook = urlFacebook;
    }

    public String getUrlTwitter() {
        return urlTwitter;
    }

    public void setUrlTwitter(String urlTwitter) {
        this.urlTwitter = urlTwitter;
    }

    public Object getUrlInstagram() {
        return urlInstagram;
    }

    public void setUrlInstagram(Object urlInstagram) {
        this.urlInstagram = urlInstagram;
    }

    public Boolean getAppartientGroupe() {
        return appartientGroupe;
    }

    public void setAppartientGroupe(Boolean appartientGroupe) {
        this.appartientGroupe = appartientGroupe;
    }

    public Object getGroupe() {
        return groupe;
    }

    public void setGroupe(Object groupe) {
        this.groupe = groupe;
    }

    public List<Object> getLabels() {
        return labels;
    }

    public void setLabels(List<Object> labels) {
        this.labels = labels;
    }

    public List<Object> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Object> certifications) {
        this.certifications = certifications;
    }

    public List<Object> getUrls() {
        return urls;
    }

    public void setUrls(List<Object> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "APIInfosTransformateur{" +
                "id=" + id +
//                ", transformateur=" + transformateur +
                ", description='" + description + '\'' +
                ", nombreEmployes='" + nombreEmployes + '\'' +
                ", urlSite='" + urlSite + '\'' +
                ", urlFacebook='" + urlFacebook + '\'' +
                ", urlTwitter='" + urlTwitter + '\'' +
                ", urlInstagram=" + urlInstagram +
                ", appartientGroupe=" + appartientGroupe +
                ", groupe=" + groupe +
                //", labels=" + labels +
                ", certifications=" + certifications +
                //", urls=" + urls +
                '}';
    }
}