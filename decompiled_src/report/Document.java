package report;

import com.xmlmind.fo.converter.Converter;
import com.xmlmind.fo.converter.OutputDestination;
import common.AObject;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.xml.sax.InputSource;

public class Document extends AObject implements ReportElement {
   public static final int ORIENTATION_PORTRAIT = 0;
   public static final int ORIENTATION_LANDSCAPE = 1;
   protected int orientation = 0;
   protected List pages = new LinkedList();
   protected String title;
   protected Bookmarks bookmarks = new Bookmarks();
   protected FopFactory fopFactory = FopFactory.newInstance();

   public String register(String var1) {
      return this.bookmarks.register(var1);
   }

   public Bookmarks getBookmarks() {
      return this.bookmarks;
   }

   public void setOrientation(int var1) {
      this.orientation = var1;
   }

   public Document(String var1, int var2) {
      this.orientation = var2;
      this.title = var1;
   }

   public Page addPage(int var1) {
      Page var2 = new Page(this, var1, this.title);
      this.pages.add(var2);
      return var2;
   }

   public void toWord(File var1) {
      try {
         Converter var2 = new Converter();
         var2.setProperty("outputFormat", "docx");
         var2.setProperty("outputEncoding", "UTF-8");
         InputSource var3 = new InputSource(this.toStream());
         OutputDestination var4 = new OutputDestination(var1.getPath());
         var2.convert(var3, var4);
      } catch (Exception var5) {
         MudgeSanity.logException("document -> toWord failed [see out.fso]: " + var1, var5, false);
         this.toFSO(new File("out.fso"));
      }

   }

   public void toFSO(File var1) {
      try {
         StringBuffer var2 = new StringBuffer(1048576);
         this.publish(var2);
         FileOutputStream var3 = new FileOutputStream("out.fso");
         var3.write(CommonUtils.toBytes(var2.toString()));
         var3.close();
      } catch (Exception var4) {
         MudgeSanity.logException("document -> toFSO failed: " + var1, var4, false);
      }

   }

   public void toPDF(File var1) {
      try {
         FileOutputStream var2 = new FileOutputStream(var1);
         Fop var3 = this.fopFactory.newFop("application/pdf", (OutputStream)var2);
         TransformerFactory var4 = TransformerFactory.newInstance();
         Transformer var5 = var4.newTransformer();
         StreamSource var6 = new StreamSource(this.toStream());
         SAXResult var7 = new SAXResult(var3.getDefaultHandler());
         var5.transform(var6, var7);
         var2.close();
      } catch (Exception var8) {
         MudgeSanity.logException("document -> toPDF failed [see out.fso]: " + var1, var8, false);
         this.toFSO(new File("out.fso"));
      }

   }

   protected InputStream toStream() {
      try {
         StringBuffer var1 = new StringBuffer(1048576);
         this.publish(var1);
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1.toString().getBytes("UTF-8"));
         return var2;
      } catch (Exception var3) {
         MudgeSanity.logException("output -> toStream failed", var3, false);
         return new ByteArrayInputStream(new byte[0]);
      }
   }

   public void publish(StringBuffer var1) {
      if (this.orientation == 0) {
         var1.append(CommonUtils.readResourceAsString("resources/fso/document_start_portrait.fso"));
      } else if (this.orientation == 1) {
         var1.append(CommonUtils.readResourceAsString("resources/fso/document_start_landscape.fso"));
      }

      this.getBookmarks().publish(var1);
      ReportUtils.PublishAll(var1, this.pages);
      var1.append("</fo:root>");
   }
}
