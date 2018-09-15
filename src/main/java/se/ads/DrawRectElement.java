package se.ads;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

/**
 * Created by ansi on 2017-05-15.
 */
public class DrawRectElement implements DrawElement{
    private Document doc;
    private Element element;
    private ApplicationContext ctx;

    public DrawRectElement(Document doc, ApplicationContext applicationContext){
        this.doc = doc;
        this.ctx = applicationContext;
    }
    public Element create(){
        this.element = doc.createElementNS(SVGApplication.SVG_NS, "rect");
        element.setAttributeNS(null, "x", "10");
        element.setAttributeNS(null, "y", "20");
        element.setAttributeNS(null, "width", "100");
        element.setAttributeNS(null, "height", "50");
        element.setAttributeNS(null, "style", "fill:red");

        EventTarget target = (EventTarget) element;
        target.addEventListener("mousedown", new OnDownAction(ctx), false);
        target.addEventListener("mousemove", new OnMoveAction(ctx), false);
        target.addEventListener("mouseup", new OnUpAction(ctx), false);
        target.addEventListener("mouseout", evt -> {
            System.out.println("mouseout listener");
        }, false);

        return element;
    }

    public Element move(int x, int y, Element element) {
        this.element.setAttribute("x", String.valueOf(x));
        this.element.setAttribute("y", String.valueOf(y));
        return this.element;
    }

    @Override
    public Element placeNew(int x, int y) {
        create();
        element.setAttribute("x", String.valueOf(x));
        element.setAttribute("y", String.valueOf(y));
        return element;
    }

}
