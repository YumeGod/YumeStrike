package com.xmlmind.fo.properties.expression;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.util.Hashtable;

public final class Function {
   public static final int ABS = 0;
   public static final int BODY_START = 1;
   public static final int CEILING = 2;
   public static final int FLOOR = 3;
   public static final int FROM_NEAREST_SPECIFIED_VALUE = 4;
   public static final int FROM_PARENT = 5;
   public static final int FROM_TABLE_COLUMN = 6;
   public static final int INHERITED_PROPERTY_VALUE = 7;
   public static final int LABEL_END = 8;
   public static final int MAX = 9;
   public static final int MERGE_PROPERTY_VALUES = 10;
   public static final int MIN = 11;
   public static final int PROPORTIONAL_COLUMN_WIDTH = 12;
   public static final int RGB = 13;
   public static final int RGB_ICC = 14;
   public static final int ROUND = 15;
   public static final int SYSTEM_COLOR = 16;
   public static final int SYSTEM_FONT = 17;
   public static final Function[] list = new Function[]{new Function(0, "abs", 1), new Function(1, "body-start", 0), new Function(2, "ceiling", 1), new Function(3, "floor", 1), new Function(4, "from-nearest-specified-value", 0, 1), new Function(5, "from-parent", 0, 1), new Function(6, "from-table-column", 0, 1), new Function(7, "inherited-property-value", 0, 1), new Function(8, "label-end", 0), new Function(9, "max", 2), new Function(10, "merge-property-values", 0, 1), new Function(11, "min", 2), new Function(12, "proportional-column-width", 1), new Function(13, "rgb", 3), new Function(14, "rgb-icc", 6), new Function(15, "round", 1), new Function(16, "system-color", 1), new Function(17, "system-font", 1, 2)};
   private static final Hashtable indexes = new Hashtable();
   public int index;
   public String name;
   public int minArgs;
   public int maxArgs;

   private Function(int var1, String var2, int var3) {
      this(var1, var2, var3, var3);
   }

   private Function(int var1, String var2, int var3, int var4) {
      this.index = var1;
      this.name = var2;
      this.minArgs = var3;
      this.maxArgs = var4;
   }

   public static int index(String var0) {
      Integer var1 = (Integer)indexes.get(var0);
      return var1 != null ? var1 : -1;
   }

   public static Function function(String var0) {
      int var1 = index(var0);
      return var1 >= 0 ? list[var1] : null;
   }

   public static String name(int var0) {
      return list[var0].name;
   }

   public static Value evaluate(int var0, Value[] var1, Property var2, Context var3) {
      Value var4 = null;
      Value[] var5 = null;
      String var6 = null;
      if (var1 != null) {
         var5 = new Value[var1.length];

         for(int var7 = 0; var7 < var1.length; ++var7) {
            switch (var1[var7].type) {
               case 23:
                  var5[var7] = evaluate(var1[var7].function(), var1[var7].arguments(), var2, var3);
                  break;
               case 28:
                  Expression var8 = var1[var7].expression();
                  var5[var7] = var8.evaluate(var2, var3);
                  break;
               default:
                  var5[var7] = var2.compute(var1[var7], var3);
            }

            if (var5[var7] == null) {
               return null;
            }
         }
      }

      switch (var0) {
         case 0:
            var4 = abs(var5[0]);
            break;
         case 1:
            var4 = var2.bodyStart(var3);
            break;
         case 2:
            var4 = ceiling(var5[0]);
            break;
         case 3:
            var4 = floor(var5[0]);
            break;
         case 4:
            if (var5 != null) {
               var6 = var5[0].name();
            }

            var4 = var2.fromNearestSpecifiedValue(var6, var3);
            break;
         case 5:
            if (var5 != null) {
               var6 = var5[0].name();
            }

            var4 = var2.fromParent(var6, var3);
            break;
         case 6:
            if (var5 != null) {
               var6 = var5[0].name();
            }

            var4 = var2.fromTableColumn(var6, var3);
            break;
         case 7:
            if (var5 != null) {
               var6 = var5[0].name();
            }

            var4 = var2.inheritedPropertyValue(var6, var3);
            break;
         case 8:
            var4 = var2.labelEnd(var3);
            break;
         case 9:
            var4 = max(var5[0], var5[1]);
         case 10:
         case 13:
         case 14:
         default:
            break;
         case 11:
            var4 = min(var5[0], var5[1]);
            break;
         case 12:
            if (var5[0].type != 3) {
               return null;
            }

            var4 = Value.proportionalColumnWidth(var5[0].number());
            break;
         case 15:
            var4 = round(var5[0]);
      }

      return var4;
   }

   private static Value abs(Value var0) {
      Value var1 = null;
      switch (var0.type) {
         case 3:
            var1 = Value.number(Math.abs(var0.number()));
            break;
         case 4:
            var1 = Value.length(Math.abs(var0.length()), var0.unit());
      }

      return var1;
   }

   private static Value ceiling(Value var0) {
      Value var1 = null;
      switch (var0.type) {
         case 3:
            var1 = Value.number(Math.ceil(var0.number()));
            break;
         case 4:
            var1 = Value.length(Math.ceil(var0.length()), var0.unit());
      }

      return var1;
   }

   private static Value floor(Value var0) {
      Value var1 = null;
      switch (var0.type) {
         case 3:
            var1 = Value.number(Math.floor(var0.number()));
            break;
         case 4:
            var1 = Value.length(Math.floor(var0.length()), var0.unit());
      }

      return var1;
   }

   private static Value max(Value var0, Value var1) {
      Value var2 = null;
      if (var1.type != var0.type) {
         return null;
      } else {
         switch (var0.type) {
            case 3:
               var2 = Value.number(Math.max(var0.number(), var1.number()));
               break;
            case 4:
               var2 = Value.length(Math.max(var0.length(), var1.length()), var0.unit());
         }

         return var2;
      }
   }

   private static Value min(Value var0, Value var1) {
      Value var2 = null;
      if (var1.type != var0.type) {
         return null;
      } else {
         switch (var0.type) {
            case 3:
               var2 = Value.number(Math.min(var0.number(), var1.number()));
               break;
            case 4:
               var2 = Value.length(Math.min(var0.length(), var1.length()), var0.unit());
         }

         return var2;
      }
   }

   private static Value round(Value var0) {
      Value var1 = null;
      switch (var0.type) {
         case 3:
            var1 = Value.number(Math.rint(var0.number()));
            break;
         case 4:
            var1 = Value.length(Math.rint(var0.length()), var0.unit());
      }

      return var1;
   }

   static {
      for(int var0 = 0; var0 < list.length; ++var0) {
         indexes.put(list[var0].name, new Integer(var0));
      }

   }
}
