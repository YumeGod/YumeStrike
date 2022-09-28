package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.util.Iterator;
import java.util.StringTokenizer;

class WhitespaceCollapser {
   private static final String SPACE = " ";
   private boolean lastEndSpace = true;

   WhitespaceCollapser(RtfContainer c) {
      Iterator it = c.getChildren().iterator();

      while(it.hasNext()) {
         Object kid = it.next();
         if (kid instanceof RtfText) {
            RtfText current = (RtfText)kid;
            this.processText(current);
         } else if (kid instanceof RtfString) {
            RtfString current = (RtfString)kid;
            this.processString(current);
         } else {
            this.lastEndSpace = true;
         }
      }

   }

   private void processText(RtfText txt) {
      String newString = this.processString(txt.getText());
      if (newString != null) {
         txt.setText(newString);
      }

   }

   private void processString(RtfString txt) {
      String newString = this.processString(txt.getText());
      if (newString != null) {
         txt.setText(newString);
      }

   }

   private String processString(String txt) {
      if (txt == null) {
         return null;
      } else if (txt.length() > 0) {
         boolean allSpaces = txt.trim().length() == 0;
         boolean endSpace = allSpaces || Character.isWhitespace(txt.charAt(txt.length() - 1));
         boolean beginSpace = Character.isWhitespace(txt.charAt(0));
         StringBuffer sb = new StringBuffer(txt.length());
         if (allSpaces) {
            if (!this.lastEndSpace) {
               sb.append(" ");
            }
         } else {
            boolean first = true;
            StringTokenizer stk = new StringTokenizer(txt, " \t\n\r");

            label44:
            while(true) {
               do {
                  if (!stk.hasMoreTokens()) {
                     break label44;
                  }

                  if (first && beginSpace && !this.lastEndSpace) {
                     sb.append(" ");
                  }

                  first = false;
                  sb.append(stk.nextToken());
               } while(!stk.hasMoreTokens() && !endSpace);

               sb.append(" ");
            }
         }

         this.lastEndSpace = endSpace;
         return sb.toString();
      } else {
         return "";
      }
   }
}
