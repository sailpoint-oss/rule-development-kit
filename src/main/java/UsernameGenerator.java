import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import sailpoint.object.Application;
import sailpoint.object.Field;
import sailpoint.object.Identity;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

public class UsernameGenerator {
    Logger log = LogManager.getLogger(UsernameGenerator.class);
    Identity identity = new Identity();
    Application application = new Application();
    IdnRuleUtil idn;
    Field field = new Field();

    int MAX_USERNAME_LENGTH = 12;


    public String generateUsername(String firstName, String lastName) throws GeneralException {
        firstName = StringUtils.trimToNull(firstName);
        lastName = StringUtils.trimToNull(lastName);
        String otherName = identity.getStringAttribute("otherName");

        if(firstName != null) {
            firstName = firstName.replaceAll("[^a-zA-Z0-9]", "");
        }

        if(lastName != null) {
            lastName = lastName.replaceAll("[^a-zA-Z0-9]", "");
        }

        if(otherName != null) {
            otherName = otherName.replaceAll("[^a-zA-Z0-9]", "");
        }

        if((firstName == null) || (lastName == null)) {
            log.debug( "AD Create User Name | Exit from generateUsername method. No last name and first name for user" );
            return null;
        }

        if(!StringUtils.isEmpty(otherName)) {
            firstName = otherName;
        }

        String username = null;
        String fullName = firstName + "." + lastName;

        if(fullName.length() > MAX_USERNAME_LENGTH) {
            int firstNameLength = firstName.length();

            if(firstNameLength > (MAX_USERNAME_LENGTH - 2)) {
                for(int lastNameLength = 0; lastNameLength < lastName.length(); lastNameLength++) {
                    username = firstName.substring(0, (MAX_USERNAME_LENGTH - 2)) + "." + lastName.charAt(lastNameLength);
                    username = username.toLowerCase();
                    if (isUnique(username)) {
                        log.debug( "AD Create User Name | Unique username generated: " + username);
                        log.debug( "AD Create User Name | Exit from the  GenerateUsername Method" );
                        return username;
                    }
                }
            } else {
                for(int lastNameLength = 0; lastNameLength < lastName.length(); lastNameLength++) {
                    username = firstName + "." + lastName.charAt(lastNameLength);
                    username = username.toLowerCase();
                    if (isUnique(username)) {
                        log.debug( "AD Create User Name | Unique username generated: " + username);
                        log.debug( "AD Create User Name | Exit from the  GenerateUsername Method" );
                        return username;
                    }
                }
            }
        } else {
            username = fullName;
            username = username.toLowerCase();
            if (isUnique(username)) {
                log.debug( "AD Create User Name | Unique username generated: " + username);
                log.debug( "AD Create User Name | Exit from the  GenerateUsername Method" );
                return username;
            } else {
                for(int lastNameLength = 0; lastNameLength < lastName.length(); lastNameLength++) {
                    username = firstName + "." + lastName.charAt(lastNameLength);
                    username = username.toLowerCase();
                    if (isUnique(username)) {
                        log.debug( "AD Create User Name | Unique username generated: " + username);
                        log.debug( "AD Create User Name | Exit from the  GenerateUsername Method" );
                        return username;
                    }
                }
            }
        }



        return null;
    }

    public boolean isUnique(String username) throws GeneralException {
        return !idn.accountExistsByDisplayName(application.getName(), username);
    }
}
