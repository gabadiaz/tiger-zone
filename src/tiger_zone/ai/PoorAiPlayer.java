package tiger_zone.ai;

import tiger_zone.Game;
import tiger_zone.PossibleMovesRule;
import tiger_zone.Tile;

/**
 * A pretty bad AI player which simply makes the first possible move found. This exists simply to demonstrate how an AI
 * player should function.
 */
public class PoorAiPlayer extends AiPlayer {

	/**
	 * Create a new instance of <code>PoorAiPlayer</code>.
	 * 
	 * @param game The game instance.
	 */
	public PoorAiPlayer(Game game) {
		super(game);
	}

	/**
	 * Have this AI player place a tile on the board.
	 */
	public final void makeMove() {
		Tile current = this.game.getBoard().getPile().pop();
		PossibleMovesRule pmr = new PossibleMovesRule(this.game.getBoard(), 0, 0, current, false);
		
		// no possible move; simply pass our turn (not what's actually supposed to happen)
		if (!pmr.evaluate()) {
			return;
		}
		
		// x = move[0]
		// y = move[1]
		// rotation = move[2]
		int[] move = pmr.getFirstPossibleMove();
		while (current.getRotation() != move[2]) {
			current.rotate();
		}
		game.getBoard().addTile(move[0], move[1], current);
		
		if(current.hasSpecial()){
			System.out.println("Tile has special attribute!");
			current.addTiger(1);
		}
	}
}