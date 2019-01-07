package se.ads;

import org.w3c.dom.Document;

public class BaseDrawElement {
    protected Document doc;
    protected ApplicationContext ctx;


    public BaseDrawElement(Document doc, ApplicationContext ctx) {
        this.doc = doc;
        this.ctx = ctx;
    }
}
