package se.ads.actions;

import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;
import se.ads.ApplicationContext;
import se.ads.MenuBalloonElement;

public class OnOverAction implements EventListener {
    private ApplicationContext ctx;

    public OnOverAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    public void handleEvent(Event evt) {
        SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
        //ctx.setSelectedItem((Element) evt.getTarget());

        DOMMouseEvent elEvt = (DOMMouseEvent) evt;
        int nowToX = elEvt.getClientX();
        int nowToY = elEvt.getClientY();

        SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
        SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
        mat = mat.inverse();                  // screen -> elem
        ctx.setInitialDragPoint((SVGOMPoint)pt.matrixTransform(mat));

            MenuBalloonElement menu = new MenuBalloonElement(this.ctx);
            Element e = menu.placeNew(10, 10);//TODO get from element under cursors
            //Element e = ctx.getCurrentElementType().placeNew(nowToX, nowToY);
            if (e != null) {
                Element elt = ctx.getDoc().getElementById("objects");
                elt.appendChild(e);
            }

    }
}