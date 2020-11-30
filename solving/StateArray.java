public class StateArray{
    //Array of length 24 with bytes (0-5), representing the color of each position on a 2x2 Rubiks Cube
    byte[] data = new byte[24];

    /**
     * Constructor for StateArray
     * @param d A string of size 24, representing the state of the cube
     */
    public StateArray(String s){
        char[] d = s.toCharArray();
        for(int i = 0; i < 24; i++){
            if(d[i] == 'w'){
                data[i] = 0;
            }else if(d[i] == 'g'){
                data[i] = 1;
            }else if(d[i] == 'r'){
                data[i] = 2;
            }else if(d[i] == 'o'){
                data[i] = 3;
            }else if(d[i] == 'y'){
                data[i] = 4;
            }else if(d[i] == 'b'){
                data[i] = 5;
            }
        }
    }

    /**
     * Constructor for StateArray
     * @param d A byte array of size 24, representing the state of the cube
     */
    public StateArray(byte[] d){
        data = d;
    }

    /**
     * Makes a copy of the instance
     */
    public StateArray clone(){
        StateArray clone = new StateArray(data.clone());
        return clone;
    }

    /**
     * Checks whether the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom
     * @return True if the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom
     */
    public boolean isCorrectOrientation(){
        if(data[20] == 5 && data[14] == 3 && data[18] == 4) return true;
        return false;
    }

    /**
     * Checks whether the back, left, down corner matches the back left down corner of s
     * @return True if the back, left, down corner matches the back left down corner of s
     */
    public boolean isCorrectOrientation(StateArray s){
        if(data[20] == s.data[20] && data[14] == s.data[14] && data[18] == s.data[18]) return true;
        return false;
    }

    /**
     * Compares two instances by the values in the data array
     * @param obj Object to compare to
     * @return True if the values of the data instance variable are the same; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj instanceof StateArray){
            StateArray test = (StateArray) obj;
            //Compares each index in each StateArray's data field
            for(int i = 0; i < 24; i++){
                if(data[i] != test.data[i]) return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Produces a hashcode based on the values in the data instance variable. Modeled after the String class's hashcode method
     * @return A hashcode based on the values in the data instance variable
     */
    @Override
    public int hashCode() {
        int h = 0;
        //Uses the String class's hashcode, but replaces the character value with the value of each index in the data field
        for(int i = 0; i < data.length; i++){
            h = 31 * h + data[i];
        }
        return h;
    }

    /**
     * Generates an array that stores the number of x, y, and z rotations to orient the cube so that the (yellow, blue, orange) corner is in the back, left, down position with yellow on bottom.
     * Only ever uses two of the three moves (x,y,z) and will either be in the order x,y or z,y
     * @param s State to be reoriented
     * @return The rotations necessary to move from the original state to the desired state. In the form [x-rotations, y-rotations, z-rotations]
     */
    public int[] orient(){
        StateArray s = clone();
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
            s.x();
        }

        //Rotates once in the z direction, then checks all y
        s.z();
        yRotations = checkYRotations(s);
        if(yRotations > -1){
            rotations[0] = 0;
            rotations[1] = yRotations;
            rotations[2] = 1;
            return rotations;
        }

        //Rotates twice (for a total of 3 times) in the z direction, then checks all y
        s.z();
        s.z();
        yRotations = checkYRotations(s);
        if(yRotations > -1){
            rotations[0] = 0;
            rotations[1] = yRotations;
            rotations[2] = 3;
            return rotations;
        }
        return null;
    }

    /**
     * Generates an array that stores the number of x, y, and z rotations to orient the cube so that the back, left, down corner matches s2's back left down corner
     * Only ever uses two of the three moves (x,y,z) and will either be in the order x,y or z,y
     * @param s2 State to be reoriented
     * @return The rotations necessary to move from the original state to the desired state. In the form [x-rotations, y-rotations, z-rotations]
     */
    public int[] orient(StateArray s2){
        StateArray s = clone();
        int[] rotations = new int[]{0, 0, 0};
        int yRotations;

        //Rotates the cube, first in the x direction, then checks every possible y rotation
        for(int x = 0; x < 4; x++){
            rotations[0] = x;
            yRotations = checkYRotations(s, s2);
            if(yRotations > -1){
                rotations[1] = yRotations;
                return rotations;
            }
            s.x();
        }

        //Rotates once in the z direction, then checks all y
        s.z();
        yRotations = checkYRotations(s, s2);
        if(yRotations > -1){
            rotations[0] = 0;
            rotations[1] = yRotations;
            rotations[2] = 1;
            return rotations;
        }

        //Rotates twice (for a total of 3 times) in the z direction, then checks all y
        s.z();
        s.z();
        yRotations = checkYRotations(s, s2);
        if(yRotations > -1){
            rotations[0] = 0;
            rotations[1] = yRotations;
            rotations[2] = 3;
            return rotations;
        }
        return rotations;
    }

    /**
     * Helper method for orient()... checks to see if any y rotations will match the bottom left down piece of s to the bottom left down piece of s2
     * @param s The StateArray to check
     * @return The number of y rotations needed to produce the desired orientation
     */
    public int checkYRotations(StateArray s, StateArray s2){
        for(int y = 0; y < 4; y++){
            if(s.isCorrectOrientation(s2)) return y;
            s.y();
        }
        return -1;
    }

    /**
     * Helper method for orient()... checks to see if any y rotations will put the (orange, yellow, blue) corner in the back left down position with yellow on the bottom
     * @param s The StateArray to check
     * @return The number of y rotations needed to produce the desired orientation
     */
    public int checkYRotations(StateArray s){
        for(int y = 0; y < 4; y++){
            if(s.isCorrectOrientation()) return y;
            s.y();
        }
        return -1;
    }

    /**
     * Rotates the cube with the rotations passed
     * @param rotations An array of ints where the rotations are in the format [x, y, z]
     */
    public void rotate(int[] rotations){
        System.out.println("hello");
        for(int x = 0; x < rotations[0]; x++) x();
        for(int z = 0; z < rotations[2]; z++) z();
        for(int y = 0; y < rotations[1]; y++) y();
    }

    /**
     * Rotates the cube with the inverse of the rotations passed
     * @param rotations An array of ints where the rotations are in the format [x, y, z]
     */
    public void rotateInverse(int[] rotations){
        for(int x = 0; x < rotations[0]; x++){
            x();
            x();
            x();
        }
        for(int z = 0; z < rotations[2]; z++){
            z();
            z();
            z();
        } 
        for(int y = 0; y < rotations[0]; y++){
            y();
            y();
            y();
        }
    }

    /******************************************************************
    *                      TURNING METHODS
    ******************************************************************/

    public StateArray copyTurn(String s){
        StateArray s2 = clone();
        switch(s){
            //Clockwise turns
            case "u":
                s2.u();
                break;
            case "f":
                s2.f();
                break;
            case "r":
                s2.r();
                break;
            case "l":
                s2.l();
                break;
            case "d":
                s2.d();
                break;
            case "b":
                s2.b();
                break;

            //Inverse turns    
            case "uP":
                s2.uP();
                break;
            case "fP":
                s2.fP();
                break;
            case "rP":
                s2.rP();
                break;
            case "lP":
                s2.lP();
                break;
            case "dP":
                s2.dP();
                break;
            case "bP":
                s2.bP();
                break;

            //Double turns
            case "u2":
                s2.u();
                s2.u();
                break;
            case "f2":
                s2.f();
                s2.f();
                break;
            case "r2":
                s2.r();
                s2.r();
                break;
            case "l2":
                s2.l();
                s2.l();
                break;
            case "d2":
                s2.d();
                s2.d();
                break;
            case "b2":
                s2.b();
                s2.b();
                break;

            //Rotations
            case "x":
                s2.x();
                break;
            case "y":
                s2.y();
                break;
            case "z":
                s2.z();
                break;
            default:
                break;
        }
        return s2;
    }

    public void u(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[0];
        data[0] = data[2];
        data[2] = data[3];
        data[3] = data[1];
        data[1] = buffer;
        //SURROUNDING STICKERS
        buffer = data[5];
        data[5] = data[9];
        data[9] = data[22];
        data[22] = data[13];
        data[13] = buffer;
        buffer = data[4];
        data[4] = data[8];
        data[8] = data[23];
        data[23] = data[12];
        data[12] = buffer;

        
    }

    public void f(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[4];
        data[4] = data[6];
        data[6] = data[7];
        data[7] = data[5];
        data[5] = buffer;
        //SURROUNDING STICKERS
        buffer = data[2];
        data[2] = data[15];
        data[15] = data[17];
        data[17] = data[8];
        data[8] = buffer;
        buffer = data[3];
        data[3] = data[13];
        data[13] = data[16];
        data[16] = data[10];
        data[10] = buffer;

        
    }

    public void r(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[8];
        data[8] = data[10];
        data[10] = data[11];
        data[11] = data[9];
        data[9] = buffer;
        //SURROUNDING STICKERS
        buffer = data[3];
        data[3] = data[7];
        data[7] = data[19];
        data[19] = data[23];
        data[23] = buffer;
        buffer = data[1];
        data[1] = data[5];
        data[5] = data[17];
        data[17] = data[21];
        data[21] = buffer;

        
    }

    public void l(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[12];
        data[12] = data[14];
        data[14] = data[15];
        data[15] = data[13];
        data[13] = buffer;
        //SURROUNDING STICKERS
        buffer = data[0];
        data[0] = data[20];
        data[20] = data[16];
        data[16] = data[4];
        data[4] = buffer;
        buffer = data[2];
        data[2] = data[22];
        data[22] = data[18];
        data[18] = data[6];
        data[6] = buffer;

        
    }

    public void d(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[16];
        data[16] = data[18];
        data[18] = data[19];
        data[19] = data[17];
        data[17] = buffer;
        //SURROUNDING STICKERS
        buffer = data[6];
        data[6] = data[14];
        data[14] = data[21];
        data[21] = data[10];
        data[10] = buffer;
        buffer = data[7];
        data[7] = data[15];
        data[15] = data[20];
        data[20] = data[11];
        data[11] = buffer;

        
    }

    public void b(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[20];
        data[20] = data[22];
        data[22] = data[23];
        data[23] = data[21];
        data[21] = buffer;
        //SURROUNDING STICKERS
        buffer = data[18];
        data[18] = data[12];
        data[12] = data[1];
        data[1] = data[11];
        data[11] = buffer;
        buffer = data[19];
        data[19] = data[14];
        data[14] = data[0];
        data[0] = data[9];
        data[9] = buffer;

        
    }

    /******************************************************************
     *                      INVERSE TURNING METHODS
     ******************************************************************/

    public void uP(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[1];
        data[1] = data[3];
        data[3] = data[2];
        data[2] = data[0];
        data[0] = buffer;
        //SURROUNDING STICKERS
        buffer = data[13];
        data[13] = data[22];
        data[22] = data[9];
        data[9] = data[5];
        data[5] = buffer;
        buffer = data[12];
        data[12] = data[23];
        data[23] = data[8];
        data[8] = data[4];
        data[4] = buffer;

        
    }

    public void fP(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[5];
        data[5] = data[7];
        data[7] = data[6];
        data[6] = data[4];
        data[4] = buffer;
        //SURROUNDING STICKERS
        buffer = data[8];
        data[8] = data[17];
        data[17] = data[15];
        data[15] = data[2];
        data[2] = buffer;
        buffer = data[10];
        data[10] = data[16];
        data[16] = data[13];
        data[13] = data[3];
        data[3] = buffer;

        
    }

    public void rP(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[9];
        data[9] = data[11];
        data[11] = data[10];
        data[10] = data[8];
        data[8] = buffer;
        //SURROUNDING STICKERS
        buffer = data[23];
        data[23] = data[19];
        data[19] = data[7];
        data[7] = data[3];
        data[3] = buffer;
        buffer = data[21];
        data[21] = data[17];
        data[17] = data[5];
        data[5] = data[1];
        data[1] = buffer;

        
    }

    public void lP(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[13];
        data[13] = data[15];
        data[15] = data[14];
        data[14] = data[12];
        data[12] = buffer;
        //SURROUNDING STICKERS
        buffer = data[4];
        data[4] = data[16];
        data[16] = data[20];
        data[20] = data[0];
        data[0] = buffer;
        buffer = data[6];
        data[6] = data[18];
        data[18] = data[22];
        data[22] = data[2];
        data[2] = buffer;

        
    }

    public void dP(){
       byte buffer;
        
       //FACE ROTATION
       buffer = data[17];
       data[17] = data[19];
       data[19] = data[18];
       data[18] = data[16];
       data[16] = buffer;
       //SURROUNDING STICKERS
       buffer = data[10];
       data[10] = data[21];
       data[21] = data[14];
       data[14] = data[6];
       data[6] = buffer;
       buffer = data[11];
       data[11] = data[20];
       data[20] = data[15];
       data[15] = data[7];
       data[7] = buffer;

       
    }

    public void bP(){
        byte buffer;
        
        //FACE ROTATION
        buffer = data[21];
        data[21] = data[23];
        data[23] = data[22];
        data[22] = data[20];
        data[20] = buffer;
        //SURROUNDING STICKERS
        buffer = data[11];
        data[11] = data[1];
        data[1] = data[12];
        data[12] = data[18];
        data[18] = buffer;
        buffer = data[9];
        data[9] = data[0];
        data[0] = data[14];
        data[14] = data[19];
        data[19] = buffer;

       
    }

    /******************************************************************
     *                      ROTATION METHODS
     ******************************************************************/

    public void x(){
        byte buffer;

        //RIGHT ROTATION
        buffer = data[8];
        data[8] = data[10];
        data[10] = data[11];
        data[11] = data[9];
        data[9] = buffer;

        //LEFT PRIME ROTATION
        buffer = data[13];
        data[13] = data[15];
        data[15] = data[14];
        data[14] = data[12];
        data[12] = buffer;

        //MAIN AXIS
        for(int i = 0; i < 4; i++){
            buffer = data[i];
            data[i] = data[4+i];
            data[4+i] = data[16+i];
            data[16+i] = data[20+i];
            data[20+i] = buffer;
        }
        
    }

    public void y(){
        byte buffer;

        //UP ROTATION
        buffer = data[0];
        data[0] = data[2];
        data[2] = data[3];
        data[3] = data[1];
        data[1] = buffer;

        //DOWN PRIME ROTATION
        buffer = data[17];
        data[17] = data[19];
        data[19] = data[18];
        data[18] = data[16];
        data[16] = buffer;

        //MAIN AXIS
        for(int i = 0; i < 4; i++){
            buffer = data[4+i];
            data[4+i] = data[8+i];
            data[8+i] = data[23-i];
            data[23-i] = data[12+i];
            data[12+i] = buffer;
        }
        
    }

    public void z(){
        byte buffer;
        
        //FRONT ROTATION
        buffer = data[4];
        data[4] = data[6];
        data[6] = data[7];
        data[7] = data[5];
        data[5] = buffer;

        //BACK PRIME ROTATION
        buffer = data[21];
        data[21] = data[23];
        data[23] = data[22];
        data[22] = data[20];
        data[20] = buffer;

        //MAIN AXIS
        byte[] right = new byte[]{9, 11, 8, 10};
        byte[] left = new byte[]{14, 12, 15, 13};

        for(int i = 0; i < 4; i++){
            buffer = data[i];
            data[i] = data[left[i]];
            data[left[i]] = data[19-i];
            data[19-i] = data[right[i]];
            data[right[i]] = buffer;
        }
        
    }
}