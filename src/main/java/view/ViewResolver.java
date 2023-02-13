package view;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public View resolve(String view) {
        if (view.startsWith(REDIRECT_PREFIX)) {
            String targetUrl = view.substring(REDIRECT_PREFIX.length());
            return new RedirectView(targetUrl);
        }

        return new HandleBarView(view);
    }

}
