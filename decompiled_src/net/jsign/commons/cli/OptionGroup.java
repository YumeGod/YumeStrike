package net.jsign.commons.cli;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OptionGroup implements Serializable {
   private static final long serialVersionUID = 1L;
   private final Map optionMap = new HashMap();
   private String selected;
   private boolean required;

   public OptionGroup addOption(Option option) {
      this.optionMap.put(option.getKey(), option);
      return this;
   }

   public Collection getNames() {
      return this.optionMap.keySet();
   }

   public Collection getOptions() {
      return this.optionMap.values();
   }

   public void setSelected(Option option) throws AlreadySelectedException {
      if (option == null) {
         this.selected = null;
      } else if (this.selected != null && !this.selected.equals(option.getKey())) {
         throw new AlreadySelectedException(this, option);
      } else {
         this.selected = option.getKey();
      }
   }

   public String getSelected() {
      return this.selected;
   }

   public void setRequired(boolean required) {
      this.required = required;
   }

   public boolean isRequired() {
      return this.required;
   }

   public String toString() {
      StringBuilder buff = new StringBuilder();
      Iterator iter = this.getOptions().iterator();
      buff.append("[");

      while(iter.hasNext()) {
         Option option = (Option)iter.next();
         if (option.getOpt() != null) {
            buff.append("-");
            buff.append(option.getOpt());
         } else {
            buff.append("--");
            buff.append(option.getLongOpt());
         }

         if (option.getDescription() != null) {
            buff.append(" ");
            buff.append(option.getDescription());
         }

         if (iter.hasNext()) {
            buff.append(", ");
         }
      }

      buff.append("]");
      return buff.toString();
   }
}
