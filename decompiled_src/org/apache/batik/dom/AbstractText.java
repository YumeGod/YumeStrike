package org.apache.batik.dom;

import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.xml.XMLUtilities;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public abstract class AbstractText extends AbstractCharacterData implements Text {
   public Text splitText(int var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         String var2 = this.getNodeValue();
         if (var1 >= 0 && var1 < var2.length()) {
            Node var3 = this.getParentNode();
            if (var3 == null) {
               throw this.createDOMException((short)1, "need.parent", new Object[0]);
            } else {
               String var4 = var2.substring(var1);
               Text var5 = this.createTextNode(var4);
               Node var6 = this.getNextSibling();
               if (var6 != null) {
                  var3.insertBefore(var5, var6);
               } else {
                  var3.appendChild(var5);
               }

               this.setNodeValue(var2.substring(0, var1));
               return var5;
            }
         } else {
            throw this.createDOMException((short)1, "offset", new Object[]{new Integer(var1)});
         }
      }
   }

   protected Node getPreviousLogicallyAdjacentTextNode(Node var1) {
      Node var2 = var1.getPreviousSibling();

      for(Node var3 = var1.getParentNode(); var2 == null && var3 != null && var3.getNodeType() == 5; var2 = var2.getPreviousSibling()) {
         var2 = var3;
         var3 = var3.getParentNode();
      }

      while(var2 != null && var2.getNodeType() == 5) {
         var2 = var2.getLastChild();
      }

      if (var2 == null) {
         return null;
      } else {
         short var4 = var2.getNodeType();
         return var4 != 3 && var4 != 4 ? null : var2;
      }
   }

   protected Node getNextLogicallyAdjacentTextNode(Node var1) {
      Node var2 = var1.getNextSibling();

      for(Node var3 = var1.getParentNode(); var2 == null && var3 != null && var3.getNodeType() == 5; var2 = var2.getNextSibling()) {
         var2 = var3;
         var3 = var3.getParentNode();
      }

      while(var2 != null && var2.getNodeType() == 5) {
         var2 = var2.getFirstChild();
      }

      if (var2 == null) {
         return null;
      } else {
         short var4 = var2.getNodeType();
         return var4 != 3 && var4 != 4 ? null : var2;
      }
   }

   public String getWholeText() {
      StringBuffer var1 = new StringBuffer();

      for(Object var2 = this; var2 != null; var2 = this.getPreviousLogicallyAdjacentTextNode((Node)var2)) {
         var1.insert(0, ((Node)var2).getNodeValue());
      }

      for(Node var3 = this.getNextLogicallyAdjacentTextNode(this); var3 != null; var3 = this.getNextLogicallyAdjacentTextNode(var3)) {
         var1.append(var3.getNodeValue());
      }

      return var1.toString();
   }

   public boolean isElementContentWhitespace() {
      int var1 = this.nodeValue.length();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (!XMLUtilities.isXMLSpace(this.nodeValue.charAt(var2))) {
            return false;
         }
      }

      Node var4 = this.getParentNode();
      if (var4.getNodeType() == 1) {
         String var3 = XMLSupport.getXMLSpace((Element)var4);
         return !var3.equals("preserve");
      } else {
         return true;
      }
   }

   public Text replaceWholeText(String var1) throws DOMException {
      Node var2;
      AbstractNode var3;
      for(var2 = this.getPreviousLogicallyAdjacentTextNode(this); var2 != null; var2 = this.getPreviousLogicallyAdjacentTextNode(var2)) {
         var3 = (AbstractNode)var2;
         if (var3.isReadonly()) {
            throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
         }
      }

      for(var2 = this.getNextLogicallyAdjacentTextNode(this); var2 != null; var2 = this.getNextLogicallyAdjacentTextNode(var2)) {
         var3 = (AbstractNode)var2;
         if (var3.isReadonly()) {
            throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
         }
      }

      var2 = this.getParentNode();

      Node var4;
      for(var4 = this.getPreviousLogicallyAdjacentTextNode(this); var4 != null; var4 = this.getPreviousLogicallyAdjacentTextNode(var4)) {
         var2.removeChild(var4);
      }

      for(var4 = this.getNextLogicallyAdjacentTextNode(this); var4 != null; var4 = this.getNextLogicallyAdjacentTextNode(var4)) {
         var2.removeChild(var4);
      }

      if (this.isReadonly()) {
         Text var5 = this.createTextNode(var1);
         var2.replaceChild(var5, this);
         return var5;
      } else {
         this.setNodeValue(var1);
         return this;
      }
   }

   public String getTextContent() {
      return this.isElementContentWhitespace() ? "" : this.getNodeValue();
   }

   protected abstract Text createTextNode(String var1);
}
