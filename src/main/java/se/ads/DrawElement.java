package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

import java.awt.*;

/**
 * Created by ansi on 2017-05-15.
 */
public interface DrawElement{
    Element create();
    Element move(SVGOMPoint dragpt, Element element);
    Element placeNew(int x, int y);
    void changeColor( Color color);
    void highlight();
    void reset();
}
