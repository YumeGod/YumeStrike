package de.javasoft.plaf.synthetica.filechooser;

import de.javasoft.io.FileOperationEvent;
import de.javasoft.io.FileOperationListener;
import de.javasoft.io.FileProperties;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.painter.ImagePainter;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class FilePropertiesDialog extends JDialog implements FileOperationListener, Runnable {
   private boolean abort;
   private long counter = 0L;
   private JLabel location;
   private JLabel files;
   private JLabel size;
   private JLabel date;
   private FileProperties props;
   private Color brightLabelColor;
   private Color labelColor;
   private static final int xGap = 10;
   private static final int yGap = 10;
   String title = "";
   String imagePath = "";
   Insets imageInsets;

   public FilePropertiesDialog(Dialog var1) {
      super(var1);
      this.init(var1);
   }

   public FilePropertiesDialog(Frame var1) {
      super(var1);
      this.init(var1);
   }

   private void init(Window var1) {
      Image var2 = null;
      this.title = UIManager.getString("FilePropertiesDialog.title");
      this.imagePath = UIManager.getString("Synthetica.filePropertiesDialog.title.background");
      this.imageInsets = SyntheticaLookAndFeel.getInsets("Synthetica.filePropertiesDialog.title.background.insets", (Component)null, new Insets(0, 0, 4, 100));
      var2 = (Image)UIManager.get("Synthetica.filePropertiesDialog.iconImage");
      this.setTitle(this.title);
      this.setLayout(new BorderLayout());
      this.getRootPane().putClientProperty("Synthetica.dialog.iconImage", var2);
      JPanel var3 = new JPanel() {
         private Image background;

         {
            this.background = (new ImageIcon(SyntheticaLookAndFeel.class.getResource(FilePropertiesDialog.this.imagePath))).getImage();
         }

         public void paintComponent(Graphics var1) {
            ImagePainter var2 = new ImagePainter(this.background, var1, 0, 0, this.getWidth(), this.background.getHeight((ImageObserver)null), FilePropertiesDialog.this.imageInsets, FilePropertiesDialog.this.imageInsets);
            var2.draw();
            Graphics2D var3 = (Graphics2D)var1;
            var3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int var4 = UIManager.getInt("Synthetica.filePropertiesDialog.title.xPos");
            int var5 = UIManager.getInt("Synthetica.filePropertiesDialog.title.yPos");
            var3.drawString(FilePropertiesDialog.this.title, var4, var5);
            var3.dispose();
         }

         public Dimension getPreferredSize() {
            return new Dimension(this.background.getWidth((ImageObserver)null), this.background.getHeight((ImageObserver)null));
         }
      };
      float var4 = SyntheticaLookAndFeel.scaleFontSize((float)UIManager.getInt("Synthetica.filePropertiesDialog.title.size"));
      var3.setFont(var3.getFont().deriveFont(1, var4));
      var3.setForeground(UIManager.getColor("Synthetica.filePropertiesDialog.title.color"));
      JPanel var5 = new JPanel(new GridBagLayout());
      var5.setBorder(new EmptyBorder(10, 10, 10, 10));
      GridBagConstraints var6 = new GridBagConstraints();
      var6.anchor = 17;
      var6.weightx = 0.0;
      var6.weighty = 0.0;
      var6.gridwidth = 2;
      var6.gridheight = 1;
      var6.gridx = 0;
      var6.gridy = 0;
      JPanel var7 = new JPanel(new BorderLayout());
      var5.add(var7, var6);
      var6.insets = new Insets(0, 0, 10, 0);
      var6.gridwidth = 2;
      var6.gridx = 0;
      var6.gridy = 1;
      var6.weightx = 1.0;
      var6.fill = 2;
      this.files = new JLabel("!");
      this.labelColor = this.files.getForeground();
      this.brightLabelColor = new Color(this.labelColor.getRGB() + 6316128);
      this.files.setForeground(this.brightLabelColor);
      var5.add(this.files, var6);
      var6.insets = new Insets(0, 0, 2, 10);
      var6.gridwidth = 1;
      var6.gridx = 0;
      var6.gridy = 2;
      var6.weightx = 0.0;
      var6.fill = 0;
      JLabel var8 = new JLabel(UIManager.getString("FilePropertiesDialog.location"));
      var5.add(var8, var6);
      var6.insets = new Insets(0, 0, 2, 0);
      var6.gridx = 1;
      var6.weightx = 1.0;
      var6.fill = 2;
      this.location = new JLabel();
      var5.add(this.location, var6);
      var6.insets = new Insets(0, 0, 2, 10);
      var6.gridwidth = 1;
      var6.gridx = 0;
      var6.gridy = 3;
      var6.weightx = 0.0;
      var6.fill = 0;
      JLabel var9 = new JLabel(UIManager.getString("FilePropertiesDialog.size"));
      var5.add(var9, var6);
      var6.insets = new Insets(0, 0, 2, 0);
      var6.gridx = 1;
      var6.weightx = 1.0;
      var6.fill = 2;
      this.size = new JLabel();
      this.size.setForeground(this.brightLabelColor);
      var5.add(this.size, var6);
      var6.insets = new Insets(0, 0, 20, 10);
      var6.gridx = 0;
      var6.gridy = 4;
      var6.weightx = 0.0;
      var6.fill = 0;
      JLabel var10 = new JLabel(UIManager.getString("FilePropertiesDialog.date"));
      var5.add(var10, var6);
      var6.insets = new Insets(0, 0, 20, 0);
      var6.gridx = 1;
      var6.weightx = 1.0;
      var6.fill = 2;
      this.date = new JLabel();
      var5.add(this.date, var6);
      var6.anchor = var1.getComponentOrientation().isLeftToRight() ? 13 : 17;
      var6.insets = new Insets(0, 0, 0, 0);
      var6.gridwidth = 2;
      var6.gridx = 0;
      var6.gridy = 5;
      var6.weightx = 0.0;
      var6.fill = 0;
      JButton var11 = new JButton(UIManager.getString("FilePropertiesDialog.ok"));
      var11.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FilePropertiesDialog.this.abort = true;
            FilePropertiesDialog.this.dispose();
         }
      });
      var11.setMinimumSize(new Dimension(var11.getPreferredSize().width * 2, var11.getPreferredSize().height));
      var11.setPreferredSize(var11.getMinimumSize());
      var5.add(var11, var6);
      this.add(var3, "North");
      this.add(var5);
      this.pack();
      int var12 = var1.getLocation().x + var1.getSize().width / 2;
      int var13 = var12 - this.getSize().width / 2;
      int var14 = var1.getLocation().y + var1.getSize().height / 2;
      int var15 = var14 - this.getSize().height / 2;
      this.setLocation(var13, var15);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            FilePropertiesDialog.this.abort = true;
         }
      });
   }

   public boolean processFileOperationEvent(FileOperationEvent var1) {
      ++this.counter;
      this.props = (FileProperties)var1.getSource();
      if (this.counter % 100L == 0L || this.counter < 10L) {
         EventQueue.invokeLater(this);
      }

      return !this.abort;
   }

   public void run() {
      NumberFormat var1 = NumberFormat.getInstance();
      String var2 = UIManager.getString("FilePropertiesDialog.filesFormat");
      String var3 = var1.format(this.props.directories);
      String var4 = var1.format(this.props.files);
      this.files.setText(MessageFormat.format(var2, var4, var3));
      String var5 = UIManager.getString("FilePropertiesDialog.sizeFormat");
      String var6 = var1.format(this.props.size);
      var1 = NumberFormat.getInstance();
      var1.setMinimumFractionDigits(2);
      var1.setMaximumFractionDigits(2);
      String var7 = var1.format((double)this.props.size / 1024.0 / 1024.0);
      this.size.setText(MessageFormat.format(var5, var6, var7));
      this.location.setText(this.props.location);
      if (this.props.directories + this.props.files == 1L) {
         SimpleDateFormat var8 = new SimpleDateFormat(UIManager.getString("FilePropertiesDialog.dateFormat"));
         this.date.setText(var8.format(new Date(this.props.lastModified)));
      } else {
         this.date.setText("---");
      }

   }

   public void refresh() {
      this.counter = 0L;
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            FilePropertiesDialog.this.files.setForeground(FilePropertiesDialog.this.labelColor);
            FilePropertiesDialog.this.size.setForeground(FilePropertiesDialog.this.labelColor);
         }
      });
      EventQueue.invokeLater(this);
   }
}
