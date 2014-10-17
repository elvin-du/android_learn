package com.htz.org;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONException;
import org.json.JSONObject;

import com.htz.org.R;
import com.htz.org.MainActivity.MyTask;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends ActionBarActivity {
	EditText etChat;
	Button btnSend;
	XMPPConnection connection;
	TextView tvChatHistory;

	private Chat chat;
	private Handler mHandle = new Handler();
	// mConnectingThread;
	// private MyTask mTask;

	private Thread mConnectingThread = new Thread() {
		@Override
		public void run() {
			ConnectionConfiguration mConfig = new ConnectionConfiguration(
					"10.32.5.47", 5222);
			mConfig.setSASLAuthenticationEnabled(false);
			mConfig.setSecurityMode(SecurityMode.disabled);
			mConfig.setReconnectionAllowed(true);
			mConfig.setCompressionEnabled(false);
			mConfig.setDebuggerEnabled(true);
			mConfig.setSendPresence(true);

			if (Build.VERSION.SDK_INT >= 14) {
				mConfig.setTruststoreType("AndroidCAStore");
				mConfig.setTruststorePassword(null);
				mConfig.setTruststorePath(null);
			} else {
				mConfig.setTruststorePath("/system/etc/security/cacerts.bks");
				mConfig.setTruststoreType("BKS");
			}
			connection = new XMPPConnection(mConfig);

			try {
				connection.connect();
				Log.i("ok", "connect ok");
			} catch (XMPPException e1) {
				e1.printStackTrace();
			}
			try {
				connection.login("admin", "JTabc.123");
				Presence packet = new Presence(Presence.Type.available);
				connection.sendPacket(packet);

				chat = connection.getChatManager().createChat(
						"otest@nick-yunio/Smack", listener);

				Log.i("ok", "login ok");
			} catch (XMPPException e1) {
				e1.printStackTrace();
			}
		}
	};

	MessageListener listener = new MessageListener() {
		public void processMessage(Chat chat, final Message message) {
			if (null != message.getBody()) {
				Log.i("Received message from: ", message.getFrom());
				Log.i("Received message: ", message.getBody());
			}
			mHandle.post(new Runnable() {

				@Override
				public void run() {
					tvChatHistory.setText(message.getBody());
				}

			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		tvChatHistory = (TextView) findViewById(R.id.tv_chat_history);
		btnSend = (Button) findViewById(R.id.btn_send);
		etChat = (EditText) findViewById(R.id.et_chat);

		mConnectingThread.start();

		// 使用匿名类注册Button事件
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String content = etChat.getText().toString();
				// mTask = new MyTask();
				// mTask.execute(content);
				Log.i("send message", content);
				Message m = new Message();
				m.setBody(content);
				try {
					chat.sendMessage(m);
					Log.i("ok", "sendmessage success");
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// private class MyTask extends AsyncTask<String,Integer,String>{
	// private Chat chat;
	//
	// @Override
	// protected String doInBackground(String... params) {
	// Log.i("sync task","doInBackground(String... arg0) ");
	// ConnectionConfiguration config = new
	// ConnectionConfiguration("10.32.5.47",5222);
	// config.setDebuggerEnabled(true);
	// XMPPConnection connection = new XMPPConnection(config);
	// try {
	// connection.connect();
	// Log.i("ok","connect ok");
	// } catch (XMPPException e1) {
	// e1.printStackTrace();
	// }
	// try {
	// connection.login("otest", "JTabc.123");
	//
	// Message m = new Message();
	// m.setBody(params[0]);
	//
	// chat = connection.getChatManager().createChat("admin@localhost", new
	// MessageListener() {
	// public void processMessage(Chat chat, Message message) {
	// Log.i("Received message: ",message.toString());
	// TextView tvChatHistory = (TextView)findViewById(R.id.tv_chat_history);
	// tvChatHistory.setText(message.toString());
	// }
	// });
	//
	// try {
	// chat.sendMessage(m);
	// return "sendmessage success";
	// } catch (XMPPException e) {
	// e.printStackTrace();
	// }
	// return "login success";
	// } catch (XMPPException e1) {
	// e1.printStackTrace();
	// }
	//
	// return null;
	// }
	//
	// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	// @Override
	// protected void onPostExecute(String result) {
	// Log.i("sync task", "onPostExecute(Result result) called"+result);
	// }
	// }
}
