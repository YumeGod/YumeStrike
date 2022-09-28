package report;

import common.CommonUtils;
import common.RegexParser;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements ReportElement {
   protected List elements = new LinkedList();
   protected Document parent = null;

   public static boolean isAllowed(char var0) {
      if (var0 != '\t' && var0 != '\n' && var0 != '\r') {
         if (var0 >= ' ' && var0 <= '\ud7ff') {
            return true;
         } else if (var0 >= '\ue000' && var0 <= '�') {
            return true;
         } else {
            return var0 >= 65536 && var0 <= 1114111;
         }
      } else {
         return true;
      }
   }

   public static String fixText(String var0) {
      StringBuffer var1 = new StringBuffer(var0.length() * 2);
      char[] var2 = var0.toCharArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] == '&') {
            var1.append("&amp;");
         } else if (var2[var3] == '<') {
            var1.append("&lt;");
         } else if (var2[var3] == '>') {
            var1.append("&gt;");
         } else if (var2[var3] == '"') {
            var1.append("&quot;");
         } else if (var2[var3] == '\'') {
            var1.append("&apos;");
         } else if (isAllowed(var2[var3])) {
            if (var2[var3] > 255) {
               String var4 = CommonUtils.toHex((long)var2[var3]);
               String var5 = CommonUtils.padr(var4, "0", 4);
               var1.append("&#x");
               var1.append(var5);
               var1.append(";");
            } else {
               var1.append(var2[var3]);
            }
         }
      }

      return var1.toString();
   }

   public Content(Document var1) {
      this.parent = var1;
   }

   public void h1(String var1) {
      this.h1(var1, var1, "left");
   }

   public void h1(String var1, String var2, String var3) {
      var1 = fixText(var1);
      StringBuffer var4 = new StringBuffer();
      var4.append("<fo:block font-size=\"18pt\"\n");
      var4.append("\t\tfont-family=\"sans-serif\"\n");
      var4.append("\t\tid=\"" + this.parent.register(var2) + "\"\n");
      var4.append("\t\tfont-weight=\"bold\"\n");
      var4.append("\t\tline-height=\"24pt\"\n");
      var4.append("\t\tspace-after.optimum=\"15pt\"\n");
      var4.append("\t\tcolor=\"black\"\n");
      var4.append("\t\ttext-align=\"" + var3 + "\"\n");
      var4.append("\t\tpadding-top=\"12pt\">\n");
      var4.append("\t" + var1 + "\n");
      var4.append("</fo:block>");
      this.elements.add(new Piece(var4.toString()));
   }

   public void h2(String var1) {
      this.h2(var1, var1);
   }

   public void h2(String var1, String var2) {
      var1 = fixText(var1);
      StringBuffer var3 = new StringBuffer();
      var3.append("<fo:block font-size=\"15pt\"\n");
      var3.append("\t\tfont-family=\"sans-serif\"\n");
      var3.append("\t\tfont-weight=\"bold\"\n");
      var3.append("\t\tid=\"" + this.parent.register(var2) + "\"\n");
      var3.append("\t\tline-height=\"24pt\"\n");
      var3.append("\t\tspace-after.optimum=\"15pt\"\n");
      var3.append("\t\tcolor=\"black\"\n");
      var3.append("\t\ttext-align=\"left\"\n");
      var3.append("\t\tpadding-top=\"6pt\"\n");
      var3.append("\t\tpadding-bottom=\"6pt\"\n");
      var3.append("\t\tmargin-bottom=\"0\">\n");
      var3.append("\t<fo:inline text-decoration=\"underline\">" + var1 + "</fo:inline>\n");
      var3.append("</fo:block>");
      this.elements.add(new Piece(var3.toString()));
   }

   public void img(String var1, String var2) {
      this.elements.add(new Piece("<fo:external-graphic src=\"" + var1 + "\" content-width=\"" + var2 + "\" />"));
   }

   public void h2_img(BufferedImage var1, String var2) {
      this.h2_img(var1, var2, var2);
   }

   public void h2_img(BufferedImage var1, String var2, String var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append("<fo:table border-separation=\"0\" margin-top=\"4pt\" margin-bottom=\"8pt\" width=\"100%\">\n");
      var4.append("\t<fo:table-body>\n");
      var4.append("\t\t<fo:table-row>\n");
      var4.append("\t\t\t<fo:table-cell display-align=\"after\" width=\"0.6in\">\n");
      var4.append("\t\t\t\t<fo:block padding=\"0\" margin=\"0\">\n");
      this.elements.add(new Piece(var4.toString()));
      this.img(ReportUtils.image((RenderedImage)var1), "0.5in");
      var4 = new StringBuffer();
      var4.append("\t\t\t\t</fo:block>\n");
      var4.append("\t\t\t</fo:table-cell>\n");
      var4.append("\t\t\t<fo:table-cell display-align=\"center\" width=\"6in\">\n");
      this.elements.add(new Piece(var4.toString()));
      this.h2(var2, var3);
      var4 = new StringBuffer();
      var4.append("\t\t\t</fo:table-cell>\n");
      var4.append("\t\t</fo:table-row>\n");
      var4.append("\t</fo:table-body>\n");
      var4.append("</fo:table>\n");
      this.elements.add(new Piece(var4.toString()));
   }

   public void h3(String var1) {
      var1 = fixText(var1);
      StringBuffer var2 = new StringBuffer();
      var2.append("<fo:block font-size=\"14pt\"\n");
      var2.append("\t\tfont-family=\"sans-serif\"\n");
      var2.append("\t\tfont-weight=\"bold\"\n");
      var2.append("\t\tline-height=\"24pt\"\n");
      var2.append("\t\tspace-after.optimum=\"15pt\"\n");
      var2.append("\t\tcolor=\"black\"\n");
      var2.append("\t\ttext-align=\"left\"\n");
      var2.append("\t\tpadding-top=\"6pt\"\n");
      var2.append("\t\tpadding-bottom=\"6pt\"\n");
      var2.append("\t\tmargin-bottom=\"0\">\n");
      var2.append("\t" + var1 + "\n");
      var2.append("</fo:block>");
      this.elements.add(new Piece(var2.toString()));
   }

   public void kvtable(Map var1) {
      this.elements.add(new KVTable(var1));
   }

   public Content block(String var1) {
      FoBlock var2 = new FoBlock(this.parent, var1);
      this.elements.add(var2);
      return var2;
   }

   public Content string() {
      Content var1 = new Content(this.parent);
      return var1;
   }

   public Content output(String var1) {
      Output var2 = new Output(this.parent, var1);
      this.elements.add(var2);
      return var2;
   }

   public Content nobreak() {
      NoBreak var1 = new NoBreak(this.parent);
      this.elements.add(var1);
      return var1;
   }

   public void list(List var1) {
      Content var2 = this.ul();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 != null) {
            var2.li().text(var4.toString());
         }
      }

   }

   public void list_formatted(List var1) {
      Content var2 = this.ul();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (var3.next() + "").trim();
         RegexParser var5 = new RegexParser(var4);
         if (var5.matches("'''(.*?)'''(.*?)")) {
            Content var6 = var2.li();
            var6.b(var5.group(1));
            var6.text(var5.group(2));
         } else if (!"".equals(var4)) {
            var2.li().text(var4);
         }
      }

   }

   public void link(String var1, String var2) {
      var1 = fixText(var1);
      this.elements.add(new Piece(ReportUtils.a(var1, var2)));
   }

   public void link_bullet(String var1, String var2) {
      Content var3 = this.ul();
      var3.li().link(var1, var2);
   }

   public Content li() {
      ListItem var1 = new ListItem(this.parent);
      this.elements.add(var1);
      return var1;
   }

   public Content ul() {
      UnorderedList var1 = new UnorderedList(this.parent);
      this.elements.add(var1);
      return var1;
   }

   public void b(String var1) {
      var1 = fixText(var1);
      this.elements.add(new Piece("<fo:inline font-weight=\"bold\">" + var1 + "</fo:inline>"));
   }

   public void text(String var1) {
      var1 = fixText(var1);
      this.elements.add(new Piece("<fo:inline>" + var1 + "</fo:inline>"));
   }

   public void color(String var1, String var2) {
      var1 = fixText(var1);
      this.elements.add(new Piece("<fo:inline color=\"" + var2 + "\">" + var1 + "</fo:inline>"));
   }

   public void color2(String var1, String var2, String var3) {
      var1 = fixText(var1);
      this.elements.add(new Piece("<fo:inline color=\"" + var2 + "\" background-color=\"" + var3 + "\">" + var1 + "</fo:inline>"));
   }

   public void h4(String var1, String var2) {
      var1 = fixText(var1);
      this.elements.add(new Piece("<fo:block font-size=\"12pt\" font-family=\"sans-serif\" font-weight=\"bold\" color=\"black\" text-align=\"" + var2 + "\">" + var1 + "</fo:block>"));
   }

   public void p(String var1, String var2) {
      var1 = fixText(var1);
      this.elements.add(new Piece("<fo:block font-size=\"12pt\" font-family=\"sans-serif\" font-weight=\"normal\" color=\"black\" text-align=\"" + var2 + "\">" + var1 + "</fo:block>"));
   }

   public void br() {
      this.elements.add(new Piece("<fo:block font-size=\"12pt\" font-family=\"sans-serif\" font-weight=\"normal\" color=\"black\" text-align=\"left\">&#160;</fo:block>"));
   }

   public void table(List var1, List var2, List var3) {
      this.elements.add(new Table(var1, var2, var3));
   }

   public void layout(List var1, List var2, List var3) {
      this.elements.add(new Layout(var1, var2, var3));
   }

   public void ts() {
      String var1 = DateFormat.getDateInstance(2).format(new Date());
      this.elements.add(new Piece("<fo:block font-size=\"12pt\" padding-bottom=\"8pt\" font-family=\"sans-serif\" font-style=\"italic\" font-weight=\"normal\" color=\"black\" text-align=\"left\">" + var1 + "</fo:block>"));
   }

   public void publish(StringBuffer var1) {
      ReportUtils.PublishAll(var1, this.elements);
   }

   public boolean isEmpty() {
      return this.elements.size() == 0;
   }

   public void bookmark(String var1) {
      this.parent.getBookmarks().bookmark(var1);
   }

   public void bookmark(String var1, String var2) {
      this.parent.getBookmarks().bookmark(var1, var2);
   }
}
