package sample;

public class WikiBooks {
    private String URL = null;


    public WikiBooks(String url){
        setURL(url);
    }
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = "https://de.wikibooks.org/wiki/"+URL;

    }


}
