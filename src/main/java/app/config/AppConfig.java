package app.config;


import auth.config.AuthConfig;
import auth.config.AuthConfigProxy;
import auth.filter.JSessionAuthUserDetailsFilter;
import app.home.controller.HomeController;
import app.ico.controller.IcoController;
import app.statics.controller.MyStaticControllerImpl;
import app.user.User;
import infra.session.SessionManager;
import java.util.List;

public class AppConfig {

    private final UserConfig userConfig;
    private final AuthConfig authConfig;
    private final ControllerConfig controllerConfig;

    private AppConfig() {
        SessionManager sessionManager = SessionManager.getInstance();
        userConfig = UserConfig.getInstance();
        authConfig = AuthConfigProxy.createInstance(userConfig.getUserDatabase(), sessionManager);
        controllerConfig = new ControllerConfig(new MyStaticControllerImpl(),
                List.of(new JSessionAuthUserDetailsFilter(sessionManager)),
                new HomeController(),
                new IcoController(),
                UserConfig.getInstance().getUserController(),
                authConfig.getAuthLoginController()
        );
        postConstruct();
    }

    private void postConstruct() {
        userConfig.getUserDatabase().save(User.builder()
                .name("abc")
                .userId("a")
                .password("b")
                .email("d@d").build());

        userConfig.getUserDatabase().save(User.builder()
                .name("abc")
                .userId("ab")
                .password("b")
                .email("d@d").build());

        userConfig.getUserDatabase().save(User.builder()
                .name("abc")
                .userId("abc")
                .password("b")
                .email("d@d").build());

    }

    private static class LazyHolder {
        private static final AppConfig instance = new AppConfig();
    }

    public static AppConfig getInstance() {
        return LazyHolder.instance;
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public ControllerConfig getControllerConfig() {
        return controllerConfig;
    }
}
