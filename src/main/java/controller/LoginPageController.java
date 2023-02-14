package controller;

import exception.RedirectException;
import view.HandlebarsViewRenderer;
import view.ViewRenderer;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.io.IOException;

public class LoginPageController implements Controller {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException, IOException {
        HttpSession session = httpRequest.getSession();
        if (session != null && session.getAttribute("user") != null) {
            String location = "/index.html";
            httpResponse.sendRedirect(location);
            throw new RedirectException(location);
        }

        ViewRenderer viewRenderer = new HandlebarsViewRenderer(httpRequest.getUrl());
        viewRenderer.render(httpResponse);
    }
}
