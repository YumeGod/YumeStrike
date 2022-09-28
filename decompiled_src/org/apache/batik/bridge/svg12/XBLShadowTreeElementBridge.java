package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.AbstractGraphicsNodeBridge;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class XBLShadowTreeElementBridge extends AbstractGraphicsNodeBridge {
   public String getLocalName() {
      return "shadowTree";
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2004/xbl";
   }

   public Bridge getInstance() {
      return new XBLShadowTreeElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         CompositeGraphicsNode var3 = new CompositeGraphicsNode();
         this.associateSVGContext(var1, var2, var3);
         return var3;
      }
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return null;
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      this.initializeDynamicSupport(var1, var2, var3);
   }

   public boolean getDisplay(Element var1) {
      return true;
   }

   public boolean isComposite() {
      return true;
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      if (var1.getTarget() instanceof Element) {
         this.handleElementAdded((CompositeGraphicsNode)this.node, this.e, (Element)var1.getTarget());
      }

   }

   public void handleElementAdded(CompositeGraphicsNode var1, Node var2, Element var3) {
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
