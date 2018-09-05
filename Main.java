package analyzer;

public class Main {

	public static void main(String[] args) throws Exception {
		GrabHTML replay = new GrabHTML("https://replay.pokemonshowdown.com/draftfrontier-gen7draftleaguetier-5817");
		replay.writeLog();
		Battle battle = new Battle();
		// for keeping track which mon died in which turn
		int turn = 1;
		// initialize a String of the Pok�mon that died for later use
		String faintingMon = "";
		for (String s : replay.getLog()) {
			// set the names for the players
			if (s.contains("|player|")) {
				if (s.contains("p1") && battle.getP1().getName() != null)
					battle.getP1().setName(s.substring(s.indexOf("p1|") + 3, s.lastIndexOf('|')));
				else if (s.contains("p2") && battle.getP2().getName() != null)
					battle.getP2().setName(s.substring(s.indexOf("p2|") + 3, s.lastIndexOf('|')));
			}

			// initialize the Pok�mon for p1
			if (s.contains("poke|p1|")) {
				if (s.contains(", "))
					battle.getP1().getTeam().add(new Pok�mon(s.substring(s.indexOf("|p1|") + 4, s.indexOf(','))));
				else if (s.contains("|item"))
					battle.getP1().getTeam().add(new Pok�mon(s.substring(s.indexOf("|p1|") + 4, s.indexOf("|item"))));
			}

			// initialize the Pok�mon for p2
			if (s.contains("poke|p2|")) {
				if (s.contains(", "))
					battle.getP2().getTeam().add(new Pok�mon(s.substring(s.indexOf("|p2|") + 4, s.indexOf(','))));
				else if (s.contains("|item"))
					battle.getP2().getTeam().add(new Pok�mon(s.substring(s.indexOf("|p2|") + 4, s.indexOf("|item"))));
			}

			if (s.contains("|switch|")) {

				// set the nicknames for p1, and if necessary also replaces any unknown forms,
				// like arceus to arceus-water
				if (s.contains("p1"))
					for (Pok�mon p : battle.getP1().getTeam()) {
						if (s.contains(p.getSpecies())) {
							if (p.getNickname() == null)
								p.setNickname(s.substring(s.indexOf(": ") + 2, s.indexOf("|" + p.getSpecies())));
							if (p.isUnknown()) {
								p.setSpecies(s.substring(s.indexOf("|" + p.getSpecies()) + 1, s.indexOf("|100")));
								p.isNowKnown();
							}
						}
					}

				// set the nicknames for p2, and if necessary also replaces any unknown forms,
				// like arceus to arceus-water
				else if (s.contains("p2"))
					for (Pok�mon p : battle.getP2().getTeam()) {
						if (s.contains(p.getSpecies())) {
							if (p.getNickname() == null)
								p.setNickname(s.substring(s.indexOf(": ") + 2, s.indexOf("|" + p.getSpecies())));
							if (p.isUnknown()) {
								p.setSpecies(s.substring(s.indexOf("|" + p.getSpecies()) + 1, s.indexOf("|100")));
								p.isNowKnown();
							}
						}
					}
			}

			// check for healing wish, that move is written down differently from other
			// kinds of fainting
			boolean healingWish = false;
			if (s.contains("|Healing Wish|")) {
				healingWish = true;
				if (s.contains("p1")) {
					for (Pok�mon p : battle.getP1().getTeam()) {
						if (p.getNickname() != null) {
							p.setFainted();
							p.setTurnFainted(turn);
						}
					}
				} else {
					for (Pok�mon p : battle.getP2().getTeam()) {
						if (p.getNickname() != null) {
							p.setFainted();
							p.setTurnFainted(turn);
						}
					}
				}
			}

			if (!healingWish) {
				// find the Pok�mon that have fainted this battle and the Pok�mon that killed
				// them
				if (s.contains("fnt")) {
					// find the fainted Pok�mon for player 1
					if (s.contains("p1")) {
						for (Pok�mon p : battle.getP1().getTeam()) {
							if (p.getNickname() != null)
								// following line makes sure that only the Pok�mon that faints this turn
								// actually gets counted as fainted
								if (s.contains(p.getNickname() + "|0 fnt")) {
									p.setFainted();
									p.setTurnFainted(turn);
									faintingMon = p.getNickname();
								}
						}

						// for when a Pok�mon is killed by sandstorm
						if (s.contains("[from] Sandstorm")) {
							boolean done = false;
							// loops back through the lines to find the last instance of sandstorm being set
							// up
							for (int i = replay.getLog().indexOf(s); i > 0 && !done; i--) {
								// if the last time sand was set up was by the same player, then no kill is
								// awarded
								if (replay.getLog().get(i).contains("|Sandstorm|[from]") && replay.getLog().get(i).contains("p1")) {
									done = true;
								}
								// if the last time sand was set up by the opponent, a kill is awarded to the
								// Pok�mon that set up the sand
								if (replay.getLog().get(i).contains("|Sandstorm|[from]") && replay.getLog().get(i).contains("p2")) {
									for (Pok�mon p : battle.getP2().getTeam()) {
										if (p.getNickname() != null)
											if (replay.getLog().get(i).contains(p.getNickname()))
												p.incrementKills();
									}
									done = true;
								}
							}
						}

						// for when a Pok�mon is killed by hail
						else if (s.contains("[from] Hail")) {
							boolean done = false;
							// loops back through the lines to find the last instance of hail being set up
							for (int i = replay.getLog().indexOf(s); i > 0 && !done; i--) {
								// if the last time hail was set up was by the same player, then no kill is
								// awarded
								if (replay.getLog().get(i).contains("|Hail|[from]") && replay.getLog().get(i).contains("p1")) {
									done = true;
								}
								// if the last time hail was set up by the opponent, a kill is awarded to the
								// Pok�mon that set up the hail
								if (replay.getLog().get(i).contains("|Hail|[from]") && replay.getLog().get(i).contains("p2")) {
									for (Pok�mon p : battle.getP2().getTeam()) {
										if (p.getNickname() != null)
											if (replay.getLog().get(i).contains(p.getNickname())) {
												p.incrementKills();
												done = true;
											}
									}
								}
							}
						}

						// for when a Pok�mon faints due to poison or toxic poison damage
						else if (s.contains("[from] psn")) {
							// loops back through the lines to find the last time the fainting Pok�mon gets
							// poisoned
							boolean done = false;
							for (int i = replay.getLog().indexOf(s); i > 0 && !done; i--) {
								// find the last time the Pok�mon that fainted got a status ailment
								if (replay.getLog().get(i).contains("-status|p1a: " + faintingMon)) {

									// if faintingMon got poisoned by toxic orb
									if (replay.getLog().get(i).contains("Toxic Orb")) {
										done = true;
									}

									// if faintingMon switched in on the line above where faintingMon got poisoned,
									// faintingMon got poisoned by toxic spikes
									if (!done) {
										if (replay.getLog().get(i - 1).contains("|switch|")) {
											// loop back through the lines to get the last time toxic spikes were set up
											for (int y = i; y > 0 && !done; y--) {
												if (replay.getLog().get(y).contains("Toxic Spikes")) {
													for (Pok�mon p : battle.getP2().getTeam()) {
														// if the nickname of a Pok�mon of the opposing team is on the line above where
														// the toxic spikes are set up, it means that that particular Pok�mon set up the
														// toxic spikes
														if (p.getNickname() != null) {
															if (replay.getLog().get(y - 1).contains(p.getNickname())) {
																p.incrementKills();
																done = true;
															}
														}
													}
												}
											}
										}
									}

									// if faintingMon has not been poisoned by toxic spikes
									if (!done) {
										// see if an opposing Pok�mon's nickname shows up the line above where
										// faintingMon got poisoned, meaning that it directly poisoned faintingMon (this
										// also works for poison touch, not intended but nice either way). If a nickname
										// shows up, award a kill to the corresponding Pok�mon
										for (Pok�mon p : battle.getP2().getTeam()) {
											if (p.getNickname() != null) {
												if (replay.getLog().get(i - 1).contains(p.getNickname())) {
													p.incrementKills();
													done = true;
												}
											}
										}
									}

									// finally, if the opposing Pok�mon that poisoned it does not show up right
									// above where faintingMon got poisoned, it got poisoned from an effect of a
									// move, here this is checked
									if (!done) {
										for (int y = replay.getLog().indexOf(s); y > 0 && !done; y--) {
											// simply find the first Pok�mon of the opposing team because that is the
											// Pok�mon that last made a move, which means it was the Pok�mon that poisoned
											// faintingMon, meaning it gets awarded a kill
											for (Pok�mon p : battle.getP2().getTeam()) {
												if (p.getNickname() != null) {
													if (replay.getLog().get(y).contains(p.getNickname())) {
														p.incrementKills();
														done = true;
													}
												}
											}
										}
									}
								}
							}
						}

						else if (s.contains("[from] item: Life Orb") || s.contains("[from] Recoil")) {
							// basically, do nothing if the Pok�mon died from life orb damage or recoil
						}
					}
				}

				if (s.contains("|turn"))
					turn++;
			}
		}
	}
}
