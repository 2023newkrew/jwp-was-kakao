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

// fluent : 모든 식별자에 대한 setter 동일한 이름으로 생성
// chain : set 메서드를 chain 형식으로
//@Accessors(fluent = true, chain = true)
//@Setter
//@RequiredArgsConstructor(staticName = "of")
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
