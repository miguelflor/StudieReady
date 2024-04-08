package desk;

import java.io.Serializable;

public interface Site extends Serializable {

    /**
     * Returns the name of the site
     * @return the name of the site
     */
     String getName();

    /**
     * Returns the URL of the site
     * @return the URL of the site
     */
     String getURL();

    /**
     * Return the domain of the website
     * @return the domain of the website
     */
    String getDomain();

    /**
     * Returns the type of this site
     * @return the type of this site
     */
    SitesType getType();


}
