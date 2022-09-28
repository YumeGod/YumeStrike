package org.apache.html.dom;

import java.util.Locale;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLElementImpl extends ElementImpl implements HTMLElement {
   private static final long serialVersionUID = 3833188025499792690L;

   HTMLElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2.toUpperCase(Locale.ENGLISH));
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getTitle() {
      return this.getAttribute("title");
   }

   public void setTitle(String var1) {
      this.setAttribute("title", var1);
   }

   public String getLang() {
      return this.getAttribute("lang");
   }

   public void setLang(String var1) {
      this.setAttribute("lang", var1);
   }

   public String getDir() {
      return this.getAttribute("dir");
   }

   public void setDir(String var1) {
      this.setAttribute("dir", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   int getInteger(String var1) {
      try {
         return Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }

   boolean getBinary(String var1) {
      return this.getAttributeNode(var1) != null;
   }

   void setAttribute(String var1, boolean var2) {
      if (var2) {
         this.setAttribute(var1, var1);
      } else {
         this.removeAttribute(var1);
      }

   }

   public Attr getAttributeNode(String var1) {
      return super.getAttributeNode(var1.toLowerCase(Locale.ENGLISH));
   }

   public Attr getAttributeNodeNS(String var1, String var2) {
      return var1 != null && var1.length() > 0 ? super.getAttributeNodeNS(var1, var2) : super.getAttributeNode(var2.toLowerCase(Locale.ENGLISH));
   }

   public String getAttribute(String var1) {
      return super.getAttribute(var1.toLowerCase(Locale.ENGLISH));
   }

   public String getAttributeNS(String var1, String var2) {
      return var1 != null && var1.length() > 0 ? super.getAttributeNS(var1, var2) : super.getAttribute(var2.toLowerCase(Locale.ENGLISH));
   }

   public final NodeList getElementsByTagName(String var1) {
      return super.getElementsByTagName(var1.toUpperCase(Locale.ENGLISH));
   }

   public final NodeList getElementsByTagNameNS(String var1, String var2) {
      return var1 != null && var1.length() > 0 ? super.getElementsByTagNameNS(var1, var2.toUpperCase(Locale.ENGLISH)) : super.getElementsByTagName(var2.toUpperCase(Locale.ENGLISH));
   }

   String capitalize(String var1) {
      char[] var2 = var1.toCharArray();
      if (var2.length <= 0) {
         return var1;
      } else {
         var2[0] = Character.toUpperCase(var2[0]);

         for(int var3 = 1; var3 < var2.length; ++var3) {
            var2[var3] = Character.toLowerCase(var2[var3]);
         }

         return String.valueOf(var2);
      }
   }

   String getCapitalized(String var1) {
      String var2 = this.getAttribute(var1);
      if (var2 != null) {
         char[] var3 = var2.toCharArray();
         if (var3.length > 0) {
            var3[0] = Character.toUpperCase(var3[0]);

            for(int var4 = 1; var4 < var3.length; ++var4) {
               var3[var4] = Character.toLowerCase(var3[var4]);
            }

            return String.valueOf(var3);
         }
      }

      return var2;
   }

   public HTMLFormElement getForm() {
      for(Node var1 = this.getParentNode(); var1 != null; var1 = var1.getParentNode()) {
         if (var1 instanceof HTMLFormElement) {
            return (HTMLFormElement)var1;
         }
      }

      return null;
   }
}
