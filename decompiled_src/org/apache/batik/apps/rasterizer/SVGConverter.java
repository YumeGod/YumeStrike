package org.apache.batik.apps.rasterizer;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.ParsedURL;

public class SVGConverter {
   public static final String ERROR_NO_SOURCES_SPECIFIED = "SVGConverter.error.no.sources.specified";
   public static final String ERROR_CANNOT_COMPUTE_DESTINATION = "SVGConverter.error.cannot.compute.destination";
   public static final String ERROR_CANNOT_USE_DST_FILE = "SVGConverter.error.cannot.use.dst.file";
   public static final String ERROR_CANNOT_ACCESS_TRANSCODER = "SVGConverter.error.cannot.access.transcoder";
   public static final String ERROR_SOURCE_SAME_AS_DESTINATION = "SVGConverter.error.source.same.as.destination";
   public static final String ERROR_CANNOT_READ_SOURCE = "SVGConverter.error.cannot.read.source";
   public static final String ERROR_CANNOT_OPEN_SOURCE = "SVGConverter.error.cannot.open.source";
   public static final String ERROR_OUTPUT_NOT_WRITEABLE = "SVGConverter.error.output.not.writeable";
   public static final String ERROR_CANNOT_OPEN_OUTPUT_FILE = "SVGConverter.error.cannot.open.output.file";
   public static final String ERROR_UNABLE_TO_CREATE_OUTPUT_DIR = "SVGConverter.error.unable.to.create.output.dir";
   public static final String ERROR_WHILE_RASTERIZING_FILE = "SVGConverter.error.while.rasterizing.file";
   protected static final String SVG_EXTENSION = ".svg";
   protected static final float DEFAULT_QUALITY = -1.0F;
   protected static final float MAXIMUM_QUALITY = 0.99F;
   protected static final DestinationType DEFAULT_RESULT_TYPE;
   protected static final float DEFAULT_WIDTH = -1.0F;
   protected static final float DEFAULT_HEIGHT = -1.0F;
   protected DestinationType destinationType;
   protected float height;
   protected float width;
   protected float maxHeight;
   protected float maxWidth;
   protected float quality;
   protected int indexed;
   protected Rectangle2D area;
   protected String language;
   protected String userStylesheet;
   protected float pixelUnitToMillimeter;
   protected boolean validate;
   protected boolean executeOnload;
   protected float snapshotTime;
   protected String allowedScriptTypes;
   protected boolean constrainScriptOrigin;
   protected boolean securityOff;
   protected List sources;
   protected File dst;
   protected Color backgroundColor;
   protected String mediaType;
   protected String defaultFontFamily;
   protected String alternateStylesheet;
   protected List files;
   protected SVGConverterController controller;

   public SVGConverter() {
      this(new DefaultSVGConverterController());
   }

   public SVGConverter(SVGConverterController var1) {
      this.destinationType = DEFAULT_RESULT_TYPE;
      this.height = -1.0F;
      this.width = -1.0F;
      this.maxHeight = -1.0F;
      this.maxWidth = -1.0F;
      this.quality = -1.0F;
      this.indexed = -1;
      this.area = null;
      this.language = null;
      this.userStylesheet = null;
      this.pixelUnitToMillimeter = -1.0F;
      this.validate = false;
      this.executeOnload = false;
      this.snapshotTime = Float.NaN;
      this.allowedScriptTypes = null;
      this.constrainScriptOrigin = true;
      this.securityOff = false;
      this.sources = null;
      this.backgroundColor = null;
      this.mediaType = null;
      this.defaultFontFamily = null;
      this.alternateStylesheet = null;
      this.files = new ArrayList();
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.controller = var1;
      }
   }

   public void setDestinationType(DestinationType var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.destinationType = var1;
      }
   }

   public DestinationType getDestinationType() {
      return this.destinationType;
   }

   public void setHeight(float var1) {
      this.height = var1;
   }

   public float getHeight() {
      return this.height;
   }

   public void setWidth(float var1) {
      this.width = var1;
   }

   public float getWidth() {
      return this.width;
   }

   public void setMaxHeight(float var1) {
      this.maxHeight = var1;
   }

   public float getMaxHeight() {
      return this.maxHeight;
   }

   public void setMaxWidth(float var1) {
      this.maxWidth = var1;
   }

   public float getMaxWidth() {
      return this.maxWidth;
   }

   public void setQuality(float var1) throws IllegalArgumentException {
      if (var1 >= 1.0F) {
         throw new IllegalArgumentException();
      } else {
         this.quality = var1;
      }
   }

   public float getQuality() {
      return this.quality;
   }

   public void setIndexed(int var1) throws IllegalArgumentException {
      this.indexed = var1;
   }

   public int getIndexed() {
      return this.indexed;
   }

   public void setLanguage(String var1) {
      this.language = var1;
   }

   public String getLanguage() {
      return this.language;
   }

   public void setUserStylesheet(String var1) {
      this.userStylesheet = var1;
   }

   public String getUserStylesheet() {
      return this.userStylesheet;
   }

   public void setPixelUnitToMillimeter(float var1) {
      this.pixelUnitToMillimeter = var1;
   }

   public float getPixelUnitToMillimeter() {
      return this.pixelUnitToMillimeter;
   }

   public void setArea(Rectangle2D var1) {
      this.area = var1;
   }

   public Rectangle2D getArea() {
      return this.area;
   }

   public void setSources(String[] var1) {
      if (var1 == null) {
         this.sources = null;
      } else {
         this.sources = new ArrayList();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] != null) {
               this.sources.add(var1[var2]);
            }
         }

         if (this.sources.size() == 0) {
            this.sources = null;
         }
      }

   }

   public List getSources() {
      return this.sources;
   }

   public void setDst(File var1) {
      this.dst = var1;
   }

   public File getDst() {
      return this.dst;
   }

   public void setBackgroundColor(Color var1) {
      this.backgroundColor = var1;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public void setMediaType(String var1) {
      this.mediaType = var1;
   }

   public String getMediaType() {
      return this.mediaType;
   }

   public void setDefaultFontFamily(String var1) {
      this.defaultFontFamily = var1;
   }

   public String getDefaultFontFamily() {
      return this.defaultFontFamily;
   }

   public void setAlternateStylesheet(String var1) {
      this.alternateStylesheet = var1;
   }

   public String getAlternateStylesheet() {
      return this.alternateStylesheet;
   }

   public void setValidate(boolean var1) {
      this.validate = var1;
   }

   public boolean getValidate() {
      return this.validate;
   }

   public void setExecuteOnload(boolean var1) {
      this.executeOnload = var1;
   }

   public boolean getExecuteOnload() {
      return this.executeOnload;
   }

   public void setSnapshotTime(float var1) {
      this.snapshotTime = var1;
   }

   public float getSnapshotTime() {
      return this.snapshotTime;
   }

   public void setAllowedScriptTypes(String var1) {
      this.allowedScriptTypes = var1;
   }

   public String getAllowedScriptTypes() {
      return this.allowedScriptTypes;
   }

   public void setConstrainScriptOrigin(boolean var1) {
      this.constrainScriptOrigin = var1;
   }

   public boolean getConstrainScriptOrigin() {
      return this.constrainScriptOrigin;
   }

   public void setSecurityOff(boolean var1) {
      this.securityOff = var1;
   }

   public boolean getSecurityOff() {
      return this.securityOff;
   }

   protected boolean isFile(File var1) {
      if (var1.exists()) {
         return var1.isFile();
      } else {
         return var1.toString().toLowerCase().endsWith(this.destinationType.getExtension());
      }
   }

   public void execute() throws SVGConverterException {
      List var1 = this.computeSources();
      Object var2 = null;
      if (var1.size() == 1 && this.dst != null && this.isFile(this.dst)) {
         var2 = new ArrayList();
         ((List)var2).add(this.dst);
      } else {
         var2 = this.computeDstFiles(var1);
      }

      Transcoder var3 = this.destinationType.getTranscoder();
      if (var3 == null) {
         throw new SVGConverterException("SVGConverter.error.cannot.access.transcoder", new Object[]{this.destinationType.toString()}, true);
      } else {
         Map var4 = this.computeTranscodingHints();
         var3.setTranscodingHints(var4);
         if (this.controller.proceedWithComputedTask(var3, var4, var1, (List)var2)) {
            for(int var5 = 0; var5 < var1.size(); ++var5) {
               SVGConverterSource var6 = (SVGConverterSource)var1.get(var5);
               File var7 = (File)((List)var2).get(var5);
               this.createOutputDir(var7);
               this.transcode(var6, var7, var3);
            }

         }
      }
   }

   protected List computeDstFiles(List var1) throws SVGConverterException {
      ArrayList var2 = new ArrayList();
      int var3;
      int var4;
      SVGConverterSource var5;
      if (this.dst != null) {
         if (this.dst.exists() && this.dst.isFile()) {
            throw new SVGConverterException("SVGConverter.error.cannot.use.dst.file");
         }

         var3 = var1.size();

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = (SVGConverterSource)var1.get(var4);
            File var6 = new File(this.dst.getPath(), this.getDestinationFile(var5.getName()));
            var2.add(var6);
         }
      } else {
         var3 = var1.size();

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = (SVGConverterSource)var1.get(var4);
            if (!(var5 instanceof SVGConverterFileSource)) {
               throw new SVGConverterException("SVGConverter.error.cannot.compute.destination", new Object[]{var5});
            }

            SVGConverterFileSource var8 = (SVGConverterFileSource)var5;
            File var7 = new File(var8.getFile().getParent(), this.getDestinationFile(var5.getName()));
            var2.add(var7);
         }
      }

      return var2;
   }

   protected List computeSources() throws SVGConverterException {
      ArrayList var1 = new ArrayList();
      if (this.sources == null) {
         throw new SVGConverterException("SVGConverter.error.no.sources.specified");
      } else {
         int var2 = this.sources.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = (String)this.sources.get(var3);
            File var5 = new File(var4);
            if (var5.exists()) {
               var1.add(new SVGConverterFileSource(var5));
            } else {
               String[] var6 = this.getFileNRef(var4);
               var5 = new File(var6[0]);
               if (var5.exists()) {
                  var1.add(new SVGConverterFileSource(var5, var6[1]));
               } else {
                  var1.add(new SVGConverterURLSource(var4));
               }
            }
         }

         return var1;
      }
   }

   public String[] getFileNRef(String var1) {
      int var2 = var1.lastIndexOf(35);
      String[] var3 = new String[]{var1, ""};
      if (var2 > -1) {
         var3[0] = var1.substring(0, var2);
         if (var2 + 1 < var1.length()) {
            var3[1] = var1.substring(var2 + 1);
         }
      }

      return var3;
   }

   protected Map computeTranscodingHints() {
      HashMap var1 = new HashMap();
      if (this.area != null) {
         var1.put(ImageTranscoder.KEY_AOI, this.area);
      }

      if (this.quality > 0.0F) {
         var1.put(JPEGTranscoder.KEY_QUALITY, new Float(this.quality));
      }

      if (this.indexed != -1) {
         var1.put(PNGTranscoder.KEY_INDEXED, new Integer(this.indexed));
      }

      if (this.backgroundColor != null) {
         var1.put(ImageTranscoder.KEY_BACKGROUND_COLOR, this.backgroundColor);
      }

      if (this.height > 0.0F) {
         var1.put(ImageTranscoder.KEY_HEIGHT, new Float(this.height));
      }

      if (this.width > 0.0F) {
         var1.put(ImageTranscoder.KEY_WIDTH, new Float(this.width));
      }

      if (this.maxHeight > 0.0F) {
         var1.put(ImageTranscoder.KEY_MAX_HEIGHT, new Float(this.maxHeight));
      }

      if (this.maxWidth > 0.0F) {
         var1.put(ImageTranscoder.KEY_MAX_WIDTH, new Float(this.maxWidth));
      }

      if (this.mediaType != null) {
         var1.put(ImageTranscoder.KEY_MEDIA, this.mediaType);
      }

      if (this.defaultFontFamily != null) {
         var1.put(ImageTranscoder.KEY_DEFAULT_FONT_FAMILY, this.defaultFontFamily);
      }

      if (this.alternateStylesheet != null) {
         var1.put(ImageTranscoder.KEY_ALTERNATE_STYLESHEET, this.alternateStylesheet);
      }

      if (this.userStylesheet != null) {
         String var2;
         try {
            URL var3 = (new File(System.getProperty("user.dir"))).toURL();
            var2 = (new ParsedURL(var3, this.userStylesheet)).toString();
         } catch (Exception var4) {
            var2 = this.userStylesheet;
         }

         var1.put(ImageTranscoder.KEY_USER_STYLESHEET_URI, var2);
      }

      if (this.language != null) {
         var1.put(ImageTranscoder.KEY_LANGUAGE, this.language);
      }

      if (this.pixelUnitToMillimeter > 0.0F) {
         var1.put(ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(this.pixelUnitToMillimeter));
      }

      if (this.validate) {
         var1.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.TRUE);
      }

      if (this.executeOnload) {
         var1.put(ImageTranscoder.KEY_EXECUTE_ONLOAD, Boolean.TRUE);
      }

      if (!Float.isNaN(this.snapshotTime)) {
         var1.put(ImageTranscoder.KEY_SNAPSHOT_TIME, new Float(this.snapshotTime));
      }

      if (this.allowedScriptTypes != null) {
         var1.put(ImageTranscoder.KEY_ALLOWED_SCRIPT_TYPES, this.allowedScriptTypes);
      }

      if (!this.constrainScriptOrigin) {
         var1.put(ImageTranscoder.KEY_CONSTRAIN_SCRIPT_ORIGIN, Boolean.FALSE);
      }

      return var1;
   }

   protected void transcode(SVGConverterSource var1, File var2, Transcoder var3) throws SVGConverterException {
      TranscoderInput var4 = null;
      TranscoderOutput var5 = null;
      FileOutputStream var6 = null;
      if (this.controller.proceedWithSourceTranscoding(var1, var2)) {
         try {
            if (var1.isSameAs(var2.getPath())) {
               throw new SVGConverterException("SVGConverter.error.source.same.as.destination", true);
            }

            if (!var1.isReadable()) {
               throw new SVGConverterException("SVGConverter.error.cannot.read.source", new Object[]{var1.getName()});
            }

            try {
               InputStream var7 = var1.openStream();
               var7.close();
            } catch (IOException var14) {
               throw new SVGConverterException("SVGConverter.error.cannot.open.source", new Object[]{var1.getName(), var14.toString()});
            }

            var4 = new TranscoderInput(var1.getURI());
            if (!this.isWriteable(var2)) {
               throw new SVGConverterException("SVGConverter.error.output.not.writeable", new Object[]{var2.getName()});
            }

            try {
               var6 = new FileOutputStream(var2);
            } catch (FileNotFoundException var13) {
               throw new SVGConverterException("SVGConverter.error.cannot.open.output.file", new Object[]{var2.getName()});
            }

            var5 = new TranscoderOutput(var6);
         } catch (SVGConverterException var15) {
            boolean var8 = this.controller.proceedOnSourceTranscodingFailure(var1, var2, var15.getErrorCode());
            if (var8) {
               return;
            }

            throw var15;
         }

         boolean var16 = false;

         try {
            var3.transcode(var4, var5);
            var16 = true;
         } catch (Exception var12) {
            var12.printStackTrace();

            try {
               var6.flush();
               var6.close();
            } catch (IOException var11) {
            }

            boolean var9 = this.controller.proceedOnSourceTranscodingFailure(var1, var2, "SVGConverter.error.while.rasterizing.file");
            if (!var9) {
               throw new SVGConverterException("SVGConverter.error.while.rasterizing.file", new Object[]{var2.getName(), var12.getMessage()});
            }
         }

         try {
            var6.flush();
            var6.close();
         } catch (IOException var10) {
            return;
         }

         if (var16) {
            this.controller.onSourceTranscodingSuccess(var1, var2);
         }

      }
   }

   protected String getDestinationFile(String var1) {
      String var4 = this.destinationType.getExtension();
      int var2 = var1.lastIndexOf(46);
      String var5 = null;
      if (var2 != -1) {
         var5 = var1.substring(0, var2) + var4;
      } else {
         var5 = var1 + var4;
      }

      return var5;
   }

   protected void createOutputDir(File var1) throws SVGConverterException {
      boolean var3 = true;
      String var4 = var1.getParent();
      if (var4 != null) {
         File var2 = new File(var1.getParent());
         if (!var2.exists()) {
            var3 = var2.mkdirs();
         } else if (!var2.isDirectory()) {
            var3 = var2.mkdirs();
         }
      }

      if (!var3) {
         throw new SVGConverterException("SVGConverter.error.unable.to.create.output.dir");
      }
   }

   protected boolean isWriteable(File var1) {
      if (var1.exists()) {
         if (!var1.canWrite()) {
            return false;
         }
      } else {
         try {
            var1.createNewFile();
         } catch (IOException var3) {
            return false;
         }
      }

      return true;
   }

   static {
      DEFAULT_RESULT_TYPE = DestinationType.PNG;
   }

   public static class SVGFileFilter implements FileFilter {
      public static final String SVG_EXTENSION = ".svg";

      public boolean accept(File var1) {
         return var1 != null && var1.getName().toLowerCase().endsWith(".svg");
      }
   }
}
