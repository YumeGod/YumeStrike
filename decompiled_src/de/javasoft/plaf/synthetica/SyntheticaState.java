package de.javasoft.plaf.synthetica;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class SyntheticaState {
   private int cState;

   public SyntheticaState() {
      this.cState = 0;
   }

   public SyntheticaState(int var1) {
      this();
      this.cState = var1;
   }

   public SyntheticaState(State var1) {
      this.cState = 0;
      this.setState(var1);
   }

   public int getState() {
      return this.cState;
   }

   public void setState(int var1) {
      this.cState = var1;
   }

   public void setState(State var1) {
      this.cState |= var1.toInt();
   }

   public void resetState(State var1) {
      if (this.isSet(var1)) {
         this.cState ^= var1.toInt();
      }

   }

   public boolean isSet(State var1) {
      return (this.getState() & var1.toInt()) > 0;
   }

   public boolean isSet(int var1) {
      return (this.getState() & var1) != 0;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SyntheticaState) {
         return ((SyntheticaState)var1).getState() == this.getState();
      } else {
         return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      State[] var5;
      int var4 = (var5 = SyntheticaState.State.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         State var2 = var5[var3];
         if ((this.getState() & var2.toInt()) != 0) {
            var1.append(':');
            var1.append(var2);
         }
      }

      return var1.toString();
   }

   public static enum State {
      UNDEFINED("UNDEFINED", 0),
      ENABLED("ENABLED", 1),
      HOVER("HOVER", 2),
      PRESSED("PRESSED", 4),
      DISABLED("DISABLED", 8),
      FOCUSED("FOCUSED", 256),
      SELECTED("SELECTED", 512),
      DEFAULT("DEFAULT", 1024),
      LOCKED("LOCKED", 2048),
      ACTIVE("ACTIVE", 4096);

      private int state;
      private String name;
      private static Map states = new HashMap();

      static {
         State[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            State var0 = var3[var1];
            states.put(var0.name, var0);
         }

      }

      private State(String var3, int var4) {
         this.name = var3;
         this.state = var4;
      }

      public static State parse(String var0) throws ParseException {
         State var1 = (State)states.get(var0.toUpperCase());
         if (var1 == null) {
            throw new ParseException("Unsupported state: " + var0, -1);
         } else {
            return var1;
         }
      }

      public int toInt() {
         return this.state;
      }

      public String toString() {
         return this.name;
      }
   }
}
