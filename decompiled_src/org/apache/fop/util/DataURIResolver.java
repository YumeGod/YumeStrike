package org.apache.fop.util;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

/** @deprecated */
public class DataURIResolver implements URIResolver {
   private final URIResolver newResolver = new org.apache.xmlgraphics.util.uri.DataURIResolver();

   /** @deprecated */
   public Source resolve(String href, String base) throws TransformerException {
      return this.newResolver.resolve(href, base);
   }
}
