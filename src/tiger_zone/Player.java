package tiger_zone;

import java.util.Stack;
import java.util.Queue;

/**
 * The <code>Player</code> class represents one of the two players in a game of Tiger Zone, and is used to keep track of
 * points and available tigers.
 */
public class Player {
	
	static class Scoring {
		private int player1Score;
		private int player2Score;
		private Board boardState;
		private int lastTileX;
		private int lastTileY;
		
		public int getplayer1Score() {
			return player1Score;
		}
		
		public int getplayer2Score() {
			return player2Score;
		}
		
		public int[] updateScore(int lastTileX, int lastTileY) {
			int[] updatedScores = new int[2]; //used to return the final values
			updatedScores[0] = getplayer1Score(); //set up scores already calculated
			updatedScores[1] = getplayer2Score();
			
			Tile lastTile = boardState.getTile(lastTileX, lastTileY); //get the last updated tile
			char[] lastTileSides = lastTile.getSides(); //get sides of last updated tile
			
			//check if last tile placed finishes a/some den(s)
			//ISSUES: not checking if lastTileX-1 < 0 or lastTileX+1 > MaxBoardX
			//it is possible it make this more efficient such as, only checking sides with jungles for completed dens instead of all sides
			//
			if(lastTileSides[0] == 'j' || lastTileSides[2] == 'j' || lastTileSides[3] == 'j' || lastTileSides[5] == 'j' || lastTileSides[6] == 'j' || lastTileSides[8] == 'j' || lastTileSides[9] == 'j' || lastTileSides[11] == 'j')
			{
				updatedScores = checkForDen(lastTileX+1, lastTileY, updatedScores);
				updatedScores = checkForDen(lastTileX, lastTileY+1, updatedScores);
				updatedScores = checkForDen(lastTileX-1, lastTileY, updatedScores);
				updatedScores = checkForDen(lastTileX+1, lastTileY-1, updatedScores);
				updatedScores = checkForDen(lastTileX+1, lastTileY+1, updatedScores);
				updatedScores = checkForDen(lastTileX+1, lastTileY-1, updatedScores);
				updatedScores = checkForDen(lastTileX-1, lastTileY+1, updatedScores);
				updatedScores = checkForDen(lastTileX-1, lastTileY-1, updatedScores);
			}
			
			//check if any lakes are completed
			updatedScores = checkForLake(lastTileX, lastTileY, updatedScores);
			
			return updatedScores;
		}
		
		public int[] checkForDen(int x, int y, int[] scores) {
			int[] foundScores = new int[2];
			foundScores[0] = 0;
			foundScores[0] = 0;
			Tile checkTile = boardState.getTile(x, y);
			if(checkTile.getCenter() == 'd'){ //check if tile has a den
				if((boardState.getTile(x+1,y) != null) //check if den is complete
						&& (boardState.getTile(x-1,y) != null) 
						&& (boardState.getTile(x,y+1) != null) 
						&& (boardState.getTile(x,y-1) != null)
						&& (boardState.getTile(x+1,y+1) != null)
						&& (boardState.getTile(x+1,y-1) != null)
						&& (boardState.getTile(x-1,y+1) != null)
						&& (boardState.getTile(x-1,y-1) != null)){
					//assign score values based on which player's tiger is there if any 
					//NOT IMPLEMENTED BECAUSE OF UNKNOWN HOW TIGERS ARE IMPLEMENTED
				}
				scores[0] += foundScores[0];
				scores[1] += foundScores[1];
			}
			return scores;
		}
		
		/* TONS OF SPAGHETTI...... here's what I attempted
		 * first if: push the cords and the tile (may only need to push cords) of the lastplaced tile into a stack
		 * while stack is not empty: pop top, check every side if it has a lake. if a side has a lake, check if there is another tile next to it.
		 * if there is a tile thats not flagged then check if that tile is a special case
		 * if special case: then flag the special cases cordinates and add one to total
		 * if normal tile: put that tile in the stack, add one to total
		 */
		public int[] checkForLake(int x, int y, int[] scores) {
			Boolean nullFound = false;
			int totalScore = 0;
			int[] XY = new int[2]; //used for the cordinate stack
			int[] tempXY = new int[2];
			XY[0] = x;
			XY[1] = y;
			Stack<int[]> XYStack = new Stack<int[]>(); //XY and tile stacks will be the same XY is the cords for the tile
			Stack<Tile> tileStack = new Stack<Tile>();
			Boolean[][] flagGrid = new Boolean[152][152];
			int p1Tigers = 0;
			int p2Tigers = 0;
			Tile temp;
			char tempcenter;
			char[] tempsides;
			
			
			Tile checkTile = boardState.getTile(x, y);
			char center = checkTile.getCenter();
			char[] sides = checkTile.getSides();
			
			//ideas: check all sides of current Tile -> if any sides has lake, check those sides -> 
			//if tile null, means lake not done end immediately, otherwise add to queue if not flagged, and flag on board
			//if nothing to add and no nulls, take from queue and repeat
			//how to implement flags: create "bool[][] flagGrid = new bool[152][152];" flag that way, takes time to make but quick to check
			//how to score: every pop yields +1 to totalScore
			//tigers still need to be implemented
			//special case tiles, change a value in the center for where we connected to it ('T','B','L','R') <- need caps
			//if special case tile is the placed tile: need to check both sides for completedness
		
			
			if(sides[1] == 'l' || sides[4] == 'l' || sides[7] == 'l' || sides[10] == 'l'){ //setting up stack: if the first tile has a lake side, add it to stack
				tileStack.push(boardState.getTile(x, y)); //set up tile stack
				XYStack.push(XY); //set up XY cordinate stack
				flagGrid[x][y] = true; //flag first tile on flaggrid
				totalScore += 1; //add 1 to total score
				
			}
			
			while(tileStack.peek() != null) {
				XY = XYStack.pop();
				checkTile = tileStack.pop();
				center = checkTile.getCenter();
				sides = checkTile.getSides();
				
				if(sides[1] == 'l') //check top
				{
					if(boardState.getTile(XY[0], XY[1]-1) == null) { //check if top has a tile
						nullFound = true;
						break;
					}
					else if (flagGrid[XY[0]][XY[1]-1] == true) {}
					else {
						temp = boardState.getTile(XY[0], XY[1]-1);
						tempcenter = temp.getCenter();
						tempsides = temp.getSides();
						if((tempcenter != 'l' && tempsides[1] == 'l' && tempsides[7] == 'l') //test for special case tiles ex. Tile N
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[1] == 'l' && tempsides[4] == 'l')
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[7] == 'l')
								|| (tempcenter != 'l' && tempsides[7] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[10] == 'l' && tempsides[1] == 'l')){
							flagGrid[XY[0]][XY[1]-1] = true; //if true, flag it then add to score, dont push it to stack
							totalScore += 1;
						}
						else {
							tempXY = XY;
							tempXY[1] = XY[1] - 1; 
							tileStack.push(temp); //set up tile stack
							XYStack.push(tempXY); //set up XY cordinate stack
							flagGrid[XY[0]][XY[1]-1] = true; //flag first tile on flaggrid
							totalScore += 1; //add 1 to total score
						}
					}
				}
				if(sides[4] == 'l') //check right
				{
					if(boardState.getTile(XY[0]+1, XY[1]) == null) { //check if right has a tile
						nullFound = true;
						break;
					}
					else if (flagGrid[XY[0]+1][XY[1]] == true) {}
					else {
						temp = boardState.getTile(XY[0]+1, XY[1]);
						tempcenter = temp.getCenter();
						tempsides = temp.getSides();
						if((tempcenter != 'l' && tempsides[1] == 'l' && tempsides[7] == 'l') //test for special case tiles ex. Tile N
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[1] == 'l' && tempsides[4] == 'l')
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[7] == 'l')
								|| (tempcenter != 'l' && tempsides[7] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[10] == 'l' && tempsides[1] == 'l')){
							flagGrid[XY[0]+1][XY[1]] = true; //if true, flag it then add to score, dont push it to stack
							totalScore += 1;
						}
						else {
							tempXY = XY;
							tempXY[0] = XY[0] + 1; 
							tileStack.push(temp); //set up tile stack
							XYStack.push(tempXY); //set up XY cordinate stack
							flagGrid[XY[0]+1][XY[1]] = true; //flag first tile on flaggrid
							totalScore += 1; //add 1 to total score
						}
					}
				}
				if(sides[7] == 'l') //check bottom
				{
					if(boardState.getTile(XY[0], XY[1]+1) == null) { //check if bottom has a tile
						nullFound = true;
						break;
					}
					else if (flagGrid[XY[0]][XY[1]+1] == true) {}
					else {
						temp = boardState.getTile(XY[0], XY[1]+1);
						tempcenter = temp.getCenter();
						tempsides = temp.getSides();
						if((tempcenter != 'l' && tempsides[1] == 'l' && tempsides[7] == 'l') //test for special case tiles ex. Tile N
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[1] == 'l' && tempsides[4] == 'l')
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[7] == 'l')
								|| (tempcenter != 'l' && tempsides[7] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[10] == 'l' && tempsides[1] == 'l')){
							flagGrid[XY[0]][XY[1]+1] = true; //if true, flag it then add to score, dont push it to stack
							totalScore += 1;
						}
						else {
							tempXY = XY;
							tempXY[1] = XY[1] + 1; 
							tileStack.push(temp); //set up tile stack
							XYStack.push(tempXY); //set up XY cordinate stack
							flagGrid[XY[0]][XY[1]+1] = true; //flag first tile on flaggrid
							totalScore += 1; //add 1 to total score
						}
					}
				}
				if(sides[10] == 'l') //check left
				{
					if(boardState.getTile(XY[0]-1, XY[1]) == null) { //check if left has a tile
						nullFound = true;
						break;
					}
					else if (flagGrid[XY[0]-1][XY[1]] == true) {}
					else {
						temp = boardState.getTile(XY[0]-1, XY[1]);
						tempcenter = temp.getCenter();
						tempsides = temp.getSides();
						if((tempcenter != 'l' && tempsides[1] == 'l' && tempsides[7] == 'l') //test for special case tiles ex. Tile N
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[1] == 'l' && tempsides[4] == 'l')
								|| (tempcenter != 'l' && tempsides[4] == 'l' && tempsides[7] == 'l')
								|| (tempcenter != 'l' && tempsides[7] == 'l' && tempsides[10] == 'l')
								|| (tempcenter != 'l' && tempsides[10] == 'l' && tempsides[1] == 'l')){
							flagGrid[XY[0]-1][XY[1]] = true; //if true, flag it then add to score, dont push it to stack
							totalScore += 1;
						}
						else {
							tempXY = XY;
							tempXY[0] = XY[0] - 1; 
							tileStack.push(temp); //set up tile stack
							XYStack.push(tempXY); //set up XY cordinate stack
							flagGrid[XY[0]-1][XY[1]] = true; //flag first tile on flaggrid
							totalScore += 1; //add 1 to total score
						}
					}
				}
				
			}
			
			
			return scores;
		}
		
		
	}
}