public class Hasher {
    public final int SEED;
    Hasher(int seed){
        SEED = seed;
    }
    public String hash(String input){
        return hashHelper(input);
    }
    private String hashHelper(String input){
        char[] base = input.toCharArray();
        String output = "";
        for (char i : base){
            double temp = i;
            temp = (temp*SEED)%128;
            output+=(char)((int)temp);
        }
        return output;
    }
}
