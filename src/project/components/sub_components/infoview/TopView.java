package project.components.sub_components.infoview;

import javax.swing.JPanel;
import javax.swing.JLabel;

import project.audio.Overseer;
import project.audio.content.AudioUtil;
import project.components.windows.ErrorWindow;
import project.constants.Size;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

public class TopView extends JPanel {
  private JPanel mainPanel;
  private Overseer seer;
  public TopView() {
    mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(Size.WIDTH- 530, Size.HEIGHT-300));
    setOpaque(true);
    mainPanel.setBackground(Color.BLACK);
  }

  public void setSeer(Overseer seer) {
    this.seer = seer;
  }

  /**
   * @deprecated Use Overseer instead
   * @param f
   * @return
   */
  @Deprecated
  public static boolean check(File f) {
    if(f == null) {
      alert("Nothing was selected");
      return false;
    } else if (!((AudioUtil)f).isMP3()) {
      alert("This is not a mp3 file");
      return false;
    }
    return true;
  }

  /**
   * @deprecated Use Overseer instead
   * @param message
   */
  @Deprecated
  public static synchronized void alert(String message) {
    new ErrorWindow(message);
  }
}
