package com.wordhunter.huntword.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;






/**
 * A JSON parsing utility. Can be used in any app where we need to
 * parse a JSON document.
 *
 * @author swagataacharyya
 *
 */
public class JsonParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JsonParser() {

	}

	public JSONObject getJSONFromUrl(String url) {
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpPost = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			} catch (ClientProtocolException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line.trim() + "\n");
			}
			is.close();
			json = sb.toString().trim();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		// return JSON String
		return jObj;

	}
}