package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.StringTokenizer;

public abstract class SerializerFactory {
   public static final String FactoriesProperty = "org.apache.xml.serialize.factories";
   private static Hashtable _factories = new Hashtable();
   // $FF: synthetic field
   static Class class$org$apache$xml$serialize$SerializerFactory;

   public static void registerSerializerFactory(SerializerFactory var0) {
      Hashtable var2 = _factories;
      synchronized(var2) {
         String var1 = var0.getSupportedMethod();
         _factories.put(var1, var0);
      }
   }

   public static SerializerFactory getSerializerFactory(String var0) {
      return (SerializerFactory)_factories.get(var0);
   }

   protected abstract String getSupportedMethod();

   public abstract Serializer makeSerializer(OutputFormat var1);

   public abstract Serializer makeSerializer(Writer var1, OutputFormat var2);

   public abstract Serializer makeSerializer(OutputStream var1, OutputFormat var2) throws UnsupportedEncodingException;

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      SerializerFactoryImpl var0 = new SerializerFactoryImpl("xml");
      registerSerializerFactory(var0);
      var0 = new SerializerFactoryImpl("html");
      registerSerializerFactory(var0);
      var0 = new SerializerFactoryImpl("xhtml");
      registerSerializerFactory(var0);
      var0 = new SerializerFactoryImpl("text");
      registerSerializerFactory(var0);
      String var1 = System.getProperty("org.apache.xml.serialize.factories");
      if (var1 != null) {
         StringTokenizer var2 = new StringTokenizer(var1, " ;,:");

         while(var2.hasMoreTokens()) {
            String var3 = var2.nextToken();

            try {
               SerializerFactory var6 = (SerializerFactory)ObjectFactory.newInstance(var3, (class$org$apache$xml$serialize$SerializerFactory == null ? (class$org$apache$xml$serialize$SerializerFactory = class$("org.apache.xml.serialize.SerializerFactory")) : class$org$apache$xml$serialize$SerializerFactory).getClassLoader(), true);
               if (_factories.containsKey(var6.getSupportedMethod())) {
                  _factories.put(var6.getSupportedMethod(), var6);
               }
            } catch (Exception var5) {
            }
         }
      }

   }
}
