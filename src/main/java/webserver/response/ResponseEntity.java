package webserver.response;

import controller.HomeController;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import model.dto.MyHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.DataOutputStream;

import static webserver.response.ResponseHeaders.*;

@Builder
public class ResponseEntity {

    private static final Logger logger = LoggerFactory.getLogger(ResponseEntity.class);
    private int status;
    private String contentType;
    private String cookie;
    private String redirectUrl;
    private byte[] body;

    public void response(DataOutputStream dos) {
        if(this.status == 200){
            response200Header(dos, this.contentType, this.cookie, this.body.length);
            ResponseBodies.responseBody(dos, this.body);
        }

        // redirect
        if(this.status == 302){
            logger.info("REDIRECT COOKIE : {}", this.cookie);
            response302Header(dos, this.cookie, this.redirectUrl);
        }
    } // 입력된 데이터 바탕으로 응답 로직 작성
}
