package org.apache.batik.anim.values;

import java.util.Arrays;
import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatablePathDataValue extends AnimatableValue {
   protected short[] commands;
   protected float[] parameters;
   protected static final char[] PATH_COMMANDS = new char[]{' ', 'z', 'M', 'm', 'L', 'l', 'C', 'c', 'Q', 'q', 'A', 'a', 'H', 'h', 'V', 'v', 'S', 's', 'T', 't'};
   protected static final int[] PATH_PARAMS = new int[]{0, 0, 2, 2, 2, 2, 6, 6, 4, 4, 7, 7, 1, 1, 1, 1, 4, 4, 2, 2};

   protected AnimatablePathDataValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatablePathDataValue(AnimationTarget var1, short[] var2, float[] var3) {
      super(var1);
      this.commands = var2;
      this.parameters = var3;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatablePathDataValue var6 = (AnimatablePathDataValue)var2;
      AnimatablePathDataValue var7 = (AnimatablePathDataValue)var4;
      boolean var8 = var2 != null;
      boolean var9 = var4 != null;
      boolean var10 = var8 && var6.parameters.length == this.parameters.length && Arrays.equals(var6.commands, this.commands);
      boolean var11 = var9 && var7.parameters.length == this.parameters.length && Arrays.equals(var7.commands, this.commands);
      AnimatablePathDataValue var12;
      if (!var10 && var8 && (double)var3 >= 0.5) {
         var12 = var6;
      } else {
         var12 = this;
      }

      int var13 = var12.commands.length;
      int var14 = var12.parameters.length;
      AnimatablePathDataValue var15;
      if (var1 == null) {
         var15 = new AnimatablePathDataValue(this.target);
         var15.commands = new short[var13];
         var15.parameters = new float[var14];
         System.arraycopy(var12.commands, 0, var15.commands, 0, var13);
      } else {
         var15 = (AnimatablePathDataValue)var1;
         if (var15.commands != null && var15.commands.length == var13) {
            if (!Arrays.equals(var12.commands, var15.commands)) {
               System.arraycopy(var12.commands, 0, var15.commands, 0, var13);
               var15.hasChanged = true;
            }
         } else {
            var15.commands = new short[var13];
            System.arraycopy(var12.commands, 0, var15.commands, 0, var13);
            var15.hasChanged = true;
         }
      }

      for(int var16 = 0; var16 < var14; ++var16) {
         float var17 = var12.parameters[var16];
         if (var10) {
            var17 += var3 * (var6.parameters[var16] - var17);
         }

         if (var11) {
            var17 += (float)var5 * var7.parameters[var16];
         }

         if (var15.parameters[var16] != var17) {
            var15.parameters[var16] = var17;
            var15.hasChanged = true;
         }
      }

      return var15;
   }

   public short[] getCommands() {
      return this.commands;
   }

   public float[] getParameters() {
      return this.parameters;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      short[] var1 = new short[this.commands.length];
      System.arraycopy(this.commands, 0, var1, 0, this.commands.length);
      float[] var2 = new float[this.parameters.length];
      return new AnimatablePathDataValue(this.target, var1, var2);
   }

   public String toStringRep() {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = 0; var3 < this.commands.length; ++var3) {
         var1.append(PATH_COMMANDS[this.commands[var3]]);

         for(int var4 = 0; var4 < PATH_PARAMS[this.commands[var3]]; ++var4) {
            var1.append(' ');
            var1.append(this.parameters[var2++]);
         }
      }

      return var1.toString();
   }
}
