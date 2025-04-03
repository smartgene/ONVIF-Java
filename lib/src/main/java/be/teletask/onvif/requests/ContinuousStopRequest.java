package be.teletask.onvif.requests;

import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.models.OnvifType;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Luke F on 07/08/2020.
 */
@Getter
@NoArgsConstructor
public class ContinuousStopRequest implements OnvifRequest {

    //Constants
    public static final String TAG = ContinuousStopRequest.class.getSimpleName();

    //Attributes
    private OnvifResponseListener listener;
    private OnvifMediaProfile mediaProfile;

    //Constructors
    public ContinuousStopRequest(OnvifMediaProfile mediaProfile) {
        this.mediaProfile = mediaProfile;
    }

    public ContinuousStopRequest(OnvifMediaProfile mediaProfile, OnvifResponseListener listener) {
        this.mediaProfile = mediaProfile;
        this.listener = listener;
    }

    //Properties

    @Override
    public String getXml() {
        String ret= "<Stop xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\">" +
                "<ProfileToken>" + mediaProfile.getToken() + "</ProfileToken>" +
                "</Stop>";
        return ret;
    }

    @Override
    public OnvifType getType() {
        return OnvifType.CONTINUOUS_MOVE;
    }

}
