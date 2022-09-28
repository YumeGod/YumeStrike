package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfText extends RtfElement {
   private static final int CHAR_NBSP = 160;
   private static final int CHAR_TAB = 137;
   private static final int CHAR_NEW_LINE = 141;
   private static final int CHAR_BOLD_START = 130;
   private static final int CHAR_BOLD_END = 131;
   private String text;
   private final RtfAttributes attr;
   public static final String ATTR_BOLD = "b";
   public static final String ATTR_ITALIC = "i";
   public static final String ATTR_UNDERLINE = "ul";
   public static final String ATTR_STRIKETHROUGH = "strike";
   public static final String ATTR_FONT_SIZE = "fs";
   public static final String ATTR_FONT_FAMILY = "f";
   public static final String ATTR_FONT_COLOR = "cf";
   public static final String ATTR_BACKGROUND_COLOR = "chcbpat";
   public static final String ATTR_SUPERSCRIPT = "super";
   public static final String ATTR_SUBSCRIPT = "sub";
   public static final String SHADING = "shading";
   public static final String SHADING_FRONT_COLOR = "cfpat";
   public static final int FULL_SHADING = 10000;
   public static final String ALIGN_CENTER = "qc";
   public static final String ALIGN_LEFT = "ql";
   public static final String ALIGN_RIGHT = "qr";
   public static final String ALIGN_JUSTIFIED = "qj";
   public static final String ALIGN_DISTRIBUTED = "qd";
   public static final String BDR_BOTTOM_SINGLE = "brdrb\\brsp40\\brdrs";
   public static final String BDR_BOTTOM_DOUBLE = "brdrb\\brsp40\\brdrdb";
   public static final String BDR_BOTTOM_EMBOSS = "brdrb\\brsp40\\brdremboss";
   public static final String BDR_BOTTOM_DOTTED = "brdrb\\brsp40\\brdrdot";
   public static final String BDR_BOTTOM_DASH = "brdrb\\brsp40\\brdrdash";
   public static final String RTF_FIELD = "field";
   public static final String RTF_FIELD_PAGE = "fldinst { PAGE }";
   public static final String RTF_FIELD_RESULT = "fldrslt";
   public static final String LEFT_INDENT_BODY = "li";
   public static final String LEFT_INDENT_FIRST = "fi-";
   public static final String RIGHT_INDENT_BODY = "ri";
   public static final String TAB_CENTER = "tqc\\tx";
   public static final String TAB_RIGHT = "tqr\\tx";
   public static final String TAB_LEADER_DOTS = "tldot";
   public static final String TAB_LEADER_HYPHEN = "tlhyph";
   public static final String TAB_LEADER_UNDER = "tlul";
   public static final String TAB_LEADER_THICK = "tlth";
   public static final String TAB_LEADER_EQUALS = "tleq";
   public static final String SPACE_BEFORE = "sb";
   public static final String SPACE_AFTER = "sa";
   public static final String[] ALIGNMENT = new String[]{"qc", "ql", "qr", "qj", "qd"};
   public static final String[] BORDER = new String[]{"brdrb\\brsp40\\brdrs", "brdrb\\brsp40\\brdrdb", "brdrb\\brsp40\\brdremboss", "brdrb\\brsp40\\brdrdot", "brdrb\\brsp40\\brdrdash"};
   public static final String[] INDENT = new String[]{"li", "fi-"};
   public static final String[] TABS = new String[]{"tqc\\tx", "tqr\\tx", "tldot", "tlhyph", "tlul", "tlth", "tleq"};
   public static final String[] ATTR_NAMES = new String[]{"b", "i", "ul", "fs", "f", "cf", "chcbpat"};

   RtfText(IRtfTextContainer parent, Writer w, String str, RtfAttributes attr) throws IOException {
      super((RtfContainer)parent, w);
      this.text = str;
      this.attr = attr;
   }

   public void writeRtfContent() throws IOException {
      if (this.attr != null) {
         this.writeAttributes(this.attr, new String[]{"sb"});
         this.writeAttributes(this.attr, new String[]{"sa"});
      }

      if (this.isTab()) {
         this.writeControlWord("tab");
      } else if (!this.isNewLine()) {
         if (this.isBold(true)) {
            this.writeControlWord("b");
         } else if (this.isBold(false)) {
            this.writeControlWord("b0");
         } else {
            this.writeGroupMark(true);
            if (this.attr != null && this.mustWriteAttributes()) {
               this.writeAttributes(this.attr, ATTR_NAMES);
            }

            RtfStringConverter.getInstance().writeRtfString(this.writer, this.text);
            this.writeGroupMark(false);
         }
      }

   }

   private boolean mustWriteAttributes() {
      return !this.isEmpty() && !this.isNbsp();
   }

   public RtfAttributes getTextContainerAttributes() {
      return this.attrib == null ? null : (RtfAttributes)this.attrib.clone();
   }

   String getText() {
      return this.text;
   }

   void setText(String str) {
      this.text = str;
   }

   public boolean isEmpty() {
      return this.text == null || this.text.trim().length() == 0;
   }

   public boolean isNbsp() {
      return !this.isEmpty() && this.text.trim().length() == 1 && this.text.charAt(0) == 160;
   }

   public boolean isTab() {
      return this.text.trim().length() == 1 && this.text.charAt(0) == 137;
   }

   public boolean isNewLine() {
      return this.text.trim().length() == 1 && this.text.charAt(0) == 141;
   }

   public boolean isBold(boolean isStart) {
      if (isStart) {
         return this.text.trim().length() == 1 && this.text.charAt(0) == 130;
      } else {
         return this.text.trim().length() == 1 && this.text.charAt(0) == 131;
      }
   }

   public RtfAttributes getTextAttributes() {
      return this.attr;
   }
}
