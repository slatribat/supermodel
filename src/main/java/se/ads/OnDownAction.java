package se.ads;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.svg.SVGLocatable;

public class OnDownAction implements org.w3c.dom.events.EventListener {

    ApplicationContext ctx;

    public OnDownAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    @Override
    public void handleEvent(Event evt) {
        SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
        ctx.setSelectedItem((Element) evt.getTarget());
        ctx.setDrag(ApplicationContext.DRAG_DOWN);



    }
}