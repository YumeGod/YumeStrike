package org.apache.batik.transcoder.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.LengthKey;
import org.apache.batik.transcoder.keys.StringKey;
import org.w3c.dom.Document;

public class PrintTranscoder extends SVGAbstractTranscoder implements Printable {
   public static final String KEY_AOI_STR = "aoi";
   public static final String KEY_HEIGHT_STR = "height";
   public static final String KEY_LANGUAGE_STR = "language";
   public static final String KEY_MARGIN_BOTTOM_STR = "marginBottom";
   public static final String KEY_MARGIN_LEFT_STR = "marginLeft";
   public static final String KEY_MARGIN_RIGHT_STR = "marginRight";
   public static final String KEY_MARGIN_TOP_STR = "marginTop";
   public static final String KEY_PAGE_HEIGHT_STR = "pageHeight";
   public static final String KEY_PAGE_ORIENTATION_STR = "pageOrientation";
   public static final String KEY_PAGE_WIDTH_STR = "pageWidth";
   public static final String KEY_PIXEL_TO_MM_STR = "pixelToMm";
   public static final String KEY_SCALE_TO_PAGE_STR = "scaleToPage";
   public static final String KEY_SHOW_PAGE_DIALOG_STR = "showPageDialog";
   public static final String KEY_SHOW_PRINTER_DIALOG_STR = "showPrinterDialog";
   public static final String KEY_USER_STYLESHEET_URI_STR = "userStylesheet";
   public static final String KEY_WIDTH_STR = "width";
   public static final String KEY_XML_PARSER_CLASSNAME_STR = "xmlParserClassName";
   public static final String VALUE_MEDIA_PRINT = "print";
   public static final String VALUE_PAGE_ORIENTATION_LANDSCAPE = "landscape";
   public static final String VALUE_PAGE_ORIENTATION_PORTRAIT = "portrait";
   public static final String VALUE_PAGE_ORIENTATION_REVERSE_LANDSCAPE = "reverseLandscape";
   private List inputs = new ArrayList();
   private List printedInputs = null;
   private int curIndex = -1;
   private BridgeContext theCtx;
   public static final TranscodingHints.Key KEY_SHOW_PAGE_DIALOG = new BooleanKey();
   public static final TranscodingHints.Key KEY_SHOW_PRINTER_DIALOG = new BooleanKey();
   public static final TranscodingHints.Key KEY_PAGE_WIDTH = new LengthKey();
   public static final TranscodingHints.Key KEY_PAGE_HEIGHT = new LengthKey();
   public static final TranscodingHints.Key KEY_MARGIN_TOP = new LengthKey();
   public static final TranscodingHints.Key KEY_MARGIN_RIGHT = new LengthKey();
   public static final TranscodingHints.Key KEY_MARGIN_BOTTOM = new LengthKey();
   public static final TranscodingHints.Key KEY_MARGIN_LEFT = new LengthKey();
   public static final TranscodingHints.Key KEY_PAGE_ORIENTATION = new StringKey();
   public static final TranscodingHints.Key KEY_SCALE_TO_PAGE = new BooleanKey();
   public static final String USAGE = "java org.apache.batik.transcoder.print.PrintTranscoder <svgFileToPrint>";

   public PrintTranscoder() {
      this.hints.put(KEY_MEDIA, "print");
   }

   public void transcode(TranscoderInput var1, TranscoderOutput var2) {
      if (var1 != null) {
         this.inputs.add(var1);
      }

   }

   protected void transcode(Document var1, String var2, TranscoderOutput var3) throws TranscoderException {
      super.transcode(var1, var2, var3);
      this.theCtx = this.ctx;
      this.ctx = null;
   }

   public void print() throws PrinterException {
      PrinterJob var1 = PrinterJob.getPrinterJob();
      PageFormat var2 = var1.defaultPage();
      Paper var3 = var2.getPaper();
      Float var4 = (Float)this.hints.get(KEY_PAGE_WIDTH);
      Float var5 = (Float)this.hints.get(KEY_PAGE_HEIGHT);
      if (var4 != null) {
         var3.setSize((double)var4, var3.getHeight());
      }

      if (var5 != null) {
         var3.setSize(var3.getWidth(), (double)var5);
      }

      float var6 = 0.0F;
      float var7 = 0.0F;
      float var8 = (float)var3.getWidth();
      float var9 = (float)var3.getHeight();
      Float var10 = (Float)this.hints.get(KEY_MARGIN_LEFT);
      Float var11 = (Float)this.hints.get(KEY_MARGIN_TOP);
      Float var12 = (Float)this.hints.get(KEY_MARGIN_RIGHT);
      Float var13 = (Float)this.hints.get(KEY_MARGIN_BOTTOM);
      if (var10 != null) {
         var6 = var10;
         var8 -= var10;
      }

      if (var11 != null) {
         var7 = var11;
         var9 -= var11;
      }

      if (var12 != null) {
         var8 -= var12;
      }

      if (var13 != null) {
         var9 -= var13;
      }

      var3.setImageableArea((double)var6, (double)var7, (double)var8, (double)var9);
      String var14 = (String)this.hints.get(KEY_PAGE_ORIENTATION);
      if ("portrait".equalsIgnoreCase(var14)) {
         var2.setOrientation(1);
      } else if ("landscape".equalsIgnoreCase(var14)) {
         var2.setOrientation(0);
      } else if ("reverseLandscape".equalsIgnoreCase(var14)) {
         var2.setOrientation(2);
      }

      var2.setPaper(var3);
      var2 = var1.validatePage(var2);
      Boolean var15 = (Boolean)this.hints.get(KEY_SHOW_PAGE_DIALOG);
      if (var15 != null && var15) {
         PageFormat var16 = var1.pageDialog(var2);
         if (var16 == var2) {
            return;
         }

         var2 = var16;
      }

      var1.setPrintable(this, var2);
      Boolean var17 = (Boolean)this.hints.get(KEY_SHOW_PRINTER_DIALOG);
      if (var17 == null || !var17 || var1.printDialog()) {
         var1.print();
      }
   }

   public int print(Graphics var1, PageFormat var2, int var3) {
      if (this.printedInputs == null) {
         this.printedInputs = new ArrayList(this.inputs);
      }

      if (var3 >= this.printedInputs.size()) {
         this.curIndex = -1;
         if (this.theCtx != null) {
            this.theCtx.dispose();
         }

         this.userAgent.displayMessage("Done");
         return 1;
      } else {
         if (this.curIndex != var3) {
            if (this.theCtx != null) {
               this.theCtx.dispose();
            }

            try {
               this.width = (float)((int)var2.getImageableWidth());
               this.height = (float)((int)var2.getImageableHeight());
               super.transcode((TranscoderInput)this.printedInputs.get(var3), (TranscoderOutput)null);
               this.curIndex = var3;
            } catch (TranscoderException var9) {
               this.drawError(var1, var9);
               return 0;
            }
         }

         Graphics2D var4 = (Graphics2D)var1;
         var4.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         var4.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         var4.setRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING, "Printing");
         AffineTransform var5 = var4.getTransform();
         Shape var6 = var4.getClip();
         var4.translate(var2.getImageableX(), var2.getImageableY());
         var4.transform(this.curTxf);

         try {
            this.root.paint(var4);
         } catch (Exception var8) {
            var4.setTransform(var5);
            var4.setClip(var6);
            this.drawError(var1, var8);
         }

         var4.setTransform(var5);
         var4.setClip(var6);
         return 0;
      }
   }

   protected void setImageSize(float var1, float var2) {
      Boolean var3 = (Boolean)this.hints.get(KEY_SCALE_TO_PAGE);
      if (var3 != null && !var3) {
         float var4 = var1;
         float var5 = var2;
         if (this.hints.containsKey(KEY_AOI)) {
            Rectangle2D var6 = (Rectangle2D)this.hints.get(KEY_AOI);
            var4 = (float)var6.getWidth();
            var5 = (float)var6.getHeight();
         }

         super.setImageSize(var4, var5);
      }

   }

   private void drawError(Graphics var1, Exception var2) {
      this.userAgent.displayError(var2);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         System.err.println("java org.apache.batik.transcoder.print.PrintTranscoder <svgFileToPrint>");
         System.exit(0);
      }

      PrintTranscoder var1 = new PrintTranscoder();
      setTranscoderFloatHint(var1, "language", KEY_LANGUAGE);
      setTranscoderFloatHint(var1, "userStylesheet", KEY_USER_STYLESHEET_URI);
      setTranscoderStringHint(var1, "xmlParserClassName", KEY_XML_PARSER_CLASSNAME);
      setTranscoderBooleanHint(var1, "scaleToPage", KEY_SCALE_TO_PAGE);
      setTranscoderRectangleHint(var1, "aoi", KEY_AOI);
      setTranscoderFloatHint(var1, "width", KEY_WIDTH);
      setTranscoderFloatHint(var1, "height", KEY_HEIGHT);
      setTranscoderFloatHint(var1, "pixelToMm", KEY_PIXEL_UNIT_TO_MILLIMETER);
      setTranscoderStringHint(var1, "pageOrientation", KEY_PAGE_ORIENTATION);
      setTranscoderFloatHint(var1, "pageWidth", KEY_PAGE_WIDTH);
      setTranscoderFloatHint(var1, "pageHeight", KEY_PAGE_HEIGHT);
      setTranscoderFloatHint(var1, "marginTop", KEY_MARGIN_TOP);
      setTranscoderFloatHint(var1, "marginRight", KEY_MARGIN_RIGHT);
      setTranscoderFloatHint(var1, "marginBottom", KEY_MARGIN_BOTTOM);
      setTranscoderFloatHint(var1, "marginLeft", KEY_MARGIN_LEFT);
      setTranscoderBooleanHint(var1, "showPageDialog", KEY_SHOW_PAGE_DIALOG);
      setTranscoderBooleanHint(var1, "showPrinterDialog", KEY_SHOW_PRINTER_DIALOG);

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.transcode(new TranscoderInput((new File(var0[var2])).toURL().toString()), (TranscoderOutput)null);
      }

      var1.print();
      System.exit(0);
   }

   public static void setTranscoderFloatHint(Transcoder var0, String var1, TranscodingHints.Key var2) {
      String var3 = System.getProperty(var1);
      if (var3 != null) {
         try {
            Float var4 = new Float(Float.parseFloat(var3));
            var0.addTranscodingHint(var2, var4);
         } catch (NumberFormatException var5) {
            handleValueError(var1, var3);
         }
      }

   }

   public static void setTranscoderRectangleHint(Transcoder var0, String var1, TranscodingHints.Key var2) {
      String var3 = System.getProperty(var1);
      if (var3 != null) {
         StringTokenizer var4 = new StringTokenizer(var3, " ,");
         if (var4.countTokens() != 4) {
            handleValueError(var1, var3);
         }

         try {
            String var5 = var4.nextToken();
            String var6 = var4.nextToken();
            String var7 = var4.nextToken();
            String var8 = var4.nextToken();
            Rectangle2D.Float var9 = new Rectangle2D.Float(Float.parseFloat(var5), Float.parseFloat(var6), Float.parseFloat(var7), Float.parseFloat(var8));
            var0.addTranscodingHint(var2, var9);
         } catch (NumberFormatException var10) {
            handleValueError(var1, var3);
         }
      }

   }

   public static void setTranscoderBooleanHint(Transcoder var0, String var1, TranscodingHints.Key var2) {
      String var3 = System.getProperty(var1);
      if (var3 != null) {
         Boolean var4 = "true".equalsIgnoreCase(var3) ? Boolean.TRUE : Boolean.FALSE;
         var0.addTranscodingHint(var2, var4);
      }

   }

   public static void setTranscoderStringHint(Transcoder var0, String var1, TranscodingHints.Key var2) {
      String var3 = System.getProperty(var1);
      if (var3 != null) {
         var0.addTranscodingHint(var2, var3);
      }

   }

   public static void handleValueError(String var0, String var1) {
      System.err.println("Invalid " + var0 + " value : " + var1);
      System.exit(1);
   }
}
