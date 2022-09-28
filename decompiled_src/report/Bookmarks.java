package report;

import common.CommonUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Bookmarks implements ReportElement {
   protected LinkedHashMap bookmarks = new LinkedHashMap();
   protected Map references = new HashMap();

   public String register(String var1) {
      String var2 = CommonUtils.garbage(var1);
      this.references.put(var1, var2);
      return var2;
   }

   public boolean isRegistered(String var1) {
      if (this.references.get(var1) == null) {
         return false;
      } else {
         return !"".equals(this.references.get(var1));
      }
   }

   public void bookmark(String var1) {
      this.bookmarks.put(var1, new LinkedList());
   }

   public void bookmark(String var1, String var2) {
      LinkedList var3 = (LinkedList)this.bookmarks.get(var1);
      if (var3 == null) {
         this.bookmarks.put(var1, new LinkedList());
         this.bookmark(var1, var2);
      } else {
         var3.add(var2);
      }

   }

   public void cleanup() {
      Iterator var1 = this.bookmarks.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         String var3 = (String)var2.getKey();
         LinkedList var4 = (LinkedList)var2.getValue();
         if (!this.isRegistered(var3) && var4.size() == 0) {
            var1.remove();
         }

         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (!this.isRegistered(var6)) {
               var5.remove();
            }
         }
      }

   }

   public void publish(StringBuffer var1) {
      this.cleanup();
      if (this.bookmarks.size() != 0) {
         var1.append("<fo:bookmark-tree>\n");
         Iterator var2 = this.bookmarks.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            String var4 = (String)var3.getKey();
            LinkedList var5 = (LinkedList)var3.getValue();
            var1.append("\t<fo:bookmark internal-destination=\"" + this.references.get(var4) + "\">\n");
            var1.append("\t\t<fo:bookmark-title>" + Content.fixText(var4) + "</fo:bookmark-title>\n");
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               var1.append("\t\t<fo:bookmark internal-destination=\"" + this.references.get(var7) + "\">\n");
               var1.append("\t\t\t<fo:bookmark-title>" + Content.fixText(var7) + "</fo:bookmark-title>\n");
               var1.append("\t</fo:bookmark>\n");
            }

            var1.append("\t</fo:bookmark>\n");
         }

         var1.append("</fo:bookmark-tree>\n");
      }
   }
}
