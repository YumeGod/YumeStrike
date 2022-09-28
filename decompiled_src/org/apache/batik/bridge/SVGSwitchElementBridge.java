package org.apache.batik.bridge;

import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGTests;

public class SVGSwitchElementBridge extends SVGGElementBridge {
   protected Element selectedChild;

   public String getLocalName() {
      return "switch";
   }

   public Bridge getInstance() {
      return new SVGSwitchElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      GraphicsNode var3 = null;
      GVTBuilder var4 = var1.getGVTBuilder();
      this.selectedChild = null;

      for(Node var5 = var2.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
         if (var5.getNodeType() == 1) {
            Element var6 = (Element)var5;
            if (var5 instanceof SVGTests && SVGUtilities.matchUserAgent(var6, var1.getUserAgent())) {
               this.selectedChild = var6;
               var3 = var4.build(var1, var6);
               break;
            }
         }
      }

      if (var3 == null) {
         return null;
      } else {
         CompositeGraphicsNode var7 = (CompositeGraphicsNode)super.createGraphicsNode(var1, var2);
         if (var7 == null) {
            return null;
         } else {
            var7.add(var3);
            return var7;
         }
      }
   }

   public boolean isComposite() {
      return false;
   }

   public void dispose() {
      this.selectedChild = null;
      super.dispose();
   }

   protected void handleElementAdded(CompositeGraphicsNode var1, Node var2, Element var3) {
      for(Node var4 = var3.getPreviousSibling(); var4 != null; var4 = var4.getPreviousSibling()) {
         if (var4 == var3) {
            return;
         }
      }

      if (var3 instanceof SVGTests && SVGUtilities.matchUserAgent(var3, this.ctx.getUserAgent())) {
         if (this.selectedChild != null) {
            var1.remove(0);
            this.disposeTree(this.selectedChild);
         }

         this.selectedChild = var3;
         GVTBuilder var6 = this.ctx.getGVTBuilder();
         GraphicsNode var5 = var6.build(this.ctx, var3);
         if (var5 != null) {
            var1.add(var5);
         }
      }

   }

   protected void handleChildElementRemoved(Element var1) {
      CompositeGraphicsNode var2 = (CompositeGraphicsNode)this.node;
      if (this.selectedChild == var1) {
         var2.remove(0);
         this.disposeTree(this.selectedChild);
         this.selectedChild = null;
         GraphicsNode var3 = null;
         GVTBuilder var4 = this.ctx.getGVTBuilder();

         for(Node var5 = var1.getNextSibling(); var5 != null; var5 = var5.getNextSibling()) {
            if (var5.getNodeType() == 1) {
               Element var6 = (Element)var5;
               if (var5 instanceof SVGTests && SVGUtilities.matchUserAgent(var6, this.ctx.getUserAgent())) {
                  var3 = var4.build(this.ctx, var6);
                  this.selectedChild = var6;
                  break;
               }
            }
         }

         if (var3 != null) {
            var2.add(var3);
         }
      }

   }
}
