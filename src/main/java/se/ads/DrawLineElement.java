package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

/**
 * Created by ansi on 2017-05-15.
 */
public class DrawLineElement extends BaseDrawElement implements DrawElement{
    private Document doc;
    private ApplicationContext ctx;
    private Integer firstPlacementX;
    private Integer firstPlacementY;

    DrawLineElement(Document doc, ApplicationContext applicationContext){
        this.doc = doc;
        this.ctx = applicationContext;
    }
    public Element create(){
        Element element = doc.createElementNS(SVGApplication.SVG_NS, "line");
        element.setAttribute("x1", "0");
        element.setAttribute("y1", "0");
        element.setAttribute("x2", "0");
        element.setAttribute("y2", "0");
        element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");

        EventTarget target = (EventTarget) element;
        target.addEventListener("mousedown", new OnDownAction(ctx), false);
        target.addEventListener("mousemove", new OnMoveAction(ctx), false);
        target.addEventListener("mouseup", new OnUpAction(ctx), false);
        target.addEventListener("mouseout", evt -> {
            //
        }, false);

        return element;
    }

    public Element move(SVGOMPoint dragpt, Element element) {
        element.setAttribute("x1", String.valueOf(dragpt.getX()));
        element.setAttribute("y1", String.valueOf(dragpt.getY()));
        element.setAttribute("x2", String.valueOf(dragpt.getX()+10));
        element.setAttribute("y2", String.valueOf(dragpt.getY()+10));
        element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");
        return element;
    }

    @Override
    public Element placeNew(int x, int y) {
        Element element = null;
        if (firstPlacementX != null && firstPlacementY != null) {
            element = create();
            element.setAttribute("x1", String.valueOf(firstPlacementX));
            element.setAttribute("y1", String.valueOf(firstPlacementY));
            element.setAttribute("x2", String.valueOf(x));
            element.setAttribute("y2", String.valueOf(y));
            element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");
            firstPlacementX = null;
            firstPlacementY = null;
        } else {
            firstPlacementX = x;
            firstPlacementY = y;
        }
        return element;
    }

}
