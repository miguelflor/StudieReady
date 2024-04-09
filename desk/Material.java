package desk;



import java.io.Serializable;
import java.util.Iterator;

public interface Material extends Comparable<Material>,Serializable {
    /**
     * Returns all the files .pdf of the subject study material
     * @return returns an iterator with all the files .pdf subject study material
     */
    Iterator<FileDetails> getFiles();

    /**
     * Returns all the sites to read of the subject study material
     * @return returns an iterator with all the sites to read of the subject study material
     */
    Iterator<Site> getSites();

    /**
     * Returns all the sites with videos of the subject study material
     * @return returns an iterator with all the subject study material
     */
    Iterator<Site> getVideos();

    /**
     * Returns the name of the material
     * @return the name of the material
     */
    String getName();

    /**
     * Sees if the video exist
     * @param name name of the video
     * @return true if the name of the video already exists in the system, otherwise false
     */
     boolean hasVideo(String name);

    /**
     * Sees if the online document exist
     * @param name name of the document
     * @return true if the name of the document already exists in the system, otherwise false
     */
     boolean hasOnlineDocument(String name);

    /**
     * Sees if the file exist
     * @param name name of the file
     * @return true if the name of the file already exists in the system, otherwise false
     */
     boolean hasFile(String name);

    /**
     * Gets the number of files that this material have
     * @return the number of files that this material have
     */
    int numFiles();

    /**
     * Gets the number of videos that this material have
     * @return the number of videos that this material have
     */
    int numVideos();


    /**
     * Gets the number of online documents that this material have
     * @return the number of online documents that this material have
     */
    int numOnlineDocs();

    /**
     * Sees if there is at least one file
     * @return true if there is at least one file, otherwise false
     */
    boolean hasFiles();

    /**
     * Sees if there is at least one video
     * @return true if there is at least one video, otherwise false
     */
    boolean hasVideos();

    /**
     * Sees if there is at least one online document
     * @return true if there is at least one online document, otherwise false
     */
    boolean hasOnlineDocs();

}
