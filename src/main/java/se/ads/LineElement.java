package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

/**
 * Created by ansi on 2017-05-15.
 */
public class LineElement extends BaseDrawElement implements DrawElement{
    private Integer firstPlacementX;
    private Integer firstPlacementY;
    private Float xdistance, ydistance, x2distance, y2distance;

    public LineElement(ApplicationContext ctx) {
        super(ctx.getDoc(), ctx);
    }
    public Element create(){
        Element element = doc.createElementNS(SVGApplication.SVG_NS, "line");
        element.setAttribute("x1", "0");
        element.setAttribute("y1", "0");
        element.setAttribute("x2", "0");
        element.setAttribute("y2", "0");
        element.setAttribute("style", "stroke:rgb(255,0,0);stroke-width:2");

        return element;
    }

    public Element move(SVGOMPoint dragpt, Element element) {
        if (xdistance == null){
            xdistance = xClickDistanse(element, dragpt);
            ydistance = yClickDistanse(element, dragpt);
            x2distance = x2ClickDistanse(element, dragpt);
            y2distance = y2ClickDistanse(element, dragpt);
        }
        logger.info("clicked x:{}, xclickdistance:{}, xRelative:{} x1:{} x2:{}", dragpt.getX(), xdistance, xRelativeToMouse(dragpt), floatValue(element, "x1"), floatValue(element, "x2"));
        element.setAttribute("x1", ""+xRelativeToMouse(dragpt));
        element.setAttribute("y1", ""+yRelativeToMouse(dragpt));
        element.setAttribute("x2", ""+x2RelativeToMouse(dragpt));
        element.setAttribute("y2", ""+y2RelativeToMouse(dragpt));
        return element;
    }

    private Float xClickDistanse(Element element, SVGOMPoint dragpt){
        return dragpt.getX() - floatValue(element, "x1");
    }
    private Float yClickDistanse(Element element, SVGOMPoint dragpt){
        return dragpt.getY() - floatValue(element, "y1");
    }

    private Float x2ClickDistanse(Element element, SVGOMPoint dragpt){
        return floatValue(element, "x2") - dragpt.getX();
    }
    private Float y2ClickDistanse(Element element, SVGOMPoint dragpt){
        return floatValue(element, "y2") - dragpt.getY();
    }

    private Float floatValue(Element element, String attr){
        return Float.parseFloat(element.getAttribute(attr));
    }

    private Float xRelativeToMouse(SVGOMPoint dragpt){
        return dragpt.getX() - xdistance;
    }

    private Float yRelativeToMouse(SVGOMPoint dragpt){
        return dragpt.getY() - ydistance;
    }

    private Float x2RelativeToMouse(SVGOMPoint dragpt){
        return x2distance + dragpt.getX();
    }

    private Float y2RelativeToMouse(SVGOMPoint dragpt){
        return y2distance + dragpt.getY();
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
