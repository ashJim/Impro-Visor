package test.reharm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import imp.data.Score;
import imp.reharm.BasicReharm;

public class ReharmTest {

    @Test
    public void eShouldBeInCMajor() {
        Score score = new Score();
        score.setKeySignature(0);
        BasicReharm reharm = new BasicReharm(score);
        assertTrue(reharm.isChordToneOf("e", "CM"));
    }

    @Test
    public void fShouldBeInDMinor() {
        Score score = new Score();
        score.setKeySignature(0);
        BasicReharm reharm = new BasicReharm(score);
        assertTrue(reharm.isChordToneOf("f", "Dm"));
    }

    @Test
    public void eShouldNotBeInCMinor() {
        Score score = new Score();
        score.setKeySignature(-3); // Must be set to a key where the C chord is minor (such as key of Eb)
        BasicReharm reharm = new BasicReharm(score);
        assertFalse(reharm.isChordToneOf("e", "Cm"));
    }
}