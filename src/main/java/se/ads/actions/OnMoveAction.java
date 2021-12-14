package se.ads.actions;

import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;
import se.ads.ApplicationContext;

public class OnMoveAction implements EventListener {
    Logger logger = LogManager.getLogger(OnMoveAction.class);

    ApplicationContext ctx;

    public OnMoveAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    @Override
    public void handleEvent(Event evt) {
        logger.info("handle event");
        if (ctx.getDrag() == ApplicationContext.DRAG_DOWN){
            logger.info("drag down pressed");
            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = ((SVGLocatable)evt.getTarget()).getScreenCTM();

            mat = mat.inverse();
            SVGOMPoint dragpt = (SVGOMPoint)pt.matrixTransform(mat);

            Element element = (Element) evt.getTarget();

            ctx.getCurrentElementType().move(dragpt, element);

        }
    }

}
