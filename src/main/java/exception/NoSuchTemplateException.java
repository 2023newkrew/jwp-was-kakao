package exception;

public class NoSuchTemplateException extends RuntimeException {
    public NoSuchTemplateException(){
        super("경로에 템플릿이 없습니다.");
    }
}
