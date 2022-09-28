package javax.xml.validation;

import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class Validator {
   protected Validator() {
   }

   public abstract void reset();

   public void validate(Source var1) throws SAXException, IOException {
      this.validate(var1, (Result)null);
   }

   public abstract void validate(Source var1, Result var2) throws SAXException, IOException;

   public abstract void setErrorHandler(ErrorHandler var1);

   public abstract ErrorHandler getErrorHandler();

   public abstract void setResourceResolver(LSResourceResolver var1);

   public abstract LSResourceResolver getResourceResolver();

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException("the name parameter is null");
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException("the name parameter is null");
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException("the name parameter is null");
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException("the name parameter is null");
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }
}
