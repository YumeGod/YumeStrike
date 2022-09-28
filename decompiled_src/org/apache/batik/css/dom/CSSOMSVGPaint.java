package org.apache.batik.css.dom;

import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.ICCColor;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGPaint;

public class CSSOMSVGPaint extends CSSOMSVGColor implements SVGPaint {
   public CSSOMSVGPaint(CSSOMSVGColor.ValueProvider var1) {
      super(var1);
   }

   public void setModificationHandler(CSSOMSVGColor.ModificationHandler var1) {
      if (!(var1 instanceof PaintModificationHandler)) {
         throw new IllegalArgumentException();
      } else {
         super.setModificationHandler(var1);
      }
   }

   public short getColorType() {
      throw new DOMException((short)15, "");
   }

   public short getPaintType() {
      Value var1 = this.valueProvider.getValue();
      switch (var1.getCssValueType()) {
         case 1:
            switch (var1.getPrimitiveType()) {
               case 20:
                  return 107;
               case 21:
                  String var5 = var1.getStringValue();
                  if (var5.equalsIgnoreCase("none")) {
                     return 101;
                  }

                  if (var5.equalsIgnoreCase("currentcolor")) {
                     return 102;
                  }

                  return 1;
               case 25:
                  return 1;
               default:
                  return 0;
            }
         case 2:
            Value var2 = var1.item(0);
            Value var3 = var1.item(1);
            switch (var2.getPrimitiveType()) {
               case 20:
                  if (var3.getCssValueType() == 2) {
                     return 106;
                  }

                  switch (var3.getPrimitiveType()) {
                     case 21:
                        String var4 = var3.getStringValue();
                        if (var4.equalsIgnoreCase("none")) {
                           return 103;
                        }

                        if (var4.equalsIgnoreCase("currentcolor")) {
                           return 104;
                        }

                        return 105;
                     case 25:
                        return 105;
                  }
               case 25:
                  return 2;
               case 21:
                  return 2;
            }
      }

      return 0;
   }

   public String getUri() {
      switch (this.getPaintType()) {
         case 103:
         case 104:
         case 105:
         case 106:
            return this.valueProvider.getValue().item(0).getStringValue();
         case 107:
            return this.valueProvider.getValue().getStringValue();
         default:
            throw new InternalError();
      }
   }

   public void setUri(String var1) {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         ((PaintModificationHandler)this.handler).uriChanged(var1);
      }
   }

   public void setPaint(short var1, String var2, String var3, String var4) {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         ((PaintModificationHandler)this.handler).paintChanged(var1, var2, var3, var4);
      }
   }

   public abstract class AbstractModificationHandler implements PaintModificationHandler {
      protected abstract Value getValue();

      public void redTextChanged(String var1) throws DOMException {
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               var1 = "rgb(" + var1 + ", " + this.getValue().getGreen().getCssText() + ", " + this.getValue().getBlue().getCssText() + ')';
               break;
            case 2:
               var1 = "rgb(" + var1 + ", " + this.getValue().item(0).getGreen().getCssText() + ", " + this.getValue().item(0).getBlue().getCssText() + ") " + this.getValue().item(1).getCssText();
               break;
            case 105:
               var1 = this.getValue().item(0) + " rgb(" + var1 + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + this.getValue().item(1).getBlue().getCssText() + ')';
               break;
            case 106:
               var1 = this.getValue().item(0) + " rgb(" + var1 + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + this.getValue().item(1).getBlue().getCssText() + ") " + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var1);
      }

      public void redFloatValueChanged(short var1, float var2) throws DOMException {
         String var3;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               var3 = "rgb(" + FloatValue.getCssText(var1, var2) + ", " + this.getValue().getGreen().getCssText() + ", " + this.getValue().getBlue().getCssText() + ')';
               break;
            case 2:
               var3 = "rgb(" + FloatValue.getCssText(var1, var2) + ", " + this.getValue().item(0).getGreen().getCssText() + ", " + this.getValue().item(0).getBlue().getCssText() + ") " + this.getValue().item(1).getCssText();
               break;
            case 105:
               var3 = this.getValue().item(0) + " rgb(" + FloatValue.getCssText(var1, var2) + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + this.getValue().item(1).getBlue().getCssText() + ')';
               break;
            case 106:
               var3 = this.getValue().item(0) + " rgb(" + FloatValue.getCssText(var1, var2) + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + this.getValue().item(1).getBlue().getCssText() + ") " + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var3);
      }

      public void greenTextChanged(String var1) throws DOMException {
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               var1 = "rgb(" + this.getValue().getRed().getCssText() + ", " + var1 + ", " + this.getValue().getBlue().getCssText() + ')';
               break;
            case 2:
               var1 = "rgb(" + this.getValue().item(0).getRed().getCssText() + ", " + var1 + ", " + this.getValue().item(0).getBlue().getCssText() + ") " + this.getValue().item(1).getCssText();
               break;
            case 105:
               var1 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + var1 + ", " + this.getValue().item(1).getBlue().getCssText() + ')';
               break;
            case 106:
               var1 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + var1 + ", " + this.getValue().item(1).getBlue().getCssText() + ") " + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var1);
      }

      public void greenFloatValueChanged(short var1, float var2) throws DOMException {
         String var3;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               var3 = "rgb(" + this.getValue().getRed().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + this.getValue().getBlue().getCssText() + ')';
               break;
            case 2:
               var3 = "rgb(" + this.getValue().item(0).getRed().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + this.getValue().item(0).getBlue().getCssText() + ") " + this.getValue().item(1).getCssText();
               break;
            case 105:
               var3 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + this.getValue().item(1).getBlue().getCssText() + ')';
               break;
            case 106:
               var3 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + this.getValue().item(1).getBlue().getCssText() + ") " + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var3);
      }

      public void blueTextChanged(String var1) throws DOMException {
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               var1 = "rgb(" + this.getValue().getRed().getCssText() + ", " + this.getValue().getGreen().getCssText() + ", " + var1 + ')';
               break;
            case 2:
               var1 = "rgb(" + this.getValue().item(0).getRed().getCssText() + ", " + this.getValue().item(0).getGreen().getCssText() + ", " + var1 + ") " + this.getValue().item(1).getCssText();
               break;
            case 105:
               var1 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + var1 + ")";
               break;
            case 106:
               var1 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + var1 + ") " + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var1);
      }

      public void blueFloatValueChanged(short var1, float var2) throws DOMException {
         String var3;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               var3 = "rgb(" + this.getValue().getRed().getCssText() + ", " + this.getValue().getGreen().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ')';
               break;
            case 2:
               var3 = "rgb(" + this.getValue().item(0).getRed().getCssText() + ", " + this.getValue().item(0).getGreen().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ") " + this.getValue().item(1).getCssText();
               break;
            case 105:
               var3 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ')';
               break;
            case 106:
               var3 = this.getValue().item(0) + " rgb(" + this.getValue().item(1).getRed().getCssText() + ", " + this.getValue().item(1).getGreen().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ") " + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var3);
      }

      public void rgbColorChanged(String var1) throws DOMException {
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 1:
               break;
            case 2:
               var1 = var1 + this.getValue().item(1).getCssText();
               break;
            case 105:
               var1 = this.getValue().item(0).getCssText() + ' ' + var1;
               break;
            case 106:
               var1 = this.getValue().item(0).getCssText() + ' ' + var1 + ' ' + this.getValue().item(2).getCssText();
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var1);
      }

      public void rgbColorICCColorChanged(String var1, String var2) throws DOMException {
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               this.textChanged(var1 + ' ' + var2);
               break;
            case 106:
               this.textChanged(this.getValue().item(0).getCssText() + ' ' + var1 + ' ' + var2);
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorChanged(short var1, String var2, String var3) throws DOMException {
         switch (var1) {
            case 1:
               this.textChanged(var2);
               break;
            case 2:
               this.textChanged(var2 + ' ' + var3);
               break;
            case 102:
               this.textChanged("currentcolor");
               break;
            default:
               throw new DOMException((short)9, "");
         }

      }

      public void colorProfileChanged(String var1) throws DOMException {
         StringBuffer var2;
         ICCColor var3;
         int var4;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(" icc-color(");
               var2.append(var1);
               var3 = (ICCColor)this.getValue().item(1);

               for(var4 = 0; var4 < var3.getLength(); ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               var2.append(')');
               this.textChanged(var2.toString());
               break;
            case 106:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(' ');
               var2.append(this.getValue().item(1).getCssText());
               var2.append(" icc-color(");
               var2.append(var1);
               var3 = (ICCColor)this.getValue().item(1);

               for(var4 = 0; var4 < var3.getLength(); ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               var2.append(')');
               this.textChanged(var2.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorsCleared() throws DOMException {
         StringBuffer var1;
         ICCColor var2;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var1 = new StringBuffer(this.getValue().item(0).getCssText());
               var1.append(" icc-color(");
               var2 = (ICCColor)this.getValue().item(1);
               var1.append(var2.getColorProfile());
               var1.append(')');
               this.textChanged(var1.toString());
               break;
            case 106:
               var1 = new StringBuffer(this.getValue().item(0).getCssText());
               var1.append(' ');
               var1.append(this.getValue().item(1).getCssText());
               var1.append(" icc-color(");
               var2 = (ICCColor)this.getValue().item(1);
               var1.append(var2.getColorProfile());
               var1.append(')');
               this.textChanged(var1.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorsInitialized(float var1) throws DOMException {
         StringBuffer var2;
         ICCColor var3;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(" icc-color(");
               var3 = (ICCColor)this.getValue().item(1);
               var2.append(var3.getColorProfile());
               var2.append(',');
               var2.append(var1);
               var2.append(')');
               this.textChanged(var2.toString());
               break;
            case 106:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(' ');
               var2.append(this.getValue().item(1).getCssText());
               var2.append(" icc-color(");
               var3 = (ICCColor)this.getValue().item(1);
               var2.append(var3.getColorProfile());
               var2.append(',');
               var2.append(var1);
               var2.append(')');
               this.textChanged(var2.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorInsertedBefore(float var1, int var2) throws DOMException {
         StringBuffer var3;
         ICCColor var4;
         int var5;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var3 = new StringBuffer(this.getValue().item(0).getCssText());
               var3.append(" icc-color(");
               var4 = (ICCColor)this.getValue().item(1);
               var3.append(var4.getColorProfile());

               for(var5 = 0; var5 < var2; ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(',');
               var3.append(var1);

               for(var5 = var2; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(')');
               this.textChanged(var3.toString());
               break;
            case 106:
               var3 = new StringBuffer(this.getValue().item(0).getCssText());
               var3.append(' ');
               var3.append(this.getValue().item(1).getCssText());
               var3.append(" icc-color(");
               var4 = (ICCColor)this.getValue().item(1);
               var3.append(var4.getColorProfile());

               for(var5 = 0; var5 < var2; ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(',');
               var3.append(var1);

               for(var5 = var2; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(')');
               this.textChanged(var3.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorReplaced(float var1, int var2) throws DOMException {
         StringBuffer var3;
         ICCColor var4;
         int var5;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var3 = new StringBuffer(this.getValue().item(0).getCssText());
               var3.append(" icc-color(");
               var4 = (ICCColor)this.getValue().item(1);
               var3.append(var4.getColorProfile());

               for(var5 = 0; var5 < var2; ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(',');
               var3.append(var1);

               for(var5 = var2 + 1; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(')');
               this.textChanged(var3.toString());
               break;
            case 106:
               var3 = new StringBuffer(this.getValue().item(0).getCssText());
               var3.append(' ');
               var3.append(this.getValue().item(1).getCssText());
               var3.append(" icc-color(");
               var4 = (ICCColor)this.getValue().item(1);
               var3.append(var4.getColorProfile());

               for(var5 = 0; var5 < var2; ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(',');
               var3.append(var1);

               for(var5 = var2 + 1; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(')');
               this.textChanged(var3.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorRemoved(int var1) throws DOMException {
         StringBuffer var2;
         ICCColor var3;
         int var4;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(" icc-color(");
               var3 = (ICCColor)this.getValue().item(1);
               var2.append(var3.getColorProfile());

               for(var4 = 0; var4 < var1; ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               for(var4 = var1 + 1; var4 < var3.getLength(); ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               var2.append(')');
               this.textChanged(var2.toString());
               break;
            case 106:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(' ');
               var2.append(this.getValue().item(1).getCssText());
               var2.append(" icc-color(");
               var3 = (ICCColor)this.getValue().item(1);
               var2.append(var3.getColorProfile());

               for(var4 = 0; var4 < var1; ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               for(var4 = var1 + 1; var4 < var3.getLength(); ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               var2.append(')');
               this.textChanged(var2.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void colorAppend(float var1) throws DOMException {
         StringBuffer var2;
         ICCColor var3;
         int var4;
         switch (CSSOMSVGPaint.this.getPaintType()) {
            case 2:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(" icc-color(");
               var3 = (ICCColor)this.getValue().item(1);
               var2.append(var3.getColorProfile());

               for(var4 = 0; var4 < var3.getLength(); ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               var2.append(',');
               var2.append(var1);
               var2.append(')');
               this.textChanged(var2.toString());
               break;
            case 106:
               var2 = new StringBuffer(this.getValue().item(0).getCssText());
               var2.append(' ');
               var2.append(this.getValue().item(1).getCssText());
               var2.append(" icc-color(");
               var3 = (ICCColor)this.getValue().item(1);
               var2.append(var3.getColorProfile());

               for(var4 = 0; var4 < var3.getLength(); ++var4) {
                  var2.append(',');
                  var2.append(var3.getColor(var4));
               }

               var2.append(',');
               var2.append(var1);
               var2.append(')');
               this.textChanged(var2.toString());
               break;
            default:
               throw new DOMException((short)7, "");
         }

      }

      public void uriChanged(String var1) {
         this.textChanged("url(" + var1 + ") none");
      }

      public void paintChanged(short var1, String var2, String var3, String var4) {
         switch (var1) {
            case 1:
               this.textChanged(var3);
               break;
            case 2:
               this.textChanged(var3 + ' ' + var4);
               break;
            case 101:
               this.textChanged("none");
               break;
            case 102:
               this.textChanged("currentcolor");
               break;
            case 103:
               this.textChanged("url(" + var2 + ") none");
               break;
            case 104:
               this.textChanged("url(" + var2 + ") currentcolor");
               break;
            case 105:
               this.textChanged("url(" + var2 + ") " + var3);
               break;
            case 106:
               this.textChanged("url(" + var2 + ") " + var3 + ' ' + var4);
               break;
            case 107:
               this.textChanged("url(" + var2 + ')');
         }

      }
   }

   public interface PaintModificationHandler extends CSSOMSVGColor.ModificationHandler {
      void uriChanged(String var1);

      void paintChanged(short var1, String var2, String var3, String var4);
   }
}
