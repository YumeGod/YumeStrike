package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.fonts.AFPFont;

public final class ActiveEnvironmentGroup extends AbstractEnvironmentGroup {
   private final List mapCodedFonts = new ArrayList();
   private List mapPageSegments = null;
   private ObjectAreaDescriptor objectAreaDescriptor = null;
   private ObjectAreaPosition objectAreaPosition = null;
   private PresentationTextDescriptor presentationTextDataDescriptor = null;
   private PageDescriptor pageDescriptor = null;
   private final Factory factory;

   public ActiveEnvironmentGroup(Factory factory, String name, int width, int height, int widthRes, int heightRes) {
      super(name);
      this.factory = factory;
      this.pageDescriptor = factory.createPageDescriptor(width, height, widthRes, heightRes);
      this.objectAreaDescriptor = factory.createObjectAreaDescriptor(width, height, widthRes, heightRes);
      this.presentationTextDataDescriptor = factory.createPresentationTextDataDescriptor(width, height, widthRes, heightRes);
   }

   public void setObjectAreaPosition(int x, int y, int rotation) {
      this.objectAreaPosition = this.factory.createObjectAreaPosition(x, y, rotation);
   }

   public PageDescriptor getPageDescriptor() {
      return this.pageDescriptor;
   }

   public PresentationTextDescriptor getPresentationTextDataDescriptor() {
      return this.presentationTextDataDescriptor;
   }

   public void writeContent(OutputStream os) throws IOException {
      super.writeTriplets(os);
      this.writeObjects(this.mapCodedFonts, os);
      this.writeObjects(this.mapDataResources, os);
      this.writeObjects(this.mapPageOverlays, os);
      this.writeObjects(this.mapPageSegments, os);
      if (this.pageDescriptor != null) {
         this.pageDescriptor.writeToStream(os);
      }

      if (this.objectAreaDescriptor != null && this.objectAreaPosition != null) {
         this.objectAreaDescriptor.writeToStream(os);
         this.objectAreaPosition.writeToStream(os);
      }

      if (this.presentationTextDataDescriptor != null) {
         this.presentationTextDataDescriptor.writeToStream(os);
      }

   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-55);
      os.write(data);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-55);
      os.write(data);
   }

   public void createFont(int fontRef, AFPFont font, int size, int orientation) {
      MapCodedFont mapCodedFont = this.getCurrentMapCodedFont();
      if (mapCodedFont == null) {
         mapCodedFont = this.factory.createMapCodedFont();
         this.mapCodedFonts.add(mapCodedFont);
      }

      try {
         mapCodedFont.addFont(fontRef, font, size, orientation);
      } catch (MaximumSizeExceededException var9) {
         mapCodedFont = this.factory.createMapCodedFont();
         this.mapCodedFonts.add(mapCodedFont);

         try {
            mapCodedFont.addFont(fontRef, font, size, orientation);
         } catch (MaximumSizeExceededException var8) {
            log.error("createFont():: resulted in a MaximumSizeExceededException");
         }
      }

   }

   private MapCodedFont getCurrentMapCodedFont() {
      int size = this.mapCodedFonts.size();
      return size > 0 ? (MapCodedFont)this.mapCodedFonts.get(size - 1) : null;
   }

   public void addMapPageSegment(String name) {
      try {
         this.needMapPageSegment().addPageSegment(name);
      } catch (MaximumSizeExceededException var3) {
         throw new IllegalStateException("Internal error: " + var3.getMessage());
      }
   }

   private MapPageSegment getCurrentMapPageSegment() {
      return (MapPageSegment)this.getLastElement(this.mapPageSegments);
   }

   private MapPageSegment needMapPageSegment() {
      if (this.mapPageSegments == null) {
         this.mapPageSegments = new ArrayList();
      }

      MapPageSegment seg = this.getCurrentMapPageSegment();
      if (seg == null || seg.isFull()) {
         seg = new MapPageSegment();
         this.mapPageSegments.add(seg);
      }

      return seg;
   }
}
