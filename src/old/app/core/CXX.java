package app.core;

import java.io.IOException;
import java.net.URL;
/**
 * @author Jack Meng
 */
public class CXX {
  @Deprecated
  public CXX() {

  }
  
  /** 
   * @return String
   * @throws IOException
   */
  @Deprecated
  public String callAPI() throws IOException {
    if(System.getProperty("os.name").contains("Windows")) {
      URL windowsAPI = getClass().getResource("/apiwrapper.exe");
      assert windowsAPI != null;
      return app.core.Host.runProcess(Runtime.getRuntime(), windowsAPI.getPath());
    }
    return null;
  }

  
  /** 
   * @return String
   * @throws IOException
   */
  @Deprecated
  public String veriyFile() throws IOException {
    if(System.getProperty("os.name").contains("Windows")) {
      URL windowsAPI = getClass().getResource("/fileint.exe");
      assert windowsAPI != null;
      return app.core.Host.runProcess(Runtime.getRuntime(), windowsAPI.getPath());
    }
    return null;
  }

  
  /** 
   * @param args
   * @throws IOException
   */
  @Deprecated
  public static void main(String[] args) throws IOException {
    new CXX().callAPI();
  }
  
}
