package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public abstract class AbstractColorManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();
   protected static final StringMap computedValues;

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      if (var1.getLexicalUnitType() == 27) {
         var1 = var1.getParameters();
         Value var3 = this.createColorComponent(var1);
         var1 = var1.getNextLexicalUnit().getNextLexicalUnit();
         Value var4 = this.createColorComponent(var1);
         var1 = var1.getNextLexicalUnit().getNextLexicalUnit();
         Value var5 = this.createColorComponent(var1);
         return this.createRGBColor(var3, var4, var5);
      } else {
         return super.createValue(var1, var2);
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6.getPrimitiveType() == 21) {
         String var7 = var6.getStringValue();
         Value var8 = (Value)computedValues.get(var7);
         if (var8 != null) {
            return var8;
         } else if (values.get(var7) == null) {
            throw new IllegalStateException("Not a system-color:" + var7);
         } else {
            return var3.getCSSContext().getSystemColor(var7);
         }
      } else {
         return super.computeValue(var1, var2, var3, var4, var5, var6);
      }
   }

   protected Value createRGBColor(Value var1, Value var2, Value var3) {
      return new RGBColorValue(var1, var2, var3);
   }

   protected Value createColorComponent(LexicalUnit var1) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 13:
            return new FloatValue((short)1, (float)var1.getIntegerValue());
         case 14:
            return new FloatValue((short)1, var1.getFloatValue());
         case 23:
            return new FloatValue((short)2, var1.getFloatValue());
         default:
            throw this.createInvalidRGBComponentUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public StringMap getIdentifiers() {
      return values;
   }

   private DOMException createInvalidRGBComponentUnitDOMException(short var1) {
      Object[] var2 = new Object[]{this.getPropertyName(), new Integer(var1)};
      String var3 = Messages.formatMessage("invalid.rgb.component.unit", var2);
      return new DOMException((short)9, var3);
   }

   static {
      values.put("aqua", ValueConstants.AQUA_VALUE);
      values.put("black", ValueConstants.BLACK_VALUE);
      values.put("blue", ValueConstants.BLUE_VALUE);
      values.put("fuchsia", ValueConstants.FUCHSIA_VALUE);
      values.put("gray", ValueConstants.GRAY_VALUE);
      values.put("green", ValueConstants.GREEN_VALUE);
      values.put("lime", ValueConstants.LIME_VALUE);
      values.put("maroon", ValueConstants.MAROON_VALUE);
      values.put("navy", ValueConstants.NAVY_VALUE);
      values.put("olive", ValueConstants.OLIVE_VALUE);
      values.put("purple", ValueConstants.PURPLE_VALUE);
      values.put("red", ValueConstants.RED_VALUE);
      values.put("silver", ValueConstants.SILVER_VALUE);
      values.put("teal", ValueConstants.TEAL_VALUE);
      values.put("white", ValueConstants.WHITE_VALUE);
      values.put("yellow", ValueConstants.YELLOW_VALUE);
      values.put("activeborder", ValueConstants.ACTIVEBORDER_VALUE);
      values.put("activecaption", ValueConstants.ACTIVECAPTION_VALUE);
      values.put("appworkspace", ValueConstants.APPWORKSPACE_VALUE);
      values.put("background", ValueConstants.BACKGROUND_VALUE);
      values.put("buttonface", ValueConstants.BUTTONFACE_VALUE);
      values.put("buttonhighlight", ValueConstants.BUTTONHIGHLIGHT_VALUE);
      values.put("buttonshadow", ValueConstants.BUTTONSHADOW_VALUE);
      values.put("buttontext", ValueConstants.BUTTONTEXT_VALUE);
      values.put("captiontext", ValueConstants.CAPTIONTEXT_VALUE);
      values.put("graytext", ValueConstants.GRAYTEXT_VALUE);
      values.put("highlight", ValueConstants.HIGHLIGHT_VALUE);
      values.put("highlighttext", ValueConstants.HIGHLIGHTTEXT_VALUE);
      values.put("inactiveborder", ValueConstants.INACTIVEBORDER_VALUE);
      values.put("inactivecaption", ValueConstants.INACTIVECAPTION_VALUE);
      values.put("inactivecaptiontext", ValueConstants.INACTIVECAPTIONTEXT_VALUE);
      values.put("infobackground", ValueConstants.INFOBACKGROUND_VALUE);
      values.put("infotext", ValueConstants.INFOTEXT_VALUE);
      values.put("menu", ValueConstants.MENU_VALUE);
      values.put("menutext", ValueConstants.MENUTEXT_VALUE);
      values.put("scrollbar", ValueConstants.SCROLLBAR_VALUE);
      values.put("threeddarkshadow", ValueConstants.THREEDDARKSHADOW_VALUE);
      values.put("threedface", ValueConstants.THREEDFACE_VALUE);
      values.put("threedhighlight", ValueConstants.THREEDHIGHLIGHT_VALUE);
      values.put("threedlightshadow", ValueConstants.THREEDLIGHTSHADOW_VALUE);
      values.put("threedshadow", ValueConstants.THREEDSHADOW_VALUE);
      values.put("window", ValueConstants.WINDOW_VALUE);
      values.put("windowframe", ValueConstants.WINDOWFRAME_VALUE);
      values.put("windowtext", ValueConstants.WINDOWTEXT_VALUE);
      computedValues = new StringMap();
      computedValues.put("black", ValueConstants.BLACK_RGB_VALUE);
      computedValues.put("silver", ValueConstants.SILVER_RGB_VALUE);
      computedValues.put("gray", ValueConstants.GRAY_RGB_VALUE);
      computedValues.put("white", ValueConstants.WHITE_RGB_VALUE);
      computedValues.put("maroon", ValueConstants.MAROON_RGB_VALUE);
      computedValues.put("red", ValueConstants.RED_RGB_VALUE);
      computedValues.put("purple", ValueConstants.PURPLE_RGB_VALUE);
      computedValues.put("fuchsia", ValueConstants.FUCHSIA_RGB_VALUE);
      computedValues.put("green", ValueConstants.GREEN_RGB_VALUE);
      computedValues.put("lime", ValueConstants.LIME_RGB_VALUE);
      computedValues.put("olive", ValueConstants.OLIVE_RGB_VALUE);
      computedValues.put("yellow", ValueConstants.YELLOW_RGB_VALUE);
      computedValues.put("navy", ValueConstants.NAVY_RGB_VALUE);
      computedValues.put("blue", ValueConstants.BLUE_RGB_VALUE);
      computedValues.put("teal", ValueConstants.TEAL_RGB_VALUE);
      computedValues.put("aqua", ValueConstants.AQUA_RGB_VALUE);
   }
}
