package com.example.jsonwebserviceapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void showInfo(View v) {
		new MyAsyncTask().execute("http://graph.facebook.com/kapilTaneja");
	}

	class MyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String str = "";
			try {
				InputStream in = OpenHttpConncetion(params[0]);
				InputStreamReader insr = new InputStreamReader(in);

				int i = 0;
				while ((i = insr.read()) != -1) {
					str = str + (char) i;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Data : " + result,
					Toast.LENGTH_SHORT).show();

			// Extract JSON Data

			JSONObject jsono;
			try {
				jsono = new JSONObject(result);

				String fname = jsono.getString("first_name");

				String link = jsono.getString("link");

				Toast.makeText(getBaseContext(), "F Name " + fname,
						Toast.LENGTH_SHORT).show();

				Toast.makeText(getBaseContext(), "Link " + link,
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				Toast.makeText(getBaseContext(),
						"JSON Exception " + e.getMessage(), Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}

	private InputStream OpenHttpConncetion(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;
		URL url = new URL(urlString);

		URLConnection conn = url.openConnection();
		if (!(conn instanceof HttpURLConnection)) {
			throw new IOException();
		}

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception e) {
			Log.d("Gufran Neworking", e.getLocalizedMessage());
			throw new IOException("Error Connecting");
		}
		return in;

	}
}
