package org.apache.fop.apps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.util.io.Base64EncodeStream;
import org.apache.xmlgraphics.util.uri.CommonURIResolver;

public class FOURIResolver implements URIResolver {
   private Log log;
   private CommonURIResolver commonURIResolver;
   private URIResolver uriResolver;
   private boolean throwExceptions;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public String checkBaseURL(String base) throws MalformedURLException {
      base = base.replace('\\', '/');
      if (!base.endsWith("/")) {
         base = base + "/";
      }

      File dir = new File(base);
      if (dir.isDirectory()) {
         return dir.toURI().toASCIIString();
      } else {
         try {
            URI baseURI = new URI(base);
            String scheme = baseURI.getScheme();
            boolean directoryExists = true;
            if ("file".equals(scheme)) {
               dir = FileUtils.toFile(baseURI.toURL());
               directoryExists = dir.isDirectory();
            }

            if (scheme == null || !directoryExists) {
               String message = "base " + base + " is not a valid directory";
               if (this.throwExceptions) {
                  throw new MalformedURLException(message);
               }

               this.log.error(message);
            }

            return baseURI.toASCIIString();
         } catch (URISyntaxException var7) {
            throw new MalformedURLException(var7.getMessage());
         }
      }
   }

   public FOURIResolver() {
      this(false);
   }

   public FOURIResolver(boolean throwExceptions) {
      this.log = LogFactory.getLog("FOP");
      this.commonURIResolver = new CommonURIResolver();
      this.uriResolver = null;
      this.throwExceptions = false;
      this.throwExceptions = throwExceptions;
   }

   private void handleException(Exception e, String errorStr, boolean strict) throws TransformerException {
      if (strict) {
         throw new TransformerException(errorStr, e);
      } else {
         this.log.error(e.getMessage());
      }
   }

   public Source resolve(String href, String base) throws TransformerException {
      Source source = null;
      source = this.commonURIResolver.resolve(href, base);
      if (source == null && this.uriResolver != null) {
         source = this.uriResolver.resolve(href, base);
      }

      if (source == null) {
         URL absoluteURL = null;
         int hashPos = href.indexOf(35);
         String fileURL;
         String fragment;
         if (hashPos >= 0) {
            fileURL = href.substring(0, hashPos);
            fragment = href.substring(hashPos);
         } else {
            fileURL = href;
            fragment = null;
         }

         File file = new File(fileURL);
         if (file.canRead() && file.isFile()) {
            try {
               if (fragment != null) {
                  absoluteURL = new URL(file.toURI().toURL().toExternalForm() + fragment);
               } else {
                  absoluteURL = file.toURI().toURL();
               }
            } catch (MalformedURLException var19) {
               this.handleException(var19, "Could not convert filename '" + href + "' to URL", this.throwExceptions);
            }
         } else if (base == null) {
            try {
               absoluteURL = new URL(href);
            } catch (MalformedURLException var18) {
               try {
                  absoluteURL = new URL("file:" + href);
               } catch (MalformedURLException var17) {
                  this.handleException(var17, "Error with URL '" + href + "'", this.throwExceptions);
               }
            }
         } else {
            URL baseURL = null;

            try {
               baseURL = new URL(base);
            } catch (MalformedURLException var16) {
               this.handleException(var16, "Error with base URL '" + base + "'", this.throwExceptions);
            }

            if (!$assertionsDisabled && baseURL == null) {
               throw new AssertionError();
            }

            String scheme = baseURL.getProtocol() + ":";
            if (href.startsWith(scheme) && "file:".equals(scheme)) {
               href = href.substring(scheme.length());
               int colonPos = href.indexOf(58);
               int slashPos = href.indexOf(47);
               if (slashPos >= 0 && colonPos >= 0 && colonPos < slashPos) {
                  href = "/" + href;
               }
            }

            try {
               absoluteURL = new URL(baseURL, href);
            } catch (MalformedURLException var15) {
               this.handleException(var15, "Error with URL; base '" + base + "' " + "href '" + href + "'", this.throwExceptions);
            }
         }

         if (absoluteURL != null) {
            String effURL = absoluteURL.toExternalForm();

            try {
               URLConnection connection = absoluteURL.openConnection();
               connection.setAllowUserInteraction(false);
               connection.setDoInput(true);
               this.updateURLConnection(connection, href);
               connection.connect();
               return new StreamSource(connection.getInputStream(), effURL);
            } catch (FileNotFoundException var13) {
               this.log.debug("File not found: " + effURL);
            } catch (IOException var14) {
               this.log.error("Error with opening URL '" + effURL + "': " + var14.getMessage());
            }
         }
      }

      return source;
   }

   protected void updateURLConnection(URLConnection connection, String href) {
   }

   protected void applyHttpBasicAuthentication(URLConnection connection, String username, String password) {
      String combined = username + ":" + password;

      try {
         ByteArrayOutputStream baout = new ByteArrayOutputStream(combined.length() * 2);
         Base64EncodeStream base64 = new Base64EncodeStream(baout);
         base64.write(combined.getBytes("UTF-8"));
         base64.close();
         connection.setRequestProperty("Authorization", "Basic " + new String(baout.toByteArray(), "UTF-8"));
      } catch (IOException var7) {
         throw new RuntimeException("Error during base64 encodation of username/password");
      }
   }

   public void setCustomURIResolver(URIResolver resolver) {
      this.uriResolver = resolver;
   }

   public URIResolver getCustomURIResolver() {
      return this.uriResolver;
   }

   public void setThrowExceptions(boolean throwExceptions) {
      this.throwExceptions = throwExceptions;
   }

   static {
      $assertionsDisabled = !FOURIResolver.class.desiredAssertionStatus();
   }
}
