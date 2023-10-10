package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlattenMultiValuedAttributeTest {
    Logger log = LogManager.getLogger(FlattenMultiValuedAttributeTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - Generic - FlattenMultiValuedAttribute.xml";

    @Test
    public void testGenericRule () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        String applicationName = "AD Source";
        String nativeIdentity = "john.doe";
        String attribute = "permissions";

        List<String> permissions = Arrays.asList("read","write","manage");

        IdnRuleUtil idn = mock();
        when(idn.getRawAccountAttribute(applicationName, nativeIdentity, attribute)).thenReturn(null);

        when(idn.getRawAccountAttribute(applicationName + " [source]", nativeIdentity, attribute)).thenReturn(permissions);

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("applicationName", applicationName);
        i.set("nativeIdentity",nativeIdentity);
        i.set("attribute", attribute);
        i.set("delimiter", ",");
        i.set("debugError", false);


        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        log.info("Beanshell script returned: " + result);




    }
}
