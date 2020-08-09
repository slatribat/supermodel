package se.ads;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.util.Map;

public class ApplicationContext {
    Logger logger = LogManager.getLogger(ApplicationContext.class);

    private int drag;
    private Element selectedItem;
    private Map<String,JLabel> uiLabels;
    private SVGOMPoint initialDragPoint;
    private Document doc;
    private DrawElement currentElementType;
    private RectElement rectElement;
    private LineElement lineElement;
    public static final int DRAG_UP = 0;
    public static final int DRAG_DOWN = 1;
    private int lastX;
    private Element currentlyHighlighted = null;
    private Element currentBalloon = null;
    private Boolean usingBalloon = false;

    public Map<String, JLabel> getUiLabels() {
        return uiLabels;
    }

    public void setUiLabels(Map<String, JLabel> uiLabels) {
        this.uiLabels = uiLabels;
    }

    public int getDrag() {
        return drag;
    }

    public void setDrag(int drag) {
        this.drag = drag;
    }

    public Element getSelectedItem() {
        return selectedItem;
    }

    public String getSelectedItemName(){
        return selectedItem != null ? selectedItem.getNodeName() : "";
    }

    public void setSelectedItem(Element selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem != null) {
            if (selectedItem.getLocalName().matches("rect")) {
                setCurrentElementType(rectElement);
            } else if (selectedItem.getLocalName().matches("line")) {
                setCurrentElementType(lineElement);
            }

            getCurrentElementType().highlight();
            setCurrentlyHighlighted(this.selectedItem);
        }
        uiLabels.get("selectedItemName").setText(getSelectedItemName());
        logger.info("currentType: {}", getSelectedItemName());
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

    public Element getCurrentlyHighlighted() {
        return currentlyHighlighted;
    }

    public void setCurrentlyHighlighted(Element currentlyHighlighted) {
        this.currentlyHighlighted = currentlyHighlighted;
    }

    public Element getCurrentBalloon() {
        return currentBalloon;
    }

    public void setCurrentBalloon(Element currentBalloon) {
        this.currentBalloon = currentBalloon;
    }

    public DrawElement getCurrentElementType() {
        return currentElementType;
    }

    public void setCurrentElementType(DrawElement currentElementType) {
        if (currentElementType instanceof RectElement){
            rectElement = new RectElement( this);
        } else if (currentElementType instanceof LineElement){
            lineElement = new LineElement( this);
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
