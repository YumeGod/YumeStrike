package com.xmlmind.fo.converter;

import com.xmlmind.fo.objects.Flow;
import com.xmlmind.fo.objects.Fo;
import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.TableCell;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import java.util.Hashtable;
import java.util.Vector;

public final class Context implements Cloneable {
   public int fo = -1;
   public PropertyValues properties;
   public Value lineHeight;
   public int breakBefore;
   public Vector spaceSequence;
   public Color background;
   public TextDecoration textDecoration = new TextDecoration();
   public Translator translator;
   public int screenResolution;
   public PageSequence pageSequence;
   public Flow flow;
   public Hashtable tableColumns;
   public TableCell tableCell;
   private Vector stack = new Vector();

   public Context startElement(int var1, PropertyValues var2) {
      this.stack.addElement(this);

      Context var3;
      try {
         var3 = (Context)this.clone();
      } catch (CloneNotSupportedException var5) {
         return null;
      }

      var3.fo = var1;
      var3.properties = var2;
      switch (var1) {
         case 10:
         case 23:
         case 44:
         case 49:
         case 54:
            var3.breakBefore = 0;
            var3.spaceSequence = null;
         default:
            return var3;
      }
   }

   public void update() {
      Property var1 = Property.list[164];
      Value var2 = this.properties.values[164];
      var2 = var1.compute(var2, this);
      if (var2 != null) {
         this.lineHeight = var2;
      }

      if (Fo.isBlock(this.fo)) {
         int var3 = this.properties.breakBefore();
         if (var3 > this.breakBefore) {
            this.breakBefore = var3;
         }

         if (this.spaceSequence == null) {
            this.spaceSequence = new Vector();
         }

         this.spaceSequence.addElement(new Space(252, this.properties.values));
      } else {
         switch (this.fo) {
            case 48:
               Context var5 = this.parent();
               int var4 = var5.properties.captionSide();
               if (var4 == 1) {
                  var5.breakBefore = 0;
                  var5.spaceSequence = null;
               } else {
                  this.breakBefore = 0;
                  this.spaceSequence = null;
               }
         }
      }

      Color var6 = this.properties.backgroundColor();
      if (var6 != null) {
         this.background = var6;
      }

      if (this.properties.isSpecified(292)) {
         this.textDecoration = this.textDecoration.copy();
         this.textDecoration.update(this.properties.values);
      }

   }

   public void characters(String var1) {
      if (var1.length() != 1 || var1.charAt(0) != ' ') {
         this.breakBefore = 0;
         this.spaceSequence = null;
      }
   }

   public Context endElement() {
      int var1 = this.stack.size();
      Context var2;
      if (var1 > 0) {
         var2 = (Context)this.stack.lastElement();
         this.stack.removeElementAt(var1 - 1);
         if (Fo.isBlock(this.fo)) {
            int var3 = this.properties.breakAfter();
            if (var3 > this.breakBefore) {
               this.breakBefore = var3;
            }

            var2.breakBefore = this.breakBefore;
            if (this.spaceSequence == null) {
               this.spaceSequence = new Vector();
            }

            this.spaceSequence.addElement(new Space(246, this.properties.values));
            var2.spaceSequence = this.spaceSequence;
         } else {
            switch (this.fo) {
               case 21:
                  var2.breakBefore = this.breakBefore;
                  var2.spaceSequence = this.spaceSequence;
               case 23:
               case 48:
                  break;
               default:
                  var2.breakBefore = 0;
                  var2.spaceSequence = null;
            }
         }
      } else {
         var2 = this;
      }

      return var2;
   }

   public Context parent() {
      return this.stack.size() > 0 ? (Context)this.stack.lastElement() : null;
   }

   public Context ancestor(int var1) {
      Context var2 = null;

      for(int var3 = this.stack.size() - 1; var3 > 0; --var3) {
         Context var4 = (Context)this.stack.elementAt(var3);
         if (var4.fo == var1) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   public Context block() {
      Context var1 = null;
      if (Fo.isBlock(this.fo)) {
         return this;
      } else {
         for(int var2 = this.stack.size() - 1; var2 > 0; --var2) {
            Context var3 = (Context)this.stack.elementAt(var2);
            if (Fo.isBlock(var3.fo)) {
               var1 = var3;
               break;
            }
         }

         return var1;
      }
   }

   public Value nearestSpecifiedValue(int var1) {
      Value var2 = null;

      for(int var3 = this.stack.size() - 1; var3 > 0; --var3) {
         Context var4 = (Context)this.stack.elementAt(var3);
         if (var4.properties.isSpecified(var1)) {
            if (var1 == 164) {
               var2 = var4.lineHeight;
            } else {
               var2 = var4.properties.values[var1];
            }
            break;
         }
      }

      return var2;
   }

   public double spaceBefore() {
      double var1 = 0.0;
      boolean var3 = false;
      if (this.spaceSequence != null && this.spaceSequence.size() != 0) {
         Space[] var4 = new Space[this.spaceSequence.size()];
         this.spaceSequence.copyInto(var4);

         int var5;
         for(var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].force) {
               var1 += var4[var5].optimum;
               var3 = true;
            }
         }

         if (!var3) {
            var5 = Integer.MIN_VALUE;
            double var6 = Double.NEGATIVE_INFINITY;

            int var8;
            for(var8 = 0; var8 < var4.length; ++var8) {
               if (var4[var8].precedence > var5) {
                  var5 = var4[var8].precedence;
               }
            }

            for(var8 = 0; var8 < var4.length; ++var8) {
               if (var4[var8].precedence == var5 && var4[var8].optimum > var6) {
                  var6 = var4[var8].optimum;
               }
            }

            var1 = var6;
         }

         return var1;
      } else {
         return 0.0;
      }
   }

   public double fontSize() {
      return this.properties.fontSize();
   }

   public double lineHeight() {
      double var1 = this.fontSize();
      Value var3 = this.lineHeight;
      if (var3.type == 9) {
         Value[] var4 = var3.space();
         var3 = var4[1];
      }

      if (var3.type == 4) {
         var1 = var3.length();
      }

      return var1;
   }

   public int bpDirection() {
      return this.properties.bpDirection();
   }

   public int ipDirection() {
      return this.properties.ipDirection();
   }

   public int linefeedTreatment() {
      return this.properties.linefeedTreatment();
   }

   public int whiteSpaceTreatment() {
      return this.properties.whiteSpaceTreatment();
   }

   public boolean whiteSpaceCollapse() {
      return this.properties.whiteSpaceCollapse();
   }

   public boolean charDataAllowed() {
      int var1 = this.fo;
      if (var1 < 0) {
         return false;
      } else {
         if (var1 == 55) {
            for(int var2 = this.stack.size() - 1; var2 > 0; --var2) {
               Context var3 = (Context)this.stack.elementAt(var2);
               if (var3.fo != 55) {
                  var1 = var3.fo;
                  break;
               }
            }
         }

         return Fo.charDataAllowed(var1);
      }
   }

   public static class Space {
      public double minimum;
      public double optimum;
      public double maximum;
      public boolean retain;
      public boolean force;
      public int precedence;

      public Space(int var1, Value[] var2) {
         String var3 = Property.name(var1);
         Value var4 = var2[Property.index(var3 + ".minimum")];
         this.minimum = var4.length();
         var4 = var2[Property.index(var3 + ".optimum")];
         this.optimum = var4.length();
         var4 = var2[Property.index(var3 + ".maximum")];
         this.maximum = var4.length();
         var4 = var2[Property.index(var3 + ".conditionality")];
         if (var4.keyword() == 163) {
            this.retain = true;
         }

         var4 = var2[Property.index(var3 + ".precedence")];
         if (var4.type == 1) {
            this.force = true;
         } else {
            this.precedence = var4.integer();
         }

      }
   }

   public static class TextDecoration implements Cloneable {
      public boolean underline;
      public boolean overline;
      public boolean lineThrough;
      public boolean blink;
      public Color color;

      public void update(Value[] var1) {
         Value var2 = var1[292];
         Value[] var3;
         if (var2.type == 27) {
            var3 = var2.list();
         } else {
            var3 = new Value[]{var2};
         }

         for(int var4 = 0; var4 < var3.length; ++var4) {
            switch (var3[var4].keyword()) {
               case 22:
                  this.blink = true;
                  break;
               case 107:
                  this.lineThrough = true;
                  break;
               case 125:
                  this.clear();
                  break;
               case 130:
                  this.blink = false;
                  break;
               case 134:
                  this.lineThrough = false;
                  break;
               case 135:
                  this.overline = false;
                  break;
               case 137:
                  this.underline = false;
                  break;
               case 145:
                  this.overline = true;
                  break;
               case 213:
                  this.underline = true;
            }
         }

         if (this.color == null) {
            this.color = var1[78].color();
         }

      }

      private void clear() {
         this.underline = false;
         this.overline = false;
         this.lineThrough = false;
         this.blink = false;
      }

      public TextDecoration copy() {
         try {
            return (TextDecoration)this.clone();
         } catch (CloneNotSupportedException var2) {
            return null;
         }
      }
   }
}
