package desk;

public class VideoDoesNotExistException extends Exception {
    public VideoDoesNotExistException(String message){
        super(message);
    }
}
