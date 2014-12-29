package com.phonetics;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;


/**
 * Created by daniel on 17/12/2014.
 */
public class TestPhonetics {
    static PhoneticsProcessor phoneticsProcessor = null;

    @BeforeClass
    public static void setup()throws Exception{
        phoneticsProcessor = new PhoneticsProcessor();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary11.txt"));
    }

    @Test
    public void testPhonetics() throws Exception{
        List<PhonicsResult> results = phoneticsProcessor.process("ˈsaɪəns", "science");
        testIPAs(results, new String[]{"s", "aɪ", "ə", "n", "s"});
        testSplitWords(results, new String[]{"sc", "i", "e", "n", "ce"});


        results = phoneticsProcessor.process("haɪt", "height");
        testIPAs(results, new String[]{"h", "aɪ", "t"});
        testSplitWords(results, new String[]{"h", "eigh", "t"});

        results = phoneticsProcessor.process("hem", "hem");
        testIPAs(results, new String[]{"h", "e", "m"});
        testSplitWords(results, new String[]{"h", "e", "m"});

        results = phoneticsProcessor.process("əʊʃiˈænɪk", "oceanic");
        testIPAs(results, new String[]{"əʊ", "ʃ", "i", "æ", "n", "ɪ", "k"});
        testSplitWords(results, new String[]{"o", "c", "e", "a", "n", "i", "c"});

        results = phoneticsProcessor.process("əbændənd", "abandoned");
        testIPAs(results,       new String[]{"ə", "b", "æ", "n", "d", "ə", "n", "d"});
        testSplitWords(results, new String[]{"a", "b", "a", "n", "d", "o", "ne", "d"});

//        results = phoneticsProcessor.process("ˈɒksn", "oxen");
//        testIPAs(results, new String[]{"ɒ", "ks", "n"});
//        testSplitWords(results, new String[]{"o", "x", "en"});
        //doesn't work need to map n -> en
    }

    @Test
    public void testPhonetics1() throws Exception {
        List<PhonicsResult> results = phoneticsProcessor.process("kəmˈpjuːtə", "computer");
        testIPAs(results, new String[]{"k", "ə", "m", "p", "juː", "t", "ə"});
        testSplitWords(results, new String[]{"c", "o", "m", "p", "u", "te", "r"});
    }

    @Test
    public void testEI() throws Exception {
        List<PhonicsResult> results = phoneticsProcessor.process("əbeɪəns", "abeyance");
        testIPAs(results, new String[]{"ə", "b", "e", "ɪ", "ə", "n", "s"});
        testSplitWords(results, new String[]{"a", "b", "e", "y", "a", "n", "ce"});

        results = phoneticsProcessor.process("ˈdeɪbreɪk", "daybreak");
        testIPAs(results, new String[]{"d", "e", "ɪ", "b", "r", "e", "ɪ", "k"});
        testSplitWords(results, new String[]{"d", "a", "y", "b", "r", "e", "a", "k"});

        results = phoneticsProcessor.process("mæθəmætɪks", "mathematics");
        testIPAs(results,       new String[]{"m", "æ", "θ", "ə", "m", "æ", "t", "ɪ", "k", "s"});
        testSplitWords(results, new String[]{"m", "a", "th" ,"e", "m", "a", "t", "i", "c", "s"});

    }


    @Test
    public void testReplaceWithCombined() throws Exception {
        List<PhonicsResult> results = phoneticsProcessor.process("ælbəm", "album");
        testIPAs(results, new String[]{"æ", "l", "b", "ə", "m"});
        testSplitWords(results, new String[]{"a", "l", "b", "u", "m"});
    }


    @Test
    public void testChrysanthemum() throws Exception {
        List<PhonicsResult> results = phoneticsProcessor.process("krɪsænθəməm", "chrysanthemum");
        testIPAs(results,       new String[]{"k", "r", "ɪ", "s", "æ", "n", "θ", "ə", "m", "ə", "m"});
        testSplitWords(results, new String[]{"ch", "r", "y", "s", "a", "n", "th","e", "m", "u", "m"});
    }

    private void testSplitWords(List<PhonicsResult> results, String[] matches) {
        List<String> splitWord = results.get(0).getSplitWords();
        Assert.assertEquals(matches.length, splitWord.size());

        for(int i=0; i<matches.length; i++) {
            Assert.assertEquals(splitWord.get(i), matches[i]);
        }
    }

    private void testIPAs(List<PhonicsResult> results, String[] matches) {
        List<IPA> ipas = results.get(0).getIpas();
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(matches.length, ipas.size());
        for (int i = 0; i < ipas.size(); i++){
            Assert.assertEquals(ipas.get(i).getSymbol(), matches[i]);
        }
    }
}
