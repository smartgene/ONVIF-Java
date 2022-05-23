package be.teletask.onvif.models;

/**
 * Created by Luke F on 10/01/2022.
 */
public class OnvifConfiguration {

    //Constants
    public static final String TAG = OnvifConfiguration.class.getSimpleName();

    //Attributes

    //Constructors
    private String name, token;
    private String minPan = "-1", maxPan = "1", minTilt = "-1", maxTilt = "1", minZoom = "0", maxZoom = "1";

    //Constructors
    public OnvifConfiguration() {
    }

    public OnvifConfiguration(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public OnvifConfiguration(String name, String token, String minPan, String maxPan, String minTilt, String maxTilt, String minZoom, String maxZoom) {
        this.name = name;
        this.token = token;
        this.minPan = minPan;
        this.maxPan = maxPan;
        this.minTilt = minTilt;
        this.maxTilt = maxTilt;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    //Properties

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(String minZoom) {
        this.minZoom = minZoom;
    }

    public String getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(String maxZoom) {
        this.maxZoom = maxZoom;
    }

    public String getMinPan() {
        return minPan;
    }

    public void setMinPan(String minPan) {
        this.minPan = minPan;
    }

    public String getMaxPan() {
        return maxPan;
    }

    public void setMaxPan(String maxPan) {
        this.maxPan = maxPan;
    }

    public String getMinTilt() {
        return minTilt;
    }

    public void setMinTilt(String minTilt) {
        this.minTilt = minTilt;
    }

    public String getMaxTilt() {
        return maxTilt;
    }

    public void setMaxTilt(String maxTilt) {
        this.maxTilt = maxTilt;
    }


    @Override
    public String toString() {
        return "OnvifConfiguration{" +
                "token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", minPan='" + minPan + '\'' +
                ", maxPan='" + maxPan + '\'' +
                ", minTilt='" + minTilt + '\'' +
                ", maxTilt='" + maxTilt + '\'' +
                ", minZoom='" + minZoom + '\'' +
                ", maxZoom='" + maxZoom + '\'' +
                '}';
    }

}
