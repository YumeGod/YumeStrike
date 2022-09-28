package org.apache.xmlgraphics.ps;

public class PSPageDeviceDictionary extends PSDictionary {
   private static final long serialVersionUID = 845943256485806509L;
   private boolean flushOnRetrieval = false;
   private PSDictionary unRetrievedContentDictionary;

   public Object put(Object key, Object value) {
      Object previousValue = super.put(key, value);
      if (this.flushOnRetrieval && (previousValue == null || !previousValue.equals(value))) {
         this.unRetrievedContentDictionary.put(key, value);
      }

      return previousValue;
   }

   public void clear() {
      super.clear();
      if (this.unRetrievedContentDictionary != null) {
         this.unRetrievedContentDictionary.clear();
      }

   }

   public boolean isEmpty() {
      return this.flushOnRetrieval ? this.unRetrievedContentDictionary.isEmpty() : super.isEmpty();
   }

   public void setFlushOnRetrieval(boolean flushOnRetrieval) {
      this.flushOnRetrieval = flushOnRetrieval;
      if (flushOnRetrieval) {
         this.unRetrievedContentDictionary = new PSDictionary();
      }

   }

   public String getContent() {
      String content;
      if (this.flushOnRetrieval) {
         content = this.unRetrievedContentDictionary.toString();
         this.unRetrievedContentDictionary.clear();
      } else {
         content = super.toString();
      }

      return content;
   }
}
