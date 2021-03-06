package se.ads.actions;

import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;
import se.ads.ApplicationContext;

public class OnClickAction implements org.w3c.dom.events.EventListener {

    ApplicationContext ctx;

    public OnClickAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    @Override
    public void handleEvent(Event evt) {
        SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
        ctx.setSelectedItem(null);

        DOMMouseEvent elEvt = (DOMMouseEvent) evt;
        int nowToX = elEvt.getClientX();
        int nowToY = elEvt.getClientY();

        SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
        SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
        mat = mat.inverse();                  // screen -> elem
        ctx.setInitialDragPoint((SVGOMPoint)pt.matrixTransform(mat));

        if (ctx.getCurrentElementType() != null) {
            Element e = ctx.getCurrentElementType().placeNew(nowToX, nowToY);
            if (e != null) {
                ctx.attachEventHandlers(e);
                Element elt = ctx.getDoc().getElementById("objects");
                elt.appendChild(e);
                ctx.setSelectedItem(e);

            }
        }
    }
}
