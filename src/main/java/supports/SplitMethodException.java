package supports;

public class SplitMethodException extends RuntimeException{
    public SplitMethodException(){
        super("split 대상이 null입니다.");
    }
}
