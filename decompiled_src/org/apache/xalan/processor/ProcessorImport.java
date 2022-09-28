package org.apache.xalan.processor;

public class ProcessorImport extends ProcessorInclude {
   static final long serialVersionUID = -8247537698214245237L;

   protected int getStylesheetType() {
      return 3;
   }

   protected String getStylesheetInclErr() {
      return "ER_IMPORTING_ITSELF";
   }
}
