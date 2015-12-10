package assessment.duc.channelrating;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONDownloader {

	private static final String TAG = JSONDownloader.class.getSimpleName();

	/* From given API with offset value,
	 * open HttpURLConnection of this API to retrieve data as a JSON object*/
	public static JSONObject getJSONObjectFromApi(String api) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(api);
			connection = (HttpURLConnection) url.openConnection();

			if (connection != null) {
				if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAVAILABLE) {
					//retry if there is problem with connection
					return getJSONObjectFromApi(api);
				}
				// get data stream from URL then read data by using BufferReader
				InputStream inputStream = connection.getInputStream();
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));

				// StringBuilder to store the retrieved JSON string from data stream
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = buffReader.readLine()) != null) {
					sb.append(line);
				}

				buffReader.close();
				inputStream.close();

				return new JSONObject(sb.toString());
			}
		} catch (JSONException e) {
			Log.e(TAG, "Cannot parse data string to JSON object: " + e.toString());
		} catch (MalformedURLException e) {
			Log.e(TAG, "Wrong format URL:" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return null;
	}
}
