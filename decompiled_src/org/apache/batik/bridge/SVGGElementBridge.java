package org.apache.batik.bridge;

import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class SVGGElementBridge extends AbstractGraphicsNodeBridge {
   public String getLocalName() {
      return "g";
   }

   public Bridge getInstance() {
      return new SVGGElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      CompositeGraphicsNode var3 = (CompositeGraphicsNode)super.createGraphicsNode(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         this.associateSVGContext(var1, var2, var3);
         RenderingHints var4 = null;
         var4 = CSSUtilities.convertColorRendering(var2, var4);
         if (var4 != null) {
            var3.setRenderingHints(var4);
         }

         Rectangle2D var5 = CSSUtilities.convertEnableBackground(var2);
         if (var5 != null) {
            var3.setBackgroundEnable(var5);
         }

         return var3;
      }
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new CompositeGraphicsNode();
   }

   public boolean isComposite() {
      return true;
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      if (var1.getTarget() instanceof Element) {
         this.handleElementAdded((CompositeGraphicsNode)this.node, this.e, (Element)var1.getTarget());
      } else {
         super.handleDOMNodeInsertedEvent(var1);
      }

   }

   protected void handleElementAdded(CompositeGraphicsNode var1, Node var2, Element var3) {
      GVTBuilder var4 = this.ctx.getGVTBuilder();
      GraphicsNode var5 = var4.build(this.ctx, var3);
      if (var5 != null) {
         int var6 = -1;

         for(Node var7 = var3.getPreviousSibling(); var7 != null; var7 = var7.getPreviousSibling()) {
            if (var7.getNodeType() == 1) {
               Element var8 = (Element)var7;

               Object var9;
               for(var9 = this.ctx.getGraphicsNode(var8); var9 != null && ((GraphicsNode)var9).getParent() != var1; var9 = ((GraphicsNode)var9).getParent()) {
               }

               if (var9 != null) {
                  var6 = var1.indexOf(var9);
                  if (var6 != -1) {
                     break;
                  }
               }
            }
         }

         ++var6;
         var1.add(var6, var5);
      }
   }
}
