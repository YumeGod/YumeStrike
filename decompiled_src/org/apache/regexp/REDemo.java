package org.apache.regexp;

import java.applet.Applet;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.swing.JFrame;

public class REDemo extends Applet implements TextListener {
   RE r = new RE();
   REDebugCompiler compiler = new REDebugCompiler();
   TextField fieldRE;
   TextField fieldMatch;
   TextArea outRE;
   TextArea outMatch;

   public void init() {
      GridBagLayout var1 = new GridBagLayout();
      this.setLayout(var1);
      GridBagConstraints var2 = new GridBagConstraints();
      var2.insets = new Insets(5, 5, 5, 5);
      var2.anchor = 13;
      var1.setConstraints(this.add(new Label("Regular expression:", 2)), var2);
      var2.gridy = 0;
      var2.anchor = 17;
      var1.setConstraints(this.add(this.fieldRE = new TextField("\\[([:javastart:][:javapart:]*)\\]", 40)), var2);
      var2.gridx = 0;
      var2.gridy = -1;
      var2.anchor = 13;
      var1.setConstraints(this.add(new Label("String:", 2)), var2);
      var2.gridy = 1;
      var2.gridx = -1;
      var2.anchor = 17;
      var1.setConstraints(this.add(this.fieldMatch = new TextField("aaa([foo])aaa", 40)), var2);
      var2.gridy = 2;
      var2.gridx = -1;
      var2.fill = 1;
      var2.weighty = 1.0;
      var2.weightx = 1.0;
      var1.setConstraints(this.add(this.outRE = new TextArea()), var2);
      var2.gridy = 2;
      var2.gridx = -1;
      var1.setConstraints(this.add(this.outMatch = new TextArea()), var2);
      this.fieldRE.addTextListener(this);
      this.fieldMatch.addTextListener(this);
      this.textValueChanged((TextEvent)null);
   }

   public static void main(String[] var0) {
      JFrame var1 = new JFrame("RE Demo");
      var1.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            System.exit(0);
         }
      });
      Container var2 = var1.getContentPane();
      var2.setLayout(new FlowLayout());
      REDemo var3 = new REDemo();
      var2.add(var3);
      var3.init();
      var1.pack();
      var1.setVisible(true);
   }

   void sayMatch(String var1) {
      this.outMatch.setText(var1);
   }

   void sayRE(String var1) {
      this.outRE.setText(var1);
   }

   public void textValueChanged(TextEvent var1) {
      if (var1 == null || var1.getSource() == this.fieldRE) {
         this.updateRE(this.fieldRE.getText());
      }

      this.updateMatch(this.fieldMatch.getText());
   }

   String throwableToString(Throwable var1) {
      String var2 = var1.getClass().getName();
      String var3;
      if ((var3 = var1.getMessage()) != null) {
         var2 = var2 + "\n" + var3;
      }

      return var2;
   }

   void updateMatch(String var1) {
      try {
         if (this.r.match(var1)) {
            String var2 = "Matches.\n\n";

            for(int var3 = 0; var3 < this.r.getParenCount(); ++var3) {
               var2 = var2 + "$" + var3 + " = " + this.r.getParen(var3) + "\n";
            }

            this.sayMatch(var2);
         } else {
            this.sayMatch("Does not match");
         }
      } catch (Throwable var4) {
         this.sayMatch(this.throwableToString(var4));
      }

   }

   void updateRE(String var1) {
      try {
         this.r.setProgram(this.compiler.compile(var1));
         CharArrayWriter var2 = new CharArrayWriter();
         this.compiler.dumpProgram(new PrintWriter(var2));
         this.sayRE(var2.toString());
         System.out.println(var2);
      } catch (Exception var3) {
         this.r.setProgram((REProgram)null);
         this.sayRE(this.throwableToString(var3));
      } catch (Throwable var4) {
         this.r.setProgram((REProgram)null);
         this.sayRE(this.throwableToString(var4));
      }

   }
}
