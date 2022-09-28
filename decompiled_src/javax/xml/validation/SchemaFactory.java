package javax.xml.validation;

import java.io.File;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class SchemaFactory {
   // $FF: synthetic field
   static Class class$javax$xml$validation$SchemaFactory;

   protected SchemaFactory() {
   }

   public static final SchemaFactory newInstance(String var0) {
      ClassLoader var1 = SecuritySupport.getContextClassLoader();
      if (var1 == null) {
         var1 = (class$javax$xml$validation$SchemaFactory == null ? (class$javax$xml$validation$SchemaFactory = class$("javax.xml.validation.SchemaFactory")) : class$javax$xml$validation$SchemaFactory).getClassLoader();
      }

      SchemaFactory var2 = (new SchemaFactoryFinder(var1)).newFactory(var0);
      if (var2 == null) {
         throw new IllegalArgumentException(var0);
      } else {
         return var2;
      }
   }

   public abstract boolean isSchemaLanguageSupported(String var1);

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

   public abstract void setErrorHandler(ErrorHandler var1);

   public abstract ErrorHandler getErrorHandler();

   public abstract void setResourceResolver(LSResourceResolver var1);

   public abstract LSResourceResolver getResourceResolver();

   public Schema newSchema(Source var1) throws SAXException {
      return this.newSchema(new Source[]{var1});
   }

   public Schema newSchema(File var1) throws SAXException {
      return this.newSchema((Source)(new StreamSource(var1)));
   }

   public Schema newSchema(URL var1) throws SAXException {
      return this.newSchema((Source)(new StreamSource(var1.toExternalForm())));
   }

   public abstract Schema newSchema(Source[] var1) throws SAXException;

   public abstract Schema newSchema() throws SAXException;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
