package org.apache.fop.render.awt.viewer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.awt.AWTRenderer;

public class PreviewDialog extends JFrame implements StatusListener {
   protected Translator translator;
   protected AWTRenderer renderer;
   protected FOUserAgent foUserAgent;
   protected float configuredTargetResolution;
   protected Renderable renderable;
   private JComboBox scale;
   private JLabel processStatus;
   private JLabel infoStatus;
   private PreviewPanel previewPanel;
   private DecimalFormat percentFormat;

   public PreviewDialog(FOUserAgent foUserAgent, Renderable renderable) {
      this.percentFormat = new DecimalFormat("###0.0#", new DecimalFormatSymbols(Locale.ENGLISH));
      this.renderer = (AWTRenderer)foUserAgent.getRendererOverride();
      this.foUserAgent = foUserAgent;
      this.configuredTargetResolution = this.foUserAgent.getTargetResolution();
      this.renderable = renderable;
      this.translator = new Translator();
      Command printAction = new Command(this.translator.getString("Menu.Print"), "Print") {
         public void doit() {
            PreviewDialog.this.startPrinterJob(true);
         }
      };
      Command firstPageAction = new Command(this.translator.getString("Menu.First.page"), "firstpg") {
         public void doit() {
            PreviewDialog.this.goToFirstPage();
         }
      };
      Command previousPageAction = new Command(this.translator.getString("Menu.Prev.page"), "prevpg") {
         public void doit() {
            PreviewDialog.this.goToPreviousPage();
         }
      };
      Command nextPageAction = new Command(this.translator.getString("Menu.Next.page"), "nextpg") {
         public void doit() {
            PreviewDialog.this.goToNextPage();
         }
      };
      Command lastPageAction = new Command(this.translator.getString("Menu.Last.page"), "lastpg") {
         public void doit() {
            PreviewDialog.this.goToLastPage();
         }
      };
      Command reloadAction = new Command(this.translator.getString("Menu.Reload"), "reload") {
         public void doit() {
            PreviewDialog.this.previewPanel.reload();
         }
      };
      Command debugAction = new Command(this.translator.getString("Menu.Debug"), "debug") {
         public void doit() {
            PreviewDialog.this.previewPanel.debug();
         }
      };
      Command aboutAction = new Command(this.translator.getString("Menu.About"), "fopLogo") {
         public void doit() {
            PreviewDialog.this.startHelpAbout();
         }
      };
      this.setTitle("FOP: AWT-" + this.translator.getString("Title.Preview"));
      this.setDefaultCloseOperation(2);
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      this.pack();
      this.setSize(screen.width * 61 / 100, screen.height * 9 / 10);
      this.previewPanel = new PreviewPanel(foUserAgent, renderable, this.renderer);
      this.getContentPane().add(this.previewPanel, "Center");
      this.previewPanel.addPageChangeListener(new PageChangeListener() {
         public void pageChanged(PageChangeEvent pce) {
            (PreviewDialog.this.new ShowInfo()).run();
         }
      });
      InputMap im = this.previewPanel.getInputMap(2);
      ActionMap am = this.previewPanel.getActionMap();
      im.put(KeyStroke.getKeyStroke(34, 0), "nextPage");
      im.put(KeyStroke.getKeyStroke(33, 0), "prevPage");
      im.put(KeyStroke.getKeyStroke(36, 0), "firstPage");
      im.put(KeyStroke.getKeyStroke(35, 0), "lastPage");
      this.previewPanel.getActionMap().put("nextPage", nextPageAction);
      this.previewPanel.getActionMap().put("prevPage", previousPageAction);
      this.previewPanel.getActionMap().put("firstPage", firstPageAction);
      this.previewPanel.getActionMap().put("lastPage", lastPageAction);
      this.scale = new JComboBox();
      this.scale.addItem(this.translator.getString("Menu.Fit.Window"));
      this.scale.addItem(this.translator.getString("Menu.Fit.Width"));
      this.scale.addItem("25%");
      this.scale.addItem("50%");
      this.scale.addItem("75%");
      this.scale.addItem("100%");
      this.scale.addItem("150%");
      this.scale.addItem("200%");
      this.scale.setMaximumSize(new Dimension(80, 24));
      this.scale.setPreferredSize(new Dimension(80, 24));
      this.scale.setSelectedItem("100%");
      this.scale.setEditable(true);
      this.scale.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            PreviewDialog.this.scaleActionPerformed(e);
         }
      });
      this.setJMenuBar(this.setupMenu());
      JToolBar toolBar = new JToolBar();
      toolBar.add(printAction);
      toolBar.add(reloadAction);
      toolBar.addSeparator();
      toolBar.add(firstPageAction);
      toolBar.add(previousPageAction);
      toolBar.add(nextPageAction);
      toolBar.add(lastPageAction);
      toolBar.addSeparator(new Dimension(20, 0));
      toolBar.add(new JLabel(this.translator.getString("Menu.Zoom") + " "));
      toolBar.add(this.scale);
      toolBar.addSeparator();
      toolBar.add(debugAction);
      toolBar.addSeparator();
      toolBar.add(aboutAction);
      this.getContentPane().add(toolBar, "North");
      JPanel statusBar = new JPanel();
      this.processStatus = new JLabel();
      this.processStatus.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 3, 0, 0)));
      this.infoStatus = new JLabel();
      this.infoStatus.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 3, 0, 0)));
      statusBar.setLayout(new GridBagLayout());
      this.processStatus.setPreferredSize(new Dimension(200, 21));
      this.processStatus.setMinimumSize(new Dimension(200, 21));
      this.infoStatus.setPreferredSize(new Dimension(100, 21));
      this.infoStatus.setMinimumSize(new Dimension(100, 21));
      statusBar.add(this.processStatus, new GridBagConstraints(0, 0, 1, 0, 2.0, 0.0, 10, 2, new Insets(0, 0, 0, 3), 0, 0));
      statusBar.add(this.infoStatus, new GridBagConstraints(1, 0, 1, 0, 1.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
      this.getContentPane().add(statusBar, "South");
   }

   public static PreviewDialog createPreviewDialog(FOUserAgent foUserAgent, Renderable renderable, boolean asMainWindow) {
      PreviewDialog frame = new PreviewDialog(foUserAgent, renderable);
      if (asMainWindow) {
         frame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent we) {
               System.exit(0);
            }
         });
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
      frame.setStatus(frame.translator.getString("Status.Build.FO.tree"));
      frame.setVisible(true);
      return frame;
   }

   public PreviewDialog(FOUserAgent foUserAgent) {
      this(foUserAgent, (Renderable)null);
   }

   private JMenuBar setupMenu() {
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu(this.translator.getString("Menu.File"));
      menu.setMnemonic(70);
      menu.add(new Command(this.translator.getString("Menu.Print"), 80) {
         public void doit() {
            PreviewDialog.this.startPrinterJob(true);
         }
      });
      if (this.renderable != null) {
         menu.add(new Command(this.translator.getString("Menu.Reload"), 82) {
            public void doit() {
               PreviewDialog.this.reload();
            }
         });
      }

      menu.addSeparator();
      menu.add(new Command(this.translator.getString("Menu.Exit"), 88) {
         public void doit() {
            PreviewDialog.this.dispose();
         }
      });
      menuBar.add(menu);
      menu = new JMenu(this.translator.getString("Menu.View"));
      menu.setMnemonic(86);
      menu.add(new Command(this.translator.getString("Menu.First.page"), 70) {
         public void doit() {
            PreviewDialog.this.goToFirstPage();
         }
      });
      menu.add(new Command(this.translator.getString("Menu.Prev.page"), 80) {
         public void doit() {
            PreviewDialog.this.goToPreviousPage();
         }
      });
      menu.add(new Command(this.translator.getString("Menu.Next.page"), 78) {
         public void doit() {
            PreviewDialog.this.goToNextPage();
         }
      });
      menu.add(new Command(this.translator.getString("Menu.Last.page"), 76) {
         public void doit() {
            PreviewDialog.this.goToLastPage();
         }
      });
      menu.add(new Command(this.translator.getString("Menu.Go.to.Page"), 71) {
         public void doit() {
            PreviewDialog.this.showGoToPageDialog();
         }
      });
      menu.addSeparator();
      JMenu subMenu = new JMenu(this.translator.getString("Menu.Zoom"));
      subMenu.setMnemonic(90);
      subMenu.add(new Command("25%", 0) {
         public void doit() {
            PreviewDialog.this.setScale(25.0);
         }
      });
      subMenu.add(new Command("50%", 0) {
         public void doit() {
            PreviewDialog.this.setScale(50.0);
         }
      });
      subMenu.add(new Command("75%", 0) {
         public void doit() {
            PreviewDialog.this.setScale(75.0);
         }
      });
      subMenu.add(new Command("100%", 0) {
         public void doit() {
            PreviewDialog.this.setScale(100.0);
         }
      });
      subMenu.add(new Command("150%", 0) {
         public void doit() {
            PreviewDialog.this.setScale(150.0);
         }
      });
      subMenu.add(new Command("200%", 0) {
         public void doit() {
            PreviewDialog.this.setScale(200.0);
         }
      });
      menu.add(subMenu);
      menu.addSeparator();
      menu.add(new Command(this.translator.getString("Menu.Default.zoom"), 68) {
         public void doit() {
            PreviewDialog.this.setScale(100.0);
         }
      });
      menu.add(new Command(this.translator.getString("Menu.Fit.Window"), 70) {
         public void doit() {
            PreviewDialog.this.setScaleToFitWindow();
         }
      });
      menu.add(new Command(this.translator.getString("Menu.Fit.Width"), 87) {
         public void doit() {
            PreviewDialog.this.setScaleToFitWidth();
         }
      });
      menu.addSeparator();
      ButtonGroup group = new ButtonGroup();
      JRadioButtonMenuItem single = new JRadioButtonMenuItem(new Command(this.translator.getString("Menu.Single"), 83) {
         public void doit() {
            PreviewDialog.this.previewPanel.setDisplayMode(1);
         }
      });
      JRadioButtonMenuItem cont = new JRadioButtonMenuItem(new Command(this.translator.getString("Menu.Continuous"), 67) {
         public void doit() {
            PreviewDialog.this.previewPanel.setDisplayMode(2);
         }
      });
      JRadioButtonMenuItem facing = new JRadioButtonMenuItem(new Command(this.translator.getString("Menu.Facing"), 0) {
         public void doit() {
            PreviewDialog.this.previewPanel.setDisplayMode(3);
         }
      });
      single.setSelected(true);
      group.add(single);
      group.add(cont);
      group.add(facing);
      menu.add(single);
      menu.add(cont);
      menu.add(facing);
      menuBar.add(menu);
      menu = new JMenu(this.translator.getString("Menu.Help"));
      menu.setMnemonic(72);
      menu.add(new Command(this.translator.getString("Menu.About"), 65) {
         public void doit() {
            PreviewDialog.this.startHelpAbout();
         }
      });
      menuBar.add(menu);
      return menuBar;
   }

   public void notifyRendererStopped() {
      this.reload();
   }

   private void reload() {
      this.setStatus(this.translator.getString("Status.Show"));
      this.previewPanel.reload();
   }

   public void goToPage(int number) {
      if (number != this.previewPanel.getPage()) {
         this.previewPanel.setPage(number);
         this.notifyPageRendered();
      }

   }

   public void goToPreviousPage() {
      int page = this.previewPanel.getPage();
      if (page > 0) {
         this.goToPage(page - 1);
      }

   }

   public void goToNextPage() {
      int page = this.previewPanel.getPage();
      if (page < this.renderer.getNumberOfPages() - 1) {
         this.goToPage(page + 1);
      }

   }

   public void goToFirstPage() {
      this.goToPage(0);
   }

   public void goToLastPage() {
      this.goToPage(this.renderer.getNumberOfPages() - 1);
   }

   private void startHelpAbout() {
      PreviewDialogAboutBox dlg = new PreviewDialogAboutBox(this, this.translator);
      Dimension dlgSize = dlg.getPreferredSize();
      Dimension frmSize = this.getSize();
      Point loc = this.getLocation();
      dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
      dlg.setVisible(true);
   }

   private void showGoToPageDialog() {
      int currentPage = this.previewPanel.getPage();
      GoToPageDialog d = new GoToPageDialog(this, this.translator.getString("Menu.Go.to.Page"), this.translator);
      d.setLocation((int)this.getLocation().getX() + 50, (int)this.getLocation().getY() + 50);
      d.setVisible(true);
      currentPage = d.getPageNumber();
      if (currentPage >= 1 && currentPage <= this.renderer.getNumberOfPages()) {
         --currentPage;
         this.goToPage(currentPage);
      }
   }

   public void setScale(double scaleFactor) {
      this.scale.setSelectedItem(this.percentFormat.format(scaleFactor) + "%");
      this.previewPanel.setScaleFactor(scaleFactor / 100.0);
   }

   public void setScaleToFitWindow() {
      try {
         this.setScale(this.previewPanel.getScaleToFitWindow() * 100.0);
      } catch (FOPException var2) {
         var2.printStackTrace();
      }

   }

   public void setScaleToFitWidth() {
      try {
         this.setScale(this.previewPanel.getScaleToFitWidth() * 100.0);
      } catch (FOPException var2) {
         var2.printStackTrace();
      }

   }

   private void scaleActionPerformed(ActionEvent e) {
      int index = this.scale.getSelectedIndex();
      if (index == 0) {
         this.setScaleToFitWindow();
      } else if (index == 1) {
         this.setScaleToFitWidth();
      } else {
         String item = (String)this.scale.getSelectedItem();
         this.setScale(Double.parseDouble(item.substring(0, item.indexOf(37))));
      }

   }

   public void startPrinterJob(boolean showDialog) {
      float saveResolution = this.foUserAgent.getTargetResolution();
      this.foUserAgent.setTargetResolution(this.configuredTargetResolution);
      PrinterJob pj = PrinterJob.getPrinterJob();
      pj.setPageable(this.renderer);
      if (!showDialog || pj.printDialog()) {
         try {
            pj.print();
         } catch (PrinterException var5) {
            var5.printStackTrace();
         }
      }

      this.foUserAgent.setTargetResolution(saveResolution);
   }

   public void setStatus(String message) {
      SwingUtilities.invokeLater(new ShowStatus(message));
   }

   public void notifyPageRendered() {
      SwingUtilities.invokeLater(new ShowInfo());
   }

   public void reportException(Exception e) {
      String msg = this.translator.getString("Exception.Occured");
      this.setStatus(msg);
      JOptionPane.showMessageDialog(this.getContentPane(), "<html><b>" + msg + ":</b><br>" + e.getClass().getName() + "<br>" + e.getMessage() + "</html>", this.translator.getString("Exception.Error"), 0);
   }

   private class ShowInfo implements Runnable {
      private ShowInfo() {
      }

      public void run() {
         String message = PreviewDialog.this.translator.getString("Status.Page") + " " + (PreviewDialog.this.previewPanel.getPage() + 1) + " " + PreviewDialog.this.translator.getString("Status.of") + " " + PreviewDialog.this.renderer.getNumberOfPages();
         PreviewDialog.this.infoStatus.setText(message);
      }

      // $FF: synthetic method
      ShowInfo(Object x1) {
         this();
      }
   }

   private class ShowStatus implements Runnable {
      private String message;

      public ShowStatus(String message) {
         this.message = message;
      }

      public void run() {
         PreviewDialog.this.processStatus.setText(this.message.toString());
      }
   }
}
