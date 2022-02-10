package backend.audioutil;

import java.io.File;
import backend.audio.*;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioSystem;

public class Player {
  private File f;
  public Clip c;
  private long frame = 0L;
  public float vols;
  private boolean alreadyOpen = false;
  private final Thread worker = new Thread();

  public Player(File f, float volume) {
    if (f.getAbsolutePath().endsWith(".mp3")) {
      this.f = Music.convert(f);
    } else {
      this.f = f;
    }
    this.vols = volume;
    try {
      c = AudioSystem.getClip();
    } catch (LineUnavailableException e1) {
      e1.printStackTrace();
    }

  }

  public Clip get() {
    return c;
  }

  public void setVolume() {
    if (c != null) {
      try {
      javax.sound.sampled.FloatControl gainControl = (javax.sound.sampled.FloatControl) c
          .getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
      float range = gainControl.getMaximum() - gainControl.getMinimum();
      float gain = (vols / 100.0f) * range + gainControl.getMinimum();
      gainControl.setValue(gain);
      } catch (IllegalArgumentException e) {
        // DO NOTHING
      }
    }
  }

  public synchronized void play() {
    if (!alreadyOpen) {
      try {
        c.open(AudioSystem.getAudioInputStream(f));
        alreadyOpen = true;
      } catch (java.io.IOException | javax.sound.sampled.UnsupportedAudioFileException
          | javax.sound.sampled.LineUnavailableException e) {
        e.printStackTrace();
      }
    }
    c.setMicrosecondPosition(frame);
    c.start();
    setVolume();
  }

  public synchronized void pause() {
    worker.interrupt();
    if (c.isRunning()) {
      c.stop();
      frame = c.getMicrosecondPosition();
    }
  }

  public synchronized long getFrame() {
    return frame;
  }

  public synchronized void setFrame(long frame) {
    this.frame = frame;
  }

  public synchronized void unload() {
    c.close();
  }

  public synchronized String getTrackName() {
    return f.getName();
  }

  public synchronized boolean loop() {
    if (c.isRunning()) {
      c.loop(Clip.LOOP_CONTINUOUSLY);
      return true;
    }
    return false;
  }
}
