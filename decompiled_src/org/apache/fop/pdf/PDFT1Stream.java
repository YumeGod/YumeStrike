package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.fonts.type1.PFBData;

public class PDFT1Stream extends AbstractPDFFontStream {
   private PFBData pfb;

   protected int getSizeHint() throws IOException {
      return this.pfb != null ? this.pfb.getLength() : 0;
   }

   protected int output(OutputStream stream) throws IOException {
      if (this.pfb == null) {
         throw new IllegalStateException("pfb must not be null at this point");
      } else {
         if (log.isDebugEnabled()) {
            log.debug("Writing " + this.pfb.getLength() + " bytes of Type 1 font data");
         }

         int length = super.output(stream);
         log.debug("Embedded Type1 font");
         return length;
      }
   }

   protected void populateStreamDict(Object lengthEntry) {
      super.populateStreamDict(lengthEntry);
      this.put("Length1", new Integer(this.pfb.getLength1()));
      this.put("Length2", new Integer(this.pfb.getLength2()));
      this.put("Length3", new Integer(this.pfb.getLength3()));
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      this.pfb.outputAllParts(out);
   }

   public void setData(PFBData pfb) throws IOException {
      this.pfb = pfb;
   }
}
