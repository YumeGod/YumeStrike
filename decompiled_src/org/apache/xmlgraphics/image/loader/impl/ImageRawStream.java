package org.apache.xmlgraphics.image.loader.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.MimeEnabledImageFlavor;

public class ImageRawStream extends AbstractImage {
   private ImageFlavor flavor;
   private InputStreamFactory streamFactory;

   public ImageRawStream(ImageInfo info, ImageFlavor flavor, InputStreamFactory streamFactory) {
      super(info);
      this.flavor = flavor;
      this.setInputStreamFactory(streamFactory);
   }

   public ImageRawStream(ImageInfo info, ImageFlavor flavor, InputStream in) {
      this(info, flavor, (InputStreamFactory)(new SingleStreamFactory(in)));
   }

   public ImageFlavor getFlavor() {
      return this.flavor;
   }

   public String getMimeType() {
      return this.getFlavor() instanceof MimeEnabledImageFlavor ? this.getFlavor().getMimeType() : "application/octet-stream";
   }

   public boolean isCacheable() {
      return !this.streamFactory.isUsedOnceOnly();
   }

   public void setInputStreamFactory(InputStreamFactory factory) {
      if (this.streamFactory != null) {
         this.streamFactory.close();
      }

      this.streamFactory = factory;
   }

   public InputStream createInputStream() {
      return this.streamFactory.createInputStream();
   }

   public void writeTo(OutputStream out) throws IOException {
      InputStream in = this.createInputStream();

      try {
         IOUtils.copy(in, out);
      } finally {
         IOUtils.closeQuietly(in);
      }

   }

   public void writeTo(File target) throws IOException {
      OutputStream out = new FileOutputStream(target);

      try {
         this.writeTo((OutputStream)out);
      } finally {
         IOUtils.closeQuietly((OutputStream)out);
      }

   }

   public static class ByteArrayStreamFactory implements InputStreamFactory {
      private byte[] data;

      public ByteArrayStreamFactory(byte[] data) {
         this.data = data;
      }

      public InputStream createInputStream() {
         return new ByteArrayInputStream(this.data);
      }

      public void close() {
      }

      public boolean isUsedOnceOnly() {
         return false;
      }
   }

   private static class SingleStreamFactory implements InputStreamFactory {
      private InputStream in;

      public SingleStreamFactory(InputStream in) {
         this.in = in;
      }

      public synchronized InputStream createInputStream() {
         if (this.in != null) {
            InputStream tempin = this.in;
            this.in = null;
            return tempin;
         } else {
            throw new IllegalStateException("Can only create an InputStream once!");
         }
      }

      public synchronized void close() {
         IOUtils.closeQuietly(this.in);
         this.in = null;
      }

      public boolean isUsedOnceOnly() {
         return true;
      }

      protected void finalize() {
         this.close();
      }
   }

   public interface InputStreamFactory {
      boolean isUsedOnceOnly();

      InputStream createInputStream();

      void close();
   }
}
