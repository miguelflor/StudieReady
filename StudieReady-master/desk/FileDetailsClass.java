package desk;

class FileDetailsClass implements FileDetails {
    //Serial Version UID of the Class
    static final long serialVersionUID = 0L;
    private String path;
    private String name;

    private final FileType type;

    /**
     * Creates a file
     */
    public FileDetailsClass(String path, String name, FileType type){
        this.path = path;
        this.name = name;
        this.type = type;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FileType getType() {
        return type;
    }

    /**
     * Sets the name of this file
     * @param name new name of this file
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a new path to this file
     * @param path new path of this file
     */
    protected void setPath(String path) {
        this.path = path;
    }
}
