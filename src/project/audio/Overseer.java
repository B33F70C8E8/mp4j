package project.audio;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import com.goxr3plus.streamplayer.enums.Status;

import project.audio.content.AudioInfoEditor;
import project.audio.content.AudioUtil;
import project.audio.content.VolumeConversion;
import project.components.sub_components.FileViewPanel;
import project.components.sub_components.infoview.SubVolumeView;
import project.components.sub_components.infoview.TopView;
import project.components.windows.ErrorWindow;
import project.constants.ColorContent;

public class Overseer extends StreamPlayer
    implements ActionListener, WindowListener, ChangeListener, StreamPlayerListener {
  private File current;
  public JButton playPauseButton, approveButton;
  public FileViewPanel fvp;
  public TopView topView;
  private JSlider volumeSlider, progressSlider;
  private boolean errorShown = false, isOpened = false;
  private long time = 0L;
  private Map<String, Object> prop;

  public Overseer(AudioUtil f, FileViewPanel fvp, TopView tv) {
    super();
    tv.setSeer(this);

    this.current = f;
    this.fvp = fvp;
    this.topView = tv;
    playPauseButton = new JButton("Play");
    playPauseButton.addActionListener(this);
    approveButton = new JButton("Select File");
    approveButton.addActionListener(this);
    volumeSlider = new JSlider(0, 100);
    volumeSlider.setValue(45);
    volumeSlider.addChangeListener(this);
    volumeSlider.setBackground(ColorContent.VOLUME_SLIDER_BG);
    volumeSlider.setForeground(ColorContent.VOLUME_SLIDER_NORMAL_FG);
    volumeSlider.setOrientation(SwingConstants.VERTICAL);

    progressSlider = new JSlider(0, 100);
    progressSlider.setBackground(ColorContent.VOLUME_SLIDER_BG);
    progressSlider.setForeground(ColorContent.PROGRESS_SLIDER_NORMAL);
    progressSlider.setOrientation(SwingConstants.HORIZONTAL);
    progressSlider.setValue(0);

    setGain(VolumeConversion.convertVolume(volumeSlider.getValue()));
    addStreamPlayerListener(this);
  }

  /**
   * @return String
   */
  public synchronized String getDir() {
    return fvp.getCurrentDirectory().getAbsolutePath();
  }

  /**
   * @return JButton
   */
  public JButton getPlayPauseButton() {
    return playPauseButton;
  }

  public JSlider getProgressSlider() {
    return progressSlider;
  }

  public JSlider getVolumeSlider() {
    return volumeSlider;
  }

  /**
   * @return JButton
   */
  public JButton getApproveButton() {
    return approveButton;
  }

  public void playFile() {
    try {
      if (!isOpened) {
        open(current);
        isOpened = true;
      }
      play();
      setGain(VolumeConversion.convertVolume(volumeSlider.getValue()));
    } catch (StreamPlayerException e) {
      e.printStackTrace();
    }
  }

  public void pokeFile(File f) {
    this.current = f;
  }

  /**
   * @return File
   */
  public File getFile() {
    return current;
  }

  /**
   * @param s
   */
  public synchronized void poke(AudioUtil s) {
    this.current = s;
  }

  public void resumeTime() {
    long secTime = time / 1000000;
    try {
      seekTo((int) secTime);
      play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean hasPlayed = false;

  /**
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(playPauseButton)) {
      if (current == null) {
        if (!errorShown) {
          new ErrorWindow("No File Selected", this);
          errorShown = true;
        } else {
          errorShown = false;
        }
      } else if (current.getAbsolutePath().endsWith("mp3")) {
        setGain(VolumeConversion.convertVolume(volumeSlider.getValue()));
        if (!isPlaying()) {
          if (!hasPlayed) {
            playFile();
            hasPlayed = true;
          } else {
            resumeTime();
          }
          playPauseButton.setEnabled(true);
          playPauseButton.setText("Pause");
        } else {
          stop();
          playPauseButton.setEnabled(true);
          playPauseButton.setText("Play");
        }
      }
    } else if (e.getSource().equals(approveButton)) {
      if (isPlaying() || isOpened()) {
        stop();
        playPauseButton.setText("Play");
      }
      if (fvp.getSelectedFile() != null) {
        current = fvp.getSelectedFile();
        try {
          open(current);
        } catch (StreamPlayerException e1) {
          e1.printStackTrace();
        }
        hasPlayed = false;
        progressSlider.setValue(0);
        topView.setAie(new AudioInfoEditor(current));
      } else {
        if (!errorShown) {
          new ErrorWindow(
              "No file selected!\nPlease select a valid Audio File of types:\n-mp3\n-wav", this);
          errorShown = true;
        }
      }
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    float volNow = VolumeConversion.convertVolume(volumeSlider.getValue());
    setGain(volNow);
    if (volumeSlider.getValue() >= 90) {
      volumeSlider.setForeground(ColorContent.VOLUME_SLIDER_HEARING_LOSS_FG);
    } else if (volumeSlider.getValue() >= 70 && volumeSlider.getValue() < 90) {
      volumeSlider.setForeground(ColorContent.VOLUME_SLIDER_WARNING_FG);
    } else if (volumeSlider.getValue() < 70 && volumeSlider.getValue() > 0) {
      volumeSlider.setForeground(ColorContent.VOLUME_SLIDER_NORMAL_FG);
    } else {
      volumeSlider.setForeground(ColorContent.VOLUME_SLIDER_MUTED_FG);
    }
  }

  /**
   * @param e
   */

  @Override
  public void windowClosed(WindowEvent e) {
    errorShown = false;
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void opened(Object arg0, Map<String, Object> arg1) {
  }

  @Override
  public synchronized void progress(int arg0, long arg1, byte[] pcmData, Map<String, Object> arg3) {
    time = arg1;
    long totalBytes = getTotalBytes();
    double progress = (arg0 > 0 && totalBytes > 0) ? (arg0 * 1.0f / totalBytes * 1.0f)
        : -1.0f;
    new Thread(() -> {
      byte[] curr = pcmData;

    
    }).start();
  }

  @Override
  public void statusUpdated(StreamPlayerEvent arg0) {
    if (arg0.getPlayerStatus().equals(Status.STOPPED)) {
      playPauseButton.setText("Play");

    }
  }

}
