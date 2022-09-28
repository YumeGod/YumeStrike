package javax.xml.transform.sax;

import javax.xml.transform.Result;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

public class SAXResult implements Result {
   public static final String FEATURE = "http://javax.xml.transform.sax.SAXResult/feature";
   private ContentHandler handler;
   private LexicalHandler lexhandler;
   private String systemId;

   public SAXResult() {
   }

   public SAXResult(ContentHandler var1) {
      this.setHandler(var1);
   }

   public void setHandler(ContentHandler var1) {
      this.handler = var1;
   }

   public ContentHandler getHandler() {
      return this.handler;
   }

   public void setLexicalHandler(LexicalHandler var1) {
      this.lexhandler = var1;
   }

   public LexicalHandler getLexicalHandler() {
      return this.lexhandler;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public String getSystemId() {
      return this.systemId;
   }
}
