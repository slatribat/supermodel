package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

/**
 * Created by ansi on 2017-05-15.
 */
public interface DrawElement{
    public Element create();
    public Element move(SVGOMPoint dragpt, Element element);
    public Element placeNew(int x, int y);
}
