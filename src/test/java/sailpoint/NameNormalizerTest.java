package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.rdk.utils.RuleXmlUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameNormalizerTest {

    Logger log = LogManager.getLogger(FlattenMultiValuedAttributeTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - Generic - NameNormalizer.xml";

    @Test
    public void normalizeNameUpperCaseToNormalNameCapitalization() throws EvalError {
        Interpreter i = new Interpreter();


        i.set("patterns", "\\b(Mc|Mac)");
        char[] delimiters = {'-', ' ', '\''};
        i.set("delimiters", delimiters);
        i.set("replacements", "{\n" +
                "      \"\\\\\\\\b(?:Von)\\\\\\\\b\": \"von\",\n" +
                "      \"\\\\\\\\b(?:Del)\\\\\\\\b\": \"del\",\n" +
                "      \"\\\\\\\\b(?:Of)\\\\\\\\b\": \"of\",\n" +
                "      \"\\\\\\\\b(?:De)\\\\\\\\b\": \"de\",\n" +
                "      \"\\\\\\\\b(?:La)\\\\\\\\b\": \"la\",\n" +
                "      \"\\\\\\\\b(?:Y)\\\\\\\\b\": \"y\",\n" +
                "      \"\\\\\\\\b(?:Iv)\\\\\\\\b\": \"IV\",\n" +
                "      \"\\\\\\\\b(?:Iii)\\\\\\\\b\": \"III\",\n" +
                "      \"\\\\\\\\b(?:Ii)\\\\\\\\b\": \"II\",\n" +
                "      \"\\\\\\\\b(?:Mc )\\\\\\\\b\": \"Mc\"\n" +
                "    }");

        i.set("input", "JOHN DOE");
        String result = "";

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        assertEquals(result, "John Doe");
    }

    @Test
    public void normalizeLowerCaseToNormalNameCapitalization() throws EvalError {
        Interpreter i = new Interpreter();


        i.set("patterns", "\\b(Mc|Mac)");
        char[] delimiters = {'-', ' ', '\''};
        i.set("delimiters", delimiters);
        i.set("replacements", "{\n" +
                "      \"\\\\\\\\b(?:Von)\\\\\\\\b\": \"von\",\n" +
                "      \"\\\\\\\\b(?:Del)\\\\\\\\b\": \"del\",\n" +
                "      \"\\\\\\\\b(?:Of)\\\\\\\\b\": \"of\",\n" +
                "      \"\\\\\\\\b(?:De)\\\\\\\\b\": \"de\",\n" +
                "      \"\\\\\\\\b(?:La)\\\\\\\\b\": \"la\",\n" +
                "      \"\\\\\\\\b(?:Y)\\\\\\\\b\": \"y\",\n" +
                "      \"\\\\\\\\b(?:Iv)\\\\\\\\b\": \"IV\",\n" +
                "      \"\\\\\\\\b(?:Iii)\\\\\\\\b\": \"III\",\n" +
                "      \"\\\\\\\\b(?:Ii)\\\\\\\\b\": \"II\",\n" +
                "      \"\\\\\\\\b(?:Mc )\\\\\\\\b\": \"Mc\"\n" +
                "    }");

        i.set("input", "tony smith");
        String result = "";

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        assertEquals(result, "Tony Smith");
    }

    @Test
    public void normalizeLowerAndUpperCaseToNormalNameCapitalization() throws EvalError {
        Interpreter i = new Interpreter();


        i.set("patterns", "\\b(Mc|Mac)");
        char[] delimiters = {'-', ' ', '\''};
        i.set("delimiters", delimiters);
        i.set("replacements", "{\n" +
                "      \"\\\\\\\\b(?:Von)\\\\\\\\b\": \"von\",\n" +
                "      \"\\\\\\\\b(?:Del)\\\\\\\\b\": \"del\",\n" +
                "      \"\\\\\\\\b(?:Of)\\\\\\\\b\": \"of\",\n" +
                "      \"\\\\\\\\b(?:De)\\\\\\\\b\": \"de\",\n" +
                "      \"\\\\\\\\b(?:La)\\\\\\\\b\": \"la\",\n" +
                "      \"\\\\\\\\b(?:Y)\\\\\\\\b\": \"y\",\n" +
                "      \"\\\\\\\\b(?:Iv)\\\\\\\\b\": \"IV\",\n" +
                "      \"\\\\\\\\b(?:Iii)\\\\\\\\b\": \"III\",\n" +
                "      \"\\\\\\\\b(?:Ii)\\\\\\\\b\": \"II\",\n" +
                "      \"\\\\\\\\b(?:Mc )\\\\\\\\b\": \"Mc\"\n" +
                "    }");

        i.set("input", "mArTiN o'mAlLeY");
        String result = "";

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        assertEquals(result, "Martin O'Malley");
    }



}
