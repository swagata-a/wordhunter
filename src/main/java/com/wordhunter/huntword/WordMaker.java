package com.wordhunter.huntword;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wordhunter.huntword.json.JsonParser;

public class WordMaker {
	int vowelIndex = 0;
	int count = 0;
	static String finalWords = "";
	String newString = "";

	public String generateRandomChars(int range) {
		char chars[] = new char[9];
		for (int noOfChars = 0; noOfChars < range; noOfChars++) {
			chars[noOfChars] = (char) (Math.random() * ('Z' - 'A' + 1) + 'A');
		}
		String randomChars = String.valueOf(chars);

		if (countVowels(randomChars) < 2) {
			randomChars = insertVowels(count, vowelIndex, randomChars);
		}
		return randomChars;

	}

	private int countVowels(String randCharacters) {
		for (int noOfChars = 0; noOfChars < randCharacters.length(); noOfChars++) {
			char alphabet = randCharacters.charAt(noOfChars);
			if (alphabet == 'a' || alphabet == 'e' || alphabet == 'i'
					|| alphabet == 'o' || alphabet == 'u' || alphabet == 'A'
					|| alphabet == 'E' || alphabet == 'I' || alphabet == 'O'
					|| alphabet == 'U') {
				count++;
				vowelIndex = 1 + randCharacters.indexOf(alphabet);
			}

		}
		return count;
	}

	private String insertVowels(int vowelsCount, int index,
			String inCompleteString) {
		newString = "";
		if (vowelsCount == 0) // when no vowel present
			newString = "AE" + inCompleteString.substring(2);
		if (vowelsCount == 1) {
			if (index == 1)
				newString = inCompleteString.substring(0, 8) + "E";
			if (index == 9)
				newString = "E" + inCompleteString.substring(1);
			else
				newString = "E" + inCompleteString.substring(1);
		}
		return newString;
	}

	public String getFinalWords(String alphabets) {
		String REST_ENDPOINT = "http://www.anagramica.com/";
		String URI_INFO_PATH = "all";
		String endpoint = REST_ENDPOINT + URI_INFO_PATH + "/" + alphabets;
		System.out.println(alphabets);
		JsonParser parser = new JsonParser();
		JSONObject json = parser.getJSONFromUrl(endpoint);
		JSONArray words = null;
		try {
			words = json.getJSONArray(URI_INFO_PATH);
			for (int i = 0; i < words.length(); i++) {
				if (words.getString(i).length() >= 3) {
					finalWords = finalWords + words.getString(i) + ",";
				}
			}
			finalWords = finalWords.substring(0, finalWords.length() - 1);
			System.out.println(finalWords);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return finalWords;
	}

	public void postData(String words, String key) throws IOException, URISyntaxException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"https://api.usergrid.com/swagata/wordhunter/worddb?access_token=YWMt1wMP0ObZEeKnyaNe-D2j8AAAAT_cKHVNQ_178tJL1Q32qa01VhQzEy7YSu8");
		post.setHeader("Content-type", "application/json; charset=utf-8");
		try {
			StringEntity input = new StringEntity("{\"key\":\"" + key
					+ "\",\"words\":\"" + words + "\"}");
			input.setContentType("application/json");
			post.setEntity(input);
			HttpResponse response = client.execute(post);
			System.out.println(response.getStatusLine());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
//Check whether the key is already available. If available, then dont store
		WordMaker randChar = new WordMaker();
		String randomSeq = randChar.generateRandomChars(9);
		randChar.getFinalWords(randomSeq);
		randChar.postData(finalWords, randomSeq);

	}
}
