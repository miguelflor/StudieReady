import desk.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;



public class Main {
    //print messsages
    public static final String DEFAULT = "Unknown command";

    private static final String SYSTEM_FILE = "material.dat";
    private static final String MATERIAL_EXIST = "%s material name already exist in the system\n";
    private static final String MATERIAL_NOT_EXIST = "%s material name isn't in the system\n";
    private static final String EXIT_MESSAGE = "Goodbye";
    private static final String FILE_EXIST = "%s file already exist\n";
    private static final String TYPE_INVALID = "The type of the file is invalid";
    private static final String ONLINE_DOC_EXIST = "%s online document already exist\n";
    private static final String VIDEO_EXIST = "%s video already exist\n";
    private static final String ERROR_OPENING = "The files are corrupted";

    private static final String ALREADY_STARTED = "The material '%s' is already opened!\n";
    private static final String CLOSE = "\nThe %s was close";
    private static final String CLS = "cls";
    private static final String FILES_PRINT = "Files:\nName\tType\n";
    private static final String FILE_FORMAT = "%s\t%s\n";
    private static final String VIDEO_PRINT = "Video:\n\nName\tDomain\n";
    private static final String VIDEO_FORMAT = "%s\t%s\n";
    private static final String ONLINE_DOC_PRINT = "Online Documents:\n\nName\tDomain\n";
    private static final String ONLINE_DOC_FORMAT = "%s\t%s\n";
    private static final String MATERIAL = "\nMaterial:";
    private static final String ARROW = ">";
    private static final String USAGE = "Usage:";
    private static final String SPACE = " ";
    private static final String FILE_NOT_EXIST = "The file '%s' does not exist\n";
    private static final String ONLINE_DOC_NOT_EXIST = "The online document '%s' does not exist\n";
    private static final String VIDEO_NOT_EXIST ="The video document '%s' does not exist\n";
    private static final String NO_ARG = "This command has no arguments";

    private static final String CLOSED_MATERIAL = "The material '%s' was already killed!\n";

    private static final String HELP = "%s - %s \n";
    private static final String FALSE_ARG = "The command %s does not exist!\n";


    public enum Commands{

        ADD_VIDEO("ADDVIDEO", "Adds a new video.", "'*%s* *name* *URL*'"),
        ADD_FILE("ADDFILE", "Adds all files in a directory if a path to a directory is given," +
                " the name of each file doesn't change, " +
                "if a name and a path is given changes the name of the file to a new name."
                , "'*command* *path*' or '*command* *name* *path*'"),
        ADD_ONLINE_DOC("ADDONLINEDOC", "Adds a new online document.",
                "'*command* *name* *URL*'"),
        ADD_MATERIAL("CREATE", "Adds a new material.", "'*%s* *name*'"),
        UNKNOWN("", "", ""),
        QUIT("QUIT", "Quits and saves the program.", "'*%s*'"),
        REMOVE_MATERIAL("REMOVE", "Removes a material.", "'*%s* *name*'"),
        OPEN("OPEN", "Opens a material.", "'*%s* *name*'"),
        CLOSE("CLOSE", "Closes a material.", "'*%s*'"),
        REMOVE_FILE("REMFILE", "Removes a file.", "'*%s* *name*'"),
        REMOVE_VIDEO("REMVIDEO", "Removes a video.", "'*%s* *name*'"),
        REMOVE_ONLINE_DOC("REMDOC", "Removes a online document.", "'*%s* *name*'"),
        LIST("LS", "lists all the content.\narguments:\n-a -> lists only the active ones", "*%s* *arg*"),
        LIST_MATERIALS("LSM", "lists all the materials.\n","*%s*"),
        START("START", "Build your work table.", "'*%s*'"),
        HELP("HELP", "Prints a message with all the commands.", "'*%s*'"),
        KILL("KILL", "Kills a material.", "'*%s* *name*'");


        private final String command;
        private final String description;
        private final String usage;

        /**
         * Creates a new command
         *
         * @param command     name of the command
         * @param description the description of the command
         * @param usage       the usage of the command
         * @pre <code>command</code>!=null
         */
        Commands(String command, String description, String usage){
            this.command = command;
            this.description = description;
            this.usage = usage;
        }

        /**
         * Gets the command string based on the value of the enum.
         *
         * @return String of the command.
         */
        public String getCommand() {
            return command;
        }


    }
    /**
     * Main function of the main class
     * @param args arguments of the application (not used)
     */
    public static void main(String[] args){

        StudySystem s = load();
        executeCommands(s);
        save(s);

    }

    /**
     * Executes commands in a loop until the user inputs "quit".
     *
     * @param studySystem Study system class that will manage all the most important functions of the program.
     */
    private static void executeCommands(@NotNull StudySystem studySystem) {
        Scanner in = new Scanner(System.in);
        Commands command;

        do {
            System.out.print(ARROW);
            List<String> args = getArgs(in.nextLine());
            command = getCommand(args.getFirst().toUpperCase());
            switch (command){
                case ADD_MATERIAL -> addMaterial(studySystem,args);
                case REMOVE_MATERIAL -> removeMaterial(studySystem,args);
                case OPEN -> openMaterial(studySystem,args,in);
                case LIST -> printMaterials(studySystem,args);
                case START -> startMaterial(studySystem,args);
                case KILL -> kill(studySystem,args);
                case HELP -> printHelp(args);
                case QUIT -> exit(args);
                default -> System.out.println(DEFAULT);
            }
            save(studySystem);
            System.out.println();


        }while (!command.equals(Commands.QUIT));
    }

    /**
     * Prints all the materials names
     */
    private static void printMaterials(StudySystem system, List<String> args) {
        try{
            if(args.size()>2){
                throw new WrongUsageException();
            }

            if(args.size()==2){

                //switch is more expandable than if
                switch (args.get(1).toUpperCase()){
                    case "-A":
                        for(Iterator<Material> it = system.activeMaterialIterator(); it.hasNext();){
                            System.out.println(it.next().getName());
                        }
                        break;
                    default:
                        throw new NonexistentArgumentException(args.get(1));
                }
            }else{
                System.out.println(MATERIAL);
                for(Iterator<Material> it = system.materialIterator(); it.hasNext();){
                    System.out.println(it.next().getName());
                }
            }


        }catch (WrongUsageException e){
            System.out.println(NO_ARG);
        } catch (NonexistentArgumentException e) {
            System.out.printf(FALSE_ARG,e.getMessage());
        }

    }

    /**
     * The menu when opening a material
     * @param studySystem the study system object
     * @param in the Scanner object
     * @param material the material name
     */
    private static void materialMenu(@NotNull StudySystem studySystem,String material, Scanner in){
        Commands command;
        do {
            System.out.print(material+ARROW);
            List<String> args = getArgs(in.nextLine());
            command = getCommand(args.get(0).toUpperCase());
            switch (command){
                case ADD_FILE -> addFile(studySystem,material,args);
                case ADD_ONLINE_DOC -> addOnlineDoc(studySystem,material,args);
                case ADD_VIDEO -> addVideo(studySystem,material,args);
                case REMOVE_FILE -> removeFile(studySystem,material,args);
                case REMOVE_ONLINE_DOC -> removeOnlineDoc(studySystem,material,args);
                case REMOVE_VIDEO -> removeVideo(studySystem,material,args);
                case LIST_MATERIALS -> printContent(studySystem,material,args);
                case LIST -> printMaterials(studySystem,args);
                case START -> startMaterial(studySystem,material,args);
                case KILL -> kill(studySystem,material,args);
                case HELP -> printHelp(args);
                case CLOSE -> closeMaterial(material,args);
                default -> System.out.println(DEFAULT);
            }
            save(studySystem);
            System.out.println();




        }while (!command.equals(Commands.CLOSE));
    }

    /**
     * Prints a help message
     */
    private static void printHelp(List<String> args){
        try{
            if(args.size()!=1){
                throw new WrongUsageException();
            }
            for(Commands cm : Commands.values()){

                if(!cm.command.equals(Commands.UNKNOWN.command)){
                    System.out.printf(HELP,cm.command,cm.description);
                }

            }
        }catch (WrongUsageException e){
            System.out.println(NO_ARG);
        }

    }


    /**
     * Removes the file from the material
     * @param studySystem the system object
     * @param material name of the material
     * @param args List of the arguments
     */
    private static void removeFile(StudySystem studySystem, String material, List<String> args) {

        try {
            if(args.size()!=2){
                throw new WrongUsageException();
            }
            String name = args.get(1);
            studySystem.removeFile(material,name);
        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
        } catch (FileDoesNotExistException e) {
            System.out.printf((FILE_NOT_EXIST),e.getMessage());
        }catch (WrongUsageException e){
            Commands cm = Commands.REMOVE_FILE;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }

    /**
     * Removes the online document from the material
     * @param studySystem the system object
     * @param material name of the material
     * @param args List of the arguments
     */
    private static void removeOnlineDoc(StudySystem studySystem, String material, List<String> args) {

        try {
            if(args.size()!=2){
                throw new WrongUsageException();
            }
            String name = args.get(1);
            studySystem.removeOnlineDoc(material,name);
        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
        } catch (OnlineDocDoesNotExistException e) {
            System.out.printf((ONLINE_DOC_NOT_EXIST),e.getMessage());
        }catch (WrongUsageException e){
            Commands cm = Commands.REMOVE_ONLINE_DOC;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }

    /**
     * Removes the video from the material
     * @param studySystem the system object
     * @param material name of the material
     * @param args list of the arguments
     */
    private static void removeVideo(StudySystem studySystem, String material, List<String> args) {

        try {

            if(args.size()!=2){
                throw new WrongUsageException();
            }
            String name = args.get(1);
            studySystem.removeVideo(material,name);

        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
        } catch (VideoDoesNotExistException e) {
            System.out.printf((VIDEO_NOT_EXIST),e.getMessage());
        }catch (WrongUsageException e){
            Commands cm = Commands.REMOVE_VIDEO;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }


    /**
     * Prints the content of a the <code>material</code>
     *
     * @param studySystem the system object
     * @param material    the material name
     * @param args        list of the arguments
     */
    private static void printContent(StudySystem studySystem, String material, List<String> args){

        try {
            if(args.size()!=1){
                throw new WrongUsageException();
            }
            if(studySystem.hasFiles(material)){
                System.out.println(FILES_PRINT);
                for(Iterator<FileDetails> it = studySystem.fileIterator(material);it.hasNext();){
                    FileDetails f = it.next();
                    System.out.printf((FILE_FORMAT),f.getName(),f.getType());
                }
                System.out.println();
            }

            if(studySystem.hasVideos(material)){
                System.out.println(VIDEO_PRINT);
                for(Iterator<Site> it = studySystem.videoIterator(material);it.hasNext();){
                    Site s = it.next();
                    System.out.printf((VIDEO_FORMAT),s.getName(),s.getDomain());
                }
                System.out.println();
            }

            if(studySystem.hasOnlineDocs(material)){
                System.out.println(ONLINE_DOC_PRINT);
                for (Iterator<Site> it =studySystem.onlineDocIterator(material);it.hasNext();){
                    Site s = it.next();
                    System.out.printf((ONLINE_DOC_FORMAT),s.getName(),s.getDomain());
                }
                System.out.println();
            }


        } catch (MaterialDoesNotExistException e) {

            System.out.printf((MATERIAL_NOT_EXIST),material);
            //throw new RuntimeException(e);
        }catch (WrongUsageException e){
            System.out.println(NO_ARG);
        }

    }

    /**
     * Closes the material and return to the main menu
     */
    private static void closeMaterial(String material, List<String> args) {
        try {
            if(args.size()!=1){
                throw new WrongUsageException();
            }
            System.out.printf((CLOSE),material);
        } catch (WrongUsageException e) {
            System.out.println(NO_ARG);
        }

    }

    /**
     * Starts the study of that <code>material</code>
     * Opens the files, the videos and the online documents
     *
     * @param studySystem the system object
     * @param material    the material name
     * @param args        list of the arguments
     */
    private static void startMaterial(StudySystem studySystem, String material, List<String> args) {
        try {
            if(args.size()!=1){
                throw new WrongUsageException();
            }
            studySystem.startStudy(material);
        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
        } catch (ErrorOpeningMaterialsException e) {
            System.out.println(ERROR_OPENING);
        }catch (WrongUsageException e) {
            System.out.println(NO_ARG);
        } catch (AlreadyOpenedException e){
            System.out.printf(ALREADY_STARTED,material);
        }
    }

    /**
     * Starts the study of a given material
     * Opens the files, the videos and the online documents
     *
     * @param studySystem the system object
     * @param args        list of the arguments
     */
    private static void startMaterial(StudySystem studySystem, List<String> args){
        String name = args.get(1);
        args.remove(1); // the number of arguments needs to be one, otherwise will raise an exception
        startMaterial(studySystem,name,args);
    }

    /**
     * Stops the study of that <code>material</code>
     * @param studySystem the system object
     * @param material the name of the material
     * @param args list of the arguments
     */
    private static void kill(StudySystem studySystem, String material, List<String> args){
        try{
            if(args.size()!=1){
                throw new WrongUsageException();
            }
            studySystem.closeStudy(material);

        } catch (WrongUsageException e) {
            System.out.println(NO_ARG);
        } catch (ClosedMaterialException e) {
            System.out.printf(CLOSED_MATERIAL,material);
        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
        }
    }

    /**
     * Stops the study of a given material
     * @param studySystem the system object
     * @param args  list of the arguments
     */
    private static void kill(StudySystem studySystem, List<String> args){
        String name = args.get(1);
        args.remove(1); // the number of arguments needs to be one, otherwise will raise an exception
        kill(studySystem,name,args);
    }

    /**
     * Adds a video to the <code>material</code>
     * @param studySystem the system object
     * @param material the material name
     * @param args a list with all the arguments
     */
    private static void addVideo(StudySystem studySystem, String material, List<String> args) {


        try {
            if(args.size()!=3){
                throw new WrongUsageException();
            }
            String name = args.get(1);
            String URL = args.get(2);
            studySystem.addVideo(material,name,URL);
        } catch (VideoAlreadyExistException e) {
            System.out.println(VIDEO_EXIST);
        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
        }catch (WrongUsageException e) {
            Commands cm = Commands.ADD_VIDEO;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }

    /**
     * Adds an online document to the <code>material</code>
     * @param studySystem the system object
     * @param material the material name
     * @param args a List with all the arguments
     */
    private static void addOnlineDoc(StudySystem studySystem, String material, List<String> args) {



        try {
            if(args.size()!=3){
                throw new WrongUsageException();
            }
            String name = args.get(1);
            String URL = args.get(2);
            studySystem.addOnlineDocument(material,name,URL);
        } catch (OnlineDocumentAlreadyExistException e) {
            System.out.println(ONLINE_DOC_EXIST);
            //throw new RuntimeException(e);
        } catch (MaterialDoesNotExistException e) {
            System.out.println(MATERIAL_NOT_EXIST);
            //throw new RuntimeException(e);
        }catch (WrongUsageException e) {
            Commands cm = Commands.ADD_ONLINE_DOC;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }

    /**
     * Adds a file to the material
     * @param studySystem the system object
     * @param material the material name
     * @param args a List with all the arguments
     */
    private static void addFile(StudySystem studySystem, String material, List<String> args) {

        try {
            if(args.size()<2||args.size()>3){
                throw new WrongUsageException();
            }
            if(args.size()==3){
                String fileName = args.get(1);
                String path = args.get(2);
                try {
                    studySystem.addFile(material,fileName,path);
                } catch (FileAlreadyExistException e) {
                    System.out.println(FILE_EXIST);
                    //throw new RuntimeException(e);
                } catch (InvalidFileTypeException e) {
                    System.out.println(TYPE_INVALID);
                    //throw new RuntimeException(e);
                } catch (MaterialDoesNotExistException e) {
                    System.out.println(MATERIAL_NOT_EXIST);
                    //throw new RuntimeException(e);
                }
            } else {

                String path = args.get(1);
                try {
                    studySystem.addFile(material,path);
                } catch (FileAlreadyExistException e) {
                    System.out.println(FILE_EXIST);
                    //throw new RuntimeException(e);
                } catch (InvalidFileTypeException e) {
                    System.out.println(TYPE_INVALID);
                    //throw new RuntimeException(e);
                } catch (MaterialDoesNotExistException e) {
                    System.out.println(MATERIAL_NOT_EXIST);
                    //throw new RuntimeException(e);
                }

            }
        }catch (WrongUsageException e){
            Commands cm = Commands.ADD_FILE;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }


    }

    private static void exit(List<String> args) {
        try{
            if(args.size()!=1){
                throw new WrongUsageException();
            }
            System.out.println(EXIT_MESSAGE);

        }catch (WrongUsageException e){
            System.out.println(NO_ARG);
        }

    }


    /**
     * Opens the material
     * @param studySystem the system object
     * @param args the list with arguments
     * @param in the scanner object
     */
    private static void openMaterial(StudySystem studySystem, List<String> args,Scanner in) {
        String material = args.get(1);
        try{
            if(args.size()!=2){
                throw new WrongUsageException();
            }

            if(!studySystem.hasMaterial(material)){
                throw new MaterialDoesNotExistException();
            }
            materialMenu(studySystem,material,in);
        }catch (WrongUsageException e){
            Commands cm = Commands.OPEN;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }catch (MaterialDoesNotExistException e){
            System.out.printf(MATERIAL_NOT_EXIST, material);
        }

    }

    /**
     * Removes the given material
     * @param studySystem study system
     * @param args a List with all the arguments
     */
    private static void removeMaterial(@NotNull StudySystem studySystem, List<String> args) {
        String name = args.get(1);
        try {
            if(args.size()!=2){
                throw new WrongUsageException();
            }
            studySystem.removeMaterial(name);
        } catch (MaterialDoesNotExistException e) {

            System.out.println(MATERIAL_NOT_EXIST);
            //throw new RuntimeException(e);
        }catch (WrongUsageException e){
            Commands cm = Commands.REMOVE_MATERIAL;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }


    /**
     * Adds a new material to the system
     * @param studySystem the study system object
     * @param args a List with all the arguments
     */
    private static void addMaterial(@NotNull StudySystem studySystem, List<String> args){
        String name = args.get(1);
        try {
            if(args.size()!=2){
                throw new WrongUsageException();
            }
            studySystem.addMaterial(name);
        } catch (MaterialAlreadyExistsException e) {
            //throw new RuntimeException(e);
            System.out.printf((MATERIAL_EXIST),name);
        }catch (WrongUsageException e){
            Commands cm = Commands.ADD_MATERIAL;
            System.out.printf(USAGE+cm.usage+"\n",cm.command);
        }
    }



    /**
     * Gets the input command and converts it to a Commands object, so it can be used in the switch.
     *
     * @param inString input string
     * @return Commands object.
     */
    private static Commands getCommand(@NotNull String inString) {
        for (Commands c : Commands.values()) {
            if (c.getCommand().equals(inString)) {
                return c;
            }
        }
        return Commands.UNKNOWN;
    }


    /**
     * De-Serializes a study system object from the <code>SYSTEM_FILE</code>, if it exists, if it doesn't, returns
     * a new study system object.
     *
     * @return returns de-serialized file if possible, otherwise returns a new study system object
     */
    private static StudySystem load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SYSTEM_FILE));
            StudySystem s = (StudySystem) ois.readObject();
            ois.close();
            return s;
        }
        // First execution(IOException): the serialization file isn´t created yet
        // Unusual situation(ClassNotFoundException): There are unserializable classes/interfaces in the object.
        catch (IOException | ClassNotFoundException e) {
            return new StudySystemClass();
        }
    }

    /**
     * Serializes the <code>s</code> object to the <code>SYSTEM_FILE</code>
     *
     * @param s object we want to serialize to the file
     */
    private static void save(StudySystem s) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SYSTEM_FILE));
            oos.writeObject(s);
            oos.flush();
            oos.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Transforms the line inserted into a list of arguments, separated by spaces
     * @param line the line inserted by the user
     * @return a list of strings ,each string is a word of the line
     */
    private static List<String> getArgs(String line){
        return new ArrayList<>(List.of(line.split(SPACE)));
    }



}