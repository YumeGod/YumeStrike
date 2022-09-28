package org.apache.xmlgraphics.ps;

import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class ImageFormGenerator extends FormGenerator {
   private RenderedImage image;
   private ImageEncoder encoder;
   private ColorSpace colorSpace;
   private int bitsPerComponent;
   private boolean invertImage;
   private Dimension pixelDimensions;

   public ImageFormGenerator(String formName, String title, Dimension2D dimensions, RenderedImage image, boolean invertImage) {
      super(formName, title, dimensions);
      this.bitsPerComponent = 8;
      this.image = image;
      this.encoder = ImageEncodingHelper.createRenderedImageEncoder(image);
      this.invertImage = invertImage;
      this.pixelDimensions = new Dimension(image.getWidth(), image.getHeight());
   }

   public ImageFormGenerator(String formName, String title, Dimension2D dimensions, Dimension dimensionsPx, ImageEncoder encoder, ColorSpace colorSpace, int bitsPerComponent, boolean invertImage) {
      super(formName, title, dimensions);
      this.bitsPerComponent = 8;
      this.pixelDimensions = dimensionsPx;
      this.encoder = encoder;
      this.colorSpace = colorSpace;
      this.bitsPerComponent = bitsPerComponent;
      this.invertImage = invertImage;
   }

   public ImageFormGenerator(String formName, String title, Dimension2D dimensions, Dimension dimensionsPx, ImageEncoder encoder, ColorSpace colorSpace, boolean invertImage) {
      this(formName, title, dimensions, dimensionsPx, encoder, colorSpace, 8, invertImage);
   }

   protected String getDataName() {
      return this.getFormName() + ":Data";
   }

   private String getAdditionalFilters(PSGenerator gen) {
      String implicitFilter = this.encoder.getImplicitFilter();
      if (implicitFilter != null) {
         return "/ASCII85Decode filter " + implicitFilter + " filter";
      } else {
         return gen.getPSLevel() >= 3 ? "/ASCII85Decode filter /FlateDecode filter" : "/ASCII85Decode filter /RunLengthDecode filter";
      }
   }

   protected void generatePaintProc(PSGenerator gen) throws IOException {
      if (gen.getPSLevel() == 2) {
         gen.writeln("    userdict /i 0 put");
      } else {
         gen.writeln("    " + this.getDataName() + " 0 setfileposition");
      }

      String dataSource;
      if (gen.getPSLevel() == 2) {
         dataSource = "{ " + this.getDataName() + " i get /i i 1 add store } bind";
      } else {
         dataSource = this.getDataName();
      }

      AffineTransform at = new AffineTransform();
      at.scale(this.getDimensions().getWidth(), this.getDimensions().getHeight());
      gen.concatMatrix(at);
      PSDictionary imageDict = new PSDictionary();
      imageDict.put("/DataSource", dataSource);
      if (this.image != null) {
         PSImageUtils.writeImageCommand(this.image, imageDict, gen);
      } else {
         imageDict.put("/BitsPerComponent", Integer.toString(this.bitsPerComponent));
         PSImageUtils.writeImageCommand(imageDict, this.pixelDimensions, this.colorSpace, this.invertImage, gen);
      }

   }

   protected void generateAdditionalDataStream(PSGenerator gen) throws IOException {
      gen.writeln("/" + this.getDataName() + " currentfile");
      gen.writeln(this.getAdditionalFilters(gen));
      if (gen.getPSLevel() == 2) {
         gen.writeln("{ /temp exch def [ { temp 16384 string readstring not {exit } if } loop ] } exec");
      } else {
         gen.writeln("/ReusableStreamDecode filter");
      }

      PSImageUtils.compressAndWriteBitmap(this.encoder, gen);
      gen.writeln("def");
   }
}
