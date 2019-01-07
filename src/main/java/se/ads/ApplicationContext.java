package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplicationContext {
    private int drag;
    private Element selectedItem;
    private SVGOMPoint initialDragPoint;
    private Document doc;
    private DrawElement currentElementType;
    private DrawRectElement drawRectElement;
    private DrawLineElement drawLineElement;
    public static final int DRAG_UP = 0;
    public static final int DRAG_DOWN = 1;
    private int lastX;

    public int getDrag() {
        return drag;
    }

    public void setDrag(int drag) {
        this.drag = drag;
    }

    public Element getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Element selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem.getLocalName().matches("rect")) {
            setCurrentElementType(drawRectElement);
        } else if (selectedItem.getLocalName().matches("line")) {
            setCurrentElementType(drawLineElement);
        }
        System.out.println("currentType:"+currentElementType.toString());
    }

    public SVGOMPoint getInitialDragPoint() {
        return initialDragPoint;
    }

    public void setInitialDragPoint(SVGOMPoint initialDragPoint) {
        this.initialDragPoint = initialDragPoint;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public DrawElement getCurrentElementType() {
        return currentElementType;
    }

    public void setCurrentElementType(DrawElement currentElementType) {
        if (currentElementType instanceof DrawRectElement){
            drawRectElement = new DrawRectElement(doc, this);
        } else if (currentElementType instanceof DrawLineElement){
            drawLineElement = new DrawLineElement(doc, this);
        }
        this.currentElementType = currentElementType;
    }

    public int getLastX() {
        return lastX;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }
}
