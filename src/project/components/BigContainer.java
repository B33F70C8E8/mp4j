package project.components;

import project.constants.Size;
import project.usables.Scheduled;

import javax.swing.*;
import java.awt.*;

public class BigContainer implements Scheduled {
  protected JFrame bigFrame;

  public BigContainer(JPanel parentJPanel) {
    bigFrame = new JFrame("Halcyon ~ Mp4J");
    bigFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    try {
      bigFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/others/frame-icon.png")).getImage());
    } catch (NullPointerException e) {
      // DO NOTHING
    }
    bigFrame.setResizable(true);
    bigFrame.setAutoRequestFocus(true);
    bigFrame.setPreferredSize(new Dimension(Size.WIDTH, Size.HEIGHT));
    bigFrame.setMinimumSize(new Dimension(Size.WIDTH, Size.HEIGHT));
    bigFrame.setMaximumSize(new Dimension(Size.MAX_BIGCONTAINER_WIDTH, Size.MAX_BIGCONTAINER_HEIGHT));
    bigFrame.setSize(new Dimension(Size.WIDTH, Size.HEIGHT));
    bigFrame.setLocationRelativeTo(null);
    bigFrame.getContentPane().add(parentJPanel);
    bigFrame.pack();
  }

  @Override
  public void run() {
    bigFrame.setVisible(true);
  }
}
