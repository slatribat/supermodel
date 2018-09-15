package se.ads;

import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;

public class OnMoveAction implements EventListener {
    ApplicationContext ctx;

    public OnMoveAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    @Override
    public void handleEvent(Event evt) {
        if (ctx.getDrag() == ApplicationContext.DRAG_DOWN){
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = ((SVGLocatable)evt.getTarget()).getScreenCTM();

            mat = mat.inverse();
            SVGOMPoint dragpt = (SVGOMPoint)pt.matrixTransform(mat);

            Element element = (Element) evt.getTarget();

            element.setAttribute("x", ""+ getMiddleX(element, dragpt));
            element.setAttribute("y", ""+ getMiddleY(element, dragpt));
        }
    }
    private float getMiddleX(Element element, SVGOMPoint dragpt){
        if (element.getLocalName().matches("rect")) {
            return dragpt.getX() - Float.valueOf(element.getAttribute("width")) / 2;
        }
        return dragpt.getX() - Float.valueOf(element.getAttribute("width")) / 2;

    }
    private float getMiddleY(Element element, SVGOMPoint dragpt){
        return dragpt.getY() - Float.valueOf(element.getAttribute("height")) / 2;
    }
}