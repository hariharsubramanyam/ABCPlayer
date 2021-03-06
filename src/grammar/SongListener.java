package grammar;

import grammar.ABCMusicParser.Abc_headerContext;
import grammar.ABCMusicParser.Abc_tuneContext;
import grammar.ABCMusicParser.BarlineContext;
import grammar.ABCMusicParser.Field_voiceContext;
import grammar.ABCMusicParser.L_bracketContext;
import grammar.ABCMusicParser.LyricContext;
import grammar.ABCMusicParser.R_bracketContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import player.AccidentalEnum;
import player.Body;
import player.Chord;
import player.Fraction;
import player.Header;
import player.KeySignature;
import player.Music;
import player.Note;
import player.NoteEnum;
import player.Rest;
import player.Song;
import player.TupletEnum;
import player.Tuplet;
import player.Voice;

/**
 * The SongListener will walk the parse tree and generate a Song object
 */

public class SongListener implements ABCMusicListener {
	
    /**
     * Fields for overall song:
     * Song = Header + Body
     */
	private Song song;
	private Header header;
	private Body body;
	
	/**
	 * Fields for Header
	 * Header = index + title + composer + key + defaultLength + meter + tempoBeat + bpm
	 */
	private int index = 1;
	private String title = "DEFAULT";
	private String composer = "DEFAULT";
	private KeySignature key = null;
	private Fraction defaultLength = null;
	private Fraction meter = new Fraction(4,4);
	private Fraction tempoBeat = null;
	private int bpm = -1;
	
	/**
	 * Fields for Body
	 * Body = List<Voice>
	 */
	private List<Voice> voices = new ArrayList<Voice>();
	
	/**
	 * Returns the bars (List<List<Music>>) for a given voice name
	 * Each bar is a List<Music>
	 */
	private HashMap<String, List<List<Music>>> barsForVoiceName = new HashMap<String, List<List<Music>>>();
	private HashMap<String, Integer> currentBarForVoiceName = new HashMap<String, Integer>();
	
	/**
	 * true if the listener is currently processing a multinote (i.e. tuplet, chord) 
	 */
	private boolean inMultinote = false;
	
	/**
	 * The current voice name being processed
	 */
	private String voiceName;
	
	/**
	 * a hashmap to associate repeats with the appropriate voice
	 * 
	 * the lists contain documentation of repeats:
	 * a repeat is a 3-element array including the following data:
	 * - range of bars to repeat (start & end)
	 * - the bar after which the repeat is inserted
	 * [startBar (inclusive), endBar (exclusive), repeatAt]
	 * the last entry in this list will always be the currently open repeat
	 * (as such, nested repeats are not supported)
	 */
	private HashMap<String, List<Integer[]>> repeatsForVoiceName = new HashMap<String, List<Integer[]>>();
	
	/**
	 * Container for things in voice
	 */
	private List<Music> tupletNotes = new ArrayList<Music>();
	private List<Music> chordNotes = new ArrayList<Music>();
	
	/**
	 * Notes are always put in this container. this will be assigned to the appropriate
	 * container from above (see below) when necessary
	 */
	private List<Music> chordParentContainer = new ArrayList<Music>();
	private List<Music> tupletParentContainer = new ArrayList<Music>();
	private List<Music> noteContainer = new ArrayList<Music>();
	
	/**
	 * When we exit the tune, we construct the song object
	 */
	@Override public void exitAbc_tune(ABCMusicParser.Abc_tuneContext ctx) {
		song = new Song(header, body);
	}
	@Override
    public void enterAbc_header(Abc_headerContext ctx) {   }
	
	/**
	 * When we exit the header, we construct the header object
	 */
	@Override public void exitAbc_header(ABCMusicParser.Abc_headerContext ctx) {
	    // If there's no default length, 
	    if(defaultLength == null)
	        defaultLength = (meter.getNumerator()*1.0/(1.0*meter.getDenominator()) < 0.75) ? new Fraction(1,16) : new Fraction(1,8);
	    if(bpm == -1)
	        bpm = 100;
	    if(tempoBeat == null)
	        tempoBeat = defaultLength;
		header = new Header(index, title, composer, key, meter, bpm, tempoBeat, defaultLength);
	}
	@Override
    public void enterAbc_tune(Abc_tuneContext ctx) {   }
	
	/**
	 * Entering the music section of the file
	 */
	@Override public void enterAbc_music(ABCMusicParser.Abc_musicContext ctx) {	
	    // if there is not voice specified, use a default voice
		//initialize containers
		if(this.voiceName == null){
	        this.voiceName = "THE_DEFAULT_VOICE";
	        List<List<Music>> newVoice = new ArrayList<List<Music>>();
	        newVoice.add(new ArrayList<Music>());
	        this.barsForVoiceName.put(this.voiceName, newVoice);
	        Integer[] startRepeat = new Integer[]{0,0,0};
	        List<Integer[]> reps = new ArrayList<Integer[]>();
	        reps.add(startRepeat);
	        this.repeatsForVoiceName.put(voiceName, reps);
	    }
	}
	@Override public void exitAbc_music(ABCMusicParser.Abc_musicContext ctx) {
	    for(String name : this.barsForVoiceName.keySet()){
	        List<List<Music>> voiceBars = barsForVoiceName.get(name);
	        List<Integer[]> repeats = repeatsForVoiceName.get(name);
	        Integer[] lastRepeat = repeats.get(repeats.size()-1);
	        if(lastRepeat[0] == null || lastRepeat[1] == null || lastRepeat[2] == null)
	        	repeats.remove(repeats.size()-1);
	        int indexShift = 0;
	        for(int i = 0; i < repeats.size(); i++){
	        	List<List<Music>> barsToRepeat = new ArrayList<List<Music>>();
	        	for(int j = repeats.get(i)[0]; j < repeats.get(i)[1]; j++) {
	        		barsToRepeat.add(voiceBars.get(j));
	        	}
	        	voiceBars.addAll(repeats.get(i)[2] + indexShift, barsToRepeat);
	        	indexShift += barsToRepeat.size();
	        }
	        List<Music> concatenatedBars = new ArrayList<Music>();
	        for(List<Music> bar : voiceBars) {
	        	concatenatedBars.addAll(bar);
	        }
	        voices.add(new Voice(name, concatenatedBars));
	    }
		body = new Body(voices);
	}
	
	/**
	 * Header Elements
	 */

	@Override public void enterField_number(ABCMusicParser.Field_numberContext ctx) { }
	@Override public void exitField_number(ABCMusicParser.Field_numberContext ctx) {
		String indexString = ctx.FIELD_NUMBER().getText().replace("X:", "").trim();
		index = Integer.parseInt(indexString);
	}

	@Override public void enterField_title(ABCMusicParser.Field_titleContext ctx) { }
	@Override public void exitField_title(ABCMusicParser.Field_titleContext ctx) {
		title = ctx.FIELD_TITLE().getText().replace("T:", "").trim();
	}

	@Override public void enterOther_fields(ABCMusicParser.Other_fieldsContext ctx) { }
	@Override public void exitOther_fields(ABCMusicParser.Other_fieldsContext ctx) {
		if(ctx.FIELD_COMPOSER() != null) {
			composer = ctx.FIELD_COMPOSER().getText().replace("C:", "").trim();
		}
		if(ctx.FIELD_DEFAULT_LENGTH() != null) {
			String fracText = ctx.FIELD_DEFAULT_LENGTH().getText().replace("L:", "").trim();
			defaultLength = new Fraction(fracText);
		}
		if(ctx.FIELD_METER() != null) {
			String meterString = ctx.FIELD_METER().getText().replace("M:", "").trim();
			if(meterString.equals("C")) {
				meter = new Fraction(4,4);
			} else if(meterString.equals("C|")) {
				meter = new Fraction(2,2);
			} else {
				meter = new Fraction(meterString);
			}
		}
		if(ctx.FIELD_TEMPO() != null) {
		    String[] tempoStrings = ctx.FIELD_TEMPO().getText().replace("Q:","").split("=");
		    tempoBeat = new Fraction(tempoStrings[0].trim());
		    bpm = Integer.parseInt(tempoStrings[1].trim());
		}
		if(ctx.FIELD_VOICE() != null) {
			//initialize containers for this voice
			voiceName = ctx.FIELD_VOICE().getText().replace("V:", "").trim();
			if(!this.barsForVoiceName.containsKey(voiceName)) {
				List<List<Music>> newVoice = new ArrayList<List<Music>>();
				newVoice.add(new ArrayList<Music>());
			    this.barsForVoiceName.put(voiceName, newVoice);
			    Integer[] startRepeat = new Integer[]{0,0,0};
			    List<Integer[]> reps = new ArrayList<Integer[]>();
			    reps.add(startRepeat);
			    this.repeatsForVoiceName.put(voiceName, reps);
			}
		}
	}

	
	@Override public void enterField_key(ABCMusicParser.Field_keyContext ctx) { }
	@Override public void exitField_key(ABCMusicParser.Field_keyContext ctx) {
		String keyString = ctx.FIELD_KEY().getText().replace("K:", "").trim();
		key = new KeySignature(keyString);
	}
	
	/**
	 * Music Elements
	 */
	
	@Override public void enterBarline(ABCMusicParser.BarlineContext ctx) {
		//parse notes into bars and keep track of repeats
		String barlineString = "";
		if(ctx.BARLINE() != null)
			barlineString = ctx.BARLINE().getText();
		else if(ctx.NTH_REPEAT() != null)
			barlineString = ctx.NTH_REPEAT().getText();
		
		List<List<Music>> bars = barsForVoiceName.get(voiceName);
		//does not support nested repeats/multiple end repeats to same start repeat
		boolean addNewBar = true;
		
		List<Integer[]> repeats = repeatsForVoiceName.get(voiceName);
		Integer[] currentRepeat  = null;
		if(repeats.size() > 0)
			currentRepeat = repeats.get(repeats.size()-1);
		else
			currentRepeat = new Integer[3];
		
		if(barlineString.equals("|:")) {
			currentRepeat[0] = new Integer(bars.size());
		} else if(barlineString.equals(":|")) {
			if(currentRepeat[1] == null)
				currentRepeat[1] = new Integer(bars.size());
			currentRepeat[2] = new Integer(bars.size() + 1);
		} else if(barlineString.equals("[1")) {
			addNewBar = false;
			currentRepeat[1] = new Integer(bars.size());
		} else if(barlineString.equals("[2")) {
			addNewBar = false;
		} else if(barlineString.equals("[|")) {
			if(currentRepeat[0] == null)
				currentRepeat[0] = new Integer(bars.size());
		}
		if(repeats.size() > 0)
			repeats.set(repeats.size()-1, currentRepeat);
		else
			repeats.add(currentRepeat);
		if(addNewBar)
			bars.add(new ArrayList<Music>());
	}
	
	@Override public void enterAbc_line(ABCMusicParser.Abc_lineContext ctx) { }
	@Override public void exitAbc_line(ABCMusicParser.Abc_lineContext ctx) {
		List<List<Music>> bars = barsForVoiceName.get(voiceName);
		int lineLength = bars.size();
		currentBarForVoiceName.put(voiceName, lineLength);
	}
	
	@Override public void enterElement(ABCMusicParser.ElementContext ctx) { }
	@Override public void exitElement(ABCMusicParser.ElementContext ctx) { }

	@Override public void enterMultinote(ABCMusicParser.MultinoteContext ctx) {	
	}
	@Override public void exitMultinote(ABCMusicParser.MultinoteContext ctx) {
	    this.inMultinote = false;
	}

	@Override public void enterTuplet_element(ABCMusicParser.Tuplet_elementContext ctx) {
		tupletParentContainer = noteContainer;
		//empty the tuplet note container
		tupletNotes = new ArrayList<Music>();
		//set the tuplet container as the current destination for all notes
		noteContainer = tupletNotes;
		this.inMultinote = true;
	}
	@Override public void exitTuplet_element(ABCMusicParser.Tuplet_elementContext ctx) {
		int type = 3;
	    if(ctx.DUPLET() != null)
		    type = 2;
	    else if (ctx.QUADRUPLET() != null)
	        type = 4;
	    
	    TupletEnum tupletType = TupletEnum.TRIPLET;
		switch(type) {
		    case 2: tupletType = TupletEnum.DUPLET; break;
		    case 3: tupletType = TupletEnum.TRIPLET; break;
		    case 4: tupletType = TupletEnum.QUADRUPLET; break;
		}
		this.inMultinote = false;
		//create the tuplet object and append it to the voice
		tupletParentContainer.add(new Tuplet(tupletType, tupletNotes));		    
		//set the container back to the main voice
		noteContainer = tupletParentContainer;
		List<List<Music>> bars = this.barsForVoiceName.get(voiceName);
		bars.get(bars.size()-1).add(new Tuplet(tupletType, tupletNotes));
	}

	@Override public void enterNote_element(ABCMusicParser.Note_elementContext ctx) { }
	/**
	 * Called whenever the listener exits a note_element. If the note element is also a multinote,
	 * it ignores and lets the multinote handler take it. If it is a basic note, parses and creates
	 * the note object
	 */
	@Override public void exitNote_element(ABCMusicParser.Note_elementContext ctx) {
		// if this is a base note element, not a multinote (chord)
		if(ctx.NOTE() != null) {
			//Split the string into a note and a note_duration part
			String noteString = ctx.NOTE().getText();
			String[] splitNote = noteString.split("(?=[\\d+/])",2);
			String pitchString = splitNote[0];
			//Parse the not duration
			Fraction duration = new Fraction(1,1);
			if(splitNote.length == 2) {
				String durationString = splitNote[1];
				String[] splitFraction = durationString.split("(?=/)|(?<=/)");
				if(splitFraction.length == 3) {
					int num = 1;
					int den = 2;
					if(splitFraction[0].equals("")) {
						den = Integer.parseInt(splitFraction[2]);
					} else if(splitFraction[2].equals("")) {
						//this case should never happen..right?
					} else {
						num = Integer.parseInt(splitFraction[0]);
						den = Integer.parseInt(splitFraction[2]);
					}
					duration = new Fraction(num, den);
				} else if(splitFraction.length == 1) {
					int num = Integer.parseInt(splitFraction[0]);
					int den = 1;
					duration = new Fraction(num, den);
				} else if (splitFraction.length == 2){
				    int num = 1;
				    int den = 2;
				    duration = new Fraction(num, den);
				}
			}
			//Set arbitrary default values
			NoteEnum baseNote = NoteEnum.C;
			AccidentalEnum accidental = AccidentalEnum.NONE;
			int octave = 0;	
			
			//split the pitch into accidental, basenote, octave
			String[] splitPitch = pitchString.split("(?=[A-Ga-gz])|(?<=[A-Ga-gz])");
			
			//parse for basenote
			String basenoteString = splitPitch[1];
			if(basenoteString.toLowerCase().equals("a")) {
				baseNote = NoteEnum.A;
			} else if(basenoteString.toLowerCase().equals("b")) {
				baseNote = NoteEnum.B;
			} else if(basenoteString.toLowerCase().equals("c")) {
				baseNote = NoteEnum.C;
			} else if(basenoteString.toLowerCase().equals("d")) {
				baseNote = NoteEnum.D;
			} else if(basenoteString.toLowerCase().equals("e")) {
				baseNote = NoteEnum.E;
			} else if(basenoteString.toLowerCase().equals("f")) {
				baseNote = NoteEnum.F;
			} else if(basenoteString.toLowerCase().equals("g")) {
				baseNote = NoteEnum.G;
			}
			
			//Apply key signature
			accidental = setKeySigAccidental(baseNote);			
			
			//Apply any inline accidentals
			String accidentalString = splitPitch[0];
			if(accidentalString.equals("_")) {
				accidental = AccidentalEnum.FLAT;
			} else if(accidentalString.equals("__")) {
				accidental = AccidentalEnum.DOUBLE_FLAT;
			} else if(accidentalString.equals("^")) {
				accidental = AccidentalEnum.SHARP;
			} else if(accidentalString.equals("^^")) {
				accidental = AccidentalEnum.DOUBLE_SHARP;
			} else if(accidentalString.equals("=")) {
				accidental = AccidentalEnum.NATURAL;
			}
			
			//parse for octave
			if(basenoteString.equals(basenoteString.toLowerCase()))
				octave++;
			if(splitPitch.length == 3) {
				//if octave is specified
				String octaveString = splitPitch[2];
				String octaveType = octaveString.substring(0, 1); 
				for(int i=0; i<octaveString.length(); i++) {
					if(octaveType.equals("'"))
						octave++;
					else if(octaveType.equals(","))
						octave--;
				}
			}		
			
			//add a rest or a note
			if(basenoteString.equals("z")) {
			    for(int i = 0; i < 20; i++)
				noteContainer.add(new Rest(duration));
			    if(!this.inMultinote) {
			        List<List<Music>> bars = this.barsForVoiceName.get(voiceName);
			    	bars.get(bars.size()-1).add(new Rest(duration));
			    }
			} else {
				noteContainer.add(new Note(baseNote, accidental, octave, duration));
				if(!this.inMultinote) {
					List<List<Music>> bars = this.barsForVoiceName.get(voiceName);
			    	bars.get(bars.size()-1).add(new Note(baseNote, accidental, octave, duration));
				}
			}
		}
	}
	
	@Override
    public void enterLyric(LyricContext ctx) {}
    @Override
    public void exitLyric(LyricContext ctx) {
    	//parse the lyrics into a list of individual syllables to be matched with notes
        List<String> lyric = new ArrayList<String>();
        StringBuilder syllable = new StringBuilder();
        String context = ctx.LYRIC().getText();

        for (int i=2; i < context.length(); i++) { //start at i = 2 so as to skip the w: at the beginning of lyric

            if (context.charAt(i) == '-') {
            	if(syllable != null) {
	            	syllable.append("-");
	                lyric.add(syllable.toString());
            	} else {
            		lyric.add("-");
            	}
                syllable = null;
            } else if (context.charAt(i) == '_') {
            	if(syllable != null) {
	            	syllable.append("_");
	                lyric.add(syllable.toString());
            	}
            	lyric.add("_");
                syllable = null;
            } else if (context.charAt(i) == '*') {
            	if(syllable != null)
            		lyric.add(syllable.toString());
            	lyric.add("");
                syllable = null;
            } else if (context.charAt(i) == '~') {
                syllable.append(" "); //
            } else if (String.valueOf(context.charAt(i)).equals("/-")) {
                syllable.append("-");
            } else if (context.charAt(i) == '|') {
                if(syllable != null)
                	lyric.add(syllable.toString());
                lyric.add("|");
                syllable = null;
            } else if(context.charAt(i) == ' ') {
            	if(syllable != null)
            		lyric.add(syllable.toString());
            	syllable = null;
            } else { // else context[i] should be a letter
            	if(syllable == null)
            		syllable = new StringBuilder();
                syllable.append(context.charAt(i) + "");
            } 
            if(i == context.length()-1 && syllable != null) {
            	//don't forget to add any leftovers at the end!
            	lyric.add(syllable.toString());
            }
        } 
        matchLyricsToNotes(lyric, barsForVoiceName.get(voiceName));
    }
    @Override
    public void enterField_voice(Field_voiceContext ctx) {}
    @Override
    public void exitField_voice(Field_voiceContext ctx) {
        voiceName = ctx.getText().replace("V:", "").trim();
        if(!this.barsForVoiceName.containsKey(voiceName)) {
        	List<List<Music>> newVoice = new ArrayList<List<Music>>();
        	newVoice.add(new ArrayList<Music>());
            this.barsForVoiceName.put(voiceName, newVoice);
		    Integer[] startRepeat = new Integer[]{0,0,0};
		    List<Integer[]> reps = new ArrayList<Integer[]>();
		    reps.add(startRepeat);
		    this.repeatsForVoiceName.put(voiceName, reps);
        }
    }

	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) { }
	@Override public void visitErrorNode(ErrorNode node) { }
	
	private AccidentalEnum setKeySigAccidental(NoteEnum note) {
		int[] accidentals = key.getAccidentals();
		int index = 0;
		switch(note) {
		case A: index = 0; break;
		case B: index = 1; break;
		case C: index = 2; break;
		case D: index = 3; break;
		case E: index = 4; break;
		case F: index = 5; break;
		case G: index = 6; break;
		}
		if(accidentals[index] == 2)
			return AccidentalEnum.DOUBLE_SHARP;
		if(accidentals[index] == 1)
			return AccidentalEnum.SHARP;
		if(accidentals[index] == 0)
			return AccidentalEnum.NONE;
		if(accidentals[index] == -1)
			return AccidentalEnum.FLAT;
		if(accidentals[index] == -2)
			return AccidentalEnum.DOUBLE_FLAT;
		return AccidentalEnum.NONE;
	}
	
	private void matchLyricsToNotes(List<String> lyrics, List<List<Music>> bars) {
		int noteCount = 0;
		int startBar = 0;
		if(currentBarForVoiceName.containsKey(voiceName))
			startBar = currentBarForVoiceName.get(voiceName);
		for(int i=startBar; i<bars.size(); i++) {
			List<Music> bar = bars.get(i);
			for(int j=0; j<bar.size(); j++) {
				int index = noteCount;
				String lyric = "";
				if(index < lyrics.size()-1 && index >= -1)
					lyric = lyrics.get(index+1);
				Music m = bar.get(j);
				if(m instanceof Note) {
					noteCount++;
					((Note)m).setSyllable(lyric);
				} else if(m instanceof Chord) {
					noteCount++;
					((Chord)m).setSyllable(lyric);
				} else if(m instanceof Tuplet) {
					for(Music subMusic : ((Tuplet)m).getNotes() ) {
						if(subMusic instanceof Note) {
							noteCount++;
							((Note)subMusic).setSyllable(lyric);
						} else if(subMusic instanceof Chord) {
							noteCount++;
							((Chord)subMusic).setSyllable(lyric);
						}
					}
				} else if(m instanceof Rest) {
					//Do nothing, because rests cannot have lyrics
				}
				//if the lyric is a new bar character
				if(index < lyrics.size()-1) {
					String nextLyric = lyrics.get(index + 1);
					if(nextLyric.equals("|")) {
						noteCount++;
						break;
					}
				}
			}
		}
	}

	public Song getSong() {
		return song;
	}
    @Override
    public void enterL_bracket(L_bracketContext ctx) {}
    @Override
    public void exitL_bracket(L_bracketContext ctx) {
        chordParentContainer = noteContainer;
        chordNotes = new ArrayList<Music>();
        noteContainer = chordNotes;
        this.inMultinote = true;
    }
    @Override
    public void enterR_bracket(R_bracketContext ctx) {}
    @Override
    public void exitR_bracket(R_bracketContext ctx) {
        ArrayList<Note> notes = new ArrayList<Note>();
        for(Music m : chordNotes) {
            notes.add((Note)m);
        }
        chordParentContainer.add(new Chord(notes));
        noteContainer = chordParentContainer;
        this.inMultinote = false;
        List<List<Music>> bars = this.barsForVoiceName.get(voiceName);
        bars.get(bars.size()-1).add(new Chord(notes));
        
    }
	@Override
	public void exitBarline(BarlineContext ctx) {}    
}
