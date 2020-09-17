package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

public class RectElement extends BaseDrawElement implements DrawElement{


    public RectElement(ApplicationContext ctx) {
        super(ctx.getDoc(), ctx);
    }

    public Element create(){
        Element element = doc.createElementNS(SVGApplication.SVG_NS, "rect");
        element.setAttribute("id", "rect");
        element.setAttribute("x", "10");
        element.setAttribute("y", "20");
        element.setAttribute("width", "100");
        element.setAttribute("height", "50");
        element.setAttribute("style", styleType+":"+ rgbcolor);
        element.setAttribute("fill-opacity", opacity);
        element.setAttribute("fill", fillColor);

        return element;
    }

    public Element move(SVGOMPoint dragpt, Element element) {
        element.setAttribute("x", ""+ getMiddleX(element, dragpt));
        element.setAttribute("y", ""+ getMiddleY(element, dragpt));
        return element;
    }

    private float getMiddleX(Element element, SVGOMPoint dragpt){
        return dragpt.getX() - Float.parseFloat(element.getAttribute("width")) / 2;
    }
    private float getMiddleY(Element element, SVGOMPoint dragpt){
        return dragpt.getY() - Float.parseFloat(element.getAttribute("height")) / 2;
    }

    @Override
    public Element placeNew(int x, int y) {
        Element element = create();
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
        return rgbcolor;
    }

    public void setColor(String color) {
        this.rgbcolor = color;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }
}
