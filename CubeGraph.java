import java.util.*;

public class CubeGraph{

    HashMap<StateArray, Vertex> vertexMap = new HashMap<StateArray, Vertex>();
    StateArray SOLVED_CUBE = new StateArray(new byte[]{0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6});
    public static void main(String[] args) {
        CubeGraph c = new CubeGraph();
        String scramble = "obobbrbryyyywwwwrgrggogo";
        if(args.length > 0) scramble = args[0];
        c.solve(scramble);
    }

    public class Vertex{
        StateArray state;
        byte dist = 0;
        Vertex prev;

        /**
         * Constructor for vertex
         * @param s State of the cube as a StateArray
         */
        public Vertex(StateArray s){
            state = s;
        }

        /**
         * Constructor for vertex
         * @param s State of the cube as a byte array
         */
        public Vertex(byte[] s){
            state = new StateArray(s);
        }

        /**
         * Given a solution for some state, converts it into a valid solution for the same state after a x' rotation
         * @param s The solution in standard form
         * @return The new solution in standard form
         */
        public String xTransform(String s){
            char[] A = s.toCharArray();
            for(int i = 0; i < A.length; i++){
                char c = A[i];
                if(c == 'U'){
                    A[i] = 'F';
                }else if(c == 'F'){
                    A[i] = 'D';
                }else if(c == 'D'){
                    A[i] = 'B';
                }else if(c == 'B'){
                    A[i] = 'U';
                }
            }

            return new String(A);
        }

        /**
         * Given a solution for some state, converts it into a valid solution for the same state after a y' rotation
         * @param s The solution in standard form
         * @return The new solution in standard form
         */
        public String yTransform(String s){
            char[] A = s.toCharArray();
            for(int i = 0; i < A.length; i++){
                char c = A[i];
                if(c == 'F'){
                    A[i] = 'R';
                }else if(c == 'R'){
                    A[i] = 'B';
                }else if(c == 'L'){
                    A[i] = 'F';
                }else if(c == 'B'){
                    A[i] = 'L';
                }
            }
            return new String(A);
        }

        /**
         * Given a solution for some state, converts it into a valid solution for the same state after a z' rotation
         * @param s The solution in standard form
         * @return The new solution in standard form
         */
        public String zTransform(String s){
            char[] A = s.toCharArray();
            for(int i = 0; i < A.length; i++){
                char c = A[i];
                if(c == 'U'){
                    A[i] = 'L';
                }else if(c == 'R'){
                    A[i] = 'U';
                }else if(c == 'L'){
                    A[i] = 'D';
                }else if(c == 'D'){
                    A[i] ='R';
                }
            }
            return new String(A);
        }

        /**
         * Finds the solution, in standard format, for the cube given that is in the correct orientation
         * @return The solution, in standard format, for the cube given that is in the correct orientation
         */
        public String findSolution(){
            String solution = "";
            Vertex v = this;
            while(v.prev != null){
                StateArray s = v.prev.state;
                if(s.equals(v.state.copyTurn("u2"))) solution += "U2 ";
                if(s.equals(v.state.copyTurn("r2"))) solution += "R2 ";
                if(s.equals(v.state.copyTurn("f2"))) solution += "F2 ";

                if(s.equals(v.state.copyTurn("u"))) solution += "U ";
                if(s.equals(v.state.copyTurn("r"))) solution += "R ";
                if(s.equals(v.state.copyTurn("f"))) solution += "F ";

                if(s.equals(v.state.copyTurn("uP"))) solution += "U' ";
                if(s.equals(v.state.copyTurn("rP"))) solution += "R' ";
                if(s.equals(v.state.copyTurn("fP"))) solution += "F' ";

                v = v.prev;
            }
            return solution;
        }

        /**
         * Finds the solution, in standard format, for the cube including the moves needed to put it in the correct oritentation
         * @param rotations The rotations necessary to orient the cube such that the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom (see orient method)
         * @return The solution, in standard format, for the cube including the moves needed to put it in the correct oritentation
         */
        public String find3GenSolution(int[] rotations){
            String solution = "";
            
            if(rotations[0] == 1) solution += "x ";
            else if(rotations[0] == 2) solution += "x2 ";
            else if(rotations[0] == 3) solution += "x' ";

            if(rotations[2] == 1) solution += "z ";
            else if(rotations[2] == 2) solution += "z2 ";
            else if(rotations[2] == 3) solution += "z' ";

            if(rotations[1] == 1) solution += "y ";
            else if(rotations[1] == 2) solution += "y2 ";
            else if(rotations[1] == 3) solution += "y' ";
            
            solution += findSolution();
            return solution;
        }

        /**
         * Uses each vertex's prev to backtrack to the solution, producing the solution.
         * @param rotations The rotations necessary to orient the cube such that the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom (see orient method)
         * @return The solution to the vertex's state in standard notation
         */
        public String findSolutionNoRotations(int[] rotations){
            String solution = findSolution();
            //Reversing y rotations
            for(int i = 0; i < rotations[1]; i++){
                solution = yTransform(solution);
            }
            
            //Reversing z rotations
            for(int i = 0; i < rotations[2]; i++){
                solution = zTransform(solution);
            }
            
            //Reversing x rotations
            for(int i = 0; i < rotations[0]; i++){
                solution = xTransform(solution);
            }

            return solution;
        }

    }

    /**
     * Creates a vertex and adds it to the vertexMap
     * @param state The state that the vertex represents
     * @return The vertex with the given state
     */
    public Vertex getVertex(byte[] s){
        StateArray state = new StateArray(s);
        Vertex v = vertexMap.get(state);
        if(v == null){
            v = new Vertex(state);
            vertexMap.put(state, v);
        }
        return v;
    }

    /**
     * Creates a vertex and adds it to the vertexMap
     * @param state The state that the vertex represents
     * @return The vertex with the given state
     */
    public Vertex getVertex(StateArray state){
        Vertex v = vertexMap.get(state);
        if(v == null){
            v = new Vertex(state);
            vertexMap.put(state, v);
        }
        return v;
    }

    /**
     * Creates a graph by 
     * 1. Starting with a solved cube 
     * 2. Adding all of the states reachable from the solved cube as its neighbors
     * 3. Repeating the process on all of its neighbors until the desired state is found
     * @param scramble The state of the scrambled cube
     * @return The vertex that corresponds to the given state
     */
    public Vertex run(StateArray scramble){
        int distCounter = 0;
        int[] numCounter = new int[20];
        Vertex s = getVertex(SOLVED_CUBE);
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
                Vertex u = getVertex(state);
                u.dist = (byte)(v.dist + 1);
                u.prev = v;
                q.add(u);
            }
        }
        return null;
    }

    /**
     * Applies all possible moves to the given state and returns an ArrayList of the resulting states. Only applies r, b, and d because f, b, and l can be achieved by applying the original moves and s rotation.
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
}

 