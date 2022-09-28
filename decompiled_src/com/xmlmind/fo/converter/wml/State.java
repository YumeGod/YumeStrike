package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.objects.Fo;
import java.util.Vector;

public final class State implements Cloneable {
   public static final int CONTEXT_FLOW = 1;
   public static final int CONTEXT_LABEL = 2;
   public static final int CONTEXT_BODY = 3;
   public static final int CONTEXT_TABLE = 4;
   public static final int CONTEXT_CAPTION = 5;
   public static final int CONTEXT_FOOTNOTE = 6;
   public static final int CONTEXT_STATIC_CONTENT = 7;
   public int fo;
   public int context;
   public int referenceWidth;
   public boolean skip;
   public List list;
   public Footnote footnote;
   public Table table;
   public TableAndCaption tableAndCaption;
   public RunProperties runProperties;
   public Link link;
   public Bookmark bookmark;
   public Paragraph paragraph;
   public Vector bookmarks;
   private Vector stack = new Vector();

   public State(int var1) {
      this.fo = var1;
   }

   public State update(int var1) throws CloneNotSupportedException {
      this.stack.addElement(this);
      State var2 = (State)this.clone();
      if (!var2.skip) {
         switch (var1) {
            case 9:
            case 15:
            case 23:
            case 27:
            case 54:
               var2.skip = true;
         }
      }

      if (!var2.skip && Fo.isBlock(var1)) {
         for(int var3 = this.stack.size() - 1; var3 >= 0; --var3) {
            State var4 = (State)this.stack.elementAt(var3);
            if (Fo.isBlock(var4.fo) || var4.fo == 12) {
               break;
            }

            if (Fo.isInline(var4.fo)) {
               var2.skip = true;
               break;
            }
         }
      }

      if (!var2.skip) {
         label44:
         switch (var1) {
            case 19:
               switch (var2.context) {
                  case 2:
                     var2.skip = true;
                  default:
                     break label44;
               }
            case 45:
            case 46:
               switch (var2.context) {
                  case 2:
                  case 5:
                  case 6:
                     var2.skip = true;
                  case 3:
                  case 4:
               }
         }
      }

      switch (var1) {
         case 10:
            var2.context = 1;
            break;
         case 12:
            var2.context = 6;
            break;
         case 21:
            var2.context = 3;
            break;
         case 22:
            var2.context = 2;
            break;
         case 44:
            var2.context = 7;
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
