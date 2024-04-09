package desk;



import java.util.*;

class MaterialClass implements Material{

    private final String name;
    private final Map<String, FileDetails> files;
    private final Map<String,Site> onlineDocs;
    private final Map<String,Site> videos;

    //Serial Version UID of the Class
    static final long serialVersionUID = 0L;

    public MaterialClass(String name){
        this.name = name;
        files = new TreeMap<>();
        videos = new HashMap<>();
        onlineDocs = new HashMap<>();
    }

    @Override
    public Iterator<FileDetails> getFiles() {
        return files.values().iterator();
    }

    @Override
    public Iterator<Site> getSites() {
        return onlineDocs.values().iterator();
    }

    @Override
    public Iterator<Site> getVideos() {
        return videos.values().iterator();
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean hasFile(String name){
        return files.containsKey(name);
    }

    public boolean hasOnlineDocument(String name){
        return onlineDocs.containsKey(name);
    }

    public boolean hasVideo(String name){
        return videos.containsKey(name);
    }

    @Override
    public boolean hasFiles() {
        return numFiles()!=0;
    }

    @Override
    public boolean hasVideos() {
        return numVideos()!=0;
    }

    @Override
    public boolean hasOnlineDocs() {
        return numOnlineDocs()!=0;
    }

    @Override
    public int numFiles() {
        return files.size();
    }

    @Override
    public int numVideos() {
        return videos.size();
    }

    @Override
    public int numOnlineDocs() {
        return onlineDocs.size();
    }

    @Override
    public int compareTo(Material o) {
       return name.compareTo(o.getName());
    }

    /**
     * Adds a new file to the study material
     * @param fileDetails Object File
     */
    protected void addFile(FileDetails fileDetails){
        files.put(fileDetails.getName(), fileDetails);
    }

    /**
     * Adds a new video to the study material
     * @param video Object Site type VIDEO
     */
    protected void addVideo(Site video){
        videos.put(video.getName(),video);
    }

    /**
     * Adds a new site to the study material
     * @param site Object Site with type READ
     */
    protected void addSite(Site site){
        onlineDocs.put(site.getName(),site);
    }

    /**
     * Removes the file from this material
     * @param name the name of the file
     */
    protected void removeFile(String name){files.remove(name);}

    /**
     * Removes the video from this material
     * @param name the name of the video
     */
    protected void removeVideo(String name){videos.remove(name);}

    /**
     * Removes the online document from this material
     * @param name the name of the online document
     */
    protected void removeOnlineDoc(String name){onlineDocs.remove(name);}





}
