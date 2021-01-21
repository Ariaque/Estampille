package istic.projet.estampille.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Class to map "DenreeAnimale" objects that come from remote API.
 */
public class APIDenreeAnimale implements  Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("typeDenree")
    @Expose
    private APITypeDenree typeDenree;
    @SerializedName("origineDenree")
    @Expose
    private APIOrigineDenree origineDenree;
    @SerializedName("infosTypeDenree")
    @Expose
    private String infosTypeDenree;
    @SerializedName("infosOrigineDenree")
    @Expose
    private String infosOrigineDenree;

    public APIDenreeAnimale(Long id, APITypeDenree typeDenree, APIOrigineDenree origineDenree, String infosTypeDenree, String infosOrigineDenree) {
        this.id = id;
        this.typeDenree = typeDenree;
        this.origineDenree = origineDenree;
        this.infosTypeDenree = infosTypeDenree;
        this.infosOrigineDenree = infosOrigineDenree;
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

    public APITypeDenree getTypeDenree() {
        return typeDenree;
    }

    public void setTypeDenree(APITypeDenree typeDenree) {
        this.typeDenree = typeDenree;
    }

    public APIOrigineDenree getOrigineDenree() {
        return origineDenree;
    }

    public void setOrigineDenree(APIOrigineDenree origineDenree) {
        this.origineDenree = origineDenree;
    }

    public String getInfosTypeDenree() {
        return infosTypeDenree;
    }

    public void setInfosTypeDenree(String infosTypeDenree) {
        this.infosTypeDenree = infosTypeDenree;
    }

    public String getInfosOrigineDenree() {
        return infosOrigineDenree;
    }

    public void setInfosOrigineDenree(String infosOrigineDenree) {
        this.infosOrigineDenree = infosOrigineDenree;
    }
}
