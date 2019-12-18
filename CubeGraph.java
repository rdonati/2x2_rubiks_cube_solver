import java.util.*;

public class CubeGraph{

    HashMap<StateArray, Vertex> vertexMap = new HashMap<StateArray, Vertex>();
    StateArray SOLVED_CUBE = new StateArray(new byte[]{0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6});

    /**
     * Creates a CubeGraph and finds a solution for the given scramble
     * @param args A string representing the state of the scrambled cube (see README.md for details)
     */
    public static void main(String[] args) {
        CubeGraph c = new CubeGraph();
        
        //Below is where you can enter the scramble in code... this will be overridden if an alternative scramble is passed in the command line
        String scramble = "obobbrbryyyywwwwrgrggogo";
        if(args.length > 0) scramble = args[0];

        c.solve(scramble);
    }

    /**
     * Prints the solution for a given state, if it is valid.
     * @param s StateArray of the state to be solved
     */
    public void solve(String scramble){
        StateArray s = new StateArray(scramble);
        //Checking to make sure that the user input is valid
        if(!isPossible(s)){
            System.out.println("Please enter a valid state...");
            return;
        }

        //Finding the rotations that match the solved cube's back left down corner with the scrables cube's
        int[] rotations = SOLVED_CUBE.orient(s);
        SOLVED_CUBE.rotate(rotations);

        Vertex finalState = run(s);
        System.out.println("\nSolution: " + finalState.findSolution());
    }

    /**
     * Creates a graph by:
     * 1. Starting with a solved cube 
     * 2. Adding all of the states reachable from the solved cube as its neighbors
     * 3. Repeating the process on all of its neighbors until the desired state is found
     * @param scramble The state of the scrambled cube
     * @return The vertex that corresponds to the given state
     */
    public Vertex run(StateArray scramble){
        int distCounter = 0;
        int[] numCounter = new int[20];
        Vertex s = addVertex(SOLVED_CUBE);
        Queue<Vertex> q = new ArrayDeque<Vertex>();
        System.out.println("# moves deep: states at that depth");

        q.add(s);

        while(!q.isEmpty()){
            Vertex v = q.poll();

            //Checks to see if the desired state has been found
            if(v.state.equals(scramble)) return v;
            
            //Prints the number of states that are 1, 2, 3... moves away from being solved
            numCounter[v.dist]++;
            if(v.dist > distCounter){
                System.out.println(distCounter + ": " + String.format("%,d", numCounter[distCounter]));
                distCounter++;
            }

            //Runs a modified version of BFS to create a graph
            ArrayList<StateArray> nbs = getNbs(v);
            for(StateArray state : nbs){
                if(vertexMap.containsKey(state)) continue;
                Vertex u = addVertex(state);
                u.dist = (byte)(v.dist + 1);
                u.prev = v;
                q.add(u);
            }
        }
        return null;
    }

    /**
     * Applies all possible moves to the given state and returns an ArrayList of the resulting states. Only applies R U and F because L D and B can be achieved by applying the original moves and a rotation.
     * @param v The original state
     * @return An ArrayList of states reached by applying every possible move to the original state
     */
    public ArrayList<StateArray> getNbs(Vertex v){
        StateArray state = v.state;
        ArrayList<StateArray> nbs = new ArrayList<StateArray>();

        //Double rotations
        nbs.add(state.copyTurn("r2"));
        nbs.add(state.copyTurn("u2"));
        nbs.add(state.copyTurn("f2"));

        //Single rotations
        nbs.add(state.copyTurn("r"));
        nbs.add(state.copyTurn("u"));
        nbs.add(state.copyTurn("f"));

        //Inverse rotations
        nbs.add(state.copyTurn("rP"));
        nbs.add(state.copyTurn("uP"));
        nbs.add(state.copyTurn("fP"));

        return nbs;
    }

    /**
     * Checks whether or not the StateArray has four of each color.
     * @param s State to be checked
     * @return True if the state has four of each color; false if not
     */
    public boolean isPossible(StateArray s){
        //Each index stores the number of occurrences of each color
        byte[] occurrences = new byte[6];
        for(int i = 0; i < 24; i++){
            occurrences[s.data[i]]++;
        }
        for(int i = 0; i < 6; i++){
            if(occurrences[i] != 4) return false;
        }
        return true;
    }

    /**
     * Creates a vertex and adds it to the vertexMap
     * @param state The state that the vertex represents
     * @return The vertex with the given state
     */
    public Vertex addVertex(StateArray state){
        Vertex v = vertexMap.get(state);
        if(v == null){
            v = new Vertex(state);
            vertexMap.put(state, v);
        }
        return v;
    }
}