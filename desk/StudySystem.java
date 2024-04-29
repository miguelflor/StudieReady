package desk;

import java.io.Serializable;
import java.util.Iterator;

public interface StudySystem extends Serializable {

    /**
     * Adds a new material to the system
     * @param name name of the material
     */
    void addMaterial(String name) throws MaterialAlreadyExistsException;


    /**
     * Adds a new video to the system
     * @param materialName material name
     * @param videoName video name
     * @param URL the URL of the video
     * @throws MaterialDoesNotExistException when the material does not exist
     * @throws VideoAlreadyExistException when the video already exist in the materials
     */
    void addVideo(String materialName, String videoName, String URL)
            throws MaterialDoesNotExistException, VideoAlreadyExistException;

    /**
     * Adds a new online document to the system
     * @param materialName material name
     * @param documentName online document name
     * @param URL the URL of the video
     * @throws MaterialDoesNotExistException when the material does not exist
     * @throws OnlineDocumentAlreadyExistException when the online document already exist in the materials
     */
    void addOnlineDocument(String materialName, String documentName, String URL)
            throws MaterialDoesNotExistException, OnlineDocumentAlreadyExistException;

    /**
     * Adds a new file to the system
     * @param materialName name of the material
     * @param fileName name of the file
     * @param path path of the file
     * @throws MaterialDoesNotExistException when the material does not exist
     * @throws FileAlreadyExistException when the name of the file already exist in the material
     * @throws InvalidFileTypeException when the type of the type is not supported
     */
    void addFile(String materialName, String fileName, String path)
            throws MaterialDoesNotExistException, FileAlreadyExistException, InvalidFileTypeException;

    /**
     * Adds a new file to the system, the name by default is the name of the given file
     * if the <code>path</code> belongs to a directory, will add all the files on the folder.
     * If the name of the file has spaces, it's replaced with "_"
     * @param materialName name of the material
     * @param path the path of the file or folder
     * @throws MaterialDoesNotExistException when the material does not exist
     * @throws FileAlreadyExistException when the name of the file already exist in the material
     * @throws InvalidFileTypeException when the type of the type is not supported
     */
    void addFile(String materialName, String path)
            throws MaterialDoesNotExistException, FileAlreadyExistException, InvalidFileTypeException;


    /**
     * Removes the file of the material
     * @param materialName name of the material
     * @param fileName name of the file
     * @throws MaterialDoesNotExistException when the name of the material does not exist
     * @throws FileDoesNotExistException when the name of the file does not exist
     */
    void removeFile(String materialName, String fileName)
            throws MaterialDoesNotExistException,FileDoesNotExistException;

    /**
     * Removes the video of the material
     * @param materialName name of the material
     * @param videoName name of the video
     * @throws MaterialDoesNotExistException when the name of the material does not exist
     * @throws VideoDoesNotExistException when the name of the video does not exist
     */
    void removeVideo(String materialName, String videoName)
            throws MaterialDoesNotExistException,VideoDoesNotExistException;

    /**
     * Removes the online document of the material
     * @param materialName name of the material
     * @param onlineDocName name of the online document
     * @throws MaterialDoesNotExistException when the name of the material does not exist
     * @throws OnlineDocDoesNotExistException when the name of the online document does not exist
     */
    void removeOnlineDoc(String materialName, String onlineDocName)
            throws MaterialDoesNotExistException,OnlineDocDoesNotExistException;

    /**
     * Starts a new study with the material that have the name <code>materialName</code>
     * @param materialName material name
     * @throws MaterialDoesNotExistException when the material does not exist
     * @throws ErrorOpeningMaterialsException when there is an error on processing the opening
     *                                        of the files of the material
     */
    void startStudy(String materialName) throws MaterialDoesNotExistException, ErrorOpeningMaterialsException,
            AlreadyOpenedException;

    /**
     * Closes a material if this one is open
     * @param materialName the name of the material to close
     * @throws MaterialDoesNotExistException if the <code>materialName</code> exists in the system
     * @throws ClosedMaterialException if the material is not opened
     */
    void closeStudy(String materialName) throws MaterialDoesNotExistException, ClosedMaterialException;

    /**
     * Removes the given material from the system
     * @param name material name
     * @throws MaterialDoesNotExistException when the material does not exist
     */
    void removeMaterial(String name) throws MaterialDoesNotExistException;

    /**
     * Prints all the files of the <code>material</code>
     * @param material the material name
     * @return an iterator with all the files of the material with the name <code>material</code>
     * @throws MaterialDoesNotExistException when the <code>material</code> does not exist in the system
     */
    Iterator<FileDetails> fileIterator(String material) throws MaterialDoesNotExistException;

    /**
     * Prints all the videos of the <code>material</code>
     * @param material the material name
     * @return an Iterator with all the videos of the <code>material</code>
     * @throws MaterialDoesNotExistException when the <code>material</code> does not exist in the system
     */
    Iterator<Site> videoIterator(String material) throws MaterialDoesNotExistException;

    /**
     * Prints all the online documents of the <code>material</code>
     * @param material the material name
     * @return an Iterator with all the online documents of the <code>material</code>
     * @throws MaterialDoesNotExistException when the <code>material</code> does not exist in the system
     */
    Iterator<Site> onlineDocIterator(String material) throws MaterialDoesNotExistException;

    /**
     * Gets all the materials
     * @return An Iterator with all the materials
     */
    Iterator<Material> materialIterator();

    /**
     * Lists all the materials that are started
     * @return An Iterator with all the materials
     */
    Iterator<Material> activeMaterialIterator();

    /**
     * Sees if the material has any files
     * @param material the name of the material
     * @return true if the materials has any files
     * @throws MaterialDoesNotExistException when the <code>material</code> does not exist in the system
     */
    boolean hasFiles(String material) throws MaterialDoesNotExistException;


    /**
     * Sees if the material has any videos
     * @param material the name of the material
     * @return true if the materials has any videos
     * @throws MaterialDoesNotExistException when the <code>material</code> does not exist in the system
     */
    boolean hasVideos(String material) throws MaterialDoesNotExistException;


    /**
     * Sees if the material has any online documents
     * @param material the name of the material
     * @return true if the materials has any online documents
     * @throws MaterialDoesNotExistException when the <code>material</code> does not exist in the system
     */
    boolean hasOnlineDocs(String material) throws MaterialDoesNotExistException;

    /**
     * Sees if the material exists
     * @param material the material name
     * @return true if the <code>material</code> exists in the system, otherwise false
     */
    boolean hasMaterial(String material);

    /**
     * get the number of materials
     * @return the number of materials
     */
    int numMaterials();

    /**
     * get the number of active materials
     * @return the number of active materials
     */
    int numActiveMaterials();

    /**
     * get the number of files
     * @param material the name of the material
     * @return the number of files
     */
    int numFiles(String material);

    /**
     * get the number of videos
     * @param material the name of the material
     * @return the number of videos
     */
    int numVideos(String material);

    /**
     * get the number of online documents
     * @param material the name of the material
     * @return the number of online documents
     */
    int numOnlineDocs(String material);


}
