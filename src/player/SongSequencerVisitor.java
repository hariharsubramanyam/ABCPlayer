package player;

import java.util.HashMap;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.LyricListener;
import sound.Pitch;
import sound.SequencePlayer;

/**
 * A "Visitor" which recursively visits a Song and allows the Sequencer to play the song
 */
public class SongSequencerVisitor implements ISongSequencerVisitor{
    
    /**
     * The default note length of the song
     */
    private Fraction defaultNoteLength;
    
    /**
     * The beats per minute of the song
     */
    private int beatsPerMinute;
    
    /**
     * The duration of one beat in the song
     */
    private Fraction tempoBeat;
    
    private HashMap<String, List<Music>> musicForVoiceName;
    
    public SongSequencerVisitor(){
        this.musicForVoiceName = new HashMap<String, List<Music>>();
    }
    
    @Override
    public void visit(Song song) {
        song.getHeader().accept(this);
        song.getBody().accept(this);
    }

    @Override
    public void visit(Header header) {
        this.defaultNoteLength = header.getDefaultLength();
        this.beatsPerMinute = header.getBeatsPerMinute();
        this.tempoBeat = header.getTempoBeat();
        System.out.println(header);
    }

    @Override
    public void visit(Body body) {
        for(Voice v : body.getVoices())
            v.accept(this);
    }

    @Override
    public void visit(Voice voice) {
        this.musicForVoiceName.put(voice.getVoiceName(), voice.getSongComponents());
    }
    
    // Transforms a Song in to language that can be fed to the MIDI Sequencer
    public void play() throws MidiUnavailableException, InvalidMidiDataException{
        // LCM calculations that ultimately give us how many ticks per beat we should have
        Fraction lcmCalc = defaultNoteLength;
        int lcm = 0;
        for (String voiceName : this.musicForVoiceName.keySet()){
            for(Music m : this.musicForVoiceName.get(voiceName)){
                lcmCalc = new Fraction(1, Fraction.LCM(m.getDuration().getDenominator(), lcmCalc.getDenominator()));
            }
            if(lcmCalc.getDenominator() > lcm){
                lcm = lcmCalc.getDenominator();
            }
        }
        LyricListener listener = new LyricListener() {
             public void processLyricEvent(String text) {
                 System.out.println(text);
             }
        };
        // Initializes a new SequencePlayer that will make our notes audible
        SequencePlayer seqPlayer = new SequencePlayer(this.beatsPerMinute, lcm, listener);
        int startTick = 0;
        int duration;
        // Considers every Voice in our Song
        for (String voiceName : this.musicForVoiceName.keySet()){
        	String lyricsPrefix = "";
        	if(!voiceName.equals("THE_DEFAULT_VOICE"))
        		lyricsPrefix = voiceName;
            startTick = 0;
            // Considers every Music element in our Voice
            for(Music m : this.musicForVoiceName.get(voiceName)){
                duration = (int) (m.getDuration().toDouble() * defaultNoteLength.toDouble() / tempoBeat.toDouble() * lcm);
                if (m instanceof Note){
                    Note mNote = (Note)m;
                    Pitch pitch = new Pitch(mNote.getNote().toString().charAt(0)).transpose(mNote.getAccidental().getSemitoneOffset() + 12*mNote.getOctave());
                    if (mNote.getSyllable() != null && !mNote.getSyllable().equals("")) {seqPlayer.addLyricEvent(lyricsPrefix + mNote.getSyllable(), startTick);}
                    seqPlayer.addNote(pitch.toMidiNote(), startTick, duration);
                    startTick += duration;
                } else if (m instanceof Chord){
                    Chord mChord = (Chord)m;
                    if (mChord.getSyllable() != null && !mChord.getSyllable().equals("")) {seqPlayer.addLyricEvent(lyricsPrefix + mChord.getSyllable(), startTick);}
                    for (Note n : mChord.getNotes()){
                        Pitch pitch = new Pitch(n.getNote().toString().charAt(0)).transpose(n.getAccidental().getSemitoneOffset() + 12*n.getOctave());
                        seqPlayer.addNote(pitch.toMidiNote(), startTick, duration);
                    }
                    startTick += duration;
                } else if (m instanceof Tuplet) {
                    Tuplet mTuplet = (Tuplet)m;
                    int tupleNoteDur = duration;
                    tupleNoteDur = getTupleNoteDur(mTuplet.getType(), tupleNoteDur);
                    for (Music tupletElem : mTuplet.getNotes()){
                        if(tupletElem instanceof Note) {
                            Pitch pitch1 = null;
                        	Note n = (Note)tupletElem;
                        	pitch1 = new Pitch(n.getNote().toString().charAt(0)).transpose(n.getAccidental().getSemitoneOffset() + 12*n.getOctave());
                        	if (n.getSyllable() != null && !n.getSyllable().equals("")) {seqPlayer.addLyricEvent(lyricsPrefix + n.getSyllable(), startTick);}
                        	seqPlayer.addNote(pitch1.toMidiNote(), startTick, tupleNoteDur); 
                        	startTick += tupleNoteDur;
                        } else if (tupletElem instanceof Chord) {
                            Chord c = (Chord)tupletElem;
                            if (c.getSyllable() != null && !c.getSyllable().equals("")) {seqPlayer.addLyricEvent(lyricsPrefix + c.getSyllable(), startTick);}
                            for (Note note : c.getNotes()){
                                Pitch pitch2 = null;
                                pitch2 = new Pitch(note.getNote().toString().charAt(0)).transpose(note.getAccidental().getSemitoneOffset() + 12*note.getOctave());
                                seqPlayer.addNote(pitch2.toMidiNote(), startTick, tupleNoteDur);
                            }
                            startTick += tupleNoteDur;
                        } else if (tupletElem instanceof Rest) {
                            startTick += tupleNoteDur;
                        } else {
                            throw new RuntimeException("You cannot build a tuple out of anything but a Note, Chord, or Rest");
                        }
                    }
                } else if (m instanceof Rest){
                    startTick += duration;
                }
            }
        }
        seqPlayer.play();
    }
    
    public static int getTupleNoteDur(TupletEnum type, int duration) {
        switch (type) {
        case DUPLET:
            duration /= 2;
            break;
        case TRIPLET:
            duration /= 3;
            break;
        case QUADRUPLET:
            duration /= 4;
            break;
        }
        return duration;
    }
    
}
