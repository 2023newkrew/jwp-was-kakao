package webserver.request;

import controller.HomeController;
import lombok.Getter;
import lombok.Setter;
import model.dto.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static utils.IOUtils.readData;

@Getter
public class HttpRequest {

    private MyHeaders headers;
    private MyParams params;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HttpRequest() {
        this.headers = new MyHeaders();
        this.params = new MyParams();
    }

    // request parsing
    public void parsingRequest(BufferedReader br){
        try{
            String line = br.readLine();

            boolean isFirstLine = false;

            // Request Header
            while(!(Objects.isNull(line) || line.equals(""))){

                isFirstLine = getFirstLineData(isFirstLine, line);

                getContentType(line);

                getContentLength(line);

                getCookie(line);

                line = br.readLine();
            }

        } catch(Exception e){
            logger.info(e.getMessage());
        }

        // [POST] Request Body (추후 다른 메서드로 확장 가능)
        if(this.getMethod().equals("POST")){
            getDataFromRequestBody(br);
        }
    }

    // Cookie 추출
    private void getCookie(String line) {
        if(!line.startsWith("Cookie: ")) return;
        Cookie cookie = new Cookie();
        cookie.parseCookie(line);
        headers.setCookie(cookie);
    }

    // ContentLength 추출
    private void getContentLength(String line) {
        if(!line.startsWith("Content-Length: ")) return;

        String contentLength = line.split(" ")[1];
        headers.put("contentLength", contentLength);
    }

    // ContentType 추출
    private void getContentType(String line) {
        if(!line.startsWith("Accept: ")) return;

        var tokens = line.split(" ")[1];
        String contentType = null;

        if(tokens.length() == 1){
            contentType = tokens.split(";")[0];
        }
        if(tokens.length() > 1){
            contentType = tokens.split(",")[0];
        }

        headers.put("contentType", contentType);
    }

    // Request Header의 첫 줄에서 필요한 데이터(method, path) 추출
    private boolean getFirstLineData(boolean isFirstLine, String line){
        if(isFirstLine) return true;

        String[] tokens = line.split(" ");
        String method = tokens[0];
        String path = tokens[1];

        if(path.contains("?")){
            Arrays.stream(path.split("\\?")[1].split("&")).forEach(str -> {
                var keyAndValue = str.split("=");
                params.put(keyAndValue[0], keyAndValue[1]);
            });
            path = path.split("\\?")[0];
        }

        headers.put("method", method);
        headers.put("path", path);

        return true;
    }

    // RequestBody에서 데이터 추출
    private void getDataFromRequestBody(BufferedReader br) {
        try{
            // Body 데이터 읽기
            String result = readData(br, Integer.parseInt(this.getContentLength()));

            // Key & Value로 Param값 추출
            Arrays.stream(result.split("&")).forEach(str -> {
                var keyAndValue = str.split("=");
                this.putParams(keyAndValue[0], keyAndValue[1]);
            });
        }
        catch(IOException e){
            logger.error(e.getMessage());
        }
    }

    public String getMethod(){
        return this.getHeaders().get("method");
    }

    public String getContentLength(){
        return this.getHeaders().get("contentLength");
    }

    public String getPath(){
        return this.getHeaders().get("path");
    }

    public String getContentType() {
        return this.getHeaders().get("contentType");
    }

    public Cookie getCookie(){
        return this.getHeaders().getCookie();
    }
    public void setCookie(Cookie cookie){
        this.getHeaders().setCookie(cookie);
    }
    public void setPath(String path){
        this.getHeaders().put("path", path);
    }

    public void putParams(String key, String value){
        this.params.put(key, value);
    }

    public boolean compareMethod(String method){
        return this.getMethod().equals(method);
    }
}
