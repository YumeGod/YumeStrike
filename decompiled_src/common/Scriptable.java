package common;

import java.util.Stack;

public interface Scriptable {
   String eventName();

   Stack eventArguments();
}
