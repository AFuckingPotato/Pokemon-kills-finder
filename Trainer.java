package analyzer;

import java.util.ArrayList;

public class Trainer {
	private String name;
	private ArrayList<Pok�mon> team;
	
	public Trainer() {
		team = new ArrayList<>();
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Pok�mon> getTeam() {
		return team;
	}
}
