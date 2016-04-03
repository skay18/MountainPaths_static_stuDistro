import java.util.*;
import java.io.*;
import java.awt.*;

public class MountainPaths 
{
    
  public static void main(String[] args) throws Exception{
      
       //construct DrawingPanel, and get its Graphics context
        DrawingPanel panel = new DrawingPanel(844, 480);
        Graphics g = panel.getGraphics();
        
        //Test Step 1 - construct mountain map data
        Scanner S = new Scanner(new File("Colorado_844x480.dat"));
        int[][] grid = read(S, 480, 844);
        
        //Test Step 2 - min, max
        int min = findMinValue(grid);
        System.out.println("Min value in map: "+min);
        
        int max = findMaxValue(grid);
        System.out.println("Max value in map: "+max);
        
        
        //Test Step 3 - draw the map
        //drawMap(g, grid);
        
        //Test Step 4 - draw a greedy path
        
        // 4.1 implement indexOfMinInCol
        ArrayList<Integer> minRow = indexOfMinInCol(grid, 0); // find the index for smallest value in col 0
        System.out.println("Row with lowest val in col 0: "+ minRow);
        
        // 4.2 use minRow as starting point to draw path
        g.setColor(Color.RED); //can set the color of the 'brush' before drawing, then method doesn't need to worry about it
        
        // draws smallest path starting from col 0 for all smallest start points
        ArrayList<Integer> distances = new ArrayList<Integer>();
        for (int i = 0; i < minRow.size(); i++){
        int newRow = minRow.get(i);
        int totalChange = drawLowestElevPath(g, grid, newRow);
        distances.add(totalChange);
        }
        
        int smallDist = distances.get(0);
        for (int d = 1; d < distances.size(); d++)
        {
          if (smallDist > distances.get(d))
            smallDist = distances.get(d);
        }
        
        System.out.println("Lowest-Elevation-Change Path starting at row "+minRow+" gives total change of: "+smallDist);
        
        //Test Step 5 - draw the best path
        g.setColor(Color.RED);
        int bestRow = indexOfLowestElevPath(g, grid, minRow);
        
        //map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.GREEN); //set brush to green for drawing best path
        int totalChange = drawLowestElevPath(g, grid, bestRow);
        System.out.println("The Lowest-Elevation-Change Path starts at row: "+bestRow+" and gives a total change of: "+totalChange);
        
    
    
    }
  
  /**
   * @param S a Scanner instantiated and pointing at a file
   * @param numRows the number of rows represented in the file
   * @param numCols the number of cols represented in the file
   * @return a 2D array (rows x cols) of the data from the file read
   */
  public static int[][] read(Scanner S, int numRows, int numCols)
  {
    // makes new grid using dimensions of rows by columns  
    int[][] grid = new int[numRows][numCols];
    
    // puts numbers in grid based on scanner input from text file
    for (int row = 0; row < numRows; row++)
    {
        for (int column = 0; column < numCols; column++)
        {
            Scanner num = S;
            grid [row][column] = num.nextInt();
        }
    }
    
    // returns grid completely filled
       return grid;
  }
  
  
  /**
   * @param grid a 2D array from which you want to find the smallest value
   * @return the smallest value in the given 2D array
   */
  public static int findMinValue(int[][] grid){
      
      // assumes first number in grid is smallest
      int smallest = grid[0][0];
      
      // runs for all values in grid
      for (int i = 0; i < grid.length; i++)
      {
          for (int j = 0; j < grid[0].length; j++)
          {
            // replaces number if smaller 
            if (grid[i][j] < smallest)
                  smallest = grid[i][j];
          }
      }
      
      return smallest;
  }
  
  /**
   * @param grid a 2D array from which you want to find the largest value
   * @return the largest value in the given 2D array
   */
  public static int findMaxValue(int[][] grid){
     
    // assumes first number in grid is largest
    int largest = grid[0][0];
    
    // runs for all values in grid
    for (int i = 0; i < grid.length; i++)
    {
        for (int j = 0; j < grid[0].length; j++)
        {
            // replaces number if larger
            if (grid[i][j] > largest)
            {  
              largest = grid[i][j];
            }
        }
    }
            
    return largest;
      
  }
  
  /**
   * Given a 2D array of elevation data create a image of size rows x cols, 
   * drawing a 1x1 rectangle for each value in the array whose color is set
   * to a a scaled gray value (0-255).  Note: to scale the values in the array 
   * to 0-255 you must find the min and max values in the original data first.
   * @param g a Graphics context to use
   * @param grid a 2D array of the data
   */
  public static void drawMap(Graphics g, int[][] grid){
    
    // initializes variables
    int num = 0;
    int gray = 0;
    
    // gets min and max using previous-made methods
    int minimum = findMinValue(grid);
    int maximum = findMaxValue(grid);
    
    // runs for all numbers in grid
    for (int i = 0; i < grid.length; i++)
    {
        for (int j = 0; j < grid[0].length; j++)
        {
            num = grid[i][j];
            
            // formula to scale numbers down
            gray = (num - minimum) * 255 / (maximum - minimum);
            
            // draws out new graph as grayscale
            g.setColor(new Color(gray, gray, gray));
            g.fillRect(j, i, 1,1);
        }
    }
  }
  
  /**
   * Scan a single column of a 2D array and return the index of the
   * row that contains the smallest value
   * @param grid a 2D array
   * @col the column in the 2D array to process
   * @return the index of smallest value from grid at the given col
   */
  public static ArrayList <Integer>indexOfMinInCol(int[][] grid, int col){
      
       // initializes arraylist to hold all indexes that have smallest num
       ArrayList<Integer> indexOfMin = new ArrayList<Integer>();
       
       // assumes first number in column is smallest
       int minNum = grid [0][col];
       
       // runs for all rows
       for (int i = 0; i < grid.length; i++)
       {
         
        // replaces minNum if the row's number is smaller
        if (grid[i][col] < minNum)
        {
          minNum = grid[i][col];
         }         
        
        
      }
      
      // since the smallest num in row obtained, now checks for how many times it occurs
      // within the same row (would cause different starting points for paths)
      for (int i = 0; i < grid.length; i++)
       {
        if (grid[i][col] == minNum)
        {
          // adds to arraylist
          indexOfMin.add(i);
         }         
      } 
      return indexOfMin;
  }
  
  
  /**
   * Find the minimum elevation-change route from West-to-East in the given grid, from the
   * given starting row, and draw it using the given graphics context
   * @param g - the graphics context to use
   * @param grid - the 2D array of elevation values
   * @param row - the starting row for traversing to find the min path
   * @return total elevation of the route
   */
  public static int drawLowestElevPath(Graphics g, int[][] grid, int row){
     
    // initializes all ints
    int distance = 0; 
    int left = 0;
    int right = 0;
    int straight = 0;
    int rowHold = row;

    for (int col = 0; col < grid[0].length - 1; col++)
    {
      // prevents outofbounds when row starts at 0
      if (rowHold == 0)
      {
        left = grid[rowHold][col + 1];
      }
      else
      {
        left = grid[rowHold - 1][col + 1];
      }  
      straight = grid[rowHold][col + 1]; 
      right = grid[rowHold + 1][col + 1];
      
      // left is largest
      if (left > straight && left > right)
      { 
        g.fillRect(col + 1, rowHold - 1, 1,1);
        distance += Math.abs(grid[rowHold - 1][col + 1] - grid[rowHold][col]);
        rowHold--;
      }
      
      // right is largest
      else if (right > straight && right > left)
      { 
        g.fillRect(col + 1, rowHold + 1, 1,1);
        distance += Math.abs(grid[rowHold + 1][col + 1] - grid[rowHold][col]);
        rowHold++;
      }
      
      // straight is largest
      else if (straight > right && straight > left)
      {
        g.fillRect(col + 1, rowHold, 1,1);
        distance += Math.abs(grid[rowHold][col + 1] - grid[rowHold][col]);
      }
      
      // straight has priority
      else if (straight > right && straight == left || straight > left && straight == right)
      {
        g.fillRect(col + 1, rowHold, 1,1);
        distance += Math.abs(grid[rowHold][col + 1] - grid[rowHold][col]);
      }
      
      // left and right are larger, randomly decided
      // ** Extra: Look one more step ahead
      else if (straight < right && straight < left && left == right)
      {
        double decide = Math.random();
        if (decide >= 0.5)
        {
           g.fillRect(col + 1, rowHold + 1, 1,1);
           distance += Math.abs(grid[rowHold + 1][col + 1] - grid[rowHold][col]);
           
        }
        else
        {
          g.fillRect(col + 1, rowHold - 1, 1,1);
          distance += Math.abs(grid[rowHold - 1][col + 1] - grid[rowHold][col]);
          rowHold--;
        }  
      }
    }
    
    return distance;
  }
  
  /**
   * Generate all west-to-east paths, find the one with the lowest total elevation change,
   * and return the index of the row that path starts on.
   * @param g - the graphics context to use
   * @param grid - the 2D array of elevation values
   * @return the index of the row where the lowest elevation-change path starts.
   */
  public static int indexOfLowestElevPath(Graphics g, int[][] grid, ArrayList minRow){
      
    // get number of paths to take
    // get distance of each path
    // color path with smallest distance green
    ArrayList<Integer> distances = new ArrayList<Integer>();
    ArrayList<Integer> startPts = minRow;
    int starts = 0;
    for (int i = 0; i < startPts.size(); i++)
    {
      starts = startPts.get(i);
      distances.add(drawLowestElevPath(g, grid, starts));
    }
    int smallestDistIndex = 0;
    int smallestDist = distances.get(0);
    for (int i = 1; i < startPts.size(); i++)
    {
      if (distances.get(i) < smallestDist)
      {
        smallestDist = distances.get(i);
        smallestDistIndex = i;
      }
    }  
    return smallestDistIndex;
  }
     
}