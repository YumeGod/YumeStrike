package org.apache.xerces.util;

import java.util.Hashtable;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMUtil {
   protected DOMUtil() {
   }

   public static void copyInto(Node var0, Node var1) throws DOMException {
      Document var2 = ((Node)var1).getOwnerDocument();
      boolean var3 = var2 instanceof DocumentImpl;
      Node var4 = var0;
      Node var5 = var0;
      Node var6 = var0;

      while(var6 != null) {
         Object var7;
         var7 = null;
         short var8 = var6.getNodeType();
         label45:
         switch (var8) {
            case 1:
               Element var9 = var2.createElement(var6.getNodeName());
               var7 = var9;
               NamedNodeMap var10 = var6.getAttributes();
               int var11 = var10.getLength();
               int var12 = 0;

               while(true) {
                  if (var12 >= var11) {
                     break label45;
                  }

                  Attr var13 = (Attr)var10.item(var12);
                  String var14 = var13.getNodeName();
                  String var15 = var13.getNodeValue();
                  var9.setAttribute(var14, var15);
                  if (var3 && !var13.getSpecified()) {
                     ((AttrImpl)var9.getAttributeNode(var14)).setSpecified(false);
                  }

                  ++var12;
               }
            case 2:
            case 6:
            default:
               throw new IllegalArgumentException("can't copy node type, " + var8 + " (" + ((Node)var7).getNodeName() + ')');
            case 3:
               var7 = var2.createTextNode(var6.getNodeValue());
               break;
            case 4:
               var7 = var2.createCDATASection(var6.getNodeValue());
               break;
            case 5:
               var7 = var2.createEntityReference(var6.getNodeName());
               break;
            case 7:
               var7 = var2.createProcessingInstruction(var6.getNodeName(), var6.getNodeValue());
               break;
            case 8:
               var7 = var2.createComment(var6.getNodeValue());
         }

         ((Node)var1).appendChild((Node)var7);
         if (var6.hasChildNodes()) {
            var5 = var6;
            var6 = var6.getFirstChild();
            var1 = var7;
         } else {
            for(var6 = var6.getNextSibling(); var6 == null && var5 != var4; var1 = ((Node)var1).getParentNode()) {
               var6 = var5.getNextSibling();
               var5 = var5.getParentNode();
            }
         }
      }

   }

   public static Element getFirstChildElement(Node var0) {
      for(Node var1 = var0.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public static Element getFirstVisibleChildElement(Node var0) {
      for(Node var1 = var0.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1 && !isHidden(var1)) {
            return (Element)var1;
         }
      }

      return null;
   }

   public static Element getFirstVisibleChildElement(Node var0, Hashtable var1) {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1 && !isHidden(var2, var1)) {
            return (Element)var2;
         }
      }

      return null;
   }

   public static Element getLastChildElement(Node var0) {
      for(Node var1 = var0.getLastChild(); var1 != null; var1 = var1.getPreviousSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public static Element getLastVisibleChildElement(Node var0) {
      for(Node var1 = var0.getLastChild(); var1 != null; var1 = var1.getPreviousSibling()) {
         if (var1.getNodeType() == 1 && !isHidden(var1)) {
            return (Element)var1;
         }
      }

      return null;
   }

   public static Element getLastVisibleChildElement(Node var0, Hashtable var1) {
      for(Node var2 = var0.getLastChild(); var2 != null; var2 = var2.getPreviousSibling()) {
         if (var2.getNodeType() == 1 && !isHidden(var2, var1)) {
            return (Element)var2;
         }
      }

      return null;
   }

   public static Element getNextSiblingElement(Node var0) {
      for(Node var1 = var0.getNextSibling(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public static Element getNextVisibleSiblingElement(Node var0) {
      for(Node var1 = var0.getNextSibling(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1 && !isHidden(var1)) {
            return (Element)var1;
         }
      }

      return null;
   }

   public static Element getNextVisibleSiblingElement(Node var0, Hashtable var1) {
      for(Node var2 = var0.getNextSibling(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1 && !isHidden(var2, var1)) {
            return (Element)var2;
         }
      }

      return null;
   }

   public static void setHidden(Node var0) {
      if (var0 instanceof NodeImpl) {
         ((NodeImpl)var0).setReadOnly(true, false);
      } else if (var0 instanceof org.apache.xerces.dom.NodeImpl) {
         ((org.apache.xerces.dom.NodeImpl)var0).setReadOnly(true, false);
      }

   }

   public static void setHidden(Node var0, Hashtable var1) {
      if (var0 instanceof NodeImpl) {
         ((NodeImpl)var0).setReadOnly(true, false);
      } else if (var0 instanceof org.apache.xerces.dom.NodeImpl) {
         ((org.apache.xerces.dom.NodeImpl)var0).setReadOnly(true, false);
      } else {
         var1.put(var0, "");
      }

   }

   public static void setVisible(Node var0) {
      if (var0 instanceof NodeImpl) {
         ((NodeImpl)var0).setReadOnly(false, false);
      } else if (var0 instanceof org.apache.xerces.dom.NodeImpl) {
         ((org.apache.xerces.dom.NodeImpl)var0).setReadOnly(false, false);
      }

   }

   public static void setVisible(Node var0, Hashtable var1) {
      if (var0 instanceof NodeImpl) {
         ((NodeImpl)var0).setReadOnly(false, false);
      } else if (var0 instanceof org.apache.xerces.dom.NodeImpl) {
         ((org.apache.xerces.dom.NodeImpl)var0).setReadOnly(false, false);
      } else {
         var1.remove(var0);
      }

   }

   public static boolean isHidden(Node var0) {
      if (var0 instanceof NodeImpl) {
         return ((NodeImpl)var0).getReadOnly();
      } else {
         return var0 instanceof org.apache.xerces.dom.NodeImpl ? ((org.apache.xerces.dom.NodeImpl)var0).getReadOnly() : false;
      }
   }

   public static boolean isHidden(Node var0, Hashtable var1) {
      if (var0 instanceof NodeImpl) {
         return ((NodeImpl)var0).getReadOnly();
      } else {
         return var0 instanceof org.apache.xerces.dom.NodeImpl ? ((org.apache.xerces.dom.NodeImpl)var0).getReadOnly() : var1.containsKey(var0);
      }
   }

   public static Element getFirstChildElement(Node var0, String var1) {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1 && var2.getNodeName().equals(var1)) {
            return (Element)var2;
         }
      }

      return null;
   }

   public static Element getLastChildElement(Node var0, String var1) {
      for(Node var2 = var0.getLastChild(); var2 != null; var2 = var2.getPreviousSibling()) {
         if (var2.getNodeType() == 1 && var2.getNodeName().equals(var1)) {
            return (Element)var2;
         }
      }

      return null;
   }

   public static Element getNextSiblingElement(Node var0, String var1) {
      for(Node var2 = var0.getNextSibling(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1 && var2.getNodeName().equals(var1)) {
            return (Element)var2;
         }
      }

      return null;
   }

   public static Element getFirstChildElementNS(Node var0, String var1, String var2) {
      for(Node var3 = var0.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1) {
            String var4 = var3.getNamespaceURI();
            if (var4 != null && var4.equals(var1) && var3.getLocalName().equals(var2)) {
               return (Element)var3;
            }
         }
      }

      return null;
   }

   public static Element getLastChildElementNS(Node var0, String var1, String var2) {
      for(Node var3 = var0.getLastChild(); var3 != null; var3 = var3.getPreviousSibling()) {
         if (var3.getNodeType() == 1) {
            String var4 = var3.getNamespaceURI();
            if (var4 != null && var4.equals(var1) && var3.getLocalName().equals(var2)) {
               return (Element)var3;
            }
         }
      }

      return null;
   }

   public static Element getNextSiblingElementNS(Node var0, String var1, String var2) {
      for(Node var3 = var0.getNextSibling(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1) {
            String var4 = var3.getNamespaceURI();
            if (var4 != null && var4.equals(var1) && var3.getLocalName().equals(var2)) {
               return (Element)var3;
            }
         }
      }

      return null;
   }

   public static Element getFirstChildElement(Node var0, String[] var1) {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var2.getNodeName().equals(var1[var3])) {
                  return (Element)var2;
               }
            }
         }
      }

      return null;
   }

   public static Element getLastChildElement(Node var0, String[] var1) {
      for(Node var2 = var0.getLastChild(); var2 != null; var2 = var2.getPreviousSibling()) {
         if (var2.getNodeType() == 1) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var2.getNodeName().equals(var1[var3])) {
                  return (Element)var2;
               }
            }
         }
      }

      return null;
   }

   public static Element getNextSiblingElement(Node var0, String[] var1) {
      for(Node var2 = var0.getNextSibling(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var2.getNodeName().equals(var1[var3])) {
                  return (Element)var2;
               }
            }
         }
      }

      return null;
   }

   public static Element getFirstChildElementNS(Node var0, String[][] var1) {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               String var4 = var2.getNamespaceURI();
               if (var4 != null && var4.equals(var1[var3][0]) && var2.getLocalName().equals(var1[var3][1])) {
                  return (Element)var2;
               }
            }
         }
      }

      return null;
   }

   public static Element getLastChildElementNS(Node var0, String[][] var1) {
      for(Node var2 = var0.getLastChild(); var2 != null; var2 = var2.getPreviousSibling()) {
         if (var2.getNodeType() == 1) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               String var4 = var2.getNamespaceURI();
               if (var4 != null && var4.equals(var1[var3][0]) && var2.getLocalName().equals(var1[var3][1])) {
                  return (Element)var2;
               }
            }
         }
      }

      return null;
   }

   public static Element getNextSiblingElementNS(Node var0, String[][] var1) {
      for(Node var2 = var0.getNextSibling(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 1) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               String var4 = var2.getNamespaceURI();
               if (var4 != null && var4.equals(var1[var3][0]) && var2.getLocalName().equals(var1[var3][1])) {
                  return (Element)var2;
               }
            }
         }
      }

      return null;
   }

   public static Element getFirstChildElement(Node var0, String var1, String var2, String var3) {
      for(Node var4 = var0.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            Element var5 = (Element)var4;
            if (var5.getNodeName().equals(var1) && var5.getAttribute(var2).equals(var3)) {
               return var5;
            }
         }
      }

      return null;
   }

   public static Element getLastChildElement(Node var0, String var1, String var2, String var3) {
      for(Node var4 = var0.getLastChild(); var4 != null; var4 = var4.getPreviousSibling()) {
         if (var4.getNodeType() == 1) {
            Element var5 = (Element)var4;
            if (var5.getNodeName().equals(var1) && var5.getAttribute(var2).equals(var3)) {
               return var5;
            }
         }
      }

      return null;
   }

   public static Element getNextSiblingElement(Node var0, String var1, String var2, String var3) {
      for(Node var4 = var0.getNextSibling(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            Element var5 = (Element)var4;
            if (var5.getNodeName().equals(var1) && var5.getAttribute(var2).equals(var3)) {
               return var5;
            }
         }
      }

      return null;
   }

   public static String getChildText(Node var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();

         for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            short var3 = var2.getNodeType();
            if (var3 == 3) {
               var1.append(var2.getNodeValue());
            } else if (var3 == 4) {
               var1.append(getChildText(var2));
            }
         }

         return var1.toString();
      }
   }

   public static String getName(Node var0) {
      return var0.getNodeName();
   }

   public static String getLocalName(Node var0) {
      String var1 = var0.getLocalName();
      return var1 != null ? var1 : var0.getNodeName();
   }

   public static Element getParent(Element var0) {
      Node var1 = var0.getParentNode();
      return var1 instanceof Element ? (Element)var1 : null;
   }

   public static Document getDocument(Node var0) {
      return var0.getOwnerDocument();
   }

   public static Element getRoot(Document var0) {
      return var0.getDocumentElement();
   }

   public static Attr getAttr(Element var0, String var1) {
      return var0.getAttributeNode(var1);
   }

   public static Attr getAttrNS(Element var0, String var1, String var2) {
      return var0.getAttributeNodeNS(var1, var2);
   }

   public static Attr[] getAttrs(Element var0) {
      NamedNodeMap var1 = var0.getAttributes();
      Attr[] var2 = new Attr[var1.getLength()];

      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         var2[var3] = (Attr)var1.item(var3);
      }

      return var2;
   }

   public static String getValue(Attr var0) {
      return var0.getValue();
   }

   public static String getAttrValue(Element var0, String var1) {
      return var0.getAttribute(var1);
   }

   public static String getAttrValueNS(Element var0, String var1, String var2) {
      return var0.getAttributeNS(var1, var2);
   }

   public static String getPrefix(Node var0) {
      return var0.getPrefix();
   }

   public static String getNamespaceURI(Node var0) {
      return var0.getNamespaceURI();
   }

   public static String getSyntheticAnnotation(Node var0) {
      return var0 instanceof ElementImpl ? ((ElementImpl)var0).getSyntheticAnnotation() : null;
   }
}
