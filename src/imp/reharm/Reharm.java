package imp.reharm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    int chordDuration;

    /** An array containing String representations of the notes of the C major scale. */
    String[] cScale         = {"c", "d", "e", "f", "g", "a", "b"};
    /** An array containing String representations of the notes of the G major scale. */
    String[] gScale         = {"g", "a", "b", "c", "d", "e", "f#"};
    /** An array containing String representations of the notes of the D major scale. */
    String[] dScale         = {"d", "e", "f#", "g", "a", "b", "c#"};
    /** An array containing String representations of the notes of the A major scale. */
    String[] aScale         = {"a", "b", "c#", "d", "e", "f#", "g#"};
    /** An array containing String representations of the notes of the E major scale. */
    String[] eScale         = {"e", "f#", "g#", "a", "b", "c#", "d#"};
    /** An array containing String representations of the notes of the B major scale. */
    String[] bScale         = {"b", "c#", "d#", "e", "f#", "g#", "a#"};
    /** An array containing String representations of the notes of the F# major scale. */
    String[] fSharpScale    = {"f#", "g#", "a#", "b", "c#", "d#", "e#"};
    /** An array containing String representations of the notes of the F major scale. */
    String[] fScale         = {"f", "g", "a", "bb", "c", "d", "e"};
    /** An array containing String representations of the notes of the Bb major scale. */
    String[] bFlatScale     = {"bb", "c", "d", "eb", "f", "g", "a"};
    /** An array containing String representations of the notes of the Eb major scale. */
    String[] eFlatScale     = {"eb", "f", "g", "ab", "bb", "c", "d"};
    /** An array containing String representations of the notes of the Ab major scale. */
    String[] aFlatScale     = {"ab", "bb", "c", "db", "eb", "f", "g"};
    /** An array containing String representations of the notes of the Db major scale. */
    String[] dFlatScale     = {"db", "eb", "f", "gb", "ab", "bb", "c"};
    /** An array containing String representations of the notes of the Gb major scale. */
    String[] gFlatScale     = {"gb", "ab", "bb", "cb", "db", "eb", "f"};

    /**
     * An array of major scales that correspond to the sharp keys. The index of the scale 
     * in the array indicates the number of sharps that key has (i.e. G major has 1 sharp
     * so is placed at index 1).
     */
    String[][] sharpKeys    = {cScale, gScale, dScale, aScale, eScale, bScale, fSharpScale};
    /**
     * An array of major scales that correspond to the flat keys. The index of the scale 
     * in the array indicates the number of flats that key has (i.e. Gb major has 6 flats
     * so is placed at index 6).
     */
    String[][] flatKeys     = {cScale, fScale, bFlatScale, eFlatScale, aFlatScale, dFlatScale, gFlatScale};

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
    public abstract void setChordDuration();
    /**
     * This method is run at every point a new chord is to be placed. It details how the 
     * chord will be picked, based on the keyChords HashMap of notes and suitable chords.
     * @param chordSlot The slot in which to put a chord. We iterate through the chordSlots 
     * and implement this method for each.
     */
    public abstract void implementChordChoice(int chordSlot);

    /**
     * Takes a major scale and turns each of its notes into corresponding triad chords.
     */
    protected String[] generateRootTriads(String[] scale) {
        String[] chords = new String[scale.length];
        for(int i = 0; i < scale.length; i++) {
            if(i == 1 || i == 2 || i == 5) {
                chords[i] = scale[i].toUpperCase() + "m";
            } else if(i == 6) {
                chords[i] = scale[i].toUpperCase() + "m7b5";
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
     * Sets the keyChords HashMap so that the elements in the notes array become the keys
     * and the arrays inside the chordSets 2-dimensional array become the values. Keys 
     * and values are paired together by matching their indexes in their respective arrays.
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

    HashMap<String, String> generateEquivalents() {
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

    public int[] getMajorScaleIndices() {
        return new int[] {0, 2, 4, 5, 7, 9, 11};
    } 

    public void populateDiatonicChordSets(String[][] destination, int indexToFill, String[] source) {
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
            addToChordSet(destination, destIndex, indexToFill, source, sourceIndex);
            sourceIndex++;
            }
        }          
    }

    public void populateOutsideChordSets(String[][] destination, int indexToFill, String[] source) {
        int[] majorScaleIndices = getMajorScaleIndices();

        for(int destIndex = 0, sourceIndex = 0; destIndex < destination.length; destIndex++) {
            boolean inKey = false;
            for(int i = 0; i < majorScaleIndices.length; i++) {
                if(destIndex == majorScaleIndices[i]) {
                    inKey = true;
                }
            }
            if(!inKey) {
            addToChordSet(destination, destIndex, indexToFill, source, sourceIndex);
            sourceIndex++;
            }
        }
    }

    private void addToChordSet(String[][] chordSets, int setIndex, int chordIndex, String[] sourceChords, int sourceIndex) {
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
        chordSets[setIndex][chordIndex] = sourceChords[sourceIndex];
    }

    /**
     * Sets how many slots will pass before thinking about adding another chord.
     * Use inside your implementation of setChordDuration() in subclasses.
     * @param slots the number of slots to wait before trying to add another chord.
     */
    public void setChordDuration(int slots) {
        chordDuration = slots;
    }

    /**
     * Generates a set of potential chords for each note in the current key signature of the score.
     */
    public void generateChords() {
        String[] notesInKey = getNotesInKey();
        setKeyChords(notesInKey, generateSubstitutions(generateRootTriads(notesInKey)));
    }

    public String[] getNotesInKey() {
        int keySig = score.getKeySignature();
        if(keySig >= 0) {
            return sharpKeys[keySig];
        } else {
            int absoluteKey = Math.abs(keySig);
            return flatKeys[absoluteKey];
        }
    }

    /**
     * Returns the name of the note at a given slot number in the target MelodyPart. 
     * Useful for finding specific notes in implementChordChoice().
     * @param slot The slot to search at.
     * @return
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
     * @param targetPart The MelodyPart to add the chord to.
     * @param slot The slot number at which to analyse the note and place the chord.
     */
    public void setRootChordFromNote(int slot) {
        String targetNoteName = getNoteNameAtSlot(slot);
        if(keyChords.containsKey(targetNoteName)) {
            // Get the chords that match to this note
            String[] chordChoices = keyChords.get(targetNoteName);
            // Select root chord within the key for this note.
            String chord = chordChoices[0];
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(slot, new Chord(chord));
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

    public void setRandomChordFromNote(int noteSetFrom, int chordSetTo) {
        String targetNoteName = getNoteNameAtSlot(noteSetFrom);
        if(keyChords.containsKey(targetNoteName)) {
            // Set the chord for this position on the score as decided in logic above
            score.getChordProg().setChord(chordSetTo, getRandomChord(targetNoteName));
        }
    }

    public Chord getRandomChord(String noteName) {
        // Get the chords that match to this note
        String[] chordChoices = keyChords.get(noteName);
        // Set up a new Random object.
        Random random = new Random();
        // Randomly select between the chords that map to this note.
        String chordName = chordChoices[random.nextInt(chordChoices.length)];
        // Return a new Chord with this name
        return new Chord(chordName);
    }

    /**
     * Sets the chord based on the most frequent note during the duration of the chord slot.
     * If multiple notes tie as most frequent, sets the one that occurs earliest.
     * @param targetPart
     * @param slot
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
     * Goes through each bar in the score, implementing chords as defined in generateSubstitutions() 
     * and implementChordChoice().
     */
    public void execute() {
        // Select the MelodyPart for analysis
        targetPart = score.getPart(0);
        if(targetPart == null) return;
        setChordDuration();
        // For each bar...
        for(int chordSlot = 0; chordSlot < targetPart.size() ; chordSlot = chordSlot + chordDuration) {
            implementChordChoice(chordSlot);
        }
    }

}
