package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import se.ads.actions.OnDownAction;
import se.ads.actions.OnMoveAction;
import se.ads.actions.OnOverAction;
import se.ads.actions.OnUpAction;

/* Meny knuten till ett element
* */
public class MenuBalloonElement extends BaseDrawElement implements DrawElement{
    private Document doc;
    private ApplicationContext ctx;
    private String styleType = "stroke";
    private String opacity = "0.1";
    private String color = "green";
    private String fillColor = "black";

    public MenuBalloonElement(ApplicationContext ctx) {
        super(ctx.getDoc(), ctx);
    }

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

    @Override
    public Element create() {
        Element element = doc.createElementNS(SVGApplication.SVG_NS, "rect");
        element.setAttribute("x", "10");
        element.setAttribute("y", "20");
        element.setAttribute("width", "12");
        element.setAttribute("height", "12");
        element.setAttribute("style", styleType+":"+color);
        element.setAttribute("fill-opacity", opacity);
        element.setAttribute("fill", fillColor);

        EventTarget target = (EventTarget) element;
        target.addEventListener("mousedown", new OnDownAction(ctx), false);
        target.addEventListener("mousemove", new OnMoveAction(ctx), false);
        target.addEventListener("mouseup", new OnUpAction(ctx), false);
        target.addEventListener("mouseover", new OnOverAction(ctx), false);
        target.addEventListener("mouseout", evt -> {
            //
        }, false);

        return element;
    }

    @Override
    public Element move(SVGOMPoint dragpt, Element element) {
        return null;
    }

    @Override
    public Element placeNew(int x, int y) {
        Element element = create();
        element.setAttribute("x", String.valueOf(x));
        element.setAttribute("y", String.valueOf(y));
        return element;
    }
}
