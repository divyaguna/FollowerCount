package com.twitter.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class CallbackServlet extends HttpServlet{
	
final static Logger log = Logger.getLogger(CallbackServlet.class);
	
	
	 private static final long serialVersionUID = -6205814293093350242L;
	
	 
		    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    	log.debug("Handling call back url and getting accesstoken with the use of oauth verifier");
		    	// Get twitter object from session
		    	Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		    	//Get twitter request token object from session
		    	RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
		    	String verifier = request.getParameter("oauth_verifier");
		    	try {
		    	    log.debug("Get twitter access token object by verifying request token");
		    	    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
		    	    request.getSession().removeAttribute("requestToken");
		    	    log.debug("Calling show User api for user information by passing screen name with the help of accessToken");
		    	    User user = twitter.showUser(accessToken.getScreenName());
		    	    log.debug("Calling method of followercount from twitter4j user class");
				    int followercount =user.getFollowersCount();
				    System.out.println(followercount);
				    log.debug("Hello "+accessToken.getScreenName()+", your twitter follower count is "+followercount);
				    request.setAttribute("followercount",String.valueOf(followercount));
				    request.getRequestDispatcher("index.jsp").forward(request, response);
		    	    }
		    	catch(Exception e) {
		    		throw new ServletException(e);
		    	}
		    }
}