package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.ext.awt.g2d.TransformStackElement;
import org.w3c.dom.Element;

public class DOMGroupManager implements SVGSyntax, ErrorConstants {
   public static final short DRAW = 1;
   public static final short FILL = 16;
   protected GraphicContext gc;
   protected DOMTreeManager domTreeManager;
   protected SVGGraphicContext groupGC;
   protected Element currentGroup;

   public DOMGroupManager(GraphicContext var1, DOMTreeManager var2) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("gc should not be null");
      } else if (var2 == null) {
         throw new SVGGraphics2DRuntimeException("domTreeManager should not be null");
      } else {
         this.gc = var1;
         this.domTreeManager = var2;
         this.recycleCurrentGroup();
         this.groupGC = var2.gcConverter.toSVG(var1);
      }
   }

   void recycleCurrentGroup() {
      this.currentGroup = this.domTreeManager.getDOMFactory().createElementNS("http://www.w3.org/2000/svg", "g");
   }

   public void addElement(Element var1) {
      this.addElement(var1, (short)17);
   }

   public void addElement(Element var1, short var2) {
      SVGGraphicContext var3;
      if (!this.currentGroup.hasChildNodes()) {
         this.currentGroup.appendChild(var1);
         this.groupGC = this.domTreeManager.gcConverter.toSVG(this.gc);
         var3 = processDeltaGC(this.groupGC, this.domTreeManager.defaultGC);
         this.domTreeManager.getStyleHandler().setStyle(this.currentGroup, var3.getGroupContext(), this.domTreeManager.getGeneratorContext());
         if ((var2 & 1) == 0) {
            var3.getGraphicElementContext().put("stroke", "none");
         }

         if ((var2 & 16) == 0) {
            var3.getGraphicElementContext().put("fill", "none");
         }

         this.domTreeManager.getStyleHandler().setStyle(var1, var3.getGraphicElementContext(), this.domTreeManager.getGeneratorContext());
         this.setTransform(this.currentGroup, var3.getTransformStack());
         this.domTreeManager.appendGroup(this.currentGroup, this);
      } else if (this.gc.isTransformStackValid()) {
         var3 = this.domTreeManager.gcConverter.toSVG(this.gc);
         SVGGraphicContext var4 = processDeltaGC(var3, this.groupGC);
         this.trimContextForElement(var4, var1);
         if (this.countOverrides(var4) <= this.domTreeManager.maxGCOverrides) {
            this.currentGroup.appendChild(var1);
            if ((var2 & 1) == 0) {
               var4.getContext().put("stroke", "none");
            }

            if ((var2 & 16) == 0) {
               var4.getContext().put("fill", "none");
            }

            this.domTreeManager.getStyleHandler().setStyle(var1, var4.getContext(), this.domTreeManager.getGeneratorContext());
            this.setTransform(var1, var4.getTransformStack());
         } else {
            this.currentGroup = this.domTreeManager.getDOMFactory().createElementNS("http://www.w3.org/2000/svg", "g");
            this.addElement(var1, var2);
         }
      } else {
         this.currentGroup = this.domTreeManager.getDOMFactory().createElementNS("http://www.w3.org/2000/svg", "g");
         this.gc.validateTransformStack();
         this.addElement(var1, var2);
      }

   }

   protected int countOverrides(SVGGraphicContext var1) {
      return var1.getGroupContext().size();
   }

   protected void trimContextForElement(SVGGraphicContext var1, Element var2) {
      String var3 = var2.getTagName();
      Map var4 = var1.getGroupContext();
      if (var3 != null) {
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            SVGAttribute var7 = SVGAttributeMap.get(var6);
            if (var7 != null && !var7.appliesTo(var3)) {
               var4.remove(var6);
            }
         }
      }

   }

   protected void setTransform(Element var1, TransformStackElement[] var2) {
      String var3 = this.domTreeManager.gcConverter.toSVG(var2).trim();
      if (var3.length() > 0) {
         var1.setAttributeNS((String)null, "transform", var3);
      }

   }

   static SVGGraphicContext processDeltaGC(SVGGraphicContext var0, SVGGraphicContext var1) {
      Map var2 = processDeltaMap(var0.getGroupContext(), var1.getGroupContext());
      Map var3 = var0.getGraphicElementContext();
      TransformStackElement[] var4 = var0.getTransformStack();
      TransformStackElement[] var5 = var1.getTransformStack();
      int var6 = var4.length - var5.length;
      TransformStackElement[] var7 = new TransformStackElement[var6];
      System.arraycopy(var4, var5.length, var7, 0, var6);
      SVGGraphicContext var8 = new SVGGraphicContext(var2, var3, var7);
      return var8;
   }

   static Map processDeltaMap(Map var0, Map var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5 = (String)var0.get(var4);
         String var6 = (String)var1.get(var4);
         if (!var5.equals(var6)) {
            var2.put(var4, var5);
         }
      }

      return var2;
   }
}
