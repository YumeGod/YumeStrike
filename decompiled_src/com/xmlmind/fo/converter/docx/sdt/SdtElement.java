package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.RunProperties;
import com.xmlmind.fo.converter.docx.Text;
import com.xmlmind.fo.converter.docx.Wml;
import com.xmlmind.fo.properties.Color;
import java.io.PrintWriter;
import org.xml.sax.Attributes;

public abstract class SdtElement {
   protected String title;
   protected String initialValue;
   protected String prompt;
   protected boolean editable = true;
   protected boolean locked = true;
   protected RunProperties properties;
   protected SdtDataBinding binding;

   public SdtElement(Attributes var1, RunProperties var2) {
      this.title = var1.getValue("", "title");
      this.initialValue = var1.getValue("", "initial-value");
      this.prompt = var1.getValue("", "prompt");
      if (this.prompt == null) {
         this.prompt = "   ";
      }

      String var3;
      if ((var3 = var1.getValue("", "editable")) != null) {
         if (var3.equals("true")) {
            this.editable = true;
         } else if (var3.equals("false")) {
            this.editable = false;
         }
      }

      if ((var3 = var1.getValue("", "locked")) != null) {
         if (var3.equals("true")) {
            this.locked = true;
         } else if (var3.equals("false")) {
            this.locked = false;
         }
      }

      this.properties = var2;
   }

   public String initialValue() {
      return this.initialValue;
   }

   public boolean preserveSpace() {
      return false;
   }

   public void setBinding(SdtDataBinding var1) {
      this.binding = var1;
   }

   public void print(PrintWriter var1) {
      var1.println("<w:sdt>");
      var1.println("<w:sdtPr>");
      if (this.title != null) {
         var1.println("<w:alias w:val=\"" + Wml.escape(this.title) + "\" />");
      }

      this.printType(var1);
      if (this.initialValue == null) {
         var1.println("<w:showingPlcHdr />");
      }

      String var2;
      if (this.locked) {
         if (this.editable) {
            var2 = "sdtLocked";
         } else {
            var2 = "sdtContentLocked";
         }
      } else if (this.editable) {
         var2 = "unlocked";
      } else {
         var2 = "contentLocked";
      }

      var1.println("<w:lock w:val=\"" + var2 + "\" />");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      if (this.binding != null) {
         this.binding.print(var1);
      }

      var1.println("</w:sdtPr>");
      if (this.initialValue == null) {
         this.printPlaceholder(var1);
      } else {
         this.printContent(var1);
      }

      var1.println("</w:sdt>");
   }

   protected void printPlaceholder(PrintWriter var1) {
      var1.println("<w:sdtContent>");
      Text var2 = new Text(this.prompt, this.promptProperties());
      var2.print(var1);
      var1.println("</w:sdtContent>");
   }

   protected void printContent(PrintWriter var1) {
   }

   protected RunProperties promptProperties() {
      RunProperties var1 = null;
      if (this.properties != null) {
         var1 = this.properties.copy();
         var1.fgColor = new Color(128, 128, 128);
      }

      return var1;
   }

   protected abstract void printType(PrintWriter var1);
}
