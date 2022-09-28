package dialog;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.windows.BeaconConsole;
import aggressor.windows.SecureShellConsole;
import common.AssertUtils;
import common.BeaconEntry;
import common.CommonUtils;
import common.MudgeSanity;
import common.StringStack;
import common.TabScreenshot;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultRowSorter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import table.FilterAndScroll;
import ui.ATable;
import ui.ATextField;
import ui.GenericTableModel;
import ui.Sorters;

public class DialogUtils {
   public static Map icache = new HashMap();

   public static JFrame dialog(String var0, int var1, int var2) {
      JFrame var3 = new JFrame(var0);
      var3.setSize(var1, var2);
      var3.setLayout(new BorderLayout());
      var3.setLocationRelativeTo((Component)null);
      var3.setIconImage(getImage("resources/armitage-icon.gif"));
      var3.setDefaultCloseOperation(2);
      return var3;
   }

   public static void close(final JFrame var0) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            var0.setVisible(false);
            var0.dispose();
         }
      });
   }

   public static void showError(final String var0) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            JOptionPane.showMessageDialog((Component)null, var0, (String)null, 0);
         }
      });
   }

   public static void showInfo(final String var0) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            JOptionPane.showMessageDialog((Component)null, var0, (String)null, 1);
         }
      });
   }

   public static void showInput(final JFrame var0, final String var1, final String var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            JOptionPane.showInputDialog(var0, var1, var2);
         }
      });
   }

   public static GenericTableModel setupModel(String var0, String[] var1, List var2) {
      GenericTableModel var3 = new GenericTableModel(var1, var0, 8);
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         var3._addEntry((Map)var4.next());
      }

      return var3;
   }

   public static Map toMap(String var0) {
      HashMap var1 = new HashMap();
      StringStack var2 = new StringStack(var0, ",");

      while(!var2.isEmpty()) {
         String var3 = var2.pop();
         String[] var4 = var3.split(": ");
         if (var4.length != 2) {
            throw new RuntimeException("toMap: '" + var0 + "' failed at: " + var3);
         }

         var1.put(var4[0].trim(), var4[1].trim());
      }

      return var1;
   }

   public static void addToClipboard(String var0) {
      addToClipboardQuiet(var0);
      showInfo("Copied text to clipboard");
   }

   public static void addToClipboardQuiet(String var0) {
      StringSelection var1 = new StringSelection(var0);
      Clipboard var2 = Toolkit.getDefaultToolkit().getSystemSelection();
      if (var2 != null) {
         var2.setContents(var1, (ClipboardOwner)null);
      }

      var2 = Toolkit.getDefaultToolkit().getSystemClipboard();
      if (var2 != null) {
         var2.setContents(var1, (ClipboardOwner)null);
      }

   }

   public static void setTableColumnWidths(JTable var0, Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         String var4 = var3.getKey() + "";
         int var5 = Integer.parseInt(var3.getValue() + "");
         var0.getColumn(var4).setPreferredWidth(var5);
      }

   }

   public static ATable setupTable(TableModel var0, String[] var1, boolean var2) {
      return var2 ? setupTable(var0, var1, 2) : setupTable(var0, var1, 0);
   }

   public static ATable setupTable(TableModel var0, String[] var1, int var2) {
      ATable var3 = new ATable(var0);
      var3.getSelectionModel().setSelectionMode(var2);
      TableRowSorter var4 = new TableRowSorter(var0);

      for(int var5 = 0; var5 < var1.length; ++var5) {
         String var6 = var1[var5];
         Comparator var7 = Sorters.getProperSorter(var6);
         if (var7 != null) {
            var4.setComparator(var5, var7);
         }
      }

      var3.setRowSorter(var4);
      return var3;
   }

   public static void sortby(JTable var0, int var1) {
      try {
         LinkedList var2 = new LinkedList();
         var2.add(new RowSorter.SortKey(var1, SortOrder.ASCENDING));
         var0.getRowSorter().setSortKeys(var2);
         ((DefaultRowSorter)var0.getRowSorter()).sort();
      } catch (Exception var3) {
         MudgeSanity.logException("sortby: " + var1, var3, false);
      }

   }

   public static void sortby(JTable var0, int var1, int var2) {
      try {
         LinkedList var3 = new LinkedList();
         var3.add(new RowSorter.SortKey(var1, SortOrder.ASCENDING));
         var3.add(new RowSorter.SortKey(var2, SortOrder.ASCENDING));
         var0.getRowSorter().setSortKeys(var3);
         ((DefaultRowSorter)var0.getRowSorter()).sort();
      } catch (Exception var4) {
         MudgeSanity.logException("sortby: " + var1 + ", " + var2, var4, false);
      }

   }

   public static void startedWebService(String var0, String var1) {
      final JFrame var2 = dialog("Success", 240, 120);
      var2.setLayout(new BorderLayout());
      JLabel var3 = new JLabel("<html>Started service: " + var0 + "<br />Copy and paste this URL to access it</html>");
      ATextField var4 = new ATextField(var1, 20);
      JButton var5 = new JButton("Ok");
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var2.setVisible(false);
            var2.dispose();
         }
      });
      var2.add(wrapComponent(var3, 5), "North");
      var2.add(wrapComponent(var4, 5), "Center");
      var2.add(center((JComponent)var5), "South");
      var2.pack();
      var2.show();
      var2.setVisible(true);
   }

   public static void presentURL(String var0) {
      final JFrame var1 = dialog("Open URL", 240, 120);
      var1.setLayout(new BorderLayout());
      JLabel var2 = new JLabel("I couldn't open your browser. Try browsing to:");
      ATextField var3 = new ATextField(var0, 20);
      JButton var4 = new JButton("Ok");
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            var1.setVisible(false);
            var1.dispose();
         }
      });
      var1.add(wrapComponent(var2, 5), "North");
      var1.add(wrapComponent(var3, 5), "Center");
      var1.add(center((JComponent)var4), "South");
      var1.pack();
      var1.show();
      var1.setVisible(true);
   }

   public static void presentText(String var0, String var1, String var2) {
      final JFrame var3 = dialog(var0, 240, 120);
      var3.setLayout(new BorderLayout());
      JLabel var4 = new JLabel("<html>" + var1 + "</html>");
      ATextField var5 = new ATextField(var2, 20);
      JButton var6 = new JButton("Ok");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var3.setVisible(false);
            var3.dispose();
         }
      });
      var3.add(wrapComponent(var4, 5), "North");
      var3.add(wrapComponent(var5, 5), "Center");
      var3.add(center((JComponent)var6), "South");
      var3.pack();
      var3.show();
      var3.setVisible(true);
   }

   public static JComponent wrapComponent(JComponent var0, int var1) {
      JPanel var2 = new JPanel();
      var2.setLayout(new BorderLayout());
      var2.add(var0, "Center");
      var2.setBorder(BorderFactory.createEmptyBorder(var1, var1, var1, var1));
      return var2;
   }

   public static JComponent pad(JComponent var0, int var1, int var2, int var3, int var4) {
      JPanel var5 = new JPanel();
      var5.setLayout(new BorderLayout());
      var5.add(var0, "Center");
      var5.setBorder(BorderFactory.createEmptyBorder(var1, var2, var3, var4));
      return var5;
   }

   private static LinkedList asList(Object var0) {
      LinkedList var1 = new LinkedList();
      var1.add(var0);
      return var1;
   }

   private static LinkedList asList(Object var0, Object var1) {
      LinkedList var2 = new LinkedList();
      var2.add(var0);
      var2.add(var1);
      return var2;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2) {
      LinkedList var3 = new LinkedList();
      var3.add(var0);
      var3.add(var1);
      var3.add(var2);
      return var3;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2, Object var3) {
      LinkedList var4 = new LinkedList();
      var4.add(var0);
      var4.add(var1);
      var4.add(var2);
      var4.add(var3);
      return var4;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2, Object var3, Object var4) {
      LinkedList var5 = new LinkedList();
      var5.add(var0);
      var5.add(var1);
      var5.add(var2);
      var5.add(var3);
      var5.add(var4);
      return var5;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      LinkedList var6 = new LinkedList();
      var6.add(var0);
      var6.add(var1);
      var6.add(var2);
      var6.add(var3);
      var6.add(var4);
      var6.add(var5);
      return var6;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      LinkedList var7 = new LinkedList();
      var7.add(var0);
      var7.add(var1);
      var7.add(var2);
      var7.add(var3);
      var7.add(var4);
      var7.add(var5);
      var7.add(var6);
      return var7;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      LinkedList var8 = new LinkedList();
      var8.add(var0);
      var8.add(var1);
      var8.add(var2);
      var8.add(var3);
      var8.add(var4);
      var8.add(var5);
      var8.add(var6);
      var8.add(var7);
      return var8;
   }

   private static LinkedList asList(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      LinkedList var9 = new LinkedList();
      var9.add(var0);
      var9.add(var1);
      var9.add(var2);
      var9.add(var3);
      var9.add(var4);
      var9.add(var5);
      var9.add(var6);
      var9.add(var7);
      var9.add(var8);
      return var9;
   }

   public static JComponent stack(JComponent var0) {
      return stack((List)asList(var0));
   }

   public static JComponent stack(JComponent var0, JComponent var1) {
      return stack((List)asList(var0, var1));
   }

   public static JComponent stack(JComponent var0, JComponent var1, JComponent var2) {
      return stack((List)asList(var0, var1, var2));
   }

   public static JComponent stack(List var0) {
      Box var1 = Box.createVerticalBox();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         JComponent var3 = (JComponent)var2.next();
         var3.setAlignmentX(0.0F);
         var1.add(var3);
      }

      return var1;
   }

   public static JComponent stackTwo(JComponent var0, JComponent var1) {
      JPanel var2 = new JPanel();
      var2.setLayout(new BorderLayout());
      var2.add(var0, "Center");
      var2.add(var1, "South");
      return var2;
   }

   public static JComponent stackThree(JComponent var0, JComponent var1, JComponent var2) {
      return stackTwo(var0, stackTwo(var1, var2));
   }

   public static JComponent center(JComponent var0) {
      return center((List)asList(var0));
   }

   public static JComponent center(JComponent var0, JComponent var1) {
      return center((List)asList(var0, var1));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2) {
      return center((List)asList(var0, var1, var2));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2, JComponent var3) {
      return center((List)asList(var0, var1, var2, var3));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2, JComponent var3, JComponent var4) {
      return center((List)asList(var0, var1, var2, var3, var4));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2, JComponent var3, JComponent var4, JComponent var5) {
      return center((List)asList(var0, var1, var2, var3, var4, var5));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2, JComponent var3, JComponent var4, JComponent var5, JComponent var6) {
      return center((List)asList(var0, var1, var2, var3, var4, var5, var6));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2, JComponent var3, JComponent var4, JComponent var5, JComponent var6, JComponent var7) {
      return center((List)asList(var0, var1, var2, var3, var4, var5, var6, var7));
   }

   public static JComponent center(JComponent var0, JComponent var1, JComponent var2, JComponent var3, JComponent var4, JComponent var5, JComponent var6, JComponent var7, JComponent var8) {
      return center((List)asList(var0, var1, var2, var3, var4, var5, var6, var7, var8));
   }

   public static JComponent center(List var0) {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(1));
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         JComponent var3 = (JComponent)var2.next();
         var1.add(var3);
      }

      return var1;
   }

   public static JComponent description(String var0) {
      JEditorPane var1 = new JEditorPane();
      var1.setContentType("text/html");
      var1.setText(var0.trim());
      var1.setEditable(false);
      var1.setOpaque(true);
      var1.setCaretPosition(0);
      var1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      JScrollPane var2 = new JScrollPane(var1);
      var2.setPreferredSize(new Dimension(0, 48));
      var2.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      return var2;
   }

   public static ActionListener gotoURL(final String var0) {
      return new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            if (Desktop.isDesktopSupported()) {
               (new Thread(new Runnable() {
                  public void run() {
                     try {
                        Desktop.getDesktop().browse((new URL(var0)).toURI());
                     } catch (Exception var2) {
                        MudgeSanity.logException("goto: " + var0 + " *grumble* *grumble*", var2, true);
                        DialogUtils.presentURL(var0);
                     }

                  }
               }, "show URL")).start();
            } else {
               CommonUtils.print_error("No desktop support to show: " + var0);
            }

         }
      };
   }

   public static boolean isShift(ActionEvent var0) {
      return (var0.getModifiers() & 1) == 1;
   }

   public static void setupTimeRenderer(JTable var0, String var1) {
      var0.getColumn(var1).setCellRenderer(ATable.getTimeTableRenderer());
   }

   public static void setupDateRenderer(JTable var0, String var1) {
      var0.getColumn(var1).setCellRenderer(ATable.getDateTableRenderer());
   }

   public static void setupSizeRenderer(JTable var0, String var1) {
      var0.getColumn(var1).setCellRenderer(ATable.getSizeTableRenderer());
   }

   public static void setupImageRenderer(JTable var0, GenericTableModel var1, String var2, String var3) {
      var0.getColumn(var2).setCellRenderer(ATable.getImageTableRenderer(var1, var3));
   }

   public static void setupBoldOnKeyRenderer(JTable var0, GenericTableModel var1, String var2, String var3) {
      var0.getColumn(var2).setCellRenderer(ATable.getBoldOnKeyRenderer(var1, var3));
   }

   public static void setupListenerStatusRenderer(JTable var0, GenericTableModel var1, String var2) {
      var0.getColumn(var2).setCellRenderer(ATable.getListenerStatusRenderer(var1));
   }

   public static boolean bool(Map var0, String var1) {
      String var2 = var0.get(var1) + "";
      return var2.equals("true");
   }

   public static String string(Map var0, String var1) {
      Object var2 = var0.get(var1);
      return var2 == null ? "" : var2.toString();
   }

   public static boolean isNumber(Map var0, String var1) {
      try {
         number(var0, var1);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public static int number(Map var0, String var1) {
      return Integer.parseInt(string(var0, var1));
   }

   public static void workAroundEditorBug(JEditorPane var0) {
      var0.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
   }

   public static JComponent top(JComponent var0) {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      var1.add(var0, "North");
      var1.add(Box.createVerticalGlue(), "Center");
      return var1;
   }

   public static String encodeColor(Color var0) {
      String var1;
      for(var1 = Integer.toHexString(var0.getRGB() & 16777215); var1.length() < 6; var1 = "0" + var1) {
      }

      return "#" + var1;
   }

   public static JButton Button(String var0, ActionListener var1) {
      JButton var2 = new JButton(var0);
      var2.addActionListener(var1);
      return var2;
   }

   public static Icon getIcon(String var0) {
      try {
         return new ImageIcon(ImageIO.read(CommonUtils.resource(var0)));
      } catch (IOException var2) {
         MudgeSanity.logException("getIcon: " + var0, var2, false);
         return null;
      }
   }

   public static Image getImage(String var0) {
      try {
         return ImageIO.read(CommonUtils.resource(var0));
      } catch (IOException var2) {
         MudgeSanity.logException("getImage: " + var0, var2, false);
         return null;
      }
   }

   public static Image getImage(String[] var0, boolean var1) {
      GraphicsEnvironment var2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsConfiguration var3 = var2.getDefaultScreenDevice().getDefaultConfiguration();
      BufferedImage var4 = var3.createCompatibleImage(1000, 776, 2);
      Graphics2D var5 = ((BufferedImage)var4).createGraphics();

      for(int var6 = 0; var6 < var0.length; ++var6) {
         try {
            BufferedImage var7 = ImageIO.read(CommonUtils.resource(var0[var6]));
            var5.drawImage(var7, 0, 0, 1000, 776, (ImageObserver)null);
         } catch (Exception var8) {
            MudgeSanity.logException("getImage: " + var0[var6], var8, false);
         }
      }

      if (var1) {
         Graphics2D var9 = (Graphics2D)var5;
         var9.setColor(Color.BLACK);
         AlphaComposite var10 = AlphaComposite.getInstance(3, 0.4F);
         var9.setComposite(var10);
         var9.fillRect(0, 0, 1000, 776);
      }

      var5.dispose();
      return var4;
   }

   public static byte[] toImage(RenderedImage var0, String var1) {
      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream(524288);
         ImageIO.write(var0, var1, var2);
         var2.close();
         return var2.toByteArray();
      } catch (Exception var3) {
         MudgeSanity.logException("toImage: " + var1, var3, false);
         return new byte[0];
      }
   }

   public static Image getImageSmall(String[] var0, boolean var1) {
      BufferedImage var2 = new BufferedImage(1000, 776, 2);
      Graphics2D var3 = ((BufferedImage)var2).createGraphics();

      for(int var4 = 0; var4 < var0.length; ++var4) {
         try {
            BufferedImage var5 = ImageIO.read(CommonUtils.resource(var0[var4]));
            var3.drawImage(var5, 0, 0, 1000, 776, (ImageObserver)null);
         } catch (Exception var7) {
            MudgeSanity.logException("getImageSmall: " + var0[var4], var7, false);
         }
      }

      if (var1) {
         float[] var8 = new float[]{1.0F, 1.0F, 1.0F, 0.5F};
         float[] var9 = new float[4];
         RescaleOp var6 = new RescaleOp(var8, var9, (RenderingHints)null);
         var2 = var6.filter((BufferedImage)var2, (BufferedImage)null);
      }

      var3.dispose();
      return var2;
   }

   public static BufferedImage resize(Image var0, int var1, int var2) {
      BufferedImage var3 = new BufferedImage(var1, var2, 2);
      Graphics2D var4 = ((BufferedImage)var3).createGraphics();
      Image var5 = var0.getScaledInstance(var1, var2, 4);
      var4.drawImage(var5, 0, 0, var1, var2, (ImageObserver)null);
      var4.dispose();
      return (BufferedImage)var3;
   }

   public static void addToTable(final ATable var0, final GenericTableModel var1, final Map var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            var0.markSelections();
            var1.addEntry(var2);
            var1.fireListeners();
            var0.restoreSelections();
         }
      });
   }

   public static void setText(final ATextField var0, final String var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            var0.setText(var1);
         }
      });
   }

   public static void setTable(final ATable var0, final GenericTableModel var1, Collection var2) {
      if (AssertUtils.TestNotNull(var0, "table")) {
         final LinkedList var3 = new LinkedList(var2);
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               var0.markSelections();
               var1.clear(var3.size());
               Iterator var1x = var3.iterator();

               while(var1x.hasNext()) {
                  var1.addEntry((Map)var1x.next());
               }

               var1.fireListeners();
               var0.restoreSelections();
            }
         });
      }
   }

   public static String[] TargetVisualizationArray(String var0, double var1, boolean var3) {
      String[] var4 = new String[]{"resources/unknown.png", null};
      if (var3) {
         var4[1] = "resources/hacked.png";
      } else {
         var4[1] = "resources/computer.png";
      }

      if (var0.equals("windows")) {
         if (var1 <= 5.0) {
            var4[0] = "resources/windows2000.png";
         } else if (var1 > 5.0 && var1 < 6.0) {
            var4[0] = "resources/windowsxp.png";
         } else if (var1 != 6.0 && var1 != 6.1) {
            if (var1 >= 6.2) {
               var4[0] = "resources/windows8.png";
            }
         } else {
            var4[0] = "resources/windows7.png";
         }
      } else {
         if (var0.equals("firewall")) {
            return CommonUtils.toArray("resources/firewall.png");
         }

         if (var0.equals("printer")) {
            return CommonUtils.toArray("resources/printer.png");
         }

         if (var0.equals("android")) {
            var4[0] = "resources/android.png";
         } else if (var0.equals("vmware")) {
            var4[0] = "resources/vmware.png";
         } else if (var0.equals("solaris")) {
            var4[0] = "resources/solaris.png";
         } else if (!var0.equals("freebsd") && !var0.equals("openbsd") && !var0.equals("netbsd")) {
            if (var0.equals("linux")) {
               var4[0] = "resources/linux.png";
            } else if (var0.equals("cisco ios")) {
               var4[0] = "resources/cisco.png";
            } else if (var0.equals("macos x")) {
               var4[0] = "resources/macosx.png";
            } else if (var0.equals("apple ios")) {
               var4[0] = "resources/ios.png";
            }
         } else {
            var4[0] = "resources/bsd.png";
         }
      }

      return var4;
   }

   public static ImageIcon TargetVisualizationSmall(String var0, double var1, boolean var3, boolean var4) {
      String var5 = "small:" + var0.toLowerCase() + "." + var1 + "." + var3 + "." + var4;
      synchronized(icache) {
         if (icache.containsKey(var5)) {
            return (ImageIcon)icache.get(var5);
         } else {
            Image var7 = getImageSmall(TargetVisualizationArray(var0.toLowerCase(), var1, var3), var4);
            ImageIcon var8 = new ImageIcon(var7.getScaledInstance((int)Math.floor((double)var7.getWidth((ImageObserver)null) / 44.0), (int)Math.floor((double)var7.getHeight((ImageObserver)null) / 44.0), 4));
            icache.put(var5, var8);
            return var8;
         }
      }
   }

   public static Image TargetVisualizationMedium(String var0, double var1, boolean var3, boolean var4) {
      String var5 = "medium:" + var0.toLowerCase() + "." + var1 + "." + var3 + "." + var4;
      synchronized(icache) {
         if (icache.containsKey(var5)) {
            return (Image)icache.get(var5);
         } else {
            Image var7 = getImageSmall(TargetVisualizationArray(var0.toLowerCase(), var1, var3), var4);
            BufferedImage var8 = resize(var7, 125, 97);
            icache.put(var5, var8);
            return var8;
         }
      }
   }

   public static Image TargetVisualization(String var0, double var1, boolean var3, boolean var4) {
      String var5 = var0.toLowerCase() + "." + var1 + "." + var3 + "." + var4;
      synchronized(icache) {
         if (icache.containsKey(var5)) {
            return (Image)icache.get(var5);
         } else {
            Image var7 = getImage(TargetVisualizationArray(var0.toLowerCase(), var1, var3), var4);
            icache.put(var5, var7);
            return var7;
         }
      }
   }

   public static void openOrActivate(final AggressorClient var0, final String var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            BeaconEntry var1x = DataUtils.getBeacon(var0.getData(), var1);
            if (!var0.getTabManager().activateConsole(var1)) {
               if (var1x.isBeacon()) {
                  BeaconConsole var2 = new BeaconConsole(var1, var0);
                  var0.getTabManager().addTab(var1x.title(), var2.getConsole(), var2.cleanup(), "Beacon console");
               } else if (var1x.isSSH()) {
                  SecureShellConsole var3 = new SecureShellConsole(var1, var0);
                  var0.getTabManager().addTab(var1x.title(), var3.getConsole(), var3.cleanup(), "SSH console");
               }
            }

         }
      });
   }

   public static void setupScreenshotShortcut(final AggressorClient var0, final ATable var1, final String var2) {
      var1.addActionForKey("ctrl pressed P", new AbstractAction() {
         public void actionPerformed(ActionEvent var1x) {
            BufferedImage var2x = var1.getScreenshot();
            byte[] var3 = DialogUtils.toImage((BufferedImage)var2x, "png");
            var0.getConnection().call("aggressor.screenshot", CommonUtils.args(new TabScreenshot(var2, var3)));
            DialogUtils.showInfo("Pushed screenshot to team server");
         }
      });
   }

   public static byte[] screenshot(Component var0) {
      BufferedImage var1 = new BufferedImage(var0.getWidth(), var0.getHeight(), 6);
      Graphics var2 = var1.getGraphics();
      var0.paint(var2);
      var2.dispose();
      byte[] var3 = toImage(var1, "png");
      return var3;
   }

   public static void removeBorderFromButton(JButton var0) {
      var0.setOpaque(false);
      var0.setContentAreaFilled(false);
      var0.setBorder(new EmptyBorder(2, 2, 2, 2));
   }

   public static JPanel FilterAndScroll(ATable var0) {
      return new FilterAndScroll(var0);
   }

   public static void showSessionPopup(AggressorClient var0, MouseEvent var1, Object[] var2) {
      if (var2.length != 0) {
         String var3 = var2[0].toString();
         String var4 = "";
         if ("beacon".equals(CommonUtils.session(var3))) {
            var4 = "beacon";
         } else {
            var4 = "ssh";
         }

         Stack var5 = new Stack();
         var5.push(CommonUtils.toSleepArray(var2));
         var0.getScriptEngine().getMenuBuilder().installMenu(var1, var4, var5);
      }
   }
}
