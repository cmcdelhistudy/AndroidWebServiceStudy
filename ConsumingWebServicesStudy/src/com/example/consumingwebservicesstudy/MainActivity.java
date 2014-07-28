package com.example.consumingwebservicesstudy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText edWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		edWord = (EditText) findViewById(R.id.editText1);

	}

	public void defineWord(View v) {
		String st = edWord.getText().toString();
		 new AccessWebServiceTask().execute(st);
		// new MyAsyncTask()
		// .execute("http://services.aonaware.com/DictService/DictService.asmx/Define?word=Ritual");

	}


	private String WordDefinition(String word) {

		InputStream in = null;
		String strDefinition = "";

		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document doc = null;

		try {

			in = OpenHttpConncetion("http://services.aonaware.com/DictService/DictService.asmx/Define?word="
					+ word);

			dbf = DocumentBuilderFactory.newInstance();

			try {

				db = dbf.newDocumentBuilder();
				doc = db.parse(in);

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//doc.getDocumentElement().normalize();

			// retriveve all the <Definition> elements
			NodeList definitionElements = doc
					.getElementsByTagName("Definition");

			// iterate over each <Definition> element
			for (int i = 0; i < definitionElements.getLength(); i++) {
				Node itemNode = definitionElements.item(i);

				if (itemNode.getNodeType() == Node.ELEMENT_NODE) {

					// convert the Definition node into an Element
					Element definitionElement = (Element) itemNode;

					// get all the <WordDefinition> elemnts under the
					// <Definition>
					// element
					NodeList wordDefinitionElements = definitionElement
							.getElementsByTagName("WordDefinition");

					strDefinition = "";

					// iterate through each <WordDefinition> elements
					for (int j = 0; j < wordDefinitionElements.getLength(); j++) {

						// convert a <WordDefinition> node into an Element
						Element wordDefinitionElement = (Element) wordDefinitionElements
								.item(j);

						// get all the child nodes under the <WordDefinition>
						NodeList textNodes = ((Node) wordDefinitionElement)
								.getChildNodes();

						strDefinition += ((Node) textNodes.item(0))
								.getNodeValue() + ".\n";

					}

				}
			}
		} catch (IOException ioe) {
			Log.d("Gufran ", ioe.getLocalizedMessage());
		}

		return strDefinition;
	}

	private class AccessWebServiceTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

			return WordDefinition(urls[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
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

	// class MyAsyncTask extends AsyncTask<String, Void, String> {
	//
	// @Override
	// protected String doInBackground(String... params) {
	// String str = "";
	// try {
	// InputStream in = OpenHttpConncetion(params[0]);
	// InputStreamReader insr = new InputStreamReader(in);
	//
	// int i = 0;
	// while ((i = insr.read()) != -1) {
	// str = str + (char) i;
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return str;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// Toast.makeText(getBaseContext(), "Data : " + result,
	// Toast.LENGTH_SHORT).show();
	// // Parsing of XML
	//
	// try {
	// DocumentBuilderFactory dbf = DocumentBuilderFactory
	// .newInstance();
	// DocumentBuilder db = dbf.newDocumentBuilder();
	// Document doc = db.parse(result);
	//
	// NodeList nl = doc.getElementsByTagName("Definitions");
	//
	// for (int i = 0; i < nl.getLength(); i++) {
	//
	// Node n = (Node) nl.item(i);
	//
	// n.get
	//
	// }
	//
	// } catch (ParserConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SAXException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// super.onPostExecute(result);
	// }
	// }

	
}
