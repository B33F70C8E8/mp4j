package app.interfaces;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.interfaces.dialog.FrameConfirmDialog;
import app.functions.Worker;
import app.interfaces.event.RoundFrame;
import javazoom.jl.decoder.*;
import javazoom.jl.player.JavaSoundAudioDevice;
import app.CLI;

import static java.lang.Math.*;

public class WindowPanel implements ActionListener, ChangeListener {
  protected JPanel bp, mainPanel;
  protected URL[] waves = new URL[4];
  protected JButton play_btn, new_file;
  protected JLabel header_notice, status, wave_synth;
  protected JSlider volume_slider;
  protected static JFrame frame;
  protected static File musicFile;
  protected static Clip clip;
  protected boolean loop = false;
  protected static boolean alreadyPlaying = false, toPause = false, playAsMp3 = false;
  protected static String music_path;
  private final JavaSoundAudioDevice audioDevice = new javazoom.jl.player.JavaSoundAudioDevice();
  private javazoom.jl.player.Player mp3Player;
  protected URL pause_icon = getClass().getResource("/icons/others/pause_button.png");
  protected Icon pause_button_ico;
  protected Worker master = new Worker();

  {
    assert pause_icon != null;
    pause_button_ico = new javax.swing.ImageIcon(pause_icon);
  }

  protected URL play_icon = getClass().getResource("/icons/others/play_button.png");
  protected Icon play_button_ico;

  {
    assert play_icon != null;
    play_button_ico = new javax.swing.ImageIcon(play_icon);
  }

  public int currentFrame = 0;

  public WindowPanel(String resource) {
    music_path = resource;

    musicFile = SelectFileWindow.getFile();
    status = new JLabel("<html><b>Currently Playing: </b></html>" + musicFile.getName());
    status.setHorizontalAlignment((int) Component.CENTER_ALIGNMENT);

    waves[0] = getClass().getResource("/icons/animated/waves/wave_1.gif");
    waves[1] = getClass().getResource("/icons/animated/waves/wave_2.gif");
    waves[2] = getClass().getResource("/icons/animated/waves/wave_3.gif");
    waves[3] = getClass().getResource("/icons/animated/waves/paused/waves0.png");

    wave_synth = new JLabel(new ImageIcon(waves[3]));
    wave_synth.setHorizontalAlignment((int) Component.CENTER_ALIGNMENT);

    URL frame_icon = getClass().getResource("/icons/others/frame-icon.png");

    assert pause_icon != null;

    assert play_icon != null;

    assert frame_icon != null;
    ImageIcon frame_ico = new ImageIcon(frame_icon);

    frame = new JFrame("Music Player - Jack Meng");
    frame.setIconImage(frame_ico.getImage());
    frame.setUndecorated(true);
    frame.addComponentListener(new RoundFrame(frame));
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);

    play_btn = new JButton(play_button_ico);
    play_btn.addActionListener(this);
    play_btn.setToolTipText("Play the current media");
    play_btn.setAlignmentX(Component.CENTER_ALIGNMENT);

    URL new_file_icon = getClass().getResource("/icons/others/file_select_folder_icon.png");
    assert new_file_icon != null;
    Icon new_file_ico = new ImageIcon(new_file_icon);

    new_file = new JButton(new_file_ico);
    new_file.addActionListener(this);
    new_file.setToolTipText("Select a new media file");
    new_file.setAlignmentX(Component.CENTER_ALIGNMENT);

    header_notice = new JLabel(
        "<html><body><strong><u>Supported File Types : .mp3 & .wav</u></strong><br><center>Place files in folder: <code>/musicML/</code></center></body></html><br>");

    header_notice.setFont(new Font("Courier", Font.PLAIN, 13));
    header_notice.setAlignmentX(Component.CENTER_ALIGNMENT);

    volume_slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
    volume_slider.setMajorTickSpacing(10);
    volume_slider.setMinorTickSpacing(1);
    volume_slider.setMinimum(0);
    volume_slider.setMaximum(100);
    volume_slider.setPaintTicks(true);
    volume_slider.setPaintLabels(true);
    volume_slider.setAlignmentX(Component.CENTER_ALIGNMENT);
    volume_slider.addChangeListener(this);
    volume_slider.setToolTipText("Change the volume. Current: " + volume_slider.getValue() + "%");

    bp = new JPanel();
    bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
    bp.add(wave_synth);
    bp.add(status);
    bp.add(play_btn);
    bp.add(new_file);
    bp.add(volume_slider);

    bp.setPreferredSize(new Dimension(500, 200));

    mainPanel = new JPanel();
    mainPanel.add(bp);

    frame.add(mainPanel);

    currentFrame = 0;
  }

  public void volumeControl() {
    if (clip != null) {
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      float range = gainControl.getMaximum() - gainControl.getMinimum();
      float gain = (volume_slider.getValue() / 100.0f) * range + gainControl.getMinimum();
      gainControl.setValue(gain);
      volume_slider.setToolTipText("Current Volume: " + volume_slider.getValue() + "%");
    }
  }

  private Thread worker = new Thread();
  
  private boolean running = false;

  /**
   * @throws JavaLayerException
   */
  public void playMusic() throws JavaLayerException {
    try {
      if (!musicFile.getName().endsWith(".mp3")) {
        javax.sound.sampled.AudioInputStream audioInputStream = javax.sound.sampled.AudioSystem
            .getAudioInputStream(musicFile);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setMicrosecondPosition(currentFrame);
        clip.start();
        playAsMp3 = false;
        volumeControl();
        new Thread(() -> {
          // if the music is finished playing, update the button icon to be play
          while (clip.isActive() && clip != null) {
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              CLI.print(e, app.global.cli.CliType.ERROR);
            }
          }
          play_btn.setIcon(play_button_ico);
        }).start();
      } else {
        if (!running) {
          playAsMp3 = true;
          mp3Player = new javazoom.jl.player.Player(new java.io.FileInputStream(musicFile), audioDevice);
          volumeControlMP3();
          worker = new Thread(() -> {
            try {
              if (currentFrame < 0 || currentFrame != 0) {
                mp3Player.play(currentFrame);
              } else {
                mp3Player.play();
              }
            } catch (JavaLayerException e) {
              e.getException();
            }

          });
          worker.start();
        }

      }

    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void pauseMusic() {
    if (clip != null) {
      currentFrame = (int) clip.getMicrosecondPosition();
      clip.stop();
    }
    if (!worker.isInterrupted() && mp3Player != null) {
      worker.interrupt();
      worker = new Thread();
      currentFrame = mp3Player.getPosition();
      mp3Player.close();
      mp3Player = null;
    }
  }

  public void volumeControlMP3() {
    if (mp3Player != null) {
      audioDevice.setLineGain(volume_slider.getValue() / 100.0f);
      volume_slider.setToolTipText("Current Volume: " + volume_slider.getValue() + "%");
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(WindowPanel::run);
    File f = new File("musicML");
    if (!f.exists())
      f.mkdir();
    WindowPanel.run();
  }

  public void setPauseState() {
    play_btn.setIcon(play_button_ico);
    play_btn.setToolTipText("Play the current media");
    status.setText("<html><b>Currently Playing Nothing</b></html>");
    wave_synth.setIcon(new ImageIcon(waves[3]));
  }

  public void setPlayState() {
    play_btn.setIcon(pause_button_ico);
    play_btn.setToolTipText("Pause the current media");
    status.setText("<html><b>Currently Playing: </b><br>" + musicFile.getName() + "</html>");
    wave_synth.setIcon(new ImageIcon(waves[(int) random() * 3]));
  }

  /**
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    CLI.print(e.getSource());
    CLI.print("Current: " + currentFrame);
    if (e.getSource().equals(play_btn)) {
      if (worker != null)
        worker.interrupt();
      if (play_btn.getIcon() == play_button_ico) {
        if (worker != null)
          worker.interrupt();
        try {
          playMusic();
        } catch (JavaLayerException e1) {
          e1.printStackTrace();
        }
        setPlayState();
      } else if (play_btn.getIcon() == pause_button_ico) {
        if (worker != null)
          worker.interrupt();
        pauseMusic();
        setPauseState();
      }
    } else if (e.getSource().equals(volume_slider)) {
      if(musicFile.getName().endsWith(".mp3")) 
        volumeControlMP3();
      else
        volumeControl();
    } else if (e.getSource().equals(new_file)) {
      new FrameConfirmDialog("Are you sure you want to exit?", frame, new SelectFileWindow(music_path));
      pauseMusic();
      setPauseState();
    }
  }

  public static void run() {
    new WindowPanel(music_path);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * @param e
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource().equals(volume_slider)) {
      if(musicFile.getAbsolutePath().endsWith(".mp3"))
        volumeControlMP3();
      else
        volumeControl();
    }
  }
}
