package desk;

public enum ContentType {
    ONLINE_DOC,
    FILE,
    VIDEO;
    //Serial Version UID of the Class
    static final long serialVersionUID = 0L;

    public static int size(){
        return values().length;
    }
}
