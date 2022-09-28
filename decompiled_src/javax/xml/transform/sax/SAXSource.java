package javax.xml.transform.sax;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class SAXSource implements Source {
   public static final String FEATURE = "http://javax.xml.transform.sax.SAXSource/feature";
   private XMLReader reader;
   private InputSource inputSource;

   public SAXSource() {
   }

   public SAXSource(XMLReader var1, InputSource var2) {
      this.reader = var1;
      this.inputSource = var2;
   }

   public SAXSource(InputSource var1) {
      this.inputSource = var1;
   }

   public void setXMLReader(XMLReader var1) {
      this.reader = var1;
   }

   public XMLReader getXMLReader() {
      return this.reader;
   }

   public void setInputSource(InputSource var1) {
      this.inputSource = var1;
   }

   public InputSource getInputSource() {
      return this.inputSource;
   }

   public void setSystemId(String var1) {
      if (null == this.inputSource) {
         this.inputSource = new InputSource(var1);
      } else {
         this.inputSource.setSystemId(var1);
      }

   }

   public String getSystemId() {
      return this.inputSource == null ? null : this.inputSource.getSystemId();
   }

   public static InputSource sourceToInputSource(Source var0) {
      if (var0 instanceof SAXSource) {
         return ((SAXSource)var0).getInputSource();
      } else if (var0 instanceof StreamSource) {
         StreamSource var1 = (StreamSource)var0;
         InputSource var2 = new InputSource(var1.getSystemId());
         var2.setByteStream(var1.getInputStream());
         var2.setCharacterStream(var1.getReader());
         var2.setPublicId(var1.getPublicId());
         return var2;
      } else {
         return null;
      }
   }
}
