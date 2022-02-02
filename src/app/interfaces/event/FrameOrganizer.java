package app.interfaces.event;

import javax.swing.JComponent;
import javax.swing.JFrame;

import app.core.PropertiesReader;

import java.awt.event.ComponentEvent;
import java.awt.RenderingHints;
import java.awt.event.ComponentListener;
import java.awt.geom.RoundRectangle2D;

import app.CLI;
/**
 * @author Jack Meng
 */
public class FrameOrganizer extends JComponent implements ComponentListener {
  private JFrame frame;

  public FrameOrganizer(JFrame frame) {
    this.frame = frame;
    frame.setUndecorated(true);
    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  }

  @Override
  public void componentResized(ComponentEvent e) {
    frame.setShape(new RoundRectangle2D.Float(0, 0, frame.getWidth(), frame.getHeight(), 15, 15));
    frame.repaint();
    CLI.print("Window transparency: " + PropertiesReader.getVal("gui.window_transparency"));
    frame.setOpacity(Float.parseFloat(PropertiesReader.getVal("gui.window_transparency")));

  }

  @Override
  public void componentMoved(ComponentEvent e) {
    // UNUSED
  }

  @Override
  public void componentShown(ComponentEvent e) {
    // UNUSED
  }

  @Override
  public void componentHidden(ComponentEvent e) {
    // UNUSED
  }

}
