/**
 * Marbles.java
 * This program uses 2D arrays to play a game similar to Checkers. The
user must press the mouse over a certain position and marble.
 * If there is a potential space for the marble to move to, it becomes
highlighted. The user can then click on any highlighted space
 * to move the marble there. In this process, the cell the marble was
originally at becomes empty, the one in the middle becomes empty
 * as well, and the new space the marble moves to becomes filled. The user
can choose to reset the board (either 7 by 7 or 9 by 9) at any
 * time during the game. If the user manages to empty out the entire board
with the exception of one marble, the user wins and a winning
 * message is printed. If there is more than one marble on the board and
the user is stuck, a losing message is printed.
 * @author Arushi Arora
 * @version 1.0
 * @since 1/9/2018
 */
import java.awt.Color;
import java.awt.Font;

public class Marbles
{
        
        private int [][] board;
        private int pause;
        private int xposition, yposition;

        
        public Marbles ( )
        {
                Font font = new Font("Arial", Font.BOLD, 18);
                StdDraw.setFont(font);
                xposition = yposition = -5;
                pause = 50;
                board = new int[][]{{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},
                                                {1,1,1,1,1,1,1,1,1},{1,1,1,1,0,1,1,1,1},{1,1,1,1,1,1,1,1,1},
                                                {-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1}};
        }

        /**
         *  Sets up and runs the game of Marbles.
         *  @param  args     An array of String arguments (not used here).
         */
        public static void main(String [] args)
        {
                Marbles run = new Marbles();
                run.playGame();
        }

        /**
         *  This method plays the game of Marbles. It calls the drawBoard method first. Then it uses the StdDraw class
         *  to check where the user has pressed the mouse. It calls the possibleMoveSpace method with the values of the
         *  current position of the mouse and the place where the user wants to move. If the user clicks on a possible move
         *  space, the marble they highlighted moves to the new space, and the marble previously in the middle is replaced
         *  with an empty cell. 
         */
        public void playGame ( )
        {
                boolean done = false;
                do
                {
                        drawBoard();
                        if(StdDraw.mousePressed())
                        {
                                double x = StdDraw.mouseX();
                                double y = StdDraw.mouseY();
                                int checkx = (int)(10 * x - 0.5);
                                int checky = (int)(10 * y - 0.5);
                                if(reset(x,y))
                                {
                                        xposition = yposition = -5;
                                }
                                else if(possibleMoveSpace(xposition,yposition,checkx,checky))
                                {

                                        //  You'll need to figure out how to "jump" from xposition, ypositionto checkx, checky.
                                        board[xposition][yposition] = 0;
                                        board[checkx][checky] = 1;
                                        board[(xposition+checkx)/2][(yposition+checky)/2] = 0;

                                        StdDraw.show(4 * pause);
                                }
                                else
                                {
                                        xposition = checkx;
                                        yposition = checky;
                                        StdDraw.show(pause);
                                }
                        }
                        StdDraw.show(pause);
                }
                while(!done);
        }

        /**
         *  This method draws the playing board. It sets the background, calls the draw cell method to 
         *  draw each cell with a marble or with appropriate highlighting, and then calls the drawResetButtons method
         *  and drawWinOrLoseMessage methods.
         */
        public void drawBoard ( )
        {
               
                StdDraw.setPenColor(new Color(0,0,0));
                StdDraw.filledSquare(0.5,0.5,0.5);
                StdDraw.setPenColor(new Color(0, 102, 204));
                StdDraw.filledSquare(0.5,0.5,0.475);
                for ( int i = 0; i < board.length; i++ )
                {
                        for ( int j = 0; j < board[i].length; j++ )
                        {
                                if(board[i][j] != -1)
                                {
                                        drawCell(i,j);
                                }
                        }
                }
                drawResetButtons();
                drawWinOrLoseMessage();

        }

        /**
         *  This method draws the reset buttons to reset the 7 by 7 and 9 by 9 boards using the StdDraw class.
         */
        public void drawResetButtons ( )
        {
                StdDraw.setPenColor(new Color(255,0, 0));
                StdDraw.filledRectangle(0.8, 0.25, 0.125, 0.05);
                StdDraw.filledRectangle(0.8, 0.12, 0.125, 0.05);
                StdDraw.setPenColor(new Color(255,255,255));
                StdDraw.text(0.8, 0.25, "RESET 7 x 7");
                StdDraw.text(0.8, 0.12, "RESET 9 x 9");
        }

        /**
         *  This method draws a win or lose message if the game is finished. If there is only one marble left,
         *  the game has ended and the user won. If there are multiple marbles and no possible move spaces, the
         *  user loses.
         */
        public void drawWinOrLoseMessage ( )
        {
            if (gameIsFinished() && countMarbles() == 1)
            {
                 StdDraw.setPenColor(new Color(255,0,0));
                StdDraw.filledRectangle(0.18, 0.75, 0.125, 0.05);
                StdDraw.setPenColor(new Color(255,255,255));
                StdDraw.text(0.18, 0.75, "YOU WIN!");
            }
            else if (gameIsFinished() && countMarbles() > 1)
            {
                 StdDraw.setPenColor(new Color(255,0,0));
                StdDraw.filledRectangle(0.18, 0.75, 0.125, 0.05);
                StdDraw.setPenColor(new Color(255,255,255));
                StdDraw.text(0.18, 0.75, "YOU LOSE!");
            }
                

        }

        /**
         *  This method resets the board. A 1 represents a cell with a marble, a 0 represents an empty cell, and -1 is
         *  used for cells that are not part of the board. The first section represents a 7 by 7 board, and the second
         *  is for a 9 by 9 board.
         */
        public boolean reset(double x, double y)
        {
                if (x>=0.675 && x <=0.925 && y>=0.20 && y<=0.3)
                {
                        board = new int[][]{{-1,-1,-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},
                                                {-1,1,1,1,1,1,1,1,-1},{-1,1,1,1,0,1,1,1,-1},{-1,1,1,1,1,1,1,1,-1},
                                                {-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1,-1,-1}};

                }

                if (x>=0.675 && x <=0.925 && y>=0.07 && y<=0.17)
                {
                        board = new int[][]{{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},
                                                {1,1,1,1,1,1,1,1,1},{1,1,1,1,0,1,1,1,1},{1,1,1,1,1,1,1,1,1},
                                                {-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1},{-1,-1,-1,1,1,1,-1,-1,-1}};

                }
                return false;
        }

        /**
         *  This method draws each cell. It first fills a square with the background color. If the particular cell
         *  is a possible move space for the marble the user has highlighted, that cell is highlighted in 
         *  red. If the cell is not an empty one, the marble image is drawn in. 
         */
        public void drawCell(int x, int y)
        {
                StdDraw.setPenColor(new Color(0,0,0));
                StdDraw.filledSquare(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.055);
                StdDraw.setPenColor(new Color(255,255,255));
                StdDraw.filledSquare(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.0425);
                StdDraw.setPenColor(new Color(200,200,200));
                StdDraw.filledCircle(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.02);
                if(x == xposition && y == yposition && board[x][y] == 1)
                {
                        StdDraw.setPenColor(new Color(0,0,0));
                        StdDraw.filledSquare(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.05);
                        StdDraw.setPenColor(new Color(230,30,30));
                        StdDraw.filledCircle(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.04);
                }
                if(possibleMoveSpace(xposition,yposition,x,y))
                {
                        StdDraw.setPenColor(new Color(0,0,0));
                        StdDraw.filledSquare(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.05);
                        StdDraw.setPenColor(new Color(255, 0, 0));
                        StdDraw.filledCircle(0.1 + 0.1 * x, 0.1 + 0.1 * y, 0.03);
                }
                if(board[x][y] == 1)
                {
                        StdDraw.picture(0.1 + 0.1 * x, 0.1 + 0.1 * y,"marble.png");
                }
        } 

        /**
         *  This method checks to see if there is a possible move space at xval, yval. The user clicks on the marble
         *  they want to move, and its coordinates are represented by x and y. If there is an adjacent marble
         *  to the one they highlighted, and an empty space 2 spaces away from the marble they highlighted, and next to 
         *  the adjacent marble, it is a possible move space. 
         */
        public boolean possibleMoveSpace(int x, int y, int xval, int yval)
        {
                //if the xval and yval pos is empty and it is exactly 2 spaces awayright/left and up/down then return true
                if (board[xval][yval] == 0 && ( (y == yval && (x+2 == xval || x-2 ==xval)) || (x == xval && (y+2 == yval || y-2 == yval))))
                  {
                     if (board[x][y] != 0)
                            
                        if (board[(x+xval)/2][(y+yval)/2] == 1)
                            return true;
                  }

                return false;
        }

        /**
         *  This method checks to see if the game is finished. If the user won the game, there should only be
         *  one marble left on the board. This is the only way to win. The user loses if they have multiple marbles
         *  left on the board and no possible moves left.
         */
        public boolean gameIsFinished()
        {
            if (countMarbles() == 1)
            {
                return true;
                
            }
            else if (countMarbles() > 1)
            {
                for (int i = 0; i<board.length; i++)
                {
                    for (int j = 0; j<board[i].length; j++)
                    {
                        if (board[i][j] == 1)
                        {
                            for (int a = 0; a<board.length; a++)
                            {
                                for (int b = 0; b<board[a].length; b++)
                                {
                                    if (possibleMoveSpace(i, j, a, b))
                                    {
                                        return false;
                                    }
                                }
                            }
 
                        }
                    }
            }
                
            }
                return true;
        }

        /**
         *  This method counts the number of remaining marbles on the board. If there is only one marble left,
         *  the user has won the game.
         */
        public int countMarbles ( )
        {
            int count = 0;
            for (int i = 0; i<board.length; i++)
            {
                for (int j = 0; j<board[i].length; j++)
                {
                    if (board[i][j] == 1) //if a marble is there
                    {
                        count++; //the number of remaining marbles
                    }
                }
            }
            //System.out.println(count);
          return count;
        }
}
