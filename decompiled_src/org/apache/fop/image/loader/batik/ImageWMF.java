package org.apache.fop.image.loader.batik;

import org.apache.batik.transcoder.wmf.tosvg.WMFRecordStore;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.AbstractImage;

public class ImageWMF extends AbstractImage {
   public static final String MIME_WMF = "image/x-wmf";
   public static final ImageFlavor WMF_IMAGE = new ImageFlavor("WMFRecordStore");
   private WMFRecordStore store;

   public ImageWMF(ImageInfo info, WMFRecordStore store) {
      super(info);
      this.store = store;
   }

   public ImageFlavor getFlavor() {
      return WMF_IMAGE;
   }

   public boolean isCacheable() {
      return true;
   }

   public WMFRecordStore getRecordStore() {
      return this.store;
   }
}
