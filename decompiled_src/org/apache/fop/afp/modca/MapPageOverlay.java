package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.fop.afp.util.BinaryUtils;

public class MapPageOverlay extends AbstractAFPObject {
   private static final int MAX_SIZE = 253;
   private List overLays = null;

   private List getOverlays() {
      if (this.overLays == null) {
         this.overLays = new ArrayList();
      }

      return this.overLays;
   }

   public void addOverlay(String name) throws MaximumSizeExceededException {
      if (this.getOverlays().size() > 253) {
         throw new MaximumSizeExceededException();
      } else if (name.length() != 8) {
         throw new IllegalArgumentException("The name of overlay " + name + " must be 8 characters");
      } else {
         if (log.isDebugEnabled()) {
            log.debug("addOverlay():: adding overlay " + name);
         }

         try {
            byte[] data = name.getBytes("Cp1146");
            this.getOverlays().add(data);
         } catch (UnsupportedEncodingException var3) {
            log.error("addOverlay():: UnsupportedEncodingException translating the name " + name);
         }

      }
   }

   public void writeToStream(OutputStream os) throws IOException {
      int oLayCount = this.getOverlays().size();
      int recordlength = oLayCount * 18;
      byte[] data = new byte[recordlength + 9];
      data[0] = 90;
      byte[] rl1 = BinaryUtils.convert(recordlength + 8, 2);
      data[1] = rl1[0];
      data[2] = rl1[1];
      data[3] = -45;
      data[4] = -85;
      data[5] = -40;
      data[6] = 0;
      data[7] = 0;
      data[8] = 0;
      int pos = 8;
      byte olayref = 0;

      for(int i = 0; i < oLayCount; ++i) {
         ++olayref;
         ++pos;
         data[pos] = 0;
         ++pos;
         data[pos] = 18;
         ++pos;
         data[pos] = 12;
         ++pos;
         data[pos] = 2;
         ++pos;
         data[pos] = -124;
         ++pos;
         data[pos] = 0;
         byte[] name = (byte[])this.overLays.get(i);

         for(int j = 0; j < name.length; ++j) {
            ++pos;
            data[pos] = name[j];
         }

         ++pos;
         data[pos] = 4;
         ++pos;
         data[pos] = 36;
         ++pos;
         data[pos] = 2;
         ++pos;
         data[pos] = olayref;
      }

      os.write(data);
   }
}
