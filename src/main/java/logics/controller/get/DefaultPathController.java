package logics.controller.get;

import logics.controller.Controller;
import logics.controller.support.RequestUtility;
import utils.FileIoUtils;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Controller that finds url and provides files located in given url with judging whether file is static or non-static.
 */
public class DefaultPathController implements Controller {
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        try {
            String modifiedURL = RequestUtility.urlConverter(httpRequest.getURI().getPath());
            return new HttpResponseVersion1().setResponseCode(200)
                    .setHeader("Content-Type", RequestUtility.getAppropriateContentType(httpRequest))
                    .setBody(FileIoUtils.loadFileFromClasspath(modifiedURL));
        } catch(URISyntaxException | IOException | NullPointerException e){ // URI가 valid하지 않거나, URI가 null이거나 한다면
            e.printStackTrace();
            return new DefaultResponseController().makeResponse(httpRequest);
        }
    }
}
