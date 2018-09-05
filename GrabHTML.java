package analyzer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GrabHTML {
	private static boolean start;
	private static boolean end;
	private URL url;
	private ArrayList<String> log;
	
	public GrabHTML(String replayURL) {
		start = false;
		end = false;
		try {
			url = new URL(replayURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		log = new ArrayList<>();
	}

	public void writeLog() throws Exception {
		
		// Set URL
		URLConnection spoof = url.openConnection();

		// Spoof the connection so we look like a web browser
		spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
		BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
		String strLine = "";

		// Loop through every line in the source
		while ((strLine = in.readLine()) != null) {

			// Prints each line to the console
			if(strLine.contains("|player|"))
				start = true;
			if(start && !end) {
				log.add(strLine);
			}
			if(strLine.contains("|win|"))
				end = true;
		}
	}
	
	public String getLine(int line) {
		return log.get(line);
	}
	
	public ArrayList<String> getLog(){
		return log;
	}
}