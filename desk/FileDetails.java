package desk;

import java.io.Serializable;

public interface FileDetails extends Serializable{
    /**
     * returns the full path of this file
     * @return the full path of this file
     */
    String getPath();

    /**
     * returns the name of this file
     * @return the name of this file
     */
    String getName();

    /**
     * Returns the type of this file
     * @return the type of this file
     */
    FileType getType();

}
