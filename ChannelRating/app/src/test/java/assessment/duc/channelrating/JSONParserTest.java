package assessment.duc.channelrating;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;

import assessment.duc.channelrating.model.ProgramEntry;

@RunWith(MockitoJUnitRunner.class)
public class JSONParserTest {

    private static final int INVALID_COUNT = -1;
    private static final String JSON_KEY_RESULTS = "results";
    private static final String JSON_KEY_TOTAL_ITEMS = "count";
    private static final String JSON_ITEM_KEY_NAME = "name";
    private static final String JSON_ITEM_KEY_START_TIME = "start_time";
    private static final String JSON_ITEM_KEY_END_TIME = "end_time";
    private static final String JSON_ITEM_KEY_CHANNEL = "channel";
    private static final String JSON_ITEM_KEY_RATING = "rating";

    @Mock
    JSONObject mMockJSONObj;

    @Mock
    JSONArray mMockJSONArray;

    @Mock
    JSONObject mMockJSONObjItem;

    @Test
    public void parseResult_ReturnsEmptyListWithNullJSONObj() throws Exception {
        List<ProgramEntry> result = JSONParser.parseResult(null);

        assertThat(result.size(), is(0));
    }

    @Test
    public void parseResult_ReturnsEmptyListWithNoJSONArray() throws Exception {
        when(mMockJSONObj.getJSONArray(JSON_KEY_RESULTS)).thenReturn(null);

        List<ProgramEntry> result = JSONParser.parseResult(mMockJSONObj);

        assertThat(result.size(), is(0));
    }

    @Test
    public void parseResult_ReturnsCorrectResult() throws Exception {
        //prepare test data for a program item
        HashMap<String, String> testJSONItem = new HashMap<String, String>();
        testJSONItem.put(JSON_ITEM_KEY_NAME, "name1");
        testJSONItem.put(JSON_ITEM_KEY_START_TIME, "stime1");
        testJSONItem.put(JSON_ITEM_KEY_END_TIME, "etime1");
        testJSONItem.put(JSON_ITEM_KEY_CHANNEL, "channel1");
        testJSONItem.put(JSON_ITEM_KEY_RATING, "M");

        // mock JSON Object with 1 program item
        when(mMockJSONObj.getJSONArray(JSON_KEY_RESULTS)).thenReturn(mMockJSONArray);
        when(mMockJSONArray.length()).thenReturn(1);
        when(mMockJSONArray.getJSONObject(anyInt())).thenReturn(mMockJSONObjItem);
        when(mMockJSONObjItem.getString(JSON_ITEM_KEY_NAME))
                .thenReturn(testJSONItem.get(JSON_ITEM_KEY_NAME));
        when(mMockJSONObjItem.getString(JSON_ITEM_KEY_START_TIME))
                .thenReturn(testJSONItem.get(JSON_ITEM_KEY_START_TIME));
        when(mMockJSONObjItem.getString(JSON_ITEM_KEY_END_TIME))
                .thenReturn(testJSONItem.get(JSON_ITEM_KEY_END_TIME));
        when(mMockJSONObjItem.getString(JSON_ITEM_KEY_CHANNEL))
                .thenReturn(testJSONItem.get(JSON_ITEM_KEY_CHANNEL));
        when(mMockJSONObjItem.getString(JSON_ITEM_KEY_RATING))
                .thenReturn(testJSONItem.get(JSON_ITEM_KEY_RATING));

        List<ProgramEntry> result = JSONParser.parseResult(mMockJSONObj);

        assertThat(result.size(), is(1));
        assertCorrectJSONItem(result.get(0), testJSONItem);
    }

    @Test
    public void parseTotalItemsCount_InvalidCountValue() throws Exception {
        when(mMockJSONObj.getInt(JSON_KEY_TOTAL_ITEMS)).thenThrow(JSONException.class);

        int result = JSONParser.parseTotalItemsCount(mMockJSONObj);

        assertThat(result, is(INVALID_COUNT));
    }

    @Test
    public void parseTotalItemsCount_ValidCountValue() throws Exception {
        when(mMockJSONObj.getInt(JSON_KEY_TOTAL_ITEMS)).thenReturn(1);

        int result = JSONParser.parseTotalItemsCount(mMockJSONObj);

        assertThat(result, is(1));
    }

    private void assertCorrectJSONItem(ProgramEntry item, HashMap<String, String> expectedJSONItem) {
        assertThat(item.getProgramName(),
                is(expectedJSONItem.get(JSON_ITEM_KEY_NAME)));
        assertThat(item.getStartTime(),
                is(expectedJSONItem.get(JSON_ITEM_KEY_START_TIME)));
        assertThat(item.getEndTime(),
                is(expectedJSONItem.get(JSON_ITEM_KEY_END_TIME)));
        assertThat(item.getChannel(),
                is(expectedJSONItem.get(JSON_ITEM_KEY_CHANNEL)));
        assertThat(item.getRating(),
                is(expectedJSONItem.get(JSON_ITEM_KEY_RATING)));
    }
}