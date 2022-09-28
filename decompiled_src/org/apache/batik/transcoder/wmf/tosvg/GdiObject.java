package org.apache.batik.transcoder.wmf.tosvg;

public class GdiObject {
   int id;
   boolean used;
   Object obj;
   int type = 0;

   GdiObject(int var1, boolean var2) {
      this.id = var1;
      this.used = var2;
      this.type = 0;
   }

   public void clear() {
      this.used = false;
      this.type = 0;
   }

   public void Setup(int var1, Object var2) {
      this.obj = var2;
      this.type = var1;
      this.used = true;
   }

   public boolean isUsed() {
      return this.used;
   }

   public int getType() {
      return this.type;
   }

   public Object getObject() {
      return this.obj;
   }

   public int getID() {
      return this.id;
   }
}
