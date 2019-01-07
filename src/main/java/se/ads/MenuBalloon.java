package se.ads;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

public class MenuBalloon {
    private Document doc;
    private ApplicationContext ctx;
    private String styleType = "stroke";
    private String opacity = "0.1";
    private String color = "red";
    private String fillColor = "black";
    public Element create(Document doc){
        Element element = doc.createElementNS(SVGApplication.SVG_NS, "rect");
        element.setAttribute("x", "10");
        element.setAttribute("y", "20");
        element.setAttribute("width", "100");
        element.setAttribute("height", "50");
        element.setAttribute("style", styleType+":"+color);
        element.setAttribute("fill-opacity", opacity);
        element.setAttribute("fill", fillColor);

        EventTarget target = (EventTarget) element;
        target.addEventListener("mousedown", new OnDownAction(ctx), false);
        target.addEventListener("mousemove", new OnMoveAction(ctx), false);
        target.addEventListener("mouseup", new OnUpAction(ctx), false);
        target.addEventListener("mouseout", evt -> {
            //
        }, false);

        return element;
    }
}
