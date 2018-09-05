package analyzer;

public class Battle {
	private Trainer p1;
	private Trainer p2;
	private Trainer winner;

	public Battle() {
		p1 = new Trainer();
		p2 = new Trainer();
	}

	public void setWinner(Trainer trainer) {
		winner = trainer;
	}

	public Trainer getWinner() {
		return winner;
	}

	public Trainer getP1() {
		return p1;
	}

	public Trainer getP2() {
		return p2;
	}

}
