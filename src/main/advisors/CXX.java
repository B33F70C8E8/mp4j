package main.advisors;

import java.io.IOException;
import java.net.URL;

public class CXX {
  public CXX() {

  }
  public String callAPI() throws IOException {
    if(System.getProperty("os.name").contains("Windows")) {
      URL windowsAPI = getClass().getResource("/apiwrapper.exe");
      new Host();
      return Host.runProcess(Runtime.getRuntime(), windowsAPI.getPath());
    }
    return null;
  }

  public static void main(String[] args) throws IOException {
    new CXX().callAPI();
  }
  
}
