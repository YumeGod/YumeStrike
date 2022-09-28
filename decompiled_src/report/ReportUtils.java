package report;

import aggressor.Prefs;
import common.CommonUtils;
import common.MudgeSanity;
import encoders.Base64;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

public class ReportUtils {
   public static String accent() {
      return Prefs.getPreferences().getString("reporting.accent.color", "#003562");
   }

   public static String a(String var0, String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("<fo:basic-link color=\"" + accent() + "\" text-decoration=\"underline\" external-destination=\"" + var1 + "\">");
      var2.append("<fo:inline>");
      var2.append(var0);
      var2.append("</fo:inline>");
      var2.append("</fo:basic-link>");
      return var2.toString();
   }

   public static String b(String var0) {
      return "<fo:inline font-weight=\"bold\">" + var0 + "</fo:inline>";
   }

   public static String br() {
      return "<fo:block> </fo:block>";
   }

   public static String u(String var0) {
      return "<fo:inline text-decoration=\"underline\">" + var0 + "</fo:inline>";
   }

   public static String i(String var0) {
      return "<fo:inline font-style=\"italic\">" + var0 + "</fo:inline>";
   }

   public static String code(String var0) {
      return "<fo:inline font-weight=\"monospace\">" + var0 + "</fo:inline>";
   }

   public static String logo() {
      String var0 = Prefs.getPreferences().getString("reporting.header_image.file", "");
      if ("".equals(var0)) {
         byte[] var1 = CommonUtils.readResource("resources/fso/logo2.png");
         return "url(&#34;data:image/png;base64," + Base64.encode(var1) + "&#xA;&#34;)";
      } else {
         return var0;
      }
   }

   public static String image(RenderedImage var0) {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream(524288);
         ImageIO.write(var0, "png", var1);
         return "url(&#34;data:image/png;base64," + Base64.encode(var1.toByteArray()) + "&#xA;&#34;)";
      } catch (Exception var2) {
         MudgeSanity.logException("could not transform image", var2, false);
         return "";
      }
   }

   public static String image(String var0) {
      byte[] var1 = CommonUtils.readResource(var0);
      return "url(&#34;data:image/png;base64," + Base64.encode(var1) + "&#xA;&#34;)";
   }

   public static String ColumnWidth(String var0) {
      return "\t<fo:table-column column-width=\"" + var0 + "\" />";
   }

   public static void PublishAll(StringBuffer var0, List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ReportElement var3 = (ReportElement)var2.next();
         var3.publish(var0);
      }

   }
}
