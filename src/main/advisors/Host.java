package main.advisors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.awt.Dimension;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;


public class Host {
  private static String lastDir = "";

  public Host(String lastDir) {
    this.lastDir = lastDir;
  }

  public static void extendedFileSaver(File i) throws IOException {
    LifePreserver lp = new LifePreserver(i.getAbsolutePath());
    lp.saveToPrevDir();
  }

  public static File openFileBrowser(java.awt.Component parent) {
    JFileChooser fileChooser = null;
    try {
      FlatAtomOneDarkContrastIJTheme.setup();
      fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setDialogTitle("Select File using File Explorer");
      fileChooser.setApproveButtonText("Select");
      fileChooser.setApproveButtonToolTipText("Select the file");
      if (lastDir != null || lastDir.length() == 0) {
        fileChooser.setCurrentDirectory(new File(lastDir));
      } else {
        fileChooser.setCurrentDirectory(new File("."));
      }
      fileChooser.setPreferredSize(new Dimension(700, 700));

      fileChooser.showOpenDialog(parent);
      return fileChooser.getSelectedFile();
    } catch (Exception e) {
      e.printStackTrace();
      new main.util.ErrorMessage(e.getStackTrace().toString());
    }
    return null;

  }

  public static String runProcess(Runtime runtime, CharSequence ch) throws IOException {
    Runtime rt = runtime;
    String[] commands = { ch.toString(), "-get t" };
    Process proc = rt.exec(commands);
    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

    String s = null;
    while ((s = stdInput.readLine()) != null) {
      return s;
    }
    return null;
  }

}
