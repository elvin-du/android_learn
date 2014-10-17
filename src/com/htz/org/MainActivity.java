package com.htz.org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.htz.org.R;

public class MainActivity extends ActionBarActivity {
	Button btnSearch;
	EditText etSearch;
	TextView tvSearchResult;
	Button btnChat;
	
	String URL = "http://10.32.5.47:8888/home";
	
	private MyTask mTask;  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnSearch = (Button)findViewById(R.id.btn_search);
        btnChat = (Button)findViewById(R.id.btn_chat);
        etSearch = (EditText)findViewById(R.id.et_search);
        tvSearchResult = (TextView)findViewById(R.id.tv_search_result);
        
        btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String search = etSearch.getText().toString();
//                mTask = new MyTask();  
//                mTask.execute(URL,"key="+search); 
//                btnSearch.setEnabled(false); 
			}
		});
        
        btnChat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,ChatActivity.class);
				startActivity(intent);
			}
		});
    }
    
    public class MyTask extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... params) {
			Log.i("sync task","doInBackground(String... arg0) ");
			HttpClient httpClient = new DefaultHttpClient();  
			Log.i("Syanc task","params"+params[0]+params[1]);
			String[] ps = params[1].split("=");
			if (ps.length <2){
				Log.d("htpp", "split failed");
				return "split failed";
			}
			
//			List<BasicNameValuePair> l = new LinkedList<BasicNameValuePair>();
//			l.add(new BasicNameValuePair(ps[0],ps[1]));
//			//对参数编码  
//			String param = URLEncodedUtils.format(l, "UTF-8");
			
			HttpGet httpGet = new HttpGet(params[0]+"?"+ps[0]+"="+ps[1]);
			try {
				HttpResponse response =  httpClient.execute(httpGet);
				String res =  EntityUtils.toString(response.getEntity());
				return res;				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
        @Override  
        protected void onPostExecute(String result) {  
            Log.i("sync task", "onPostExecute(Result result) called"+result);
          
			try {
				JSONObject obj = new JSONObject(result);
				tvSearchResult.setText(obj.getString("result"));  
	            btnSearch.setEnabled(true);  
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }  
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
