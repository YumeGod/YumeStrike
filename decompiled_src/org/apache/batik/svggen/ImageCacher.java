package org.apache.batik.svggen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ImageCacher implements SVGSyntax, ErrorConstants {
   DOMTreeManager domTreeManager;
   Map imageCache;
   Checksum checkSum;

   public ImageCacher() {
      this.domTreeManager = null;
      this.imageCache = new HashMap();
      this.checkSum = new Adler32();
   }

   public ImageCacher(DOMTreeManager var1) {
      this();
      this.setDOMTreeManager(var1);
   }

   public void setDOMTreeManager(DOMTreeManager var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.domTreeManager = var1;
      }
   }

   public DOMTreeManager getDOMTreeManager() {
      return this.domTreeManager;
   }

   public String lookup(ByteArrayOutputStream var1, int var2, int var3, SVGGeneratorContext var4) throws SVGGraphics2DIOException {
      int var5 = this.getChecksum(var1.toByteArray());
      Integer var6 = new Integer(var5);
      String var7 = null;
      Object var8 = this.getCacheableData(var1);
      LinkedList var9 = (LinkedList)this.imageCache.get(var6);
      if (var9 == null) {
         var9 = new LinkedList();
         this.imageCache.put(var6, var9);
      } else {
         ListIterator var10 = var9.listIterator(0);

         while(var10.hasNext()) {
            ImageCacheEntry var11 = (ImageCacheEntry)var10.next();
            if (var11.checksum == var5 && this.imagesMatch(var11.src, var8)) {
               var7 = var11.href;
               break;
            }
         }
      }

      if (var7 == null) {
         ImageCacheEntry var12 = this.createEntry(var5, var8, var2, var3, var4);
         var9.add(var12);
         var7 = var12.href;
      }

      return var7;
   }

   abstract Object getCacheableData(ByteArrayOutputStream var1);

   abstract boolean imagesMatch(Object var1, Object var2) throws SVGGraphics2DIOException;

   abstract ImageCacheEntry createEntry(int var1, Object var2, int var3, int var4, SVGGeneratorContext var5) throws SVGGraphics2DIOException;

   int getChecksum(byte[] var1) {
      this.checkSum.reset();
      this.checkSum.update(var1, 0, var1.length);
      return (int)this.checkSum.getValue();
   }

   public static class External extends ImageCacher {
      private String imageDir;
      private String prefix;
      private String suffix;

      public External(String var1, String var2, String var3) {
         this.imageDir = var1;
         this.prefix = var2;
         this.suffix = var3;
      }

      Object getCacheableData(ByteArrayOutputStream var1) {
         return var1;
      }

      boolean imagesMatch(Object var1, Object var2) throws SVGGraphics2DIOException {
         boolean var3 = false;

         try {
            FileInputStream var4 = new FileInputStream((File)var1);
            int var5 = var4.available();
            byte[] var6 = new byte[var5];
            byte[] var7 = ((ByteArrayOutputStream)var2).toByteArray();

            for(int var8 = 0; var8 != var5; var8 += var4.read(var6, var8, var5 - var8)) {
            }

            var3 = Arrays.equals(var6, var7);
            return var3;
         } catch (IOException var9) {
            throw new SVGGraphics2DIOException("could not read image File " + ((File)var1).getName());
         }
      }

      ImageCacheEntry createEntry(int var1, Object var2, int var3, int var4, SVGGeneratorContext var5) throws SVGGraphics2DIOException {
         File var6 = null;

         try {
            while(var6 == null) {
               String var7 = var5.idGenerator.generateID(this.prefix);
               var6 = new File(this.imageDir, var7 + this.suffix);
               if (var6.exists()) {
                  var6 = null;
               }
            }

            FileOutputStream var9 = new FileOutputStream(var6);
            ((ByteArrayOutputStream)var2).writeTo(var9);
            ((ByteArrayOutputStream)var2).close();
         } catch (IOException var8) {
            throw new SVGGraphics2DIOException("could not write image File " + var6.getName());
         }

         return new ImageCacheEntry(var1, var6, var6.getName());
      }
   }

   public static class Embedded extends ImageCacher {
      public void setDOMTreeManager(DOMTreeManager var1) {
         if (this.domTreeManager != var1) {
            this.domTreeManager = var1;
            this.imageCache = new HashMap();
         }

      }

      Object getCacheableData(ByteArrayOutputStream var1) {
         return "data:image/png;base64," + var1.toString();
      }

      boolean imagesMatch(Object var1, Object var2) {
         return var1.equals(var2);
      }

      ImageCacheEntry createEntry(int var1, Object var2, int var3, int var4, SVGGeneratorContext var5) {
         String var6 = var5.idGenerator.generateID("image");
         this.addToTree(var6, (String)var2, var3, var4, var5);
         return new ImageCacheEntry(var1, var2, "#" + var6);
      }

      private void addToTree(String var1, String var2, int var3, int var4, SVGGeneratorContext var5) {
         Document var6 = this.domTreeManager.getDOMFactory();
         Element var7 = var6.createElementNS("http://www.w3.org/2000/svg", "image");
         var7.setAttributeNS((String)null, "id", var1);
         var7.setAttributeNS((String)null, "width", Integer.toString(var3));
         var7.setAttributeNS((String)null, "height", Integer.toString(var4));
         var7.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", var2);
         this.domTreeManager.addOtherDef(var7);
      }
   }

   private static class ImageCacheEntry {
      public int checksum;
      public Object src;
      public String href;

      ImageCacheEntry(int var1, Object var2, String var3) {
         this.checksum = var1;
         this.src = var2;
         this.href = var3;
      }
   }
}
