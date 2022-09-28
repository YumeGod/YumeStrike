package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.Writer;

/** @deprecated */
public class XHTMLSerializer extends HTMLSerializer {
   public XHTMLSerializer() {
      super(true, new OutputFormat("xhtml", (String)null, false));
   }

   public XHTMLSerializer(OutputFormat var1) {
      super(true, var1 != null ? var1 : new OutputFormat("xhtml", (String)null, false));
   }

   public XHTMLSerializer(Writer var1, OutputFormat var2) {
      super(true, var2 != null ? var2 : new OutputFormat("xhtml", (String)null, false));
      this.setOutputCharStream(var1);
   }

   public XHTMLSerializer(OutputStream var1, OutputFormat var2) {
      super(true, var2 != null ? var2 : new OutputFormat("xhtml", (String)null, false));
      this.setOutputByteStream(var1);
   }

   public void setOutputFormat(OutputFormat var1) {
      super.setOutputFormat(var1 != null ? var1 : new OutputFormat("xhtml", (String)null, false));
   }
}
