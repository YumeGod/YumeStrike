package com.mxgraph.util;

import java.io.Serializable;

public class mxImage implements Serializable, Cloneable {
   private static final long serialVersionUID = 8541229679513497585L;
   protected String src;
   protected int width;
   protected int height;

   public mxImage(String var1, int var2, int var3) {
      this.src = var1;
      this.width = var2;
      this.height = var3;
   }

   public String getSrc() {
      return this.src;
   }

   public void setSrc(String var1) {
      this.src = var1;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int var1) {
      this.height = var1;
   }
}
