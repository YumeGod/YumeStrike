package javax.xml.transform.stream;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.transform.Source;

public class StreamSource implements Source {
   public static final String FEATURE = "http://javax.xml.transform.stream.StreamSource/feature";
   private String publicId;
   private String systemId;
   private InputStream inputStream;
   private Reader reader;

   public StreamSource() {
   }

   public StreamSource(InputStream var1) {
      this.setInputStream(var1);
   }

   public StreamSource(InputStream var1, String var2) {
      this.setInputStream(var1);
      this.setSystemId(var2);
   }

   public StreamSource(Reader var1) {
      this.setReader(var1);
   }

   public StreamSource(Reader var1, String var2) {
      this.setReader(var1);
      this.setSystemId(var2);
   }

   public StreamSource(String var1) {
      this.systemId = var1;
   }

   public StreamSource(File var1) {
      this.setSystemId(var1);
   }

   public void setInputStream(InputStream var1) {
      this.inputStream = var1;
   }

   public InputStream getInputStream() {
      return this.inputStream;
   }

   public void setReader(Reader var1) {
      this.reader = var1;
   }

   public Reader getReader() {
      return this.reader;
   }

   public void setPublicId(String var1) {
      this.publicId = var1;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public void setSystemId(File var1) {
      this.systemId = FilePathToURI.filepath2URI(var1.getAbsolutePath());
   }
}
