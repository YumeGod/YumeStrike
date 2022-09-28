package org.apache.batik.util.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class JErrorPane extends JPanel implements ActionMap {
   protected static final String RESOURCES = "org.apache.batik.util.gui.resources.JErrorPane";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.JErrorPane", Locale.getDefault());
   protected static ResourceManager resources;
   protected String msg;
   protected String stacktrace;
   protected ButtonFactory bf;
   protected JComponent detailsArea;
   protected JButton showDetailButton;
   protected boolean isDetailShown;
   protected JPanel subpanel;
   protected Map listeners;

   public JErrorPane(Throwable var1, int var2) {
      super(new GridBagLayout());
      this.bf = new ButtonFactory(bundle, this);
      this.isDetailShown = false;
      this.listeners = new HashMap();
      this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      this.listeners.put("ShowDetailButtonAction", new ShowDetailButtonAction());
      this.listeners.put("OKButtonAction", new OKButtonAction());
      this.msg = bundle.getString("Heading.text") + "\n\n" + var1.getMessage();
      StringWriter var3 = new StringWriter();
      var1.printStackTrace(new PrintWriter(var3));
      var3.flush();
      this.stacktrace = var3.toString();
      ExtendedGridBagConstraints var4 = new ExtendedGridBagConstraints();
      JTextArea var5 = new JTextArea();
      var5.setText(this.msg);
      var5.setColumns(50);
      var5.setFont((new JLabel()).getFont());
      var5.setForeground((new JLabel()).getForeground());
      var5.setOpaque(false);
      var5.setEditable(false);
      var5.setLineWrap(true);
      var4.setWeight(0.0, 0.0);
      var4.anchor = 17;
      var4.fill = 0;
      var4.setGridBounds(0, 0, 1, 1);
      this.add(var5, var4);
      var4.setWeight(1.0, 0.0);
      var4.anchor = 10;
      var4.fill = 2;
      var4.setGridBounds(0, 1, 1, 1);
      this.add(this.createButtonsPanel(), var4);
      JTextArea var6 = new JTextArea();
      var5.setColumns(50);
      var6.setText(this.stacktrace);
      var6.setEditable(false);
      this.detailsArea = new JPanel(new BorderLayout(0, 10));
      this.detailsArea.add(new JSeparator(), "North");
      this.detailsArea.add(new JScrollPane(var6), "Center");
      this.subpanel = new JPanel(new BorderLayout());
      var4.insets = new Insets(10, 4, 4, 4);
      var4.setWeight(1.0, 1.0);
      var4.anchor = 10;
      var4.fill = 1;
      var4.setGridBounds(0, 2, 1, 1);
      this.add(this.subpanel, var4);
   }

   public JDialog createDialog(Component var1, String var2) {
      JDialog var3 = new JDialog(JOptionPane.getFrameForComponent(var1), var2);
      var3.getContentPane().add(this, "Center");
      var3.pack();
      return var3;
   }

   protected JPanel createButtonsPanel() {
      JPanel var1 = new JPanel(new FlowLayout(2));
      this.showDetailButton = this.bf.createJButton("ShowDetailButton");
      var1.add(this.showDetailButton);
      JButton var2 = this.bf.createJButton("OKButton");
      var1.add(var2);
      return var1;
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   static {
      resources = new ResourceManager(bundle);
   }

   protected class ShowDetailButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JErrorPane.this.isDetailShown) {
            JErrorPane.this.subpanel.remove(JErrorPane.this.detailsArea);
            JErrorPane.this.isDetailShown = false;
            JErrorPane.this.showDetailButton.setText(JErrorPane.resources.getString("ShowDetailButton.text"));
         } else {
            JErrorPane.this.subpanel.add(JErrorPane.this.detailsArea, "Center");
            JErrorPane.this.showDetailButton.setText(JErrorPane.resources.getString("ShowDetailButton.text2"));
            JErrorPane.this.isDetailShown = true;
         }

         ((JDialog)JErrorPane.this.getTopLevelAncestor()).pack();
      }
   }

   protected class OKButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         ((JDialog)JErrorPane.this.getTopLevelAncestor()).dispose();
      }
   }
}
