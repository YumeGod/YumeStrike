package org.apache.fop.area.inline;

public class TextArea extends AbstractTextArea {
   public TextArea() {
   }

   public TextArea(int stretch, int shrink, int adj) {
      super(stretch, shrink, adj);
   }

   public void removeText() {
      this.inlines.clear();
   }

   public void addWord(String word, int offset) {
      this.addWord(word, offset, (int[])null);
   }

   public void addWord(String word, int offset, int[] letterAdjust) {
      WordArea wordArea = new WordArea(word, offset, letterAdjust);
      this.addChildArea(wordArea);
      wordArea.setParentArea(this);
   }

   public void addSpace(char space, int offset, boolean adjustable) {
      SpaceArea spaceArea = new SpaceArea(space, offset, adjustable);
      this.addChildArea(spaceArea);
      spaceArea.setParentArea(this);
   }

   public String getText() {
      StringBuffer text = new StringBuffer();

      for(int i = 0; i < this.inlines.size(); ++i) {
         InlineArea child = (InlineArea)this.inlines.get(i);
         if (child instanceof WordArea) {
            text.append(((WordArea)child).getWord());
         } else {
            text.append(((SpaceArea)child).getSpace());
         }
      }

      return text.toString();
   }

   public String toString() {
      return "TextArea{text=" + this.getText() + "}";
   }
}
