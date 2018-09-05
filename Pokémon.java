package analyzer;

public class Pokémon {
	private String species;
	private String nickname;
	private int kills;
	private boolean fainted;
	private int turnFainted;
	private boolean unknownSpecies;

	public Pokémon(String s) {
		species = s;
		if (s.charAt(species.length() - 1) == '*') {
			species = s.substring(0, s.indexOf("-*"));
			unknownSpecies = true;
		} else {
			species = s;
			unknownSpecies = false;
		}
		turnFainted = 0;
	}
	
	public void setSpecies(String s) {
		species = s;
	}

	public String getSpecies() {
		return species;
	}

	public void setNickname(String s) {
		nickname = s;
	}

	public String getNickname() {
		return nickname;
	}

	public void incrementKills() {
		kills++;
	}

	public int getKills() {
		return kills;
	}

	public void setFainted() {
		fainted = true;
	}

	public boolean getFainted() {
		return fainted;
	}

	public void isNowKnown() {
		unknownSpecies = false;
	}

	public boolean isUnknown() {
		return unknownSpecies;
	}
	
	public int getTurnFainted() {
		return turnFainted;
	}
	
	public void setTurnFainted(int turn) {
		turnFainted = turn;
	}
}
