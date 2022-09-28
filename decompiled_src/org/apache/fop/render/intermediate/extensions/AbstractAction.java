package org.apache.fop.render.intermediate.extensions;

import org.apache.xmlgraphics.util.XMLizable;

public abstract class AbstractAction implements XMLizable {
   private String id;
   private String structurePointer;

   public void setID(String id) {
      this.id = id;
   }

   public String getID() {
      return this.id;
   }

   public void setStructurePointer(String structurePointer) {
      this.structurePointer = structurePointer;
   }

   public String getStructurePointer() {
      return this.structurePointer;
   }

   public boolean hasID() {
      return this.id != null;
   }

   public abstract boolean isSame(AbstractAction var1);

   public boolean isComplete() {
      return true;
   }

   public String getIDPrefix() {
      return null;
   }
}
