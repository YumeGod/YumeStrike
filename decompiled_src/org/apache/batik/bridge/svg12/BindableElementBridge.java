package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.AbstractGraphicsNodeBridge;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.bridge.ScriptingEnvironment;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg12.BindableElement;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class BindableElementBridge extends AbstractGraphicsNodeBridge implements SVG12BridgeUpdateHandler {
   public String getNamespaceURI() {
      return "*";
   }

   public String getLocalName() {
      return "*";
   }

   public Bridge getInstance() {
      return new BindableElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         CompositeGraphicsNode var3 = this.buildCompositeGraphicsNode(var1, var2, (CompositeGraphicsNode)null);
         return var3;
      }
   }

   public CompositeGraphicsNode buildCompositeGraphicsNode(BridgeContext var1, Element var2, CompositeGraphicsNode var3) {
      BindableElement var4 = (BindableElement)var2;
      Element var5 = var4.getXblShadowTree();
      UpdateManager var6 = var1.getUpdateManager();
      ScriptingEnvironment var7 = var6 == null ? null : var6.getScriptingEnvironment();
      if (var7 != null && var5 != null) {
         var7.addScriptingListeners(var5);
      }

      if (var3 == null) {
         var3 = new CompositeGraphicsNode();
         this.associateSVGContext(var1, var2, var3);
      } else {
         int var8 = var3.size();

         for(int var9 = 0; var9 < var8; ++var9) {
            var3.remove(0);
         }
      }

      GVTBuilder var11 = var1.getGVTBuilder();
      if (var5 != null) {
         GraphicsNode var12 = var11.build(var1, var5);
         if (var12 != null) {
            var3.add(var12);
         }
      } else {
         for(Node var13 = var2.getFirstChild(); var13 != null; var13 = var13.getNextSibling()) {
            if (var13.getNodeType() == 1) {
               GraphicsNode var10 = var11.build(var1, (Element)var13);
               if (var10 != null) {
                  var3.add(var10);
               }
            }
         }
      }

      return var3;
   }

   public void dispose() {
      BindableElement var1 = (BindableElement)this.e;
      if (var1 != null && var1.getCSSFirstChild() != null) {
         this.disposeTree(var1.getCSSFirstChild());
      }

      super.dispose();
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return null;
   }

   public boolean isComposite() {
      return false;
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      this.initializeDynamicSupport(var1, var2, var3);
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      BindableElement var2 = (BindableElement)this.e;
      Element var3 = var2.getXblShadowTree();
      if (var3 == null && var1.getTarget() instanceof Element) {
         this.handleElementAdded((CompositeGraphicsNode)this.node, this.e, (Element)var1.getTarget());
      }

   }

   public void handleBindingEvent(Element var1, Element var2) {
      CompositeGraphicsNode var3 = this.node.getParent();
      var3.remove(this.node);
      this.disposeTree(this.e);
      this.handleElementAdded(var3, this.e.getParentNode(), this.e);
   }

   public void handleContentSelectionChangedEvent(ContentSelectionChangedEvent var1) {
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
