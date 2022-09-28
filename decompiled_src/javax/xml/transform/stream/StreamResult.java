package javax.xml.transform.stream;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import javax.xml.transform.Result;

public class StreamResult implements Result {
   public static final String FEATURE = "http://javax.xml.transform.stream.StreamResult/feature";
   private String systemId;
   private OutputStream outputStream;
   private Writer writer;

   public StreamResult() {
   }

   public StreamResult(OutputStream var1) {
      this.setOutputStream(var1);
   }

   public StreamResult(Writer var1) {
      this.setWriter(var1);
   }

   public StreamResult(String var1) {
      this.systemId = var1;
   }

   public StreamResult(File var1) {
      this.setSystemId(var1);
   }

   public void setOutputStream(OutputStream var1) {
      this.outputStream = var1;
   }

   public OutputStream getOutputStream() {
      return this.outputStream;
   }

   public void setWriter(Writer var1) {
      this.writer = var1;
   }

   public Writer getWriter() {
      return this.writer;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public void setSystemId(File var1) {
      try {
         Method var2 = var1.getClass().getMethod("toURI", (Class[])null);
         Object var3 = var2.invoke(var1, (Object[])null);
         Method var4 = var3.getClass().getMethod("toString", (Class[])null);
         this.systemId = (String)var4.invoke(var3, (Object[])null);
      } catch (Exception var6) {
         try {
            this.systemId = var1.toURL().toString();
         } catch (MalformedURLException var5) {
            this.systemId = null;
            throw new RuntimeException("javax.xml.transform.stream.StreamResult#setSystemId(File f) with MalformedURLException: " + var5.toString());
         }
      }

   }

   public String getSystemId() {
      return this.systemId;
   }
}
