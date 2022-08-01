package imp.reharm;

import imp.data.Score;

public class MediumReharm extends Reharm {

    public MediumReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        String[][] chordSets = chromaticChordSets();
        initStandardSubstitutions(chordSets);
        return chordSets;
    }

    @Override
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure() / 2);
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        String currentNote = getNoteNameAtSlot(chordSlot);
        
        if(currentNote == null) {
            return;
        }

        if(isStartOfBar(chordSlot)) {
            if(inKey(currentNote)) {
                setRootChordFromNote(chordSlot);
            }
            if(!inKey(currentNote)) {
                setDiminishedChord(chordSlot);
            }
        }
        if(!isStartOfBar(chordSlot)) {
            if(inKey(currentNote)) {
                setHarmonicSub(chordSlot);
            }
            if(!inKey(currentNote)) {
                setDiminishedChord(chordSlot);
            }
        }
    }
    
}
