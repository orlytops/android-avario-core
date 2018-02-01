package com.avario.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by aeroheart-c6 on 12/01/2017.
 */
public class Config {
  public static final String TAG = "Avario/Config";

  private static final String PREFKEY_RES_FETCHED         = "setting__res_fetched";
  private static final String PREFKEY_HTTP_HOST           = "setting__http_host";
  private static final String PREFKEY_HTTP_PORT           = "setting__http_port";
  private static final String PREFKEY_HTTP_SSL            = "setting__http_ssl";
  private static final String PREFKEY_USERNAME            = "setting__username";
  private static final String PREFKEY_PASSWORD            = "setting__password";
  private static final String PREFKEY_ASSET_ROOT          = "setting__assets";
  private static final String PREFKEY_BOOTSTRAP           = "setting__bootstrap";
  private static final String PREFKEY_ROOM_SELECTED       = "roomSelected";
  private static final String PREFKEY_HOLD_SECONDS        = "setting__hold_seconds";
  private static final String PREFKEY_LIGHT_ALGO          = "algo_light";
  private static final String PREFKEY_IS_KIOSK            = "is_kiosk";
  private static final String PREFKEY_IS_TABLET           = "is_tablet";
  private static final String PREFKEY_FCM_TOKEN           = "fcm_token";
  private static final String PREFKEY_TO_IGNORE           = "to_ignore";
  private static final String PREFKEY_IS_IMAGE_DOWNLOADED = "is_image_downloaded";
  private static final String PREFKEY_IS_IMAGE_LAN        = "is_image_lan";

  private static Config instance = null;

  private Context           context;
  private SharedPreferences prefs;

  private String  tempHttpHost;
  private String  tempHttpPort;
  private boolean tempSSL;
  private boolean tempFetched;
  private boolean tempIsKiosk;
  private boolean tempIsTablet;
  private String  tempUsername;
  private String  temppassword;
  private String  tempBootstrap;
  private String  roomSelected;
  private String  toIgnore;
  private boolean isImageDownloaded;
  private boolean isImageLan;

  public static Config getInstance() {
    return Config.instance;
  }

  /**
   * Retrieves the singleton instance of this class. It is important to pass the application
   * context here because this object will be alive throughout the duration of the app.
   *
   * @param context the application context
   * @return instance of the APIClient singleton
   */
  public static Config getInstance(Context context) {
    if (Config.instance == null) { Config.instance = new Config(context); }

    return Config.instance;
  }

  private Config(Context context) {
    super();

    this.context = context;
    this.prefs = PreferenceManager.getDefaultSharedPreferences(context);

    tempHttpHost = fetchString(PREFKEY_HTTP_HOST);
    tempHttpPort = fetchString(PREFKEY_HTTP_PORT);
    tempSSL = fetchBoolean(PREFKEY_HTTP_SSL);
    tempFetched = fetchBoolean(PREFKEY_RES_FETCHED);
    tempIsKiosk = fetchBoolean(PREFKEY_IS_KIOSK);
    tempIsTablet = fetchBoolean(PREFKEY_IS_TABLET);
    tempUsername = fetchString(PREFKEY_USERNAME);
    temppassword = fetchString(PREFKEY_PASSWORD);
    tempBootstrap = fetchString(PREFKEY_BOOTSTRAP);
    toIgnore = fetchString(PREFKEY_TO_IGNORE);
    roomSelected = fetchString(PREFKEY_ROOM_SELECTED);
    isImageDownloaded = fetchBoolean(PREFKEY_IS_IMAGE_DOWNLOADED);
    isImageLan = fetchBoolean(PREFKEY_IS_IMAGE_LAN);
  }

  public boolean isSet() {
    String httpHost = this.getHttpHost(),
        httpPort = this.getHttpPort();

    return (httpHost != null && httpHost.length() > 0)
        && (httpPort != null && httpPort.length() > 0);
  }

  public boolean isResourcesFetched() {
    return this.fetchBoolean(PREFKEY_RES_FETCHED);
  }

  public String getHttpDomain() {
    return String.format("http%s://%s:%s",
        this.isHttpSSL() ? "s" : "",
        this.getHttpHost(),
        this.getHttpPort()
    );
  }

  public String getHttpHost() {
    return this.fetchString(PREFKEY_HTTP_HOST);
  }

  public String getHttpPort() {
    return this.fetchString(PREFKEY_HTTP_PORT);
  }

  public boolean isHttpSSL() {
    return this.fetchBoolean(PREFKEY_HTTP_SSL);
  }

  public String getUsername() {
    return this.fetchString(PREFKEY_USERNAME);
  }

  public String getPassword() {
    return this.fetchString(PREFKEY_PASSWORD);
  }

  public boolean isKiosk() {
    return this.fetchBoolean(PREFKEY_IS_KIOSK);
  }

  public boolean isTablet() {
    return this.fetchBoolean(PREFKEY_IS_TABLET);
  }

  public boolean isImageDownloaded() {
    return this.fetchBoolean(PREFKEY_IS_IMAGE_DOWNLOADED);
  }

  public boolean isImageLan() {
    return this.fetchBoolean(PREFKEY_IS_IMAGE_LAN);
  }

  public List<String> getRoomSelected() {
    Type listType = new TypeToken<List<String>>() {
    }.getType();

    Gson gson = new Gson();
    List<String> roomsSelected = gson.fromJson(this.fetchString(PREFKEY_ROOM_SELECTED), listType);

    return roomsSelected;
  }

  public String getFCM() {
    return this.fetchString(PREFKEY_FCM_TOKEN);
  }

  public String getToIgnore() {
    return this.fetchString(PREFKEY_TO_IGNORE);
  }

  public String getAssetRoot() {
    return String.format("%s/x%%.1f", this.fetchString(
        PREFKEY_ASSET_ROOT,
        R.string.app_url_assetroot)
    );
  }

  public String getBootstrapURL() {
    return String.format("%s%s",
        this.getHttpDomain(),
        this.fetchString(PREFKEY_BOOTSTRAP, R.string.app_url_assetroot)
    );
  }

  public void setBootstrapFetched(boolean fetched) {
    this.prefs.edit().putBoolean(PREFKEY_RES_FETCHED, fetched).apply();
  }

  public void setHttpHost(String httpHost) {
    this.prefs.edit().putString(PREFKEY_HTTP_HOST, httpHost).apply();
  }

  public void setHttpPort(String httpPort) {
    this.prefs.edit().putString(PREFKEY_HTTP_PORT, httpPort).apply();
  }

  public void setHttpSSL(boolean ssl) {
    this.prefs.edit().putBoolean(PREFKEY_HTTP_SSL, ssl).apply();
  }

  public void setUsername(String username) {
    this.prefs.edit().putString(PREFKEY_USERNAME, username).apply();
  }

  public void setPassword(String password) {
    this.prefs.edit().putString(PREFKEY_PASSWORD, password).apply();
  }

  public void setIsKiosk(boolean isKiosk) {
    this.prefs.edit().putBoolean(PREFKEY_IS_KIOSK, isKiosk).apply();
  }

  public void setIsImageDownloaded(boolean isImageDownloaded) {
    this.prefs.edit().putBoolean(PREFKEY_IS_IMAGE_DOWNLOADED, isImageDownloaded).apply();
  }

  public void setIsImageLan(boolean isImageLan) {
    this.prefs.edit().putBoolean(PREFKEY_IS_IMAGE_LAN, isImageLan).apply();
  }

  public void setIsTablet(boolean isTablet) {
    this.prefs.edit().putBoolean(PREFKEY_IS_TABLET, isTablet).apply();
  }

  public void setBootstrap(String bootstrap) {
    this.prefs.edit().putString(PREFKEY_BOOTSTRAP, bootstrap).apply();
  }

  public void setFCM(String token) {
    this.prefs.edit().putString(PREFKEY_FCM_TOKEN, token).apply();
  }

  public void setToIgnore(String version) {
    this.prefs.edit().putString(PREFKEY_TO_IGNORE, version).apply();
  }

  public void setRoomSelected(String roomSelected) {
    this.prefs.edit().putString(PREFKEY_ROOM_SELECTED, roomSelected).apply();
  }

  /**
   * Restore previous config data.
   */
  public void restore() {
    this.prefs.edit().putBoolean(PREFKEY_IS_KIOSK, tempIsKiosk).apply();
    this.prefs.edit().putBoolean(PREFKEY_IS_TABLET, tempIsTablet).apply();
    this.prefs.edit().putBoolean(PREFKEY_HTTP_SSL, tempSSL).apply();
    this.prefs.edit().putBoolean(PREFKEY_RES_FETCHED, tempFetched).apply();
    this.prefs.edit().putString(PREFKEY_HTTP_PORT, tempHttpPort).apply();
    this.prefs.edit().putString(PREFKEY_HTTP_HOST, tempHttpHost).apply();
    this.prefs.edit().putString(PREFKEY_PASSWORD, temppassword).apply();
    this.prefs.edit().putString(PREFKEY_USERNAME, tempUsername).apply();
    this.prefs.edit().putString(PREFKEY_BOOTSTRAP, tempBootstrap).apply();
    this.prefs.edit().putString(PREFKEY_ROOM_SELECTED, roomSelected).apply();
    this.prefs.edit().putString(PREFKEY_TO_IGNORE, toIgnore).apply();
    this.prefs.edit().putBoolean(PREFKEY_IS_IMAGE_DOWNLOADED, isImageDownloaded).apply();
    this.prefs.edit().putBoolean(PREFKEY_IS_IMAGE_LAN, isImageLan).apply();
  }

  public void clear() {
    this.prefs.edit()
        .clear()
        .apply();
  }

  /**
   * Override the previous stored config data.
   */
  public void apply() {
    tempIsKiosk = fetchBoolean(PREFKEY_IS_KIOSK);
    tempIsTablet = fetchBoolean(PREFKEY_IS_TABLET);
    tempSSL = fetchBoolean(PREFKEY_HTTP_SSL);
    tempFetched = fetchBoolean(PREFKEY_RES_FETCHED);
    tempHttpPort = fetchString(PREFKEY_HTTP_PORT);
    tempHttpHost = fetchString(PREFKEY_HTTP_HOST);
    temppassword = fetchString(PREFKEY_PASSWORD);
    tempUsername = fetchString(PREFKEY_USERNAME);
    tempBootstrap = fetchString(PREFKEY_BOOTSTRAP);
    roomSelected = fetchString(PREFKEY_ROOM_SELECTED);
    toIgnore = fetchString(PREFKEY_TO_IGNORE);
    isImageDownloaded = fetchBoolean(PREFKEY_IS_IMAGE_DOWNLOADED);
    isImageLan = fetchBoolean(PREFKEY_IS_IMAGE_LAN);
  }

  private boolean fetchBoolean(String key) {
    return this.prefs.getBoolean(key, false);
  }

  private int fetchInteger(String key, int referenceId) {
    int response = Integer.parseInt(this.prefs.getString(key, "-1"));

    if (response == -1) { response = this.context.getResources().getInteger(referenceId); }

    return response;
  }

  private String fetchString(String key, int referenceId) {
    String response = this.prefs.getString(key, null);

    if (response == null) { response = this.context.getString(referenceId); }

    return response;
  }

  private String fetchString(String key) {
    return this.prefs.getString(key, null);
  }

 /* */

  /**
   * Store light algo to be use when app reboots.
   *
   * @param algos ArrayList of Light.Algo class.
   *//*
  public void setLightAlgo(ArrayList<Light.Algo> algos) {
    String set = new Gson().toJson(algos);
    this.prefs.edit().putString(PREFKEY_LIGHT_ALGO, set).apply();
  }

  // To reuse algo list when app is closed.
  public ArrayList<Light.Algo> getLightAlgo() {
    return new Gson().fromJson(this.prefs.getString(PREFKEY_LIGHT_ALGO, null),
        new TypeToken<ArrayList<Light.Algo>>() {
        }.getType());
  }*/

  // Delete algo content to avoid redundancy.
  public void deleteAlgo() {
    this.prefs.edit().remove(PREFKEY_LIGHT_ALGO).apply();
  }
}
