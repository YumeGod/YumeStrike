package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class RtfJforCmd extends RtfContainer {
   private static final String PARA_KEEP_ON = "para-keep:on";
   private static final String PARA_KEEP_OFF = "para-keep:off";
   private final RtfAttributes attrib;
   private ParagraphKeeptogetherContext paragraphKeeptogetherContext;

   RtfJforCmd(RtfContainer parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w);
      this.attrib = attrs;
      this.paragraphKeeptogetherContext = ParagraphKeeptogetherContext.getInstance();
   }

   public boolean isEmpty() {
      return true;
   }

   public void process() {
      Iterator it = this.attrib.nameIterator();

      while(it.hasNext()) {
         String cmd = (String)it.next();
         if (cmd.equals("para-keep:on")) {
            ParagraphKeeptogetherContext.keepTogetherOpen();
         } else if (cmd.equals("para-keep:off")) {
            ParagraphKeeptogetherContext.keepTogetherClose();
         }
      }

   }
}
