package se.ads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseDrawElement {
    Logger logger = LogManager.getLogger(BaseDrawElement.class);

    protected Document doc;
    protected ApplicationContext ctx;

     String styleType = "stroke";
     String opacity = "0.1";
     String rgbcolor = "rgb(150,15,150)";
     String fillColor = "black";
     String strokeWidthDefault = "1";

    public BaseDrawElement(Document doc, ApplicationContext ctx) {
        this.doc = doc;
        this.ctx = ctx;
    }

    public void highlight() {
        if (ctx.getCurrentlyHighlighted() != null) {
            reset();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(styleType).append(":").append(parseColor(ctx.getSelectedItem()));
        sb.append(";").append("stroke-width:").append(6);
        ctx.getSelectedItem().setAttribute("style", sb.toString());
    }

    public void reset() {
        StringBuilder sb = new StringBuilder();
        sb.append(styleType).append(":").append(parseColor(ctx.getCurrentlyHighlighted()));
        sb.append(";").append("stroke-width:").append(strokeWidthDefault);
        ctx.getCurrentlyHighlighted().setAttribute("style", sb.toString());
    }

    public void changeColor(Color color) {
        StringBuilder sb = new StringBuilder();
        Integer strokeWidth = parseStrokeWidth(ctx.getSelectedItem());

        sb.append(styleType).append(":").append("rgb(").append(color.getRed()).append(",").append(color.getGreen()).append(",").append(color.getBlue())
                .append(")");
        if (strokeWidth != null)
            sb.append(";").append("stroke-width:").append(strokeWidth);
        ctx.getSelectedItem().setAttribute("style", sb.toString());
    }

    String parseColor(Element element){
        String color = null;
        Pattern pattern = Pattern.compile(".*(rgb\\(\\d\\d*\\d*,\\d\\d*\\d*,\\d\\d*\\d*\\)).*");
        Matcher matcher = pattern.matcher(element.getAttribute("style"));

        if(matcher.find()) {
            color = matcher.group(1);
        }
        if (color == null){
            logger.warn("color null from {}", element.getAttribute("style"));
        }
        return color;
    }

    Integer parseStrokeWidth(Element element){
        Integer width = null;
        Pattern pattern = Pattern.compile(".*stroke-width:(\\d\\.*\\d*)");
        Matcher matcher = pattern.matcher(element.getAttribute("style"));

        if(matcher.find()) {
            width = Integer.valueOf(matcher.group(1));
        }
        return width;
    }
}
