package infra.webserver;

import infra.dispatcherservlet.FrontController;
import infra.utils.request.MyRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final FrontController frontController;

    public RequestHandler(Socket connection, FrontController frontController) {
        this.connection = connection;
        this.frontController = frontController;
    }

    public void run() {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))
        ) {
            MyRequest myRequest = MyRequest.builder(bufferedReader)
                    .build();
            DataOutputStream dos = new DataOutputStream(out);
            toFrontController(myRequest, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private void toFrontController(MyRequest myRequest, DataOutputStream dos)
            throws InvocationTargetException, IllegalAccessException {
        frontController.handle(myRequest, dos);

    }
}
