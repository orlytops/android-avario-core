package com.avario.core.websockets;

import com.avario.core.Config;
import com.avario.core.interfaces.BootstrapListener;
import com.avario.core.interfaces.StateChangeListener;
import com.avario.core.interfaces.StateListener;
import com.avario.core.models.RequestEvent;
import com.avario.core.models.calls.ServicePost;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.StatusLine;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.eclipse.paho.client.mqttv3.internal.websocket.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import timber.log.Timber;

/**
 * Created by orly on 1/8/18.
 */

public class AvarioWebSocket {

  private static final String TAG = "SocketIO";

  private static final String TYPE   = "type";
  private static final String ID     = "id";
  private static final String RESULT = "result";

  private static final String AUTH_OK = "auth_ok";

  private static final int GET_BOOTSTRAP_ID    = 1;
  private static final int GET_STATES_ID       = 3;
  private static final int GET_STATE_CHANGE_ID = 15;

  private static final String GET_BOOTSTRAP_TYPE          = "get_boot_strap";
  private static final String GET_STATES_TYPE             = "get_states";
  private static final String GET_STATE_CHANGE_EVENT_TYPE = "state_changed";
  private static final String GET_STATE_CHANGE_TYPE       = "subscribe_events";

  private static AvarioWebSocket instance = null;

  private WebSocket webSocket;
  private Gson      gson;

  private StateListener       stateListener;
  private BootstrapListener   bootstrapListener;
  private StateChangeListener stateChangeListener;

  private int requestCount = 11;

  public static AvarioWebSocket getInstance() {
    if (AvarioWebSocket.instance == null) {
      AvarioWebSocket.instance = new AvarioWebSocket();
    }
    return AvarioWebSocket.instance;
  }

  private AvarioWebSocket() {

  }

  private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return new java.security.cert.X509Certificate[]{};
    }

    public void checkClientTrusted(X509Certificate[] chain,
        String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain,
        String authType) throws CertificateException {
    }
  }};


  public AvarioWebSocket start() {
    gson = new Gson();
    WebSocketFactory factory = new WebSocketFactory();

    if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 9) {
      try {
        // StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        Class<?> strictModeClass = Class.forName("android.os.StrictMode", true,
            Thread.currentThread()
                .getContextClassLoader());
        Class<?> threadPolicyClass = Class.forName("android.os.StrictMode$ThreadPolicy", true,
            Thread.currentThread()
                .getContextClassLoader());
        Field laxField = threadPolicyClass.getField("LAX");
        Method setThreadPolicyMethod = strictModeClass.getMethod("setThreadPolicy",
            threadPolicyClass);
        setThreadPolicyMethod.invoke(strictModeClass, laxField.get(null));
      } catch (Exception e) { }
    }

    try {
      SSLContext context = NaiveSSLContext.getInstance("TLS");
      context.init(null, trustAllCerts, null);
      factory.setSSLContext(context);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }

    factory.setVerifyHostname(false);

    try {
      webSocket = factory.createSocket("wss://192.168.0.18:22443/api/websocket");
    } catch (IOException e) {
      e.printStackTrace();
    }

    webSocket.addListener(new WebSocketAdapter() {
      @Override
      public void onConnected(WebSocket websocket,
          Map<String, List<String>> headers)
          throws Exception {
        Timber.d("WebSocket connected");
        requestCount = 11;
      }

      @Override
      public void onConnectError(WebSocket websocket,
          WebSocketException exception)
          throws Exception {
        Timber.d("WebSocket error: %s", exception.getMessage());
      }

      @Override
      public void onTextMessage(WebSocket socket, String text)
          throws Exception {
        handleMessages(socket, text);
      }

      @Override
      public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame)
          throws Exception {
        Timber.e("Send Error: %s", cause.getMessage());
      }

      @Override
      public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        Timber.e("Error: %s", cause.getMessage());
      }

      @Override
      public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data)
          throws Exception {
        Timber.e("Text message Error: %s", cause.getMessage());
      }

      @Override
      public void onMessageError(WebSocket websocket, WebSocketException cause,
          List<WebSocketFrame> frames) throws Exception {
        Timber.e("Message Error: %s", cause.getMessage());
      }

      @Override
      public void onUnexpectedError(WebSocket websocket, WebSocketException cause)
          throws Exception {
        Timber.e("Unexpected Error: %s", cause.getMessage());
      }

      @Override
      public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
          WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        Timber.e("Websocket Disconnected");
        Timber.e("Websocket close by server: %s", closedByServer);
        if (closedByServer) {
          Timber.e("Webscoket server close reason: %s", serverCloseFrame.getCloseReason());
        } else {
          Timber.e("Webscoket client close reason: %s", clientCloseFrame.getCloseReason());
        }
        start();
      }
    });
    Config config = Config.getInstance();

    webSocket.addHeader("Authorization", String.format("Basic %s", Base64.encode(String.format(
        "%s:%s",
        config.getUsername(),
        config.getPassword()
    ))));

    connectWebSocket();

    return this;
  }

  private void connectWebSocket() {
    try {
      webSocket.connect();
    } catch (OpeningHandshakeException e) {
      // A violation against the WebSocket protocol was detected
      // during the opening handshake.
      // Status line.
      StatusLine sl = e.getStatusLine();
      System.out.println("=== Status Line ===");
      System.out.format("HTTP Version  = %s\n", sl.getHttpVersion());
      System.out.format("Status Code   = %d\n", sl.getStatusCode());
      System.out.format("Reason Phrase = %s\n", sl.getReasonPhrase());

      // HTTP headers.
      Map<String, List<String>> headers = e.getHeaders();
      System.out.println("=== HTTP Headers ===");
      for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
        // Header name.
        String name = entry.getKey();

        // Values of the header.
        List<String> values = entry.getValue();

        if (values == null || values.size() == 0) {
          // Print the name only.
          System.out.println(name);
          continue;
        }

        for (String value : values) {
          // Print the name and the value.
          System.out.format("%s: %s\n", name, value);
        }
      }
    } catch (WebSocketException e) {
      // Failed to establish a WebSocket connection.
      Timber.e("WebSocketException: %s", e.getMessage());
      start();
    }
  }

  private void handleMessages(WebSocket socket, String text)
      throws JSONException {

    JSONObject messageJson = new JSONObject(text);

    Timber.i("Type =====> %s", messageJson.getString(TYPE));
    switch (messageJson.getString(TYPE)) {
    case AUTH_OK:
      Timber.i("Authentication Successful");
      RequestEvent requestEvent = new RequestEvent();
      requestEvent.setId(GET_STATE_CHANGE_ID);
      requestEvent.setType(GET_STATE_CHANGE_TYPE);
      requestEvent.setEventType(GET_STATE_CHANGE_EVENT_TYPE);
      webSocket.sendText(gson.toJson(requestEvent));
      break;
    }
    Timber.i("Message =====> %s", messageJson.toString());
    Timber.i("ID =====> %s", messageJson.getInt(ID));
    switch (messageJson.getInt(ID)) {
    case GET_STATES_ID: {
      stateListener.onResponse(messageJson.getJSONArray(RESULT));
    }
    if (stateListener != null) { break; }

    case GET_BOOTSTRAP_ID:
      if (bootstrapListener != null) {
        bootstrapListener.onResponse(messageJson.getJSONObject(RESULT));
      }
      break;

    case GET_STATE_CHANGE_ID:
      if (stateChangeListener != null) {
        Timber.d("State change ========> %s", messageJson.toString());
        stateChangeListener.onResponse(messageJson);
      }
      break;
    }

  }

  public void postRequest(ServicePost servicePost) {
    servicePost.setId(requestCount++);
    Timber.i("Request =============> %s", servicePost.toJson());
    webSocket.sendText(servicePost.toJson());

  }

  //SETTERS AND GETTERS
  public void getStates(StateListener stateListener) {
    this.stateListener = stateListener;
    RequestEvent requestEvent = new RequestEvent();
    requestEvent.setId(GET_STATES_ID);
    requestEvent.setType(GET_STATES_TYPE);
    webSocket.sendText(gson.toJson(requestEvent));
  }

  public void getBootstrap(BootstrapListener bootstrapListener) {
    this.bootstrapListener = bootstrapListener;
    RequestEvent requestEvent = new RequestEvent();
    requestEvent.setId(GET_BOOTSTRAP_ID);
    requestEvent.setType(GET_BOOTSTRAP_TYPE);
    //webSocket.sendText(gson.toJson(requestEvent));
  }

  public void setStateChangeListener(StateChangeListener stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }

  public WebSocket getWebSocket() {
    return webSocket;
  }
}