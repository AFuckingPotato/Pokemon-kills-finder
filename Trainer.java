package analyzer;

import java.util.ArrayList;

public class Trainer {
	private String name;
	private ArrayList<Pokémon> team;
	
	public Trainer() {
		team = new ArrayList<>();
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Pokémon> getTeam() {
		return team;
	}
}
