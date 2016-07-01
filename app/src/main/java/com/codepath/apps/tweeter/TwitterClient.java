package com.codepath.apps.tweeter;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = Config.BASE_URL; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = Config.API_KEY;       // Change this
	public static final String REST_CONSUMER_SECRET = Config.API_SECRET; // Change this
	public static final String REST_CALLBACK_URL = "oauth://ctsimpletweets"; // Change this (here and in manifest)
	public static final String HOME_TIMELINE_ENDPOINT = "statuses/home_timeline.json";
	public static final String MENTIONS_TIMELINE_ENDPOINT = "statuses/mentions_timeline.json";
    public static final String RETWEETS_OF_ME_TIMELINE_ENDPOINT = " statuses/retweets_of_me.json";
    public static final String USER_TIMELINE_ENDPOINT = "statuses/user_timeline.json";
	public static final String ACCOUNT_CREDS_ENDPOINT = "account/verify_credentials.json";
	public static final String STATUS_UPDATE_ENDPOINT = "statuses/update.json";




	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here

	public void getTimeline(String url, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(url);
		RequestParams params = new RequestParams();
		params.put("count" , 15);
		params.put("since_id", 1);
		getClient().get(apiUrl, params, handler);
	}

    public void getUserTimeline(String user_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(USER_TIMELINE_ENDPOINT);
        Log.d("DEBUG", "getUserTimeline: APIURL " + apiUrl);
        RequestParams params = new RequestParams();
        params.put("count" , 15);
		params.put("user_id", user_id);
        params.put("since_id", 1);
//        params.put("user_id", user_id);
        getClient().get(apiUrl, params, handler);

    }

	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(ACCOUNT_CREDS_ENDPOINT);
		RequestParams params = new RequestParams();
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String text, JsonHttpResponseHandler jsonHttpResponseHandler) {
		String apiURl = getApiUrl(STATUS_UPDATE_ENDPOINT);
		RequestParams params = new RequestParams();
		params.put("status", text);
		getClient().post(apiURl, params, jsonHttpResponseHandler);
	}

	public void postReply(String text, String in_reply_to_status_id, AsyncHttpResponseHandler handler) {
		String apiURl = getApiUrl(STATUS_UPDATE_ENDPOINT);
		RequestParams params = new RequestParams();
		params.put("status", text);
		params.put("in_reply_to_status_id", in_reply_to_status_id);
		getClient().post(apiURl, params, handler);
	}

    public void postRetweet(String status_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/" + status_id + ".json");
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    public void postUnretweet(String status_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/unretweet/" + status_id + ".json");
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}