package app.interfaces;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import app.CLI;
import app.core.Cache;
import app.core.JSONParser;
import app.core.PropertiesReader;
import app.global.Items;
import app.global.VersionInfo;
import app.telemetry.FileIntegrity;
import app.telemetry.Logger;
import app.telemetry.api.Wrapper;
import app.interfaces.theme.LAFCommitter;
import app.interfaces.theme.Parser;
import app.interfaces.dialog.ErrorMessage;
import app.interfaces.dialog.OKWindow;
import app.interfaces.event.RoundFrame;

import java.net.URL;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;

public class SettingsWindow implements Runnable, ActionListener, ItemListener {
  public static JFrame frame;
  private final JPanel panel;
  private final JLabel title;
  private final JComboBox<String> theme;
  private final JLabel information;
  private final JButton verifyFile, clearCache, resetProperties, clearLogs;
  private Wrapper wrapper = new Wrapper();
  private FileIntegrity fileIntegrity = new FileIntegrity();

  public SettingsWindow(WelcomeWindow something) throws IOException {
    panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(500, 400));

    title = new JLabel("Settings");
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setFont(title.getFont().deriveFont(title.getFont().getSize() * 1.5f));

    verifyFile = new JButton("Verify File Integrity");
    verifyFile.setAlignmentX(Component.CENTER_ALIGNMENT);
    URL URLFILEINT = getClass().getResource("/icons/others/fileint_icon.png");
    assert URLFILEINT != null;
    Icon fileINTCO = new ImageIcon(URLFILEINT);
    verifyFile.setIcon(fileINTCO);
    verifyFile.addActionListener(this);

    theme = new JComboBox<>(Items.themes);
    theme.setAlignmentX(Component.CENTER_ALIGNMENT);
    theme.setMaximumSize(new Dimension(theme.getPreferredSize().width, theme.getPreferredSize().height));
    theme.setSelectedItem(PropertiesReader.getProp("gui.defaultTheme"));
    theme.setToolTipText("Change the theme of the application");
    theme.addItemListener(this);

    String json = wrapper.run();
    String versionInfo = ("<html><p>Your Version: " + VersionInfo.VERSION + "<br>Latest Release: "
        + (JSONParser.parseElement("latest_release", json) == null
            || java.util.Objects.equals(app.core.JSONParser.parseElement("latest_release", json), "")
                ? "Unavaliable"
                : JSONParser.parseElement("latest_release", json))
        + "<br>"
        + "Latest Patch: "
        + (JSONParser.parseElement("latest_patch", json) == null
            || java.util.Objects.equals(app.core.JSONParser.parseElement("latest_patch", json), "")
                ? "Unavaliable"
                : JSONParser.parseElement("latest_patch", json))
        + "<br>" + "Latest Beta: "
        + (JSONParser.parseElement("latest_beta", json) == null
            || java.util.Objects.equals(app.core.JSONParser.parseElement("latest_beta", json), "")
                ? "Unavaliable"
                : JSONParser.parseElement("latest_beta", json))
        + "</p></html>");

    information = new JLabel(versionInfo);
    information.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    information.setFont(information.getFont().deriveFont(information.getFont().getStyle() | Font.ITALIC));

    clearCache = new JButton("Clear Cache");
    clearCache.setAlignmentX(Component.CENTER_ALIGNMENT);
    URL URLCLEARCACHE = getClass().getResource("/trashcan_icon.png");
    assert URLCLEARCACHE != null;
    Icon trashcanCO = new ImageIcon(URLCLEARCACHE);
    clearCache.setIcon(trashcanCO);
    clearCache.addActionListener(this);

    clearLogs = new JButton("Clear Logs");
    clearLogs.setAlignmentX(Component.CENTER_ALIGNMENT);
    clearLogs.setIcon(trashcanCO);
    clearLogs.addActionListener(this);

    resetProperties = new JButton("Reset Properties");
    resetProperties.setAlignmentX(Component.CENTER_ALIGNMENT);
    URL URLRESETPROP = getClass().getResource("/icons/others/reset_icon.png");
    assert URLRESETPROP != null;
    Icon resetPROPCO = new ImageIcon(URLRESETPROP);
    resetProperties.setIcon(resetPROPCO);
    resetProperties.addActionListener(this);

    panel.add(title);
    panel.add(Box.createHorizontalStrut(10));
    panel.add(verifyFile);
    panel.add(clearCache);
    panel.add(resetProperties);
    panel.add(clearLogs);
    panel.add(theme);
    panel.add(Box.createHorizontalStrut(10));
    panel.add(Box.createHorizontalStrut(10));
    panel.add(information);

    URL icon = getClass().getResource("/icons/others/information_icon.png");
    assert icon != null;
    ImageIcon imageIcon = new ImageIcon(icon);
    frame = new JFrame("Music Player | Settings");
    frame.setSize(500, 600);
    frame.setResizable(false);
    frame.setIconImage(imageIcon.getImage());
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setUndecorated(true);
    frame.addComponentListener(new RoundFrame(frame));
    frame.add(panel);
  }

  public SettingsWindow() throws IOException {

    panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(500, 600));

    title = new JLabel("Settings");
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setFont(title.getFont().deriveFont(title.getFont().getSize() * 2.0f));

    verifyFile = new JButton("Check File Integrity");
    verifyFile.setAlignmentX(Component.CENTER_ALIGNMENT);
    URL URLFILEINT = getClass().getResource("/icons/others/fileint_icon.png");
    assert URLFILEINT != null;
    Icon fileINTCO = new ImageIcon(URLFILEINT);
    verifyFile.setIcon(fileINTCO);
    verifyFile.addActionListener(this);

    theme = new JComboBox<>(Items.themes);
    theme.setToolTipText("Change the theme of the application");
    theme.setAlignmentX(Component.CENTER_ALIGNMENT);
    theme.setMaximumSize(new Dimension(theme.getPreferredSize().width, theme.getPreferredSize().height));
    theme.setSelectedItem(PropertiesReader.getProp("gui.defaultTheme"));
    theme.addItemListener(this);

    String json = wrapper.run();
    String versionInfo = ("<html><p>Your Version: " + VersionInfo.VERSION + "<br>Latest Release: "
        + (JSONParser.parseElement("latest_release", json) == null
            || java.util.Objects.equals(app.core.JSONParser.parseElement("latest_release", json), "")
                ? "Unavaliable"
                : JSONParser.parseElement("latest_release", json))
        + "<br>"
        + "Latest Patch: "
        + (JSONParser.parseElement("latest_patch", json) == null
            || java.util.Objects.equals(app.core.JSONParser.parseElement("latest_patch", json), "")
                ? "Unavaliable"
                : JSONParser.parseElement("latest_patch", json))
        + "<br>" + "Latest Beta: "
        + (JSONParser.parseElement("latest_beta", json) == null
            || java.util.Objects.equals(app.core.JSONParser.parseElement("latest_beta", json), "")
                ? "Unavaliable"
                : JSONParser.parseElement("latest_beta", json))
        + "</p></html>");

    information = new JLabel(versionInfo);
    information.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    information.setFont(information.getFont().deriveFont(information.getFont().getStyle() | Font.ITALIC));

    clearCache = new JButton("Clear Cache");
    clearCache.setAlignmentX(Component.CENTER_ALIGNMENT);
    URL URLCLEARCACHE = getClass().getResource("/icons/others/trashcan_icon.png");
    assert URLCLEARCACHE != null;
    Icon trashcanCO = new ImageIcon(URLCLEARCACHE);
    clearCache.setIcon(trashcanCO);
    clearCache.addActionListener(this);

    clearLogs = new JButton("Clear Logs");
    clearLogs.setAlignmentX(Component.CENTER_ALIGNMENT);
    clearLogs.setIcon(trashcanCO);
    clearLogs.addActionListener(this);

    resetProperties = new JButton("Reset Properties");
    resetProperties.setAlignmentX(Component.CENTER_ALIGNMENT);
    URL URLRESETPROP = getClass().getResource("/icons/others/reset_icon.png");
    assert URLRESETPROP != null;
    Icon resetPROPCO = new ImageIcon(URLRESETPROP);
    resetProperties.setIcon(resetPROPCO);
    resetProperties.addActionListener(this);

    panel.add(title);
    panel.add(Box.createHorizontalStrut(10));
    panel.add(verifyFile);
    panel.add(clearCache);
    panel.add(resetProperties);
    panel.add(clearLogs);
    panel.add(theme);
    panel.add(Box.createHorizontalStrut(10));
    panel.add(Box.createHorizontalStrut(30));
    panel.add(information);

    URL icon = getClass().getResource("/icons/others/information_icon.png");
    assert icon != null;
    ImageIcon imageIcon = new ImageIcon(icon);
    frame = new JFrame("Music Player | Settings");
    frame.setSize(500, 400);
    frame.setResizable(false);
    frame.setIconImage(imageIcon.getImage());
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setUndecorated(true);
    frame.addComponentListener(new RoundFrame(frame));
    frame.add(panel);
  }

  @Override
  public void run() {
    frame.setVisible(true);
    frame.pack();
  }

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    new SettingsWindow().run();
  }

  /**
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(verifyFile)) {
      try {
        String isNice = fileIntegrity.isGood();
        Thread.sleep(800);
        if (isNice.equals("0"))
          new OKWindow("Files are all good!");
        else
          new ErrorMessage("Was unable to process the file: \n" + isNice);

      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(clearCache)) {
      try {
        if (Cache.cleanCache()) {
          new OKWindow("Cache Cleared");
        } else {
          new ErrorMessage("Could not clear Cache at this moment");
        }
      } catch (Exception e1) {
        e1.printStackTrace();
        CLI.print(e1.getMessage(), app.global.cli.CliType.WARNING);
      }
    } else if (e.getSource().equals(resetProperties)) {
      if (PropertiesReader.reset()) {
        new OKWindow("Properties Reset");
      } else {
        new ErrorMessage("Reset failed");
      }
    } else if (e.getSource().equals(clearLogs)) {
      if (Logger.clear()) {
        new OKWindow("Logs Cleared");
      } else {
        new ErrorMessage("Cannot Clear Logs at this moment");
      }
    }

  }

  /**
   * @return Object
   */
  public static Object getInstance() {
    return frame;
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    if(e.getSource().equals(theme)) {
      new LAFCommitter(java.awt.Frame.getFrames()).setMultTheme(new Parser(java.util.Objects.requireNonNull(theme.getSelectedItem()).toString()).getTheme());
      try {
        new Parser(java.util.Objects.requireNonNull(theme.getSelectedItem()).toString()).parseThemeToProperty();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

}
