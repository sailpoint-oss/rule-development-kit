package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.connector.webservices.Endpoint;
import sailpoint.connector.webservices.WebServicesClient;
import sailpoint.object.*;
import sailpoint.ps.utils.RuleXmlUtils;
import sailpoint.rule.Account;
import sailpoint.rule.ManagedAttributeDetails;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;

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

        ProvisioningPlan plan = mock(ProvisioningPlan.class);
        Filter filter = mock(Filter.class);

        AccountRequest accountRequest = mock(AccountRequest.class);
        when(accountRequest.getOp()).thenReturn(ProvisioningPlan.ObjectOperation.Disable);

        plan.getAccountRequests();
//        ManagedAttribute managedAttribute = mock(ManagedAttribute.class);
//        AttributeDefinition attributeDefinition = mock(AttributeDefinition.class);
//        ManagedAttributeDetails managedAttributeDetails = mock(ManagedAttributeDetails.class);
//        Link link = mock(Link.class);
//        QueryOptions qo = mock(QueryOptions.class);
//        Account account = mock(Account.class);
//        Bundle bundle = mock(Bundle.class);
//        Endpoint endpoint = mock(Endpoint.class);
//        WebServicesClient webServicesClient = mock(WebServicesClient.class);
//        Field field = mock(Field.class);
//        Schema schema = mock(Schema.class);

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

        log.info("Beanshell script returned: " + result);




    }
}
