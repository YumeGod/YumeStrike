package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.ToSVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WMFTranscoder extends ToSVGAbstractTranscoder {
   public static final String WMF_EXTENSION = ".wmf";
   public static final String SVG_EXTENSION = ".svg";

   public void transcode(TranscoderInput var1, TranscoderOutput var2) throws TranscoderException {
      DataInputStream var3 = this.getCompatibleInput(var1);
      WMFRecordStore var4 = new WMFRecordStore();

      try {
         var4.read(var3);
      } catch (IOException var20) {
         this.handler.fatalError(new TranscoderException(var20));
         return;
      }

      float var7 = 1.0F;
      float var5;
      float var6;
      if (this.hints.containsKey(KEY_INPUT_WIDTH)) {
         var5 = (float)(Integer)this.hints.get(KEY_INPUT_WIDTH);
         var6 = (float)(Integer)this.hints.get(KEY_INPUT_HEIGHT);
      } else {
         var5 = (float)var4.getWidthPixels();
         var6 = (float)var4.getHeightPixels();
      }

      if (this.hints.containsKey(KEY_WIDTH)) {
         float var8 = (Float)this.hints.get(KEY_WIDTH);
         var7 = var8 / var5;
         float var9 = var6 * var8 / var5;
      }

      int var10 = 0;
      int var11 = 0;
      if (this.hints.containsKey(KEY_XOFFSET)) {
         var10 = (Integer)this.hints.get(KEY_XOFFSET);
      }

      if (this.hints.containsKey(KEY_YOFFSET)) {
         var11 = (Integer)this.hints.get(KEY_YOFFSET);
      }

      float var12 = var4.getUnitsToPixels() * var7;
      int var13 = (int)(var4.getVpX() * var12);
      int var14 = (int)(var4.getVpY() * var12);
      int var15;
      int var16;
      if (this.hints.containsKey(KEY_INPUT_WIDTH)) {
         var15 = (int)((float)(Integer)this.hints.get(KEY_INPUT_WIDTH) * var7);
         var16 = (int)((float)(Integer)this.hints.get(KEY_INPUT_HEIGHT) * var7);
      } else {
         var15 = (int)((float)var4.getWidthUnits() * var12);
         var16 = (int)((float)var4.getHeightUnits() * var12);
      }

      WMFPainter var17 = new WMFPainter(var4, var10, var11, var7);
      Document var18 = this.createDocument(var2);
      this.svgGenerator = new SVGGraphics2D(var18);
      this.svgGenerator.getGeneratorContext().setPrecision(4);
      var17.paint(this.svgGenerator);
      this.svgGenerator.setSVGCanvasSize(new Dimension(var15, var16));
      Element var19 = this.svgGenerator.getRoot();
      var19.setAttributeNS((String)null, "viewBox", String.valueOf(var13) + ' ' + var14 + ' ' + var15 + ' ' + var16);
      this.writeSVGToOutput(this.svgGenerator, var19, var2);
   }

   private DataInputStream getCompatibleInput(TranscoderInput var1) throws TranscoderException {
      if (var1 == null) {
         this.handler.fatalError(new TranscoderException(String.valueOf(65280)));
      }

      InputStream var2 = var1.getInputStream();
      if (var2 != null) {
         return new DataInputStream(new BufferedInputStream(var2));
      } else {
         String var3 = var1.getURI();
         if (var3 != null) {
            try {
               URL var4 = new URL(var3);
               var2 = var4.openStream();
               return new DataInputStream(new BufferedInputStream(var2));
            } catch (MalformedURLException var5) {
               this.handler.fatalError(new TranscoderException(var5));
            } catch (IOException var6) {
               this.handler.fatalError(new TranscoderException(var6));
            }
         }

         this.handler.fatalError(new TranscoderException(String.valueOf(65281)));
         return null;
      }
   }

   public static void main(String[] var0) throws TranscoderException {
      if (var0.length < 1) {
         System.out.println("Usage : WMFTranscoder.main <file 1> ... <file n>");
         System.exit(1);
      }

      WMFTranscoder var1 = new WMFTranscoder();
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var0[var3];
         if (!var4.toLowerCase().endsWith(".wmf")) {
            System.err.println(var0[var3] + " does not have the " + ".wmf" + " extension. It is ignored");
         } else {
            System.out.print("Processing : " + var0[var3] + "...");
            String var5 = var4.substring(0, var4.toLowerCase().indexOf(".wmf")) + ".svg";
            File var6 = new File(var4);
            File var7 = new File(var5);

            try {
               TranscoderInput var8 = new TranscoderInput(var6.toURL().toString());
               TranscoderOutput var9 = new TranscoderOutput(new FileOutputStream(var7));
               var1.transcode(var8, var9);
            } catch (MalformedURLException var10) {
               throw new TranscoderException(var10);
            } catch (IOException var11) {
               throw new TranscoderException(var11);
            }

            System.out.println(".... Done");
         }
      }

      System.exit(0);
   }
}
