package nstar.usna.edu.nstar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m181446 on 2/15/18.
 */

public class ChangeRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://midn.cs.usna.edu/~m181446/nstar/updateUser.php";
    private Map<String, String> params;

    public ChangeRequest(String username, String phoneNumber, String busVoltage, String busCurrent,
                         String tempZP, String tempZone, String tempBat, Response.Listener<String>listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("number", phoneNumber);
        params.put("BUS_VOLT_LIMIT", busVoltage);
        params.put("BUS_CUR_LIMIT", busCurrent);
        params.put("TEMP_ZP_LIMIT", tempZP);
        params.put("TEMP_ZN_LIMIT", tempZone);
        params.put("BAT_TEMP_LIMIT", tempBat);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
