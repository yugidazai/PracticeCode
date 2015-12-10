package assessment.duc.channelrating.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import assessment.duc.channelrating.R;
import assessment.duc.channelrating.model.ProgramEntry;

public class ProgramListAdapter extends ArrayAdapter<ProgramEntry> {

	private LayoutInflater mInflater;

	public ProgramListAdapter(Activity context, List<ProgramEntry> programList) {
		super(context, R.layout.channel_rating_list_item_layout, programList);
		mInflater = context.getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// inflate each view's layout once, then keep this layout in a view holder to reuse later 
	    if (convertView == null) {
	    	convertView = mInflater.inflate(
	    			R.layout.channel_rating_list_item_layout, null);
	    	ViewHolder viewHolder = new ViewHolder(convertView);
	    	convertView.setTag(viewHolder);
	    }

	    ProgramEntry entry = getItem(position);
	    if (entry != null) {
		    // fill loaded data into View holder
		    ViewHolder holder = (ViewHolder) convertView.getTag();
		    holder.name.setText(entry.getProgramName());
		    holder.startTime.setText(entry.getStartTime());
		    holder.endTime.setText(entry.getEndTime());
		    holder.channel.setText(entry.getChannel());
		    holder.rating.setText(entry.getRating());
	    }
	    
	    return convertView;
    }
	
	static class ViewHolder {
		TextView name;
		TextView startTime;
		TextView endTime;
		TextView channel;
		TextView rating;

		ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.nameTV);
			startTime = (TextView) view.findViewById(R.id.startTimeTV);
			endTime = (TextView) view.findViewById(R.id.endTimeTV);
			channel = (TextView) view.findViewById(R.id.channelTV);
			rating = (TextView) view.findViewById(R.id.ratingTV);
		}
	}
}
