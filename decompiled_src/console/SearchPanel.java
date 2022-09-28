package console;

import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class SearchPanel extends JPanel implements ActionListener {
   protected JTextField search = null;
   protected JLabel status = null;
   protected JTextComponent component = null;
   protected int index = 0;
   protected Color highlight = null;

   public void actionPerformed(ActionEvent var1) {
      if (var1.getActionCommand().equals(">")) {
         ++this.index;
         this.scrollToIndex();
      } else if (var1.getActionCommand().equals("<")) {
         --this.index;
         this.scrollToIndex();
      } else {
         this.searchBuffer();
         this.scrollToIndex();
      }

   }

   private void scrollToIndex() {
      Highlighter.Highlight[] var1 = this.component.getHighlighter().getHighlights();
      if (var1.length == 0) {
         if (this.search.getText().trim().length() > 0) {
            this.status.setText("Phrase not found");
         }

      } else {
         try {
            if (this.index < 0) {
               this.index = var1.length - 1 - this.index;
            }

            int var2 = this.index % var1.length;
            this.status.setText(var2 + 1 + " of " + var1.length);
            int var3 = var1[var2].getStartOffset();
            Rectangle var4 = this.component.modelToView(var3);
            this.component.scrollRectToVisible(var4);
         } catch (BadLocationException var5) {
         }

      }
   }

   private void searchBuffer() {
      this.clear();
      String var1 = this.search.getText().toLowerCase().trim();
      if (var1.length() != 0) {
         DefaultHighlighter.DefaultHighlightPainter var2 = new DefaultHighlighter.DefaultHighlightPainter(this.highlight);

         try {
            String var3 = this.component.getText().toLowerCase();
            if ((System.getProperty("os.name") + "").indexOf("Windows") != -1) {
               var3 = var3.replaceAll("\r\n", "\n");
            }

            int var4 = -1;

            while((var4 = var3.indexOf(var1, var4 + 1)) != -1) {
               this.component.getHighlighter().addHighlight(var4, var4 + var1.length(), var2);
            }
         } catch (Exception var5) {
         }

      }
   }

   public void requestFocus() {
      this.search.requestFocus();
   }

   public void clear() {
      this.component.getHighlighter().removeAllHighlights();
      this.index = 0;
      this.status.setText("");
   }

   public SearchPanel(JTextComponent var1, Color var2) {
      this.component = var1;
      this.highlight = var2;
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(1, 1, 1, 1));
      JButton var3 = new JButton("<");
      var3.setActionCommand("<");
      JButton var4 = new JButton(">");
      var4.setActionCommand(">");
      DialogUtils.removeBorderFromButton(var3);
      DialogUtils.removeBorderFromButton(var4);
      var3.addActionListener(this);
      var4.addActionListener(this);
      JPanel var5 = new JPanel();
      var5.setLayout(new GridLayout(1, 2));
      var5.add(var3);
      var5.add(var4);
      this.search = new JTextField(15);
      this.search.addActionListener(this);
      JPanel var6 = new JPanel();
      var6.setLayout(new FlowLayout());
      var6.add(new JLabel("Find: "));
      var6.add(this.search);
      var6.add(var5);
      this.add(var6, "West");
      this.status = new JLabel("");
      this.add(this.status, "Center");
   }
}
