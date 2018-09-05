package se.ads;

import org.w3c.dom.Element;

/**
 * Created by ansi on 2017-05-15.
 */
public interface DrawElement {
    public Element create();
    public Element move(int x, int y, Element element);
    public Element placeNew(int x, int y);
}
