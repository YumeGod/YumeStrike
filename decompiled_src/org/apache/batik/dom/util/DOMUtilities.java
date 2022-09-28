package org.apache.batik.dom.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.batik.xml.XMLUtilities;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMUtilities extends XMLUtilities {
   protected static final String[] LOCK_STRINGS = new String[]{"", "CapsLock", "NumLock", "NumLock CapsLock", "Scroll", "Scroll CapsLock", "Scroll NumLock", "Scroll NumLock CapsLock", "KanaMode", "KanaMode CapsLock", "KanaMode NumLock", "KanaMode NumLock CapsLock", "KanaMode Scroll", "KanaMode Scroll CapsLock", "KanaMode Scroll NumLock", "KanaMode Scroll NumLock CapsLock"};
   protected static final String[] MODIFIER_STRINGS = new String[]{"", "Shift", "Control", "Control Shift", "Meta", "Meta Shift", "Control Meta", "Control Meta Shift", "Alt", "Alt Shift", "Alt Control", "Alt Control Shift", "Alt Meta", "Alt Meta Shift", "Alt Control Meta", "Alt Control Meta Shift", "AltGraph", "AltGraph Shift", "AltGraph Control", "AltGraph Control Shift", "AltGraph Meta", "AltGraph Meta Shift", "AltGraph Control Meta", "AltGraph Control Meta Shift", "Alt AltGraph", "Alt AltGraph Shift", "Alt AltGraph Control", "Alt AltGraph Control Shift", "Alt AltGraph Meta", "Alt AltGraph Meta Shift", "Alt AltGraph Control Meta", "Alt AltGraph Control Meta Shift"};

   protected DOMUtilities() {
   }

   public static void writeDocument(Document var0, Writer var1) throws IOException {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         writeNode(var2, var1);
      }

   }

   public static void writeNode(Node var0, Writer var1) throws IOException {
      switch (var0.getNodeType()) {
         case 1:
            var1.write("<");
            var1.write(var0.getNodeName());
            if (var0.hasAttributes()) {
               NamedNodeMap var2 = var0.getAttributes();
               int var7 = var2.getLength();

               for(int var8 = 0; var8 < var7; ++var8) {
                  Attr var9 = (Attr)var2.item(var8);
                  var1.write(" ");
                  var1.write(var9.getNodeName());
                  var1.write("=\"");
                  var1.write(contentToString(var9.getNodeValue()));
                  var1.write("\"");
               }
            }

            Node var6 = var0.getFirstChild();
            if (var6 != null) {
               var1.write(">");

               while(var6 != null) {
                  writeNode(var6, var1);
                  var6 = var6.getNextSibling();
               }

               var1.write("</");
               var1.write(var0.getNodeName());
               var1.write(">");
            } else {
               var1.write("/>");
            }
            break;
         case 2:
         case 6:
         case 9:
         default:
            throw new IOException("Unknown DOM node type " + var0.getNodeType());
         case 3:
            var1.write(contentToString(var0.getNodeValue()));
            break;
         case 4:
            var1.write("<![CDATA[");
            var1.write(var0.getNodeValue());
            var1.write("]]>");
            break;
         case 5:
            var1.write("&");
            var1.write(var0.getNodeName());
            var1.write(";");
            break;
         case 7:
            var1.write("<?");
            var1.write(var0.getNodeName());
            var1.write(" ");
            var1.write(var0.getNodeValue());
            var1.write("?>");
            break;
         case 8:
            var1.write("<!--");
            var1.write(var0.getNodeValue());
            var1.write("-->");
            break;
         case 10:
            DocumentType var3 = (DocumentType)var0;
            var1.write("<!DOCTYPE ");
            var1.write(var0.getOwnerDocument().getDocumentElement().getNodeName());
            String var4 = var3.getPublicId();
            if (var4 != null) {
               var1.write(" PUBLIC \"" + var3.getNodeName() + "\" \"" + var4 + "\">");
            } else {
               String var5 = var3.getSystemId();
               if (var5 != null) {
                  var1.write(" SYSTEM \"" + var5 + "\">");
               }
            }
      }

   }

   public static String getXML(Node var0) {
      StringWriter var1 = new StringWriter();

      try {
         writeNode(var0, var1);
         var1.close();
      } catch (IOException var3) {
         return "";
      }

      return var1.toString();
   }

   public static String contentToString(String var0) {
      StringBuffer var1 = new StringBuffer(var0.length());

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         switch (var3) {
            case '"':
               var1.append("&quot;");
               break;
            case '&':
               var1.append("&amp;");
               break;
            case '\'':
               var1.append("&apos;");
               break;
            case '<':
               var1.append("&lt;");
               break;
            case '>':
               var1.append("&gt;");
               break;
            default:
               var1.append(var3);
         }
      }

      return var1.toString();
   }

   public static int getChildIndex(Node var0, Node var1) {
      return var0 != null && var0.getParentNode() == var1 && var0.getParentNode() != null ? getChildIndex(var0) : -1;
   }

   public static int getChildIndex(Node var0) {
      NodeList var1 = var0.getParentNode().getChildNodes();

      for(int var2 = 0; var2 < var1.getLength(); ++var2) {
         Node var3 = var1.item(var2);
         if (var3 == var0) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean isAnyNodeAncestorOf(ArrayList var0, Node var1) {
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         Node var4 = (Node)var0.get(var3);
         if (isAncestorOf(var4, var1)) {
            return true;
         }
      }

      return false;
   }

   public static boolean isAncestorOf(Node var0, Node var1) {
      if (var0 != null && var1 != null) {
         for(Node var2 = var1.getParentNode(); var2 != null; var2 = var2.getParentNode()) {
            if (var2 == var0) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean isParentOf(Node var0, Node var1) {
      return var0 != null && var1 != null && var0.getParentNode() == var1;
   }

   public static boolean canAppend(Node var0, Node var1) {
      return var0 != null && var1 != null && var0 != var1 && !isAncestorOf(var0, var1);
   }

   public static boolean canAppendAny(ArrayList var0, Node var1) {
      if (!canHaveChildren(var1)) {
         return false;
      } else {
         int var2 = var0.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            Node var4 = (Node)var0.get(var3);
            if (canAppend(var4, var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean canHaveChildren(Node var0) {
      if (var0 == null) {
         return false;
      } else {
         switch (var0.getNodeType()) {
            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
               return false;
            case 5:
            case 6:
            default:
               return true;
         }
      }
   }

   public static Node parseXML(String var0, Document var1, String var2, Map var3, String var4, SAXDocumentFactory var5) {
      String var6 = "";
      String var7 = "";
      if (var4 != null) {
         var6 = "<" + var4;
         if (var3 != null) {
            var6 = var6 + " ";
            Set var8 = var3.keySet();

            String var10;
            String var11;
            for(Iterator var9 = var8.iterator(); var9.hasNext(); var6 = var6 + var10 + "=\"" + var11 + "\" ") {
               var10 = (String)var9.next();
               var11 = (String)var3.get(var10);
            }
         }

         var6 = var6 + ">";
         var7 = var7 + "</" + var4 + ">";
      }

      if (var6.trim().length() == 0 && var7.trim().length() == 0) {
         try {
            Document var16 = var5.createDocument(var2, (Reader)(new StringReader(var0)));
            if (var1 == null) {
               return var16;
            }

            DocumentFragment var18 = var1.createDocumentFragment();
            var18.appendChild(var1.importNode(var16.getDocumentElement(), true));
            return var18;
         } catch (Exception var14) {
         }
      }

      StringBuffer var15 = new StringBuffer(var6.length() + var0.length() + var7.length());
      var15.append(var6);
      var15.append(var0);
      var15.append(var7);
      String var17 = var15.toString();

      try {
         Document var19 = var5.createDocument(var2, (Reader)(new StringReader(var17)));
         if (var1 == null) {
            return var19;
         }

         for(Node var20 = var19.getDocumentElement().getFirstChild(); var20 != null; var20 = var20.getNextSibling()) {
            if (var20.getNodeType() == 1) {
               var20 = var1.importNode(var20, true);
               DocumentFragment var12 = var1.createDocumentFragment();
               var12.appendChild(var20);
               return var12;
            }
         }
      } catch (Exception var13) {
      }

      return null;
   }

   public static Document deepCloneDocument(Document var0, DOMImplementation var1) {
      Element var2 = var0.getDocumentElement();
      Document var3 = var1.createDocument(var2.getNamespaceURI(), var2.getNodeName(), (DocumentType)null);
      Element var4 = var3.getDocumentElement();
      boolean var5 = true;

      for(Node var6 = var0.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
         if (var6 != var2) {
            if (var6.getNodeType() != 10) {
               if (var5) {
                  var3.insertBefore(var3.importNode(var6, true), var4);
               } else {
                  var3.appendChild(var3.importNode(var6, true));
               }
            }
         } else {
            var5 = false;
            if (var2.hasAttributes()) {
               NamedNodeMap var7 = var2.getAttributes();
               int var8 = var7.getLength();

               for(int var9 = 0; var9 < var8; ++var9) {
                  var4.setAttributeNode((Attr)var3.importNode(var7.item(var9), true));
               }
            }

            for(Node var10 = var2.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
               var4.appendChild(var3.importNode(var10, true));
            }
         }
      }

      return var3;
   }

   public static boolean isValidName(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         char var2 = var0.charAt(0);
         int var3 = var2 / 32;
         int var4 = var2 % 32;
         if ((NAME_FIRST_CHARACTER[var3] & 1 << var4) == 0) {
            return false;
         } else {
            for(int var5 = 1; var5 < var1; ++var5) {
               var2 = var0.charAt(var5);
               var3 = var2 / 32;
               var4 = var2 % 32;
               if ((NAME_CHARACTER[var3] & 1 << var4) == 0) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static boolean isValidName11(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         char var2 = var0.charAt(0);
         int var3 = var2 / 32;
         int var4 = var2 % 32;
         if ((NAME11_FIRST_CHARACTER[var3] & 1 << var4) == 0) {
            return false;
         } else {
            for(int var5 = 1; var5 < var1; ++var5) {
               var2 = var0.charAt(var5);
               var3 = var2 / 32;
               var4 = var2 % 32;
               if ((NAME11_CHARACTER[var3] & 1 << var4) == 0) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static boolean isValidPrefix(String var0) {
      return var0.indexOf(58) == -1;
   }

   public static String getPrefix(String var0) {
      int var1 = var0.indexOf(58);
      return var1 != -1 && var1 != var0.length() - 1 ? var0.substring(0, var1) : null;
   }

   public static String getLocalName(String var0) {
      int var1 = var0.indexOf(58);
      return var1 != -1 && var1 != var0.length() - 1 ? var0.substring(var1 + 1) : var0;
   }

   public static void parseStyleSheetPIData(String var0, HashTable var1) {
      char var2;
      int var3;
      for(var3 = 0; var3 < var0.length(); ++var3) {
         var2 = var0.charAt(var3);
         if (!XMLUtilities.isXMLSpace(var2)) {
            break;
         }
      }

      while(var3 < var0.length()) {
         var2 = var0.charAt(var3);
         int var4 = var2 / 32;
         int var5 = var2 % 32;
         if ((NAME_FIRST_CHARACTER[var4] & 1 << var5) == 0) {
            throw new DOMException((short)5, "Wrong name initial:  " + var2);
         }

         StringBuffer var6 = new StringBuffer();
         var6.append(var2);

         while(true) {
            ++var3;
            if (var3 >= var0.length()) {
               break;
            }

            var2 = var0.charAt(var3);
            var4 = var2 / 32;
            var5 = var2 % 32;
            if ((NAME_CHARACTER[var4] & 1 << var5) == 0) {
               break;
            }

            var6.append(var2);
         }

         if (var3 >= var0.length()) {
            throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
         }

         while(var3 < var0.length()) {
            var2 = var0.charAt(var3);
            if (!XMLUtilities.isXMLSpace(var2)) {
               break;
            }

            ++var3;
         }

         if (var3 >= var0.length()) {
            throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
         }

         if (var0.charAt(var3) != '=') {
            throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
         }

         ++var3;

         while(var3 < var0.length()) {
            var2 = var0.charAt(var3);
            if (!XMLUtilities.isXMLSpace(var2)) {
               break;
            }

            ++var3;
         }

         if (var3 >= var0.length()) {
            throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
         }

         var2 = var0.charAt(var3);
         ++var3;
         StringBuffer var7 = new StringBuffer();
         if (var2 == '\'') {
            while(var3 < var0.length()) {
               var2 = var0.charAt(var3);
               if (var2 == '\'') {
                  break;
               }

               var7.append(var2);
               ++var3;
            }

            if (var3 >= var0.length()) {
               throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
            }
         } else {
            if (var2 != '"') {
               throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
            }

            while(var3 < var0.length()) {
               var2 = var0.charAt(var3);
               if (var2 == '"') {
                  break;
               }

               var7.append(var2);
               ++var3;
            }

            if (var3 >= var0.length()) {
               throw new DOMException((short)12, "Wrong xml-stylesheet data: " + var0);
            }
         }

         var1.put(var6.toString().intern(), var7.toString());
         ++var3;

         while(var3 < var0.length()) {
            var2 = var0.charAt(var3);
            if (!XMLUtilities.isXMLSpace(var2)) {
               break;
            }

            ++var3;
         }
      }

   }

   public static String getModifiersList(int var0, int var1) {
      return DOMUtilitiesSupport.getModifiersList(var0, var1);
   }
}
