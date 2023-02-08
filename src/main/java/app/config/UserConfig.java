package app.config;

import app.user.controller.UserController;
import app.user.dao.UserDao;
import app.user.dao.UserDaoImpl;
import app.user.db.UserDatabase;
import app.user.db.UserDatabaseImpl;
import app.user.repository.UserRepository;
import app.user.repository.UserRepositoryImpl;
import app.user.service.UserService;
import app.user.service.UserServiceImpl;

public class UserConfig {

    private final UserDatabase userDatabase;
    private final UserRepository userRepository;
    private final UserDao userDao;
    private final UserService userService;
    private final UserController userController;

    private UserConfig() {
        userDatabase = new UserDatabaseImpl();
        userRepository = new UserRepositoryImpl(userDatabase);
        userDao = new UserDaoImpl(userRepository);
        userService = new UserServiceImpl(userDao);
        userController = new UserController(userService);
    }

    private static class LazyHolder {
        private static final UserConfig instance = new UserConfig();

    }

    public static UserConfig getInstance() {
        return LazyHolder.instance;
    }

    public UserController getUserController() {
        return userController;
    }

    public UserService getUserService() {
        return userService;
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
}
