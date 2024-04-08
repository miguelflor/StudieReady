package desk;

import java.io.Serializable;

public enum FileType implements Serializable {

    HTML,
    PDF,
    NONE,
    WORD;

    //Serial Version UID of the Class
    static final long serialVersionUID = 0L;
    private final static char DOT = '.';



    public static FileType toType(String path){

        StringBuilder type = new StringBuilder();

        for (int i = 0; i < path.length() && path.charAt(path.length()-1-i) != DOT; i++) {
            char c = path.charAt(path.length()-i-1);
            type.insert(0, c);
        }

        String typeString = type.toString();

        switch (typeString.toLowerCase()){
            case "html" -> {return HTML;}
            case "pdf" -> {return PDF;}
            case "docx" -> {return WORD;}
            default -> {return NONE;}
        }

    }

}
