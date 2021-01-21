package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
/**
 * Class to map "InfosTransformateur" objects that come from remote API.
 */
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
    private String urlInstagram;
    @SerializedName("appartient_groupe")
    @Expose
    private Boolean appartientGroupe;
    @SerializedName("groupe")
    @Expose
    private Object groupe;
    @SerializedName("labels")
    @Expose
    private List<APILabel> labels = null;
    @SerializedName("certifications")
    @Expose
    private List<APICertification> certifications = null;
    @SerializedName("urls")
    @Expose
    private List<APIVideo> urls = null;
    @SerializedName("fermesP")
    @Expose
    private List<APIFermePartenaire> fermesP = null;
    @SerializedName("denrees")
    @Expose
    private List<APIDenreeAnimale> denreesA = null;

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
    public APIInfosTransformateur(Integer id, APITransformateur transformateur, String description, String nombreEmployes, String urlSite, String urlFacebook, String urlTwitter, String urlInstagram, Boolean appartientGroupe, Object groupe, List<APILabel> labels, List<APICertification> certifications, List<APIVideo> urls, List<APIFermePartenaire> fermesP, List<APIDenreeAnimale> denreesA) {
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
        this.fermesP = fermesP;
        this.denreesA = denreesA;
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

    public String getUrlInstagram() {
        return urlInstagram;
    }

    public void setUrlInstagram(String urlInstagram) {
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

    public List<APILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<APILabel> labels) {
        this.labels = labels;
    }

    public List<APICertification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<APICertification> certifications) {
        this.certifications = certifications;
    }

    public List<APIVideo> getUrls() {
        return urls;
    }

    public void setUrls(List<APIVideo> urls) {
        this.urls = urls;
    }

    public List<APIFermePartenaire> getFermesP() {
        return fermesP;
    }

    public void setFermesP(List<APIFermePartenaire> fermesP) {
        this.fermesP = fermesP;
    }

    public List<APIDenreeAnimale> getDenreesA() {
        return denreesA;
    }

    public void setDenreesA(List<APIDenreeAnimale> denreesA) {
        this.denreesA = denreesA;
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