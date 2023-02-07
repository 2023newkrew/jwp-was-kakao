package model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.enumeration.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
@Builder
public class HttpResponse {
    @Builder.Default
    private ResponseHeader header = ResponseHeader.of(new LinkedHashMap<>());
    private HttpStatus status;
    @Builder.Default
    private ResponseBody body = new ResponseBody();

    public boolean isBodyExists() {
        return !body.isEmpty();
    }
    public String findHeaderValue(String key) {
        return header.getHeaders().get(key);
    }

    public void setHeaderAttribute(String key, String value) {
        header.put(key, value);
    }


    public byte[] getBody() {
        return body.get();
    }

    public int getBodyLength() {
        return body.length();
    }

    public Set<Map.Entry<String, String>> getHeaderEntrySet() {
        return header.getEntrySet();
    }

    public String getStatusLine() {
        return status.getStatusLine();
    }
}
