package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.fop.render.rtf.rtflib.tools.ImageUtil;

public class RtfExternalGraphic extends RtfElement {
   protected URL url = null;
   protected int height = -1;
   protected int heightPercent = -1;
   protected int heightDesired = -1;
   protected boolean perCentH = false;
   protected int width = -1;
   protected int widthPercent = -1;
   protected int widthDesired = -1;
   protected boolean perCentW = false;
   protected boolean scaleUniform = false;
   private int[] cropValues = new int[4];
   protected int graphicCompressionRate = 80;
   private byte[] imagedata = null;
   private FormatBase imageformat;

   public RtfExternalGraphic(RtfContainer container, Writer writer) throws IOException {
      super(container, writer);
   }

   public RtfExternalGraphic(RtfContainer container, Writer writer, RtfAttributes attributes) throws IOException {
      super(container, writer, attributes);
   }

   protected void writeRtfContent() throws IOException {
      try {
         this.writeRtfContentWithException();
      } catch (ExternalGraphicException var2) {
         this.writeExceptionInRtf(var2);
      }

   }

   protected void writeRtfContentWithException() throws IOException {
      if (this.writer != null) {
         if (this.url == null && this.imagedata == null) {
            throw new ExternalGraphicException("No image data is available (neither URL, nor in-memory)");
         } else {
            String linkToRoot = System.getProperty("jfor_link_to_root");
            if (this.url != null && linkToRoot != null) {
               this.writer.write("{\\field {\\* \\fldinst { INCLUDEPICTURE \"");
               this.writer.write(linkToRoot);
               File urlFile = new File(this.url.getFile());
               this.writer.write(urlFile.getName());
               this.writer.write("\" \\\\* MERGEFORMAT \\\\d }}}");
            } else {
               if (this.imagedata == null) {
                  try {
                     InputStream in = this.url.openStream();

                     try {
                        this.imagedata = IOUtils.toByteArray(this.url.openStream());
                     } finally {
                        IOUtils.closeQuietly(in);
                     }
                  } catch (Exception var9) {
                     throw new ExternalGraphicException("The attribute 'src' of <fo:external-graphic> has a invalid value: '" + this.url + "' (" + var9 + ")");
                  }
               }

               if (this.imagedata != null) {
                  String file = this.url != null ? this.url.getFile() : "<unknown>";
                  this.imageformat = RtfExternalGraphic.FormatBase.determineFormat(this.imagedata);
                  if (this.imageformat != null) {
                     this.imageformat = this.imageformat.convert(this.imageformat, this.imagedata);
                  }

                  if (this.imageformat != null && this.imageformat.getType() != -1 && !"".equals(this.imageformat.getRtfTag())) {
                     this.writeGroupMark(true);
                     this.writeStarControlWord("shppict");
                     this.writeGroupMark(true);
                     this.writeControlWord("pict");
                     StringBuffer buf = new StringBuffer(this.imagedata.length * 3);
                     this.writeControlWord(this.imageformat.getRtfTag());
                     this.computeImageSize();
                     this.writeSizeInfo();
                     this.writeAttributes(this.getRtfAttributes(), (String[])null);

                     int len;
                     for(len = 0; len < this.imagedata.length; ++len) {
                        int iData = this.imagedata[len];
                        if (iData < 0) {
                           iData += 256;
                        }

                        if (iData < 16) {
                           buf.append('0');
                        }

                        buf.append(Integer.toHexString(iData));
                     }

                     len = buf.length();
                     char[] chars = new char[len];
                     buf.getChars(0, len, chars, 0);
                     this.writer.write(chars);
                     this.writeGroupMark(false);
                     this.writeGroupMark(false);
                  } else {
                     throw new ExternalGraphicException("The tag <fo:external-graphic> does not support " + file.substring(file.lastIndexOf(".") + 1) + " - image type.");
                  }
               }
            }
         }
      }
   }

   private void computeImageSize() {
      if (this.imageformat.getType() == 1) {
         this.width = ImageUtil.getIntFromByteArray(this.imagedata, 16, 4, true);
         this.height = ImageUtil.getIntFromByteArray(this.imagedata, 20, 4, true);
      } else {
         int basis;
         if (this.imageformat.getType() == 2) {
            basis = -1;
            byte ff = -1;
            byte c0 = -64;

            for(int i = 0; i < this.imagedata.length; ++i) {
               byte b = this.imagedata[i];
               if (b == ff && i != this.imagedata.length - 1) {
                  b = this.imagedata[i + 1];
                  if (b == c0) {
                     basis = i + 5;
                     break;
                  }
               }
            }

            if (basis != -1) {
               this.width = ImageUtil.getIntFromByteArray(this.imagedata, basis + 2, 2, true);
               this.height = ImageUtil.getIntFromByteArray(this.imagedata, basis, 2, true);
            }
         } else if (this.imageformat.getType() == 0) {
            int i = false;
            basis = ImageUtil.getIntFromByteArray(this.imagedata, 151, 4, false);
            if (basis != 0) {
               this.width = basis;
            }

            basis = ImageUtil.getIntFromByteArray(this.imagedata, 155, 4, false);
            if (basis != 0) {
               this.height = basis;
            }
         }
      }

   }

   private void writeSizeInfo() throws IOException {
      if (this.width != -1) {
         this.writeControlWord("picw" + this.width);
      }

      if (this.height != -1) {
         this.writeControlWord("pich" + this.height);
      }

      if (this.widthDesired != -1) {
         if (this.perCentW) {
            this.writeControlWord("picscalex" + this.widthDesired);
         } else {
            this.writeControlWord("picwgoal" + this.widthDesired);
         }
      } else if (this.scaleUniform && this.heightDesired != -1) {
         if (this.perCentH) {
            this.writeControlWord("picscalex" + this.heightDesired);
         } else {
            this.writeControlWord("picscalex" + this.heightDesired * 100 / this.height);
         }
      }

      if (this.heightDesired != -1) {
         if (this.perCentH) {
            this.writeControlWord("picscaley" + this.heightDesired);
         } else {
            this.writeControlWord("pichgoal" + this.heightDesired);
         }
      } else if (this.scaleUniform && this.widthDesired != -1) {
         if (this.perCentW) {
            this.writeControlWord("picscaley" + this.widthDesired);
         } else {
            this.writeControlWord("picscaley" + this.widthDesired * 100 / this.width);
         }
      }

      if (this.cropValues[0] != 0) {
         this.writeOneAttribute("piccropl", new Integer(this.cropValues[0]));
      }

      if (this.cropValues[1] != 0) {
         this.writeOneAttribute("piccropt", new Integer(this.cropValues[1]));
      }

      if (this.cropValues[2] != 0) {
         this.writeOneAttribute("piccropr", new Integer(this.cropValues[2]));
      }

      if (this.cropValues[3] != 0) {
         this.writeOneAttribute("piccropb", new Integer(this.cropValues[3]));
      }

   }

   public void setHeight(String theHeight) {
      this.heightDesired = ImageUtil.getInt(theHeight);
      this.perCentH = ImageUtil.isPercent(theHeight);
   }

   public void setWidth(String theWidth) {
      this.widthDesired = ImageUtil.getInt(theWidth);
      this.perCentW = ImageUtil.isPercent(theWidth);
   }

   public void setWidthTwips(int twips) {
      this.widthDesired = twips;
      this.perCentW = false;
   }

   public void setHeightTwips(int twips) {
      this.heightDesired = twips;
      this.perCentH = false;
   }

   public void setScaling(String value) {
      this.setUniformScaling("uniform".equalsIgnoreCase(value));
   }

   public void setUniformScaling(boolean uniform) {
      this.scaleUniform = uniform;
   }

   public void setCropping(int left, int top, int right, int bottom) {
      this.cropValues[0] = left;
      this.cropValues[1] = top;
      this.cropValues[2] = right;
      this.cropValues[3] = bottom;
   }

   public void setImageData(byte[] data) throws IOException {
      this.imagedata = data;
   }

   public void setURL(String urlString) throws IOException {
      URL tmpUrl = null;

      try {
         tmpUrl = new URL(urlString);
      } catch (MalformedURLException var6) {
         try {
            tmpUrl = (new File(urlString)).toURL();
         } catch (MalformedURLException var5) {
            throw new ExternalGraphicException("The attribute 'src' of <fo:external-graphic> has a invalid value: '" + urlString + "' (" + var5 + ")");
         }
      }

      this.url = tmpUrl;
   }

   public int getCompressionRate() {
      return this.graphicCompressionRate;
   }

   public boolean setCompressionRate(int percent) {
      if (percent >= 1 && percent <= 100) {
         this.graphicCompressionRate = percent;
         return true;
      } else {
         return false;
      }
   }

   public boolean isEmpty() {
      return this.url == null;
   }

   private static class FormatPNG extends FormatBase {
      private FormatPNG() {
         super(null);
      }

      public static boolean isFormat(byte[] data) {
         byte[] pattern = new byte[]{80, 78, 71};
         return ImageUtil.compareHexValues(pattern, data, 1, true);
      }

      public int getType() {
         return 1;
      }

      public String getRtfTag() {
         return "pngblip";
      }

      // $FF: synthetic method
      FormatPNG(Object x0) {
         this();
      }
   }

   private static class FormatJPG extends FormatBase {
      private FormatJPG() {
         super(null);
      }

      public static boolean isFormat(byte[] data) {
         byte[] pattern = new byte[]{-1, -40};
         return ImageUtil.compareHexValues(pattern, data, 0, true);
      }

      public int getType() {
         return 2;
      }

      public String getRtfTag() {
         return "jpegblip";
      }

      // $FF: synthetic method
      FormatJPG(Object x0) {
         this();
      }
   }

   private static class FormatBMP extends FormatBase {
      private FormatBMP() {
         super(null);
      }

      public static boolean isFormat(byte[] data) {
         byte[] pattern = new byte[]{66, 77};
         return ImageUtil.compareHexValues(pattern, data, 0, true);
      }

      public int getType() {
         return 3;
      }

      // $FF: synthetic method
      FormatBMP(Object x0) {
         this();
      }
   }

   private static class FormatEMF extends FormatBase {
      private FormatEMF() {
         super(null);
      }

      public static boolean isFormat(byte[] data) {
         byte[] pattern = new byte[]{1, 0, 0};
         return ImageUtil.compareHexValues(pattern, data, 0, true);
      }

      public int getType() {
         return 0;
      }

      public String getRtfTag() {
         return "emfblip";
      }

      // $FF: synthetic method
      FormatEMF(Object x0) {
         this();
      }
   }

   private static class FormatGIF extends FormatBase {
      private FormatGIF() {
         super(null);
      }

      public static boolean isFormat(byte[] data) {
         byte[] pattern = new byte[]{71, 73, 70, 56};
         return ImageUtil.compareHexValues(pattern, data, 0, true);
      }

      public int getType() {
         return 50;
      }

      // $FF: synthetic method
      FormatGIF(Object x0) {
         this();
      }
   }

   private static class FormatBase {
      private FormatBase() {
      }

      public static boolean isFormat(byte[] data) {
         return false;
      }

      public FormatBase convert(FormatBase format, byte[] data) {
         return format;
      }

      public static FormatBase determineFormat(byte[] data) {
         if (RtfExternalGraphic.FormatPNG.isFormat(data)) {
            return new FormatPNG();
         } else if (RtfExternalGraphic.FormatJPG.isFormat(data)) {
            return new FormatJPG();
         } else if (RtfExternalGraphic.FormatEMF.isFormat(data)) {
            return new FormatEMF();
         } else if (RtfExternalGraphic.FormatGIF.isFormat(data)) {
            return new FormatGIF();
         } else {
            return RtfExternalGraphic.FormatBMP.isFormat(data) ? new FormatBMP() : null;
         }
      }

      public int getType() {
         return -1;
      }

      public String getRtfTag() {
         return "";
      }

      // $FF: synthetic method
      FormatBase(Object x0) {
         this();
      }
   }

   public static class ExternalGraphicException extends IOException {
      ExternalGraphicException(String reason) {
         super(reason);
      }
   }
}
