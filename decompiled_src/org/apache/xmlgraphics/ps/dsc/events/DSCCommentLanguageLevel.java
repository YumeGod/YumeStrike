package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public class DSCCommentLanguageLevel extends AbstractDSCComment {
   private int level;

   public DSCCommentLanguageLevel() {
   }

   public DSCCommentLanguageLevel(int level) {
      this.level = level;
   }

   public int getLanguageLevel() {
      return this.level;
   }

   public String getName() {
      return "LanguageLevel";
   }

   public boolean hasValues() {
      return true;
   }

   public void parseValue(String value) {
      this.level = Integer.parseInt(value);
   }

   public void generate(PSGenerator gen) throws IOException {
      if (this.level <= 0) {
         throw new IllegalStateException("Language Level was not properly set");
      } else {
         gen.writeDSCComment(this.getName(), (Object)(new Integer(this.getLanguageLevel())));
      }
   }
}
