package org.apache.xalan.transformer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.xml.sax.ContentHandler;

public class SerializerSwitcher {
   public static void switchSerializerIfHTML(TransformerImpl transformer, String ns, String localName) throws TransformerException {
      if (null != transformer) {
         if ((null == ns || ns.length() == 0) && localName.equalsIgnoreCase("html")) {
            if (null != transformer.getOutputPropertyNoDefault("method")) {
               return;
            }

            Properties prevProperties = transformer.getOutputFormat().getProperties();
            OutputProperties htmlOutputProperties = new OutputProperties("html");
            htmlOutputProperties.copyFrom(prevProperties, true);
            Properties htmlProperties = htmlOutputProperties.getProperties();

            try {
               Serializer oldSerializer = null;
               if (null != oldSerializer) {
                  Serializer serializer = SerializerFactory.getSerializer(htmlProperties);
                  Writer writer = ((Serializer)oldSerializer).getWriter();
                  if (null != writer) {
                     serializer.setWriter(writer);
                  } else {
                     OutputStream os = ((Serializer)oldSerializer).getOutputStream();
                     if (null != os) {
                        serializer.setOutputStream(os);
                     }
                  }

                  ContentHandler ch = serializer.asContentHandler();
                  transformer.setContentHandler(ch);
               }
            } catch (IOException var10) {
               throw new TransformerException(var10);
            }
         }

      }
   }

   private static String getOutputPropertyNoDefault(String qnameString, Properties props) throws IllegalArgumentException {
      String value = (String)props.get(qnameString);
      return value;
   }

   public static Serializer switchSerializerIfHTML(String ns, String localName, Properties props, Serializer oldSerializer) throws TransformerException {
      Serializer newSerializer = oldSerializer;
      if ((null == ns || ns.length() == 0) && localName.equalsIgnoreCase("html")) {
         if (null != getOutputPropertyNoDefault("method", props)) {
            return oldSerializer;
         }

         OutputProperties htmlOutputProperties = new OutputProperties("html");
         htmlOutputProperties.copyFrom(props, true);
         Properties htmlProperties = htmlOutputProperties.getProperties();
         if (null != oldSerializer) {
            Serializer serializer = SerializerFactory.getSerializer(htmlProperties);
            Writer writer = oldSerializer.getWriter();
            if (null != writer) {
               serializer.setWriter(writer);
            } else {
               OutputStream os = serializer.getOutputStream();
               if (null != os) {
                  serializer.setOutputStream(os);
               }
            }

            newSerializer = serializer;
         }
      }

      return newSerializer;
   }
}
