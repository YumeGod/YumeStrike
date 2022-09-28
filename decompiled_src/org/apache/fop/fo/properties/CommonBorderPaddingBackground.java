package org.apache.fop.fo.properties;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;

public class CommonBorderPaddingBackground {
   private static final PropertyCache cache;
   private int hash = -1;
   public final int backgroundAttachment;
   public final Color backgroundColor;
   public final String backgroundImage;
   public final int backgroundRepeat;
   public final Length backgroundPositionHorizontal;
   public final Length backgroundPositionVertical;
   private ImageInfo backgroundImageInfo;
   public static final int BEFORE = 0;
   public static final int AFTER = 1;
   public static final int START = 2;
   public static final int END = 3;
   private static final BorderInfo defaultBorderInfo;
   private BorderInfo[] borderInfo = new BorderInfo[4];
   private CondLengthProperty[] padding = new CondLengthProperty[4];

   public static BorderInfo getDefaultBorderInfo() {
      return defaultBorderInfo;
   }

   private CommonBorderPaddingBackground(PropertyList pList) throws PropertyException {
      this.backgroundAttachment = pList.get(8).getEnum();
      Color bc = pList.get(9).getColor(pList.getFObj().getUserAgent());
      if (bc.getAlpha() == 0) {
         this.backgroundColor = null;
      } else {
         this.backgroundColor = bc;
      }

      String img = pList.get(10).getString();
      if (img != null && !"none".equals(img)) {
         this.backgroundImage = img;
         this.backgroundRepeat = pList.get(14).getEnum();
         this.backgroundPositionHorizontal = pList.get(12).getLength();
         this.backgroundPositionVertical = pList.get(13).getLength();
      } else {
         this.backgroundImage = "";
         this.backgroundRepeat = -1;
         this.backgroundPositionHorizontal = null;
         this.backgroundPositionVertical = null;
      }

      this.initBorderInfo(pList, 0, 23, 25, 26, 172);
      this.initBorderInfo(pList, 1, 19, 21, 22, 171);
      this.initBorderInfo(pList, 2, 47, 49, 50, 177);
      this.initBorderInfo(pList, 3, 33, 35, 36, 174);
   }

   public static CommonBorderPaddingBackground getInstance(PropertyList pList) throws PropertyException {
      CommonBorderPaddingBackground newInstance = new CommonBorderPaddingBackground(pList);
      CommonBorderPaddingBackground cachedInstance = null;
      if ((newInstance.padding[0] == null || newInstance.padding[0].getLength().isAbsolute()) && (newInstance.padding[1] == null || newInstance.padding[1].getLength().isAbsolute()) && (newInstance.padding[2] == null || newInstance.padding[2].getLength().isAbsolute()) && (newInstance.padding[3] == null || newInstance.padding[3].getLength().isAbsolute()) && (newInstance.backgroundPositionHorizontal == null || newInstance.backgroundPositionHorizontal.isAbsolute()) && (newInstance.backgroundPositionVertical == null || newInstance.backgroundPositionVertical.isAbsolute())) {
         cachedInstance = cache.fetch(newInstance);
      }

      if ((cachedInstance == null || cachedInstance == newInstance) && !"".equals(newInstance.backgroundImage)) {
         String uri = URISpecification.getURL(newInstance.backgroundImage);
         FObj fobj = pList.getFObj();
         FOUserAgent userAgent = pList.getFObj().getUserAgent();
         ImageManager manager = userAgent.getFactory().getImageManager();
         ImageSessionContext sessionContext = userAgent.getImageSessionContext();

         ResourceEventProducer eventProducer;
         try {
            ImageInfo info = manager.getImageInfo(uri, sessionContext);
            newInstance.backgroundImageInfo = info;
         } catch (ImageException var11) {
            eventProducer = ResourceEventProducer.Provider.get(fobj.getUserAgent().getEventBroadcaster());
            eventProducer.imageError(fobj, uri, var11, fobj.getLocator());
         } catch (FileNotFoundException var12) {
            eventProducer = ResourceEventProducer.Provider.get(fobj.getUserAgent().getEventBroadcaster());
            eventProducer.imageNotFound(fobj, uri, var12, fobj.getLocator());
         } catch (IOException var13) {
            eventProducer = ResourceEventProducer.Provider.get(fobj.getUserAgent().getEventBroadcaster());
            eventProducer.imageIOError(fobj, uri, var13, fobj.getLocator());
         }
      }

      return cachedInstance != null ? cachedInstance : newInstance;
   }

   private void initBorderInfo(PropertyList pList, int side, int colorProp, int styleProp, int widthProp, int paddingProp) throws PropertyException {
      this.padding[side] = pList.get(paddingProp).getCondLength();
      int style = pList.get(styleProp).getEnum();
      if (style != 95) {
         FOUserAgent ua = pList.getFObj().getUserAgent();
         this.setBorderInfo(CommonBorderPaddingBackground.BorderInfo.getInstance(style, pList.get(widthProp).getCondLength(), pList.get(colorProp).getColor(ua)), side);
      }

   }

   private void setBorderInfo(BorderInfo info, int side) {
      this.borderInfo[side] = info;
   }

   public BorderInfo getBorderInfo(int side) {
      return this.borderInfo[side] == null ? getDefaultBorderInfo() : this.borderInfo[side];
   }

   public ImageInfo getImageInfo() {
      return this.backgroundImageInfo;
   }

   public int getBorderStartWidth(boolean discard) {
      return this.getBorderWidth(2, discard);
   }

   public int getBorderEndWidth(boolean discard) {
      return this.getBorderWidth(3, discard);
   }

   public int getBorderBeforeWidth(boolean discard) {
      return this.getBorderWidth(0, discard);
   }

   public int getBorderAfterWidth(boolean discard) {
      return this.getBorderWidth(1, discard);
   }

   public int getPaddingStart(boolean discard, PercentBaseContext context) {
      return this.getPadding(2, discard, context);
   }

   public int getPaddingEnd(boolean discard, PercentBaseContext context) {
      return this.getPadding(3, discard, context);
   }

   public int getPaddingBefore(boolean discard, PercentBaseContext context) {
      return this.getPadding(0, discard, context);
   }

   public int getPaddingAfter(boolean discard, PercentBaseContext context) {
      return this.getPadding(1, discard, context);
   }

   public int getBorderWidth(int side, boolean discard) {
      return this.borderInfo[side] != null && this.borderInfo[side].mStyle != 95 && this.borderInfo[side].mStyle != 57 && (!discard || !this.borderInfo[side].mWidth.isDiscard()) ? this.borderInfo[side].mWidth.getLengthValue() : 0;
   }

   public Color getBorderColor(int side) {
      return this.borderInfo[side] != null ? this.borderInfo[side].getColor() : null;
   }

   public int getBorderStyle(int side) {
      return this.borderInfo[side] != null ? this.borderInfo[side].mStyle : 95;
   }

   public int getPadding(int side, boolean discard, PercentBaseContext context) {
      return this.padding[side] != null && (!discard || !this.padding[side].isDiscard()) ? this.padding[side].getLengthValue(context) : 0;
   }

   public CondLengthProperty getPaddingLengthProperty(int side) {
      return this.padding[side];
   }

   public int getIPPaddingAndBorder(boolean discard, PercentBaseContext context) {
      return this.getPaddingStart(discard, context) + this.getPaddingEnd(discard, context) + this.getBorderStartWidth(discard) + this.getBorderEndWidth(discard);
   }

   public int getBPPaddingAndBorder(boolean discard, PercentBaseContext context) {
      return this.getPaddingBefore(discard, context) + this.getPaddingAfter(discard, context) + this.getBorderBeforeWidth(discard) + this.getBorderAfterWidth(discard);
   }

   public String toString() {
      return "CommonBordersAndPadding (Before, After, Start, End):\nBorders: (" + this.getBorderBeforeWidth(false) + ", " + this.getBorderAfterWidth(false) + ", " + this.getBorderStartWidth(false) + ", " + this.getBorderEndWidth(false) + ")\n" + "Border Colors: (" + this.getBorderColor(0) + ", " + this.getBorderColor(1) + ", " + this.getBorderColor(2) + ", " + this.getBorderColor(3) + ")\n" + "Padding: (" + this.getPaddingBefore(false, (PercentBaseContext)null) + ", " + this.getPaddingAfter(false, (PercentBaseContext)null) + ", " + this.getPaddingStart(false, (PercentBaseContext)null) + ", " + this.getPaddingEnd(false, (PercentBaseContext)null) + ")\n";
   }

   public boolean hasBackground() {
      return this.backgroundColor != null || this.getImageInfo() != null;
   }

   public boolean hasBorder() {
      return this.getBorderBeforeWidth(false) + this.getBorderAfterWidth(false) + this.getBorderStartWidth(false) + this.getBorderEndWidth(false) > 0;
   }

   public boolean hasPadding(PercentBaseContext context) {
      return this.getPaddingBefore(false, context) + this.getPaddingAfter(false, context) + this.getPaddingStart(false, context) + this.getPaddingEnd(false, context) > 0;
   }

   public boolean hasBorderInfo() {
      return this.borderInfo[0] != null || this.borderInfo[1] != null || this.borderInfo[2] != null || this.borderInfo[3] != null;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public int getBackgroundAttachment() {
      return this.backgroundAttachment;
   }

   public String getBackgroundImage() {
      return this.backgroundImage;
   }

   public int getBackgroundRepeat() {
      return this.backgroundRepeat;
   }

   public Length getBackgroundPositionHorizontal() {
      return this.backgroundPositionHorizontal;
   }

   public Length getBackgroundPositionVertical() {
      return this.backgroundPositionVertical;
   }

   public ImageInfo getBackgroundImageInfo() {
      return this.backgroundImageInfo;
   }

   public BorderInfo[] getBorderInfo() {
      return this.borderInfo;
   }

   public CondLengthProperty[] getPadding() {
      return this.padding;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof CommonBorderPaddingBackground)) {
         return false;
      } else {
         CommonBorderPaddingBackground cbpb = (CommonBorderPaddingBackground)obj;
         return this.backgroundAttachment == cbpb.backgroundAttachment && this.backgroundColor == cbpb.backgroundColor && this.backgroundImage.equals(cbpb.backgroundImage) && this.backgroundPositionHorizontal == cbpb.backgroundPositionHorizontal && this.backgroundPositionVertical == cbpb.backgroundPositionVertical && this.backgroundRepeat == cbpb.backgroundRepeat && this.borderInfo[0] == cbpb.borderInfo[0] && this.borderInfo[1] == cbpb.borderInfo[1] && this.borderInfo[2] == cbpb.borderInfo[2] && this.borderInfo[3] == cbpb.borderInfo[3] && this.padding[0] == cbpb.padding[0] && this.padding[1] == cbpb.padding[1] && this.padding[2] == cbpb.padding[2] && this.padding[3] == cbpb.padding[3];
      }
   }

   public int hashCode() {
      if (this.hash == -1) {
         int hash = 17;
         hash = 37 * hash + this.backgroundAttachment;
         hash = 37 * hash + (this.backgroundColor == null ? 0 : this.backgroundColor.hashCode());
         hash = 37 * hash + (this.backgroundImage == null ? 0 : this.backgroundImage.hashCode());
         hash = 37 * hash + (this.backgroundPositionHorizontal == null ? 0 : this.backgroundPositionHorizontal.hashCode());
         hash = 37 * hash + (this.backgroundPositionVertical == null ? 0 : this.backgroundPositionVertical.hashCode());
         hash = 37 * hash + this.backgroundRepeat;
         hash = 37 * hash + (this.borderInfo[0] == null ? 0 : this.borderInfo[0].hashCode());
         hash = 37 * hash + (this.borderInfo[1] == null ? 0 : this.borderInfo[1].hashCode());
         hash = 37 * hash + (this.borderInfo[2] == null ? 0 : this.borderInfo[2].hashCode());
         hash = 37 * hash + (this.borderInfo[3] == null ? 0 : this.borderInfo[3].hashCode());
         hash = 37 * hash + (this.padding[0] == null ? 0 : this.padding[0].hashCode());
         hash = 37 * hash + (this.padding[1] == null ? 0 : this.padding[1].hashCode());
         hash = 37 * hash + (this.padding[2] == null ? 0 : this.padding[2].hashCode());
         hash = 37 * hash + (this.padding[3] == null ? 0 : this.padding[3].hashCode());
         this.hash = hash;
      }

      return this.hash;
   }

   static {
      cache = new PropertyCache(CommonBorderPaddingBackground.class);
      defaultBorderInfo = CommonBorderPaddingBackground.BorderInfo.getInstance(95, new ConditionalNullLength(), (Color)null);
   }

   private static class ConditionalNullLength extends CondLengthProperty {
      private ConditionalNullLength() {
      }

      public Property getComponent(int cmpId) {
         throw new UnsupportedOperationException();
      }

      public Property getConditionality() {
         throw new UnsupportedOperationException();
      }

      public Length getLength() {
         throw new UnsupportedOperationException();
      }

      public Property getLengthComponent() {
         throw new UnsupportedOperationException();
      }

      public int getLengthValue() {
         return 0;
      }

      public int getLengthValue(PercentBaseContext context) {
         return 0;
      }

      public boolean isDiscard() {
         return true;
      }

      public void setComponent(int cmpId, Property cmpnValue, boolean isDefault) {
         throw new UnsupportedOperationException();
      }

      public String toString() {
         return "CondLength[0mpt, discard]";
      }

      // $FF: synthetic method
      ConditionalNullLength(Object x0) {
         this();
      }
   }

   public static class BorderInfo {
      private static final PropertyCache cache = new PropertyCache(BorderInfo.class);
      private int mStyle;
      private Color mColor;
      private CondLengthProperty mWidth;
      private int hash = -1;

      private BorderInfo(int style, CondLengthProperty width, Color color) {
         this.mStyle = style;
         this.mWidth = width;
         this.mColor = color;
      }

      public static BorderInfo getInstance(int style, CondLengthProperty width, Color color) {
         return cache.fetch(new BorderInfo(style, width, color));
      }

      public int getStyle() {
         return this.mStyle;
      }

      public Color getColor() {
         return this.mColor;
      }

      public CondLengthProperty getWidth() {
         return this.mWidth;
      }

      public int getRetainedWidth() {
         return this.mStyle != 95 && this.mStyle != 57 ? this.mWidth.getLengthValue() : 0;
      }

      public String toString() {
         StringBuffer sb = new StringBuffer("BorderInfo");
         sb.append(" {");
         sb.append(this.mStyle);
         sb.append(", ");
         sb.append(this.mColor);
         sb.append(", ");
         sb.append(this.mWidth);
         sb.append("}");
         return sb.toString();
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof BorderInfo)) {
            return false;
         } else {
            BorderInfo bi = (BorderInfo)obj;
            return this.mColor == bi.mColor && this.mStyle == bi.mStyle && this.mWidth == bi.mWidth;
         }
      }

      public int hashCode() {
         if (this.hash == -1) {
            int hash = 17;
            hash = 37 * hash + (this.mColor == null ? 0 : this.mColor.hashCode());
            hash = 37 * hash + this.mStyle;
            hash = 37 * hash + (this.mWidth == null ? 0 : this.mWidth.hashCode());
            this.hash = hash;
         }

         return this.hash;
      }
   }
}
