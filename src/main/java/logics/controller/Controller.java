package logics.controller;

import utils.requests.HttpRequest;
import utils.response.HttpResponse;

/**
 * Controller abstract class contains defaultContentType,
 * and defines what should be implemented in concrete class.
 */
public interface Controller {
    HttpResponse makeResponse(HttpRequest httpRequest);
}
