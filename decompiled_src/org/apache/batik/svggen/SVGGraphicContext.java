package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.Map;
import org.apache.batik.ext.awt.g2d.TransformStackElement;
import org.apache.batik.util.SVGConstants;

public class SVGGraphicContext implements SVGConstants, ErrorConstants {
   private static final String[] leafOnlyAttributes = new String[]{"opacity", "filter", "clip-path"};
   private static final String[] defaultValues = new String[]{"1", "none", "none"};
   private Map context;
   private Map groupContext;
   private Map graphicElementContext;
   private TransformStackElement[] transformStack;

   public SVGGraphicContext(Map var1, TransformStackElement[] var2) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("context map(s) should not be null");
      } else if (var2 == null) {
         throw new SVGGraphics2DRuntimeException("transformer stack should not be null");
      } else {
         this.context = var1;
         this.transformStack = var2;
         this.computeGroupAndGraphicElementContext();
      }
   }

   public SVGGraphicContext(Map var1, Map var2, TransformStackElement[] var3) {
      if (var1 != null && var2 != null) {
         if (var3 == null) {
            throw new SVGGraphics2DRuntimeException("transformer stack should not be null");
         } else {
            this.groupContext = var1;
            this.graphicElementContext = var2;
            this.transformStack = var3;
            this.computeContext();
         }
      } else {
         throw new SVGGraphics2DRuntimeException("context map(s) should not be null");
      }
   }

   public Map getContext() {
      return this.context;
   }

   public Map getGroupContext() {
      return this.groupContext;
   }

   public Map getGraphicElementContext() {
      return this.graphicElementContext;
   }

   public TransformStackElement[] getTransformStack() {
      return this.transformStack;
   }

   private void computeContext() {
      if (this.context == null) {
         this.context = new HashMap(this.groupContext);
         this.context.putAll(this.graphicElementContext);
      }
   }

   private void computeGroupAndGraphicElementContext() {
      if (this.groupContext == null) {
         this.groupContext = new HashMap(this.context);
         this.graphicElementContext = new HashMap();

         for(int var1 = 0; var1 < leafOnlyAttributes.length; ++var1) {
            Object var2 = this.groupContext.get(leafOnlyAttributes[var1]);
            if (var2 != null) {
               if (!var2.equals(defaultValues[var1])) {
                  this.graphicElementContext.put(leafOnlyAttributes[var1], var2);
               }

               this.groupContext.remove(leafOnlyAttributes[var1]);
            }
         }

      }
   }
}
