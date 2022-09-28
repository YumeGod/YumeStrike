package org.apache.batik.anim;

import org.apache.batik.anim.timing.TimedElement;

public class AnimationException extends RuntimeException {
   protected TimedElement e;
   protected String code;
   protected Object[] params;
   protected String message;

   public AnimationException(TimedElement var1, String var2, Object[] var3) {
      this.e = var1;
      this.code = var2;
      this.params = var3;
   }

   public TimedElement getElement() {
      return this.e;
   }

   public String getCode() {
      return this.code;
   }

   public Object[] getParams() {
      return this.params;
   }

   public String getMessage() {
      return TimedElement.formatMessage(this.code, this.params);
   }
}
