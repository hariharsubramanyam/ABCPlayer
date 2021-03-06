package player;

import java.util.ArrayList;
import java.util.List;

/**
 * This defines the basic functionality of all the constituents of the song (music + lyric).
 * Voice provides methods for the Visitor to use to transform notes, chords, lyrics, etc. to something that
 * can be used and played by the SequencePlayer. 
 */
public class Voice implements ISongSequencerVisitable{

    /**
     * The name of this voice
     */
    private String voiceName;
    
    /**
     * The list of music (ex. notes (with lyrics), chords, tuplets) in this voice
     */
    private List<Music> songComponents;

    /**
     * Getter function for returning the voice name.
     * @return Returns the voice name.
     */
    public String getVoiceName() {
        return voiceName;
    }

    /**
     * Setter method for setting the voice name.
     * @param voiceName is inputed as a string.
     */
    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }
    
    /**
     * Returns a list of the Musics in the current Song.
     * @return the song components
     */
    public List<Music> getSongComponents() {
        return this.songComponents;
    }

    /**
     * Setter method for setting the components of the song.
     * @param songComponents is a list of Musics.
     */
    public void setSongComponents(List<Music> songComponents) {
        this.songComponents = songComponents;
    }

    /**
     * Initializes the new Voice object.
     * @param voiceName is a String.
     * @param songComponents is a list of Musics.
     */
    public Voice(String voiceName, List<Music> songComponents){
        this.voiceName = voiceName;
        this.songComponents = new ArrayList<Music>();
        for (Music s : songComponents)
            this.songComponents.add(s.copy());
    }
    
    /**
     * Throws an exception because you cannot find the total duration of the song as it is not something that is tracked.
     * You can only know the duration of one Music at a time. (Tuplet, Note, Chord, Rest)
     */
    public Fraction getDuration(){
        throw new UnsupportedOperationException("Can't get duration");
        }

    /**
     * Allows a visitor object to process this object
     */
    @Override
    public void accept(ISongSequencerVisitor visitor) {
        visitor.visit(this);
    }
    
    /**
     * @return the string representation of this object
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(this.voiceName +": ");
        for (Music m : this.songComponents)
            builder.append("\n - " + m.toString() + " ");
        return builder.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Voice))
            return false;
        Voice compareVoice = (Voice)other;
        for (int i = 0; i < compareVoice.songComponents.size(); i ++) {
           if (!(compareVoice.songComponents.get(i).equals(this.songComponents.get(i))))
               return false;
        }
        return true;
    }
}
