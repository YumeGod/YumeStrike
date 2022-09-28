package org.apache.batik.bridge;

import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.util.HaltingThread;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GVTBuilder implements SVGConstants {
   public GraphicsNode build(BridgeContext var1, Document var2) {
      var1.setDocument(var2);
      var1.initializeDocument(var2);
      var1.setGVTBuilder(this);
      DocumentBridge var3 = var1.getDocumentBridge();
      RootGraphicsNode var4 = null;

      try {
         var4 = var3.createGraphicsNode(var1, var2);
         Element var5 = var2.getDocumentElement();
         GraphicsNode var6 = null;
         Bridge var7 = var1.getBridge(var5);
         if (var7 == null || !(var7 instanceof GraphicsNodeBridge)) {
            return null;
         }

         GraphicsNodeBridge var8 = (GraphicsNodeBridge)var7;
         var6 = var8.createGraphicsNode(var1, var5);
         if (var6 == null) {
            return null;
         }

         var4.getChildren().add(var6);
         this.buildComposite(var1, var5, (CompositeGraphicsNode)var6);
         var8.buildGraphicsNode(var1, var5, var6);
         var3.buildGraphicsNode(var1, var2, var4);
      } catch (BridgeException var9) {
         var9.setGraphicsNode(var4);
         throw var9;
      }

      if (var1.isInteractive()) {
         var1.addUIEventListeners(var2);
         var1.addGVTListener(var2);
      }

      if (var1.isDynamic()) {
         var1.addDOMListeners();
      }

      return var4;
   }

   public GraphicsNode build(BridgeContext var1, Element var2) {
      Bridge var3 = var1.getBridge(var2);
      if (var3 instanceof GenericBridge) {
         ((GenericBridge)var3).handleElement(var1, var2);
         this.handleGenericBridges(var1, var2);
         return null;
      } else if (var3 != null && var3 instanceof GraphicsNodeBridge) {
         GraphicsNodeBridge var4 = (GraphicsNodeBridge)var3;
         if (!var4.getDisplay(var2)) {
            this.handleGenericBridges(var1, var2);
            return null;
         } else {
            GraphicsNode var5 = var4.createGraphicsNode(var1, var2);
            if (var5 != null) {
               if (var4.isComposite()) {
                  this.buildComposite(var1, var2, (CompositeGraphicsNode)var5);
               } else {
                  this.handleGenericBridges(var1, var2);
               }

               var4.buildGraphicsNode(var1, var2, var5);
            }

            if (var1.isDynamic()) {
            }

            return var5;
         }
      } else {
         this.handleGenericBridges(var1, var2);
         return null;
      }
   }

   protected void buildComposite(BridgeContext var1, Element var2, CompositeGraphicsNode var3) {
      for(Node var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            this.buildGraphicsNode(var1, (Element)var4, var3);
         }
      }

   }

   protected void buildGraphicsNode(BridgeContext var1, Element var2, CompositeGraphicsNode var3) {
      if (HaltingThread.hasBeenHalted()) {
         throw new InterruptedBridgeException();
      } else {
         Bridge var4 = var1.getBridge(var2);
         if (var4 instanceof GenericBridge) {
            ((GenericBridge)var4).handleElement(var1, var2);
            this.handleGenericBridges(var1, var2);
         } else if (var4 != null && var4 instanceof GraphicsNodeBridge) {
            if (!CSSUtilities.convertDisplay(var2)) {
               this.handleGenericBridges(var1, var2);
            } else {
               GraphicsNodeBridge var5 = (GraphicsNodeBridge)var4;

               try {
                  GraphicsNode var6 = var5.createGraphicsNode(var1, var2);
                  if (var6 != null) {
                     var3.getChildren().add(var6);
                     if (var5.isComposite()) {
                        this.buildComposite(var1, var2, (CompositeGraphicsNode)var6);
                     } else {
                        this.handleGenericBridges(var1, var2);
                     }

                     var5.buildGraphicsNode(var1, var2, var6);
                  } else {
                     this.handleGenericBridges(var1, var2);
                  }

               } catch (BridgeException var8) {
                  GraphicsNode var7 = var8.getGraphicsNode();
                  if (var7 != null) {
                     var3.getChildren().add(var7);
                     var5.buildGraphicsNode(var1, var2, var7);
                     var8.setGraphicsNode((GraphicsNode)null);
                  }

                  throw var8;
               }
            }
         } else {
            this.handleGenericBridges(var1, var2);
         }
      }
   }

   protected void handleGenericBridges(BridgeContext var1, Element var2) {
      for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof Element) {
            Element var4 = (Element)var3;
            Bridge var5 = var1.getBridge(var4);
            if (var5 instanceof GenericBridge) {
               ((GenericBridge)var5).handleElement(var1, var4);
            }

            this.handleGenericBridges(var1, var4);
         }
      }

   }
}
