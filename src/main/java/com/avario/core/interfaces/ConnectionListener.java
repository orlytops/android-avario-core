package com.avario.core.interfaces;

/**
 * Created by orlytops on 3/20/2018.
 */

public interface ConnectionListener {

  void onConnected();

  void onDisconnect(int retry);

}
