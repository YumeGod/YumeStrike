package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;

public class UIKey {
   private StringBuilder sb;

   public UIKey(String var1, SyntheticaState var2) {
      this(var1, var2, -1, -1, -1);
   }

   public UIKey(String var1, SyntheticaState var2, String var3) {
      this(var1, var2, -1, -1, -1, var3);
   }

   UIKey(String var1, SyntheticaState var2, int var3, int var4, int var5) {
      this(var1, var2, var3, var4, var5, "Synthetica.");
   }

   UIKey(String var1, SyntheticaState var2, int var3, int var4, int var5, String var6) {
      this.sb = new StringBuilder(var6);
      this.sb.append(var1);
      if (var3 >= 0) {
         if (var3 == 1) {
            this.sb.append(".up");
         } else if (var3 == 5) {
            this.sb.append(".down");
         } else if (var3 == 7) {
            this.sb.append(".left");
         } else if (var3 == 3) {
            this.sb.append(".right");
         }
      } else if (var4 >= 0) {
         if (var4 == 1) {
            this.sb.append(".top");
         } else if (var4 == 2) {
            this.sb.append(".left");
         } else if (var4 == 3) {
            this.sb.append(".bottom");
         } else if (var4 == 4) {
            this.sb.append(".right");
         }
      } else if (var5 >= 0) {
         if (var5 == 0) {
            this.sb.append(".x");
         } else if (var5 == 1) {
            this.sb.append(".y");
         }
      }

      if (var2.isSet(SyntheticaState.State.DEFAULT)) {
         this.sb.append(".default");
      }

      if (var2.isSet(SyntheticaState.State.DISABLED)) {
         this.sb.append(".disabled");
      } else if (var2.isSet(SyntheticaState.State.PRESSED)) {
         this.sb.append(".pressed");
      } else if (var2.isSet(SyntheticaState.State.HOVER)) {
         this.sb.append(".hover");
      }

      if (var2.isSet(SyntheticaState.State.SELECTED)) {
         this.sb.append(".selected");
      }

      if (var2.isSet(SyntheticaState.State.LOCKED)) {
         this.sb.append(".locked");
      }

      if (var2.isSet(SyntheticaState.State.ACTIVE)) {
         this.sb.append(".active");
      }

   }

   public String get() {
      return this.sb.toString();
   }

   String get(String var1) {
      StringBuilder var2 = new StringBuilder(this.sb.toString());
      var2.append(".");
      var2.append(var1);
      return var2.toString();
   }

   static Object findProperty(JComponent var0, String var1, String var2, boolean var3, int var4) {
      char var5 = '.';

      for(int var6 = var1.length(); var6 > -1; --var4) {
         var1 = var1.substring(0, var6);
         StringBuilder var7 = new StringBuilder(var1);
         if (var2 != null) {
            var7.append(var5);
            var7.append(var2);
         }

         Object var8 = SyntheticaLookAndFeel.get(var7.toString(), (Component)var0);
         if (var8 != null || !var3 || var4 == 0) {
            return var8;
         }

         var6 = var1.lastIndexOf(var5);
      }

      return null;
   }

   static Object findProperty(SynthContext var0, String var1, String var2, boolean var3, int var4) {
      return findProperty(var0.getComponent(), var1, var2, var3, var4);
   }

   Object findProperty(SynthContext var1, String var2, boolean var3, int var4) {
      return findProperty(var1, this.get(), var2, var3, var4);
   }
}
