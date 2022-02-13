package app.interfaces.theme.rules;

import app.interfaces.theme.Refresh;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkMediumIJTheme;

import java.awt.Color;

import javax.swing.*;
/**
 * @author Jack Meng
 */
public class Gruvbox implements Refresh {
  @Override
  public void refresh(java.awt.Window frame) throws UnsupportedLookAndFeelException {
    UIManager.setLookAndFeel(new FlatGruvboxDarkMediumIJTheme());
    try {
      SwingUtilities.updateComponentTreeUI(frame);
    } catch (Exception e) {
      // do nothing
    }
    frame.pack();
  }

  public String toString() {
    return "gruvbox";
  }

  @Override
  public Color getBorderColor() {
    return new Color(146, 131, 116);
  }
}
