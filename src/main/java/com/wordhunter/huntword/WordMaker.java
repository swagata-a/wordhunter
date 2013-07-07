package com.wordhunter.huntword;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

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
	
	final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String generateRandomChars(int range) {
		int length = alphabet.length();
		Random r = new Random();
		StringBuilder randomCharsStr = new StringBuilder();
		while (randomCharsStr.length() != range) {
			String c = alphabet.charAt(r.nextInt(length)) + "";
			if (!randomCharsStr.toString().contains(c)) {
				randomCharsStr.append(c);
			}
		}
		String randomChars = randomCharsStr.toString();
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
			String incompleteString) {
		String newString = "";
		if (vowelsCount == 0) { // when no vowel present
			newString = "AE" + incompleteString.substring(2);
		} else if (vowelsCount == 1) {
			if (!isVowelPresent("E", incompleteString)) {
				newString = getFinalString(index, incompleteString, "E");
			} else if (!isVowelPresent("A", incompleteString)) {
				newString = getFinalString(index, incompleteString, "A");
			} else if (!isVowelPresent("I", incompleteString)) {
				newString = getFinalString(index, incompleteString, "I");
			}
		}
		return newString;
	}

	private String getFinalString(int index, String originalString,
			String replacement) {
		StringBuilder newStringBuilder = new StringBuilder();
		if (index == 1) {
			newStringBuilder.append(originalString.substring(0, 8)).append(
					replacement);

		}
		if (index == 9) {
			newStringBuilder.append(replacement).append(
					originalString.substring(1));
		} else {
			newStringBuilder.append(replacement).append(
					originalString.substring(1));
		}
		return newStringBuilder.toString();
	}

	private boolean isVowelPresent(String vowel, String text) {
		return (text.contains(vowel));
	}

	public String getFinalWords(String alphabets) {
		String finalWords = "";
		String REST_ENDPOINT = "http://www.anagramica.com/";
		String URI_INFO_PATH = "all";
		String endpoint = REST_ENDPOINT + URI_INFO_PATH + "/" + alphabets;
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
//			finalWords = finalWords.substring(0, finalWords.length() - 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return finalWords;
	}

	public void postData(String words, String key) throws IOException,
			URISyntaxException {
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
		// Check whether the key is already available. If available, then dont
		// store
		WordMaker randChar = new WordMaker();
		int count = 0;
		while(true){
			String randomSeq = randChar.generateRandomChars(9);
			System.out.println(randomSeq);
			String words = randChar.getFinalWords(randomSeq);
			if (words.split(",").length > 10) {
				randChar.postData(words, randomSeq);
				System.out.println(count);
				count++;
			}
		}

	}
}
