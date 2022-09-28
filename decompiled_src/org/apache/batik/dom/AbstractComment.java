package org.apache.batik.dom;

import org.w3c.dom.Comment;

public abstract class AbstractComment extends AbstractCharacterData implements Comment {
   public String getNodeName() {
      return "#comment";
   }

   public short getNodeType() {
      return 8;
   }

   public String getTextContent() {
      return this.getNodeValue();
   }
}
