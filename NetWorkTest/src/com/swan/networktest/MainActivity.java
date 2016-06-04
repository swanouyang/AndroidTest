package com.swan.networktest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
//import java.util.jar.Attributes;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	public static final int SHOW_RESPONSE = 0;

	private static final int PARSE_XML_PULL = 1;
	private static final int PARSE_XML_SAX = 2;
	private static final int PARSE_JSON_JSONOBJECT = 3;
	private static final int PARSE_JSON_GSON = 4;

	private Button hucParseBaidu;
	private Button hcParseBaidu;
	private Button hucParseXmlWithPull;
	private Button hucParseXmlWithSAX;
	private Button hucParseJSONWithJSONObject;
	private Button hucParseJSONWithGSON;

	private TextView responseText;

	private int parseType = PARSE_XML_PULL;
	private HttpCallbackListener httpCallbackListener = null;

	String baiduAddress = "http://www.baidu.com";
	// the address of ipv4 can be found with command "ipconfig" in cmd.exe
	String xmlAddress = "http://192.168.1.101:90/XML/get_data.xml";
	String jsonAddress = "http://192.168.1.101:90/JSON/get_data.json";

	StringBuilder stringBuilder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		hucParseBaidu = (Button) findViewById(R.id.hucParseBaidu);
		hucParseBaidu.setOnClickListener(this);
		hcParseBaidu = (Button) findViewById(R.id.hcParseBaidu);
		hcParseBaidu.setOnClickListener(this);
		hucParseXmlWithPull = (Button) findViewById(R.id.hucParseXmlWithPull);
		hucParseXmlWithPull.setOnClickListener(this);
		hucParseXmlWithSAX = (Button) findViewById(R.id.hucParseXmlWithSAX);
		hucParseXmlWithSAX.setOnClickListener(this);
		hucParseJSONWithJSONObject = (Button) findViewById(R.id.hucParseJSONWithJSONObject);
		hucParseJSONWithJSONObject.setOnClickListener(this);
		hucParseJSONWithGSON = (Button) findViewById(R.id.hucParseJSONWithGSON);
		hucParseJSONWithGSON.setOnClickListener(this);

		responseText = (TextView) findViewById(R.id.reponse);
		stringBuilder = new StringBuilder();

		httpCallbackListener = new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				switch (parseType) {
				case PARSE_XML_PULL:
					parseXMLWithPull(response);
					break;
				case PARSE_XML_SAX:
					parseXMLWithSAX(response);
					break;
				case PARSE_JSON_JSONOBJECT:
					parseJSONWithJSONObject(response);
					break;
				case PARSE_JSON_GSON:
					parseJSONWithGSON(response);
					break;
				default:
					break;
				}
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hucParseBaidu: {
			sendRequestWithHttpURLConnection();
			break;
		}
		case R.id.hcParseBaidu: {
			sendRequestWithHttpClient();
			break;
		}
		case R.id.hucParseXmlWithPull: {
			parseType = PARSE_XML_PULL;
			HttpUtil.sendHttpRequest(xmlAddress, httpCallbackListener);
			break;
		}
		case R.id.hucParseXmlWithSAX: {
			parseType = PARSE_XML_SAX;
			HttpUtil.sendHttpRequest(xmlAddress, httpCallbackListener);
			break;
		}
		case R.id.hucParseJSONWithJSONObject: {
			parseType = PARSE_JSON_JSONOBJECT;
			HttpUtil.sendHttpRequest(jsonAddress, httpCallbackListener);
			break;
		}
		case R.id.hucParseJSONWithGSON: {
			parseType = PARSE_JSON_GSON;
			HttpUtil.sendHttpRequest(jsonAddress, httpCallbackListener);
			break;
		}
		default:
			break;
		}
	}

	private void sendRequestWithHttpURLConnection() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(baiduAddress);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					Message msg = new Message();
					msg.what = SHOW_RESPONSE;
					msg.obj = response.toString();
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				String response = (String) msg.obj;
				responseText.setText(response);
				break;

			default:
				break;
			}
		}
	};

	private void sendRequestWithHttpClient() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(baiduAddress);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						String response = EntityUtils.toString(httpEntity,
								"utf-8");
						Message msg = new Message();
						msg.what = SHOW_RESPONSE;
						msg.obj = response.toString();
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void parseXMLWithPull(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlData));

			int eventType = xmlPullParser.getEventType();
			String id = "";
			String name = "";
			String version = "";
			Log.d("MainActivity", "----Pull Start----");
			stringBuilder.setLength(0);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG: {
					if ("id".equals(nodeName)) {
						id = xmlPullParser.nextText();
					} else if ("name".equals(nodeName)) {
						name = xmlPullParser.nextText();
					} else if ("version".equals(nodeName)) {
						version = xmlPullParser.nextText();
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					if ("app".equals(nodeName)) {
						stringBuilder.append("\nid is ");
						stringBuilder.append(id);
						stringBuilder.append("\nname is ");
						stringBuilder.append(name);
						stringBuilder.append("\nversion is");
						stringBuilder.append(version);
						Log.d("MainActivity", "id is " + id);
						Log.d("MainActivity", "name is " + name);
						Log.d("MainActivity", "version is " + version);
					}
					break;
				}
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
			sendMessage();
			Log.d("MainActivity", "----Pull End----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseXMLWithSAX(String xmlData) {
		try {
			stringBuilder.setLength(0);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			ContentHandler handler = new ContentHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(xmlData)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class ContentHandler extends DefaultHandler {
		private String nodeName;
		private StringBuilder id;
		private StringBuilder name;
		private StringBuilder version;
		private boolean isStart = false;

		@Override
		public void startDocument() throws SAXException {
			Log.d("MainActivity", "----SAX Start----");
			id = new StringBuilder();
			name = new StringBuilder();
			version = new StringBuilder();
		}

		@Override
		public void endDocument() throws SAXException {
			sendMessage();
			Log.d("MainActivity", "----SAX End----");
		}

		// Override error: import java.util.jar.Attributes;
		// Have you imported the right Attributes class?
		// You would see this error if you'd imported any Attributes class
		// other than org.xml.sax.Attributes.
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attibutes) throws SAXException {
			nodeName = localName;
			isStart = true;
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			isStart = false;
			if ("app".equals(localName)) {
				stringBuilder.append("\nid is ");
				stringBuilder.append(id);
				stringBuilder.append("\nname is ");
				stringBuilder.append(name);
				stringBuilder.append("\nversion is");
				stringBuilder.append(version);
				Log.d("MainActivity", "id is " + id);
				Log.d("MainActivity", "name is " + name);
				Log.d("MainActivity", "version is " + version);

				id.setLength(0);
				name.setLength(0);
				version.setLength(0);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (!isStart) {
				return;
			}
			if ("id".equals(nodeName)) {
				id.append(ch, start, length);
			} else if ("name".equals(nodeName)) {
				name.append(ch, start, length);
			} else if ("version".equals(nodeName)) {
				version.append(ch, start, length);
			}
		}
	}

	private void parseJSONWithJSONObject(String jsonData) {
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			stringBuilder.setLength(0);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				String version = jsonObject.getString("version");

				stringBuilder.append("\nid is ");
				stringBuilder.append(id);
				stringBuilder.append("\nname is ");
				stringBuilder.append(name);
				stringBuilder.append("\nversion is");
				stringBuilder.append(version);
				Log.d("MainActivity", "id is " + id);
				Log.d("MainActivity", "name is " + name);
				Log.d("MainActivity", "version is " + version);
			}
			sendMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class App {
		private String id;
		private String name;
		private String version;

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

	private void parseJSONWithGSON(String jsonData) {
		Gson gson = new Gson();
		List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
		}.getType());

		stringBuilder.setLength(0);
		for (App app : appList) {
			stringBuilder.append("\nid is ");
			stringBuilder.append(app.getId());
			stringBuilder.append("\nname is ");
			stringBuilder.append(app.getName());
			stringBuilder.append("\nversion is");
			stringBuilder.append(app.getVersion());
			Log.d("MainActivity", "id is " + app.getId());
			Log.d("MainActivity", "name is " + app.getName());
			Log.d("MainActivity", "version is " + app.getVersion());
		}
		
		sendMessage();
	}
	
	private void sendMessage() {
		Message msg = new Message();
		msg.what = SHOW_RESPONSE;
		msg.obj = stringBuilder.toString();
		handler.sendMessage(msg);
	}
}


