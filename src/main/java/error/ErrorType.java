package error;

public enum ErrorType {

    BUFFER_READ_FAILED("버퍼를 읽어오는 데 실패하였습니다."),
    FILE_READ_FAILED("파일을 읽어오는 데 실패하였습니다."),
    DATA_WRITE_FAILED("데이터를 쓰는 데 실패하였습니다."),
    CONTROLLER_NOT_FOUND("해당 요청을 처리할 컨트롤러가 존재하지 않습니다."),
    UNSUPPORTED_PROTOCOL("지원하지 않는 프로토콜입니다."),
    UNSUPPORTED_CONTENT_TYPE("지원하지 않는 컨텐츠 타입입니다.");
    ;

    private String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
