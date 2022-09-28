package org.apache.batik.dom;

import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.stylesheets.LinkStyle;
import org.w3c.dom.stylesheets.StyleSheet;

public class StyleSheetProcessingInstruction extends AbstractProcessingInstruction implements LinkStyle {
   protected boolean readonly;
   protected transient StyleSheet sheet;
   protected StyleSheetFactory factory;
   protected transient HashTable pseudoAttributes;

   protected StyleSheetProcessingInstruction() {
   }

   public StyleSheetProcessingInstruction(String var1, AbstractDocument var2, StyleSheetFactory var3) {
      this.ownerDocument = var2;
      this.setData(var1);
      this.factory = var3;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   public void setNodeName(String var1) {
   }

   public String getTarget() {
      return "xml-stylesheet";
   }

   public StyleSheet getSheet() {
      if (this.sheet == null) {
         this.sheet = this.factory.createStyleSheet(this, this.getPseudoAttributes());
      }

      return this.sheet;
   }

   public HashTable getPseudoAttributes() {
      if (this.pseudoAttributes == null) {
         this.pseudoAttributes = new HashTable();
         this.pseudoAttributes.put("alternate", "no");
         this.pseudoAttributes.put("media", "all");
         DOMUtilities.parseStyleSheetPIData(this.data, this.pseudoAttributes);
      }

      return this.pseudoAttributes;
   }

   public void setData(String var1) throws DOMException {
      super.setData(var1);
      this.sheet = null;
      this.pseudoAttributes = null;
   }

   protected Node newNode() {
      return new StyleSheetProcessingInstruction();
   }
}
