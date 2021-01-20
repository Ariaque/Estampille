package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Class to map "OrigineDenree" objects that come from remote API.
 */
public class APIOrigineDenree implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("pays")
    @Expose
    private String pays;
    @SerializedName("region")
    @Expose
    private String region;

    public APIOrigineDenree(Long id, String pays, String region) {
        this.id = id;
        this.pays = pays;
        this.region = region;
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

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
