package org.apache.batik.ext.awt.image.spi;

public class ImageWriterParams {
   private Integer resolution;
   private Float jpegQuality;
   private Boolean jpegForceBaseline;
   private String compressionMethod;

   public Integer getResolution() {
      return this.resolution;
   }

   public Float getJPEGQuality() {
      return this.jpegQuality;
   }

   public Boolean getJPEGForceBaseline() {
      return this.jpegForceBaseline;
   }

   public String getCompressionMethod() {
      return this.compressionMethod;
   }

   public void setResolution(int var1) {
      this.resolution = new Integer(var1);
   }

   public void setJPEGQuality(float var1, boolean var2) {
      this.jpegQuality = new Float(var1);
      this.jpegForceBaseline = var2 ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setCompressionMethod(String var1) {
      this.compressionMethod = var1;
   }
}
