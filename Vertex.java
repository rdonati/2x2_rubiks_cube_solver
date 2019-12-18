public class Vertex{
    //Stores the cube's state (see README.md for clarification)
    StateArray state;
    //Number of moves required to solve (distance from the solved cube)
    byte dist = 0;
    //The Vertex from which this one was generated
    Vertex prev;

    /**
     * Constructor for vertex
     * @param s State of the cube as a StateArray
     */
    public Vertex(StateArray s){
        state = s;
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
     * Given a solution for some state, converts it into a valid solution for the same state after a x' rotation
     * @param s The solution in standard form
     * @return The new solution in standard form
     */
    public String xTransform(String s){
        char[] A = s.toCharArray();
        for(int i = 0; i < A.length; i++){
            char c = A[i];
            if(c == 'U') A[i] = 'F';
            else if(c == 'F') A[i] = 'D';
            else if(c == 'D') A[i] = 'B';
            else if(c == 'B') A[i] = 'U';
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
            if(c == 'F') A[i] = 'R';
            else if(c == 'R') A[i] = 'B';
            else if(c == 'L') A[i] = 'F';
            else if(c == 'B') A[i] = 'L';
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
            if(c == 'U') A[i] = 'L';
            else if(c == 'R') A[i] = 'U';
            else if(c == 'L') A[i] = 'D';
            else if(c == 'D') A[i] ='R';
        }
        return new String(A);
    }

    /***********************************************************************************************************************
     * THE FOLLOWING METHODS ARE NOT USED ANYMORE, BUT ARE BEING KEPT IN CASE THEY BECOME USEFUL IN THE FUTURE
     ***********************************************************************************************************************/

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
        for(int i = 0; i < rotations[1]; i++) solution = yTransform(solution);
        //Reversing z rotations
        for(int i = 0; i < rotations[2]; i++) solution = zTransform(solution);
        //Reversing x rotations
        for(int i = 0; i < rotations[0]; i++) solution = xTransform(solution);

        return solution;
    }
}