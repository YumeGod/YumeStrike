package org.apache.batik.bridge.svg12;

import java.util.ArrayList;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathPatternContentSelector extends AbstractContentSelector {
   protected NSPrefixResolver prefixResolver = new NSPrefixResolver();
   protected XPath xpath;
   protected XPathContext context;
   protected SelectedNodes selectedContent;
   protected String expression;

   public XPathPatternContentSelector(ContentManager var1, XBLOMContentElement var2, Element var3, String var4) {
      super(var1, var2, var3);
      this.expression = var4;
      this.parse();
   }

   protected void parse() {
      this.context = new XPathContext();

      try {
         this.xpath = new XPath(this.expression, (SourceLocator)null, this.prefixResolver, 1);
      } catch (TransformerException var3) {
         AbstractDocument var2 = (AbstractDocument)this.contentElement.getOwnerDocument();
         throw var2.createXPathException((short)51, "xpath.invalid.expression", new Object[]{this.expression, var3.getMessage()});
      }
   }

   public NodeList getSelectedContent() {
      if (this.selectedContent == null) {
         this.selectedContent = new SelectedNodes();
      }

      return this.selectedContent;
   }

   boolean update() {
      if (this.selectedContent == null) {
         this.selectedContent = new SelectedNodes();
         return true;
      } else {
         this.parse();
         return this.selectedContent.update();
      }
   }

   protected class NSPrefixResolver implements PrefixResolver {
      public String getBaseIdentifier() {
         return null;
      }

      public String getNamespaceForPrefix(String var1) {
         return XPathPatternContentSelector.this.contentElement.lookupNamespaceURI(var1);
      }

      public String getNamespaceForPrefix(String var1, Node var2) {
         return XPathPatternContentSelector.this.contentElement.lookupNamespaceURI(var1);
      }

      public boolean handlesNullPrefixes() {
         return false;
      }
   }

   protected class SelectedNodes implements NodeList {
      protected ArrayList nodes = new ArrayList(10);

      public SelectedNodes() {
         this.update();
      }

      protected boolean update() {
         ArrayList var1 = (ArrayList)this.nodes.clone();
         this.nodes.clear();

         for(Node var2 = XPathPatternContentSelector.this.boundElement.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            this.update(var2);
         }

         int var4 = this.nodes.size();
         if (var1.size() != var4) {
            return true;
         } else {
            for(int var3 = 0; var3 < var4; ++var3) {
               if (var1.get(var3) != this.nodes.get(var3)) {
                  return true;
               }
            }

            return false;
         }
      }

      protected boolean descendantSelected(Node var1) {
         for(var1 = var1.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
            if (XPathPatternContentSelector.this.isSelected(var1) || this.descendantSelected(var1)) {
               return true;
            }
         }

         return false;
      }

      protected void update(Node var1) {
         if (!XPathPatternContentSelector.this.isSelected(var1)) {
            try {
               double var2 = XPathPatternContentSelector.this.xpath.execute(XPathPatternContentSelector.this.context, var1, XPathPatternContentSelector.this.prefixResolver).num();
               if (var2 != Double.NEGATIVE_INFINITY) {
                  if (!this.descendantSelected(var1)) {
                     this.nodes.add(var1);
                  }
               } else {
                  for(var1 = var1.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
                     this.update(var1);
                  }
               }
            } catch (TransformerException var4) {
               AbstractDocument var3 = (AbstractDocument)XPathPatternContentSelector.this.contentElement.getOwnerDocument();
               throw var3.createXPathException((short)51, "xpath.error", new Object[]{XPathPatternContentSelector.this.expression, var4.getMessage()});
            }
         }

      }

      public Node item(int var1) {
         return var1 >= 0 && var1 < this.nodes.size() ? (Node)this.nodes.get(var1) : null;
      }

      public int getLength() {
         return this.nodes.size();
      }
   }
}
