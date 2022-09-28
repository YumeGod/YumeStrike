package org.apache.batik.apps.svgbrowser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.Overlay;
import org.w3c.dom.Element;

public class ElementOverlayManager {
   protected Color elementOverlayStrokeColor;
   protected Color elementOverlayColor;
   protected boolean xorMode;
   protected JSVGCanvas canvas;
   protected Overlay elementOverlay;
   protected ArrayList elements;
   protected ElementOverlayController controller;
   protected boolean isOverlayEnabled;

   public ElementOverlayManager(JSVGCanvas var1) {
      this.elementOverlayStrokeColor = Color.black;
      this.elementOverlayColor = Color.white;
      this.xorMode = true;
      this.elementOverlay = new ElementOverlay();
      this.isOverlayEnabled = true;
      this.canvas = var1;
      this.elements = new ArrayList();
      var1.getOverlays().add(this.elementOverlay);
   }

   public void addElement(Element var1) {
      this.elements.add(var1);
   }

   public void removeElement(Element var1) {
      if (this.elements.remove(var1)) {
      }

   }

   public void removeElements() {
      this.elements.clear();
      this.repaint();
   }

   protected Rectangle getAllElementsBounds() {
      Rectangle var1 = null;
      int var2 = this.elements.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         Element var4 = (Element)this.elements.get(var3);
         Rectangle var5 = this.getElementBounds(var4);
         if (var1 == null) {
            var1 = var5;
         } else {
            var1.add(var5);
         }
      }

      return var1;
   }

   protected Rectangle getElementBounds(Element var1) {
      return this.getElementBounds(this.canvas.getUpdateManager().getBridgeContext().getGraphicsNode(var1));
   }

   protected Rectangle getElementBounds(GraphicsNode var1) {
      if (var1 == null) {
         return null;
      } else {
         AffineTransform var2 = this.canvas.getRenderingTransform();
         Shape var3 = var2.createTransformedShape(var1.getOutline());
         return this.outset(var3.getBounds(), 1);
      }
   }

   protected Rectangle outset(Rectangle var1, int var2) {
      var1.x -= var2;
      var1.y -= var2;
      var1.width += 2 * var2;
      var1.height += 2 * var2;
      return var1;
   }

   public void repaint() {
      this.canvas.repaint();
   }

   public Color getElementOverlayColor() {
      return this.elementOverlayColor;
   }

   public void setElementOverlayColor(Color var1) {
      this.elementOverlayColor = var1;
   }

   public Color getElementOverlayStrokeColor() {
      return this.elementOverlayStrokeColor;
   }

   public void setElementOverlayStrokeColor(Color var1) {
      this.elementOverlayStrokeColor = var1;
   }

   public boolean isXorMode() {
      return this.xorMode;
   }

   public void setXorMode(boolean var1) {
      this.xorMode = var1;
   }

   public Overlay getElementOverlay() {
      return this.elementOverlay;
   }

   public void removeOverlay() {
      this.canvas.getOverlays().remove(this.elementOverlay);
   }

   public void setController(ElementOverlayController var1) {
      this.controller = var1;
   }

   public boolean isOverlayEnabled() {
      return this.isOverlayEnabled;
   }

   public void setOverlayEnabled(boolean var1) {
      this.isOverlayEnabled = var1;
   }

   public class ElementOverlay implements Overlay {
      public void paint(Graphics var1) {
         if (ElementOverlayManager.this.controller.isOverlayEnabled() && ElementOverlayManager.this.isOverlayEnabled()) {
            int var2 = ElementOverlayManager.this.elements.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               Element var4 = (Element)ElementOverlayManager.this.elements.get(var3);
               GraphicsNode var5 = ElementOverlayManager.this.canvas.getUpdateManager().getBridgeContext().getGraphicsNode(var4);
               if (var5 != null) {
                  AffineTransform var6 = var5.getGlobalTransform();
                  Shape var7 = var5.getOutline();
                  AffineTransform var8 = ElementOverlayManager.this.canvas.getRenderingTransform();
                  var8.concatenate(var6);
                  Shape var9 = var8.createTransformedShape(var7);
                  if (var9 == null) {
                     break;
                  }

                  Graphics2D var10 = (Graphics2D)var1;
                  if (ElementOverlayManager.this.xorMode) {
                     var10.setColor(Color.black);
                     var10.setXORMode(Color.yellow);
                     var10.fill(var9);
                     var10.draw(var9);
                  } else {
                     var10.setColor(ElementOverlayManager.this.elementOverlayColor);
                     var10.setStroke(new BasicStroke(1.8F));
                     var10.setColor(ElementOverlayManager.this.elementOverlayStrokeColor);
                     var10.draw(var9);
                  }
               }
            }
         }

      }
   }
}
