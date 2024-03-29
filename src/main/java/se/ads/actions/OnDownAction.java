package se.ads.actions;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import se.ads.ApplicationContext;

public class OnDownAction implements org.w3c.dom.events.EventListener {

    ApplicationContext ctx;

    public OnDownAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    @Override
    public void handleEvent(Event evt) {
        ctx.setSelectedItem((Element) evt.getTarget());
        ctx.setDrag(ApplicationContext.DRAG_DOWN);
    }
}
