package org.apache.xmlgraphics.ps.dsc.events;

public abstract class AbstractEvent implements DSCEvent {
   public boolean isComment() {
      return false;
   }

   public boolean isDSCComment() {
      return false;
   }

   public boolean isHeaderComment() {
      return false;
   }

   public boolean isLine() {
      return false;
   }

   public DSCComment asDSCComment() {
      throw new ClassCastException(this.getClass().getName());
   }

   public PostScriptLine asLine() {
      throw new ClassCastException(this.getClass().getName());
   }
}
