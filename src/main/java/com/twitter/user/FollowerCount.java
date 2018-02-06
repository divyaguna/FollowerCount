package com.twitter.user;
import org.apache.log4j.Logger;

import org.apache.log4j.BasicConfigurator;

import java.io.IOException;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class FollowerCount extends HttpServlet{
	
	final static Logger log = Logger.getLogger(FollowerCount.class);
	
	private final String CONSUMER_KEY ="gbAvfhuMvbKXHvgA7MKFHNRfg";
	private final String CONSUMER_SECRET="ooyT7w5AjZrTZTu2HWNA88W0aDGWm2a98s1zvQa2T9Iu1DPf9N";
	 private static final long serialVersionUID = -6205814293093350242L;
	
	 
		    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			    
		    	BasicConfigurator.configure();
		    	
		    	try{
		    		log.debug("Setting consumer key and consumer secret to configure builder and passing to twitter factory");
		    		ConfigurationBuilder builder = new ConfigurationBuilder();
		    		builder.setOAuthConsumerKey(CONSUMER_KEY);
		    		builder.setOAuthConsumerSecret(CONSUMER_SECRET);
		    		
		    		Configuration configuration = builder.build();
		    		TwitterFactory factory = new TwitterFactory(configuration);
		    		log.debug("Creating twitter instance");
		    		Twitter twitter = factory.getInstance();
		    		//getting session of request and setting twitter attribute with twitter object for later use of session at the time of call back
		    		request.getSession().setAttribute("twitter", twitter);
		    		//creating call back url by getting request url and passing to requestToken Object
		    		StringBuffer callbackURL = request.getRequestURL();
		    	    int index = callbackURL.lastIndexOf("/");
		    	    callbackURL.replace(index, callbackURL.length(), "").append("/callback");
			    RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
			    //Setting of requestToken session to request object with the help of requestToken object for later to get accesstoken at the time of call back
			    request.getSession().setAttribute("requestToken", requestToken);
			    log.debug("Sending authentication url of twitter to browser and authenticating then getting back to call back url with oauth_verifier");
			    response.sendRedirect(requestToken.getAuthenticationURL());
		    	}
		    	catch(TwitterException e) {
		    		throw new ServletException(e);
		    	}
		    }
}

