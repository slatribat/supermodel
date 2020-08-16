package se.ads;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.util.SVGConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;
import se.ads.actions.OnClickAction;
import se.ads.actions.OnUpAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ansi on 2017-05-14.
 */
public class SVGApplication {
    Logger logger = LogManager.getLogger(SVGApplication.class);
    static final String SVG_NS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    protected JFrame frame;
    protected JSVGCanvas svgCanvas = new JSVGCanvas();
    protected JLabel label = new JLabel();
    protected UpdateManager updateManager;
    protected Document doc;
    protected int x = 0, y = 0;
    ApplicationContext ctx = new ApplicationContext();

    public static void main(String[] args) {
        JFrame f = new JFrame("SuperModel");
        SVGApplication app = new SVGApplication(f);

        // Add components to the frame.
        f.getContentPane().add(app.createComponents());

        // Display the frame.
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(400, 400);
        f.pack();
        f.setVisible(true);
    }


    public SVGApplication(JFrame f) {
        frame = f;
    }

    public JComponent createComponents() {
        // Create a panel and add the button, status label and the SVG canvas.
        final JPanel panel = new JPanel(new BorderLayout());

        JButton buttonNewClass = getButtonNewClass();
        JButton buttonNewLine = getButtonNewLine();
        JButton buttonLoad = getButtonLoad(panel);
        ColorChooserButton colorChooser = new ColorChooserButton(Color.WHITE);
        colorChooser.addColorChangedListener(newColor -> {
            logger.info("color chosen{}",newColor);
            BaseDrawElement elementType = new BaseDrawElement(doc, ctx);
            elementType.changeColor( newColor);
        });

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(buttonLoad);
        p.add(buttonNewClass);
        p.add(buttonNewLine);
        p.add(colorChooser);
        p.add(label);

        JPanel statusBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selectedItemName = new JLabel(ctx.getSelectedItemName());
        statusBarPanel.add(selectedItemName);
        Map<String,JLabel> uiTextsMap = new HashMap<>();
        uiTextsMap.put("selectedItemName", selectedItemName);
        ctx.setUiLabels(uiTextsMap);

        panel.add("North", p);
        panel.add("Center", svgCanvas);
        panel.add("South", statusBarPanel);

        svgRendering();

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                calculateSize((SVGDocument)doc);
            }
        });

        return panel;
    }

    private JButton getButtonNewLine() {
        JButton buttonNewLine = new JButton("line");
        buttonNewLine.addActionListener(ae ->
                ctx.setCurrentElementType(new LineElement( ctx)));
        return buttonNewLine;
    }

    private JButton getButtonNewClass() {
        JButton buttonNewClass = new JButton("class");
        buttonNewClass.addActionListener(ae ->
                ctx.setCurrentElementType(new RectElement( ctx)));
        return buttonNewClass;
    }

    private JButton getButtonLoad(JPanel panel) {
        JButton buttonLoad = new JButton("Load...");
        buttonLoad.addActionListener(ae -> {
            JFileChooser fc = new JFileChooser(".");
            int choice = fc.showOpenDialog(panel);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    svgCanvas.setURI(f.toURL().toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return buttonLoad;
    }

    private void svgRendering() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        doc = impl.createDocument(SVG_NS, "svg", null);
        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        svgCanvas.setDocument(doc);
        ctx.setDoc(doc);
        Element svgRoot = doc.getDocumentElement();

        // Set the JSVGCanvas listeners.
        svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                label.setText("Document Loading...");
            }

            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                label.setText("Document Loaded.");
            }
        });

        svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildStarted(GVTTreeBuilderEvent e) {
                label.setText("Build Started...");
            }

            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                label.setText("Build Done.");
                frame.pack();
            }
        });

        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
                label.setText("Rendering Started...");
            }

            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                label.setText("");
                updateManager = svgCanvas.getUpdateManager();

                // create the array of rectangles
                String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
                Element g = doc.createElementNS(svgNS, "g");
                g.setAttributeNS(null, "id", "objects");

                addGlassPane();

                Element elt = svgCanvas.getSVGDocument().getElementById("glasspane");
                EventTarget target = (EventTarget) elt;

                /*target.addEventListener("mousedown", new OnDownAction(ctx), false);
                 target.addEventListener("mousemove", new OnMoveAction(ctx), false);*/
                target.addEventListener("mouseup", new OnUpAction(ctx), false);
                /*target.addEventListener("mouseout", new OnUpAction(ctx), false);*/
                target.addEventListener("click", new OnClickAction(ctx), false);

                if (ctx.getSelectedItem() == null) {
                    svgRoot.appendChild(g);
                }

                Point2D p = getCanvasCoordinate(new Point2D.Float(x, y));

            }
        });
    }

    private void calculateSize(SVGDocument doc) {
        Dimension d = frame.getSize();

        doc.getRootElement().setAttributeNS(null,
                SVGConstants.SVG_WIDTH_ATTRIBUTE, d.getWidth() + "");
        doc.getRootElement().setAttributeNS(null,
                SVGConstants.SVG_HEIGHT_ATTRIBUTE, d.getHeight() + "");
    }

    private Point2D getCanvasCoordinate(Point2D p) {
        Point2D pt = p;
        try {
            AffineTransform bt = svgCanvas.getRenderingTransform().createInverse();
            return bt.transform(p, null);
        } catch (NoninvertibleTransformException ex) {
            return pt;
        }
    }

    public void addGlassPane() {
        Document doc = svgCanvas.getSVGDocument();
        Element rectangle = doc.createElementNS(SVG_NS, "rect");
        rectangle.setAttributeNS(null, "x", "0");
        rectangle.setAttributeNS(null, "y", "0");
        rectangle.setAttributeNS(null, "width", "600");
        rectangle.setAttributeNS(null, "height", "600");
        rectangle.setAttributeNS(null, "style", "fill:none;pointer-events:fill");
        rectangle.setAttributeNS(null, "id", "glasspane");
        Element svgRoot = doc.getDocumentElement();

        svgRoot.insertBefore(rectangle, doc.getElementById("objects"));

    }
}
