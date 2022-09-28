package com.mxgraph.reader;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class mxGraphViewImageReader extends mxGraphViewReader {
   protected Color background;
   protected boolean antiAlias;
   protected int border;
   protected boolean cropping;
   protected mxRectangle clip;

   public mxGraphViewImageReader() {
      this((Color)null);
   }

   public mxGraphViewImageReader(Color var1) {
      this(var1, 0);
   }

   public mxGraphViewImageReader(Color var1, int var2) {
      this(var1, var2, true);
   }

   public mxGraphViewImageReader(Color var1, int var2, boolean var3) {
      this(var1, var2, var3, true);
   }

   public mxGraphViewImageReader(Color var1, int var2, boolean var3, boolean var4) {
      this.setBackground(var1);
      this.setBorder(var2);
      this.setAntiAlias(var3);
      this.setCropping(var4);
   }

   public Color getBackground() {
      return this.background;
   }

   public void setBackground(Color var1) {
      this.background = var1;
   }

   public int getBorder() {
      return this.border;
   }

   public void setBorder(int var1) {
      this.border = var1;
   }

   public boolean isAntiAlias() {
      return this.antiAlias;
   }

   public void setAntiAlias(boolean var1) {
      this.antiAlias = var1;
   }

   public boolean isCropping() {
      return this.cropping;
   }

   public void setCropping(boolean var1) {
      this.cropping = var1;
   }

   public mxRectangle getClip() {
      return this.clip;
   }

   public void setClip(mxRectangle var1) {
      this.clip = var1;
   }

   public mxICanvas createCanvas(Map var1) {
      boolean var2 = false;
      boolean var3 = false;
      int var4 = 0;
      int var5 = 0;
      mxRectangle var6 = this.getClip();
      int var9;
      int var10;
      if (var6 != null) {
         var4 -= (int)var6.getX();
         var5 -= (int)var6.getY();
         var9 = (int)var6.getWidth();
         var10 = (int)var6.getHeight();
      } else {
         int var7 = (int)Math.round(mxUtils.getDouble(var1, "x"));
         int var8 = (int)Math.round(mxUtils.getDouble(var1, "y"));
         var9 = (int)Math.round(mxUtils.getDouble(var1, "width")) + this.border + 3;
         var10 = (int)Math.round(mxUtils.getDouble(var1, "height")) + this.border + 3;
         if (this.isCropping()) {
            var4 = -var7 + 3;
            var5 = -var8 + 3;
         } else {
            var9 += var7;
            var10 += var8;
         }
      }

      mxImageCanvas var11 = new mxImageCanvas(this.createGraphicsCanvas(), var9, var10, this.getBackground(), this.isAntiAlias());
      var11.setTranslate(var4, var5);
      return var11;
   }

   protected mxGraphics2DCanvas createGraphicsCanvas() {
      return new mxGraphics2DCanvas();
   }

   public static BufferedImage convert(String var0, mxGraphViewImageReader var1) throws ParserConfigurationException, SAXException, IOException {
      return convert(new InputSource(new FileInputStream(var0)), var1);
   }

   public static BufferedImage convert(InputSource var0, mxGraphViewImageReader var1) throws ParserConfigurationException, SAXException, IOException {
      BufferedImage var2 = null;
      SAXParser var3 = SAXParserFactory.newInstance().newSAXParser();
      XMLReader var4 = var3.getXMLReader();
      var4.setContentHandler(var1);
      var4.parse(var0);
      if (var1.getCanvas() instanceof mxImageCanvas) {
         var2 = ((mxImageCanvas)var1.getCanvas()).destroy();
      }

      return var2;
   }
}
