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
}