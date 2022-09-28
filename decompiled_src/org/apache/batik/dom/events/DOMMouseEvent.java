package org.apache.batik.dom.events;

import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

public class DOMMouseEvent extends DOMUIEvent implements MouseEvent {
   private int screenX;
   private int screenY;
   private int clientX;
   private int clientY;
   private short button;
   private EventTarget relatedTarget;
   protected HashSet modifierKeys = new HashSet();

   public int getScreenX() {
      return this.screenX;
   }

   public int getScreenY() {
      return this.screenY;
   }

   public int getClientX() {
      return this.clientX;
   }

   public int getClientY() {
      return this.clientY;
   }

   public boolean getCtrlKey() {
      return this.modifierKeys.contains("Control");
   }

   public boolean getShiftKey() {
      return this.modifierKeys.contains("Shift");
   }

   public boolean getAltKey() {
      return this.modifierKeys.contains("Alt");
   }

   public boolean getMetaKey() {
      return this.modifierKeys.contains("Meta");
   }

   public short getButton() {
      return this.button;
   }

   public EventTarget getRelatedTarget() {
      return this.relatedTarget;
   }

   public boolean getModifierState(String var1) {
      return this.modifierKeys.contains(var1);
   }

   public String getModifiersString() {
      if (this.modifierKeys.isEmpty()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer(this.modifierKeys.size() * 8);
         Iterator var2 = this.modifierKeys.iterator();
         var1.append((String)var2.next());

         while(var2.hasNext()) {
            var1.append(' ');
            var1.append((String)var2.next());
         }

         return var1.toString();
      }
   }

   public void initMouseEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, short var14, EventTarget var15) {
      this.initUIEvent(var1, var2, var3, var4, var5);
      this.screenX = var6;
      this.screenY = var7;
      this.clientX = var8;
      this.clientY = var9;
      if (var10) {
         this.modifierKeys.add("Control");
      }

      if (var11) {
         this.modifierKeys.add("Alt");
      }

      if (var12) {
         this.modifierKeys.add("Shift");
      }

      if (var13) {
         this.modifierKeys.add("Meta");
      }

      this.button = var14;
      this.relatedTarget = var15;
   }

   public void initMouseEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, int var6, int var7, int var8, int var9, int var10, short var11, EventTarget var12, String var13) {
      this.initUIEventNS(var1, var2, var3, var4, var5, var6);
      this.screenX = var7;
      this.screenY = var8;
      this.clientX = var9;
      this.clientY = var10;
      this.button = var11;
      this.relatedTarget = var12;
      this.modifierKeys.clear();
      String[] var14 = this.split(var13);

      for(int var15 = 0; var15 < var14.length; ++var15) {
         this.modifierKeys.add(var14[var15]);
      }

   }
}
