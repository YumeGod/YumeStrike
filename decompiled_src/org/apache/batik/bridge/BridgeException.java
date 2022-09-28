package org.apache.batik.bridge;

import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class BridgeException extends RuntimeException {
   protected Element e;
   protected String code;
   protected String message;
   protected Object[] params;
   protected int line;
   protected GraphicsNode node;

   public BridgeException(BridgeContext var1, LiveAttributeException var2) {
      switch (var2.getCode()) {
         case 0:
            this.code = "attribute.missing";
            break;
         case 1:
            this.code = "attribute.malformed";
            break;
         case 2:
            this.code = "length.negative";
            break;
         default:
            throw new IllegalStateException("Unknown LiveAttributeException error code " + var2.getCode());
      }

      this.e = var2.getElement();
      this.params = new Object[]{var2.getAttributeName(), var2.getValue()};
      if (this.e != null && var1 != null) {
         this.line = var1.getDocumentLoader().getLineNumber(this.e);
      }

   }

   public BridgeException(BridgeContext var1, Element var2, String var3, Object[] var4) {
      this.e = var2;
      this.code = var3;
      this.params = var4;
      if (var2 != null && var1 != null) {
         this.line = var1.getDocumentLoader().getLineNumber(var2);
      }

   }

   public BridgeException(BridgeContext var1, Element var2, Exception var3, String var4, Object[] var5) {
      this.e = var2;
      this.message = var3.getMessage();
      this.code = var4;
      this.params = var5;
      if (var2 != null && var1 != null) {
         this.line = var1.getDocumentLoader().getLineNumber(var2);
      }

   }

   public BridgeException(BridgeContext var1, Element var2, String var3) {
      this.e = var2;
      this.message = var3;
      if (var2 != null && var1 != null) {
         this.line = var1.getDocumentLoader().getLineNumber(var2);
      }

   }

   public Element getElement() {
      return this.e;
   }

   public void setGraphicsNode(GraphicsNode var1) {
      this.node = var1;
   }

   public GraphicsNode getGraphicsNode() {
      return this.node;
   }

   public String getMessage() {
      if (this.message != null) {
         return this.message;
      } else {
         String var2 = "<Unknown Element>";
         SVGDocument var3 = null;
         if (this.e != null) {
            var3 = (SVGDocument)this.e.getOwnerDocument();
            var2 = this.e.getLocalName();
         }

         String var1;
         if (var3 == null) {
            var1 = "<Unknown Document>";
         } else {
            var1 = var3.getURL();
         }

         Object[] var4 = new Object[this.params.length + 3];
         var4[0] = var1;
         var4[1] = new Integer(this.line);
         var4[2] = var2;
         System.arraycopy(this.params, 0, var4, 3, this.params.length);
         return Messages.formatMessage(this.code, var4);
      }
   }

   public String getCode() {
      return this.code;
   }
}
