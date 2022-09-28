package org.apache.fop.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class ServletContextURIResolver implements URIResolver {
   public static final String SERVLET_CONTEXT_PROTOCOL = "servlet-context:";
   private ServletContext servletContext;

   public ServletContextURIResolver(ServletContext servletContext) {
      this.servletContext = servletContext;
   }

   public Source resolve(String href, String base) throws TransformerException {
      if (href.startsWith("servlet-context:")) {
         return this.resolveServletContextURI(href.substring("servlet-context:".length()));
      } else if (base != null && base.startsWith("servlet-context:") && href.indexOf(58) < 0) {
         String abs = base + href;
         return this.resolveServletContextURI(abs.substring("servlet-context:".length()));
      } else {
         return null;
      }
   }

   protected Source resolveServletContextURI(String path) throws TransformerException {
      while(path.startsWith("//")) {
         path = path.substring(1);
      }

      try {
         URL url = this.servletContext.getResource(path);
         InputStream in = this.servletContext.getResourceAsStream(path);
         if (in != null) {
            if (url != null) {
               return new StreamSource(in, url.toExternalForm());
            } else {
               return new StreamSource(in);
            }
         } else {
            throw new TransformerException("Resource does not exist. \"" + path + "\" is not accessible through the servlet context.");
         }
      } catch (MalformedURLException var4) {
         throw new TransformerException("Error accessing resource using servlet context: " + path, var4);
      }
   }
}
