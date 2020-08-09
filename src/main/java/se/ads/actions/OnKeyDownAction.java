package se.ads.actions;

import org.apache.batik.dom.events.DOMKeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGLocatable;
import se.ads.ApplicationContext;

public class OnKeyDownAction implements EventListener {
    Logger logger = LogManager.getLogger(OnKeyDownAction.class);

    private ApplicationContext ctx;

    public OnKeyDownAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    public void handleEvent(Event evt) {
        SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
        ctx.setSelectedItem((Element) evt.getTarget());

        DOMKeyEvent elEvt = (DOMKeyEvent) evt;
        if (elEvt.getKeyCode() == 127) {
            logger.info("Removing element {}", evt.getTarget());
            Element elt = ctx.getDoc().getElementById("objects");
            elt.removeChild(ctx.getSelectedItem());
            ctx.setSelectedItem(null);
        }

    }
}
