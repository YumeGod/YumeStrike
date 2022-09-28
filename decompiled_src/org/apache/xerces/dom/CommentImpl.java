package org.apache.xerces.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;

public class CommentImpl extends CharacterDataImpl implements CharacterData, Comment {
   static final long serialVersionUID = -2685736833408134044L;

   public CommentImpl(CoreDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public short getNodeType() {
      return 8;
   }

   public String getNodeName() {
      return "#comment";
   }
}
