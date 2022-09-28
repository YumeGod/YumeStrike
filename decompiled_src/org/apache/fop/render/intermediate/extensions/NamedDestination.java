package org.apache.fop.render.intermediate.extensions;

public class NamedDestination {
   private String name;
   private AbstractAction action;

   public NamedDestination(String name, AbstractAction action) {
      this.name = name;
      this.action = action;
   }

   public String getName() {
      return this.name;
   }

   public AbstractAction getAction() {
      return this.action;
   }

   public void setAction(AbstractAction action) {
      this.action = action;
   }
}
