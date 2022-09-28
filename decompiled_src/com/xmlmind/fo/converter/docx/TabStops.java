package com.xmlmind.fo.converter.docx;

import java.io.PrintWriter;
import java.util.Vector;

public final class TabStops {
   private int tabCount;
   private Vector tabList = new Vector();
   private TabStop[] tabs;

   public void add(TabStop var1) {
      this.tabList.addElement(var1);
      ++this.tabCount;
   }

   public void add(TabStops var1) {
      for(int var2 = 0; var2 < var1.tabCount; ++var2) {
         this.tabList.addElement(var1.tabList.elementAt(var2));
      }

      this.tabCount += var1.tabCount;
   }

   public int count() {
      return this.tabCount;
   }

   public void layout(double var1, double var3, double var5) {
      if (this.tabCount != 0) {
         this.tabs = new TabStop[this.tabCount];

         int var7;
         for(var7 = 0; var7 < this.tabs.length; ++var7) {
            this.tabs[var7] = (TabStop)this.tabList.elementAt(var7);
            if (this.tabs[var7].position < 0.0) {
               TabStop var10000 = this.tabs[var7];
               var10000.position += var1;
            }
         }

         for(var7 = 0; var7 < this.tabs.length; ++var7) {
            if (this.tabs[var7].position == Double.MAX_VALUE) {
               double var10;
               if (var7 > 0) {
                  var10 = this.tabs[var7 - 1].position;
               } else {
                  var10 = var3;
               }

               double var12 = var1 - var5;
               int var8 = var7 + 1;

               int var9;
               for(var9 = 1; var8 < this.tabs.length; ++var9) {
                  if (this.tabs[var8].position != Double.MAX_VALUE) {
                     var12 = this.tabs[var8].position;
                     break;
                  }

                  ++var8;
               }

               for(double var14 = (var12 - var10) / (double)(var9 + 1); var7 < var8; ++var7) {
                  this.tabs[var7].position = var10 + var14;
                  var10 = this.tabs[var7].position;
               }
            }
         }

      }
   }

   public void print(PrintWriter var1) {
      if (this.tabs != null) {
         var1.println("<w:tabs>");

         for(int var2 = 0; var2 < this.tabs.length; ++var2) {
            this.tabs[var2].print(var1);
         }

         var1.print("</w:tabs>");
      }
   }
}
