package console;

import aggressor.Prefs;
import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class Console extends AssociatedPanel implements FocusListener {
   protected JTextPane console;
   protected JTextField input;
   protected JTextPane prompt;
   protected StatusBar status;
   protected PrintStream log;
   protected Properties display;
   protected Font consoleFont;
   protected Colors colors;
   protected ClickListener clickl;
   protected String defaultPrompt;
   protected LinkedList components;
   protected ListIterator history;
   protected boolean promptLock;
   protected Replacements[] colorme;
   protected JPanel bottom;

   public void addWordClickListener(ActionListener var1) {
      this.clickl.addListener(var1);
   }

   public void writeToLog(PrintStream var1) {
      this.log = var1;
   }

   public void setDefaultPrompt(String var1) {
      this.defaultPrompt = var1;
   }

   public void setPopupMenu(ConsolePopup var1) {
      this.clickl.setPopup(var1);
   }

   public JTextField getInput() {
      return this.input;
   }

   public void updateProperties(Properties var1) {
      this.display = var1;
      this.updateComponentLooks();
   }

   private void updateComponentLooks() {
      this.colors = new Colors(this.display);
      Color var1 = Prefs.getPreferences().getColor("console.foreground.color", "#c0c0c0");
      Color var2 = Prefs.getPreferences().getColor("console.background.color", "#000000");
      Iterator var3 = this.components.iterator();

      while(true) {
         while(var3.hasNext()) {
            JComponent var4 = (JComponent)var3.next();
            if (var4 == this.status) {
               var4.setFont(this.consoleFont);
            } else {
               var4.setForeground(var1);
               if (var4 != this.console && var4 != this.prompt) {
                  var4.setBackground(var2);
               } else {
                  var4.setOpaque(false);
               }

               var4.setFont(this.consoleFont);
               if (var4 != this.console && var4 != this.prompt) {
                  var4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
               } else {
                  var4.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
               }

               if (var4 instanceof JTextComponent) {
                  JTextComponent var5 = (JTextComponent)var4;
                  var5.setCaretColor(var1.brighter());
               }
            }
         }

         return;
      }
   }

   public String getPromptText() {
      return this.prompt.getText();
   }

   public void setPrompt(String var1) {
      String var2 = "��";
      if (!var1.equals(var2) && !var1.equals("null")) {
         this.defaultPrompt = var1;
         this.colors.set(this.prompt, this.fixText(var1));
      } else {
         this.colors.set(this.prompt, this.fixText(this.defaultPrompt));
      }

   }

   public void updatePrompt(final String var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            if (!Console.this.promptLock) {
               Console.this.setPrompt(var1);
            }

         }
      });
   }

   public void setStyle(String var1) {
      String[] var2 = var1.trim().split("\n");
      this.colorme = new Replacements[var2.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String[] var4 = var2[var3].split("\\t+");
         if (var4.length == 2) {
            var4[1] = var4[1].replace("\\c", "\u0003");
            var4[1] = var4[1].replace("\\o", "\u000f");
            var4[1] = var4[1].replace("\\u", "\u001f");
            this.colorme[var3] = new Replacements(var4[0], var4[1]);
         } else {
            System.err.println(var2[var3] + "<-- didn't split right:" + var4.length);
         }
      }

   }

   protected String fixText(String var1) {
      if (this.colorme == null) {
         return var1;
      } else {
         StringBuffer var2 = new StringBuffer();
         String[] var3 = var1.split("(?<=\\n)");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];

            for(int var6 = 0; var6 < this.colorme.length; ++var6) {
               if (this.colorme[var6] != null) {
                  var5 = this.colorme[var6].original.matcher(var5).replaceFirst(this.colorme[var6].replacer);
               }
            }

            var2.append(var5);
         }

         return var2.toString();
      }
   }

   protected void appendToConsole(String var1) {
      var1 = this.fixText(var1);
      if (var1.length() != 0) {
         if (!var1.endsWith("\n") && !var1.endsWith("\r")) {
            int var2 = var1.lastIndexOf("\n");
            if (var2 != -1) {
               this.colors.append(this.console, var1.substring(0, var2 + 1));
               this.updatePrompt(var1.substring(var2 + 1) + " ");
               if (this.log != null) {
                  this.log.print(this.colors.strip(var1.substring(0, var2 + 1)));
               }
            } else {
               this.updatePrompt(var1);
            }

            this.promptLock = true;
         } else {
            if (!this.promptLock) {
               this.colors.append(this.console, var1);
               if (this.log != null) {
                  this.log.print(this.colors.strip(var1));
               }
            } else {
               this.colors.append(this.console, this.prompt.getText());
            }

            if (!var1.startsWith(this.prompt.getText())) {
               this.promptLock = false;
            }
         }

         if (this.console.getDocument().getLength() >= 1) {
            this.console.setCaretPosition(this.console.getDocument().getLength() - 1);
         }

      }
   }

   public void append(final String var1) {
      if (var1 != null) {
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               Console.this.appendToConsole(var1);
            }
         });
      }
   }

   public void clear() {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            Console.this.console.setText("");
         }
      });
   }

   public void noInput() {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            Console.this.remove(Console.this.bottom);
            Console.this.validate();
         }
      });
   }

   public Console() {
      this(new Properties(), false);
   }

   public Console(boolean var1) {
      this(new Properties(), var1);
   }

   public Console(Properties var1, boolean var2) {
      this.log = null;
      this.defaultPrompt = "aggressor > ";
      this.components = new LinkedList();
      this.history = (new LinkedList()).listIterator(0);
      this.promptLock = false;
      this.colorme = null;
      this.bottom = null;
      this.display = var1;
      this.consoleFont = Prefs.getPreferences().getFont("console.font.font", "Monospaced BOLD 14");
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(2, 2, 2, 2));
      this.console = new JTextPane();
      this.console.setEditable(false);
      this.console.addFocusListener(this);
      this.console.setCaret(new DefaultCaret() {
         public void setSelectionVisible(boolean var1) {
            super.setSelectionVisible(true);
         }
      });
      JScrollPane var3 = new JScrollPane(this.console, 22, 30);
      this.add(var3, "Center");
      this.prompt = new JTextPane();
      this.prompt.setEditable(false);
      this.input = new JTextField();
      this.input.setKeymap(JTextField.addKeymap((String)null, this.input.getKeymap()));
      this.input.addMouseListener(new MouseAdapter() {
         public void checkEvent(MouseEvent var1) {
            if (var1.isPopupTrigger()) {
               Console.this.getPopupMenu((JTextComponent)var1.getSource()).show((JComponent)var1.getSource(), var1.getX(), var1.getY());
            }

         }

         public void mouseClicked(MouseEvent var1) {
            this.checkEvent(var1);
         }

         public void mousePressed(MouseEvent var1) {
            this.checkEvent(var1);
         }

         public void mouseReleased(MouseEvent var1) {
            this.checkEvent(var1);
         }
      });
      this.input.setFocusTraversalKeys(0, new HashSet());
      this.input.setFocusTraversalKeys(1, new HashSet());
      this.input.setFocusTraversalKeys(2, new HashSet());
      this.bottom = new JPanel();
      this.bottom.setLayout(new BorderLayout());
      this.status = new StatusBar(var1);
      if (var2) {
         this.bottom.add(this.status, "North");
      }

      this.bottom.add(this.input, "Center");
      this.bottom.add(this.prompt, "West");
      this.add(this.bottom, "South");
      this.components.add(this.input);
      this.components.add(this.console);
      this.components.add(var3);
      this.components.add(this.prompt);
      this.components.add(this.bottom);
      this.components.add(this.status);
      this.components.add(this);
      this.updateComponentLooks();
      this.addActionForKeySetting("console.clear_screen.shortcut", "ctrl K", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.console.setText("");
         }
      });
      this.addActionForKeySetting("console.select_all.shortcut", "ctrl A", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.console.requestFocus();
            Console.this.console.selectAll();
         }
      });
      this.addActionForKeySetting("console.clear_buffer.shortcut", "ESCAPE", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.input.setText("");
         }
      });
      this.setupFindShortcutFeature();
      this.setupPageShortcutFeature();
      this.setupFontShortcutFeature();
      this.setupHistoryFeature();
      this.clickl = new ClickListener(this);
      this.console.addMouseListener(this.clickl);
      Color var4 = Prefs.getPreferences().getColor("console.background.color", "#000000");
      this.console.setBackground(new Color(0, 0, 0, 0));
      this.prompt.setBackground(new Color(0, 0, 0, 0));
      var3.getViewport().setBackground(var4);
      this.console.setOpaque(false);
   }

   public StatusBar getStatusBar() {
      return this.status;
   }

   public JPopupMenu getPopupMenu(final JTextComponent var1) {
      JPopupMenu var2 = new JPopupMenu();
      JMenuItem var3 = new JMenuItem("Cut", 67);
      JMenuItem var4 = new JMenuItem("Copy", 111);
      JMenuItem var5 = new JMenuItem("Paste", 80);
      JMenuItem var6 = new JMenuItem("Clear", 108);
      if (var1.isEditable()) {
         var2.add(var3);
      }

      var2.add(var4);
      var2.add(var5);
      var2.add(var6);
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            var1.cut();
         }
      });
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            var1.copy();
         }
      });
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            var1.cut();
         }
      });
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.input.paste();
         }
      });
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            var1.setText("");
         }
      });
      return var2;
   }

   private void setupFindShortcutFeature() {
      Properties var1 = this.display;
      this.addActionForKeySetting("console.find.shortcut", "ctrl pressed F", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Color var2 = Prefs.getPreferences().getColor("console.highlight.color", "#0000cc");
            final SearchPanel var3 = new SearchPanel(Console.this.console, var2);
            final JPanel var4 = new JPanel();
            JButton var5 = new JButton("X ");
            DialogUtils.removeBorderFromButton(var5);
            var5.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  Console.this.remove(var4);
                  Console.this.validate();
                  var3.clear();
               }
            });
            var4.setLayout(new BorderLayout());
            var4.add(var3, "Center");
            var4.add(var5, "East");
            Console.this.add(var4, "North");
            Console.this.validate();
            var3.requestFocusInWindow();
            var3.requestFocus();
         }
      });
   }

   private void setupFontShortcutFeature() {
      this.addActionForKeySetting("console.font_size_plus.shortcut", "ctrl EQUALS", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.changeFontSize(1.0F);
         }
      });
      this.addActionForKeySetting("console.font_size_minus.shortcut", "ctrl MINUS", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.changeFontSize(-1.0F);
         }
      });
      this.addActionForKeySetting("console.font_size_reset.shortcut", "ctrl pressed 0", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Console.this.consoleFont = Prefs.getPreferences().getFont("console.font.font", "Monospaced BOLD 14");
            Console.this.updateComponentLooks();
         }
      });
   }

   private void setupPageShortcutFeature() {
      this.addActionForKeySetting("console.page_up.shortcut", "pressed PAGE_UP", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Rectangle var2 = new Rectangle(Console.this.console.getVisibleRect());
            Rectangle var3 = new Rectangle(0, (int)(var2.getY() - var2.getHeight() / 2.0), 1, 1);
            if (var3.getY() <= 0.0) {
               var2.setLocation(0, 0);
            }

            Console.this.console.scrollRectToVisible(var3);
         }
      });
      this.addActionForKeySetting("console.page_down.shortcut", "pressed PAGE_DOWN", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Rectangle var2 = new Rectangle(Console.this.console.getVisibleRect());
            Rectangle var3 = new Rectangle(0, (int)(var2.getY() + var2.getHeight() + var2.getHeight() / 2.0), 1, 1);
            if (var3.getY() >= (double)Console.this.console.getHeight()) {
               var2.setLocation(0, Console.this.console.getHeight());
            }

            Console.this.console.scrollRectToVisible(var3);
         }
      });
   }

   private void setupHistoryFeature() {
      this.input.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (!"".equals(var1.getActionCommand())) {
               Console.this.history.add(var1.getActionCommand());
            }

         }
      });
      this.addActionForKeySetting("console.history_previous.shortcut", "UP", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            if (Console.this.history.hasPrevious()) {
               Console.this.input.setText((String)Console.this.history.previous());
            } else {
               Console.this.input.setText("");
            }

         }
      });
      this.addActionForKeySetting("console.history_next.shortcut", "DOWN", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            if (Console.this.history.hasNext()) {
               Console.this.input.setText((String)Console.this.history.next());
            } else {
               Console.this.input.setText("");
            }

         }
      });
   }

   private void changeFontSize(float var1) {
      this.consoleFont = this.consoleFont.deriveFont(this.consoleFont.getSize2D() + var1);
      this.updateComponentLooks();
   }

   public void addActionForKeyStroke(KeyStroke var1, Action var2) {
      this.input.getKeymap().addActionForKeyStroke(var1, var2);
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

   public void focusGained(FocusEvent var1) {
      if (!var1.isTemporary() && var1.getComponent() == this.console && (System.getProperty("os.name") + "").indexOf("Windows") == -1 && (System.getProperty("os.name") + "").indexOf("Mac") == -1) {
         this.input.requestFocusInWindow();
      }

   }

   public boolean requestFocusInWindow() {
      return this.input.requestFocusInWindow();
   }

   public void focusLost(FocusEvent var1) {
   }

   private static class Replacements {
      public Pattern original;
      public String replacer;

      public Replacements(String var1, String var2) {
         this.original = Pattern.compile(var1);
         this.replacer = var2;
      }
   }

   public class ClickListener extends MouseAdapter {
      protected LinkedList listeners = new LinkedList();
      protected ConsolePopup popup = null;
      protected Console parent = null;

      public ClickListener(Console var2) {
         this.parent = var2;
      }

      public void setPopup(ConsolePopup var1) {
         this.popup = var1;
      }

      public void addListener(ActionListener var1) {
         this.listeners.add(var1);
      }

      public void mousePressed(MouseEvent var1) {
         this.checkPopup(var1);
      }

      public void mouseReleased(MouseEvent var1) {
         this.checkPopup(var1);
      }

      public void checkPopup(MouseEvent var1) {
         if (var1.isPopupTrigger()) {
            if (this.popup != null && Console.this.console.getSelectedText() == null) {
               String var2 = this.resolveWord(var1.getPoint());
               this.popup.showPopup(var2, var1);
            } else {
               Console.this.getPopupMenu((JTextComponent)var1.getSource()).show((JComponent)var1.getSource(), var1.getX(), var1.getY());
            }
         }

      }

      public void mouseClicked(MouseEvent var1) {
         if (!var1.isPopupTrigger()) {
            String var2 = this.resolveWord(var1.getPoint());
            Iterator var3 = this.listeners.iterator();
            new ActionEvent(this.parent, 0, var2);
            if (!"".equals(var2)) {
               while(var3.hasNext()) {
                  ActionListener var5 = (ActionListener)var3.next();
                  var5.actionPerformed(new ActionEvent(this.parent, 0, var2));
               }
            }
         } else {
            this.checkPopup(var1);
         }

      }

      public String resolveWord(Point var1) {
         int var2 = Console.this.console.viewToModel(var1);
         String var3 = Console.this.console.getText().replace("\n", " ").replaceAll("\\s", " ");
         int var4 = var3.lastIndexOf(" ", var2);
         int var5 = var3.indexOf(" ", var2);
         if (var4 == -1) {
            var4 = 0;
         }

         if (var5 == -1) {
            var5 = var3.length();
         }

         if (var5 >= var4) {
            String var6 = var3.substring(var4, var5).trim();
            return var6;
         } else {
            return null;
         }
      }
   }
}
