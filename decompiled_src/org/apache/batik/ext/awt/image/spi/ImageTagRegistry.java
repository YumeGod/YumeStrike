package org.apache.batik.ext.awt.image.spi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.batik.ext.awt.color.ICCColorSpaceExt;
import org.apache.batik.ext.awt.image.URLImageCache;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.ProfileRable;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.Service;

public class ImageTagRegistry implements ErrorConstants {
   List entries;
   List extensions;
   List mimeTypes;
   URLImageCache rawCache;
   URLImageCache imgCache;
   static ImageTagRegistry registry = null;
   static BrokenLinkProvider defaultProvider = new DefaultBrokenLinkProvider();
   static BrokenLinkProvider brokenLinkProvider = null;
   // $FF: synthetic field
   static Class class$org$apache$batik$ext$awt$image$spi$RegistryEntry;

   public ImageTagRegistry() {
      this((URLImageCache)null, (URLImageCache)null);
   }

   public ImageTagRegistry(URLImageCache var1, URLImageCache var2) {
      this.entries = new LinkedList();
      this.extensions = null;
      this.mimeTypes = null;
      if (var1 == null) {
         var1 = new URLImageCache();
      }

      if (var2 == null) {
         var2 = new URLImageCache();
      }

      this.rawCache = var1;
      this.imgCache = var2;
   }

   public void flushCache() {
      this.rawCache.flush();
      this.imgCache.flush();
   }

   public void flushImage(ParsedURL var1) {
      this.rawCache.clear(var1);
      this.imgCache.clear(var1);
   }

   public Filter checkCache(ParsedURL var1, ICCColorSpaceExt var2) {
      boolean var3 = var2 != null;
      Object var4 = null;
      URLImageCache var5;
      if (var3) {
         var5 = this.rawCache;
      } else {
         var5 = this.imgCache;
      }

      var4 = var5.request(var1);
      if (var4 == null) {
         var5.clear(var1);
         return null;
      } else {
         if (var2 != null) {
            var4 = new ProfileRable((Filter)var4, var2);
         }

         return (Filter)var4;
      }
   }

   public Filter readURL(ParsedURL var1) {
      return this.readURL((InputStream)null, var1, (ICCColorSpaceExt)null, true, true);
   }

   public Filter readURL(ParsedURL var1, ICCColorSpaceExt var2) {
      return this.readURL((InputStream)null, var1, var2, true, true);
   }

   public Filter readURL(InputStream var1, ParsedURL var2, ICCColorSpaceExt var3, boolean var4, boolean var5) {
      if (var1 != null && !((InputStream)var1).markSupported()) {
         var1 = new BufferedInputStream((InputStream)var1);
      }

      boolean var6 = var3 != null;
      Object var7 = null;
      URLImageCache var8 = null;
      if (var2 != null) {
         if (var6) {
            var8 = this.rawCache;
         } else {
            var8 = this.imgCache;
         }

         var7 = var8.request(var2);
         if (var7 != null) {
            if (var3 != null) {
               var7 = new ProfileRable((Filter)var7, var3);
            }

            return (Filter)var7;
         }
      }

      boolean var9 = false;
      List var10 = this.getRegisteredMimeTypes();
      Iterator var11 = this.entries.iterator();

      while(var11.hasNext()) {
         RegistryEntry var12 = (RegistryEntry)var11.next();
         if (var12 instanceof URLRegistryEntry) {
            if (var2 != null && var4) {
               URLRegistryEntry var17 = (URLRegistryEntry)var12;
               if (var17.isCompatibleURL(var2)) {
                  var7 = var17.handleURL(var2, var6);
                  if (var7 != null) {
                     break;
                  }
               }
            }
         } else if (var12 instanceof StreamRegistryEntry) {
            StreamRegistryEntry var13 = (StreamRegistryEntry)var12;
            if (!var9) {
               try {
                  if (var1 == null) {
                     if (var2 == null || !var4) {
                        break;
                     }

                     try {
                        var1 = var2.openStream(var10.iterator());
                     } catch (IOException var15) {
                        var9 = true;
                        continue;
                     }

                     if (!((InputStream)var1).markSupported()) {
                        var1 = new BufferedInputStream((InputStream)var1);
                     }
                  }

                  if (var13.isCompatibleStream((InputStream)var1)) {
                     var7 = var13.handleStream((InputStream)var1, var2, var6);
                     if (var7 != null) {
                        break;
                     }
                  }
               } catch (StreamCorruptedException var16) {
                  var1 = null;
               }
            }
         }
      }

      if (var8 != null) {
         var8.put(var2, (Filter)var7);
      }

      if (var7 == null) {
         if (!var5) {
            return null;
         } else {
            return var9 ? getBrokenLinkImage(this, "url.unreachable", (Object[])null) : getBrokenLinkImage(this, "url.uninterpretable", (Object[])null);
         }
      } else if (BrokenLinkProvider.hasBrokenLinkProperty((Filter)var7)) {
         return (Filter)(var5 ? var7 : null);
      } else {
         if (var3 != null) {
            var7 = new ProfileRable((Filter)var7, var3);
         }

         return (Filter)var7;
      }
   }

   public Filter readStream(InputStream var1) {
      return this.readStream(var1, (ICCColorSpaceExt)null);
   }

   public Filter readStream(InputStream var1, ICCColorSpaceExt var2) {
      if (!((InputStream)var1).markSupported()) {
         var1 = new BufferedInputStream((InputStream)var1);
      }

      boolean var3 = var2 != null;
      Object var4 = null;
      Iterator var5 = this.entries.iterator();

      while(var5.hasNext()) {
         RegistryEntry var6 = (RegistryEntry)var5.next();
         if (var6 instanceof StreamRegistryEntry) {
            StreamRegistryEntry var7 = (StreamRegistryEntry)var6;

            try {
               if (var7.isCompatibleStream((InputStream)var1)) {
                  var4 = var7.handleStream((InputStream)var1, (ParsedURL)null, var3);
                  if (var4 != null) {
                     break;
                  }
               }
            } catch (StreamCorruptedException var9) {
               break;
            }
         }
      }

      if (var4 == null) {
         return getBrokenLinkImage(this, "stream.unreadable", (Object[])null);
      } else {
         if (var2 != null && !BrokenLinkProvider.hasBrokenLinkProperty((Filter)var4)) {
            var4 = new ProfileRable((Filter)var4, var2);
         }

         return (Filter)var4;
      }
   }

   public synchronized void register(RegistryEntry var1) {
      float var2 = var1.getPriority();
      ListIterator var3 = this.entries.listIterator();

      RegistryEntry var4;
      do {
         if (!var3.hasNext()) {
            var3.add(var1);
            this.extensions = null;
            this.mimeTypes = null;
            return;
         }

         var4 = (RegistryEntry)var3.next();
      } while(!(var4.getPriority() > var2));

      var3.previous();
      var3.add(var1);
   }

   public synchronized List getRegisteredExtensions() {
      if (this.extensions != null) {
         return this.extensions;
      } else {
         this.extensions = new LinkedList();
         Iterator var1 = this.entries.iterator();

         while(var1.hasNext()) {
            RegistryEntry var2 = (RegistryEntry)var1.next();
            this.extensions.addAll(var2.getStandardExtensions());
         }

         this.extensions = Collections.unmodifiableList(this.extensions);
         return this.extensions;
      }
   }

   public synchronized List getRegisteredMimeTypes() {
      if (this.mimeTypes != null) {
         return this.mimeTypes;
      } else {
         this.mimeTypes = new LinkedList();
         Iterator var1 = this.entries.iterator();

         while(var1.hasNext()) {
            RegistryEntry var2 = (RegistryEntry)var1.next();
            this.mimeTypes.addAll(var2.getMimeTypes());
         }

         this.mimeTypes = Collections.unmodifiableList(this.mimeTypes);
         return this.mimeTypes;
      }
   }

   public static synchronized ImageTagRegistry getRegistry() {
      if (registry != null) {
         return registry;
      } else {
         registry = new ImageTagRegistry();
         registry.register(new JDKRegistryEntry());
         Iterator var0 = Service.providers(class$org$apache$batik$ext$awt$image$spi$RegistryEntry == null ? (class$org$apache$batik$ext$awt$image$spi$RegistryEntry = class$("org.apache.batik.ext.awt.image.spi.RegistryEntry")) : class$org$apache$batik$ext$awt$image$spi$RegistryEntry);

         while(var0.hasNext()) {
            RegistryEntry var1 = (RegistryEntry)var0.next();
            registry.register(var1);
         }

         return registry;
      }
   }

   public static synchronized Filter getBrokenLinkImage(Object var0, String var1, Object[] var2) {
      Filter var3 = null;
      if (brokenLinkProvider != null) {
         var3 = brokenLinkProvider.getBrokenLinkImage(var0, var1, var2);
      }

      if (var3 == null) {
         var3 = defaultProvider.getBrokenLinkImage(var0, var1, var2);
      }

      return var3;
   }

   public static synchronized void setBrokenLinkProvider(BrokenLinkProvider var0) {
      brokenLinkProvider = var0;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
