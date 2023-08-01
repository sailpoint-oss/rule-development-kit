package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.ps.utils.RuleXmlUtils;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsernameGeneratorTest {
    Logger log = LogManager.getLogger(UsernameGeneratorTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - AttributeGenerator - UsernameGenerator.xml";

    @Test
    public void testUsernameGeneratorWhereFirstNameIsGreaterThanMAXLENGTH () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock();
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(true).thenReturn(false);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("applicationName");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("Tyler");
        when(identity.getLastname()).thenReturn("Smith");
        when(identity.getStringAttribute("otherName")).thenReturn("");
        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        assertNotNull(result);
        assertEquals(result, "tyler.s");

        log.info("Beanshell script returned: " + result);

    }
}
