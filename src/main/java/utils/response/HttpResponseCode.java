package utils.response;

/**
 * Express server response code such as OK(200), BAD_REQUEST(400), etc.
 */
public enum HttpResponseCode {
    OK(200, "OK"), FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad Request"), NOT_FOUND(404, "Not Found"), METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String response;
    HttpResponseCode(int statusCode, String response){
        this.statusCode = statusCode;
        this.response = response;
    }

    /**
     * find HttpResponseCode by statusCode such as 200, 404.
     * @param statusCode to be matched with HttpResponseCode.
     * @throws IllegalArgumentException when status code is not supported.
     * @return correspondent HttpResponseCode.
     */
    public static HttpResponseCode findByStatusCode(int statusCode){
        for (HttpResponseCode code : HttpResponseCode.values()){
            if (code.statusCode == statusCode){
                return code;
            }
        }
        throw new IllegalArgumentException("Unsupported code");
    }

    @Override
    public String toString(){
        return this.statusCode + " " + this.response;
    }
}
