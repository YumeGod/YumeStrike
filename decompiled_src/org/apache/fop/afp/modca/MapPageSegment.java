package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.fop.afp.util.BinaryUtils;

public class MapPageSegment extends AbstractAFPObject {
   private static final int MAX_SIZE = 127;
   private Set pageSegments = null;

   private Set getPageSegments() {
      if (this.pageSegments == null) {
         this.pageSegments = new HashSet();
      }

      return this.pageSegments;
   }

   public void addPageSegment(String name) throws MaximumSizeExceededException {
      if (this.getPageSegments().size() > 127) {
         throw new MaximumSizeExceededException();
      } else if (name.length() > 8) {
         throw new IllegalArgumentException("The name of page segment " + name + " must not be longer than 8 characters");
      } else {
         if (log.isDebugEnabled()) {
            log.debug("addPageSegment():: adding page segment " + name);
         }

         this.getPageSegments().add(name);
      }
   }

   public boolean isFull() {
      return this.pageSegments.size() >= 127;
   }

   public void writeToStream(OutputStream os) throws IOException {
      int count = this.getPageSegments().size();
      byte groupLength = 12;
      int groupsLength = count * groupLength;
      byte[] data = new byte[groupsLength + 12 + 1];
      data[0] = 90;
      byte[] rl1 = BinaryUtils.convert(data.length - 1, 2);
      data[1] = rl1[0];
      data[2] = rl1[1];
      data[3] = -45;
      data[4] = -79;
      data[5] = 95;
      data[6] = 0;
      data[7] = 0;
      data[8] = 0;
      data[9] = groupLength;
      data[10] = 0;
      data[11] = 0;
      data[12] = 0;
      int pos = 13;

      for(Iterator iter = this.pageSegments.iterator(); iter.hasNext(); pos += 8) {
         pos += 4;
         String name = (String)iter.next();

         try {
            byte[] nameBytes = name.getBytes("Cp1146");
            System.arraycopy(nameBytes, 0, data, pos, nameBytes.length);
         } catch (UnsupportedEncodingException var11) {
            log.error("UnsupportedEncodingException translating the name " + name);
         }
      }

      os.write(data);
   }
}
