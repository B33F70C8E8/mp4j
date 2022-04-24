package project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import project.audio.Overseer;
import project.components.BigContainer;
import project.components.ParentPanel;
import project.components.ProcessesSchedule;
import project.components.sub_components.FileViewPanel;
import project.components.sub_components.FileViewWrapper;
import project.components.sub_components.InfoView;
import project.components.windows.ErrorWindow;
import project.components.windows.LoggerWindow;
import project.test.FilePaneNOthers.Size;
import project.components.sub_components.infoview.BottomView;
import project.components.sub_components.infoview.TopView;

public class Main {
  private static void launch() {
    ParentPanel pb;
    FileViewPanel fileViewPanel = new FileViewPanel();
    Map<JComponent, String> panels = new HashMap<>();
    TopView tv = new TopView();
    Overseer overseer = new Overseer(null, fileViewPanel, tv);
    JSplitPane otherSide = new InfoView(tv, new BottomView(overseer));
    fileViewPanel.getAl();
    FileViewWrapper fvw = new FileViewWrapper(fileViewPanel, overseer);
    JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, otherSide, fvw);
    jsp.setDividerLocation(Size.WIDTH - fvw.getWidth());
    fileViewPanel.dispatch();
    panels.put(jsp, BorderLayout.CENTER);
    pb = new ParentPanel(panels);
    new BigContainer(pb).run();
  }

  
  /** 
   * @param args
   */
  public static synchronized void main(String[] args) {
    try {
      ProcessesSchedule.main();
      Thread.sleep(100);
      launch();
    } catch (Exception e) {
      e.fillInStackTrace();
      e.printStackTrace();
    }
  }
}
