package com.example.hi.onetime.Auth;

/**
 * Created by Hi on 16-06-2017.
 */



        import android.content.Context;
        import android.util.Log;

        import com.amazonaws.AmazonServiceException;
        import com.amazonaws.auth.CognitoCachingCredentialsProvider;
        import com.amazonaws.regions.Region;
        import com.amazonaws.regions.Regions;
        import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * This class is used to get clients to the various AWS services. Before
 * accessing a client the credentials should be checked to ensure validity.
 */
public class AmazonClientManager {
      static CognitoCachingCredentialsProvider credentials;

    public CognitoCachingCredentialsProvider getCredentials() {
        return credentials;
    }

    public void setCredentials(CognitoCachingCredentialsProvider credentials) {
        this.credentials = credentials;
    }

    private static final String LOG_TAG = "AmazonClientManager";

    private static AmazonDynamoDBClient ddb = null;
    private static Context context;

    public AmazonClientManager(Context context) {
        this.context = context;
    }

    public static AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }

    public boolean hasCredentials() {
        return (!(Constants.IDENTITY_POOL_ID.equalsIgnoreCase("us-east-1:e9b08250-5265-41a4-b97d-e282e8e99431")
                || Constants.TEST_TABLE_NAME.equalsIgnoreCase("amazonapp-mobilehub-191482968-Locations")));
    }

    public static void validateCredentials() {

        if (ddb == null) {
            initClients();
        }
    }

    private static void initClients() {
        /* credentials = new CognitoCachingCredentialsProvider(
                context,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);*/
        credentials = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:eb8a2d21-a776-4e67-84d2-e99644667e58", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
        Log.e(LOG_TAG, "Error, wipeCredentialsOnAuthError called" + ex);
        if (
            // STS
            // http://docs.amazonwebservices.com/STS/latest/APIReference/CommonErrors.html
                ex.getErrorCode().equals("IncompleteSignature")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InvalidClientTokenId")
                        || ex.getErrorCode().equals("OptInRequired")
                        || ex.getErrorCode().equals("RequestExpired")
                        || ex.getErrorCode().equals("ServiceUnavailable")

                        // DynamoDB
                        // http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                        || ex.getErrorCode().equals("AccessDeniedException")
                        || ex.getErrorCode().equals("IncompleteSignatureException")
                        || ex.getErrorCode().equals(
                        "MissingAuthenticationTokenException")
                        || ex.getErrorCode().equals("ValidationException")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InternalServerError")) {

            return true;
        }

        return false;
    }
}