package org.apache.xmlgraphics.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public final class ClasspathResource {
   private final Map contentMappings = new HashMap();
   private static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";
   private static final String CONTENT_TYPE_KEY = "Content-Type";
   private static ClasspathResource classpathResource;

   private ClasspathResource() {
      this.loadManifests();
   }

   public static synchronized ClasspathResource getInstance() {
      if (classpathResource == null) {
         classpathResource = new ClasspathResource();
      }

      return classpathResource;
   }

   private Set getClassLoadersForResources() {
      Set v = new HashSet();

      ClassLoader l;
      try {
         l = ClassLoader.getSystemClassLoader();
         if (l != null) {
            v.add(l);
         }
      } catch (SecurityException var5) {
      }

      try {
         l = Thread.currentThread().getContextClassLoader();
         if (l != null) {
            v.add(l);
         }
      } catch (SecurityException var4) {
      }

      try {
         l = ClasspathResource.class.getClassLoader();
         if (l != null) {
            v.add(l);
         }
      } catch (SecurityException var3) {
      }

      return v;
   }

   private void loadManifests() {
      try {
         Iterator it = this.getClassLoadersForResources().iterator();

         while(it.hasNext()) {
            ClassLoader classLoader = (ClassLoader)it.next();
            Enumeration e = classLoader.getResources("META-INF/MANIFEST.MF");

            while(e.hasMoreElements()) {
               URL u = (URL)e.nextElement();

               try {
                  Manifest manifest = new Manifest(u.openStream());
                  Map entries = manifest.getEntries();
                  Iterator entrysetiterator = entries.entrySet().iterator();

                  while(entrysetiterator.hasNext()) {
                     Map.Entry entry = (Map.Entry)entrysetiterator.next();
                     String name = (String)entry.getKey();
                     Attributes attributes = (Attributes)entry.getValue();
                     String contentType = attributes.getValue("Content-Type");
                     if (contentType != null) {
                        this.addToMapping(contentType, name, classLoader);
                     }
                  }
               } catch (IOException var12) {
               }
            }
         }
      } catch (IOException var13) {
      }

   }

   private void addToMapping(String contentType, String name, ClassLoader classLoader) {
      List existingFiles = (List)this.contentMappings.get(contentType);
      if (existingFiles == null) {
         existingFiles = new Vector();
         this.contentMappings.put(contentType, existingFiles);
      }

      URL url = classLoader.getResource(name);
      if (url != null) {
         ((List)existingFiles).add(url);
      }

   }

   public List listResourcesOfMimeType(String mimeType) {
      List content = (List)this.contentMappings.get(mimeType);
      return content == null ? Collections.EMPTY_LIST : content;
   }
}
