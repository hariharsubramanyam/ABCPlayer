package player;

import java.util.HashMap;
import java.util.Map;

/**
 * A key signature specifies accidentals that must be applied a set of notes
 * This class provides an enumeration of all the key signatures and a map to the accidentals they provide 
 */

public class KeySignature {
    /**
     * Array indicating the accidentals applied to each note, where
     * accidental[i] = the number of semitones to increase the i'th note by
     * and the "i'th" note can be determined as follows:
     * 0 = A
     * 1 = B
     * 2 = C
     * 3 = D
     * 4 = E
     * 5 = F
     * 6 = G
     */
    private int[] accidentals = new int[7];
    
    /**
     * A map where the keys are the key signatures (ex. D, C#)
     * and the values are the accidental offsets (as explained in the accidentals array comment)
     */
    private Map<String, int[]> keySigMap;
    
    /**
     * The key (ex. D, C#)
     */
    private final String key;
    
    /**
     * Initializes a new KeySignature object.
     * @param key is inputed as a string (ex "C", "D#m", "Gb")
     */
    public KeySignature(String key) {
        this.key = key;
        this.keySigMap = this.buildKeySigMap();
        this.accidentals = this.keySigMap.get(key);
    }
    
    /**
     * @return Returns the key as a string.
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * @return Returns a list representing the accidentals associated with the given key.
     */
    public int[] getAccidentals() {
        return this.accidentals.clone();
    }
    
    /**
     * @return the string representation of the object
     */
    @Override
    public String toString(){
        return this.key;
    }
    
    /**
     * Creates a map of the accidentals corresponding to each key in music.
     * -1 represents a flat, 0 represents a natural, and 1 represents a sharp.
     * The array corresponding to each key is ordered {A,B,C,D,E,F,G}
     * @return Returns a map of each key to an array of its respective accidentals.
     */
    private Map<String, int[]> buildKeySigMap() {
        Map<String, int[]> keySigMap = new HashMap<String, int[]>();
        keySigMap.put("C", new int[] {0,0,0,0,0,0,0});
        keySigMap.put("Am", new int[] {0,0,0,0,0,0,0});
        keySigMap.put("G", new int[] {0,0,0,0,0,1,0});
        keySigMap.put("Em", new int[] {0,0,0,0,0,1,0});
        keySigMap.put("D", new int[] {0,0,1,0,0,1,0});
        keySigMap.put("Bm", new int[] {0,0,1,0,0,1,0});
        keySigMap.put("A", new int[] {0,0,1,0,0,1,1});
        keySigMap.put("F#m", new int[] {0,0,1,0,0,1,1});
        keySigMap.put("E", new int[] {0,0,1,1,0,1,1});
        keySigMap.put("C#m", new int[] {0,0,1,1,0,1,1});
        keySigMap.put("B", new int[] {1,1,1,0,0,1,1});
        keySigMap.put("G#m", new int[] {1,1,1,0,0,1,1});
        keySigMap.put("F#", new int[] {1,1,1,1,0,1,1});
        keySigMap.put("D#m", new int[] {1,1,1,1,0,1,1});
        keySigMap.put("C#", new int[] {1,1,1,1,1,1,1});
        keySigMap.put("A#m", new int[] {1,1,1,1,1,1,1});
        keySigMap.put("F", new int[] {0,-1,0,0,0,0,0});
        keySigMap.put("Dm", new int[] {0,-1,0,0,0,0,0});
        keySigMap.put("Bb", new int[] {0,-1,0,0,-1,0,0});
        keySigMap.put("Gm", new int[] {0,-1,0,0,-1,0,0});
        keySigMap.put("Eb", new int[] {-1,-1,0,0,-1,0,0});
        keySigMap.put("Cm", new int[] {-1,-1,0,0,-1,0,0});
        keySigMap.put("Ab", new int[] {-1,-1,0,-1,-1,0,0});
        keySigMap.put("Fm", new int[] {-1,-1,0,-1,-1,0,0});
        keySigMap.put("Db", new int[] {-1,-1,0,-1,-1,0,-1});
        keySigMap.put("Bbm", new int[] {-1,-1,0,-1,-1,0,-1});
        keySigMap.put("Gb", new int[] {-1,-1,-1,-1,-1,0,-1});
        keySigMap.put("Ebm", new int[] {-1,-1,-1,-1,-1,0,-1});
        keySigMap.put("Cb", new int[] {-1,-1,-1,-1,-1,-1,-1});
        keySigMap.put("Abm", new int[] {-1,-1,-1,-1,-1,-1,-1});
        return keySigMap;
    }
    
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof KeySignature))
            return false;
        KeySignature compareKeySig = (KeySignature)other;
        if (compareKeySig.key.equals(this.key)) {
            return true;
        } else {return false;}
    }
}
