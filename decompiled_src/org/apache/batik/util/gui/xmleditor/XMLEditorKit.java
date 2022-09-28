package org.apache.batik.util.gui.xmleditor;

import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class XMLEditorKit extends DefaultEditorKit {
   public static final String XML_MIME_TYPE = "text/xml";
   protected XMLContext context;
   protected ViewFactory factory;

   public XMLEditorKit() {
      this((XMLContext)null);
   }

   public XMLEditorKit(XMLContext var1) {
      this.factory = null;
      this.factory = new XMLViewFactory();
      if (var1 == null) {
         this.context = new XMLContext();
      } else {
         this.context = var1;
      }

   }

   public XMLContext getStylePreferences() {
      return this.context;
   }

   public void install(JEditorPane var1) {
      super.install(var1);
      Font var2 = this.context.getSyntaxFont("default");
      if (var2 != null) {
         var1.setFont((Font)var2);
      }

   }

   public String getContentType() {
      return "text/xml";
   }

   public Object clone() {
      XMLEditorKit var1 = new XMLEditorKit();
      var1.context = this.context;
      return var1;
   }

   public Document createDefaultDocument() {
      XMLDocument var1 = new XMLDocument(this.context);
      return var1;
   }

   public ViewFactory getViewFactory() {
      return this.factory;
   }

   protected class XMLViewFactory implements ViewFactory {
      public View create(Element var1) {
         return new XMLView(XMLEditorKit.this.context, var1);
      }
   }
}
