package se.ads;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class OnUpAction implements EventListener {
    private ApplicationContext ctx;

    public OnUpAction(ApplicationContext applicationContext){
        this.ctx = applicationContext;
    }

    public void handleEvent(Event evt) {
        ctx.setDrag(ApplicationContext.DRAG_UP);

    }
}