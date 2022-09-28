package de.javasoft.plaf.synthetica.filechooser;

import java.io.File;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class SyntheticaFileChooser extends JFileChooser {
   public Icon getIcon(File var1) {
      FileSystemView var2 = FileSystemView.getFileSystemView();
      return var2.getSystemIcon(var1);
   }
}
