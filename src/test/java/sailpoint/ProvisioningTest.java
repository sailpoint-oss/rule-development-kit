package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.object.*;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.tools.GeneralException;

import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProvisioningTest {
    Logger log = LogManager.getLogger(ProvisioningTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - BeforeProvisioningRule - Example Rule.xml";

    @Test
    public void testProvisioning () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        ProvisioningPlan plan = mock(ProvisioningPlan.class);
        Filter filter = mock(Filter.class);

        AccountRequest accountRequest = mock(AccountRequest.class);
        when(accountRequest.getOp()).thenReturn(ProvisioningPlan.ObjectOperation.Disable);

        List<AccountRequest> accountRequests = new ArrayList<>();
        accountRequests.add(accountRequest);


        when(plan.getAccountRequests()).thenReturn(accountRequests);

        String result = "";

        i.set("log", log);
        i.set("plan", plan);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        log.info("Beanshell script returned: " + result);




    }
}
