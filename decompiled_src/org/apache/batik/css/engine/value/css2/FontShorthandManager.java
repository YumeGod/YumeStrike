package org.apache.batik.css.engine.value.css2;

import java.util.HashSet;
import java.util.Set;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueFactory;
import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.CSSLexicalUnit;
import org.w3c.css.sac.LexicalUnit;

public class FontShorthandManager extends AbstractValueFactory implements ShorthandManager {
   static LexicalUnit NORMAL_LU = CSSLexicalUnit.createString((short)35, "normal", (LexicalUnit)null);
   static LexicalUnit BOLD_LU = CSSLexicalUnit.createString((short)35, "bold", (LexicalUnit)null);
   static LexicalUnit MEDIUM_LU = CSSLexicalUnit.createString((short)35, "medium", (LexicalUnit)null);
   static LexicalUnit SZ_10PT_LU = CSSLexicalUnit.createFloat((short)21, 10.0F, (LexicalUnit)null);
   static LexicalUnit SZ_8PT_LU = CSSLexicalUnit.createFloat((short)21, 8.0F, (LexicalUnit)null);
   static LexicalUnit FONT_FAMILY_LU = CSSLexicalUnit.createString((short)35, "Dialog", (LexicalUnit)null);
   protected static final Set values;

   public String getPropertyName() {
      return "font";
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public void handleSystemFont(CSSEngine var1, ShorthandManager.PropertyHandler var2, String var3, boolean var4) {
      LexicalUnit var5 = NORMAL_LU;
      LexicalUnit var6 = NORMAL_LU;
      LexicalUnit var7 = NORMAL_LU;
      LexicalUnit var8 = NORMAL_LU;
      LexicalUnit var9 = FONT_FAMILY_LU;
      LexicalUnit var10;
      if (var3.equals("small-caption")) {
         var10 = SZ_8PT_LU;
      } else {
         var10 = SZ_10PT_LU;
      }

      var2.property("font-family", var9, var4);
      var2.property("font-style", var5, var4);
      var2.property("font-variant", var6, var4);
      var2.property("font-weight", var7, var4);
      var2.property("font-size", var10, var4);
      var2.property("line-height", var8, var4);
   }

   public void setValues(CSSEngine var1, ShorthandManager.PropertyHandler var2, LexicalUnit var3, boolean var4) {
      switch (var3.getLexicalUnitType()) {
         case 12:
            return;
         case 35:
            String var5 = var3.getStringValue().toLowerCase();
            if (values.contains(var5)) {
               this.handleSystemFont(var1, var2, var5, var4);
               return;
            }
         default:
            LexicalUnit var28 = null;
            LexicalUnit var6 = null;
            LexicalUnit var7 = null;
            LexicalUnit var8 = null;
            LexicalUnit var9 = null;
            Object var10 = null;
            ValueManager[] var11 = var1.getValueManagers();
            int var12 = var1.getPropertyIndex("font-style");
            int var13 = var1.getPropertyIndex("font-variant");
            int var14 = var1.getPropertyIndex("font-weight");
            int var15 = var1.getPropertyIndex("font-size");
            int var16 = var1.getPropertyIndex("line-height");
            IdentifierManager var17 = (IdentifierManager)var11[var12];
            IdentifierManager var18 = (IdentifierManager)var11[var13];
            IdentifierManager var19 = (IdentifierManager)var11[var14];
            FontSizeManager var20 = (FontSizeManager)var11[var15];
            StringMap var21 = var17.getIdentifiers();
            StringMap var22 = var18.getIdentifiers();
            StringMap var23 = var19.getIdentifiers();
            StringMap var24 = var20.getIdentifiers();
            boolean var25 = false;
            LexicalUnit var26 = null;

            String var27;
            while(!var25 && var3 != null) {
               switch (var3.getLexicalUnitType()) {
                  case 13:
                     if (var26 == null && var7 == null) {
                        var26 = var3;
                     } else {
                        var25 = true;
                     }
                     break;
                  case 35:
                     var27 = var3.getStringValue().toLowerCase().intern();
                     if (var28 == null && var21.get(var27) != null) {
                        var28 = var3;
                        if (var26 != null) {
                           if (var7 != null) {
                              throw this.createInvalidLexicalUnitDOMException(var26.getLexicalUnitType());
                           }

                           var7 = var26;
                           var26 = null;
                        }
                     } else if (var6 == null && var22.get(var27) != null) {
                        var6 = var3;
                        if (var26 != null) {
                           if (var7 != null) {
                              throw this.createInvalidLexicalUnitDOMException(var26.getLexicalUnitType());
                           }

                           var7 = var26;
                           var26 = null;
                        }
                     } else if (var26 == null && var7 == null && var23.get(var27) != null) {
                        var7 = var3;
                     } else {
                        var25 = true;
                     }
                     break;
                  default:
                     var25 = true;
               }

               if (!var25) {
                  var3 = var3.getNextLexicalUnit();
               }
            }

            if (var3 == null) {
               throw this.createMalformedLexicalUnitDOMException();
            } else {
               switch (var3.getLexicalUnitType()) {
                  case 13:
                  case 14:
                  case 15:
                  case 16:
                  case 17:
                  case 18:
                  case 19:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                     var8 = var3;
                     var3 = var3.getNextLexicalUnit();
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                  case 28:
                  case 29:
                  case 30:
                  case 31:
                  case 32:
                  case 33:
                  case 34:
                  default:
                     break;
                  case 35:
                     var27 = var3.getStringValue().toLowerCase().intern();
                     if (var24.get(var27) != null) {
                        var8 = var3;
                        var3 = var3.getNextLexicalUnit();
                     }
               }

               if (var8 == null) {
                  if (var26 == null) {
                     throw this.createInvalidLexicalUnitDOMException(var3.getLexicalUnitType());
                  }

                  var8 = var26;
                  var26 = null;
               }

               if (var26 != null) {
                  if (var7 != null) {
                     throw this.createInvalidLexicalUnitDOMException(var26.getLexicalUnitType());
                  }

                  var7 = var26;
               }

               if (var3 == null) {
                  throw this.createMalformedLexicalUnitDOMException();
               } else {
                  switch (var3.getLexicalUnitType()) {
                     case 4:
                        var3 = var3.getNextLexicalUnit();
                        if (var3 == null) {
                           throw this.createMalformedLexicalUnitDOMException();
                        } else {
                           var9 = var3;
                           var3 = var3.getNextLexicalUnit();
                        }
                     default:
                        if (var3 == null) {
                           throw this.createMalformedLexicalUnitDOMException();
                        } else {
                           if (var28 == null) {
                              var28 = NORMAL_LU;
                           }

                           if (var6 == null) {
                              var6 = NORMAL_LU;
                           }

                           if (var7 == null) {
                              var7 = NORMAL_LU;
                           }

                           if (var9 == null) {
                              var9 = NORMAL_LU;
                           }

                           var2.property("font-family", var3, var4);
                           var2.property("font-style", var28, var4);
                           var2.property("font-variant", var6, var4);
                           var2.property("font-weight", var7, var4);
                           var2.property("font-size", var8, var4);
                           if (var16 != -1) {
                              var2.property("line-height", var9, var4);
                           }

                        }
                  }
               }
            }
      }
   }

   static {
      CSSLexicalUnit var0 = CSSLexicalUnit.createString((short)35, "Helvetica", FONT_FAMILY_LU);
      CSSLexicalUnit.createString((short)35, "sans-serif", var0);
      values = new HashSet();
      values.add("caption");
      values.add("icon");
      values.add("menu");
      values.add("message-box");
      values.add("small-caption");
      values.add("status-bar");
   }
}
