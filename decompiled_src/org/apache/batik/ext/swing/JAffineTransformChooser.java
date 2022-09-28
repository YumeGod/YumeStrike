package org.apache.batik.ext.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

public class JAffineTransformChooser extends JGridBagPanel {
   public static final String LABEL_ANGLE = "JAffineTransformChooser.label.angle";
   public static final String LABEL_DEGREE = "JAffineTransformChooser.label.degree";
   public static final String LABEL_PERCENT = "JAffineTransformChooser.label.percent";
   public static final String LABEL_ROTATE = "JAffineTransformChooser.label.rotate";
   public static final String LABEL_SCALE = "JAffineTransformChooser.label.scale";
   public static final String LABEL_RX = "JAffineTransformChooser.label.rx";
   public static final String LABEL_RY = "JAffineTransformChooser.label.ry";
   public static final String LABEL_SX = "JAffineTransformChooser.label.sx";
   public static final String LABEL_SY = "JAffineTransformChooser.label.sy";
   public static final String LABEL_TRANSLATE = "JAffineTransformChooser.label.translate";
   public static final String LABEL_TX = "JAffineTransformChooser.label.tx";
   public static final String LABEL_TY = "JAffineTransformChooser.label.ty";
   public static final String CONFIG_TEXT_FIELD_WIDTH = "JAffineTransformChooser.config.text.field.width";
   public static final String CONFIG_TOP_PAD = "JAffineTransformChooser.config.top.pad";
   public static final String CONFIG_LEFT_PAD = "JAffineTransformChooser.config.left.pad";
   public static final String CONFIG_BOTTOM_PAD = "JAffineTransformChooser.config.bottom.pad";
   public static final String CONFIG_RIGHT_PAD = "JAffineTransformChooser.config.right.pad";
   protected AffineTransform txf;
   protected DoubleDocument txModel = new DoubleDocument();
   protected DoubleDocument tyModel = new DoubleDocument();
   protected DoubleDocument sxModel = new DoubleDocument();
   protected DoubleDocument syModel = new DoubleDocument();
   protected DoubleDocument rxModel = new DoubleDocument();
   protected DoubleDocument ryModel = new DoubleDocument();
   protected DoubleDocument rotateModel = new DoubleDocument();
   protected static final double RAD_TO_DEG = 57.29577951308232;
   protected static final double DEG_TO_RAD = 0.017453292519943295;

   public JAffineTransformChooser() {
      this.build();
      this.setAffineTransform(new AffineTransform());
   }

   protected void build() {
      Component var1 = this.buildPanel(Resources.getString("JAffineTransformChooser.label.translate"), Resources.getString("JAffineTransformChooser.label.tx"), this.txModel, Resources.getString("JAffineTransformChooser.label.ty"), this.tyModel, "", "", true);
      Component var2 = this.buildPanel(Resources.getString("JAffineTransformChooser.label.scale"), Resources.getString("JAffineTransformChooser.label.sx"), this.sxModel, Resources.getString("JAffineTransformChooser.label.sy"), this.syModel, Resources.getString("JAffineTransformChooser.label.percent"), Resources.getString("JAffineTransformChooser.label.percent"), true);
      Component var3 = this.buildRotatePanel();
      this.add(var1, 0, 0, 1, 1, 10, 1, 1.0, 1.0);
      this.add(var2, 1, 0, 1, 1, 10, 1, 1.0, 1.0);
      this.add(var3, 0, 1, 2, 1, 10, 1, 1.0, 1.0);
   }

   protected Component buildRotatePanel() {
      JGridBagPanel var1 = new JGridBagPanel();
      Component var2 = this.buildPanel(Resources.getString("JAffineTransformChooser.label.rotate"), Resources.getString("JAffineTransformChooser.label.angle"), this.rotateModel, (String)null, (Document)null, Resources.getString("JAffineTransformChooser.label.degree"), (String)null, false);
      Component var3 = this.buildPanel("", Resources.getString("JAffineTransformChooser.label.rx"), this.rxModel, Resources.getString("JAffineTransformChooser.label.ry"), this.ryModel, (String)null, (String)null, false);
      var1.add(var2, 0, 0, 1, 1, 10, 1, 1.0, 1.0);
      var1.add(var3, 1, 0, 1, 1, 10, 1, 1.0, 1.0);
      this.setPanelBorder(var1, Resources.getString("JAffineTransformChooser.label.rotate"));
      return var1;
   }

   protected Component buildPanel(String var1, String var2, Document var3, String var4, Document var5, String var6, String var7, boolean var8) {
      JGridBagPanel var9 = new JGridBagPanel();
      this.addToPanelAtRow(var2, var3, var6, var9, 0);
      if (var4 != null) {
         this.addToPanelAtRow(var4, var5, var7, var9, 1);
      }

      if (var8) {
         this.setPanelBorder(var9, var1);
      }

      return var9;
   }

   public void setPanelBorder(JComponent var1, String var2) {
      TitledBorder var3 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), var2);
      int var4 = Resources.getInteger("JAffineTransformChooser.config.top.pad");
      int var5 = Resources.getInteger("JAffineTransformChooser.config.left.pad");
      int var6 = Resources.getInteger("JAffineTransformChooser.config.bottom.pad");
      int var7 = Resources.getInteger("JAffineTransformChooser.config.right.pad");
      CompoundBorder var8 = BorderFactory.createCompoundBorder(var3, BorderFactory.createEmptyBorder(var4, var5, var6, var7));
      var1.setBorder(var8);
   }

   protected void addToPanelAtRow(String var1, Document var2, String var3, JGridBagPanel var4, int var5) {
      JTextField var6 = new JTextField(Resources.getInteger("JAffineTransformChooser.config.text.field.width"));
      var6.setDocument(var2);
      var4.add(new JLabel(var1), 0, var5, 1, 1, 17, 2, 0.0, 0.0);
      var4.add(var6, 1, var5, 1, 1, 10, 2, 1.0, 0.0);
      var4.add(new JLabel(var3), 2, var5, 1, 1, 17, 2, 0.0, 0.0);
   }

   public AffineTransform getAffineTransform() {
      double var1 = this.sxModel.getValue() / 100.0;
      double var3 = this.syModel.getValue() / 100.0;
      double var5 = this.rotateModel.getValue() * 0.017453292519943295;
      double var7 = this.rxModel.getValue();
      double var9 = this.ryModel.getValue();
      double var11 = this.txModel.getValue();
      double var13 = this.tyModel.getValue();
      double[] var15 = new double[6];
      double var16 = Math.sin(var5);
      double var18 = Math.cos(var5);
      var15[0] = var1 * var18;
      var15[1] = var1 * var16;
      var15[2] = -var3 * var16;
      var15[3] = var3 * var18;
      var15[4] = var11 + var7 - var7 * var18 + var9 * var16;
      var15[5] = var13 + var9 - var7 * var16 - var9 * var18;
      this.txf = new AffineTransform(var15);
      return this.txf;
   }

   public void setAffineTransform(AffineTransform var1) {
      if (var1 == null) {
         var1 = new AffineTransform();
      }

      this.txf = var1;
      double[] var2 = new double[6];
      var1.getMatrix(var2);
      this.txModel.setValue(var2[4]);
      this.tyModel.setValue(var2[5]);
      double var3 = Math.sqrt(var2[0] * var2[0] + var2[1] * var2[1]);
      double var5 = Math.sqrt(var2[2] * var2[2] + var2[3] * var2[3]);
      this.sxModel.setValue(100.0 * var3);
      this.syModel.setValue(100.0 * var5);
      double var7 = 0.0;
      if (var2[0] > 0.0) {
         var7 = Math.atan2(var2[1], var2[0]);
      }

      this.rotateModel.setValue(57.29577951308232 * var7);
      this.rxModel.setValue(0.0);
      this.ryModel.setValue(0.0);
   }

   public static AffineTransform showDialog(Component var0, String var1) {
      JAffineTransformChooser var2 = new JAffineTransformChooser();
      AffineTransformTracker var3 = new AffineTransformTracker(var2);
      Dialog var4 = new Dialog(var0, var1, true, var2, var3, (ActionListener)null);
      var4.addWindowListener(new Closer());
      var4.addComponentListener(new DisposeOnClose());
      var4.setVisible(true);
      return var3.getAffineTransform();
   }

   public static Dialog createDialog(Component var0, String var1) {
      JAffineTransformChooser var2 = new JAffineTransformChooser();
      AffineTransformTracker var3 = new AffineTransformTracker(var2);
      Dialog var4 = new Dialog(var0, var1, true, var2, var3, (ActionListener)null);
      var4.addWindowListener(new Closer());
      var4.addComponentListener(new DisposeOnClose());
      return var4;
   }

   public static void main(String[] var0) {
      AffineTransform var1 = showDialog((Component)null, "Hello");
      if (var1 == null) {
         System.out.println("Cancelled");
      } else {
         System.out.println("t = " + var1);
      }

   }

   static class DisposeOnClose extends ComponentAdapter implements Serializable {
      public void componentHidden(ComponentEvent var1) {
         Window var2 = (Window)var1.getComponent();
         var2.dispose();
      }
   }

   static class Closer extends WindowAdapter implements Serializable {
      public void windowClosing(WindowEvent var1) {
         Window var2 = var1.getWindow();
         var2.setVisible(false);
      }
   }

   public static class Dialog extends JDialog {
      private JAffineTransformChooser chooserPane;
      private AffineTransformTracker tracker;
      public static final String LABEL_OK = "JAffineTransformChooser.label.ok";
      public static final String LABEL_CANCEL = "JAffineTransformChooser.label.cancel";
      public static final String LABEL_RESET = "JAffineTransformChooser.label.reset";
      public static final String ACTION_COMMAND_OK = "OK";
      public static final String ACTION_COMMAND_CANCEL = "cancel";

      public Dialog(Component var1, String var2, boolean var3, JAffineTransformChooser var4, AffineTransformTracker var5, ActionListener var6) {
         super(JOptionPane.getFrameForComponent(var1), var2, var3);
         this.chooserPane = var4;
         this.tracker = var5;
         String var7 = Resources.getString("JAffineTransformChooser.label.ok");
         String var8 = Resources.getString("JAffineTransformChooser.label.cancel");
         String var9 = Resources.getString("JAffineTransformChooser.label.reset");
         Container var10 = this.getContentPane();
         var10.setLayout(new BorderLayout());
         var10.add(var4, "Center");
         JPanel var11 = new JPanel();
         var11.setLayout(new FlowLayout(1));
         JButton var12 = new JButton(var7);
         this.getRootPane().setDefaultButton(var12);
         var12.setActionCommand("OK");
         if (var5 != null) {
            var12.addActionListener(var5);
         }

         var12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Dialog.this.setVisible(false);
            }
         });
         var11.add(var12);
         JButton var13 = new JButton(var8);
         this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent var1) {
               if (var1.getKeyCode() == 27) {
                  Dialog.this.setVisible(false);
               }

            }
         });
         var13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Dialog.this.setVisible(false);
            }
         });
         var11.add(var13);
         JButton var14 = new JButton(var9);
         var14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               Dialog.this.reset();
            }
         });
         var11.add(var14);
         var10.add(var11, "South");
         this.pack();
         this.setLocationRelativeTo(var1);
      }

      public void setVisible(boolean var1) {
         if (var1) {
            this.tracker.reset();
         }

         super.setVisible(var1);
      }

      public AffineTransform showDialog() {
         this.setVisible(true);
         return this.tracker.getAffineTransform();
      }

      public void reset() {
         this.chooserPane.setAffineTransform(new AffineTransform());
      }

      public void setTransform(AffineTransform var1) {
         if (var1 == null) {
            var1 = new AffineTransform();
         }

         this.chooserPane.setAffineTransform(var1);
      }
   }
}
