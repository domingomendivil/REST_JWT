package uy.gub.dgi.googleapi;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2.Tokeninfo;

public class GoogleApiImpl implements GoogleApi {

	
	private String url ="https://console.developers.google.com/apis/credentials/oauthclient/";
	
	private String client_secret_2= "37569178841-f4brk3ed8ec15vt36iodcb26c35aic8h.apps.googleusercontent.com";
	
	private String projectName="project=angelic-turbine-134215";
	
	
	
	private final GoogleAuthorizationCodeFlow flow;
	
	private static HttpTransport HTTP_TRANSPORT;
	
	
    
    
	  /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();
	
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	
	private static final String CLIENT_ID = "37569178841-f4brk3ed8ec15vt36iodcb26c35aic8h.apps.googleusercontent.com";
	
	private static final String CLIENT_SECRET = "AIzaSyAzf4NtSnMRoS8rkLaS4VcNvq4r2W6J97A";
	
	private static final Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
	
	private String stateToken;
	
	private static final String CALLBACK_URI = "http://localhost:63342/PruebaMingo/redirect.html";
	
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
	        System.getProperty("user.home"), ".credentials/calendar-java-quickstart");
	
	static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

	
	/**
	 * Generates a secure state token 
	 */
	private void generateStateToken(){
		SecureRandom sr1 = new SecureRandom();
		stateToken = "google;"+sr1.nextInt();

	}
	
	public GoogleApiImpl() {
		Collection<String> scopes = SCOPE;
		HttpTransport transport =HTTP_TRANSPORT;
		flow = new GoogleAuthorizationCodeFlow.Builder(transport, JSON_FACTORY, CLIENT_ID,CLIENT_SECRET, scopes).build();
		generateStateToken();
	}

	@Override
	public String getLoginPage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
	 /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR_READONLY);

	
	 public static Credential authorize() throws IOException {
	        // Build flow and trigger user authorization request.
		 GoogleAuthorizationCodeFlow.Builder codeFlow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID,CLIENT_SECRET, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY);
		 System.out.println("CodeFlow built");
	        GoogleAuthorizationCodeFlow flow = codeFlow      		
	                .setAccessType("offline")
	                .build();
	        String accessToken = "4/rx8L6MCUvlExZfbRkyPeRL3tQH2lxXQO6ujEGcbzy0g#";
	        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
			Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("Web client 1")
					.build();
			System.out.println("oauth2 "+oauth2.getServicePath());
			Tokeninfo tokenInfo2 = oauth2.tokeninfo().setAccessToken(accessToken);
			System.out.println("tokeninfo2 seteado");
			System.out.println("TOKEN INFO "+tokenInfo2.execute());


			//System.out.println(tokeninfo.toString());
	       // System.out.println("CodeFlow built called");
	        Credential credential2 = new AuthorizationCodeInstalledApp(
	            flow, new LocalServerReceiver()).authorize("user");
	      //  System.out.println(
	      //          "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
	        return credential;
	    }

	
	public String getUserprofile(String authCode) throws GooleAPIException{
		GoogleTokenResponse response;
		
		/**GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
		Oauth2 oauth2 = new Oauth2	.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
		
		Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
		System.out.println(tokeninfo.toString());**/

		try {
			response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
			final Credential credential = flow.createAndStoreCredential(response, null);
			final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
			final GenericUrl url = new GenericUrl(USER_INFO_URL);
			final HttpRequest request = requestFactory.buildGetRequest(url);
			request.getHeaders().setContentType("application/json");
			final String jsonIdentity = request.execute().parseAsString();
			return jsonIdentity;
		} catch (IOException e) {
			e.printStackTrace();
			throw new GooleAPIException(e);
		}
		
	}
	
	
	  public static com.google.api.services.calendar.Calendar
      getCalendarService() throws IOException {
      Credential credential = authorize();
      return new com.google.api.services.calendar.Calendar.Builder(
              HTTP_TRANSPORT, JSON_FACTORY, credential)
              .setApplicationName("Web client 1")
              .build();
  }

	public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
            getCalendarService();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
            .setMaxResults(10)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }

	@Override
	public GoogleUserProfile getGoogleUserProfile() {
		// TODO Auto-generated method stub
		return null;
	}


}
