package org.apache.xmlgraphics.ps.dsc.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.dsc.DSCException;
import org.apache.xmlgraphics.ps.dsc.DSCFilter;
import org.apache.xmlgraphics.ps.dsc.DSCParser;
import org.apache.xmlgraphics.ps.dsc.DSCParserConstants;
import org.apache.xmlgraphics.ps.dsc.DefaultNestedDocumentHandler;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentPage;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentPages;
import org.apache.xmlgraphics.ps.dsc.events.DSCEvent;
import org.apache.xmlgraphics.ps.dsc.events.DSCHeaderComment;

public class PageExtractor implements DSCParserConstants {
   public static void extractPages(InputStream in, OutputStream out, int from, int to) throws IOException, DSCException {
      if (from <= 0) {
         throw new IllegalArgumentException("'from' page number must be 1 or higher");
      } else if (to < from) {
         throw new IllegalArgumentException("'to' page number must be equal or larger than the 'from' page number");
      } else {
         DSCParser parser = new DSCParser(in);
         PSGenerator gen = new PSGenerator(out);
         parser.addListener(new DefaultNestedDocumentHandler(gen));
         int pageCount = 0;
         DSCHeaderComment header = DSCTools.checkAndSkipDSC30Header(parser);
         header.generate(gen);
         DSCCommentPages pages = new DSCCommentPages(to - from + 1);
         pages.generate(gen);
         parser.setFilter(new DSCFilter() {
            public boolean accept(DSCEvent event) {
               if (event.isDSCComment()) {
                  return !event.asDSCComment().getName().equals("Pages");
               } else {
                  return true;
               }
            }
         });
         DSCComment pageOrTrailer = parser.nextDSCComment("Page", gen);
         if (pageOrTrailer == null) {
            throw new DSCException("Page expected, but none found");
         } else {
            parser.setFilter((DSCFilter)null);

            do {
               DSCCommentPage page = (DSCCommentPage)pageOrTrailer;
               boolean validPage = page.getPagePosition() >= from && page.getPagePosition() <= to;
               if (validPage) {
                  page.setPagePosition(page.getPagePosition() - from + 1);
                  page.generate(gen);
                  ++pageCount;
               }

               pageOrTrailer = DSCTools.nextPageOrTrailer(parser, validPage ? gen : null);
               if (pageOrTrailer == null) {
                  throw new DSCException("File is not DSC-compliant: Unexpected end of file");
               }
            } while("Page".equals(pageOrTrailer.getName()));

            pageOrTrailer.generate(gen);

            while(parser.hasNext()) {
               DSCEvent event = parser.nextEvent();
               event.generate(gen);
            }

         }
      }
   }
}
