package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class RtfLeader extends RtfContainer {
   private RtfAttributes attrs = null;
   public static final String LEADER_TABLEAD = "tablead";
   public static final String LEADER_USETAB = "tabuse";
   public static final String LEADER_WIDTH = "lwidth";
   public static final String LEADER_DOTTED = "uld";
   public static final String LEADER_MIDDLEDOTTED = "uldash";
   public static final String LEADER_HYPHENS = "ulhwave";
   public static final String LEADER_UNDERLINE = "ulthdashdd";
   public static final String LEADER_EQUAL = "uldb";
   public static final String LEADER_THICK = "ulth";
   public static final String LEADER_TAB_DOTTED = "tldot";
   public static final String LEADER_TAB_MIDDLEDOTTED = "tlmdot";
   public static final String LEADER_TAB_HYPHENS = "tlhyph";
   public static final String LEADER_TAB_UNDERLINE = "tlul";
   public static final String LEADER_TAB_EQUAL = "tleq";
   public static final String LEADER_TAB_THICK = "tlth";
   public static final String LEADER_IGNORE_STYLE = "pard";
   public static final String LEADER_RULE_THICKNESS = "fs";
   public static final String LEADER_PATTERN_WIDTH = "expndtw";
   public static final String LEADER_ZERO_WIDTH = "zwbo";
   public static final int LEADER_STANDARD_WIDTH = 30;
   public static final String LEADER_UP = "up4";
   public static final String LEADER_EXPAND = "expnd-2";
   public static final String LEADER_TAB_VALUE = "tab";
   public static final String LEADER_TAB_RIGHT = "tqr";
   public static final String LEADER_TAB_WIDTH = "tx";

   RtfLeader(RtfContainer parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w);
      this.attrs = attrs;
   }

   protected void writeRtfContent() throws IOException {
      int thickness = 30;
      String tablead = null;
      String tabwidth = null;
      Iterator it = this.attrs.nameIterator();

      while(it.hasNext()) {
         String name = (String)it.next();
         if (this.attrs.isSet(name)) {
            if (name.equals("tablead")) {
               tablead = this.attrs.getValue("tablead").toString();
            } else if (name.equals("lwidth")) {
               tabwidth = this.attrs.getValue("lwidth").toString();
            }
         }
      }

      if (this.attrs.getValue("fs") != null) {
         thickness += Integer.parseInt(this.attrs.getValue("fs").toString()) / 1000 * 2;
         this.attrs.unset("fs");
      }

      this.attrs.unset("lwidth");
      this.attrs.unset("tablead");
      if (this.attrs.getValue("tabuse") != null) {
         this.attrs.unset("tabuse");
         this.writeControlWord("tqr");
         if (tablead != null) {
            this.writeControlWord(tablead);
         }

         this.writeControlWord("tx" + tabwidth);
         this.writeGroupMark(true);
         this.writeControlWord("pard");
         this.writeAttributes(this.attrs, (String[])null);
         this.writeControlWord("expnd-2");
         this.writeControlWord("tab");
         this.writeGroupMark(false);
      } else {
         this.writeControlWord("pard");
         this.writeControlWord("zwbo");
         this.writeGroupMark(true);
         this.writeControlWord("fs" + thickness);
         this.writeControlWord("up4");
         super.writeAttributes(this.attrs, (String[])null);
         if (tablead != null) {
            this.writeControlWord(tablead);
         }

         for(double d = (double)(Integer.parseInt(tabwidth) / 560) * 7.5; d >= 1.0; --d) {
            RtfStringConverter.getInstance().writeRtfString(this.writer, " ");
         }

         this.writeGroupMark(false);
         this.writeControlWord("zwbo");
      }

   }

   public boolean isEmpty() {
      return false;
   }
}
