package com.twitter.user;
import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class FollowerCount extends HttpServlet{
	
	final static Logger log = Logger.getLogger(FollowerCount.class);
	
	private final String CONSUMER_KEY ="gbAvfhuMvbKXHvgA7MKFHNRfg";
	private final String CONSUMER_SECRET="ooyT7w5AjZrTZTu2HWNA88W0aDGWm2a98s1zvQa2T9Iu1DPf9N";
	 private static final long serialVersionUID = -6205814293093350242L;
	
	 
		    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			    // The factory instance is re-useable and thread safe.
		    	BasicConfigurator.configure();
		    	log.debug("inside doGet method");
		    	 // Set response content type
		    	
		    	String oauth_verifier,oauth_token;
		    	AccessToken accessToken=null;
		    	 
		        response.setContentType("text/html");

		        // Actual logic goes here.
		        //PrintWriter out = response.getWriter();
		    	try{
		    		ConfigurationBuilder builder = new ConfigurationBuilder();
		    		builder.setOAuthConsumerKey(CONSUMER_KEY);
		    		builder.setOAuthConsumerSecret(CONSUMER_SECRET);
		    		
		    		Configuration configuration = builder.build();
		    		TwitterFactory factory = new TwitterFactory(configuration);
		    		Twitter twitter = factory.getInstance();
		    		log.debug("twitter instance created");
			   /* Twitter twitter = TwitterFactory.getSingleton();
			    twitter.setOAuthConsumer("gbAvfhuMvbKXHvgA7MKFHNRfg", "ooyT7w5AjZrTZTu2HWNA88W0aDGWm2a98s1zvQa2T9Iu1DPf9N");*/
			    RequestToken requestToken = twitter.getOAuthRequestToken();
			    //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			  
			      System.out.println("Open the following URL and grant access to your account:");
			      System.out.println(requestToken.getAuthorizationURL());
			      try {
			    	  Desktop desktop = java.awt.Desktop.getDesktop();
			    	  URI oURL = new URI(requestToken.getAuthorizationURL());
			    	  desktop.browse(oURL);
			    	} catch (Exception e) {
			    	  e.printStackTrace();
			    	}
			      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			      oauth_token=requestToken.getToken();
			      oauth_verifier=requestToken.getTokenSecret();
			      accessToken=twitter.getOAuthAccessToken(oauth_token, oauth_verifier);
			      
			     /* String pin = br.readLine();
			      try{
			         if(pin.length() > 0){
			           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			         }else{
			           accessToken = twitter.getOAuthAccessToken();
			         }
			      } catch (TwitterException te) {
			        if(401 == te.getStatusCode()){
			          System.out.println("Unable to get the access token.");
			        }else{
			          te.printStackTrace();
			        }
			      }*/
			      System.out.println("accessToken"+accessToken);
			    
			    User user = twitter.showUser(accessToken.getScreenName());
			    int followercount =user.getFollowersCount();
			    System.out.println(followercount);
			    request.setAttribute("followercount",String.valueOf(followercount));
			    request.getRequestDispatcher("index.jsp").forward(request, response); 
			    }
			    catch(TwitterException e){
			    	 e.printStackTrace();
			    }
			 /* //persist to the accessToken for future reference.
			    storeAccessToken((int) twitter.verifyCredentials().getId() , accessToken);
			    Status status = twitter.updateStatus(args[0]);
			    System.out.println("Successfully updated the status to [" + status.getText() + "].");
			    System.exit(0);
			  }
			  private static void storeAccessToken(int useId, AccessToken accessToken){
			    //store accessToken.getToken()
			    //store accessToken.getTokenSecret()
			  }*/
			   
		 }
		 
}

