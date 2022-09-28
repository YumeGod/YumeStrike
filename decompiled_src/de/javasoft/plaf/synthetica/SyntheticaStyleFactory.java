package de.javasoft.plaf.synthetica;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;
import sun.swing.BakedArrayList;
import sun.swing.plaf.synth.DefaultSynthStyle;
import sun.swing.plaf.synth.StyleAssociation;

class SyntheticaStyleFactory {
   public static final int NAME = 0;
   public static final int REGION = 1;
   private List styles;
   private BakedArrayList tmpList;
   private Map resolvedStyles;
   private SynthStyle defaultStyle;

   SyntheticaStyleFactory(SynthStyleFactory var1) {
      try {
         Class var2 = Class.forName("javax.swing.plaf.synth.DefaultSynthStyleFactory");
         Field var3 = var2.getDeclaredField("_styles");
         var3.setAccessible(true);
         this.styles = (List)var3.get(var1);
         Field var4 = var2.getDeclaredField("_tmpList");
         var4.setAccessible(true);
         this.tmpList = (BakedArrayList)var4.get(var1);
         Field var5 = var2.getDeclaredField("_resolvedStyles");
         var5.setAccessible(true);
         this.resolvedStyles = (Map)var5.get(var1);
         Field var6 = var2.getDeclaredField("_defaultStyle");
         var6.setAccessible(true);
         this.defaultStyle = (SynthStyle)var6.get(var1);
      } catch (Exception var7) {
         throw new RuntimeException(var7);
      }
   }

   public synchronized SynthStyle getStyle(JComponent var1, Region var2) {
      BakedArrayList var3 = this.tmpList;
      var3.clear();
      this.getMatchingStyles(var3, this.styles, var1, var2);
      if (var3.size() == 0) {
         if (this.defaultStyle == null) {
            this.defaultStyle = new DefaultSynthStyle();
            ((DefaultSynthStyle)this.defaultStyle).setFont(new FontUIResource("Dialog", 0, 12));
         }

         return this.defaultStyle;
      } else {
         var3.cacheHashCode();
         SynthStyle var4 = var3.size() == 0 ? null : (SynthStyle)this.resolvedStyles.get(var3);
         if (var4 == null) {
            var4 = this.mergeStyles(var3);
            if (var4 != null) {
               this.resolvedStyles.put(new BakedArrayList(var3), var4);
            }
         }

         return var4;
      }
   }

   private void getMatchingStyles(List var1, List var2, JComponent var3, Region var4) {
      String var5 = var4.getName().toLowerCase(Locale.ENGLISH);
      String var6 = SyntheticaLookAndFeel.getStyleName(var3);
      if (var6 == null) {
         var6 = "";
      }

      for(int var7 = var2.size() - 1; var7 >= 0; --var7) {
         StyleAssociation var8 = (StyleAssociation)var2.get(var7);
         String var9 = var8.getID() == 0 ? var6 : var5;
         if (var8.matches(var9) && var1.indexOf(var8.getStyle()) == -1) {
            var1.add(var8.getStyle());
         }
      }

   }

   private SynthStyle mergeStyles(BakedArrayList var1) {
      int var2 = var1.size();
      if (var2 == 0) {
         return null;
      } else if (var2 == 1) {
         return (SynthStyle)((DefaultSynthStyle)var1.get(0)).clone();
      } else {
         DefaultSynthStyle var3 = (DefaultSynthStyle)var1.get(var2 - 1);
         var3 = (DefaultSynthStyle)var3.clone();

         for(int var4 = var2 - 2; var4 >= 0; --var4) {
            var3 = ((DefaultSynthStyle)var1.get(var4)).addTo(var3);
         }

         return var3;
      }
   }
}
