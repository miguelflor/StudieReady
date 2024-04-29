package desk;

import java.io.Serial;

class SiteClass implements Site {

    //Serial Version UID of the Class
    @Serial
    private static final long serialVersionUID = 0L;
    private String name;
    private String URL;
    private final SitesType type;

    public SiteClass(String name, String URL, SitesType type){
        this.name = name;
        this.URL = URL;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public String getDomain(){return URL.replaceAll("http(s)?://|www\\.|/.*", "");}

    @Override
    public SitesType getType() {
        return type;
    }

    /**
     * Changes the name of
     * @param name name of the site
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * The URL of the site
     * Changes also the domain accordingly
     * @param URL the URL of the website
     */
    protected void setURL(String URL) {
        this.URL = URL;
    }

}
