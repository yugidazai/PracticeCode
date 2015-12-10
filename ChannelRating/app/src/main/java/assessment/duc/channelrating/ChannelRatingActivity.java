package assessment.duc.channelrating;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import assessment.duc.channelrating.adapter.ProgramListAdapter;
import assessment.duc.channelrating.model.ProgramEntry;

public class ChannelRatingActivity extends Activity {

    private static final String TAG = ChannelRatingActivity.class.getSimpleName();
    private static final int JSON_DATA_BLOCK_SIZE = 10; // decided by API policy

    private boolean mIsLoadingDdata = false;
    private int mCurrentOffset = 0;
    private int mTotalItemsCount = JSONParser.INVALID_COUNT;
    private ProgressDialog mProgressDialog;
    private ProgramListAdapter mProgramListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_rating);
        // only display progress dialog once at the beginning
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.progress_dialog_msg));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        // instantiate adapter for ListView of programs' list
        mProgramListAdapter = new ProgramListAdapter(this, new ArrayList<ProgramEntry>());
        ListView programListView = (ListView) findViewById(R.id.program_list);
        programListView.setAdapter(mProgramListAdapter);
        programListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // load next available data from URL
                if (!mIsLoadingDdata && totalItemCount < mTotalItemsCount) {
                    // Firstly, we have 2 blocks of items loaded.
                    // If user scrolls down to 2nd item of any block,
                    // download 1 more block in advance (only if not downloaded)
                    // e.g. index 1: download 3rd block,
                    //		index 11: download 4th block (do not download more data if scroll back to index 1)
                    //		...
                    if (firstVisibleItem % 10 == 1
                            && (totalItemCount - firstVisibleItem) < 2 * JSON_DATA_BLOCK_SIZE) {
                        downloadData();
                    }
                }
            }
        });

        // get first block of items with OFFSET 0
        downloadData();
    }

    private void downloadData() {
        if (!mIsLoadingDdata) {
            mIsLoadingDdata = true;
            new DataDownloadAsyncTask().execute();
        }
    }

    /* From given URL with OFFSET value,
     * each download task retrieves the next block of items*/
    class DataDownloadAsyncTask extends AsyncTask<Void, Void, List<ProgramEntry>> {

        private static final String DATA_URL =
                "https://www.whatsbeef.net/wabz/guide.php?start=";

        @Override
        protected List<ProgramEntry> doInBackground(Void... params) {
            // Get JSON object from url with current OFFSET, then parse JSON object
            // to get a list of next block of items' data from this OFFSET
            JSONObject jsonObj = JSONDownloader.getJSONObjectFromApi(DATA_URL + mCurrentOffset);
            if (jsonObj == null) {
                return new ArrayList<ProgramEntry>();
            }

            // retrieve the "count" valid value from JSON Object
            if (mTotalItemsCount == JSONParser.INVALID_COUNT) {
                mTotalItemsCount = JSONParser.parseTotalItemsCount(jsonObj);
            }

            return JSONParser.parseResult(jsonObj);
        }

        @Override
        protected void onPostExecute(List<ProgramEntry> result) {
            // update data list for the adapter and notify it to update ListView
            mProgramListAdapter.addAll(result);
            mProgramListAdapter.notifyDataSetChanged();

            // done loading data, reset this flag to false
            mIsLoadingDdata = false;

            // Offset is updated after previous download task
            mCurrentOffset += JSON_DATA_BLOCK_SIZE;

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                // After downloading 1st block, retrieve 1 more block of items in advance
                if (mCurrentOffset == JSON_DATA_BLOCK_SIZE) {
                    downloadData();
                } else {
                    // dismiss dialog after downloading first 2 blocks of items
                    mProgressDialog.dismiss();
                }
            }
        }
    }
}
