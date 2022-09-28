package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlgraphics.ps.PSGenerator;

public class DSCCommentPage extends AbstractDSCComment {
   private String pageName;
   private int pagePosition;

   public DSCCommentPage() {
      this.pagePosition = -1;
   }

   public DSCCommentPage(String pageName, int pagePosition) {
      this.pagePosition = -1;
      this.setPageName(pageName);
      this.setPagePosition(pagePosition);
   }

   public DSCCommentPage(int pagePosition) {
      this(Integer.toString(pagePosition), pagePosition);
   }

   public String getPageName() {
      return this.pageName;
   }

   public void setPageName(String name) {
      this.pageName = name;
   }

   public int getPagePosition() {
      return this.pagePosition;
   }

   public void setPagePosition(int position) {
      if (position <= 0) {
         throw new IllegalArgumentException("position must be 1 or above");
      } else {
         this.pagePosition = position;
      }
   }

   public String getName() {
      return "Page";
   }

   public boolean hasValues() {
      return true;
   }

   public void parseValue(String value) {
      List params = this.splitParams(value);
      Iterator iter = params.iterator();
      this.pageName = (String)iter.next();
      this.pagePosition = Integer.parseInt((String)iter.next());
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeDSCComment(this.getName(), new Object[]{this.getPageName(), new Integer(this.getPagePosition())});
   }
}
