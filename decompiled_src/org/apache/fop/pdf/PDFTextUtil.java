package org.apache.fop.pdf;

import java.awt.geom.AffineTransform;

public abstract class PDFTextUtil {
   private static final int DEC = 8;
   public static final int TR_FILL = 0;
   public static final int TR_STROKE = 1;
   public static final int TR_FILL_STROKE = 2;
   public static final int TR_INVISIBLE = 3;
   public static final int TR_FILL_CLIP = 4;
   public static final int TR_STROKE_CLIP = 5;
   public static final int TR_FILL_STROKE_CLIP = 6;
   public static final int TR_CLIP = 7;
   private boolean inTextObject = false;
   private String startText;
   private String endText;
   private boolean useMultiByte;
   private StringBuffer bufTJ;
   private int textRenderingMode = 0;
   private String currentFontName;
   private double currentFontSize;

   protected abstract void write(String var1);

   private void writeAffineTransform(AffineTransform at, StringBuffer sb) {
      double[] lt = new double[6];
      at.getMatrix(lt);
      sb.append(PDFNumber.doubleOut(lt[0], 8)).append(" ");
      sb.append(PDFNumber.doubleOut(lt[1], 8)).append(" ");
      sb.append(PDFNumber.doubleOut(lt[2], 8)).append(" ");
      sb.append(PDFNumber.doubleOut(lt[3], 8)).append(" ");
      sb.append(PDFNumber.doubleOut(lt[4], 8)).append(" ");
      sb.append(PDFNumber.doubleOut(lt[5], 8));
   }

   private void writeChar(char ch, StringBuffer sb) {
      if (!this.useMultiByte) {
         if (ch >= ' ' && ch <= 127) {
            switch (ch) {
               case '(':
               case ')':
               case '\\':
                  sb.append("\\");
               default:
                  sb.append(ch);
            }
         } else {
            sb.append("\\").append(Integer.toOctalString(ch));
         }
      } else {
         sb.append(PDFText.toUnicodeHex(ch));
      }

   }

   private void checkInTextObject() {
      if (!this.inTextObject) {
         throw new IllegalStateException("Not in text object");
      }
   }

   public boolean isInTextObject() {
      return this.inTextObject;
   }

   public void beginTextObject() {
      if (this.inTextObject) {
         throw new IllegalStateException("Already in text object");
      } else {
         this.write("BT\n");
         this.inTextObject = true;
      }
   }

   public void endTextObject() {
      this.checkInTextObject();
      this.write("ET\n");
      this.inTextObject = false;
      this.initValues();
   }

   protected void initValues() {
      this.currentFontName = null;
      this.currentFontSize = 0.0;
      this.textRenderingMode = 0;
   }

   public void saveGraphicsState() {
      this.write("q\n");
   }

   public void restoreGraphicsState() {
      this.write("Q\n");
   }

   public void concatMatrix(AffineTransform at) {
      if (!at.isIdentity()) {
         this.writeTJ();
         StringBuffer sb = new StringBuffer();
         this.writeAffineTransform(at, sb);
         sb.append(" cm\n");
         this.write(sb.toString());
      }

   }

   public void writeTf(String fontName, double fontSize) {
      this.checkInTextObject();
      this.write("/" + fontName + " " + PDFNumber.doubleOut(fontSize) + " Tf\n");
      this.startText = this.useMultiByte ? "<" : "(";
      this.endText = this.useMultiByte ? ">" : ")";
   }

   public void updateTf(String fontName, double fontSize, boolean multiByte) {
      this.checkInTextObject();
      if (!fontName.equals(this.currentFontName) || fontSize != this.currentFontSize) {
         this.writeTJ();
         this.currentFontName = fontName;
         this.currentFontSize = fontSize;
         this.useMultiByte = multiByte;
         this.writeTf(fontName, fontSize);
      }

   }

   public void setTextRenderingMode(int mode) {
      if (mode >= 0 && mode <= 7) {
         if (mode != this.textRenderingMode) {
            this.writeTJ();
            this.textRenderingMode = mode;
            this.write(this.textRenderingMode + " Tr\n");
         }

      } else {
         throw new IllegalArgumentException("Illegal value for text rendering mode. Expected: 0-7");
      }
   }

   public void setTextRenderingMode(boolean fill, boolean stroke, boolean addToClip) {
      int mode;
      if (fill) {
         mode = stroke ? 2 : 0;
      } else {
         mode = stroke ? 1 : 3;
      }

      if (addToClip) {
         mode += 4;
      }

      this.setTextRenderingMode(mode);
   }

   public void writeTextMatrix(AffineTransform localTransform) {
      StringBuffer sb = new StringBuffer();
      this.writeAffineTransform(localTransform, sb);
      sb.append(" Tm ");
      this.write(sb.toString());
   }

   public void writeTJMappedChar(char codepoint) {
      if (this.bufTJ == null) {
         this.bufTJ = new StringBuffer();
      }

      if (this.bufTJ.length() == 0) {
         this.bufTJ.append("[").append(this.startText);
      }

      this.writeChar(codepoint, this.bufTJ);
   }

   public void adjustGlyphTJ(double adjust) {
      if (this.bufTJ == null) {
         this.bufTJ = new StringBuffer();
      }

      if (this.bufTJ.length() > 0) {
         this.bufTJ.append(this.endText).append(" ");
      }

      if (this.bufTJ.length() == 0) {
         this.bufTJ.append("[");
      }

      this.bufTJ.append(PDFNumber.doubleOut(adjust, 4));
      this.bufTJ.append(" ");
      this.bufTJ.append(this.startText);
   }

   public void writeTJ() {
      if (this.isInString()) {
         this.bufTJ.append(this.endText).append("] TJ\n");
         this.write(this.bufTJ.toString());
         this.bufTJ.setLength(0);
      }

   }

   private boolean isInString() {
      return this.bufTJ != null && this.bufTJ.length() > 0;
   }
}
