package com.mxgraph.view;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxUtils;
import java.util.Collection;
import java.util.Iterator;
import org.w3c.dom.Element;

public class mxMultiplicity {
   protected String type;
   protected String attr;
   protected String value;
   protected boolean source;
   protected int min = 0;
   protected String max = "n";
   protected Collection validNeighbors;
   protected boolean validNeighborsAllowed = true;
   protected String countError;
   protected String typeError;

   public mxMultiplicity(boolean var1, String var2, String var3, String var4, int var5, String var6, Collection var7, String var8, String var9, boolean var10) {
      this.source = var1;
      this.type = var2;
      this.attr = var3;
      this.value = var4;
      this.min = var5;
      this.max = var6;
      this.validNeighbors = var7;
      this.countError = var8;
      this.typeError = var9;
      this.validNeighborsAllowed = var10;
   }

   public String check(mxGraph var1, Object var2, Object var3, Object var4, int var5, int var6) {
      mxIGraphModel var7 = var1.getModel();
      Object var8 = var7.getValue(var3);
      Object var9 = var7.getValue(var4);
      StringBuffer var10 = new StringBuffer();
      if (this.source && this.checkType(var1, var8, this.type, this.attr, this.value) || !this.source && this.checkType(var1, var9, this.type, this.attr, this.value)) {
         if (!this.isUnlimited()) {
            int var11 = this.getMaxValue();
            if (var11 == 0 || this.source && var5 >= var11 || !this.source && var6 >= var11) {
               var10.append(this.countError + "\n");
            }
         }

         if (this.validNeighbors != null) {
            boolean var14 = !this.validNeighborsAllowed;
            Iterator var12 = this.validNeighbors.iterator();

            while(var12.hasNext()) {
               String var13 = (String)var12.next();
               if (this.source && this.checkType(var1, var9, var13)) {
                  var14 = this.validNeighborsAllowed;
                  break;
               }

               if (!this.source && this.checkType(var1, var8, var13)) {
                  var14 = this.validNeighborsAllowed;
                  break;
               }
            }

            if (!var14) {
               var10.append(this.typeError + "\n");
            }
         }
      }

      return var10.length() > 0 ? var10.toString() : null;
   }

   public boolean checkType(mxGraph var1, Object var2, String var3) {
      return this.checkType(var1, var2, var3, (String)null, (String)null);
   }

   public boolean checkType(mxGraph var1, Object var2, String var3, String var4, String var5) {
      if (var2 != null) {
         return var2 instanceof Element ? mxUtils.isNode(var2, var3, var4, var5) : var2.equals(var3);
      } else {
         return false;
      }
   }

   public boolean isUnlimited() {
      return this.max == null || this.max == "n";
   }

   public int getMaxValue() {
      try {
         return Integer.parseInt(this.max);
      } catch (NumberFormatException var2) {
         return 0;
      }
   }
}
