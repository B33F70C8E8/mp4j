package app.core;

import java.io.IOException;

import app.global.Items;

import java.io.File;

public abstract class Cache {
  private Cache() {
  }

  public static boolean cleanCache() throws IOException {
    File cache = new File(Items.items[0]);
    if (cache.isDirectory()) {
      for (File f : cache.listFiles()) {
        f.delete();
      }
      return true;
    }
    return false;
  }
}
