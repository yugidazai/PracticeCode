package assessment.duc.channelrating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import assessment.duc.channelrating.model.ProgramEntry;

public class JSONParser {

	public static final int INVALID_COUNT = -1;
	private static final String JSON_KEY_RESULTS = "results";
	private static final String JSON_KEY_TOTAL_ITEMS = "count";
	private static final String JSON_ITEM_KEY_NAME = "name";
	private static final String JSON_ITEM_KEY_START_TIME = "start_time";
	private static final String JSON_ITEM_KEY_END_TIME = "end_time";
	private static final String JSON_ITEM_KEY_CHANNEL = "channel";
	private static final String JSON_ITEM_KEY_RATING = "rating";

	public static List<ProgramEntry> parseResult(JSONObject jsonObj) {
		List<ProgramEntry> programList = new ArrayList<ProgramEntry>();
		if (jsonObj != null) {
			try {
				/* a jsonObj contain data like this:
				 * {
				 * 		"results":[
				 * 			{"name":"name1","start_time":"s_time1","end_time":"e_time1","channel":"channel1","rating":"M"}
				 * 			{"name":"name2","start_time":"s_time2","end_time":"e_time2","channel":"channel2","rating":"M"}
				 * 			{"name":"name3","start_time":"s_time3","end_time":"e_time3","channel":"channel3","rating":"M"}
				 * 			{...}
				 *			... 				
				 * 		],
				 * 		"count":28
				 * }
				 * We parse the key "results" to get the list of programs' info,
				 * then parse each sub item to get all values about a program item:
				 * name, start time, end time, channel, rating*/
				JSONArray dataJsonResult = jsonObj.getJSONArray(JSON_KEY_RESULTS);

				if (dataJsonResult != null) {
					// loop through all items of programs
					for (int i = 0; i < dataJsonResult.length(); i++) {
						JSONObject jsonProgramItem = dataJsonResult.getJSONObject(i);

						// Storing all json values of jsonProgramItem in a ProgramEntry instance
						// {
						//	"name":"name1"
						//	,"start_time":"s_time1"
						//	,"end_time":"e_time1"
						//	,"channel":"channel1"
						//	,"rating":"M"
						// }
						ProgramEntry programEntry = new ProgramEntry(
								jsonProgramItem.getString(JSON_ITEM_KEY_NAME)
								, jsonProgramItem.getString(JSON_ITEM_KEY_START_TIME)
								, jsonProgramItem.getString(JSON_ITEM_KEY_END_TIME)
								, jsonProgramItem.getString(JSON_ITEM_KEY_CHANNEL)
								, jsonProgramItem.getString(JSON_ITEM_KEY_RATING));
						programList.add(programEntry);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return programList;
	}

	/* retrieve the "count" value as the number of all items*/
    public static int parseTotalItemsCount(JSONObject jsonObj) {
    	try {
			return jsonObj.getInt(JSON_KEY_TOTAL_ITEMS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return INVALID_COUNT;
    }
}
