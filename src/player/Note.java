package player;


/**
 * Note is an immutable class that inherits from Music and implements Voice.
 * This is the basis for the music in the song.
 */
public class Note implements Music {
    
    private final NoteEnum note;
    private final AccidentalEnum accidental;
    private final int octave;
    private final Fraction duration;
    
    public Note(NoteEnum note, AccidentalEnum accidental, int octave, Fraction duration){
        this.note = note;
        this.accidental = accidental;
        this.octave = octave;
        this.duration = duration;
    }

    @Override
    public String toString() {
        StringBuilder octaveBuilder = new StringBuilder();
        for(int i = 0; i < this.octave; i++)
            octaveBuilder.append("'");
        for(int i = 0; i < this.octave*-1; i++)
            octaveBuilder.append(",");
        return String.format("%s%s%s%s", this.accidental.toString(), this.note.toString(), octaveBuilder.toString(), this.duration.toString());
    }
   
    /**
     * @return Returns the set duration of the note.
     */
    @Override
    public Fraction getDuration() {return this.duration;}
    
    /**
     * @return Returns the pure note, without accidentals (A,B,C,D,E,F,G)
     */
    public NoteEnum getNote() {
        return note;
    }

    /**
     * @return Returns the accidental associated with the note (-1, 0, 1)
     */
    public AccidentalEnum getAccidental() {
        return accidental;
    }

    /**
     * @return Returns the octave associated with the note. 
     * Any note beyond the octave containing middle C will need to be specified.
     */
    public int getOctave() {
        return octave;
    }
    
    /**
     * @return Gives an new Note with the same specifications as the current note.
     */
    @Override
    public Music copy() {
        return new Note(this.note, this.accidental, this.octave, this.duration);
    }
}
