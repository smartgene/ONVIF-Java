package be.teletask.onvif;

import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifServices;
import be.teletask.onvif.parsers.*;
import be.teletask.onvif.requests.*;
import be.teletask.onvif.responses.OnvifResponse;
import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tomas Verhelst on 03/09/2018.
 * Copyright (c) 2018 TELETASK BVBA. All rights reserved.
 */
public class OnvifExecutor {

    //Constants
    public static final String TAG = OnvifExecutor.class.getSimpleName();

    //Attributes
    private OkHttpClient client;
    private MediaType reqBodyType;
    private RequestBody reqBody;

    private Credentials credentials;
    private OnvifResponseListener onvifResponseListener;

    //Constructors

    OnvifExecutor(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
        credentials = new Credentials("username", "password");
        DigestAuthenticator authenticator = new DigestAuthenticator(credentials);
        Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();

        client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                .build();

        reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");
    }

    //Methods

    /**
     * Sends a request to the Onvif-compatible device.
     *
     * @param device
     * @param request
     */
    void sendRequest(OnvifDevice device, OnvifRequest request) {
        if (onvifResponseListener == null) {
            return;
        }

        credentials.setUserName(device.getUsername());
        credentials.setPassword(device.getPassword());
        reqBody = RequestBody.create(OnvifXMLBuilder.getSoapHeader() + request.getXml() + OnvifXMLBuilder.getEnvelopeEnd(), reqBodyType);
        performXmlRequest(device, request, buildOnvifRequest(device, request));
    }

    /**
     * Clears up the resources.
     */
    public void cleanUp() {
        if (onvifResponseListener != null)
            onvifResponseListener = null;
        client.dispatcher().cancelAll();
    }

    //Properties

    public void setOnvifResponseListener(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
    }

    private void performXmlRequest(OnvifDevice device, OnvifRequest request, Request xmlRequest) {
        if (xmlRequest == null)
            return;

        client.newCall(xmlRequest)
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response xmlResponse) throws IOException {

                        OnvifResponse response = new OnvifResponse(request);
                        ResponseBody xmlBody = xmlResponse.body();

                        if (xmlResponse.code() == 200 && xmlBody != null) {
                            response.setSuccess(true);
                            response.setXml(xmlBody.string());
                            parseResponse(device, response);
                            return;
                        }

                        String errorMessage = "";
                        if (xmlBody != null)
                            errorMessage = xmlBody.string();

                        if (onvifResponseListener != null)
                            onvifResponseListener.onError(device, xmlResponse.code(), errorMessage);

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (onvifResponseListener != null)
                            onvifResponseListener.onError(device, -1, e.getMessage());
                    }

                });
    }

    private void parseResponse(OnvifDevice device, OnvifResponse response) {
        switch (response.request().getType()) {
            case GET_SERVICES:
                // call this to initialise service paths (device/media/ptz..)
                OnvifServices path = new GetServicesParser().parse(response);
                device.setPath(path);
                ((GetServicesRequest) response.request()).getListener().onServicesReceived(device, path);
                break;
            case GET_DEVICE_INFORMATION:
                ((GetDeviceInformationRequest) response.request()).getListener().onDeviceInformationReceived(device,
                        new GetDeviceInformationParser().parse(response));
                break;
            case GET_MEDIA_PROFILES:
                ((GetMediaProfilesRequest) response.request()).getListener().onMediaProfilesReceived(device,
                        new GetMediaProfilesParser().parse(response));
                break;
            case GET_STREAM_URI:
                GetMediaStreamRequest streamRequest = (GetMediaStreamRequest) response.request();
                streamRequest.getListener().onMediaStreamURIReceived(device, streamRequest.getMediaProfile(),
                        new GetMediaStreamParser().parse(response));
                break;
            case GET_SNAPSHOT_URI:
                GetSnapshotUriRequest getSnapshotUriRequest = (GetSnapshotUriRequest) response.request();
                getSnapshotUriRequest.getListener().onSnapshotURIReceived(device, getSnapshotUriRequest.getMediaProfile(),
                        new GetSnapshotUriParser().parse(response));
                break;
            case GET_STATUS:
                GetStatusRequest getStatusRequest = (GetStatusRequest) response.request();
                getStatusRequest.getListener().onStatusReceived(device, getStatusRequest.getMediaProfile(),
                        new GetStatusParser().parse(response));
                break;
            case GET_PRESETS:
                GetPresetsRequest getPresetsRequest = (GetPresetsRequest) response.request();
                getPresetsRequest.getListener().onPresetsReceived(device, getPresetsRequest.getMediaProfile(),
                        new GetPresetsParser().parse(response));
                break;
            case SET_PRESET:
                SetPresetRequest setPresetRequest = (SetPresetRequest) response.request();
                setPresetRequest.getListener().onPresetReceived(device, setPresetRequest.getMediaProfile(),
                        new SetPresetParser().parse(response));
                break;
            case GET_CONFIGURATIONS:
                ((GetConfigurationsRequest) response.request()).getListener().onConfigurationsReceived(device,
                        new GetConfigurationsParser().parse(response));
                break;
            default:
                onvifResponseListener.onResponse(device, response);
                break;
        }
    }

    private Request buildOnvifRequest(OnvifDevice device, OnvifRequest request) {
        return new Request.Builder()
                .url(getUrlForRequest(device, request))
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .post(reqBody)
                .build();
    }

    private String getUrlForRequest(OnvifDevice device, OnvifRequest request) {
        return device.getHostNameUrl() + getPathForRequest(device, request);
    }

    private String getPathForRequest(OnvifDevice device, OnvifRequest request) {
        switch (request.getType()) {
            case GET_SERVICES:
            case GET_DEVICE_INFORMATION:
                return device.getPath().getDevicePath();
            case GET_MEDIA_PROFILES:
            case GET_STREAM_URI:
            case GET_SNAPSHOT_URI:
                return device.getPath().getMediaPath();
            case ABSOLUTE_MOVE:
            case CONTINUOUS_MOVE:
            case GET_STATUS:
            case GOTO_HOME_POSITION:
            case GOTO_PRESET:
            case GET_PRESETS:
            case SET_PRESET:
            case REMOVE_PRESET:
            case GET_CONFIGURATIONS:
                return device.getPath().getPtzPath();
            default:
                return device.getPath().getServicesPath();
        }
    }

    private String bodyToString(Request request) {

        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            if (copy.body() != null)
                copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
