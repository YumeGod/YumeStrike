package org.apache.batik.svggen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMTreeManager implements SVGSyntax, ErrorConstants {
   int maxGCOverrides;
   protected final List groupManagers = Collections.synchronizedList(new ArrayList());
   protected List genericDefSet = new LinkedList();
   SVGGraphicContext defaultGC;
   protected Element topLevelGroup;
   SVGGraphicContextConverter gcConverter;
   protected SVGGeneratorContext generatorContext;
   protected SVGBufferedImageOp filterConverter;
   protected List otherDefs;

   public DOMTreeManager(GraphicContext var1, SVGGeneratorContext var2, int var3) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("gc should not be null");
      } else if (var3 <= 0) {
         throw new SVGGraphics2DRuntimeException("maxGcOverrides should be greater than zero");
      } else if (var2 == null) {
         throw new SVGGraphics2DRuntimeException("generatorContext should not be null");
      } else {
         this.generatorContext = var2;
         this.maxGCOverrides = var3;
         this.recycleTopLevelGroup();
         this.defaultGC = this.gcConverter.toSVG(var1);
      }
   }

   public void addGroupManager(DOMGroupManager var1) {
      if (var1 != null) {
         this.groupManagers.add(var1);
      }

   }

   public void removeGroupManager(DOMGroupManager var1) {
      if (var1 != null) {
         this.groupManagers.remove(var1);
      }

   }

   public void appendGroup(Element var1, DOMGroupManager var2) {
      this.topLevelGroup.appendChild(var1);
      synchronized(this.groupManagers) {
         int var4 = this.groupManagers.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            DOMGroupManager var6 = (DOMGroupManager)this.groupManagers.get(var5);
            if (var6 != var2) {
               var6.recycleCurrentGroup();
            }
         }

      }
   }

   protected void recycleTopLevelGroup() {
      this.recycleTopLevelGroup(true);
   }

   protected void recycleTopLevelGroup(boolean var1) {
      synchronized(this.groupManagers) {
         int var3 = this.groupManagers.size();
         int var4 = 0;

         while(true) {
            if (var4 >= var3) {
               break;
            }

            DOMGroupManager var5 = (DOMGroupManager)this.groupManagers.get(var4);
            var5.recycleCurrentGroup();
            ++var4;
         }
      }

      this.topLevelGroup = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "g");
      if (var1) {
         this.filterConverter = new SVGBufferedImageOp(this.generatorContext);
         this.gcConverter = new SVGGraphicContextConverter(this.generatorContext);
      }

   }

   public void setTopLevelGroup(Element var1) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("topLevelGroup should not be null");
      } else if (!"g".equalsIgnoreCase(var1.getTagName())) {
         throw new SVGGraphics2DRuntimeException("topLevelGroup should be a group <g>");
      } else {
         this.recycleTopLevelGroup(false);
         this.topLevelGroup = var1;
      }
   }

   public Element getRoot() {
      return this.getRoot((Element)null);
   }

   public Element getRoot(Element var1) {
      Element var2 = var1;
      if (var1 == null) {
         var2 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "svg");
      }

      if (this.gcConverter.getCompositeConverter().getAlphaCompositeConverter().requiresBackgroundAccess()) {
         var2.setAttributeNS((String)null, "enable-background", "new");
      }

      if (this.generatorContext.generatorComment != null) {
         Comment var3 = this.generatorContext.domFactory.createComment(this.generatorContext.generatorComment);
         var2.appendChild(var3);
      }

      this.applyDefaultRenderingStyle(var2);
      var2.appendChild(this.getGenericDefinitions());
      var2.appendChild(this.getTopLevelGroup());
      return var2;
   }

   public void applyDefaultRenderingStyle(Element var1) {
      Map var2 = this.defaultGC.getGroupContext();
      this.generatorContext.styleHandler.setStyle(var1, var2, this.generatorContext);
   }

   public Element getGenericDefinitions() {
      Element var1 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "defs");
      Iterator var2 = this.genericDefSet.iterator();

      while(var2.hasNext()) {
         var1.appendChild((Element)var2.next());
      }

      var1.setAttributeNS((String)null, "id", "genericDefs");
      return var1;
   }

   public ExtensionHandler getExtensionHandler() {
      return this.generatorContext.getExtensionHandler();
   }

   void setExtensionHandler(ExtensionHandler var1) {
      this.generatorContext.setExtensionHandler(var1);
   }

   public List getDefinitionSet() {
      List var1 = this.gcConverter.getDefinitionSet();
      var1.removeAll(this.genericDefSet);
      var1.addAll(this.filterConverter.getDefinitionSet());
      if (this.otherDefs != null) {
         var1.addAll(this.otherDefs);
         this.otherDefs = null;
      }

      this.filterConverter = new SVGBufferedImageOp(this.generatorContext);
      this.gcConverter = new SVGGraphicContextConverter(this.generatorContext);
      return var1;
   }

   public void addOtherDef(Element var1) {
      if (this.otherDefs == null) {
         this.otherDefs = new LinkedList();
      }

      this.otherDefs.add(var1);
   }

   public Element getTopLevelGroup() {
      boolean var1 = true;
      return this.getTopLevelGroup(var1);
   }

   public Element getTopLevelGroup(boolean var1) {
      Element var2 = this.topLevelGroup;
      if (var1) {
         List var3 = this.getDefinitionSet();
         if (var3.size() > 0) {
            Element var4 = null;
            NodeList var5 = var2.getElementsByTagName("defs");
            if (var5.getLength() > 0) {
               var4 = (Element)var5.item(0);
            }

            if (var4 == null) {
               var4 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "defs");
               var4.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("defs"));
               var2.insertBefore(var4, var2.getFirstChild());
            }

            Iterator var6 = var3.iterator();

            while(var6.hasNext()) {
               var4.appendChild((Element)var6.next());
            }
         }
      }

      this.recycleTopLevelGroup(false);
      return var2;
   }

   public SVGBufferedImageOp getFilterConverter() {
      return this.filterConverter;
   }

   public SVGGraphicContextConverter getGraphicContextConverter() {
      return this.gcConverter;
   }

   SVGGeneratorContext getGeneratorContext() {
      return this.generatorContext;
   }

   Document getDOMFactory() {
      return this.generatorContext.domFactory;
   }

   StyleHandler getStyleHandler() {
      return this.generatorContext.styleHandler;
   }
}
