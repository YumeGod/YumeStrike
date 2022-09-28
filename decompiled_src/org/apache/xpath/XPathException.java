package org.apache.xpath;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

public class XPathException extends TransformerException {
   static final long serialVersionUID = 4263549717619045963L;
   Object m_styleNode = null;
   protected Exception m_exception;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   public Object getStylesheetNode() {
      return this.m_styleNode;
   }

   public void setStylesheetNode(Object styleNode) {
      this.m_styleNode = styleNode;
   }

   public XPathException(String message, ExpressionNode ex) {
      super(message);
      this.setLocator(ex);
      this.setStylesheetNode(this.getStylesheetNode(ex));
   }

   public XPathException(String message) {
      super(message);
   }

   public Node getStylesheetNode(ExpressionNode ex) {
      ExpressionNode owner = this.getExpressionOwner(ex);
      return null != owner && owner instanceof Node ? (Node)owner : null;
   }

   protected ExpressionNode getExpressionOwner(ExpressionNode ex) {
      ExpressionNode parent;
      for(parent = ex.exprGetParent(); null != parent && parent instanceof Expression; parent = parent.exprGetParent()) {
      }

      return parent;
   }

   public XPathException(String message, Object styleNode) {
      super(message);
      this.m_styleNode = styleNode;
   }

   public XPathException(String message, Node styleNode, Exception e) {
      super(message);
      this.m_styleNode = styleNode;
      this.m_exception = e;
   }

   public XPathException(String message, Exception e) {
      super(message);
      this.m_exception = e;
   }

   public void printStackTrace(PrintStream s) {
      if (s == null) {
         s = System.err;
      }

      try {
         super.printStackTrace(s);
      } catch (Exception var6) {
      }

      Throwable exception = this.m_exception;

      for(int i = 0; i < 10 && null != exception; ++i) {
         s.println("---------");
         ((Throwable)exception).printStackTrace(s);
         if (exception instanceof TransformerException) {
            TransformerException se = (TransformerException)exception;
            Throwable prev = exception;
            exception = se.getException();
            if (prev == exception) {
               break;
            }
         } else {
            exception = null;
         }
      }

   }

   public String getMessage() {
      String lastMessage = super.getMessage();
      Throwable exception = this.m_exception;

      while(null != exception) {
         String nextMessage = ((Throwable)exception).getMessage();
         if (null != nextMessage) {
            lastMessage = nextMessage;
         }

         if (exception instanceof TransformerException) {
            TransformerException se = (TransformerException)exception;
            Throwable prev = exception;
            exception = se.getException();
            if (prev == exception) {
               break;
            }
         } else {
            exception = null;
         }
      }

      return null != lastMessage ? lastMessage : "";
   }

   public void printStackTrace(PrintWriter s) {
      if (s == null) {
         s = new PrintWriter(System.err);
      }

      try {
         super.printStackTrace(s);
      } catch (Exception var9) {
      }

      boolean isJdk14OrHigher = false;

      try {
         (class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable).getMethod("getCause", (Class[])null);
         isJdk14OrHigher = true;
      } catch (NoSuchMethodException var8) {
      }

      if (!isJdk14OrHigher) {
         Throwable exception = this.m_exception;

         for(int i = 0; i < 10 && null != exception; ++i) {
            s.println("---------");

            try {
               ((Throwable)exception).printStackTrace(s);
            } catch (Exception var7) {
               s.println("Could not print stack trace...");
            }

            if (exception instanceof TransformerException) {
               TransformerException se = (TransformerException)exception;
               Throwable prev = exception;
               exception = se.getException();
               if (prev == exception) {
                  exception = null;
                  break;
               }
            } else {
               exception = null;
            }
         }
      }

   }

   public Throwable getException() {
      return this.m_exception;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
