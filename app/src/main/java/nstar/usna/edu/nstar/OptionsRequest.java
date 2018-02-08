package nstar.usna.edu.nstar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m181446 on 2/6/18.
 */

public class OptionsRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://midn.cs.usna.edu/~m181446/nstar/checkLimits.php";
    public Map<String, String> params;

    public OptionsRequest(String username, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
