package com.mxgraph.util;

import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JLabel;

public class mxLightweightLabel extends JLabel {
   private static final long serialVersionUID = -6771477489533614010L;
   protected static mxLightweightLabel sharedInstance;

   public static mxLightweightLabel getSharedInstance() {
      return sharedInstance;
   }

   public mxLightweightLabel() {
      this.setFont(new Font(mxConstants.DEFAULT_FONTFAMILY, 0, mxConstants.DEFAULT_FONTSIZE));
      this.setVerticalAlignment(1);
   }

   public void validate() {
   }

   public void revalidate() {
   }

   public void repaint(long var1, int var3, int var4, int var5, int var6) {
   }

   public void repaint(Rectangle var1) {
   }

   protected void firePropertyChange(String var1, Object var2, Object var3) {
      if (var1 == "text") {
         super.firePropertyChange(var1, var2, var3);
      }

   }

   public void firePropertyChange(String var1, byte var2, byte var3) {
   }

   public void firePropertyChange(String var1, char var2, char var3) {
   }

   public void firePropertyChange(String var1, short var2, short var3) {
   }

   public void firePropertyChange(String var1, int var2, int var3) {
   }

   public void firePropertyChange(String var1, long var2, long var4) {
   }

   public void firePropertyChange(String var1, float var2, float var3) {
   }

   public void firePropertyChange(String var1, double var2, double var4) {
   }

   public void firePropertyChange(String var1, boolean var2, boolean var3) {
   }

   static {
      try {
         sharedInstance = new mxLightweightLabel();
      } catch (Exception var1) {
      }

   }
}
