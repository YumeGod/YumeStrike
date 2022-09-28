package common;

import java.io.Serializable;
import java.util.Stack;

public class TranscriptReset implements Serializable, Scriptable {
   public String eventName() {
      return "data_reset";
   }

   public Stack eventArguments() {
      return new Stack();
   }
}
