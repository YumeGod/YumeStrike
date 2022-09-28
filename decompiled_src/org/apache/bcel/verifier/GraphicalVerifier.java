package org.apache.bcel.verifier;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.UIManager;
import org.apache.bcel.generic.Type;

public class GraphicalVerifier {
   boolean packFrame = false;

   public GraphicalVerifier() {
      VerifierAppFrame frame = new VerifierAppFrame();
      if (this.packFrame) {
         frame.pack();
      } else {
         frame.validate();
      }

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = frame.getSize();
      if (frameSize.height > screenSize.height) {
         frameSize.height = screenSize.height;
      }

      if (frameSize.width > screenSize.width) {
         frameSize.width = screenSize.width;
      }

      frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      frame.setVisible(true);
      frame.classNamesJList.setModel(new VerifierFactoryListModel());
      VerifierFactory.getVerifier(Type.OBJECT.getClassName());
      frame.classNamesJList.setSelectedIndex(0);
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      new GraphicalVerifier();
   }
}
