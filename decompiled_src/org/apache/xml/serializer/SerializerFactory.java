package org.apache.xml.serializer;

import java.util.Hashtable;
import java.util.Properties;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;

public final class SerializerFactory {
   private static Hashtable m_formats = new Hashtable();

   private SerializerFactory() {
   }

   public static Serializer getSerializer(Properties format) {
      try {
         String method = format.getProperty("method");
         String className;
         if (method == null) {
            className = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[]{"method"});
            throw new IllegalArgumentException(className);
         } else {
            className = format.getProperty("{http://xml.apache.org/xalan}content-handler");
            if (null == className) {
               Properties methodDefaults = OutputPropertiesFactory.getDefaultMethodProperties(method);
               className = methodDefaults.getProperty("{http://xml.apache.org/xalan}content-handler");
               if (null == className) {
                  String msg = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[]{"{http://xml.apache.org/xalan}content-handler"});
                  throw new IllegalArgumentException(msg);
               }
            }

            ClassLoader loader = ObjectFactory.findClassLoader();
            Class cls = ObjectFactory.findProviderClass(className, loader, true);
            Object obj = cls.newInstance();
            Object ser;
            if (obj instanceof SerializationHandler) {
               ser = (Serializer)cls.newInstance();
               ((Serializer)ser).setOutputFormat(format);
            } else {
               if (!(obj instanceof ContentHandler)) {
                  throw new Exception(Utils.messages.createMessage("ER_SERIALIZER_NOT_CONTENTHANDLER", new Object[]{className}));
               }

               className = "org.apache.xml.serializer.ToXMLSAXHandler";
               cls = ObjectFactory.findProviderClass(className, loader, true);
               SerializationHandler sh = (SerializationHandler)cls.newInstance();
               sh.setContentHandler((ContentHandler)obj);
               sh.setOutputFormat(format);
               ser = sh;
            }

            return (Serializer)ser;
         }
      } catch (Exception var8) {
         throw new WrappedRuntimeException(var8);
      }
   }
}
