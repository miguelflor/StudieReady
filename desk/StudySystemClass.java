package desk;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.*;

public class StudySystemClass implements StudySystem{
    //Serial Version UID of the Class
    @Serial
    private static final long serialVersionUID = 0L;
    private static final String ADOBE_DIRECTORY = "C:\\Program Files\\Adobe\\Acrobat DC\\Acrobat";
    private static final String CHROME_DIRECTORY = "C:\\Program Files\\Google\\Chrome\\Application";
    private static final String ADOBE_EXE = "Acrobat.exe";
    private static final String CHROME_EXE = "chrome.exe";
    private static final String NEW_WINDOW_CHROME = "--new-window";
    private static final String FULL_WINDOW_CHROME = "--start-maximized";
    private static final String NEW_WINDOW_ADOBE = "/n";
    private static final String CMD = "cmd";
    private static final String C = "/c";

    private final Map<String,Material> materials;
    private transient Map<String,Map<ContentType,Process>> startedMaterials;

    public StudySystemClass(){
        startedMaterials = new HashMap<>();
        materials = new TreeMap<>();
    }

    @Override
    public void addMaterial(String name) throws MaterialAlreadyExistsException {
        if(hasMaterial(name)){
            throw new MaterialAlreadyExistsException();
        }

        Material m = new MaterialClass(name);

        materials.put(name,m);
    }


    @Override
    public void removeMaterial(String name) throws MaterialDoesNotExistException {
        if(!hasMaterial(name)){
            throw new MaterialDoesNotExistException();
        }
        materials.remove(name);
    }

    @Override
    public void addVideo(String materialName, String videoName, String URL)
            throws MaterialDoesNotExistException,VideoAlreadyExistException {
        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }
        if(hasVideo(materialName,videoName)){
            throw new VideoAlreadyExistException();
        }

        Site video = new SiteClass(videoName,URL,SitesType.VIDEOS);

        ((MaterialClass) materials.get(materialName)).addVideo(video);


    }

    @Override
    public void addOnlineDocument(String materialName, String documentName, String URL)
            throws MaterialDoesNotExistException,OnlineDocumentAlreadyExistException {

        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }
        if(hasOnlineDocument(materialName,documentName)){
            throw new OnlineDocumentAlreadyExistException();
        }

        Site onlineDocument = new SiteClass(documentName,URL,SitesType.DOCUMENT);

        ((MaterialClass) materials.get(materialName)).addSite(onlineDocument);

    }

    @Override
    public void addFile(String materialName, String fileName, String path)
            throws MaterialDoesNotExistException, FileAlreadyExistException,InvalidFileTypeException {

        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }
        if(hasFile(materialName,fileName)){
            throw new FileAlreadyExistException();
        }

        FileType type = FileType.toType(path);

        if(type.equals(FileType.NONE)){
            throw new InvalidFileTypeException();
        }

        FileDetails fileDetails = new FileDetailsClass(path,fileName,type);
        ((MaterialClass) materials.get(materialName)).addFile(fileDetails);

    }

    @Override
    public void addFile(String materialName, String path)
            throws MaterialDoesNotExistException, FileAlreadyExistException, InvalidFileTypeException {

        File file = new File(path);
        if(file.isDirectory()){
            File[] filesList = file.listFiles();
            for (File f :
                    Objects.requireNonNull(filesList)) {
                addFile(materialName,f.getName().replace(" ","_"),f.getAbsolutePath());
            }
        }else{
            addFile(materialName,file.getName().replace(" ","_"),file.getAbsolutePath());
        }

    }

    @Override
    public void removeFile(String materialName, String fileName)
            throws MaterialDoesNotExistException,FileDoesNotExistException {
        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }

        if(!hasFile(materialName,fileName)){
            throw new FileDoesNotExistException(fileName);
        }

        ((MaterialClass) materials.get(materialName)).removeFile(fileName);

    }

    @Override
    public void removeVideo(String materialName, String videoName)
            throws MaterialDoesNotExistException,VideoDoesNotExistException {
        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }

        if(!hasVideo(materialName,videoName)){
            throw new VideoDoesNotExistException(videoName);
        }

        ((MaterialClass) materials.get(materialName)).removeVideo(videoName);

    }

    @Override
    public void removeOnlineDoc(String materialName, String onlineDocName)
            throws MaterialDoesNotExistException,OnlineDocDoesNotExistException {
        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }

        if(!hasOnlineDocument(materialName,onlineDocName)){
            throw new OnlineDocDoesNotExistException(onlineDocName);
        }

        ((MaterialClass) materials.get(materialName)).removeOnlineDoc(onlineDocName);


    }

    @Override
    public void startStudy(String materialName) throws MaterialDoesNotExistException, ErrorOpeningMaterialsException,
            AlreadyOpenedException {

        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }

        setStartedMaterials(new HashMap<>());

        if(startedMaterials.containsKey(materialName)){
            if(!removeClosed(materialName)){
                throw new AlreadyOpenedException();
            }
        }

        Material m = materials.get(materialName);

        ProcessBuilder openFilesPb = openFiles(m);
        ProcessBuilder openOnlineDocuments = openSites(m,SitesType.DOCUMENT);
        ProcessBuilder openVideos = openSites(m,SitesType.VIDEOS);

        try {
            Map<ContentType,Process> processes;
            if(allClose(materialName)){
                processes = new HashMap<>(ContentType.size());
            }else{
                processes = startedMaterials.get(materialName);
            }


            if(m.hasFiles()&&!processes.containsKey(ContentType.FILE)){
               processes.put(ContentType.FILE,openFilesPb.start());
            }
            if(m.hasVideos()&&!processes.containsKey(ContentType.VIDEO)){
                processes.put(ContentType.VIDEO,openVideos.start());
            }
            if(m.hasOnlineDocs()&&!processes.containsKey(ContentType.ONLINE_DOC)){
                processes.put(ContentType.ONLINE_DOC,openOnlineDocuments.start());
            }

            if(!processes.isEmpty()){
                startedMaterials.put(materialName,processes);
            }

        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ErrorOpeningMaterialsException();
        }
    }

    @Override
    public void closeStudy(String materialName) throws MaterialDoesNotExistException, ClosedMaterialException {
        if(!hasMaterial(materialName)){
            throw new MaterialDoesNotExistException();
        }
        if(!startedMaterials.containsKey(materialName)){
            throw new ClosedMaterialException();
        }
        setStartedMaterials(new HashMap<>());
        Map<ContentType,Process> processes = startedMaterials.remove(materialName);
        for (Process process :
                processes.values()) {
            if(process.isAlive()){
                process.descendants().forEach(ProcessHandle::destroyForcibly);
            }
        }

    }

    //booleans

    @Override
    public boolean hasFiles(String material) throws MaterialDoesNotExistException {
        if(!hasMaterial(material)){
            throw new MaterialDoesNotExistException();
        }

        return materials.get(material).numFiles()!=0;
    }

    @Override
    public boolean hasVideos(String material) throws MaterialDoesNotExistException {
        if(!hasMaterial(material)){
            throw new MaterialDoesNotExistException();
        }

        return materials.get(material).numVideos()!=0;
    }


    public boolean hasOnlineDocs(String material) throws MaterialDoesNotExistException {
        if(!hasMaterial(material)){
            throw new MaterialDoesNotExistException();
        }

        return materials.get(material).numOnlineDocs()!=0;
    }
    @Override
    public boolean hasMaterial(String name){
        return materials.containsKey(name);
    }

    //Iterators
    @Override
    public Iterator<FileDetails> fileIterator(String material) throws MaterialDoesNotExistException {

        if(!hasMaterial(material)){
            throw new MaterialDoesNotExistException();
        }

        return materials.get(material).getFiles();
    }

    @Override
    public Iterator<Site> videoIterator(String material) throws MaterialDoesNotExistException {

        if(!hasMaterial(material)){
            throw new MaterialDoesNotExistException();
        }

        return materials.get(material).getVideos();
    }

    @Override
    public Iterator<Site> onlineDocIterator(String material) throws MaterialDoesNotExistException {

        if(!hasMaterial(material)){
            throw new MaterialDoesNotExistException();
        }

        return materials.get(material).getSites();
    }

    @Override
    public Iterator<Material> materialIterator() {
        return materials.values().iterator();
    }

    @Override
    public Iterator<Material> activeMaterialIterator() {
        Set<Material> started = new TreeSet<Material>();

        for(String material : startedMaterials.keySet()){
            started.add(materials.get(material));
        }

        return started.iterator();

    }

    @Override
    public int numMaterials() {
        return materials.size();
    }

    @Override
    public int numActiveMaterials() {
        return startedMaterials.size();
    }

    @Override
    public int numFiles(String material) {
        return materials.get(material).numFiles();
    }

    @Override
    public int numVideos(String material) {
        return materials.get(material).numVideos();
    }

    @Override
    public int numOnlineDocs(String material) {
        return materials.get(material).numOnlineDocs();
    }


    //private

    /**
     * Gives a <code>ProcessBuilder</code> object to be start with the cmd code to start the files with the given app
     * At this time only build with the pdf files (Adobe Reader)
     * @param m Object Material that has the files
     * @return  a <code>ProcessBuilder</code> object to be start with the cmd code to start the files with the given app
     */
    private ProcessBuilder openFiles(Material m){
        List<String> command = new ArrayList<>();
        command.add(CMD);
        command.add(C);
        command.add(ADOBE_EXE);
        command.add(NEW_WINDOW_ADOBE);
        for (Iterator<FileDetails> it = m.getFiles(); it.hasNext(); ) {
            FileDetails f = it.next();
            command.add(f.getPath());
        }
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(ADOBE_DIRECTORY));
        return pb;
    }

    /**
     * Opens the onlineDocuments
     * @param m material object
     * @param type type of the material
     * @return a ProcessBuilder object with the execution of sites
     */
    private ProcessBuilder openSites(Material m, SitesType type){

        List<String> command = new ArrayList<>();
        Iterator<Site> it;
        if(type.equals(SitesType.DOCUMENT)){
            it = m.getSites();
        }else{
            it = m.getVideos();
        }

        command.add(CMD);
        command.add(C);
        command.add(CHROME_EXE);
        command.add(NEW_WINDOW_CHROME);
        command.add(FULL_WINDOW_CHROME);

        while (it.hasNext()) {
            Site f = it.next();
            command.add(f.getURL());
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(CHROME_DIRECTORY));
        return pb;
    }


    /**
     * Sees if it has the <code>videoName</code> in the system
     * @param materialName material name
     * @param videoName video name
     * @return true if the <code>videoName</code> is in the system, otherwise false
     */
    private boolean hasVideo(String materialName,String videoName){
        return materials.get(materialName).hasVideo(videoName);
    }

    /**
     * Sees if it has the <code>fileName</code> in the system
     * @param materialName material name
     * @param fileName file name
     * @return true if the <code>fileName</code> is in the system, otherwise false
     */
    private boolean hasFile(String materialName, String fileName){
        return materials.get(materialName).hasFile(fileName);
    }

    /**
     * Sees if it has the <code>documentName</code> in the system
     * @param materialName material name
     * @param documentName file name
     * @return true if the <code>documentName</code> is in the system, otherwise false
     */
    private boolean hasOnlineDocument(String materialName, String documentName){
        return materials.get(materialName).hasOnlineDocument(documentName);
    }

    /**
     * Removes the closed processes from the system
     * @param materialName material name
     * @return true if a process was removed, otherwise false
     */
    private boolean removeClosed(String materialName){
        Map<ContentType,Process> processes = startedMaterials.get(materialName);
        int initialSize = processes.size();
        processes.values().removeIf(process -> !process.isAlive());

        return processes.size() != initialSize;
    }

    /**
     * Sees if all the content of the <code>materialName</code> are open
     * @param materialName the name of the material
     * @return true if all the content of the <code>materialName</code> are open, otherwise false
     */
    private boolean allClose(String materialName){
        Map<ContentType,Process> processes = startedMaterials.get(materialName);
        return processes==null||processes.isEmpty();
    }

    /**
     * starts the variable that contains the al the process started
     */
    private void setStartedMaterials(Map<String,Map<ContentType,Process>> sm){
        if (startedMaterials == null){
            startedMaterials = sm;
        }

    }

}
