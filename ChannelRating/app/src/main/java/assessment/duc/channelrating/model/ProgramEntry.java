package assessment.duc.channelrating.model;

public class ProgramEntry {

	private String programName;
	private String startTime;
	private String endTime;
	private String channel;
	private String rating;
	
	public ProgramEntry(String programName, String startTime, String endTime, String channel, String rating) {
		this.programName = programName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.channel = channel;
		this.rating = rating;
	}

	public String getProgramName() {
		return programName;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getChannel() {
		return channel;
	}

	public String getRating() {
		return rating;
	}
}
