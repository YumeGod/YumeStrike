package org.apache.fop.svg;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class SVGUtilities {
   private static final String SVG_NS = "http://www.w3.org/2000/svg";

   public static final Document createSVGDocument(float width, float height) {
      DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
      Document doc = impl.createDocument("http://www.w3.org/2000/svg", "svg", (DocumentType)null);
      Element svgRoot = doc.getDocumentElement();
      svgRoot.setAttributeNS((String)null, "width", "" + width);
      svgRoot.setAttributeNS((String)null, "height", "" + height);
      return doc;
   }

   public static final float getStringWidth(String str, Font font) {
      Rectangle2D rect = font.getStringBounds(str, 0, str.length(), new FontRenderContext(new AffineTransform(), true, true));
      return (float)rect.getWidth();
   }

   public static final float getStringHeight(String str, Font font) {
      Rectangle2D rect = font.getStringBounds(str, 0, str.length(), new FontRenderContext(new AffineTransform(), true, true));
      return (float)rect.getHeight();
   }

   public static final Rectangle2D getStringBounds(String str, Font font) {
      return font.getStringBounds(str, 0, str.length(), new FontRenderContext(new AffineTransform(), true, true));
   }

   public static final Element createLine(Document doc, float x, float y, float x2, float y2) {
      Element ellipse = doc.createElementNS("http://www.w3.org/2000/svg", "line");
      ellipse.setAttributeNS((String)null, "x1", "" + x);
      ellipse.setAttributeNS((String)null, "x2", "" + x2);
      ellipse.setAttributeNS((String)null, "y1", "" + y);
      ellipse.setAttributeNS((String)null, "y2", "" + y2);
      return ellipse;
   }

   public static final Element createEllipse(Document doc, float cx, float cy, float rx, float ry) {
      Element ellipse = doc.createElementNS("http://www.w3.org/2000/svg", "ellipse");
      ellipse.setAttributeNS((String)null, "cx", "" + cx);
      ellipse.setAttributeNS((String)null, "rx", "" + rx);
      ellipse.setAttributeNS((String)null, "cy", "" + cy);
      ellipse.setAttributeNS((String)null, "ry", "" + ry);
      return ellipse;
   }

   public static final Element createPath(Document doc, String str) {
      Element path = doc.createElementNS("http://www.w3.org/2000/svg", "path");
      path.setAttributeNS((String)null, "d", str);
      return path;
   }

   public static final Element createText(Document doc, float x, float y, String str) {
      Element textGraph = doc.createElementNS("http://www.w3.org/2000/svg", "text");
      textGraph.setAttributeNS((String)null, "x", "" + x);
      textGraph.setAttributeNS((String)null, "y", "" + y);
      Text text = doc.createTextNode(str);
      textGraph.appendChild(text);
      return textGraph;
   }

   public static final Element createRect(Document doc, float x, float y, float width, float height) {
      Element border = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
      border.setAttributeNS((String)null, "x", "" + x);
      border.setAttributeNS((String)null, "y", "" + y);
      border.setAttributeNS((String)null, "width", "" + width);
      border.setAttributeNS((String)null, "height", "" + height);
      return border;
   }

   public static final Element createG(Document doc) {
      Element border = doc.createElementNS("http://www.w3.org/2000/svg", "g");
      return border;
   }

   public static final Element createClip(Document doc, Element els, String id) {
      Element border = doc.createElementNS("http://www.w3.org/2000/svg", "clipPath");
      border.setAttributeNS((String)null, "id", id);
      border.appendChild(els);
      return border;
   }

   public static final Element createImage(Document doc, String ref, float width, float height) {
      Element border = doc.createElementNS("http://www.w3.org/2000/svg", "image");
      border.setAttributeNS("http://www.w3.org/1999/xlink", "href", ref);
      border.setAttributeNS((String)null, "width", "" + width);
      border.setAttributeNS((String)null, "height", "" + height);
      return border;
   }

   public static final Element wrapText(Document doc, String str, Font font, float width) {
      Element g = createG(doc);
      StringTokenizer st = new StringTokenizer(str, " \t\r\n");
      float totalWidth = 0.0F;
      String totalStr = "";
      int line = 0;
      float height = getStringHeight(str, font);

      while(st.hasMoreTokens()) {
         String token = st.nextToken();
         float strwidth = getStringWidth(token, font);
         totalWidth += strwidth;
         if (totalWidth > width) {
            if (totalStr.equals("")) {
               totalStr = token;
               token = "";
               strwidth = 0.0F;
            }

            Element text = createText(doc, 0.0F, (float)line * (height + 5.0F), totalStr);
            g.appendChild(text);
            totalStr = token;
            totalWidth = strwidth;
            ++line;
         } else {
            totalStr = totalStr + " " + token;
         }
      }

      return g;
   }
}
