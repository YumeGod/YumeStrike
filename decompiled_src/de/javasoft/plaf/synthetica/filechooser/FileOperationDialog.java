package de.javasoft.plaf.synthetica.filechooser;

import de.javasoft.io.FileOperationEvent;
import de.javasoft.io.FileOperationListener;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.painter.ImagePainter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class FileOperationDialog extends JDialog implements FileOperationListener, Runnable {
   public static final int COPY_OPERATION = 1;
   public static final int MOVE_OPERATION = 2;
   public static final int DELETE_OPERATION = 3;
   private boolean abort;
   private JLabel folderName;
   private JLabel fileName;
   private String currentFolderName;
   private String currentFileName;
   private JProgressBar progressBar;
   private static final int xGap = 10;
   private static final int yGap = 10;
   String title = "";
   String imagePath = "";
   Insets imageInsets;

   public FileOperationDialog(Dialog var1, int var2) {
      super(var1);
      this.init(var1, var2);
   }

   public FileOperationDialog(Frame var1, int var2) {
      super(var1);
      this.init(var1, var2);
   }

   private void init(Window var1, int var2) {
      Image var3 = null;
      switch (var2) {
         case 1:
            this.title = UIManager.getString("FileOperationDialog.copy.title");
            this.imagePath = UIManager.getString("Synthetica.fileOperationDialog.title.copyBackground");
            this.imageInsets = SyntheticaLookAndFeel.getInsets("Synthetica.fileOperationDialog.title.copyBackground.insets", (Component)null, new Insets(0, 0, 4, 100));
            var3 = (Image)UIManager.get("Synthetica.fileOperationDialog.copy.iconImage");
            break;
         case 2:
            this.title = UIManager.getString("FileOperationDialog.move.title");
            this.imagePath = UIManager.getString("Synthetica.fileOperationDialog.title.moveBackground");
            this.imageInsets = SyntheticaLookAndFeel.getInsets("Synthetica.fileOperationDialog.title.moveBackground.insets", (Component)null, new Insets(0, 0, 4, 100));
            var3 = (Image)UIManager.get("Synthetica.fileOperationDialog.move.iconImage");
            break;
         case 3:
            this.title = UIManager.getString("FileOperationDialog.delete.title");
            this.imagePath = UIManager.getString("Synthetica.fileOperationDialog.title.deleteBackground");
            this.imageInsets = SyntheticaLookAndFeel.getInsets("Synthetica.fileOperationDialog.title.deleteBackground.insets", (Component)null, new Insets(0, 0, 4, 100));
            var3 = (Image)UIManager.get("Synthetica.fileOperationDialog.delete.iconImage");
      }

      this.setTitle(this.title);
      this.setLayout(new BorderLayout());
      this.getRootPane().putClientProperty("Synthetica.dialog.iconImage", var3);
      JPanel var4 = new JPanel() {
         private Image background;

         {
            this.background = (new ImageIcon(SyntheticaLookAndFeel.class.getResource(FileOperationDialog.this.imagePath))).getImage();
         }

         public void paintComponent(Graphics var1) {
            ImagePainter var2 = new ImagePainter(this.background, var1, 0, 0, this.getWidth(), this.background.getHeight((ImageObserver)null), FileOperationDialog.this.imageInsets, FileOperationDialog.this.imageInsets);
            var2.draw();
            Graphics2D var3 = (Graphics2D)var1;
            var3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int var4 = UIManager.getInt("Synthetica.fileOperationDialog.title.xPos");
            int var5 = UIManager.getInt("Synthetica.fileOperationDialog.title.yPos");
            var3.drawString(FileOperationDialog.this.title, var4, var5);
            var3.dispose();
         }

         public Dimension getPreferredSize() {
            return new Dimension(this.background.getWidth((ImageObserver)null), this.background.getHeight((ImageObserver)null));
         }
      };
      float var5 = SyntheticaLookAndFeel.scaleFontSize((float)UIManager.getInt("Synthetica.fileOperationDialog.title.size"));
      var4.setFont(var4.getFont().deriveFont(1, var5));
      var4.setForeground(UIManager.getColor("Synthetica.fileOperationDialog.title.color"));
      JPanel var6 = new JPanel(new GridBagLayout());
      var6.setBorder(new EmptyBorder(10, 10, 10, 10));
      GridBagConstraints var7 = new GridBagConstraints();
      var7.anchor = 17;
      var7.weightx = 0.0;
      var7.weighty = 0.0;
      var7.gridwidth = 2;
      var7.gridheight = 1;
      var7.gridx = 0;
      var7.gridy = 0;
      JPanel var8 = new JPanel(new BorderLayout());
      var6.add(var8, var7);
      var7.insets = new Insets(0, 0, 2, 10);
      var7.gridwidth = 1;
      var7.gridx = 0;
      var7.gridy = 1;
      var7.weightx = 0.0;
      var7.fill = 0;
      JLabel var9 = new JLabel(UIManager.getString("FileOperationDialog.folder"));
      var6.add(var9, var7);
      var7.insets = new Insets(0, 0, 2, 0);
      var7.gridx = 1;
      var7.weightx = 1.0;
      var7.fill = 2;
      this.folderName = new JLabel();
      var6.add(this.folderName, var7);
      var7.insets = new Insets(0, 0, 10, 10);
      var7.gridx = 0;
      var7.gridy = 2;
      var7.weightx = 0.0;
      var7.fill = 0;
      JLabel var10 = new JLabel(UIManager.getString("FileOperationDialog.file"));
      var6.add(var10, var7);
      var7.insets = new Insets(0, 0, 10, 0);
      var7.gridx = 1;
      var7.weightx = 1.0;
      var7.fill = 2;
      this.fileName = new JLabel();
      var6.add(this.fileName, var7);
      var7.insets = new Insets(0, 0, 20, 0);
      var7.gridwidth = 2;
      var7.gridx = 0;
      var7.gridy = 3;
      this.progressBar = new JProgressBar(0, 50);
      var6.add(this.progressBar, var7);
      var7.anchor = var1.getComponentOrientation().isLeftToRight() ? 13 : 17;
      var7.insets = new Insets(0, 0, 0, 0);
      var7.gridx = 0;
      var7.gridy = 4;
      var7.weightx = 0.0;
      var7.fill = 0;
      JButton var11 = new JButton(UIManager.getString("FileOperationDialog.cancel"));
      var11.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FileOperationDialog.this.abort = true;
            FileOperationDialog.this.dispose();
         }
      });
      var6.add(var11, var7);
      this.add(var4, "North");
      this.add(var6);
      this.pack();
      int var12 = var1.getLocation().x + var1.getSize().width / 2;
      int var13 = var12 - this.getSize().width / 2;
      int var14 = var1.getLocation().y + var1.getSize().height / 2;
      int var15 = var14 - this.getSize().height / 2;
      this.setLocation(var13, var15);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            FileOperationDialog.this.abort = true;
         }
      });
   }

   public boolean processFileOperationEvent(FileOperationEvent var1) {
      this.currentFolderName = var1.getFile().getParentFile().getName();
      this.currentFileName = var1.getFile().getName();
      EventQueue.invokeLater(this);
      return !this.abort;
   }

   public void run() {
      this.folderName.setText(this.currentFolderName);
      this.fileName.setText(this.currentFileName);
      this.progressBar.setValue((this.progressBar.getValue() + 1) % 50);
   }
}
