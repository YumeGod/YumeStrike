package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.objects.Fo;
import java.util.Vector;

public final class State implements Cloneable {
   public static final int CONTEXT_FLOW = 1;
   public static final int CONTEXT_LIST_ITEM_LABEL = 2;
   public static final int CONTEXT_LIST_ITEM_BODY = 3;
   public static final int CONTEXT_TABLE = 4;
   public static final int CONTEXT_CAPTION = 5;
   public static final int CONTEXT_FOOTNOTE_LABEL = 6;
   public static final int CONTEXT_FOOTNOTE_BODY = 7;
   public static final int CONTEXT_STATIC_CONTENT = 8;
   public int fo;
   public int context;
   public int listLevel;
   public double referenceWidth;
   public boolean skipContent;
   public Table table;
   public TableAndCaption tableAndCaption;
   public List list;
   public ListItem listItem;
   public Footnote footnote;
   public Link link;
   public Bookmark bookmark;
   public Paragraph paragraph;
   public Vector bookmarks;
   public RunProperties runProperties;
   private Vector stack = new Vector();

   public State(int var1) {
      this.fo = var1;
   }

   public State update(int var1) throws CloneNotSupportedException {
      State var2;
      this.stack.addElement(this);
      var2 = (State)this.clone();
      label44:
      switch (var1) {
         case 9:
         case 15:
         case 23:
         case 27:
         case 54:
            var2.skipContent = true;
            break;
         case 19:
            ++var2.listLevel;
            switch (var2.context) {
               case 2:
                  var2.skipContent = true;
               default:
                  break label44;
            }
         case 45:
         case 46:
            switch (var2.context) {
               case 2:
               case 5:
               case 7:
                  var2.skipContent = true;
               default:
                  break label44;
            }
         case 49:
            var2.listLevel = 0;
      }

      if (!var2.skipContent && Fo.isBlock(var1)) {
         for(int var3 = this.stack.size() - 1; var3 >= 0; --var3) {
            State var4 = (State)this.stack.elementAt(var3);
            if (Fo.isBlock(var4.fo) || var4.fo == 12) {
               break;
            }

            if (Fo.isInline(var4.fo)) {
               var2.skipContent = true;
               break;
            }
         }
      }

      switch (var1) {
         case 10:
            var2.context = 1;
            break;
         case 11:
            var2.context = 6;
            break;
         case 12:
            var2.context = 7;
            break;
         case 21:
            var2.context = 3;
            break;
         case 22:
            var2.context = 2;
            break;
         case 44:
            var2.context = 8;
            break;
         case 45:
            var2.context = 4;
            break;
         case 48:
            var2.context = 5;
      }

      var2.fo = var1;
      var2.runProperties = null;
      var2.bookmark = null;
      return var2;
   }

   public State restore() {
      int var1 = this.stack.size();
      State var2;
      if (var1 > 0) {
         var2 = (State)this.stack.lastElement();
         this.stack.removeElementAt(var1 - 1);
      } else {
         var2 = this;
      }

      return var2;
   }

   public int parentContext() {
      int var1 = this.context;

      for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
         State var3 = (State)this.stack.elementAt(var2);
         if (var3.context != var1) {
            var1 = var3.context;
            break;
         }
      }

      return var1;
   }
}
