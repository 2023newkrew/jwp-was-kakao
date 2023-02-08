package logics.get;

import logics.Controller;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;
import utils.session.SessionManager;

import java.util.Objects;

/**
 * In case of being already logged-in, redirect to index.html
 */
public class LoginPageController extends Controller {
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        String sessionId = parseSessionKey(httpRequest.getHeaderParameter("Cookie"));
        if (Objects.isNull(sessionId) || Objects.isNull(SessionManager.getManager.findSession(sessionId))) {
            return defaultPathHandling(httpRequest);
        }
        return new HttpResponseVersion1().setResponseCode(302)
                .setHeader("Location", "/index.html");
    }

    private String parseSessionKey(String value){
        try {
            String[] parsedValue = value.split(";");
            for (String v : parsedValue){
                if (v.trim().startsWith("JSESSIONID") && v.trim().split("=").length == 2){
                    return v.trim().split("=")[1];
                }
            }
            return null;
        } catch(IndexOutOfBoundsException e){
            return null;
        }
    }
}
