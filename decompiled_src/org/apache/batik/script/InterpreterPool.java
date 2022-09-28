package org.apache.batik.script;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.util.Service;
import org.w3c.dom.Document;

public class InterpreterPool {
   public static final String BIND_NAME_DOCUMENT = "document";
   protected static Map defaultFactories = new HashMap(7);
   protected Map factories = new HashMap(7);
   // $FF: synthetic field
   static Class class$org$apache$batik$script$InterpreterFactory;

   public InterpreterPool() {
      this.factories.putAll(defaultFactories);
   }

   public Interpreter createInterpreter(Document var1, String var2) {
      InterpreterFactory var3 = (InterpreterFactory)this.factories.get(var2);
      if (var3 == null) {
         return null;
      } else {
         Interpreter var4 = null;
         SVGOMDocument var5 = (SVGOMDocument)var1;

         try {
            URL var6 = new URL(var5.getDocumentURI());
            var4 = var3.createInterpreter(var6, var5.isSVG12());
         } catch (MalformedURLException var7) {
         }

         if (var4 == null) {
            return null;
         } else {
            if (var1 != null) {
               var4.bindObject("document", var1);
            }

            return var4;
         }
      }
   }

   public void putInterpreterFactory(String var1, InterpreterFactory var2) {
      this.factories.put(var1, var2);
   }

   public void removeInterpreterFactory(String var1) {
      this.factories.remove(var1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      Iterator var0 = Service.providers(class$org$apache$batik$script$InterpreterFactory == null ? (class$org$apache$batik$script$InterpreterFactory = class$("org.apache.batik.script.InterpreterFactory")) : class$org$apache$batik$script$InterpreterFactory);

      while(var0.hasNext()) {
         InterpreterFactory var1 = null;
         var1 = (InterpreterFactory)var0.next();
         String[] var2 = var1.getMimeTypes();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            defaultFactories.put(var2[var3], var1);
         }
      }

   }
}
