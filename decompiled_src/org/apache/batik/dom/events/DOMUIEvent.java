package org.apache.batik.dom.events;

import java.util.ArrayList;
import org.apache.batik.xml.XMLUtilities;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

public class DOMUIEvent extends AbstractEvent implements UIEvent {
   private AbstractView view;
   private int detail;

   public AbstractView getView() {
      return this.view;
   }

   public int getDetail() {
      return this.detail;
   }

   public void initUIEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5) {
      this.initEvent(var1, var2, var3);
      this.view = var4;
      this.detail = var5;
   }

   public void initUIEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, int var6) {
      this.initEventNS(var1, var2, var3, var4);
      this.view = var5;
      this.detail = var6;
   }

   protected String[] split(String var1) {
      ArrayList var2 = new ArrayList(8);
      int var4 = 0;
      int var5 = var1.length();

      while(true) {
         char var6;
         do {
            if (var4 >= var5) {
               return (String[])var2.toArray(new String[var2.size()]);
            }

            var6 = var1.charAt(var4++);
         } while(XMLUtilities.isXMLSpace(var6));

         StringBuffer var3 = new StringBuffer();
         var3.append(var6);

         while(var4 < var5) {
            var6 = var1.charAt(var4++);
            if (XMLUtilities.isXMLSpace(var6)) {
               var2.add(var3.toString());
               break;
            }

            var3.append(var6);
         }

         if (var4 == var5) {
            var2.add(var3.toString());
         }
      }
   }
}
