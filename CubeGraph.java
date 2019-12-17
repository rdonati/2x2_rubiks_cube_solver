import java.util.*;

public class CubeGraph{

    HashMap<StateArray, Vertex> vertexMap = new HashMap<StateArray, Vertex>();
    StateArray SOLVED_CUBE = new StateArray(new byte[]{0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6});
    public static void main(String[] args) {
        CubeGraph c = new CubeGraph();
        String scramble = "bbbbrrrryyyywwwwggggoooo";
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
                if(s.equals(u(u(v.state)))) solution += "U2 ";
                if(s.equals(f(f(v.state)))) solution += "F2 ";
                if(s.equals(r(r(v.state)))) solution += "R2 ";

                if(s.equals(u(v.state))) solution += "U ";
                if(s.equals(f(v.state))) solution += "F ";
                if(s.equals(r(v.state))) solution += "R ";
                
                if(s.equals(uP(v.state))) solution += "U' ";
                if(s.equals(fP(v.state))) solution += "F' ";
                if(s.equals(rP(v.state))) solution += "R' ";

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
        nbs.add(r(r(state)));
        nbs.add(u(u(state)));
        nbs.add(f(f(state)));

        //Single rotations
        nbs.add(r(state));
        nbs.add(u(state));
        nbs.add(f(state));

        //Inverse rotations
        nbs.add(rP(state));
        nbs.add(uP(state));
        nbs.add(fP(state));

        return nbs;
    }

    /**
     * Generates an array that stores the number of x, y, and z rotations to orient the cube so that the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom.
     * Only ever uses two of the three moves (x,y,z) and will either be in the order x,y or z,y
     * @param s State to be reoriented
     * @return The rotations necessary to move from the original state to the desired state. In the form [x-rotations, y-rotations, z-rotations]
     */
    public int[] orient(StateArray s){
        int[] rotations = new int[]{0, 0, 0};
        int yRotations;

        //Rotates the cube, first in the x direction, then checks every possible y rotation
        for(int x = 0; x < 4; x++){
            rotations[0] = x;
            yRotations = checkYRotations(s);
            if(yRotations > -1){
                rotations[1] = yRotations;
                return rotations;
            }
            s = x(s);
        }

        //Rotates once in the z direction, then checks all y
        s = z(s);
        yRotations = checkYRotations(s);
        if(yRotations > -1){
            rotations[0] = 0;
            rotations[1] = yRotations;
            rotations[2] = 1;
            return rotations;
        }

        //Rotates twice (for a total of 3 times) in the z direction, then checks all y
        s = z(z(s));
        yRotations = checkYRotations(s);
        if(yRotations > -1){
            rotations[0] = 0;
            rotations[1] = yRotations;
            rotations[2] = 3;
            return rotations;
        }

        return null;
    }

    public int checkYRotations(StateArray s){
        for(int y = 0; y < 4; y++){
            if(s.isCorrectOrientation()) return y;
            s = y(s);
        }
        return -1;
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

        //Finding rotations to put the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom
        int[] rotations = orient(s);

        //Applying rotations
        for(int x = 0; x < rotations[0]; x++) s = x(s);
        for(int z = 0; z < rotations[2]; z++) s = z(s);
        for(int y = 0; y < rotations[1]; y++) s = y(s);

        Vertex finalState = run(s);
        System.out.println(finalState.find3GenSolution(rotations));
    }

    /******************************************************************
    *                      TURNING METHODS
    ******************************************************************/

    public StateArray u(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[0];
        A[0] = A[2];
        A[2] = A[3];
        A[3] = A[1];
        A[1] = buffer;
        //SURROUNDING STICKERS
        buffer = A[5];
        A[5] = A[9];
        A[9] = A[22];
        A[22] = A[13];
        A[13] = buffer;
        buffer = A[4];
        A[4] = A[8];
        A[8] = A[23];
        A[23] = A[12];
        A[12] = buffer;

        return s2;
    }

    public StateArray f(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[4];
        A[4] = A[6];
        A[6] = A[7];
        A[7] = A[5];
        A[5] = buffer;
        //SURROUNDING STICKERS
        buffer = A[2];
        A[2] = A[15];
        A[15] = A[17];
        A[17] = A[8];
        A[8] = buffer;
        buffer = A[3];
        A[3] = A[13];
        A[13] = A[16];
        A[16] = A[10];
        A[10] = buffer;

        return s2;
    }

    public StateArray r(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[8];
        A[8] = A[10];
        A[10] = A[11];
        A[11] = A[9];
        A[9] = buffer;
        //SURROUNDING STICKERS
        buffer = A[3];
        A[3] = A[7];
        A[7] = A[19];
        A[19] = A[23];
        A[23] = buffer;
        buffer = A[1];
        A[1] = A[5];
        A[5] = A[17];
        A[17] = A[21];
        A[21] = buffer;

        return s2;
    }

    public StateArray l(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[12];
        A[12] = A[14];
        A[14] = A[15];
        A[15] = A[13];
        A[13] = buffer;
        //SURROUNDING STICKERS
        buffer = A[0];
        A[0] = A[20];
        A[20] = A[16];
        A[16] = A[4];
        A[4] = buffer;
        buffer = A[2];
        A[2] = A[22];
        A[22] = A[18];
        A[18] = A[6];
        A[6] = buffer;

        return s2;
    }

    public StateArray d(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[16];
        A[16] = A[18];
        A[18] = A[19];
        A[19] = A[17];
        A[17] = buffer;
        //SURROUNDING STICKERS
        buffer = A[6];
        A[6] = A[14];
        A[14] = A[21];
        A[21] = A[10];
        A[10] = buffer;
        buffer = A[7];
        A[7] = A[15];
        A[15] = A[20];
        A[20] = A[11];
        A[11] = buffer;

        return s2;
    }

    public StateArray b(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[20];
        A[20] = A[22];
        A[22] = A[23];
        A[23] = A[21];
        A[21] = buffer;
        //SURROUNDING STICKERS
        buffer = A[18];
        A[18] = A[12];
        A[12] = A[1];
        A[1] = A[11];
        A[11] = buffer;
        buffer = A[19];
        A[19] = A[14];
        A[14] = A[0];
        A[0] = A[9];
        A[9] = buffer;

        return s2;
    }

    /******************************************************************
     *                      INVERSE TURNING METHODS
     ******************************************************************/

    public StateArray uP(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[1];
        A[1] = A[3];
        A[3] = A[2];
        A[2] = A[0];
        A[0] = buffer;
        //SURROUNDING STICKERS
        buffer = A[13];
        A[13] = A[22];
        A[22] = A[9];
        A[9] = A[5];
        A[5] = buffer;
        buffer = A[12];
        A[12] = A[23];
        A[23] = A[8];
        A[8] = A[4];
        A[4] = buffer;

        return s2;
    }

    public StateArray fP(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[5];
        A[5] = A[7];
        A[7] = A[6];
        A[6] = A[4];
        A[4] = buffer;
        //SURROUNDING STICKERS
        buffer = A[8];
        A[8] = A[17];
        A[17] = A[15];
        A[15] = A[2];
        A[2] = buffer;
        buffer = A[10];
        A[10] = A[16];
        A[16] = A[13];
        A[13] = A[3];
        A[3] = buffer;

        return s2;
    }

    public StateArray rP(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[9];
        A[9] = A[11];
        A[11] = A[10];
        A[10] = A[8];
        A[8] = buffer;
        //SURROUNDING STICKERS
        buffer = A[23];
        A[23] = A[19];
        A[19] = A[7];
        A[7] = A[3];
        A[3] = buffer;
        buffer = A[21];
        A[21] = A[17];
        A[17] = A[5];
        A[5] = A[1];
        A[1] = buffer;

        return s2;
    }

    public StateArray lP(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[13];
        A[13] = A[15];
        A[15] = A[14];
        A[14] = A[12];
        A[12] = buffer;
        //SURROUNDING STICKERS
        buffer = A[4];
        A[4] = A[16];
        A[16] = A[20];
        A[20] = A[0];
        A[0] = buffer;
        buffer = A[6];
        A[6] = A[18];
        A[18] = A[22];
        A[22] = A[2];
        A[2] = buffer;

        return s2;
    }

    public StateArray dP(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
       //FACE ROTATION
       buffer = A[17];
       A[17] = A[19];
       A[19] = A[18];
       A[18] = A[16];
       A[16] = buffer;
       //SURROUNDING STICKERS
       buffer = A[10];
       A[10] = A[21];
       A[21] = A[14];
       A[14] = A[6];
       A[6] = buffer;
       buffer = A[11];
       A[11] = A[20];
       A[20] = A[15];
       A[15] = A[7];
       A[7] = buffer;

       return s2;
    }

    public StateArray bP(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FACE ROTATION
        buffer = A[21];
        A[21] = A[23];
        A[23] = A[22];
        A[22] = A[20];
        A[20] = buffer;
        //SURROUNDING STICKERS
        buffer = A[11];
        A[11] = A[1];
        A[1] = A[12];
        A[12] = A[18];
        A[18] = buffer;
        buffer = A[9];
        A[9] = A[0];
        A[0] = A[14];
        A[14] = A[19];
        A[19] = buffer;

       return s2;
    }

    /******************************************************************
     *                      ROTATION METHODS
     ******************************************************************/

    public StateArray x(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;

        //RIGHT ROTATION
        buffer = A[8];
        A[8] = A[10];
        A[10] = A[11];
        A[11] = A[9];
        A[9] = buffer;

        //LEFT PRIME ROTATION
        buffer = A[13];
        A[13] = A[15];
        A[15] = A[14];
        A[14] = A[12];
        A[12] = buffer;

        //MAIN AXIS
        for(int i = 0; i < 4; i++){
            buffer = A[i];
            A[i] = A[4+i];
            A[4+i] = A[16+i];
            A[16+i] = A[20+i];
            A[20+i] = buffer;
        }
        return s2;
    }

    public StateArray y(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;

        //UP ROTATION
        buffer = A[0];
        A[0] = A[2];
        A[2] = A[3];
        A[3] = A[1];
        A[1] = buffer;

        //DOWN PRIME ROTATION
        buffer = A[17];
        A[17] = A[19];
        A[19] = A[18];
        A[18] = A[16];
        A[16] = buffer;

        //MAIN AXIS
        for(int i = 0; i < 4; i++){
            buffer = A[4+i];
            A[4+i] = A[8+i];
            A[8+i] = A[23-i];
            A[23-i] = A[12+i];
            A[12+i] = buffer;
        }
        return s2;
    }

    public StateArray z(StateArray state){
        StateArray s2 = state.clone();
        byte[] A = s2.data;
        byte buffer;
        //FRONT ROTATION
        buffer = A[4];
        A[4] = A[6];
        A[6] = A[7];
        A[7] = A[5];
        A[5] = buffer;

        //BACK PRIME ROTATION
        buffer = A[21];
        A[21] = A[23];
        A[23] = A[22];
        A[22] = A[20];
        A[20] = buffer;

        //MAIN AXIS
        byte[] right = new byte[]{9, 11, 8, 10};
        byte[] left = new byte[]{14, 12, 15, 13};

        for(int i = 0; i < 4; i++){
            buffer = A[i];
            A[i] = A[left[i]];
            A[left[i]] = A[19-i];
            A[19-i] = A[right[i]];
            A[right[i]] = buffer;
        }
        return s2;
    }
}