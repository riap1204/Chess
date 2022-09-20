package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Ria Parikh Alexander Osterman
 *
 */

/**
 * @author riaparikh
 *
 */
/**
 * @author riaparikh
 *
 */
public class Chess
{ 
	
	/**
	 *  An Arraylist to keep track of the number of white colored pieces in the game
	 */
	static ArrayList<piece> whitePieces=new ArrayList<piece>();
	
	/**
	 *  An Arraylist to keep track of the number of black colored pieces in the game
	 */
	static ArrayList<piece> blackPieces=new ArrayList<piece>();
	
	
	/**
	 *  A variable to keep track of which team's chance is it. True means white's turn, False means black's turn
	 */
	public static boolean turn=true; 
	
	/**
	 *  A variable to track the location of the king of the white team
	 */
	public static space whiteKingLocation;
	
	/**
	 *   A variable to track the location of the king of the black team
	 */
	public static space blackKingLocation;
	
	/**
	 *  An 8x8 board of type space that resembles an actual chess board (to play chess on)
	 */
	public static space board[][]=new space[8][8];
	
	/**
	 *  A boolean variable that is set to True in case of a draw
	 */
	public static boolean draw;
	
	/**
	 * A boolean variable that is set to True in case one of the player resigns
	 */
	public static boolean resign;	//will get set to true if a draw or resign happens
	/**
	 * a variable for reverting the board correctly on a promotion
	 */
	public static boolean promoted=false;	
	/**
	 *  A variable to keep track whether the first move of a pawn was a single step or double steps.
	 *  In case the pawn takes two steps in the first move, the value of twosteps is set to the file index
	 *  of the pawn. If not, then by default it is -1
	 */
	public static int twoSteps=-1;

	
	
	
	/**
	 * @author RiaParikh AlexanderOsterman
	 * An abstract class to provide a common functionality for all the pieces in chess
	 */
	abstract static class piece
	{
		/**
		 *  A variable to keep track of what team the piece belongs to 
		 *  True means white team, and false means black team
		 */
		boolean team;	
		/**
		 *  A variable to keep track of the location of the piece
		 *  Files are columns that go up and down the chessboard
		 *  FileIndex refers to the file of the piece
		 */
		int fileindex;	
		/**
		 * A variable to keep track of the location of the piece
		 * Ranks are rows that go from side to side across the chessboard
		 * rankindex refers to the rank of the piece
		 */
		int rankindex;
		
		/**
		 *  A boolean variable to check if the current move is the first move of that particular chess piece
		 */
		boolean isFirstMove=true;
		
		/**
		 * A method to find all spaces the piece can legally move to
		 * @return returns list of all  spaces the piece can legally move to
		 */
		abstract ArrayList<space> legalMoves();	
		
		/**
		 * A method to return the name of the specific piece
		 * @return name of the piece
		 */
		abstract public String toString();
		{
			
		}
		
		/**
		 * A method to move the given chess piece from one space to another
		 * @param move that gives the current file and rank of the piece, and then the file and rank
		 * of the space to be moved to
		 */
		abstract void movePiece(move move);	//not fully implemented yet
		
		/** 
		 * A method to find all the spaces that can be attacked by the current piece
		 * @return an arraylist of all spaces under attack by the current piece
		 */
		abstract public ArrayList<space> AttackedSquares();
	}
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to refer to the pawn piece of the chess. It implements the abstract class piece
	 *
	 */
	static class pawn extends piece
	{
		
		public pawn(boolean team, int file, int rank)
		{
			this.team=team;
			this.fileindex=file;
			this.rankindex=rank;
		}
		
		public String toString()
		{
			return this.team ? "wp" : "bp";
		}
		
		public void movePiece(move move)
		{
			if(board[move.endindex1][move.endindex2].occupiedBy!=null)
			{
				if(this.team)
				{
					blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
				else
				{
					whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
			}
			
			if(board[move.endindex1][move.endindex2].occupiedBy==null&&move.endindex1!=move.startindex1)
			{
				if(team)
				{
					blackPieces.remove(board[move.endindex1][4].occupiedBy);
					board[move.endindex1][4].occupiedBy=null;
				}
				else
				{
					whitePieces.remove(board[move.endindex1][3].occupiedBy);
					board[move.endindex1][3].occupiedBy=null;
				}
			}
			
			board[move.endindex1][move.endindex2].occupiedBy=this;
			board[move.startindex1][move.startindex2].occupiedBy=null;
			this.fileindex=move.endindex1;
			this.rankindex=move.endindex2;
			
			if (move.endindex2==move.startindex2+2 || move.endindex2==move.startindex2-2){
				
				twoSteps=move.endindex1;
				
			}
			if(move.endindex2==7||move.endindex2==0)
			{
				promoted=true;
				if(move.promotion==0)
				{
					if(team)
					{
						whitePieces.remove(this);
						piece queen=new queen(true, this.fileindex, this.rankindex);
						whitePieces.add(queen);
						board[this.fileindex][this.rankindex].occupiedBy=queen;
					}
					else
					{
						blackPieces.remove(this);
						piece queen=new queen(false, this.fileindex, this.rankindex);
						blackPieces.add(queen);
						board[this.fileindex][this.rankindex].occupiedBy=queen;
					}
				}
				else if(move.promotion==1)
				{
					if(team)
					{
						whitePieces.remove(this);
						piece rook=new rook(true, this.fileindex, this.rankindex);
						whitePieces.add(rook);
						board[this.fileindex][this.rankindex].occupiedBy=rook;
					}
					else
					{
						blackPieces.remove(this);
						piece rook=new rook(false, this.fileindex, this.rankindex);
						blackPieces.add(rook);
						board[this.fileindex][this.rankindex].occupiedBy=rook;
					}
				}
				else if(move.promotion==2)
				{
					if(team)
					{
						whitePieces.remove(this);
						piece knight=new knight(true, this.fileindex, this.rankindex);
						whitePieces.add(knight);
						board[this.fileindex][this.rankindex].occupiedBy=knight;
					}
					else
					{
						blackPieces.remove(this);
						piece knight=new knight(false, this.fileindex, this.rankindex);
						blackPieces.add(knight);
						board[this.fileindex][this.rankindex].occupiedBy=knight;
					}
				}
				else if(move.promotion==3)
				{
					if(team)
					{
						whitePieces.remove(this);
						piece bishop=new bishop(true, this.fileindex, this.rankindex);
						whitePieces.add(bishop);
						board[this.fileindex][this.rankindex].occupiedBy=bishop;
					}
					else
					{
						blackPieces.remove(this);
						piece bishop=new bishop(false, this.fileindex, this.rankindex);
						blackPieces.add(bishop);
						board[this.fileindex][this.rankindex].occupiedBy=bishop;
					}
				}
				this.fileindex=move.startindex1;	//stuff gets messed up if it doesn't move back (very edge case error)
				this.rankindex=move.startindex2;
			}
		}
		
		public ArrayList<space> legalMoves()
		{
			ArrayList<space> result=new ArrayList<space>();
			int direction;	//for which direction the pawn moves
			if(this.team)
			{
				direction=1;
			}
			else
			{
				direction=-1;	//move backward for black team
			}

			//System.out.println("white team");
			//System.out.println(this.rankindex);
			if(board[this.fileindex][this.rankindex+(1*direction)].occupiedBy==null)	//TODO make sure it's not placing own king into check, 
				//temporarily move the piece, then call updateAttacks and if king is in check then call revertboard to change back and the space should not be added to legal moves
			{
				move t1= new move(this.fileindex,this.rankindex,this.fileindex,this.rankindex+(1*direction),false,false);
				//Checking if this specific move leads to check
				if(testMove(t1))
				{
					result.add(board[this.fileindex][this.rankindex+(1*direction)]);
				}
				
				
				if(isFirstMove&&board[this.fileindex][this.rankindex+(2*direction)].occupiedBy==null)
				{
					t1= new move(this.fileindex,this.rankindex,this.fileindex,this.rankindex+(2*direction),false,false);
					//Checking if this specific move leads to check
					if(testMove(t1))
					{
						result.add(board[this.fileindex][this.rankindex+(2*direction)]);
					}
				}
					
			}
			//en passant check
			if(twoSteps!=-1) {
				int currentfile=twoSteps;
				if(this.fileindex+1==currentfile && this.rankindex==3)
				{
					move t2= new move(this.fileindex,this.rankindex,this.fileindex+1,this.rankindex-1,false,false);
					if(testMove(t2))
					{
						result.add(board[this.fileindex+1][this.rankindex-1]);
					}
					
				}
				
				if(this.fileindex+1==currentfile && this.rankindex==4)
				{
					move t3= new move(this.fileindex,this.rankindex,this.fileindex+1,this.rankindex+1,false,false);
					if(testMove(t3))
					{
						result.add(board[this.fileindex+1][this.rankindex+1]);
					}
				}
				
				if(this.fileindex-1==currentfile && this.rankindex==3)
				{
					move t4= new move(this.fileindex,this.rankindex,this.fileindex-1,this.rankindex-1,false,false);
					if(testMove(t4))
					{
						result.add(board[this.fileindex-1][this.rankindex-1]);
					}
					
				}
				
				if(this.fileindex-1==currentfile && this.rankindex==4)
				{
					move t5= new move(this.fileindex,this.rankindex,this.fileindex-1,this.rankindex+1,false,false);
					if(testMove(t5))
					{
						result.add(board[this.fileindex-1][this.rankindex+1]);
					}
				}
				
				
			}
			
			ArrayList<space> attacking=this.AttackedSquares();	//pawns can only move diagonally when attacking
			//System.out.println(attacking.size());
			for(int i=0; i<attacking.size();i++)
			{
				space test=attacking.get(i);
				if(test.occupiedBy!=null)
				{
					if(testMove(new move(this.fileindex, this.rankindex, test.file, test.rank, false, false)))
					{
						result.add(test);
					}
				}
			}
			//System.out.println(result);
			return result;
		}
		
		public ArrayList<space> AttackedSquares()
		{
			ArrayList<space> result=new ArrayList<space>();
			if(this.team)
			{
				if(this.fileindex!=0)
				{
					result.add(board[fileindex-1][rankindex+1]);
				}
				if(this.fileindex!=7)
				{
					result.add(board[fileindex+1][rankindex+1]);
				}
			}
			else
			{
				if(this.fileindex!=0)
				{
					result.add(board[fileindex-1][rankindex-1]);
				}
				if(this.fileindex!=7)
				{
					result.add(board[fileindex+1][rankindex-1]);
				}
			}
			return result;
		}
	}
	
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to refer to the rook piece of the chess. It implements the abstract class piece
	 *
	 */
	static class rook extends piece
	{
		public rook(boolean team, int file, int rank)
		{
			this.team=team;
			this.fileindex=file;
			this.rankindex=rank;
		}
		
		public String toString()
		{
			return this.team ? "wR" : "bR";
		}
		
		public void movePiece(move move)
		{
			
			if(board[move.endindex1][move.endindex2].occupiedBy!=null)
			{
				if(this.team)
				{
					blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
				else
				{
					whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
			}
			board[move.endindex1][move.endindex2].occupiedBy=this;
			board[move.startindex1][move.startindex2].occupiedBy=null;
			this.fileindex=move.endindex1;
			this.rankindex=move.endindex2;
			
		
		}
		
		public ArrayList<space> legalMoves()
		{
			ArrayList<space> result=new ArrayList<space>();
			ArrayList<space> attacking=this.AttackedSquares();	//legal moves are only whatever this piece is attacking minus moves that place yourself in check
			for(int i=0; i<attacking.size();i++)
			{
				space test=attacking.get(i);
				if(testMove(new move(this.fileindex, this.rankindex, test.file, test.rank, false, false)))
				{
					result.add(test);
				}
			}
			return result;
		}
		
		public ArrayList<space> AttackedSquares()
		{
			ArrayList<space> result=new ArrayList<space>();
			
			for(int i=1; fileindex-i>=0;i++)		//4 for loops to check in the 4 directions the piece can go
			{
				result.add(board[fileindex-i][rankindex]);
				if(board[fileindex-i][rankindex].occupiedBy!=null)		//a piece is blocking further movement
				{
					break;
				}
			}
			
			for(int i=1; fileindex+i<=7;i++)
			{
				result.add(board[fileindex+i][rankindex]);
				if(board[fileindex+i][rankindex].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; rankindex-i>=0;i++)
			{
				result.add(board[fileindex][rankindex-i]);
				if(board[fileindex][rankindex-i].occupiedBy!=null)
				{
					break;
				}
				
			}
			
			for(int i=1; rankindex+i<=7;i++)
			{
				result.add(board[fileindex][rankindex+i]);
				if(board[fileindex][rankindex+i].occupiedBy!=null)
				{
					break;
				}
			}
			
			return result;
		}

	}
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to refer to the knight piece of the chess. It implements the abstract class piece
	 *
	 */
	static class knight extends piece
	{
		public knight(boolean team, int file, int rank)
		{
			this.team=team;
			this.fileindex=file;
			this.rankindex=rank;
		}
		
		public String toString()
		{
			return this.team ? "wN" : "bN";
		}
		
		public void movePiece(move move)
		{
			if(board[move.endindex1][move.endindex2].occupiedBy!=null)
			{
				if(this.team)
				{
					blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
				else
				{
					whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
			}
			board[move.endindex1][move.endindex2].occupiedBy=this;
			board[move.startindex1][move.startindex2].occupiedBy=null;
			this.fileindex=move.endindex1;
			this.rankindex=move.endindex2;
			
		}
		
		public ArrayList<space> legalMoves()
		{
			ArrayList<space> result=new ArrayList<space>();
			ArrayList<space> attacking=this.AttackedSquares();
			for(int i=0; i<attacking.size();i++)
			{
				space test=attacking.get(i);
				if(testMove(new move(this.fileindex, this.rankindex, test.file, test.rank, false, false)))
				{
					result.add(test);
				}
			}
			return result;
		}
		
		public ArrayList<space> AttackedSquares()
		{
			ArrayList<space> result=new ArrayList<space>();
			if(fileindex>1)
			{
				if(rankindex>0)
				{
					result.add(board[fileindex-2][rankindex-1]);
				}
				if(rankindex<7)
				{
					result.add(board[fileindex-2][rankindex+1]);
				}
			}
			if(rankindex<6)
			{
				if(fileindex>0)
				{
					result.add(board[fileindex-1][rankindex+2]);
				}
				if(fileindex<7)
				{
					result.add(board[fileindex+1][rankindex+2]);
				}
			}
			if(fileindex<6)
			{
				if(rankindex<7)
				{
					result.add(board[fileindex+2][rankindex+1]);
				}
				if(rankindex>0)
				{
					result.add(board[fileindex+2][rankindex-1]);
				}
			}
			if(rankindex>1)
			{
				if(fileindex>0)
				{
					result.add(board[fileindex-1][rankindex-2]);
				}
				if(fileindex<7)
				{
					result.add(board[fileindex+1][rankindex-2]);
				}
			}
			return result;
		}

	}
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to refer to the bishop piece of the chess. It implements the abstract class piece
	 *
	 */
	static class bishop extends piece
	{
		public bishop(boolean team, int file, int rank)
		{
			this.team=team;
			this.fileindex=file;
			this.rankindex=rank;
		}
		
		public String toString()
		{
			return this.team ? "wB" : "bB";
		}
		
		public void movePiece(move move)
		{
			if(board[move.endindex1][move.endindex2].occupiedBy!=null)
			{
				if(this.team)
				{
					blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
				else
				{
					whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
			}
			board[move.endindex1][move.endindex2].occupiedBy=this;
			board[move.startindex1][move.startindex2].occupiedBy=null;
			this.fileindex=move.endindex1;
			this.rankindex=move.endindex2;
			
		}
		
		public ArrayList<space> legalMoves()
		{
			
			ArrayList<space> result=new ArrayList<space>();
			ArrayList<space> attacking=this.AttackedSquares();
			for(int i=0; i<attacking.size();i++)
			{
				space test=attacking.get(i);
				if(testMove(new move(this.fileindex, this.rankindex, test.file, test.rank, false, false)))
				{
					result.add(test);
				}
			}
			return result;
		}
		
		public ArrayList<space> AttackedSquares()
		{
			ArrayList<space> result=new ArrayList<space>();
			
			for(int i=1; fileindex-i>=0&&rankindex-i>=0;i++)		//4 for loops to check in the 4 directions the piece can go
			{
				result.add(board[fileindex-i][rankindex-i]);
				if(board[fileindex-i][rankindex-i].occupiedBy!=null)		//a piece is blocking further movement
				{
					break;
				}
			}
			
			for(int i=1; fileindex+i<=7&&rankindex-i>=0;i++)
			{
				result.add(board[fileindex+i][rankindex-i]);
				if(board[fileindex+i][rankindex-i].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; rankindex+i<=7&&fileindex-i>=0;i++)
			{
				result.add(board[fileindex-i][rankindex+i]);
				if(board[fileindex-i][rankindex+i].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; rankindex+i<=7&&fileindex+i<=7;i++)
			{
				result.add(board[fileindex+i][rankindex+i]);
				if(board[fileindex+i][rankindex+i].occupiedBy!=null)
				{
					break;
				}
			}
			return result;
		}

	}
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to refer to the queen piece of the chess. It implements the abstract class piece
	 *
	 */
	static class queen extends piece
	{
		public queen(boolean team, int file, int rank)
		{
			this.team=team;
			this.fileindex=file;
			this.rankindex=rank;
		}
		
		public String toString()
		{
			return this.team ? "wQ" : "bQ";
		}
		
		public void movePiece(move move)
		{
			if(board[move.endindex1][move.endindex2].occupiedBy!=null)
			{
				if(this.team)
				{
					blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
				else
				{
					whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
			}
			board[move.endindex1][move.endindex2].occupiedBy=this;
			board[move.startindex1][move.startindex2].occupiedBy=null;
			this.fileindex=move.endindex1;
			this.rankindex=move.endindex2;
			
		}
		
		public ArrayList<space> legalMoves()
		{
			ArrayList<space> result=new ArrayList<space>();
			ArrayList<space> attacking=this.AttackedSquares();
			for(int i=0; i<attacking.size();i++)
			{
				space test=attacking.get(i);
				if(testMove(new move(this.fileindex, this.rankindex, test.file, test.rank, false, false)))
				{
					result.add(test);
				}
			}
			return result;
		}
		
		public ArrayList<space> AttackedSquares()
		{
			ArrayList<space> result=new ArrayList<space>();
			
			for(int i=1; fileindex-i>=0&&rankindex-i>=0;i++)		//combined code from rook and bishop
			{
				result.add(board[fileindex-i][rankindex-i]);
				if(board[fileindex-i][rankindex-i].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; fileindex+i<=7&&rankindex-i>=0;i++)
			{
				result.add(board[fileindex+i][rankindex-i]);
				if(board[fileindex+i][rankindex-i].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; rankindex+i<=7&&fileindex-i>=0;i++)
			{
				result.add(board[fileindex-i][rankindex+i]);
				if(board[fileindex-i][rankindex+i].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; rankindex+i<=7&&fileindex+i<=7;i++)
			{
				result.add(board[fileindex+i][rankindex+i]);
				if(board[fileindex+i][rankindex+i].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; fileindex-i>=0;i++)
			{
				result.add(board[fileindex-i][rankindex]);
				if(board[fileindex-i][rankindex].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; fileindex+i<=7;i++)
			{
				result.add(board[fileindex+i][rankindex]);
				if(board[fileindex+i][rankindex].occupiedBy!=null)
				{
					break;
				}
			}
			
			for(int i=1; rankindex-i>=0;i++)
			{
				result.add(board[fileindex][rankindex-i]);
				if(board[fileindex][rankindex-i].occupiedBy!=null)
				{
					break;
				}
				
			}
			
			for(int i=1; rankindex+i<=7;i++)
			{
				result.add(board[fileindex][rankindex+i]);
				if(board[fileindex][rankindex+i].occupiedBy!=null)
				{
					break;
				}
			}
			//System.out.println(result);
			return result;
		}

	}
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to refer to the king piece of the chess. It implements the abstract class piece
	 *
	 */
	static class king extends piece
	{
		public king(boolean team, int file, int rank)
		{
			this.team=team;
			this.fileindex=file;
			this.rankindex=rank;
			
		}
		
		public String toString()
		{
			return this.team ? "wK" : "bK";
		}
		
		public void movePiece(move move)
		{
			if(board[move.endindex1][move.endindex2].occupiedBy!=null)
			{
				if(this.team)
				{
					blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
				else
				{
					whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
				}
			}
			
			piece p=board[move.startindex1][move.startindex2].occupiedBy;
			if(p.isFirstMove && (move.endindex1==2 && move.endindex2==7))
			{
				board[0][7].occupiedBy.movePiece(new move(0,7,3,7,false,false));
			}
			
			if(p.isFirstMove && (move.endindex1==2 && move.endindex2==0))
			{
				board[0][0].occupiedBy.movePiece(new move(0,0,3,0,false,false));
			}
			
			if(p.isFirstMove && (move.endindex1==6 && move.endindex2==0))
			{
				board[7][0].occupiedBy.movePiece(new move(7,0,5,0,false,false));
			}
			
			if(p.isFirstMove && (move.endindex1==6 && move.endindex2==7))
			{
				board[7][7].occupiedBy.movePiece(new move(7,7,5,7,false,false));
			}
					
			
			board[move.endindex1][move.endindex2].occupiedBy=this;
			board[move.startindex1][move.startindex2].occupiedBy=null;
			this.fileindex=move.endindex1;
			this.rankindex=move.endindex2;
			if(this.team)
			{
				whiteKingLocation=board[move.endindex1][move.endindex2];
			}
			else
			{
				blackKingLocation=board[move.endindex1][move.endindex2];
			}
		}
		
		public ArrayList<space> legalMoves()
		{
			ArrayList<space> result=new ArrayList<space>();
			ArrayList<space> attacking=this.AttackedSquares();
			for(int i=0; i<attacking.size();i++)
			{
				space test=attacking.get(i);
				if(testMove(new move(this.fileindex, this.rankindex, test.file, test.rank, false, false)))
				{
					result.add(test);
				}
			}
			updateAttacks();
			if(!board[fileindex][rankindex].isAttacked)
			{
				if(isFirstMove&& board[0][0].occupiedBy!=null&&team)
				{
					piece p = board[0][0].occupiedBy;
					if(p.isFirstMove && board[1][0].occupiedBy==null && board[2][0].occupiedBy==null && board[3][0].occupiedBy==null&&!board[3][0].isAttacked&&!board[2][0].isAttacked)
					{
						result.add(board[this.fileindex-2][ this.rankindex]);
					}
				}
			
				if(isFirstMove&& board[7][0].occupiedBy!=null&&team)
				{
					piece p = board[7][0].occupiedBy;
					if(p.isFirstMove && board[6][0].occupiedBy==null && board[5][0].occupiedBy==null&&!board[6][0].isAttacked&&!board[5][0].isAttacked)
					{
						result.add(board[this.fileindex+2][this.rankindex]);
					}
				}
			
				if(isFirstMove&& board[0][7].occupiedBy!=null&&!team)
				{
					piece p = board[0][7].occupiedBy;
					if(p.isFirstMove && board[1][7].occupiedBy==null && board[2][7].occupiedBy==null && board[3][7].occupiedBy==null&&!board[3][7].isAttacked&&!board[2][7].isAttacked)
					{
						result.add(board[this.fileindex-2][ this.rankindex]);
					}
				}
			
				if(isFirstMove&& board[7][7].occupiedBy!=null&&!team)
				{
					piece p = board[7][7].occupiedBy;
					if(p.isFirstMove && board[6][7].occupiedBy==null && board[5][7].occupiedBy==null&&!board[6][7].isAttacked&&!board[5][7].isAttacked)
					{
						result.add(board[this.fileindex+2][ this.rankindex]);
					}
				}
			}
			
			
			return result;
		}
		
		public ArrayList<space> AttackedSquares()
		{
			ArrayList<space> result=new ArrayList<space>();
			if(rankindex==7)
			{
				if(fileindex==0)
				{
					result.add(board[fileindex+1][rankindex]);
					result.add(board[fileindex+1][rankindex-1]);
					result.add(board[fileindex][rankindex-1]);
				}
				else if(fileindex==7)
				{
					result.add(board[fileindex-1][rankindex-1]);
					result.add(board[fileindex-1][rankindex]);
					result.add(board[fileindex][rankindex-1]);
				}
				else
				{
					result.add(board[fileindex-1][rankindex-1]);
					result.add(board[fileindex-1][rankindex]);
					result.add(board[fileindex+1][rankindex]);
					result.add(board[fileindex+1][rankindex-1]);
					result.add(board[fileindex][rankindex-1]);
				}
			}
			else if(rankindex==0)
			{
				if(fileindex==0)
				{
					result.add(board[fileindex][rankindex+1]);
					result.add(board[fileindex+1][rankindex+1]);
					result.add(board[fileindex+1][rankindex]);
				}
				else if(fileindex==7)
				{
					result.add(board[fileindex-1][rankindex]);
					result.add(board[fileindex-1][rankindex+1]);
					result.add(board[fileindex][rankindex+1]);
				}
				else
				{
					result.add(board[fileindex-1][rankindex]);
					result.add(board[fileindex-1][rankindex+1]);
					result.add(board[fileindex][rankindex+1]);
					result.add(board[fileindex+1][rankindex+1]);
					result.add(board[fileindex+1][rankindex]);
				}
			}
			else if(fileindex==0)
			{
				result.add(board[fileindex][rankindex+1]);
				result.add(board[fileindex+1][rankindex+1]);
				result.add(board[fileindex+1][rankindex]);
				result.add(board[fileindex+1][rankindex-1]);
				result.add(board[fileindex][rankindex-1]);
			}
			else if(fileindex==7)
			{
				result.add(board[fileindex-1][rankindex-1]);
				result.add(board[fileindex-1][rankindex]);
				result.add(board[fileindex-1][rankindex+1]);
				result.add(board[fileindex][rankindex+1]);
				result.add(board[fileindex][rankindex-1]);
			}
			else
			{
				result.add(board[fileindex-1][rankindex-1]);
				result.add(board[fileindex-1][rankindex]);
				result.add(board[fileindex-1][rankindex+1]);
				result.add(board[fileindex][rankindex+1]);
				result.add(board[fileindex+1][rankindex+1]);
				result.add(board[fileindex+1][rankindex]);
				result.add(board[fileindex+1][rankindex-1]);
				result.add(board[fileindex][rankindex-1]);
			}
			return result;
		}

	}
	
	/**
	 * @author Ria Parikh Alexander Osterman
	 * Class to keep track on each particular space(square) on the chessboard
	 */
	static class space
	{
		/**
		 *  Variable to keep track of the file of that particular space(square)
		 */
		int file;	
		/**
		 * Variable to keep track of the rank of that particular space(square)
		 */
		int rank;
		/**
		 *  Boolean variable to keep track of the color of the space(square)
		 *  If it is true, then it means it is a white square, and false means it is a black square
		 */
		boolean lightSquare;	//for drawing the board only
		/**
		 *  A boolean variable to keep track of which specific chess piece lies on that square
		 */
		piece occupiedBy;
		/**
		 * A boolean variable to check if that particular square is under attack or not
		 */
		boolean isAttacked;
		/**
		 * A constructor to initialize the square with it's particular file, rank and lightsquare (cololr)
		 * @param file
		 * @param rank
		 * @param lightSquare
		 */
		public space(int file, int rank, boolean lightSquare)
		{
			this.file=file;
			this.rank=rank;
			this.lightSquare=lightSquare;
		}
		
		/**
		 *  @return the file and rank of that particular space(box)
		 */
		public String toString()
		{
			return ""+file+" "+rank;
		}
	}
	
	static class move
	{
		/**
		 *  Variable to refer to the fileindex of the current position of the piece
		 */
		int startindex1;
		/**
		 * Variable to refer to the rankindex of the current position of the piece
		 */
		int startindex2;
		/**
		 *  Variable to refer to the fileindex of the position the piece is to be moved to
		 */
		int endindex1;
		/**
		 *  Variable to refer to the rankindex of the position the piece is to be moved to
		 */
		int endindex2;
		/**
		 *  A boolean variable that is set to True in case of a draw
		 */
		boolean draw;
		/**
		 * A boolean variable that is set to True in case if one of the player resigns
		 */
		boolean resign;
		/**
		 *  A variable to refer to what specific piece a pawn is being promoted to in case of a promotion
		 */
		int promotion; 
		
		/**
		 * Constructor to initialize the respective fields of an object of the move class
		 * @param startindex1
		 * @param startindex2
		 * @param endindex1
		 * @param endindex2
		 * @param draw
		 * @param resign
		 * @param promotion
		 */
		public move(int startindex1, int startindex2, int endindex1, int endindex2, boolean draw, boolean resign, int promotion)
		{
			this.startindex1=startindex1;
			this.startindex2=startindex2;
			this.endindex1=endindex1;
			this.endindex2=endindex2;
			this.draw=draw;
			this.resign=resign;
			this.promotion=promotion;
		}
		
		public move(int startindex1, int startindex2, int endindex1, int endindex2, boolean draw, boolean resign)
		{
			this.startindex1=startindex1;
			this.startindex2=startindex2;
			this.endindex1=endindex1;
			this.endindex2=endindex2;
			this.draw=draw;
			this.resign=resign;
			this.promotion=0;
		}
	}
	
    /**
     * The main function in order to get the chess game running
     * @param args
     */
    public static void main(String args[])
    {
    	initialize();
        while(!isGameOver())
        {	
        	drawboard();
        	updateAttacks();
        	if(turn)	//just prints check if king is in check
        	{
        		if(whiteKingLocation.isAttacked)
        		{
        			System.out.println("Check");
        		}
        	}
        	else
        	{
        		if(blackKingLocation.isAttacked)
        		{
        			System.out.println("Check");
        		}
        	}
        	
			move move = parseinput();
			
        	while(!isValidInput(move))	//keep trying until there's a valid input
        	{
				move=parseinput();
        	}
        	execute(move);
        }
        printResult();
    }
    
    /**
     *  A method to draw the chessboard in order for the user to view it
     */
    private static void drawboard()
    {
    	for(int i=7; i>=0;i--)	//going backwards because the 8th rank is on top
    	{
    		for(int j=0; j<8;j++)
    		{
    			if(board[j][i].occupiedBy!=null)
    			{
    				piece piece=board[j][i].occupiedBy;
    				
    				System.out.print(piece.toString());
    			}
    			else
    			{
    				if(board[j][i].lightSquare)
    				{
    					System.out.print("  ");
    				}
    				else
    				{
    					System.out.print("##");
    				}
    			}
    			System.out.print(" ");
    		}
    		System.out.println(i+1);
    	}
    	System.out.println(" a  b  c  d  e  f  g  h\n");
    }
    
    /**
     * A method to get the next specific move from the user
     * @return move. (The specific move to be performed on what particular piece)
     */
    private static move parseinput()
    {
    	if(turn)
    	{
    		System.out.print("White's move: ");
    	}
    	else
    	{
    		System.out.print("Black's move: ");
    	}
    	
    	BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
    	String inputString=null;
		try {
			inputString = input.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String[] tokens=inputString.split(" ");
    	
    	if(tokens[0].equals("resign"))
    	{
    		return new move(-1, -1, -1, -1, false, true);
    	}
    	
    	int file1=(int) (tokens[0].charAt(0)-'a');
    	int rank1=(int) (tokens[0].charAt(1)-'1');
    	
    	int file2=(int) (tokens[1].charAt(0)-'a');
    	int rank2=(int) (tokens[1].charAt(1)-'1');
    	
    	move move=new move(file1, rank1, file2, rank2, false, false);
    	
    	if(tokens.length==3)
    	{
    		if(tokens[2].equals("draw?"))
    		{
    			move.draw=true;
    		}
    		else if(tokens[2].equals("Q"))
    		{
    			move.promotion=0;
    		}
    		else if(tokens[2].equals("R"))
    		{
    			move.promotion=1;
    		}
    		else if(tokens[2].equals("N"))
    		{
    			move.promotion=2;
    		}
    		else if(tokens[2].equals("B"))
    		{
    			move.promotion=3;
    		}
    	}
    	
    	return move;
    }
    
    /** A method to check if the particular value inputted by the user is valid or not
     * @param input 
     * @return True if the input is valid, false is the input is not valid
     */
    private static boolean isValidInput(move input)
    {
    	if(input.resign)
    	{
    		return true;
    	}
    	
    	piece piece=board[input.startindex1][input.startindex2].occupiedBy;
    	if(piece==null||piece.team!=turn)
    	{
    		System.out.println("Illegal move, try again");
    		return false;
    	}
    	if(piece.legalMoves()!=null&&piece.legalMoves().contains(board[input.endindex1][input.endindex2]))	//if the space is within the pieces legal moves then return true
    	{
    		return true;
    	}
    	System.out.println("Illegal move, try again");
    	return false;
    }
    
    /** A method to perform the move inoutted by the user on a specific piece in the game
     * @param move to be executed
     */
    private static void execute(move move)
    {
    	turn=!turn;
    	if(move.resign)
    	{
    		resign=true;
    		return;
    	}
    	if(move.draw)
    	{
    		draw=true;
    	}
    	twoSteps=-1;
    	//System.out.println("real move");
    	piece movingPiece=board[move.startindex1][move.startindex2].occupiedBy;
    	movingPiece.movePiece(move);
    	movingPiece.isFirstMove=false;
    	promoted=false;
    	System.out.println();
    }
    
    /** A method to check if the game is over, or if it is to be conitnued playing
     * @return True if there is a checkmate, draw or a resign. False otherwise
     */
    private static boolean isGameOver()
    {
    	if(checkmate())
    	{
    		return true;
    	}
    	/*else if(stalemate())	//not required for this project
    	{
    		return true;
    	}*/
    	else if(draw)
    	{
    		return true;
    	}
    	else if(resign)
    	{
    		return true;
    	}
    	else return false;
    }
    
    /**
     * A method to check if there is a checkmate in the game or not
     * @return True if there is a checkmate, false if there is not checkmate
     */
    private static boolean checkmate()
    {
    	if(turn)
    	{
    		if(whiteKingLocation.isAttacked)
    		{
    			for(int i=0; i<whitePieces.size();i++)		//checks every piece to see if there are legal moves
    			{
    				if(whitePieces.get(i).legalMoves().size()!=0)
    				{
    					return false;		//if there's legal move then return
    				}
    			}
    			return true;	//no legal moves found so it's checkmate
    		}
    	}
    	else	//same thing just black pieces
    	{
    		if(blackKingLocation.isAttacked)
    		{
    			//System.out.println("attacked");
    			for(int i=0; i<blackPieces.size();i++)		//checks every piece to see if there are legal moves
    			{
    				if(blackPieces.get(i).legalMoves().size()!=0)
    				{
    					return false;		//if there's legal move then return
    				}
    			}
    			return true;	//no legal moves found so it's checkmate
    		}
    	}
    	return false;
    }
    
   /* private static boolean stalemate()
    {
    	//pretty much exact same code but this time the king isn't in check
    	//since the checkmate function would've checked if king is in check, we don't need to check if it's not in check this time
    	if(turn==0)
    	{
    		for(int i=0; i<whitePieces.size();i++)		//checks every piece to see if there are legal moves
    		{
    			if(whitePieces.get(i).legalMoves()!=null)
    			{
    				return false;		//if there's legal move then return
    			}
    		}
    		return true;	//no legal moves found so it's checkmate
    	}
    	else	//same thing just black pieces
    	{
    		for(int i=0; i<blackPieces.size();i++)		//checks every piece to see if there are legal moves
    		{
    			if(blackPieces.get(i).legalMoves()!=null)
    			{
    				return false;		//if there's legal move then return
    			}
    		}
    		return true;	//no legal moves found so it's checkmate
    	}
    }*/
    
    /**
     *  A method to print the result of the game
     */
    private static void printResult()
    {
    	if(checkmate())
    	{
    		drawboard();
    		System.out.println("Checkmate");
    		if(turn)
    		{
    			System.out.println("Black wins");
    		}
    		else
    		{
    			System.out.println("White wins");
    		}
    	}
    	else if(resign)
    	{
    		if(turn)
    		{
    			System.out.println("White wins");
    		}
    		else
    		{
    			System.out.println("Black wins");
    		}
    	}
    	else if(draw)
    	{
    		drawboard();
    		if(turn)
        	{
        		System.out.print("White's move: ");
        	}
        	else
        	{
        		System.out.print("Black's move: ");
        	}
        	
        	BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
        	try {
				String inputString=input.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    /**
     *  A method to intialize the chessboard
     */
    private static void initialize()	//sets up the board
    {
    	boolean light=true;
    	for(int i=0; i<8; i++)	//this creates all the spaces on the board
    	{
    		light=!light;	//when changing ranks need to alternate one more time
    		for(int j=0; j<8; j++)
    		{
    			board[i][j]=new space(i, j, light);
    			light=!light;	//to alternate the colors
    		}
    	}
    	piece piece=null;
    	for(int i=0; i<8; i++)	//for loop for creating all the pawns
    	{
    		piece=new pawn(true, i, 1);	//creating white pawns
    		board[i][1].occupiedBy=piece;
    		whitePieces.add(piece);
    		
    		piece=new pawn(false, i ,6);	//creating black pawns
    		board[i][6].occupiedBy=piece;
    		blackPieces.add(piece);
    	}
    	piece=new rook(true, 0, 0);		//creating all the other pieces manually
    	board[0][0].occupiedBy=piece;	//white rook a1
    	whitePieces.add(piece);
    	
    	piece=new rook(true, 7, 0);
    	board[7][0].occupiedBy=piece;	//white rook h1
    	whitePieces.add(piece);
    	
    	piece=new rook(false, 0, 7);
    	board[0][7].occupiedBy=piece;	//black rook a8
    	blackPieces.add(piece);
    	
    	piece=new rook(false, 7, 7);
    	board[7][7].occupiedBy=piece;	//black rook h8
    	blackPieces.add(piece);
    	
    	
    	piece=new knight(true, 1, 0);
    	board[1][0].occupiedBy=piece;	//white knight b1
    	whitePieces.add(piece);
    	
    	piece=new knight(true, 6, 0);
    	board[6][0].occupiedBy=piece;	//white knight g1
    	whitePieces.add(piece);
    	
    	piece=new knight(false, 1, 7);
    	board[1][7].occupiedBy=piece;	//black knight b8
    	blackPieces.add(piece);
    	
    	piece=new knight(false, 6, 7);
    	board[6][7].occupiedBy=piece;	//black knight g8
    	blackPieces.add(piece);
    	
    	
    	piece=new bishop(true, 2, 0);
    	board[2][0].occupiedBy=piece;	//white bishop c1
    	whitePieces.add(piece);
    	
    	piece=new bishop(true, 5, 0);
    	board[5][0].occupiedBy=piece;	//white bishop f1
    	whitePieces.add(piece);
    	
    	piece=new bishop(false, 2, 7);
    	board[2][7].occupiedBy=piece;	//black bishop c8
    	blackPieces.add(piece);
    	
    	piece=new bishop(false, 5, 7);
    	board[5][7].occupiedBy=piece;	//black bishop f8
    	blackPieces.add(piece);
    	
    	
    	piece=new queen(true, 3, 0);
    	board[3][0].occupiedBy=piece;	//white queen d1
    	whitePieces.add(piece);
    	
    	piece=new queen(false, 3, 7);
    	board[3][7].occupiedBy=piece;	//black queen d8
    	blackPieces.add(piece);
    	
    	
    	piece=new king(true, 4, 0);
    	board[4][0].occupiedBy=piece;	//white king e1
    	whitePieces.add(piece);
    	whiteKingLocation=board[4][0];
    	
    	piece=new king(false, 4, 7);
    	board[4][7].occupiedBy=piece;	//black king e8
    	blackPieces.add(piece);
    	blackKingLocation=board[4][7];
    }
    
    /**
     *  A method that checks which squares are under attack after each move made by the users
     */
    private static void updateAttacks()
    {
    	for(int i=0; i<8;i++)	//reset board
    	{
    		for(int j=0; j<8; j++)
    		{
    			board[i][j].isAttacked=false;
    		}
    	}
    	if(turn)	//if it's white turn then black is doing the attacking
    	{
    		for(int i=0; i<blackPieces.size();i++)
    		{
    			ArrayList<space> spaces=blackPieces.get(i).AttackedSquares();
    			for(int j=0; j<spaces.size(); j++)
    			{
    				spaces.get(j).isAttacked=true;
    			}
    		}
    	}
    	else		//vice versa
    	{
    		for(int i=0; i<whitePieces.size();i++)
    		{
    			ArrayList<space> spaces=whitePieces.get(i).AttackedSquares();
    			for(int j=0; j<spaces.size(); j++)
    			{
    				spaces.get(j).isAttacked=true;
    			} 
    		}
    	}
    }
    
    /**
     * A method that reverts that particular move and resets the chessboard to the way it was before that particular move
     * @param move ;  what move is to be performed
     * @param piece ; piece to be moved
     */
    private static void revertBoard(move move, piece piece)
    {
    	if(promoted)	//need to revert a piece back into pawn form
    	{
    		piece pawn=new pawn(board[move.endindex1][move.endindex2].occupiedBy.team, move.startindex1, move.startindex2);
    		if(pawn.team)
    		{
    			whitePieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
    			whitePieces.add(pawn);
    		}
    		else
    		{
    			blackPieces.remove(board[move.endindex1][move.endindex2].occupiedBy);
    			blackPieces.add(pawn);
    		}
    		board[move.startindex1][move.startindex2].occupiedBy=pawn;
    		board[move.endindex1][move.endindex2].occupiedBy=piece;
    		promoted=false;
    	}
    	else
    	{
    		board[move.startindex1][move.startindex2].occupiedBy=board[move.endindex1][move.endindex2].occupiedBy;
    		board[move.startindex1][move.startindex2].occupiedBy.fileindex=move.startindex1;
    		board[move.startindex1][move.startindex2].occupiedBy.rankindex=move.startindex2;
    		board[move.endindex1][move.endindex2].occupiedBy=piece;
    	}
    	
    	if(piece!=null)
    	{
    		if(piece.team)
    		{
    			whitePieces.add(piece);
    		}
    		else
    		{
    			blackPieces.add(piece);
    		}
    	}
    	
    	if(twoSteps!=-1)	//checking is an en passant was made, have to manually put the piece back
    	{
    		if(turn)
    		{
        		if(board[twoSteps][4].occupiedBy==null)	//if it's null then the pawn was removed by en passant, there's no other way it could've happened
        		{										//because if twoSteps was set then the pawn just moved here so it has to be occupied. and any other piece taking this square would put that piece there, not null
        			piece pawn=new pawn(false, twoSteps, 4);
        			blackPieces.add(pawn);
        			board[twoSteps][4].occupiedBy=pawn;
        		}
    		}
    		else
    		{
    			if(board[twoSteps][3].occupiedBy==null)
        		{
    				piece pawn=new pawn(true, twoSteps, 3);
        			whitePieces.add(pawn);
        			board[twoSteps][3].occupiedBy=pawn;
        		}
    		}
    	}
    }
    
    /**
     * A method to check if that particular move could be the next valid move
     * @param move ; move to be performed
     * @return ; True if the particular move can be performed, false if the move cannot be performed
     */
    private static boolean testMove(move move)
    {
    	//System.out.println("test move");
    	piece prevPiece=board[move.endindex1][move.endindex2].occupiedBy;
    	if(prevPiece!=null&&prevPiece.team==board[move.startindex1][move.startindex2].occupiedBy.team)
    	{
    		return false;
    	}
    	int temp=twoSteps;
    	board[move.startindex1][move.startindex2].occupiedBy.movePiece(move);
    	twoSteps=temp;
    	updateAttacks();
    	revertBoard(move, prevPiece);
    	if(turn)
    	{
    		return !whiteKingLocation.isAttacked;
    	}
    	else
    	{
    		return !blackKingLocation.isAttacked;
    	}
    }
}