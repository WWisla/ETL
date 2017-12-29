package etl;

import org.jsoup.nodes.Element;

/**
 * Created by Daniel K on 2017-12-29.
 */
public class Opinia{
    private Element opinia;

    public Opinia(Element opinia){
        this.opinia = opinia;
    }

    public String outerHtml(){
        return opinia.outerHtml();
    }
}
