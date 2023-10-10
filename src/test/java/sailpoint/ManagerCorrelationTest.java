package sailpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import bsh.EvalError;
import bsh.Interpreter;
import sailpoint.object.Link;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

public class ManagerCorrelationTest {
    Logger log = LogManager.getLogger(ManagerCorrelationTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - ManagerCorrelation - Manager Email Correlation.xml";

    @Test
    public void testWhereManagerHasEmail () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        String managerEmail = "pat.smith@mail.com";
        IdnRuleUtil idn = mock();
        Link link = mock();
        Object managerAttributeValue = mock();

        when(link.getAttribute("manager.email")).thenReturn(managerEmail);

        i.set("log", log);
        i.set("idn", idn);
        i.set("link", link);
        i.set("managerAttributeValue", managerAttributeValue);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) i.eval(source);

        verify(link, times(1)).getAttribute("manager.email");

        assertNotNull(result);
        assertEquals(result.get("identityAttributeName"), "email");
        assertEquals(result.get("identityAttributeValue"), managerEmail);

    }
}
