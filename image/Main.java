import java.util.ArrayList;

/**
 * COMP 3760 Lab 8
 * 
 * Set 3A
 * Choi, Wonyoung (Ricky)
 * A01020199
 * 
 * Main class.
 * Prints out the answers for questions #2 ~ 6.
 * Includes inner class Graph.
 */
public class Main {
  public static void main(String[] args) {
    Main mainClass = new Main();

    System.out.println("Question 2:");
    System.out.println("-----------");
    Graph myGraph1, myGraph2, myGraph3;

      myGraph1 = mainClass.new Graph(5, false);  // Constructs sample graph one
      myGraph1.addEdge(0, 1);
      myGraph1.addEdge(0, 3);
      myGraph1.addEdge(0, 4);
      myGraph1.addEdge(1, 0);
      myGraph1.addEdge(1, 2);
      myGraph1.addEdge(1, 4);
      myGraph1.addEdge(2, 1);
      myGraph1.addEdge(2, 3);
      myGraph1.addEdge(3, 0);
      myGraph1.addEdge(3, 2);
      myGraph1.addEdge(3, 4);
      myGraph1.addEdge(4, 0);
      myGraph1.addEdge(4, 1);
      myGraph1.addEdge(4, 3);
      System.out.println(myGraph1.toString());  // Prints sample graph one
      
      myGraph2 = mainClass.new Graph(4, false);  // Constructs sample graph two
      myGraph2.addEdge(0, 1);
      myGraph2.addEdge(1, 0);
      myGraph2.addEdge(1, 2);
      myGraph2.addEdge(2, 1);
      myGraph2.addEdge(2, 3);
      myGraph2.addEdge(3, 2);
      System.out.println(myGraph2.toString());  // Prints sample graph two
     
      myGraph3 = mainClass.new Graph(6, false);  // Constructs sample graph three
      myGraph3.addEdge(0, 2);
      myGraph3.addEdge(0, 4);
      myGraph3.addEdge(1, 3);
      myGraph3.addEdge(1, 5);
      myGraph3.addEdge(2, 0);
      myGraph3.addEdge(2, 4);
      myGraph3.addEdge(3, 1);
      myGraph3.addEdge(3, 5);
      myGraph3.addEdge(4, 0);
      myGraph3.addEdge(4, 2);
      myGraph3.addEdge(5, 1);
      myGraph3.addEdge(5, 3);
      System.out.println(myGraph3.toString());  // Prints sample graph three

      System.out.println("Question 3:");
      System.out.println("-----------");
      System.out.println(myGraph1.degree(0) == 3);  // The result should be 3
      System.out.println(myGraph2.degree(3) == 1);  // The result should be 1
      System.out.println(myGraph3.degree(4) == 2);  // The result should be 2
      System.out.println(myGraph3.inDegree(4) == -1);  // The result should be -1
      System.out.println();

      System.out.println("Question 4:");
      System.out.println("-----------");
      Graph directedGraph = mainClass.new Graph(5, true);  // Constructs directed graph
      directedGraph.addEdge(0, 0);
      directedGraph.addEdge(0, 4);
      directedGraph.addEdge(1, 2);
      directedGraph.addEdge(1, 4);
      directedGraph.addEdge(2, 0);
      directedGraph.addEdge(2, 3);
      directedGraph.addEdge(3, 1);
      directedGraph.addEdge(3, 2);
      directedGraph.addEdge(4, 3);
      System.out.println(directedGraph.toString());
      System.out.println(directedGraph.outDegree(3) == 2);  // The result should be 2
      System.out.println(directedGraph.inDegree(1) == 1);  // The result should be 2
      System.out.println(directedGraph.degree(1) == -1);  // The result should be -1
      System.out.println();

      System.out.println("Question 5:");
      System.out.println("-----------");
      Graph anotherGraph = mainClass.new Graph(8, false);
      anotherGraph.addEdge(0, 1);
      anotherGraph.addEdge(0, 2);
      anotherGraph.addEdge(0, 4);
      anotherGraph.addEdge(1, 0);
      anotherGraph.addEdge(1, 3);
      anotherGraph.addEdge(1, 5);
      anotherGraph.addEdge(2, 0);
      anotherGraph.addEdge(2, 3);
      anotherGraph.addEdge(2, 6);
      anotherGraph.addEdge(3, 1);
      anotherGraph.addEdge(3, 2);
      anotherGraph.addEdge(3, 7);
      anotherGraph.addEdge(4, 0);
      anotherGraph.addEdge(4, 5);
      anotherGraph.addEdge(4, 6);
      anotherGraph.addEdge(5, 1);
      anotherGraph.addEdge(5, 4);
      anotherGraph.addEdge(5, 7);
      anotherGraph.addEdge(6, 2);
      anotherGraph.addEdge(6, 4);
      anotherGraph.addEdge(6, 7);
      anotherGraph.addEdge(7, 3);
      anotherGraph.addEdge(7, 5);
      anotherGraph.addEdge(7, 6);
      System.out.println(anotherGraph.toString());
      System.out.println("Performing DFS...");
      anotherGraph.performDFS();

      System.out.println("\nQuestion 6:");
      System.out.println("-----------");
      System.out.println("Performing BFS...");
      anotherGraph.performBFS();
    }

  /**
   * Graph class. It can represent either directed or undirected graph.
   */
  public class Graph {
    private int[][] vertices;
    private boolean isDirected = false;
  
    public Graph(int v, boolean isDirected) {
      this.vertices = new int[v][v];
      this.isDirected = isDirected;
    }

    /**
     * Gets the 2D adjacency matrix.
     * @return 2D integer array
     */
    public int[][] getVertices() {
      return this.vertices;
    }
  
    /**
     * Adds an edge to the graph from vertex u to vertex v.
     * @param u an integer in the range 0..v-1
     * @param v an integer in the range 0..v-1
     */
    public void addEdge(int u, int v) {
      vertices[u][v] = 1;
    }
  
    /**
     * Returns a string which prints out the graph in adjacency matrix.
     */
    @Override
    public String toString() {
      String result = "";
      for (int i = 0; i < vertices.length; i++) {
        for (int j = 0; j < vertices[i].length; j++) {
          if (j != vertices[i].length - 1) {
            result += (vertices[i][j] + " ");
          } else {
            result += (vertices[i][j] + "\n");
          }
        }
      }
      return result;
    }
  
    /**
     * Returns an integer degree of the vertex in an undirected graph.
     * Returns -1 if this method is performed from a directed graph.
     * @param v an integer vertex
     * @return an integer degree of the chosen vertex
     */
    public int degree(int v) {
      if (this.isDirected) { return -1; }
      int result = 0;
      for (int i = 0; i < vertices.length; i++) {
        result += vertices[v][i];
      }
      return result;
    }
  

    /**
     * Returns an integer in-degree of the vertex in a directed graph.
     * Returns -1 if this method is performed from a undirected graph.
     * @param v an integer vertex
     * @return an integer in-degree of the chosen vertex
     */
    public int inDegree(int v) {
      if (!this.isDirected) { return -1; }
      int result = 0;
      for (int i = 0; i < vertices.length; i++) {
        result += vertices[i][v];
      }
      return result;
    }
  
    /**
     * Returns an integer out-degree of the vertex in a directed graph.
     * Returns -1 if this method is performed from a undirected graph.
     * @param v an integer vertex
     * @return an integer out-degree of the chosen vertex
     */
    public int outDegree(int v) {
      if (!this.isDirected) { return -1; }
      int result = 0;
      for (int i = 0; i < vertices.length; i++) {
        result += vertices[v][i];
      }
      return result;
    }
  
   /**
    * A driver function for performing depth-first-search on a graph.
    */
    public void performDFS() {
      int[] visited = new int[vertices.length];
      for (int i = 0; i < vertices.length; i++) {
        if (visited[i] == 0) {  // if vertex i is not visited
          dfs(i, visited); 
        }
      }
    }
    
    /**
     * A helper function for performing depth-first-search on a graph.
     * @param v an integer vertex
     * @param visited an integer array that holds information of each vertex whether it is visted or not
     */
    public void dfs(int v, int[] visited) {
      if (visited[v] == 0) {
        System.out.println("visiting vertex " + v);
        visited[v] = 1;  // mark vertex v as visited
        for (int w = 0; w < vertices[v].length; w++) {
          if (vertices[v][w] == 1 && visited[w] == 0) {  // for each vertex w adjacent to v, if w is not visited
            dfs(w, visited);
          }
        }
      }
    }
    
    /**
     * Performs breadth-first-search on a graph.
     */
    public void performBFS() {
      int[] visited = new int[vertices.length];
      ArrayList<Integer> queue = new ArrayList<Integer>();
      queue.add(0);  // start BFS from vertex 0
      while(queue.size() != 0) {
        int v = queue.get(0);
        if (visited[v] == 0) {
          System.out.println("visiting vertex " + v);
          visited[v] = 1;
          
          for (int w = 0; w < vertices[v].length; w++) {
            // for each vertex w adjacent to v, iv w is not visited and hasn't been added to the queue before
            if (vertices[v][w] == 1 && visited[w] == 0 && !queue.contains(w)) {
              queue.add(w);
            }
          }

          queue.remove(0);
        }
      }
    }
  }
}