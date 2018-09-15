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
    private String styleType = "stroke";
    private String opacity = "0.1";
    private String color = "red";
    private String fillColor = "black";

    public DrawRectElement(Document doc, ApplicationContext applicationContext){
        this.doc = doc;
        this.ctx = applicationContext;
    }
    public Element create(){
        this.element = doc.createElementNS(SVGApplication.SVG_NS, "rect");
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

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getOpacity() {
        return opacity;
    }

    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }
}
