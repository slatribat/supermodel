package se.ads;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

/**
 * Created by ansi on 2017-05-14.
 */
public class SVGApplication {
    static final String SVG_NS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    protected JFrame frame;
    protected JSVGCanvas svgCanvas = new JSVGCanvas();
    protected JLabel label = new JLabel();
    protected UpdateManager updateManager;
    protected Document doc;
    protected DrawElement currentElementType;
    protected int x = 0, y = 0;

    public static void main(String[] args) {
        // Create a new JFrame.
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
        JButton buttonLoad = new JButton("Load...");
        JButton buttonNewClass = new JButton("class");
        JButton buttonNewLine = new JButton("line");

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(buttonLoad);
        p.add(buttonNewClass);
        p.add(buttonNewLine);
        p.add(label);

        panel.add("North", p);
        panel.add("Center", svgCanvas);

        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        doc = impl.createDocument(SVG_NS, "svg", null);
        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        svgCanvas.setDocument(doc);

        // Set the button action.
        buttonLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
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
            }
        });

        buttonNewClass.addActionListener(ae -> currentElementType = new DrawClassElement(doc));
        buttonNewLine.addActionListener(ae -> currentElementType = new DrawLineElement(doc));

        // Set the JSVGCanvas listeners.
        svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                label.setText("Document Loading...");
            }

            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                label.setText("Document Loaded.");
            }
        });
        Element svgRoot = doc.getDocumentElement();

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
                addGlassPane();

                Element elt = svgCanvas.getSVGDocument().getElementById("glasspane");
                EventTarget target = (EventTarget) elt;

                target.addEventListener("mousedown", new OnDownAction(), false);
                target.addEventListener("mousemove", new OnMoveAction(), false);
                target.addEventListener("mouseup", new OnUpAction(), false);
                target.addEventListener("mouseout", new OnUpAction(), false);
                //target.addEventListener("mouseover", new OnOverAction(), false);
                //target.addEventListener("click", new OnClickAction(), false);


                Element elt2 = svgCanvas.getSVGDocument().getElementById("rectangles");
                EventTarget t2 = (EventTarget)elt2;

                Point2D p = getCanvasCoordinate(new Point2D.Float(x, y));
         GraphicsNode gn = svgCanvas.getGraphicsNode().nodeHitAt(p);
         BridgeContext bc =
                 ((JSVGCanvas) svgCanvas).getUpdateManager().getBridgeContext();
             //(Element) bc.getElement(gn);


               // t2.addEventListener("click", new ObjectClick(), false);
            }
        });

        return panel;
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
                 rectangle.setAttributeNS(null, "width", "400");
                 rectangle.setAttributeNS(null, "height", "400");
                 rectangle.setAttributeNS(null, "style", "fill:none;pointer-events:fill");
                 rectangle.setAttributeNS(null, "id", "glasspane");
                 Element svgRoot = doc.getDocumentElement();

                 svgRoot.appendChild(rectangle);

             }


    protected int drag;
    protected Element selectedItem;
    protected SVGOMPoint initialDragPoint;
    protected final int DRAG_UP = 0;
    protected final int DRAG_DOWN = 1;

    public class OnDownAction implements org.w3c.dom.events.EventListener {

        @Override
        public void handleEvent(Event evt) {
            SVGLocatable thisNode = (SVGLocatable) evt.getTarget();
            selectedItem = (Element) evt.getTarget();
            drag = DRAG_DOWN;


            DOMMouseEvent elEvt = (DOMMouseEvent) evt;
            int nowToX = elEvt.getClientX();
            int nowToY = elEvt.getClientY();

            SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
            SVGMatrix mat = thisNode.getScreenCTM();  // elem -> screen
            mat = mat.inverse();                  // screen -> elem
            initialDragPoint = (SVGOMPoint)pt.matrixTransform(mat);

            Element svgRoot = doc.getDocumentElement();
            if (currentElementType != null) {
                Element e = currentElementType.placeNew(nowToX, nowToY);
                svgRoot.appendChild(e);
            }
        }
    }
    public class OnUpAction implements EventListener {
           public void handleEvent(Event evt) {
               drag = DRAG_UP;



           }
    }

  public class OnMoveAction implements EventListener{

      @Override
      public void handleEvent(Event evt) {
          if (drag == DRAG_DOWN){
              DOMMouseEvent elEvt = (DOMMouseEvent) evt;
              int nowToX = elEvt.getClientX();
              int nowToY = elEvt.getClientY();
              SVGOMPoint pt = new SVGOMPoint(nowToX, nowToY);
              SVGMatrix mat = ((SVGLocatable)evt.getTarget()).getScreenCTM();

              mat = mat.inverse();
              SVGOMPoint dragpt = (SVGOMPoint)pt.matrixTransform(mat);

              Element element = (Element) evt.getTarget();
              float newX = dragpt.getX();
              if (dragpt.getX() < initialDragPoint.getX()){
                  newX = newX - 10;
                  System.out.println("left");
              }
              System.out.println("initialX:" + initialDragPoint.getX());
              System.out.println("x:" + element.getAttribute("x") + " y:" + element.getAttribute("y") + " -> " + dragpt.getX() + " y:"+dragpt.getY());
              element.setAttribute("x", ""+ newX);
          }
      }
  }

}