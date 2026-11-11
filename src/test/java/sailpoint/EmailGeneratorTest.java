package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailGeneratorTest {
    Logger log = LogManager.getLogger(EmailGeneratorTest.class);

    String result1 = "";
    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - AttributeGenerator - EmailGenerator.xml";

    // Test case for "N" (Contractor)
    @Test
    public void testUsernameGeneratorWhereFirstAndLastNameValidContractor() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // First attempt: email is not unique
            .thenReturn(true);  // Second attempt: email becomes unique after the first iteration

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("tyler");
        when(identity.getLastname()).thenReturn("Smith");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getAttribute("isAssociate")).thenReturn("N"); // Contractor
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result1 = (String) i.eval(source);

        assertNotNull(result1);
        assertEquals(result1, "tyler.smith1_contractor@jmfamily.com");

        log.info("Beanshell script returned: " + result1);
    }

    // Test case for "Y" (Non-Contractor)
    @Test
    public void testUsernameGeneratorWhereFirstAndLastNameValidEmployee() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // First attempt: email is not unique
            .thenReturn(true);  // Second attempt: email becomes unique after the first iteration

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("tyler");
        when(identity.getLastname()).thenReturn("Smith");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getAttribute("isAssociate")).thenReturn("Y"); // Non-Contractor
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result1 = (String) i.eval(source);

        assertNotNull(result1);
        assertEquals(result1, "tyler.smith1@jmfamily.com");

        log.info("Beanshell script returned: " + result1);
    }

    // Test case for "N" (Contractor) with a different Company ID
    @Test
    public void testUsernameGeneratorWithDifferentCompanyIDContractor() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // First attempt: email is not unique
            .thenReturn(true);  // Second attempt: email becomes unique after the first iteration

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("alice");
        when(identity.getLastname()).thenReturn("johnson");
        when(identity.getAttribute("companyId")).thenReturn("300"); // Different Company ID
        when(identity.getAttribute("isAssociate")).thenReturn("N"); // Contractor
        when(identity.getId()).thenReturn("a1b2c3d4");

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // The domain should be "jmagroup.com" based on company ID "300"
        assertNotNull(result);
        assertEquals(result, "alice.johnson1_contractor@jmagroup.com");

        log.info("Beanshell script returned: " + result);
    }

    // Test case for "Y" (Non-Contractor) with a different Company ID
    @Test
    public void testUsernameGeneratorWithDifferentCompanyIDEmployee() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any())).thenReturn(true);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("alice");
        when(identity.getLastname()).thenReturn("johnson");
        when(identity.getAttribute("companyId")).thenReturn("300"); // Different Company ID
        when(identity.getAttribute("isAssociate")).thenReturn("Y"); // Non-Contractor
        when(identity.getId()).thenReturn("a1b2c3d4");

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // The domain should be "jmagroup.com" based on company ID "300"
        assertNotNull(result);
        assertEquals(result, "alice.johnson@jmagroup.com");

        log.info("Beanshell script returned: " + result);
    }

    // Test case for "N" (Contractor) with a non-unique email initially
    @Test
    public void testUsernameGeneratorWithNonUniqueEmailInitiallyContractor() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);

        // Simulate that the email is initially not unique
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // First attempt: email is not unique
            .thenReturn(true);  // Second attempt: email becomes unique after the first iteration

        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("john");
        when(identity.getLastname()).thenReturn("doe");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");
        when(identity.getAttribute("isAssociate")).thenReturn("N");

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // Since the first email attempt should not be unique, we expect the iteration to happen
        assertNotNull(result);
        assertEquals(result, "john.doe1_contractor@jmfamily.com"); // After iteration 1, the email should be unique

        log.info("Beanshell script returned: " + result);
    }

    // Test case for "Y" (Non-Contractor) with a non-unique email initially
    @Test
    public void testUsernameGeneratorWithNonUniqueEmailInitiallyEmployee() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);

        // Simulate that the email is initially not unique
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // First attempt: email is not unique
            .thenReturn(true);  // Second attempt: email becomes unique after the first iteration

        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("john");
        when(identity.getLastname()).thenReturn("doe");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // Since the first email attempt should not be unique, we expect the iteration to happen
        assertNotNull(result);
        assertEquals(result, "john.doe1@jmfamily.com"); // After iteration 1, the email should be unique

        log.info("Beanshell script returned: " + result);
    }
    @Test
    public void testUsernameGeneratorWithMultipleNonUniqueEmailAttemptsContractor() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);

        // Simulate that the email is not unique for several attempts
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // Email is not unique on first attempt
            .thenReturn(false) // Email still not unique on second attempt
            .thenReturn(true);  // Becomes unique after the third attempt

        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("john");
        when(identity.getLastname()).thenReturn("doe");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getAttribute("isAssociate")).thenReturn("N"); // Contractor
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // After several attempts, email should finally be unique
        assertNotNull(result);
        assertEquals(result, "john.doe2_contractor@jmfamily.com"); // After 2 failed attempts, we expect the third attempt to work

        log.info("Beanshell script returned: " + result);
    }


    @Test
    public void testUsernameGeneratorWithMultipleNonUniqueEmailAttemptsEmployee() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);

        // Simulate that the email is not unique for several attempts
        when(idn.isUniqueLDAPValue(any(), any(), any(), any()))
            .thenReturn(false) // Email is not unique on first attempt
            .thenReturn(false) // Email still not unique on second attempt
            .thenReturn(true);  // Becomes unique after the third attempt

        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("john");
        when(identity.getLastname()).thenReturn("doe");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getAttribute("isAssociate")).thenReturn("Y"); // Non-Contractor
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");

        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // After several attempts, email should finally be unique
        assertNotNull(result);
        assertEquals(result, "john.doe2@jmfamily.com"); // After 2 failed attempts, we expect the third attempt to work

        log.info("Beanshell script returned: " + result);
    }


}
