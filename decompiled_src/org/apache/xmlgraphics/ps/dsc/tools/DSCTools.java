package org.apache.xmlgraphics.ps.dsc.tools;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.dsc.DSCException;
import org.apache.xmlgraphics.ps.dsc.DSCParser;
import org.apache.xmlgraphics.ps.dsc.DSCParserConstants;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;
import org.apache.xmlgraphics.ps.dsc.events.DSCEvent;
import org.apache.xmlgraphics.ps.dsc.events.DSCHeaderComment;
import org.apache.xmlgraphics.ps.dsc.events.PostScriptComment;

public class DSCTools implements DSCParserConstants {
   public static boolean headerCommentsEndHere(DSCEvent event) {
      switch (event.getEventType()) {
         case 1:
            DSCComment comment = event.asDSCComment();
            return comment.getName().equals("EndComments");
         case 2:
            String s = ((PostScriptComment)event).getComment();
            if (s != null && s.length() != 0) {
               char c = s.charAt(0);
               return "\n\t ".indexOf(c) >= 0;
            }

            return true;
         default:
            return true;
      }
   }

   public static DSCHeaderComment checkAndSkipDSC30Header(DSCParser parser) throws DSCException, IOException {
      if (!parser.hasNext()) {
         throw new DSCException("File has no content");
      } else {
         DSCEvent event = parser.nextEvent();
         if (event.getEventType() == 0) {
            DSCHeaderComment header = (DSCHeaderComment)event;
            if (!header.isPSAdobe30()) {
               throw new DSCException("PostScript file does not start with '%!PS-Adobe-3.0'");
            } else {
               return header;
            }
         } else {
            throw new DSCException("PostScript file does not start with '%!PS-Adobe-3.0'");
         }
      }
   }

   public static DSCComment nextPageOrTrailer(DSCParser parser, PSGenerator gen) throws IOException, DSCException {
      while(parser.hasNext()) {
         DSCEvent event = parser.nextEvent();
         if (event.getEventType() == 1) {
            DSCComment comment = event.asDSCComment();
            if ("Page".equals(comment.getName())) {
               return comment;
            }

            if ("Trailer".equals(comment.getName())) {
               return comment;
            }
         } else if (event.getEventType() == 4) {
            return event.asDSCComment();
         }

         if (gen != null) {
            event.generate(gen);
         }
      }

      return null;
   }
}
