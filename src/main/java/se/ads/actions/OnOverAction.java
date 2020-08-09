package se.ads.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGRect;
import se.ads.ApplicationContext;

public class OnOverAction implements EventListener {
    Logger logger = LogManager.getLogger(OnOverAction.class);

    private ApplicationContext ctx;

    public OnOverAction(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void handleEvent(Event evt) {
        SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
        SVGRect svgRect = thisNode.getBBox();
        Element element = (Element) evt.getCurrentTarget();

        if (!element.getAttribute("id").equals("balloon")) {
            logger.info("over node with x:({}),y({}}", svgRect.getX(), svgRect.getY());

        }
    }
}
