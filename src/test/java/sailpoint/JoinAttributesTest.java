package sailpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import bsh.EvalError;
import bsh.Interpreter;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.tools.GeneralException;


@SuppressWarnings("unused")
public class JoinAttributesTest {
    Logger log = LogManager.getLogger(JoinAttributesTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - BuildMap - JoinAttributes.xml";

    @SuppressWarnings("unchecked")
    @Test
    public void testEnableUSer () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        List<String> columns = Arrays.asList("User_Status");
        List<String> rows = Arrays.asList("A");


        i.set("log", log);
        i.set("cols", columns);
        i.set("record", rows);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);

        //@SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) i.eval(source);

        log.info("FINAL RESULT"+ result);


    }
    @SuppressWarnings("unchecked")
    @Test
    public void testDisableUser () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        List<String> columns = Arrays.asList("User_Status");
        List<String> rows = Arrays.asList("I");


        i.set("log", log);
        i.set("cols", columns);
        i.set("record", rows);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);

        //@SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) i.eval(source);

        log.info(result);

    }
}