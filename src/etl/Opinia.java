package etl;

import org.jsoup.nodes.Element;

/**
 * Created by Daniel K on 2017-12-29.
 */
public class Opinia{
    //contain full review html code
    private Element review;

    public Opinia(Element review){
        this.review = review;
    }

    public String outerHtml(){
        return review.outerHtml();
    }
}
