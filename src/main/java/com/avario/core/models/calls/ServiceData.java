package com.avario.core.models.calls;

import com.google.gson.annotations.SerializedName;

/**
 * Created by orly on 2/1/18.
 */

public class ServiceData {

    @SerializedName("entity_id")
    private String entityId;

    @SerializedName("avl_entity_id")
    private String avlEntityId;

    @SerializedName("brightness")
    private int brightness;

    @SerializedName("algorithm")
    private String algorithm;

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setAvlEntityId(String avlEntityId) {
        this.avlEntityId = avlEntityId;
    }

    public String getAvlEntityId() {
        return avlEntityId;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
