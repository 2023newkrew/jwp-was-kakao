package utils.requests;

/**
 * RequestMethod represents various http request methods such as GET, POST, PUT, etc.
 */
// 추후 containsBody와 같은 인수를 가지도록 할 수도 있다 - ex. GET 요청의 경우 body를 가지지 않는다.
public enum RequestMethod {
    GET, POST, HEAD, DELETE, PATCH, PUT, OPTIONS, TRACE, CONNECT
}
