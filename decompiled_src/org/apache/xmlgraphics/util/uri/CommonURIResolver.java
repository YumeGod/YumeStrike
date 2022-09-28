package org.apache.xmlgraphics.util.uri;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.apache.xmlgraphics.util.Service;

public class CommonURIResolver implements URIResolver {
   private final List uriResolvers = new LinkedList();

   public CommonURIResolver() {
      Iterator iter = Service.providers(URIResolver.class);

      while(iter.hasNext()) {
         URIResolver resolver = (URIResolver)iter.next();
         this.register(resolver);
      }

   }

   public static CommonURIResolver getDefaultURIResolver() {
      return CommonURIResolver.DefaultInstanceHolder.INSTANCE;
   }

   public Source resolve(String href, String base) {
      synchronized(this.uriResolvers) {
         Iterator it = this.uriResolvers.iterator();

         while(true) {
            if (it.hasNext()) {
               URIResolver currentResolver = (URIResolver)it.next();

               Source var10000;
               try {
                  Source result = currentResolver.resolve(href, base);
                  if (result == null) {
                     continue;
                  }

                  var10000 = result;
               } catch (TransformerException var8) {
                  continue;
               }

               return var10000;
            }

            return null;
         }
      }
   }

   public void register(URIResolver uriResolver) {
      synchronized(this.uriResolvers) {
         this.uriResolvers.add(uriResolver);
      }
   }

   public void unregister(URIResolver uriResolver) {
      synchronized(this.uriResolvers) {
         this.uriResolvers.remove(uriResolver);
      }
   }

   private static final class DefaultInstanceHolder {
      private static final CommonURIResolver INSTANCE = new CommonURIResolver();
   }
}
