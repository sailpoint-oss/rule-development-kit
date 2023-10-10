package sailpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import bsh.EvalError;
import bsh.Interpreter;
import sailpoint.object.Identity;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

public class IdentityAttributeTest {
    Logger log = LogManager.getLogger(IdentityAttributeTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - IdentityAttribute - Example Rule.xml";

    @Test
    public void testWhereActive() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock();
        Identity identity = mock();
        String oldValue = null;

        when(identity.getAttribute("startDate")).thenReturn(getDate(-7));
        when(identity.getAttribute("endDate")).thenReturn(getDate(7));

        i.set("log", log);
        i.set("idn", idn);
        i.set("identity", identity);
        i.set("oldValue", oldValue);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);

        String result = (String) i.eval(source);

        verify(identity, times(4)).getAttribute(argThat(date -> "startDate".equals(date) || "endDate".equals(date)));

        assertNotNull(result);
        assertEquals(result, "active");

    }

    @Test
    public void testWhereStartDateInFuture() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock();
        Identity identity = mock();
        String oldValue = null;

        when(identity.getAttribute("startDate")).thenReturn(getDate(7));
        when(identity.getAttribute("endDate")).thenReturn(getDate(14));

        i.set("log", log);
        i.set("idn", idn);
        i.set("identity", identity);
        i.set("oldValue", oldValue);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);

        String result = (String) i.eval(source);

        verify(identity, times(4)).getAttribute(argThat(date -> "startDate".equals(date) || "endDate".equals(date)));

        assertNotNull(result);
        assertEquals(result, "prehire");

    }

    @Test
    public void testWherePastEndDate() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock();
        Identity identity = mock();
        String oldValue = null;

        when(identity.getAttribute("startDate")).thenReturn(getDate(-14));
        when(identity.getAttribute("endDate")).thenReturn(getDate(-7));

        i.set("log", log);
        i.set("idn", idn);
        i.set("identity", identity);
        i.set("oldValue", oldValue);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);

        String result = (String) i.eval(source);

        verify(identity, times(4)).getAttribute(argThat(date -> "startDate".equals(date) || "endDate".equals(date)));

        assertNotNull(result);
        assertEquals(result, "inactive");

    }
    public String getDate(int numberOfDaysToAdjust) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, numberOfDaysToAdjust);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(c.getTime());
    }
}
