package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.AbstractGraphicsNodeBridge;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XBLContentElementBridge extends AbstractGraphicsNodeBridge {
   protected ContentChangedListener contentChangedListener;
   protected ContentManager contentManager;

   public String getLocalName() {
      return "content";
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2004/xbl";
   }

   public Bridge getInstance() {
      return new XBLContentElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      CompositeGraphicsNode var3 = this.buildCompositeGraphicsNode(var1, var2, (CompositeGraphicsNode)null);
      return var3;
   }

   public CompositeGraphicsNode buildCompositeGraphicsNode(BridgeContext var1, Element var2, CompositeGraphicsNode var3) {
      XBLOMContentElement var4 = (XBLOMContentElement)var2;
      AbstractDocument var5 = (AbstractDocument)var2.getOwnerDocument();
      DefaultXBLManager var6 = (DefaultXBLManager)var5.getXBLManager();
      this.contentManager = var6.getContentManager(var2);
      if (var3 == null) {
         var3 = new CompositeGraphicsNode();
         this.associateSVGContext(var1, var2, var3);
      } else {
         int var7 = var3.size();

         for(int var8 = 0; var8 < var7; ++var8) {
            var3.remove(0);
         }
      }

      GVTBuilder var12 = var1.getGVTBuilder();
      NodeList var13 = this.contentManager.getSelectedContent(var4);
      if (var13 != null) {
         for(int var9 = 0; var9 < var13.getLength(); ++var9) {
            Node var10 = var13.item(var9);
            if (var10.getNodeType() == 1) {
               GraphicsNode var11 = var12.build(var1, (Element)var10);
               if (var11 != null) {
                  var3.add(var11);
               }
            }
         }
      }

      if (var1.isDynamic() && this.contentChangedListener == null) {
         this.contentChangedListener = new ContentChangedListener();
         this.contentManager.addContentSelectionChangedListener(var4, this.contentChangedListener);
      }

      return var3;
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
      return false;
   }

   public void dispose() {
      super.dispose();
      if (this.contentChangedListener != null) {
         this.contentManager.removeContentSelectionChangedListener((XBLOMContentElement)this.e, this.contentChangedListener);
      }

   }

   protected class ContentChangedListener implements ContentSelectionChangedListener {
      public void contentSelectionChanged(ContentSelectionChangedEvent var1) {
         XBLContentElementBridge.this.buildCompositeGraphicsNode(XBLContentElementBridge.this.ctx, XBLContentElementBridge.this.e, (CompositeGraphicsNode)XBLContentElementBridge.this.node);
      }
   }
}
