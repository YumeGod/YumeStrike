package com.xmlmind.fo.objects;

import java.util.Hashtable;

public final class Fo {
   public static final int BASIC_LINK = 0;
   public static final int BIDI_OVERRIDE = 1;
   public static final int BLOCK = 2;
   public static final int BLOCK_CONTAINER = 3;
   public static final int CHARACTER = 4;
   public static final int COLOR_PROFILE = 5;
   public static final int CONDITIONAL_PAGE_MASTER_REFERENCE = 6;
   public static final int DECLARATIONS = 7;
   public static final int EXTERNAL_GRAPHIC = 8;
   public static final int FLOAT = 9;
   public static final int FLOW = 10;
   public static final int FOOTNOTE = 11;
   public static final int FOOTNOTE_BODY = 12;
   public static final int INITIAL_PROPERTY_SET = 13;
   public static final int INLINE = 14;
   public static final int INLINE_CONTAINER = 15;
   public static final int INSTREAM_FOREIGN_OBJECT = 16;
   public static final int LAYOUT_MASTER_SET = 17;
   public static final int LEADER = 18;
   public static final int LIST_BLOCK = 19;
   public static final int LIST_ITEM = 20;
   public static final int LIST_ITEM_BODY = 21;
   public static final int LIST_ITEM_LABEL = 22;
   public static final int MARKER = 23;
   public static final int MULTI_CASE = 24;
   public static final int MULTI_PROPERTIES = 25;
   public static final int MULTI_PROPERTY_SET = 26;
   public static final int MULTI_SWITCH = 27;
   public static final int MULTI_TOGGLE = 28;
   public static final int PAGE_NUMBER = 29;
   public static final int PAGE_NUMBER_CITATION = 30;
   public static final int PAGE_SEQUENCE = 31;
   public static final int PAGE_SEQUENCE_MASTER = 32;
   public static final int REGION_AFTER = 33;
   public static final int REGION_BEFORE = 34;
   public static final int REGION_BODY = 35;
   public static final int REGION_END = 36;
   public static final int REGION_START = 37;
   public static final int REPEATABLE_PAGE_MASTER_ALTERNATIVES = 38;
   public static final int REPEATABLE_PAGE_MASTER_REFERENCE = 39;
   public static final int RETRIEVE_MARKER = 40;
   public static final int ROOT = 41;
   public static final int SIMPLE_PAGE_MASTER = 42;
   public static final int SINGLE_PAGE_MASTER_REFERENCE = 43;
   public static final int STATIC_CONTENT = 44;
   public static final int TABLE = 45;
   public static final int TABLE_AND_CAPTION = 46;
   public static final int TABLE_BODY = 47;
   public static final int TABLE_CAPTION = 48;
   public static final int TABLE_CELL = 49;
   public static final int TABLE_COLUMN = 50;
   public static final int TABLE_FOOTER = 51;
   public static final int TABLE_HEADER = 52;
   public static final int TABLE_ROW = 53;
   public static final int TITLE = 54;
   public static final int WRAPPER = 55;
   public static final int FO_COUNT = 56;
   public static final int TYPE_NONE = 0;
   public static final int TYPE_FLOW = 1;
   public static final int TYPE_BLOCK = 2;
   public static final int TYPE_INLINE = 3;
   public static final Fo[] list = new Fo[]{new Fo(0, "basic-link", 3, true), new Fo(1, "bidi-override", 3, true), new Fo(2, "block", 2, true), new Fo(3, "block-container", 2, false), new Fo(4, "character", 3, false), new Fo(5, "color-profile", 0, false), new Fo(6, "conditional-page-master-reference", 0, false), new Fo(7, "declarations", 0, false), new Fo(8, "external-graphic", 3, false), new Fo(9, "float", 0, false), new Fo(10, "flow", 1, false), new Fo(11, "footnote", 0, false), new Fo(12, "footnote-body", 0, false), new Fo(13, "initial-property-set", 0, false), new Fo(14, "inline", 3, true), new Fo(15, "inline-container", 3, false), new Fo(16, "instream-foreign-object", 3, false), new Fo(17, "layout-master-set", 0, false), new Fo(18, "leader", 3, true), new Fo(19, "list-block", 2, false), new Fo(20, "list-item", 2, false), new Fo(21, "list-item-body", 0, false), new Fo(22, "list-item-label", 0, false), new Fo(23, "marker", 0, true), new Fo(24, "multi-case", 0, true), new Fo(25, "multi-properties", 0, false), new Fo(26, "multi-property-set", 0, false), new Fo(27, "multi-switch", 0, false), new Fo(28, "multi-toggle", 3, true), new Fo(29, "page-number", 3, false), new Fo(30, "page-number-citation", 3, false), new Fo(31, "page-sequence", 0, false), new Fo(32, "page-sequence-master", 0, false), new Fo(33, "region-after", 0, false), new Fo(34, "region-before", 0, false), new Fo(35, "region-body", 0, false), new Fo(36, "region-end", 0, false), new Fo(37, "region-start", 0, false), new Fo(38, "repeatable-page-master-alternatives", 0, false), new Fo(39, "repeatable-page-master-reference", 0, false), new Fo(40, "retrieve-marker", 0, false), new Fo(41, "root", 0, false), new Fo(42, "simple-page-master", 0, false), new Fo(43, "single-page-master-reference", 0, false), new Fo(44, "static-content", 1, false), new Fo(45, "table", 2, false), new Fo(46, "table-and-caption", 2, false), new Fo(47, "table-body", 0, false), new Fo(48, "table-caption", 0, false), new Fo(49, "table-cell", 0, false), new Fo(50, "table-column", 0, false), new Fo(51, "table-footer", 0, false), new Fo(52, "table-header", 0, false), new Fo(53, "table-row", 0, false), new Fo(54, "title", 1, true), new Fo(55, "wrapper", 0, true)};
   private static final Hashtable indexes = new Hashtable();
   public int index;
   public String name;
   public int type;
   public boolean pcdata;

   public Fo(int var1, String var2, int var3, boolean var4) {
      this.index = var1;
      this.name = var2;
      this.type = var3;
      this.pcdata = var4;
   }

   public static void check() {
      if (list.length != 56) {
         throw new Error("size mismatch: 56 " + list.length);
      } else {
         for(int var0 = 0; var0 < list.length; ++var0) {
            if (list[var0].index != var0) {
               throw new Error("index mismatch: " + var0 + " " + list[var0].index);
            }
         }

      }
   }

   public static int index(String var0) {
      Object var1 = indexes.get(var0);
      return var1 != null ? (Integer)var1 : -1;
   }

   public static String name(int var0) {
      return list[var0].name;
   }

   public static boolean isFlow(int var0) {
      return list[var0].type == 1;
   }

   public static boolean isBlock(int var0) {
      return list[var0].type == 2;
   }

   public static boolean isInline(int var0) {
      return list[var0].type == 3;
   }

   public static boolean isReference(int var0) {
      boolean var1 = false;
      switch (var0) {
         case 3:
         case 12:
         case 15:
         case 49:
            var1 = true;
         default:
            return var1;
      }
   }

   public static boolean charDataAllowed(int var0) {
      return list[var0].pcdata;
   }

   static {
      for(int var0 = 0; var0 < list.length; ++var0) {
         indexes.put(list[var0].name, new Integer(var0));
      }

   }
}
