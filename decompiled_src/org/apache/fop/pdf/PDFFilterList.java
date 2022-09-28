package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDFFilterList {
   public static final String DEFAULT_FILTER = "default";
   public static final String CONTENT_FILTER = "content";
   public static final String PRECOMPRESSED_FILTER = "precompressed";
   public static final String IMAGE_FILTER = "image";
   public static final String JPEG_FILTER = "jpeg";
   public static final String TIFF_FILTER = "tiff";
   public static final String FONT_FILTER = "font";
   public static final String METADATA_FILTER = "metadata";
   private List filters = new ArrayList();
   private boolean ignoreASCIIFilters = false;
   private boolean disableAllFilters = false;

   public PDFFilterList() {
   }

   public PDFFilterList(boolean ignoreASCIIFilters) {
      this.ignoreASCIIFilters = ignoreASCIIFilters;
   }

   public void setDisableAllFilters(boolean value) {
      this.disableAllFilters = value;
   }

   public boolean isDisableAllFilters() {
      return this.disableAllFilters;
   }

   public boolean isInitialized() {
      return this.filters.size() > 0;
   }

   public void addFilter(PDFFilter filter) {
      if (filter != null) {
         if (this.ignoreASCIIFilters && filter.isASCIIFilter()) {
            return;
         }

         this.filters.add(filter);
      }

   }

   public void addFilter(String filterType) {
      if (filterType != null) {
         if (filterType.equals("flate")) {
            this.addFilter((PDFFilter)(new FlateFilter()));
         } else if (filterType.equals("null")) {
            this.addFilter((PDFFilter)(new NullFilter()));
         } else if (filterType.equals("ascii-85")) {
            if (this.ignoreASCIIFilters) {
               return;
            }

            this.addFilter((PDFFilter)(new ASCII85Filter()));
         } else {
            if (!filterType.equals("ascii-hex")) {
               if (filterType.equals("")) {
                  return;
               }

               throw new IllegalArgumentException("Unsupported filter type in stream-filter-list: " + filterType);
            }

            if (this.ignoreASCIIFilters) {
               return;
            }

            this.addFilter((PDFFilter)(new ASCIIHexFilter()));
         }

      }
   }

   public void ensureFilterInPlace(PDFFilter pdfFilter) {
      if (this.filters.size() == 0) {
         this.addFilter(pdfFilter);
      } else if (!this.filters.get(0).equals(pdfFilter)) {
         this.filters.add(0, pdfFilter);
      }

   }

   public void addDefaultFilters(Map filters, String type) {
      if ("metadata".equals(type)) {
         this.addFilter((PDFFilter)(new NullFilter()));
      } else {
         List filterset = null;
         if (filters != null) {
            filterset = (List)filters.get(type);
            if (filterset == null) {
               filterset = (List)filters.get("default");
            }
         }

         if (filterset != null && filterset.size() != 0) {
            for(int i = 0; i < filterset.size(); ++i) {
               String v = (String)filterset.get(i);
               this.addFilter(v);
            }
         } else if ("jpeg".equals(type)) {
            this.addFilter((PDFFilter)(new NullFilter()));
         } else if ("tiff".equals(type)) {
            this.addFilter((PDFFilter)(new NullFilter()));
         } else if ("precompressed".equals(type)) {
            this.addFilter((PDFFilter)(new NullFilter()));
         } else {
            this.addFilter((PDFFilter)(new FlateFilter()));
         }

      }
   }

   protected String buildFilterDictEntries() {
      if (this.filters != null && this.filters.size() > 0) {
         List names = new ArrayList();
         List parms = new ArrayList();
         int nonNullParams = this.populateNamesAndParms(names, parms);
         return this.buildFilterEntries(names) + (nonNullParams > 0 ? this.buildDecodeParms(parms) : "");
      } else {
         return "";
      }
   }

   protected void putFilterDictEntries(PDFDictionary dict) {
      if (this.filters != null && this.filters.size() > 0) {
         List names = new ArrayList();
         List parms = new ArrayList();
         this.populateNamesAndParms(names, parms);
         this.putFilterEntries(dict, names);
         this.putDecodeParams(dict, parms);
      }

   }

   private int populateNamesAndParms(List names, List parms) {
      int nonNullParams = 0;

      for(int count = 0; count < this.filters.size(); ++count) {
         PDFFilter filter = (PDFFilter)this.filters.get(count);
         if (filter.getName().length() > 0) {
            names.add(0, filter.getName());
            PDFObject param = filter.getDecodeParms();
            if (param != null) {
               parms.add(0, param);
               ++nonNullParams;
            } else {
               parms.add(0, (Object)null);
            }
         }
      }

      return nonNullParams;
   }

   private String buildFilterEntries(List names) {
      int filterCount = 0;
      StringBuffer sb = new StringBuffer(64);

      for(int i = 0; i < names.size(); ++i) {
         String name = (String)names.get(i);
         if (name.length() > 0) {
            ++filterCount;
            sb.append(name);
            sb.append(" ");
         }
      }

      if (filterCount > 0) {
         if (filterCount > 1) {
            return "/Filter [ " + sb.toString() + "]";
         } else {
            return "/Filter " + sb.toString();
         }
      } else {
         return "";
      }
   }

   private void putFilterEntries(PDFDictionary dict, List names) {
      PDFArray array = new PDFArray(dict);
      int i = 0;

      for(int c = names.size(); i < c; ++i) {
         String name = (String)names.get(i);
         if (name.length() > 0) {
            array.add(new PDFName(name));
         }
      }

      if (array.length() > 0) {
         if (array.length() > 1) {
            dict.put("Filter", array);
         } else {
            dict.put("Filter", array.get(0));
         }
      }

   }

   private String buildDecodeParms(List parms) {
      StringBuffer sb = new StringBuffer();
      boolean needParmsEntry = false;
      sb.append("\n/DecodeParms ");
      if (parms.size() > 1) {
         sb.append("[ ");
      }

      for(int count = 0; count < parms.size(); ++count) {
         String s = (String)parms.get(count);
         if (s != null) {
            sb.append(s);
            needParmsEntry = true;
         } else {
            sb.append("null");
         }

         sb.append(" ");
      }

      if (parms.size() > 1) {
         sb.append("]");
      }

      if (needParmsEntry) {
         return sb.toString();
      } else {
         return "";
      }
   }

   private void putDecodeParams(PDFDictionary dict, List parms) {
      boolean needParmsEntry = false;
      PDFArray array = new PDFArray(dict);
      int i = 0;

      for(int c = parms.size(); i < c; ++i) {
         Object obj = parms.get(i);
         if (obj != null) {
            array.add(obj);
            needParmsEntry = true;
         } else {
            array.add((Object)null);
         }
      }

      if (array.length() > 0 & needParmsEntry) {
         if (array.length() > 1) {
            dict.put("DecodeParms", array);
         } else {
            dict.put("DecodeParms", array.get(0));
         }
      }

   }

   public OutputStream applyFilters(OutputStream stream) throws IOException {
      OutputStream out = stream;
      if (this.filters != null && !this.isDisableAllFilters()) {
         for(int count = this.filters.size() - 1; count >= 0; --count) {
            PDFFilter filter = (PDFFilter)this.filters.get(count);
            out = filter.applyFilter(out);
         }
      }

      return out;
   }
}
