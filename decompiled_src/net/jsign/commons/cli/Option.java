package net.jsign.commons.cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Option implements Cloneable, Serializable {
   public static final int UNINITIALIZED = -1;
   public static final int UNLIMITED_VALUES = -2;
   private static final long serialVersionUID = 1L;
   private final String opt;
   private String longOpt;
   private String argName;
   private String description;
   private boolean required;
   private boolean optionalArg;
   private int numberOfArgs;
   private Class type;
   private List values;
   private char valuesep;

   private Option(Builder builder) {
      this.numberOfArgs = -1;
      this.type = String.class;
      this.values = new ArrayList();
      this.argName = builder.argName;
      this.description = builder.description;
      this.longOpt = builder.longOpt;
      this.numberOfArgs = builder.numberOfArgs;
      this.opt = builder.opt;
      this.optionalArg = builder.optionalArg;
      this.required = builder.required;
      this.type = builder.type;
      this.valuesep = builder.valuesep;
   }

   public Option(String opt, String description) throws IllegalArgumentException {
      this(opt, (String)null, false, description);
   }

   public Option(String opt, boolean hasArg, String description) throws IllegalArgumentException {
      this(opt, (String)null, hasArg, description);
   }

   public Option(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
      this.numberOfArgs = -1;
      this.type = String.class;
      this.values = new ArrayList();
      OptionValidator.validateOption(opt);
      this.opt = opt;
      this.longOpt = longOpt;
      if (hasArg) {
         this.numberOfArgs = 1;
      }

      this.description = description;
   }

   public int getId() {
      return this.getKey().charAt(0);
   }

   String getKey() {
      return this.opt == null ? this.longOpt : this.opt;
   }

   public String getOpt() {
      return this.opt;
   }

   public Object getType() {
      return this.type;
   }

   /** @deprecated */
   @Deprecated
   public void setType(Object type) {
      this.setType((Class)type);
   }

   public void setType(Class type) {
      this.type = type;
   }

   public String getLongOpt() {
      return this.longOpt;
   }

   public void setLongOpt(String longOpt) {
      this.longOpt = longOpt;
   }

   public void setOptionalArg(boolean optionalArg) {
      this.optionalArg = optionalArg;
   }

   public boolean hasOptionalArg() {
      return this.optionalArg;
   }

   public boolean hasLongOpt() {
      return this.longOpt != null;
   }

   public boolean hasArg() {
      return this.numberOfArgs > 0 || this.numberOfArgs == -2;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public boolean isRequired() {
      return this.required;
   }

   public void setRequired(boolean required) {
      this.required = required;
   }

   public void setArgName(String argName) {
      this.argName = argName;
   }

   public String getArgName() {
      return this.argName;
   }

   public boolean hasArgName() {
      return this.argName != null && this.argName.length() > 0;
   }

   public boolean hasArgs() {
      return this.numberOfArgs > 1 || this.numberOfArgs == -2;
   }

   public void setArgs(int num) {
      this.numberOfArgs = num;
   }

   public void setValueSeparator(char sep) {
      this.valuesep = sep;
   }

   public char getValueSeparator() {
      return this.valuesep;
   }

   public boolean hasValueSeparator() {
      return this.valuesep > 0;
   }

   public int getArgs() {
      return this.numberOfArgs;
   }

   void addValueForProcessing(String value) {
      if (this.numberOfArgs == -1) {
         throw new RuntimeException("NO_ARGS_ALLOWED");
      } else {
         this.processValue(value);
      }
   }

   private void processValue(String value) {
      if (this.hasValueSeparator()) {
         char sep = this.getValueSeparator();

         for(int index = value.indexOf(sep); index != -1 && this.values.size() != this.numberOfArgs - 1; index = value.indexOf(sep)) {
            this.add(value.substring(0, index));
            value = value.substring(index + 1);
         }
      }

      this.add(value);
   }

   private void add(String value) {
      if (!this.acceptsArg()) {
         throw new RuntimeException("Cannot add value, list full.");
      } else {
         this.values.add(value);
      }
   }

   public String getValue() {
      return this.hasNoValues() ? null : (String)this.values.get(0);
   }

   public String getValue(int index) throws IndexOutOfBoundsException {
      return this.hasNoValues() ? null : (String)this.values.get(index);
   }

   public String getValue(String defaultValue) {
      String value = this.getValue();
      return value != null ? value : defaultValue;
   }

   public String[] getValues() {
      return this.hasNoValues() ? null : (String[])this.values.toArray(new String[this.values.size()]);
   }

   public List getValuesList() {
      return this.values;
   }

   public String toString() {
      StringBuilder buf = (new StringBuilder()).append("[ option: ");
      buf.append(this.opt);
      if (this.longOpt != null) {
         buf.append(" ").append(this.longOpt);
      }

      buf.append(" ");
      if (this.hasArgs()) {
         buf.append("[ARG...]");
      } else if (this.hasArg()) {
         buf.append(" [ARG]");
      }

      buf.append(" :: ").append(this.description);
      if (this.type != null) {
         buf.append(" :: ").append(this.type);
      }

      buf.append(" ]");
      return buf.toString();
   }

   private boolean hasNoValues() {
      return this.values.isEmpty();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Option option = (Option)o;
         if (this.opt != null) {
            if (!this.opt.equals(option.opt)) {
               return false;
            }
         } else if (option.opt != null) {
            return false;
         }

         if (this.longOpt != null) {
            if (this.longOpt.equals(option.longOpt)) {
               return true;
            }
         } else if (option.longOpt == null) {
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.opt != null ? this.opt.hashCode() : 0;
      result = 31 * result + (this.longOpt != null ? this.longOpt.hashCode() : 0);
      return result;
   }

   public Object clone() {
      try {
         Option option = (Option)super.clone();
         option.values = new ArrayList(this.values);
         return option;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException("A CloneNotSupportedException was thrown: " + var2.getMessage());
      }
   }

   void clearValues() {
      this.values.clear();
   }

   /** @deprecated */
   @Deprecated
   public boolean addValue(String value) {
      throw new UnsupportedOperationException("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ");
   }

   boolean acceptsArg() {
      return (this.hasArg() || this.hasArgs() || this.hasOptionalArg()) && (this.numberOfArgs <= 0 || this.values.size() < this.numberOfArgs);
   }

   boolean requiresArg() {
      if (this.optionalArg) {
         return false;
      } else {
         return this.numberOfArgs == -2 ? this.values.isEmpty() : this.acceptsArg();
      }
   }

   public static Builder builder() {
      return builder((String)null);
   }

   public static Builder builder(String opt) {
      return new Builder(opt);
   }

   // $FF: synthetic method
   Option(Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private final String opt;
      private String description;
      private String longOpt;
      private String argName;
      private boolean required;
      private boolean optionalArg;
      private int numberOfArgs;
      private Class type;
      private char valuesep;

      private Builder(String opt) throws IllegalArgumentException {
         this.numberOfArgs = -1;
         this.type = String.class;
         OptionValidator.validateOption(opt);
         this.opt = opt;
      }

      public Builder argName(String argName) {
         this.argName = argName;
         return this;
      }

      public Builder desc(String description) {
         this.description = description;
         return this;
      }

      public Builder longOpt(String longOpt) {
         this.longOpt = longOpt;
         return this;
      }

      public Builder numberOfArgs(int numberOfArgs) {
         this.numberOfArgs = numberOfArgs;
         return this;
      }

      public Builder optionalArg(boolean isOptional) {
         this.optionalArg = isOptional;
         return this;
      }

      public Builder required() {
         return this.required(true);
      }

      public Builder required(boolean required) {
         this.required = required;
         return this;
      }

      public Builder type(Class type) {
         this.type = type;
         return this;
      }

      public Builder valueSeparator() {
         return this.valueSeparator('=');
      }

      public Builder valueSeparator(char sep) {
         this.valuesep = sep;
         return this;
      }

      public Builder hasArg() {
         return this.hasArg(true);
      }

      public Builder hasArg(boolean hasArg) {
         this.numberOfArgs = hasArg ? 1 : -1;
         return this;
      }

      public Builder hasArgs() {
         this.numberOfArgs = -2;
         return this;
      }

      public Option build() {
         if (this.opt == null && this.longOpt == null) {
            throw new IllegalArgumentException("Either opt or longOpt must be specified");
         } else {
            return new Option(this);
         }
      }

      // $FF: synthetic method
      Builder(String x0, Object x1) throws IllegalArgumentException {
         this(x0);
      }
   }
}
