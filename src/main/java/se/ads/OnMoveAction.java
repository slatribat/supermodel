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
            float newX = dragpt.getX();
            if (dragpt.getX() < ctx.getInitialDragPoint().getX()){
                newX = newX - 10;
                System.out.println("left");
            }
            System.out.println("initialX:" + ctx.getInitialDragPoint().getX());
            System.out.println("x:" + element.getAttribute("x") + " y:" + element.getAttribute("y") + " -> " + dragpt.getX() + " y:"+dragpt.getY());
            element.setAttribute("x", ""+ newX);
        }
    }
}