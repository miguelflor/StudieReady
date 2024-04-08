package desk;

public class NonexistentArgumentException extends Exception{
    public NonexistentArgumentException(String arg) {
        super(arg);
    }
}
