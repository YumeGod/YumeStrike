package org.apache.xmlgraphics.ps;

public class DSCConstants {
   public static final String PS_ADOBE_30 = "%!PS-Adobe-3.0";
   public static final String EPSF_30 = "EPSF-3.0";
   public static final String BBOX = "BoundingBox";
   public static final String HIRES_BBOX = "HiResBoundingBox";
   public static final String COPYRIGHT = "Copyright";
   public static final String CREATOR = "Creator";
   public static final String CREATION_DATE = "CreationDate";
   public static final String DOCUMENT_DATA = "BoundingBox";
   public static final String EMULATION = "Emulation";
   public static final String END_COMMENTS = "EndComments";
   public static final String EXTENSIONS = "Extensions";
   public static final String FOR = "For";
   public static final String LANGUAGE_LEVEL = "LanguageLevel";
   public static final String ORIENTATION = "Orientation";
   public static final String PAGES = "Pages";
   public static final String PAGE_ORDER = "PageOrder";
   public static final String ROUTING = "Routing";
   public static final String TITLE = "Title";
   public static final String VERSION = "Version";
   public static final String NEXT_LINE = "+ ";
   public static final String BEGIN_DATA = "BeginData";
   public static final String END_DATA = "EndData";
   public static final String BEGIN_DEFAULTS = "BeginDefaults";
   public static final String END_DEFAULTS = "EndDefaults";
   public static final String BEGIN_EMULATION = "BeginEmulation";
   public static final String END_EMULATION = "EndEmulation";
   public static final String BEGIN_PREVIEW = "BeginPreview";
   public static final String END_PREVIEW = "EndPreview";
   public static final String BEGIN_PROLOG = "BeginProlog";
   public static final String END_PROLOG = "EndProlog";
   public static final String BEGIN_SETUP = "BeginSetup";
   public static final String END_SETUP = "EndSetup";
   public static final String BEGIN_OBJECT = "BeginObject";
   public static final String END_OBJECT = "EndObject";
   public static final String BEGIN_PAGE_SETUP = "BeginPageSetup";
   public static final String END_PAGE_SETUP = "EndPageSetup";
   public static final String PAGE = "Page";
   public static final String PAGE_BBOX = "PageBoundingBox";
   public static final String PAGE_HIRES_BBOX = "PageHiResBoundingBox";
   public static final String PAGE_ORIENTATION = "PageOrientation";
   public static final String PAGE_TRAILER = "PageTrailer";
   public static final String TRAILER = "Trailer";
   /** @deprecated */
   public static final String END_PAGE = "EndPage";
   public static final String EOF = "EOF";
   public static final String DOCUMENT_MEDIA = "DocumentMedia";
   public static final String DOCUMENT_NEEDED_RESOURCES = "DocumentNeededResources";
   public static final String DOCUMENT_SUPPLIED_RESOURCES = "DocumentSuppliedResources";
   public static final String REQUIREMENTS = "Requirements";
   public static final String BEGIN_DOCUMENT = "BeginDocument";
   public static final String END_DOCUMENT = "EndDocument";
   public static final String INCLUDE_DOCUMENT = "IncludeDocument";
   public static final String BEGIN_FEATURE = "BeginFeature";
   public static final String END_FEATURE = "EndFeature";
   public static final String INCLUDE_FEATURE = "IncludeFeature";
   public static final String BEGIN_RESOURCE = "BeginResource";
   public static final String END_RESOURCE = "EndResource";
   public static final String INCLUDE_RESOURCE = "IncludeResource";
   public static final String PAGE_MEDIA = "PageMedia";
   public static final String PAGE_REQUIREMENTS = "PageRequirements";
   public static final String PAGE_RESOURCES = "PageResources";
   public static final Object ATEND = new AtendIndicator();

   private static final class AtendIndicator {
      private AtendIndicator() {
      }

      public String toString() {
         return "(atend)";
      }

      // $FF: synthetic method
      AtendIndicator(Object x0) {
         this();
      }
   }
}
