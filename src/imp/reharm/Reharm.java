package imp.reharm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import imp.data.Chord;
import imp.data.MelodyPart;
import imp.data.Note;
import imp.data.Score;

public abstract class Reharm {
    
    /**
     * Creates a new Reharm instance. Called by subclasses upon their instantiation.
     * @param score The Score instance that this Reharm instance will reharmonise.
     */
    public Reharm(Score score) {
        this.score = score;
    }

    /**
     * The HashMap of chords diatonic to the given key signature.
     */
    protected HashMap<String, String[]> keyChords;
    
    /**
     * The Score instance to reharmonise.
     */
    protected Score score;

    /**
     * The MelodyPart instance in the Score to reharmonise.
     */
    protected MelodyPart targetPart;

    /**
     * The number of slots that each chord will last
     */
    protected int chordDuration;
    /**
     * An array of locked slots
     */
    protected ArrayList<Integer> lockedSlots;

    /** An array containing String representations of the notes of the C major scale. */
    protected String[] cScale         = {"c", "d", "e", "f", "g", "a", "b"};
    /** An array containing String representations of the notes of the G major scale. */
    protected String[] gScale         = {"g", "a", "b", "c", "d", "e", "f#"};
    /** An array containing String representations of the notes of the D major scale. */
    protected String[] dScale         = {"d", "e", "f#", "g", "a", "b", "c#"};
    /** An array containing String representations of the notes of the A major scale. */
    protected String[] aScale         = {"a", "b", "c#", "d", "e", "f#", "g#"};
    /** An array containing String representations of the notes of the E major scale. */
    protected String[] eScale         = {"e", "f#", "g#", "a", "b", "c#", "d#"};
    /** An array containing String representations of the notes of the B major scale. */
    protected String[] bScale         = {"b", "c#", "d#", "e", "f#", "g#", "a#"};
    /** An array containing String representations of the notes of the F# major scale. */
    protected String[] fSharpScale    = {"f#", "g#", "a#", "b", "c#", "d#", "e#"};
    /** An array containing String representations of the notes of the F major scale. */
    protected String[] fScale         = {"f", "g", "a", "bb", "c", "d", "e"};
    /** An array containing String representations of the notes of the Bb major scale. */
    protected String[] bFlatScale     = {"bb", "c", "d", "eb", "f", "g", "a"};
    /** An array containing String representations of the notes of the Eb major scale. */
    protected String[] eFlatScale     = {"eb", "f", "g", "ab", "bb", "c", "d"};
    /** An array containing String representations of the notes of the Ab major scale. */
    protected String[] aFlatScale     = {"ab", "bb", "c", "db", "eb", "f", "g"};
    /** An array containing String representations of the notes of the Db major scale. */
    protected String[] dFlatScale     = {"db", "eb", "f", "gb", "ab", "bb", "c"};
    /** An array containing String representations of the notes of the Gb major scale. */
    protected String[] gFlatScale     = {"gb", "ab", "bb", "cb", "db", "eb", "f"};

    /**
     * An array of major scales that correspond to the sharp keys. The index of the scale 
     * in the array indicates the number of sharps that key has (i.e. G major has 1 sharp
     * so is placed at index 1).
     */
    protected String[][] sharpKeys    = {cScale, gScale, dScale, aScale, eScale, bScale, fSharpScale};
    /**
     * An array of major scales that correspond to the flat keys. The index of the scale 
     * in the array indicates the number of flats that key has (i.e. Gb major has 6 flats
     * so is placed at index 6).
     */
    protected String[][] flatKeys     = {cScale, fScale, bFlatScale, eFlatScale, aFlatScale, dFlatScale, gFlatScale};


    /* ABSTRACT METHODS TO BE DEFINED IN SUBCLASSES */

    /**
     * Use this to match groups of chords to the notes in the scale with which they will fit.
     * Make and return a 2-dimensional array of Strings taken from the chords parameter.
     * @param chords The root chords to base the chord groups on.
     * @return A two-dimensional array of chord groups, each matching the index of a note in the scale.
     */
    public abstract String[][] generateSubstitutions(String[] chords);
    /**
     * This method sets the frequency at which new chords will be generated. Simply make a call to 
     * setChordDuration(chordDuration), replacing chordDuration parameter with the required length 
     * in slots between chords.
     */
    public abstract int getChordDuration();
    /**
     * This method is run at every point a new chord is to be placed. It details how the 
     * chord will be picked, based on the keyChords HashMap of notes and suitable chords.
     * @param slot The slot in which to put a chord. We iterate through the chordSlots 
     * and implement this method for each.
     */
    public abstract void implementChordChoice(int slot);

    public abstract void adjustChordChoice(int slot);


    /* METHODS USED TO RUN THE REHARM FEATURE (CALLED IN NOTATE) */

    /**
     * Generates a set of potential chords for each note in the current key signature of the score.
     */
    public void generateChords() {
        String[] notesInKey = getNotesInKey();
        setKeyChords(notesInKey, generateSubstitutions(generateRootTriads(notesInKey)));
    }

    /**
     * Goes through each bar in the score, implementing chords as defined in generateSubstitutions() 
     * and implementChordChoice().
     */
    public void execute() {
        // Select the MelodyPart for analysis
        targetPart = score.getPart(0);
        if(targetPart == null) return;
        initLockedSlots();
        score.getChordProg().delUnits(0, score.getChordProg().size());
        chordDuration = getChordDuration();
        // Iterate through each bar, setting chords that work with the notes present
        for(int chordSlot = 0; chordSlot < targetPart.size(); chordSlot = chordSlot + chordDuration) {
            implementChordChoice(chordSlot);
        }
        // Iterate through each bar again, adjusting the chords to work with surrounding chords.
        for(int chordSlot = 0; chordSlot < targetPart.size(); chordSlot = chordSlot + chordDuration) {
            adjustChordChoice(chordSlot);
        }
    }


    /* HELPER FUNCTIONS */

    /* Useful in generateSubstitutions() */

    /**
     * Generates an empty array to hold potential chord choice names for each of the 7 diatonic notes 
     * in the current key. Each index matches up to the corresponding note in the major scale for
     * the key, but zero-indexed (e.g. chords to be paired with note 1 in scale are placed in the 
     * array at index 0, etc.)
     * @return The empty array, to be filled with String arrays of appropriate chord names.
     */
    public String[][] diatonicChordSets() {
        return new String[7][];
    }


    /**
     * Generates an empty array to hold potential chord choice names for each of the 12 chromatic notes 
     * in the current key. Each index matches up to the corresponding note in the chromatic scale for
     * the key, but zero-indexed (e.g. in the key of C, chords to be paired with the note C are placed 
     * at index 0 and those that go with Db are placed at index 1,  etc.)
     * @return The empty array, to be filled with String arrays of appropriate chord names.
     */
    public String[][] chromaticChordSets() {
        return new String[12][];
    }


    /**
     * Sets the keyChords HashMap so that the elements in the notes array become the keys
     * and the arrays inside the chordSets 2-dimensional array become the values. Keys 
     * and values are paired together by matching the indices in their respective arrays.
     * @param notes The note names that will become the keys.
     * @param chordSets The arrays of chord names that will become the values.
     */
    public void setKeyChords(String[] notes, String[][] chordSets) {
        if(keyChords == null) {
            keyChords = new HashMap<>();
        }
        keyChords.clear();
        
        if(chordSets.length == 7) {
            for(int i = 0; i < notes.length && i < chordSets.length; i++) {
                keyChords.put(notes[i], chordSets[i]);
            }
        }
        if(chordSets.length == 12) {
            // Define the number of semitones between notes of the major scale.
            int[] intervals = {2, 2, 1, 2, 2, 2};
            // Match the notes of the major scale to the corresponding chord sets,
            // leave out the gaps between the major scale notes for now.
            int i = 0;
            int j = 0;
            while(i < notes.length) {
                keyChords.put(notes[i], chordSets[j]);
                if(i < 6) j+=intervals[i];
                i++;
            }
            // Fill in the gaps between the major scale notes.
            for(i = 0, j = 1; i < notes.length; i++, j++) {
                if(i == 2 || i == 6) { // Case 1: There is no gap between two notes (they are a semitone apart).
                    j--;
                    continue;
                } else if(notes[i].length() == 1) { // Case 2: There is a gap, and the current note is a natural (no sharps or flats).
                    // add a # symbol to the note and match it to the corresponding chord set.
                    keyChords.put(notes[i] + "#", chordSets[i+j]);
                    // get enharmonic equivalent of # note above and match it to the corresponding chord set.
                    keyChords.put(generateEquivalents().get(notes[i] + "#"), chordSets[i+j]);
                } else if(notes[i].charAt(1) == '#') { // Case 3: There is a gap, and the current note is a sharp (#).
                    String newNote = Character.toString(notes[i+1].charAt(0));
                    keyChords.put(newNote, chordSets[i+j]);
                } else if(notes[i].charAt(1) == 'b') { // Case 4: There is a gap, and the current note is a flat (b).
                    String newNote = Character.toString(notes[i].charAt(0));
                    keyChords.put(newNote, chordSets[i+j]);
                }
            }
        }
    }

    
    /** 
     * Targets all the chordSets array positions that correspond to notes that are in-key and fills them
     * at the given index with chords from the given source chord set.
     * @param destination The chordSets array to be filled.
     * @param indexToFill The index to fill within each targeted position of the chordSets array.
     * @param source The chords to use to populate the destination chordSets array.
     */
    public void populateDiatonicChordSets(String[][] destination, int indexToFill, String[] source) {
        if(destination.length == 7) {
            for(int i = 0; i < destination.length; i++) {
                addToChordSet(destination, i, indexToFill, source[i]);
            }
        }
        if(destination.length == 12) {
            // Populate the destination array with the contents of the source array
            int[] majorScaleIndices = getMajorScaleIndices();
            
            for(int destIndex = 0, sourceIndex = 0; destIndex < destination.length; destIndex++) {
                boolean inKey = false;
                for(int i = 0; i < majorScaleIndices.length; i++) {
                    if(destIndex == majorScaleIndices[i]) {
                        inKey = true;
                    }
                }
                // If outputIndex is the same as one of the chords indices...
                if(inKey) {
                addToChordSet(destination, destIndex, indexToFill, source[sourceIndex]);
                sourceIndex++;
                }
            }          
        }
    }


    public void initLockedSlots() {
        if(lockedSlots == null) {
            lockedSlots = new ArrayList<>();
        }
        unlockAllSlots();
    }


    public void lock(int slot) {
        if(isLocked(slot)) return;
        lockedSlots.add(slot);
    }


    public void unlock(int slot) {
        lockedSlots.remove((Integer)slot);
    }


    public boolean isLocked(int slot) {
        return lockedSlots.contains(slot);
    }
    

    public void unlockAllSlots() {
        lockedSlots.clear();
    }

    
    /** 
     * Targets all the chordSets array positions that correspond to notes that are not in-key and fills them
     * at the given index with chords from the given source chord set.
     * @param destination The chordSets array to be filled.
     * @param indexToFill The index to fill within each targeted position of the chordSets array.
     * @param source The chords to use to populate the destination chordSets array.
     */
    public void populateOutsideChordSets(String[][] destination, int indexToFill, String[] source) {
        // Do not allow this method to complete if user has not given a chromatic chordSets destination array of length 12
        if(destination.length != 12) return;

        int[] majorScaleIndices = getMajorScaleIndices();

        for(int destIndex = 0, sourceIndex = 0; destIndex < destination.length; destIndex++) {
            boolean inKey = false;
            for(int i = 0; i < majorScaleIndices.length; i++) {
                if(destIndex == majorScaleIndices[i]) {
                    inKey = true;
                }
            }
            if(!inKey) {
            addToChordSet(destination, destIndex, indexToFill, source[sourceIndex]);
            sourceIndex++;
            }
        }
    }


    /**
     * Adds all the default chord names to the default positions to the chordSets array.
     * @param chordSets The array to add chord names to.
     */
    public void initStandardSubstitutions(String[][] chordSets) {
        addDiatonicChords(chordSets);
        addTritoneSubs(chordSets);
        addHarmonicSubs(chordSets);
        addDimChords(chordSets);
    }


    /**
     * Pairs the corresponding diatonic chords with the notes in the major scale in the current key. 
     * @param chordSets The array to add chord names to.
     */
    public void addDiatonicChords(String[][] chordSets) {
        populateDiatonicChordSets(chordSets, 0, generateRootTriads(getNotesInKey()));
    }

    
    /**
     * Pairs the corresponding tritone substitutions with the notes in the major scale in the current key. 
     * @param chordSets The array to add chord names to.
     */
    public void addTritoneSubs(String[][] chordSets) {
        // Create an array of tritone substitutions to add to chordSets later.
        String[] tritoneChords = generateTritoneSubs();
        // Add tritone subs to the diatonic positions in the chordSets array.
        populateDiatonicChordSets(chordSets, 1, tritoneChords);
    }


    /**
     * Pairs the corresponding harmonic substitutions with the notes in the major scale in the current key. 
     * @param chordSets The array to add chord names to.
     */
    public void addHarmonicSubs(String[][] chordSets) {
        if(chordSets.length == 7) {
            addToChordSet(chordSets, 0, 2, generateRootTriads(getNotesInKey())[2]);
            addToChordSet(chordSets, 0, 3, generateRootTriads(getNotesInKey())[5]);
            addToChordSet(chordSets, 1, 2, generateRootTriads(getNotesInKey())[3]);
            addToChordSet(chordSets, 2, 2, generateRootTriads(getNotesInKey())[5]);
            addToChordSet(chordSets, 2, 3, generateRootTriads(getNotesInKey())[1]);
            addToChordSet(chordSets, 3, 2, generateRootTriads(getNotesInKey())[1]);
            addToChordSet(chordSets, 4, 2, generateRootTriads(getNotesInKey())[6]);
            addToChordSet(chordSets, 5, 2, generateRootTriads(getNotesInKey())[0]);
            addToChordSet(chordSets, 5, 3, generateRootTriads(getNotesInKey())[2]);
            addToChordSet(chordSets, 6, 2, generateRootTriads(getNotesInKey())[4]);
        }
        if(chordSets.length == 12) {
            addToChordSet(chordSets, 0, 2, generateRootTriads(getNotesInKey())[2]);
            addToChordSet(chordSets, 0, 3, generateRootTriads(getNotesInKey())[5]);
            addToChordSet(chordSets, 2, 2, generateRootTriads(getNotesInKey())[3]);
            addToChordSet(chordSets, 4, 2, generateRootTriads(getNotesInKey())[5]);
            addToChordSet(chordSets, 4, 3, generateRootTriads(getNotesInKey())[1]);
            addToChordSet(chordSets, 5, 2, generateRootTriads(getNotesInKey())[1]);
            addToChordSet(chordSets, 7, 2, generateRootTriads(getNotesInKey())[6]);
            addToChordSet(chordSets, 9, 2, generateRootTriads(getNotesInKey())[0]);
            addToChordSet(chordSets, 9, 3, generateRootTriads(getNotesInKey())[2]);
            addToChordSet(chordSets, 11, 2, generateRootTriads(getNotesInKey())[4]);
        }
    }


    /**
     * Pairs the corresponding diminished chords with the notes that fall outside the current key. 
     * @param chordSets The array to add chord names to.
     */
    public void addDimChords(String[][] chordSets) {
        // Create diminished chords for the non-diatonic positions of the chordSets array.
        String[] dimChords = generateNonDiatonicDimChords();
        // Add diminished chords to the non-diatonic positions in the chordSets array.
        populateOutsideChordSets(chordSets, 0, dimChords);
    }


    /**
     * Analyses the diatonic chords of the current key and creates a new 
     * chord array of tritone substitution triads based on those chords.
     * @return An array of tritone substitutions for the given chords.
     */
    public String[] generateTritoneSubs() {
        String[] chords = generateRootTriads(getNotesInKey());
        String[] tritoneSubs = new String[chords.length];
        for(int i = 0; i < chords.length; i++) {
            String chord = chords[(i + 3) % 7];
            
            if(i == 3) {
                if(chord.length() > 1 && (chord.charAt(1) == '#' || chord.charAt(1) == 'b')) {
                    chord = chord.substring(0, 2);
                } else if(chord.length() > 1) {
                    chord = chord.substring(0, 1);
                }
            } if(i != 3) {
                if(chord.length() > 1 && chord.charAt(1) == '#') {
                    chord = chords[(i + 4) % 7].substring(0, 1);
                } else if(chord.length() > 1 && chord.charAt(1) == 'b') {
                    chord = chord.substring(0, 1);
                } else if (chord.length() > 1) {
                    if(chord.charAt(0) == 'B') {
                        chord = "C";
                    } else if(chord.charAt(0) == 'E') {
                        chord = "F";
                    } else {
                        chord = chord.substring(0, 1) + "#";
                    }
                } else {
                    chord += "#";
                }
            }
            tritoneSubs[i] = chord;
        }
        return tritoneSubs;
    }


    /**
     * Creates a new chord array of diminished 7th chords based on the chromatic notes outside of 
     * the current key.
     * @return An array of diminished chords based on all notes outside of the current key.
     */
    public String[] generateNonDiatonicDimChords() {
        String[] dimChords = new String[5];
        String[] diatonicChords = generateRootTriads(getNotesInKey());
        
        for(int i = 0, j = 0; i < diatonicChords.length && j < dimChords.length; i++, j++) {
            if(i == 2 || i == 6) {
                j--;
                continue;
            } else {
                if(diatonicChords[i].length() > 1 && diatonicChords[i].charAt(1) == '#') {
                    dimChords[j] = diatonicChords[i+1].substring(0, 1) + "dim7";
                } else if(diatonicChords[i].length() > 1 && diatonicChords[i].charAt(1) == 'b') {
                    dimChords[j] = diatonicChords[i].substring(0, 1) + "dim7";
                } else {
                    dimChords[j] = diatonicChords[i].substring(0, 1) + "#dim7";
                }
            }
        }
        return dimChords;
    }


    /* Useful in setChordDuration() */

    /**
     * Sets how many slots will pass before thinking about adding another chord.
     * Use inside your implementation of setChordDuration() in subclasses.
     * @param slots the number of slots to wait before trying to add another chord.
     */
    public void setChordDuration(int slots) {
        chordDuration = slots;
    }


    /* Useful in implementChordChoice() */
    
    /**
     * Checks whether the given slot is at the start of a bar.
     * @param slot The slot to check.
     * @return True if slot is at the start of the bar, false otherwise.
     */
    public boolean isStartOfBar(int slot) {
        return slot % score.getSlotsPerMeasure() == 0;
    }


    /**
     * Gets the root note associated with a given chord.
     * @param chord The name of the chord to get the root note for.
     * @return The name of the root note of the chord.
     */
    public String getRoot(String chord) {
        // Get the root note of the chord
        String root = "";
        if(chord.length() == 1) {
            root = chord.toLowerCase();
        } else if(chord.length() > 1) {
            if(chord.charAt(1) == '#' || chord.charAt(1) == 'b') {
                root = chord.substring(0, 1).toLowerCase() + chord.substring(1, 2);
            } else {
                root = chord.substring(0, 1).toLowerCase();
            }
        }
        return root;
    }


    /** 
     * Returns an array of string representations of the notes in the corresponding 
     * 7th arpeggio in the current key based on the chord given.
     * @param chord The chord to base the arpeggio on.
     * @return String[] An array of notes that make up the corresponding arpeggio.
     */
    public String[] get7thArpeggio(String chord) {
        // Find the index of the current note in the notes in key
        String[] notesInKey = getNotesInKey();
        int indexInKey = 0;
        boolean isNull = true;
        for(int i = 0; i < notesInKey.length; i++) {
            if(notesInKey[i].equals(getRoot(chord))) {
                indexInKey = i;
                isNull = false;
            }
        }
        if(isNull) {
            return null;
        } else {
            String[] arpeggio = new String[4];
            for(int i = 0, j = indexInKey; i < 4; i++, j = (j + 2) % 7) {
                arpeggio[i] = notesInKey[j];
            }
            return arpeggio;
        }
    }


    /**
     * Looks at the given slot and either uses the note that falls in its place to 
     * generate a chord or deletes the chord that is present.
     * @param chordChoice The index of the chord to be placed, taken from the chordSets 
     * array for the note at the position defined in chordSlot. 
     * @param chordSlot The slot at which to check the note and maybe add the chord.
     */
    public void maybeSetChord(int chordChoice, int chordSlot) {
        Random random = new Random();
        if(random.nextInt(2) == 1) {
            setChordFromNote(chordSlot, chordChoice);
        } else {
            Chord chord = score.getChordProg().getChord(chordSlot);
            if(chord != null) {
                score.getChordProg().delUnit(chordSlot);
            }
        }
    }


    /**
     * Takes a given chord name and turns it into a dominant chord with a random extension.
     * @param chord The name of the chord to be manipulated.
     * @return The new extended chord name.
     */
    public String random7thExtension(String chord) {
        String root = getRoot(chord).toUpperCase();
        Random random = new Random();
        int extChoice = random.nextInt(4);
        String extension = "";
        switch(extChoice) {
            case 0:
                extension = "7";
                break;
            case 1:
                extension = "9";
                break;
            case 2:
                extension = "11";
                break;
            case 3:
                extension = "13";
                break;
        }
        return root + extension;
    }


    /**
     * Takes a given chord name and turns it into a dominant chord with a random alteration.
     * @param chord The name of the chord to be manipulated.
     * @return The new altered chord name.
     */
    public String random7thAlteration(String chord) {
        String root = getRoot(chord).toUpperCase();
        Random random = new Random();
        int altChoice = random.nextInt(4);
        String alteration = "";
        switch(altChoice) {
            case 0:
                alteration = "7#9";
                break;
            case 1:
                alteration = "7b9";
                break;
            case 2:
                alteration = "7#5";
                break;
            case 3:
                alteration = "7b5";
                break;
        }
        return root + alteration;
    }
    

    /**
     * Takes a given chord name and adds a random extension.
     * @param chord The name of the chord to be manipulated.
     * @return The new extended chord name.
     */
    public String randomExtension(String chord) {
        Random random = new Random();
        int extChoice = random.nextInt(5);
        
        String[] triads = generateRootTriads(getNotesInKey());
        
        if(chord.equals(triads[6])) {
            if(chord.length() > 1 && (chord.charAt(1) == '#' || chord.charAt(1) == 'b')) {
                return chord.substring(0, 2) + "m7b5";
            } else {
                return chord.substring(0, 1) + "m7b5";
            }
        }
        if(extChoice == 1) {
            // major 7
            if(chord.equals(triads[0]) || chord.equals(triads[3])) {
                return chord + "M7";
            }
            // minor 7 or dominant 7
            if(chord.equals(triads[1]) || chord.equals(triads[2]) || chord.equals(triads[5]) || chord.equals(triads[4])) {
                return chord + "7";
            }
        }
        if(extChoice == 2) {
            // major 9
            if(chord.equals(triads[0]) || chord.equals(triads[3])) {
                return chord + "M9";
            }
            // minor 9 or dominant 9
            if(chord.equals(triads[1]) || chord.equals(triads[5]) || chord.equals(triads[4])) {
                return chord + "9";
            }

        }
        if(extChoice == 3) {
            // major 11
            if(chord.equals(triads[0])) {
                return chord + "M7";
            }
            // minor 11 or dominant 11
            if(chord.equals(triads[1]) || chord.equals(triads[2]) || chord.equals(triads[5]) || chord.equals(triads[4])) {
                return chord + "11";
            }
        }
        if(extChoice == 4) {
            // major 13
            if(chord.equals(triads[0]) || chord.equals(triads[3])) {
                return chord + "M13";
            }
            // minor 13 or dominant 13
            if(chord.equals(triads[1]) || chord.equals(triads[4])) {
                return chord + "13";
            }
        }
        return chord;
    }


    /**
     * Sets chord V with an added random alteration in the given slot.
     * @param slot The slot to set the chord.
     */
    public void setAlteredChord(int slot, int chordNumber) {
        if(isLocked(slot)) return;
        String chord = generateRootTriads(getNotesInKey())[chordNumber - 1];
        score.getChordProg().setChord(slot, new Chord(random7thAlteration(chord)));
    }


    /**
     * Gets the tritone substitution of the chord at the given source slot and puts it in the dest slot.
     * @param sourceSlot The slot with the chord to base the tritone substitution on.
     * @param destSlot The destination for the tritone substitution chord.
     */
    // public void setTritoneSub(int sourceSlot, int destSlot) {
    //     String currentNote = getNoteNameAtSlot(sourceSlot);
    //     // Case 1: current note is not in key
    //     if(!inKey(currentNote)) return;
    //     // Case 2: current note is in key, and should have a tritone substitution set from it
    //     score.getChordProg().setChord(destSlot, new Chord(random7thExtension(keyChords.get(currentNote)[1])));
    // }
    public void setTritoneSub(int sourceSlot, int destSlot) {
        if(isLocked(destSlot)) return;
        Chord sourceChord = score.getChordProg().getChord(sourceSlot);
        if(sourceChord == null) return;

        String sourceChordName = sourceChord.getName();
        String sourceChordRoot = getRoot(sourceChordName);

        String[] notesInKey = getNotesInKey();

        int i = 0;
        int indexInKey = 0;
        boolean found = false;
        while(!found || i < notesInKey.length) {
            if(notesInKey[i].equals(sourceChordRoot)) {
                indexInKey = i;
                found = true;
            }
            i++;
        }
        if(!found) return;

        String[] tritoneSubs = generateTritoneSubs();
        String destChordName = random7thExtension(tritoneSubs[indexInKey]);

        score.getChordProg().setChord(destSlot, new Chord(destChordName));
    }





    /**
     * Gets the harmonic substitutions for the diatonic chord based on the note at the source slot 
     * and sets one at random in the destination slot.
     * @param sourceSlot The slot that holds the note from which to source the chord choices.
     * @param destSlot The slot to put the new chord.
     */
    public Chord setHarmonicSub(int sourceSlot, int destSlot) {
        String currentNote = getNoteNameAtSlot(sourceSlot);
        // Case 1: current note is not in key
        if(!inKey(currentNote)) return null;
        Chord chord = null;
        String[] notesInKey = getNotesInKey();
        Random random = new Random();
        // Case 2: current note is in key and its diatonic chord has 2 harmonic substitutions
        if(currentNote.equals(notesInKey[0]) ||
        currentNote.equals(notesInKey[2]) ||
        currentNote.equals(notesInKey[5])) {
            int choice = random.nextInt(3);
            if(choice == 0) {
                chord = new Chord(randomExtension(keyChords.get(currentNote)[0]));
                score.getChordProg().setChord(destSlot, chord);
            } else if(choice == 1) {
                chord = new Chord(randomExtension(keyChords.get(currentNote)[2]));
                score.getChordProg().setChord(destSlot, chord);
            } else {
                chord = new Chord(randomExtension(keyChords.get(currentNote)[3]));
                score.getChordProg().setChord(destSlot, chord);
            }
        } else {
            // Case 3: current note is in key and its diatonic chord has 1 harmonic substitution
            int choice = random.nextInt(2);
            if(choice == 0) {
                chord = new Chord(randomExtension(keyChords.get(currentNote)[0]));
                score.getChordProg().setChord(destSlot, chord);
            } else {
                chord = new Chord(randomExtension(keyChords.get(currentNote)[2]));
                score.getChordProg().setChord(destSlot, chord);
            }
        }
        return chord;
    }


    /**
     * Looks at the note in the given slot, gets the harmonic substitutions for the diatonic chord based on 
     * that note and picks one at random to go in the slot.
     * @param slot The slot to place the chord.
     */
    public Chord setHarmonicSub(int slot) {
        return setHarmonicSub(slot, slot);
    }


    /**
     * Looks at the given note name and either uses that note to generate a chord in the given chordSlot
     * or deletes the chord that is present in the given chordSlot.
     * @param noteToSetChordFrom The note to look up in the chordSets array, in order to find a chord to add.
     * @param chordChoice The index of the chord to be placed, taken from the chordSets 
     * array for the note defined in noteToSetChordFrom. 
     * @param chordSlot The slot at which to check the note and maybe add the chord.
     */
    public void maybeSetChord(String noteToSetChordFrom, int chordChoice, int chordSlot) {
        Random random = new Random();
        if(random.nextInt(2) == 1) {
            setChordFromNote(noteToSetChordFrom, chordChoice, chordSlot);
        } else {
            Chord chord = score.getChordProg().getChord(chordSlot);
            if(chord != null) {
                score.getChordProg().delUnit(chordSlot);
            }
        }
    }


    /** 
     * Checks whether the given note is in the current key.
     * @param note The note to check.
     * @return boolean true if in key, false otherwise.
     */
    public boolean inKey(String note) {
        List<String> notesInKey = Arrays.asList(getNotesInKey());
        return notesInKey.contains(note);
    }


    /**
     * Returns the name of the note at a given slot number in the target MelodyPart. 
     * Useful for finding specific notes in implementChordChoice().
     * @param slot The slot to search at.
     * @return The name of the note at the given slot.
     */
    public String getNoteNameAtSlot(int slot) {
        // Get the note at the beginning of the bar or 
        // move to the next bar if there is no note.
        Note targetNote = targetPart.getNote(slot);
        if(targetNote == null) return null;
        // Get the name of the note at the beginning of the bar
        return targetNote.getPitchClassName();
    }


    /**
     * Finds the note at a given slot, selects the first chord from the keyChords 
     * choices for that note and places it above the note at that slot.
     * @param slot The slot number at which to analyse the note and place the chord.
     */
    public void setRootChord(int slot) {
        String targetNoteName = getNoteNameAtSlot(slot);
        if(keyChords.containsKey(targetNoteName) && inKey(targetNoteName)) {
            // Get the chords that match to this note
            String[] chordChoices = keyChords.get(targetNoteName);
            // Select root chord within the key for this note.
            String chord = chordChoices[0];
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(slot, new Chord(randomExtension(chord)));
        }
    }

    
    /**
     * Finds the note at the given slot and sets its corresponding diminished chord 
     * in that position on the score. Does nothing if the note is in key.
     * @param slot The slot to add the chord to.
     */
    public void setDiminishedChord(int slot) {
        String targetNoteName = getNoteNameAtSlot(slot);
        if(keyChords.containsKey(targetNoteName) && !inKey(targetNoteName)) {
            String[] chordChoices = keyChords.get(targetNoteName);
            String dimChord = chordChoices[0];
            score.getChordProg().setChord(slot, new Chord(dimChord));
        }
    }


    /** 
     * Looks at the note at the given slotToPlaceChord index and places the chord from that note's 
     * choices that is found at the given chordChoiceIndex.
     * @param slotToPlaceChord The slot to check the note from and place chord at.
     * @param chordChoiceIndex The index of the required chord in the chord array corresponding to 
     * the note in the slot.
     */
    public void setChordFromNote(int slotToPlaceChord, int chordChoiceIndex) {
        String targetNoteName = getNoteNameAtSlot(slotToPlaceChord);
        if(keyChords.containsKey(targetNoteName)) {
            // Get the chords that match to this note
            String[] chordChoices = keyChords.get(targetNoteName);
            // Select root chord within the key for this note.
            String chordName = chordChoices[chordChoiceIndex];
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(slotToPlaceChord, new Chord(chordName));
        }
    }


    /**
     * Looks at the note name given in the noteToSetChordFrom string, gets the chord from its corresponding 
     * chord array at the given chordChoiceIndex and places it in the given slotToPlaceChord.
     * @param noteToSetChordFrom The note to use to choose a chord to set.
     * @param chordChoiceIndex The index of the required chord from note's corresponding chord array.
     * @param slotToPlaceChord The slot to place the chord in.
     */
    public void setChordFromNote(String noteToSetChordFrom, int chordChoiceIndex, int slotToPlaceChord) {
        if(keyChords.containsKey(noteToSetChordFrom)) {
            // Get the chords that match to this note
            String[] chordChoices = keyChords.get(noteToSetChordFrom);
            // Select required chord choice within the options for this note.
            String chordName = chordChoices[chordChoiceIndex];
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(slotToPlaceChord, new Chord(chordName));
        }
    }

    /**
     * Finds the note at a given slot, selects a chord at random from the list 
     * of suitable choices for that note, then places the chord in that slot.
     * @param slot The slot number at which to analyse the note and place the chord.
     */
    public void setRandomChordFromNote(int slot) {
        String targetNoteName = getNoteNameAtSlot(slot);
        if(keyChords.containsKey(targetNoteName)) {
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(slot, getRandomChord(targetNoteName));
        }
    }

    
    /** 
     * Looks at the note in the noteSetFrom slot index, selects a chord at random from it's 
     * corresponding chord choices and places that chord at the chordSetTo slot index.
     * @param sourceSlot The slot index of the note to use to select a chord.
     * @param destSlot The slot index to place the chord.
     */
    public void setRandomChordFromNote(int sourceSlot, int destSlot) {
        String targetNoteName = getNoteNameAtSlot(sourceSlot);
        if(keyChords.containsKey(targetNoteName)) {
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(destSlot, getRandomChord(targetNoteName));
        }
    }


    /**
     * Sets the chord based on the most frequent note between the given chord slot 
     * and the following chord slot. If multiple notes tie as most frequent, sets 
     * the one that occurs earliest.
     * @param slot The slot at which to check for the most common note.
     */
    public void setRootChordFromCommonNote(int slot) {
        // a place to store the notes in the bar
        ArrayList<String> notesInBar = new ArrayList<>();
        // For each note in that bar...
        for(int i = slot; i < slot + chordDuration; i++) {
            // get the note
            String noteName = getNoteNameAtSlot(i);
            // move on if not a diatonic note
            if(!keyChords.containsKey(noteName)) continue;
            // add to notesInBar container
            notesInBar.add(noteName);
        }
        String mostFrequentNote = null;
        int freq = 0;
        for(String n : notesInBar) {
            if(Collections.frequency(notesInBar, n) > freq) {
                mostFrequentNote = n;
                freq = Collections.frequency(notesInBar, n);
            }
        }
        String[] chordChoices = keyChords.get(mostFrequentNote);
        if(chordChoices != null) {
            String chord = chordChoices[0];
            // Set the chord for this position on the score as decided in logic above
            if(chord != null) {
            score.getChordProg().setChord(slot, new Chord(chord));
            }
        }
    }


    /**
     * Checks whether the given note is a standard chord tone of the given chord, up to the 7th degree.
     * @param note The name of the note to check.
     * @param chord The name of the chord to look for the note in.
     * @return true if note is a chord tone of the chord, false otherwise.
     */
    public boolean isChordToneOf(String note, String chord) {
        // Get the 7th arpeggio notes of the chord
        String[] arp = get7thArpeggio(chord);
        if(arp == null) return false;
        // Check whether the current note is a note of the 7th arpeggio for the chord
        int i = 0;
        boolean inArp = false;
        while(!inArp && i < arp.length) {
            if(note.equals(arp[i])) {
                inArp = true;
            }
            i++;
        }
        return inArp;
    }


    /**
     * Returns an array containing the names of the notes which occur most frequently between the given slot 
     * and the next slot.
     * @param slot
     * @return
     */
    public ArrayList<String> mostFrequentNotes(int slot) {
        MelodyPart part = score.getPart(0);
        ArrayList<String> notes = new ArrayList<>();
        
        for(int i = slot; i < slot + chordDuration; i++) {
            Note note = part.getNote(i);
            if(note == null) continue;
            String noteName = note.getPitchClassName();
            if(!keyChords.containsKey(noteName)) continue;

        }

        return notes;
    }


    public ArrayList<String> getNotesInSlot(int slot) {
        ArrayList<String> notesInSlot = new ArrayList<>();
        MelodyPart part = score.getPart(0);
        for(int i = slot; i < slot + chordDuration; i++) {
            Note note = part.getNote(i);
            if(note == null) continue;
            String noteName = note.getPitchClassName();
            if(!keyChords.containsKey(noteName)) continue;
            notesInSlot.add(noteName);
        }
        return notesInSlot;
    }


    public ArrayList<String> getBestChordMatches(int slot) {
        // set up a hash map for notes of the key with int value attached to each for frequency of notes within their diatonic arp.
        HashMap<String, Integer> rootNotes = new HashMap<>();
        for(String root : getNotesInKey()) {
            rootNotes.put(root, 0);
        }
        // for each note in the slot:
        for(String note : getNotesInSlot(slot)) {
            // check note against each key in the keySet for the hash map, add 1 to each which includes the note within its arp.
            for(String root : rootNotes.keySet()) {
                if(isChordToneOf(note, root)) {
                    if(rootNotes.get(root) == 0) {
                        rootNotes.put(root, 1);
                    } else {
                        rootNotes.put(root, rootNotes.get(root) + 1);
                    }
                }
            }
        }
        // save the highest int value in the hash map values
        int highestFrequency = Collections.max(rootNotes.values());
        // create a new array list for the notes with the highest frequency
        ArrayList<String> bestMatches = new ArrayList<>();
        // for each of the notes in the hash map keySet:
        for(String root : rootNotes.keySet()) {
            // if that note's value == the highest int:
            if(rootNotes.get(root) == highestFrequency) {
                // add it to the new array list
                bestMatches.add(root);
            }
        }
        return bestMatches;
    }


    public Chord setChordMatch(int slot) {
        ArrayList<String> matches = getBestChordMatches(slot);
        Random random = new Random();
        int choice = random.nextInt(matches.size());
        Chord newChord = new Chord(randomExtension(keyChords.get(matches.get(choice))[0]));
        score.getChordProg().setChord(slot, newChord);
        return newChord;
    }


    public void setDifferentChordTo(int sourceSlot, int destSlot) {
        // The note to set chord from
        Note destNote = score.getPart(0).getNote(destSlot);
        if(destNote == null) return;
        // The chord to avoid setting
        Chord avoidChord = score.getChordProg().getChord(sourceSlot);
        if(avoidChord == null) {
            setChordMatch(destSlot);
            return;
        }
        // The root note for the avoid chord
        String avoidRoot = getRoot(avoidChord.getName());
        // Establish a chord that will be set
        Chord destChord = null;
        // while the chord is either null or has the same root as the avoid chord...
        while(destChord == null || getRoot(destChord.getName()).equals(avoidRoot)) {
            // System.out.println(getRoot(destChord.getName()).equals(avoidRoot));
            // System.out.println(avoidRoot);
            // System.out.println(getRoot(destChord.getName()));
            destChord = setHarmonicSub(destSlot);
            if(destChord == null) {
                destChord = setChordMatch(destSlot);
            }
        }
    }


    public boolean isTritoneSub(int slot) {
        // Get the chord to check
        Chord chord = score.getChordProg().getChord(slot);
        if(chord == null) return false;
        // Get the note from which to source the tritone sub
        Note note = score.getPart(0).getNote(slot - chordDuration);
        if(note == null) return false;
        // Get the String representations of the chord and note
        String chordName = chord.getName();
        if(!isDominant(chordName)) return false;
        String noteName = note.getPitchClassName();
        // Get the root note for the chord
        String chordRoot = getRoot(chordName);
        // Get the root note for the tritone substitution chord associated with the note
        String tritoneRoot = getRoot(keyChords.get(noteName)[1]);
        // check whether the tritoneRoot is the same as the root note for the chord and return the result
        return chordRoot.equals(tritoneRoot);    
    }


    public boolean isDominant(String chord) {   
        if(chord.length() == 1) {
            if(!getRoot(chord).equals(getNotesInKey()[4])) return false;
        } 
        if(chord.length() == 2) {
            // String[] list = new String[{7, 9, 11, 13}]
            ArrayList<String> extensions = new ArrayList<>(Arrays.asList("7", "9"));
            if(getRoot(chord).length() == 1) {
                if(!extensions.contains(chord.substring(1, 2))) return false;
            }
            if(getRoot(chord).length() > 1) {
                if(!getRoot(chord).equals(getNotesInKey()[4])) return false;
            }
        }
        if(chord.length() > 2) {
            ArrayList<String> extensions = new ArrayList<>(Arrays.asList("7", "9", "1"));
            if(getRoot(chord).length() == 1) {
                if(!extensions.contains(chord.substring(1, 2))) return false;
            }
            if(getRoot(chord).length() == 2) {
                if(!extensions.contains(chord.substring(2, 3))) return false;
            }
        }

        return true;
    }


    /**
     * Returns the most frequently occurring note between the current slot and the next slot.
     * @param slot The slot to start checking at.
     * @return The name of the most frequently occurring note between the given slot and the next slot.
     */
    public String mostFrequentNote(int slot) {
        // a place to store the notes in the slot
        ArrayList<String> notesInSlot = new ArrayList<>();
        // For each note in that slot...
        for(int i = slot; i < slot + chordDuration; i++) {
            // get the note
            String noteName = getNoteNameAtSlot(i);
            // move on if not an included note
            if(!keyChords.containsKey(noteName)) continue;
            // add to notesInBar container
            notesInSlot.add(noteName);
        }
        String mostFrequentNote = null;
        int freq = 0;
        for(String n : notesInSlot) {
            if(Collections.frequency(notesInSlot, n) > freq) {
                mostFrequentNote = n;
                freq = Collections.frequency(notesInSlot, n);
            }
        }
        return mostFrequentNote;
    }


    public boolean isChordNumber(int slot, int chordNumber) {
        if(chordNumber < 1 || chordNumber > 7) return false;
        Chord ourChord = score.getChordProg().getChord(slot);
        if(ourChord == null) return false;
        String ourChordName = ourChord.getName();
        String ourChordRoot = getRoot(ourChordName);
        String targetRoot = getNotesInKey()[chordNumber - 1];

        return ourChordRoot.equals(targetRoot);
    }


    public void setDiatonicChord(int slot, int chordNumber) {
        if(isLocked(slot)) return;
        if(chordNumber < 1 || chordNumber > 7) {
            System.out.println("chord number out of range");
        }
        
        String[] notesInKey = getNotesInKey();
        String root = notesInKey[chordNumber - 1];
        String triad = keyChords.get(root)[0];
        String chord = randomExtension(triad);

        score.getChordProg().setChord(slot, new Chord(chord));
    }


    public boolean isLastBar(int slot) {
        return slot + score.getSlotsPerMeasure() >= score.getPart(0).size();
    }


    /**
     * Checks whether the majority of the notes are in key between a given slot and the next slot.
     * @param slot The slot to start checking at.
     * @return true if the majority of the notes between the given slot and the next are in key, false otherwise.
     */
    public boolean majorityInKey(int slot) {
        ArrayList<String> inKeyNotes = new ArrayList<>();
        ArrayList<String> outsideNotes = new ArrayList<>();
        
        // For each note in that slot...
        for(int i = slot; i < slot + chordDuration; i++) {
            // get the note
            String noteName = getNoteNameAtSlot(i);
            // move on if not an included note
            if(!keyChords.containsKey(noteName)) continue;
            // add to the right container
            if(inKey(noteName)) {
                inKeyNotes.add(noteName);
            } else {
                outsideNotes.add(noteName);
            }
        }
        return inKeyNotes.size() > outsideNotes.size();
    }


    /** 
     * Looks at the current key signature registered in Impro-Visor's preferences pane and 
     * returns an array of the notes in that key.
     * @return String[] The array of notes in the current key.
     */
    protected String[] getNotesInKey() {
        int keySig = score.getKeySignature();
        if(keySig >= 0) {
            return sharpKeys[keySig];
        } else {
            int absoluteKey = Math.abs(keySig);
            return flatKeys[absoluteKey];
        }
    }


    /**
     * Checks whether there is a chord at the given slot.
     * @param slot The slot to check for a chord.
     * @return true if given slot contains a chord, false otherwise.
     */
    public boolean hasChord(int slot) {
        if(slot < 0 || slot > score.getChordProg().size() - 1) return false;
        return score.getChordProg().getChord(slot) != null;
    }


    /**
     * Checks whether the given slots contain any variation on chord V and chord I in the key, respectively.
     * @param slotV The slot to check for chord V.
     * @param slotI The slot to check for chord I.
     * @return true if the two slots contain chord V and I respectively, false otherwise.
     */
    public boolean isChordVthenI(int slotV, int slotI) {
        // Get the chord contained at slotV
        Chord checkV = score.getChordProg().getChord(slotV);
        // if there is no chord, or the chord is not a variation of chord V, 
        // we do not have chord V then I.
        if(checkV == null || !isChordV(checkV)) return false;
        // Get the chord contained at slotI
        Chord checkI = score.getChordProg().getChord(slotI);
        // if there is no chord, or the chord is not a variation of chord I, 
        // we do not have chord V then I.
        if(checkI == null || !isChordI(checkI)) return false;
        // Otherwise, we have chord V followed by chord I.
        return true;
    }


    /**
     * Checks whether the given chord is some variation on chord V in the current key.
     * @param chord The chord to check.
     * @return true if the given chord is some variation on chord V, false otherwise.
     */
    public boolean isChordV(Chord chord) {
        
        if(chord == null) return false;

        String chordRoot = getRoot(chord.getName());
        String vRoot = getNotesInKey()[4];

        if(!chordRoot.equals(vRoot)) return false;

        return true;

    }


    /**
     * Checks whether the given chord is some variation on chord I in the current key.
     * @param chord The chord to check.
     * @return true if the given chord is some variation on chord I, false otherwise.
     */
    public boolean isChordI(Chord chord) {
        
        if(chord == null) return false;

        String chordRoot = getRoot(chord.getName());
        String vRoot = getNotesInKey()[0];

        if(!chordRoot.equals(vRoot)) return false;

        return true;

    }


    /* Private methods used by this superclass only */

    /**
     * Takes a major scale and turns each of its notes into corresponding triad chords.
     * @param scale The scale used as a set of root notes from which to build triads.
     * @return an array of strings representing the generated triad chord names.
     */
    private String[] generateRootTriads(String[] scale) {
        String[] chords = new String[scale.length];
        for(int i = 0; i < scale.length; i++) {
            if(i == 1 || i == 2 || i == 5) {
                chords[i] = scale[i].toUpperCase() + "m";
            } else if(i == 6) {
                chords[i] = scale[i].toUpperCase() + "mb5";
            } else {
                chords[i] = scale[i].toUpperCase();
            } 
            if(chords[i].length() == 1) continue;
            if(chords[i].charAt(1) == 'B') {
                char[] nameArr = chords[i].toCharArray();
                nameArr[1] = 'b';
                chords[i] = "";
                for(char c : nameArr) {
                    chords[i] += c;
                }
            }
        }
        return chords;
    }
    

    /** 
     * Creates a HashMap of "accidental" notes and their enharmonic equivalents.
     * @return HashMap<String, String> with the sharp notes as keys and their flat equivalents as values.
     */
    private HashMap<String, String> generateEquivalents() {
        HashMap<String, String> equivalents = new HashMap<>();
        equivalents.put("c#", "db");
        equivalents.put("d#", "eb");
        equivalents.put("e#", "f");
        equivalents.put("f#", "gb");
        equivalents.put("g#", "ab");
        equivalents.put("a#", "bb");
        equivalents.put("b#", "c");
        return equivalents;
    }

    
    /** 
     * Gets the positions of the major scale indices within an array representing the chromatic scale.
     * @return int[] The indices at which major scale notes would fall.
     */
    private int[] getMajorScaleIndices() {
        return new int[] {0, 2, 4, 5, 7, 9, 11};
    } 

    
    /** 
     * Adds a specified chord name to a specified index in a specified set in the chordsets array.
     * @param chordSets The chordSets array to add to.
     * @param setIndex The index of the set of chords to add to.
     * @param chordIndex The index to add the chord within the set.
     * @param chordToAdd The chord to be added.
     */
    private void addToChordSet(String[][] chordSets, int setIndex, int chordIndex, String chordToAdd) {
        // If the array at this index is null...
        if(chordSets[setIndex] == null) {
            chordSets[setIndex] = new String[1];
        }
        // If the index we are trying to fill is out of bounds...
        else if (chordSets[setIndex].length <= chordIndex) {
            String[] newArr = new String[chordIndex + 1];
            for(int i = 0; i < chordSets[setIndex].length; i++) {
                newArr[i] = chordSets[setIndex][i];
            }
            chordSets[setIndex] = newArr;
        }
        // Add the new element to the array
        chordSets[setIndex][chordIndex] = chordToAdd;
    }


    /** 
     * Looks at the given note name and retrieves a chord at random from its corresponding chord choices.
     * @param noteName The name of the note to use to select a chord.
     * @return Chord The chord that is selected randomly from the available choices for that note.
     */
    private Chord getRandomChord(String noteName) {
        // Get the chords that match to this note
        String[] chordChoices = keyChords.get(noteName);
        // Set up a new Random object.
        Random random = new Random();
        // Randomly select between the chords that map to this note.
        String chordName = chordChoices[random.nextInt(chordChoices.length)];
        // Return a new Chord with this name
        return new Chord(chordName);
    }
}
