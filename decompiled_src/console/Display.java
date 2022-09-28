package console;

import aggressor.Prefs;
import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import ui.CopyPopup;

public class Display extends JPanel {
   protected JTextPane console;
   protected Properties display;
   protected Font consoleFont;
   protected Colors colors;
   protected LinkedList components;

   private void updateComponentLooks() {
      this.colors = new Colors(this.display);
      Color var1 = Prefs.getPreferences().getColor("console.foreground.color", "#ffffff");
      Color var2 = Prefs.getPreferences().getColor("console.background.color", "#000000");
      Iterator var3 = this.components.iterator();

      while(var3.hasNext()) {
         JComponent var4 = (JComponent)var3.next();
         if (var4 == this.console) {
            var4.setOpaque(false);
         } else {
            var4.setBackground(var2);
         }

         var4.setForeground(var1);
         var4.setFont(this.consoleFont);
         if (var4 == this.console) {
            var4.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
         } else {
            var4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
         }

         if (var4 instanceof JTextComponent) {
            JTextComponent var5 = (JTextComponent)var4;
            var5.setCaretColor(var1.brighter());
         }
      }

   }

   public void append(final String var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            Display.this._append(var1);
         }
      });
   }

   public void _append(String var1) {
      Rectangle var2 = this.console.getVisibleRect();
      this.colors.append(this.console, var1);
      this.console.scrollRectToVisible(var2);
   }

   public void setText(final String var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            Display.this.console.setText(var1);
         }
      });
   }

   public void setTextDirect(String var1) {
      this.console.setText(var1);
   }

   public Display() {
      this(new Properties());
   }

   public Display(Properties var1) {
      this.components = new LinkedList();
      this.display = var1;
      this.consoleFont = Prefs.getPreferences().getFont("console.font.font", "Monospaced BOLD 14");
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(2, 2, 2, 2));
      this.console = new JTextPane();
      this.console.setEditable(false);
      this.console.setCaret(new DefaultCaret() {
         public void setSelectionVisible(boolean var1) {
            super.setSelectionVisible(true);
         }
      });
      JScrollPane var2 = new JScrollPane(this.console, 22, 30);
      this.add(var2, "Center");
      this.components.add(this.console);
      this.components.add(var2);
      this.components.add(this);
      this.updateComponentLooks();
      new CopyPopup(this.console);
      this.addActionForKeySetting("console.clear_screen.shortcut", "ctrl K", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Display.this.console.setText("");
         }
      });
      this.addActionForKeySetting("console.select_all.shortcut", "ctrl A", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Display.this.console.requestFocus();
            Display.this.console.selectAll();
         }
      });
      this.setupFindShortcutFeature();
      this.setupPageShortcutFeature();
      this.setupFontShortcutFeature();
      this.console.setBackground(new Color(0, 0, 0, 0));
      Color var3 = Prefs.getPreferences().getColor("console.background.color", "#000000");
      var2.getViewport().setBackground(var3);
      this.console.setOpaque(false);
   }

   private void setupFindShortcutFeature() {
      Properties var1 = this.display;
      this.addActionForKeySetting("console.find.shortcut", "ctrl pressed F", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Color var2 = Prefs.getPreferences().getColor("console.highlight.color", "#0000cc");
            final SearchPanel var3 = new SearchPanel(Display.this.console, var2);
            final JPanel var4 = new JPanel();
            JButton var5 = new JButton("X ");
            DialogUtils.removeBorderFromButton(var5);
            var5.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  Display.this.remove(var4);
                  Display.this.validate();
                  var3.clear();
               }
            });
            var4.setLayout(new BorderLayout());
            var4.add(var3, "Center");
            var4.add(var5, "East");
            Display.this.add(var4, "North");
            Display.this.validate();
            var3.requestFocusInWindow();
            var3.requestFocus();
         }
      });
   }

   private void setupFontShortcutFeature() {
      this.addActionForKeySetting("console.font_size_plus.shortcut", "ctrl EQUALS", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Display.this.changeFontSize(1.0F);
         }
      });
      this.addActionForKeySetting("console.font_size_minus.shortcut", "ctrl MINUS", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Display.this.changeFontSize(-1.0F);
         }
      });
      this.addActionForKeySetting("console.font_size_reset.shortcut", "ctrl pressed 0", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Display.this.consoleFont = Prefs.getPreferences().getFont("console.font.font", "Monospaced BOLD 14");
            Display.this.updateComponentLooks();
         }
      });
   }

   private void setupPageShortcutFeature() {
      this.addActionForKeySetting("console.page_up.shortcut", "pressed PAGE_UP", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Rectangle var2 = new Rectangle(Display.this.console.getVisibleRect());
            Rectangle var3 = new Rectangle(0, (int)(var2.getY() - var2.getHeight() / 2.0), 1, 1);
            if (var3.getY() <= 0.0) {
               var2.setLocation(0, 0);
            }

            Display.this.console.scrollRectToVisible(var3);
         }
      });
      this.addActionForKeySetting("console.page_down.shortcut", "pressed PAGE_DOWN", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Rectangle var2 = new Rectangle(Display.this.console.getVisibleRect());
            Rectangle var3 = new Rectangle(0, (int)(var2.getY() + var2.getHeight() + var2.getHeight() / 2.0), 1, 1);
            if (var3.getY() >= (double)Display.this.console.getHeight()) {
               var2.setLocation(0, Display.this.console.getHeight());
            }

            Display.this.console.scrollRectToVisible(var3);
         }
      });
   }

   private void changeFontSize(float var1) {
      this.consoleFont = this.consoleFont.deriveFont(this.consoleFont.getSize2D() + var1);
      this.updateComponentLooks();
   }

   public void addActionForKeyStroke(KeyStroke var1, Action var2) {
      this.console.getKeymap().addActionForKeyStroke(var1, var2);
   }

   public void addActionForKey(String var1, Action var2) {
      this.addActionForKeyStroke(KeyStroke.getKeyStroke(var1), var2);
   }

   public void addActionForKeySetting(String var1, String var2, Action var3) {
      KeyStroke var4 = KeyStroke.getKeyStroke(this.display.getProperty(var1, var2));
      if (var4 != null) {
         this.addActionForKeyStroke(var4, var3);
      }

   }

   public void clear() {
      CommonUtils.Guard();
      this.console.setDocument(new DefaultStyledDocument());
   }

   public void swap(StyledDocument var1) {
      CommonUtils.Guard();
      this.console.setDocument(var1);
   }

   public JTextPane getConsole() {
      return this.console;
   }
}
