import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_SYSTEM_PROPERTY;
import static com.amazonaws.SDKGlobalConfiguration.SECRET_KEY_SYSTEM_PROPERTY;
import static com.digitalfuel.core.Config.EMAIL_TEMPLATES_JSON;
public class AWSCognitoUtils {
    
    public static String accessKey = null;
    public static String secretKey = null;
    public static String clientId = null ;
    public static String poolId = null;
    public static AWSCognitoIdentityProvider provider;
    public static Regions regions;
    
    public void signUp(String username, String password, String emailAddress) {
        setupCognito();
        SignUpRequest signUpRequest = new SignUpRequest()
                .withUsername(username)
                .withClientId(clientId)
                .withPassword(password)
               .withUserAttributes(getUserAttributes(emailAddress));
        provider.signUp(signUpRequest);
    }
    public void confirmSignup(String username){
        MailReader mailReader = new MailReader();
        setupCognito();
        String otpEmailBody;
        String otpCode;
        String otpEmailSubject = "Your verification code";
       
//You need to create MailReader util method which read the OTP from email body as per your email format.
        otpCode = mailReader.readEmail(otpEmailSubject);;
        
        provider.confirmSignUp(new ConfirmSignUpRequest().withUsername(username).withClientId(clientId).withConfirmationCode(otpCode));
    }
    public static List<AttributeType> getUserAttributes(String emailAddress) {
        ArrayList<AttributeType> attributes = new ArrayList<>();
        attributes.add(new        AttributeType().withName("email").withValue(emailAddress));
        return attributes;
    }
    public static void setupCognito(){
        regions = <Provide AWS Region name e.g. Regions.EU_WEST_1>;
        accessKey = “<Replace your accessKey>”;
        secretKey = “<Replace your secretKey>”;
        clientId = “<Replace your clientId>”;
        poolId = “<Replace your poolId>”;
System.setProperty(ACCESS_KEY_SYSTEM_PROPERTY, accessKey); System.setProperty(SECRET_KEY_SYSTEM_PROPERTY, secretKey);
        
provider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(regions).withCredentials(new SystemPropertiesCredentialsProvider()).build();
    }
}