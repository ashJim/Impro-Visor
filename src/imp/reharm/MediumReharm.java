package imp.reharm;

import imp.data.Score;

public class MediumReharm extends Reharm {

    public MediumReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        String[][] chordSets = chromaticChordSets();

        

        return chordSets;
    }

    @Override
    public void setChordDuration() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        // TODO Auto-generated method stub
        
    }
    
}
