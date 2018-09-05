package se.ads;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by ansi on 2017-05-15.
 */
public class DrawLineElement implements DrawElement{
    Document doc;
    Element element;

    public DrawLineElement(Document doc){
        super();
        this.doc = doc;

    }
    public Element create(){
        this.element = doc.createElementNS(SVGApplication.SVG_NS, "line");
        element.setAttribute("x1", "0");
        element.setAttribute("y1", "0");
        element.setAttribute("x2", "0");
        element.setAttribute("y2", "0");
        element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");
        return element;
    }

    public Element move(int x, int y, Element element) {
        this.element.setAttribute("x1", String.valueOf(x));
        this.element.setAttribute("y1", String.valueOf(y));
        this.element.setAttribute("x2", String.valueOf(x));
        this.element.setAttribute("y2", String.valueOf(y+10));
        this.element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");
        return this.element;
    }

    @Override
    public Element placeNew(int x, int y) {
        create();
        element.setAttribute("x1", String.valueOf(x));
        element.setAttribute("y1", String.valueOf(y));
        element.setAttribute("x2", String.valueOf(x));
        element.setAttribute("y2", String.valueOf(y+10));
        element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");
        return element;
    }

}
