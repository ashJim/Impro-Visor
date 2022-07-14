package imp.reharm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import imp.data.Chord;
import imp.data.MelodyPart;
import imp.data.Note;
import imp.data.Score;

public class BasicReharm extends Reharm {

    public BasicReharm(Score score) {
        super(score);
    }

    @Override
    public void execute() {
        generateChords(score.getKeySignature());
        // Select the MelodyPart for analysis
        MelodyPart targetPart = score.getPart(0);
        if(targetPart == null) return;
        // For each bar...
        for(int i = 0; i < targetPart.size() ; i = i + score.getSlotsPerMeasure()) {
            // a place to store the notes in the bar
            ArrayList<String> notesInBar = new ArrayList<>();
            // For each note in that bar...
            for(int j = i; j < i + score.getSlotsPerMeasure(); j++) {
                // get the note
                Note note = targetPart.getNote(j);
                // move on if no note
                if(note == null) continue;
                // get the note's name
                String noteName = note.getPitchClassName();
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
                score.getChordProg().setChord(i, new Chord(chord));
                }
            }
        }
    }
    


    @Override
    protected HashMap<String, String[]> generateCChords() {
        HashMap<String, String[]> cChords = new HashMap<>();

        String[] notes = {"c", "d", "e", "f", "g", "a", "b"};
        String[][] chordSets = {{"C"}, {"Dm"}, {"Em"}, {"F"}, {"G"}, {"Am"}, {"Bm7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            cChords.put(notes[i], chordSets[i]);
        }

        return cChords;
    }

    @Override
    protected HashMap<String, String[]> generateGChords() {
        HashMap<String, String[]> gChords = new HashMap<>();

        String[] notes = {"g", "a", "b", "c", "d", "e", "f#"};
        String[][] chordSets = {{"G"}, {"Am"}, {"Bm"}, {"C"}, {"D"}, {"Em"}, {"F#m7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            gChords.put(notes[i], chordSets[i]);
        }

        return gChords;
    }

    @Override
    protected HashMap<String, String[]> generateDChords() {
        HashMap<String, String[]> dChords = new HashMap<>();

        String[] notes = {"d", "e", "f#", "g", "a", "b", "c#"};
        String[][] chordSets = {{"D"}, {"Em"}, {"F#m"}, {"G"}, {"A"}, {"Bm"}, {"C#m7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            dChords.put(notes[i], chordSets[i]);
        }

        return dChords;
    }

    @Override
    protected HashMap<String, String[]> generateAChords() {
        HashMap<String, String[]> aChords = new HashMap<>();

        String[] notes = {"a", "b", "c#", "d", "e", "f#", "g#"};
        String[][] chordSets = {{"A"}, {"Bm"}, {"C#m"}, {"D"}, {"E"}, {"F#m"}, {"G#m7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            aChords.put(notes[i], chordSets[i]);
        }

        return aChords;
    }

    @Override
    protected HashMap<String, String[]> generateEChords() {
        HashMap<String, String[]> eChords = new HashMap<>();

        String[] notes = {"e", "f#", "g#", "a", "b", "c#", "d#"};
        String[][] chordSets = {{"E"}, {"F#m"}, {"G#m"}, {"A"}, {"B"}, {"C#m"}, {"D#m7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            eChords.put(notes[i], chordSets[i]);
        }

        return eChords;
    }

    @Override
    protected HashMap<String, String[]> generateBChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"b", "c#", "d#", "e", "f#", "g#", "a#"};
        String[][] chordSets = {{"B"}, {"C#m"}, {"D#m"}, {"E"}, {"F#"}, {"G#m"}, {"A#m7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateFSharpChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"f#", "g#", "a#", "b", "c#", "d#", "e#"};
        String[][] chordSets = {{"F#"}, {"G#m"}, {"A#m"}, {"B"}, {"C#"}, {"D#m"}, {"E#m7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateFChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"f", "g", "a", "bb", "c", "d", "e"};
        String[][] chordSets = {{"F"}, {"Gm"}, {"Am"}, {"Bb"}, {"C"}, {"Dm"}, {"Em7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateBFlatChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"bb", "c", "d", "eb", "f", "g", "a"};
        String[][] chordSets = {{"Bb"}, {"Cm"}, {"Dm"}, {"Eb"}, {"F"}, {"Gm"}, {"Am7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateEFlatChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"eb", "f", "g", "ab", "bb", "c", "d"};
        String[][] chordSets = {{"Eb"}, {"Fm"}, {"Gm"}, {"Ab"}, {"Bb"}, {"Cm"}, {"Dm7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateAFlatChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"ab", "bb", "c", "db", "eb", "f", "g"};
        String[][] chordSets = {{"Ab"}, {"Bbm"}, {"Cm"}, {"Db"}, {"Eb"}, {"Fm"}, {"Gm7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateDFlatChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"db", "eb", "f", "gb", "ab", "bb", "c"};
        String[][] chordSets = {{"Db"}, {"Ebm"}, {"Fm"}, {"Gb"}, {"Ab"}, {"Bbm"}, {"Cm7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }

    @Override
    protected HashMap<String, String[]> generateGFlatChords() {
        HashMap<String, String[]> noteChordMap = new HashMap<>();

        String[] notes = {"gb", "ab", "bb", "cb", "db", "eb", "f"};
        String[][] chordSets = {{"Gb"}, {"Abm"}, {"Bbm"}, {"Cb"}, {"Db"}, {"Ebm"}, {"Fm7b5"}};

        for(int i = 0; i < notes.length && i < chordSets.length; i++) {
            noteChordMap.put(notes[i], chordSets[i]);
        }

        return noteChordMap;
    }


}
