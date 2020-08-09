package se.ads;

import org.w3c.dom.Document;

public class BaseDrawElement {
    protected Document doc;
    protected ApplicationContext ctx;


    public BaseDrawElement(Document doc, ApplicationContext ctx) {
        this.doc = doc;
        this.ctx = ctx;
    }

    public void highlight() {
        if (ctx.getCurrentlyHighlighted() != null) {
            reset();
        }
        ctx.getSelectedItem().setAttribute("style", "stroke:rgb(255,3,0);stroke-width:6");
    }

    public void reset() {
        ctx.getCurrentlyHighlighted().setAttribute("style", "stroke:rgb(255,0,0);stroke-width:1");
    }
}
