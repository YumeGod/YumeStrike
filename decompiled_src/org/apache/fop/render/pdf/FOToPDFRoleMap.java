package org.apache.fop.render.pdf;

import java.util.HashMap;
import java.util.Map;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.pdf.PDFName;
import org.apache.fop.pdf.PDFObject;
import org.apache.fop.pdf.PDFStructElem;
import org.w3c.dom.Node;

final class FOToPDFRoleMap {
   private static final Map STANDARD_STRUCTURE_TYPES;
   private static final Map DEFAULT_MAPPINGS;
   private static final PDFName THEAD;
   private static final PDFName NON_STRUCT;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private static void addStructureType(String structureType) {
      STANDARD_STRUCTURE_TYPES.put(structureType, new PDFName(structureType));
   }

   private static void addMapping(String fo, String structureType) {
      PDFName type = (PDFName)STANDARD_STRUCTURE_TYPES.get(structureType);
      if (!$assertionsDisabled && type == null) {
         throw new AssertionError();
      } else {
         addMapping(fo, (Mapper)(new SimpleMapper(type)));
      }
   }

   private static void addMapping(String fo, Mapper mapper) {
      DEFAULT_MAPPINGS.put(fo, mapper);
   }

   public static PDFName mapFormattingObject(String fo, PDFObject parent) {
      Mapper mapper = (Mapper)DEFAULT_MAPPINGS.get(fo);
      return mapper != null ? mapper.getStructureType(parent) : NON_STRUCT;
   }

   public static PDFName mapFormattingObject(Node fo, PDFObject parent, EventBroadcaster eventBroadcaster) {
      PDFName type = null;
      Node role = fo.getAttributes().getNamedItemNS((String)null, "role");
      if (role == null) {
         type = mapFormattingObject(fo.getLocalName(), parent);
      } else {
         String customType = role.getNodeValue();
         type = (PDFName)STANDARD_STRUCTURE_TYPES.get(customType);
         if (type == null) {
            String foName = fo.getLocalName();
            type = mapFormattingObject(foName, parent);
            PDFEventProducer.Provider.get(eventBroadcaster).nonStandardStructureType(fo, foName, customType, type.toString().substring(1));
         }
      }

      if (!$assertionsDisabled && type == null) {
         throw new AssertionError();
      } else {
         return type;
      }
   }

   private FOToPDFRoleMap() {
   }

   static {
      $assertionsDisabled = !FOToPDFRoleMap.class.desiredAssertionStatus();
      STANDARD_STRUCTURE_TYPES = new HashMap();
      DEFAULT_MAPPINGS = new HashMap();
      addStructureType("Document");
      addStructureType("Part");
      addStructureType("Art");
      addStructureType("Sect");
      addStructureType("Div");
      addStructureType("BlockQuote");
      addStructureType("Caption");
      addStructureType("TOC");
      addStructureType("TOCI");
      addStructureType("Index");
      addStructureType("NonStruct");
      addStructureType("Private");
      addStructureType("H");
      addStructureType("H1");
      addStructureType("H2");
      addStructureType("H3");
      addStructureType("H4");
      addStructureType("H5");
      addStructureType("H6");
      addStructureType("P");
      addStructureType("L");
      addStructureType("LI");
      addStructureType("Lbl");
      addStructureType("LBody");
      addStructureType("Table");
      addStructureType("TR");
      addStructureType("TH");
      addStructureType("TD");
      addStructureType("THead");
      addStructureType("TBody");
      addStructureType("TFoot");
      addStructureType("Span");
      addStructureType("Quote");
      addStructureType("Note");
      addStructureType("Reference");
      addStructureType("BibEntry");
      addStructureType("Code");
      addStructureType("Link");
      addStructureType("Annot");
      addStructureType("Ruby");
      addStructureType("RB");
      addStructureType("RT");
      addStructureType("RP");
      addStructureType("Warichu");
      addStructureType("WT");
      addStructureType("WP");
      addStructureType("Figure");
      addStructureType("Formula");
      addStructureType("Form");
      NON_STRUCT = (PDFName)STANDARD_STRUCTURE_TYPES.get("NonStruct");
      if (!$assertionsDisabled && NON_STRUCT == null) {
         throw new AssertionError();
      } else {
         THEAD = (PDFName)STANDARD_STRUCTURE_TYPES.get("THead");
         if (!$assertionsDisabled && THEAD == null) {
            throw new AssertionError();
         } else {
            addMapping("root", "Document");
            addMapping("page-sequence", "Part");
            addMapping("flow", "Sect");
            addMapping("static-content", "Sect");
            addMapping("block", "P");
            addMapping("block-container", "Div");
            addMapping("character", "Span");
            addMapping("external-graphic", "Figure");
            addMapping("instream-foreign-object", "Figure");
            addMapping("inline", "Span");
            addMapping("inline-container", "Div");
            addMapping("page-number", "Quote");
            addMapping("page-number-citation", "Quote");
            addMapping("page-number-citation-last", "Quote");
            addMapping("table-and-caption", "Div");
            addMapping("table", "Table");
            addMapping("table-caption", "Caption");
            addMapping("table-header", "THead");
            addMapping("table-footer", "TFoot");
            addMapping("table-body", "TBody");
            addMapping("table-row", "TR");
            addMapping("table-cell", (Mapper)(new TableCellMapper()));
            addMapping("list-block", "L");
            addMapping("list-item", "LI");
            addMapping("list-item-body", "LBody");
            addMapping("list-item-label", "Lbl");
            addMapping("basic-link", "Link");
            addMapping("float", "Div");
            addMapping("footnote", "Note");
            addMapping("footnote-body", "Sect");
            addMapping("wrapper", "Span");
            addMapping("marker", "Private");
         }
      }
   }

   private static class TableCellMapper implements Mapper {
      private TableCellMapper() {
      }

      public PDFName getStructureType(PDFObject parent) {
         PDFStructElem grandParent = ((PDFStructElem)parent).getParentStructElem();
         PDFName type;
         if (FOToPDFRoleMap.THEAD.equals(grandParent.getStructureType())) {
            type = (PDFName)FOToPDFRoleMap.STANDARD_STRUCTURE_TYPES.get("TH");
         } else {
            type = (PDFName)FOToPDFRoleMap.STANDARD_STRUCTURE_TYPES.get("TD");
         }

         assert type != null;

         return type;
      }

      // $FF: synthetic method
      TableCellMapper(Object x0) {
         this();
      }
   }

   private static class SimpleMapper implements Mapper {
      private PDFName structureType;

      public SimpleMapper(PDFName structureType) {
         this.structureType = structureType;
      }

      public PDFName getStructureType(PDFObject parent) {
         return this.structureType;
      }
   }

   private interface Mapper {
      PDFName getStructureType(PDFObject var1);
   }
}
