package com.iradetskiy.vkapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class VKApi {
	
	public static final String TAG = VKApi.class.getName();
	
	private String mAccessToken;
	private static VKApi mApi;
	
	private final static String SCHEME = "https";
	public final static String AUTHORITY = "api.vk.com";
	public final static String PATH = "/method";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String USER_ID = "user_id";
	
	private VKApi(String accessToken) {
		mAccessToken = accessToken;
	}

	public static VKApi getApi() {
		return mApi;
	}
	
	public static VKApi getApi(String accessToken) {
		if (mApi == null) {
			mApi = new VKApi(accessToken);
		}
		return mApi;
	}
	
	public String getAccessToken() {
		return mAccessToken;
	}
	
	public VKAudioSearchResponse searchAudio(VKAudioSearchRequest request) {
		
		String[] keys = {VKAudioSearchRequest.Q, VKAudioSearchRequest.AUTO_COMPLETE, VKAudioSearchRequest.SORT, 
				VKAudioSearchRequest.LYRICS, VKAudioSearchRequest.COUNT, VKAudioSearchRequest.OFFSET, ACCESS_TOKEN};
		String[] values = {request.q, Integer.toString(request.auto_complete), Integer.toString(request.sort),
				Integer.toString(request.lyrics), Integer.toString(request.count), Integer.toString(request.offset), mAccessToken};
		
		URI requestURI = composeRequest("/audio.search.xml", keys, values);
		String responseXML = getResponse(requestURI);
		VKAudioSearchResponse response = VKAudioSearchResponse.fromXml(responseXML);
		
		return response;
    }

	public VKUsersGetResponse getUsers(VKGetUsersRequest request) {
		
		String[] keys = {VKGetUsersRequest.UIDS, VKGetUsersRequest.FIELDS, VKGetUsersRequest.NAME_CASE, ACCESS_TOKEN};
		String[] values = {request.uids, request.fields, request.name_case, mAccessToken};
		
		URI requestURI = composeRequest("/users.get.xml", keys, values);
		String responseXML = getResponse(requestURI);
		VKUsersGetResponse response = VKUsersGetResponse.fromXml(responseXML);
		
		return response;
	}
	
	public VKAudioGetResponse getAudio(VKAudioGetRequest request) {
		
		String[] keys = {VKAudioGetRequest.UID, VKAudioGetRequest.GID, VKAudioGetRequest.ALBUM_ID, VKAudioGetRequest.AIDS, VKAudioGetRequest.NEED_USER,
				VKAudioGetRequest.COUNT, VKAudioGetRequest.OFFSET, ACCESS_TOKEN};
		String[] values = {request.uid, request.gid, request.album_id, request.aids, Integer.toString(request.need_user), 
				Integer.toString(request.count), Integer.toString(request.offset), mAccessToken};
		
		URI requestURI = composeRequest("/audio.get.xml", keys, values);
		String responseXML = getResponse(requestURI);
		VKAudioGetResponse response = VKAudioGetResponse.fromXml(responseXML);
		
		return response;
	}

    public boolean deleteAudio(VKAudioItem item) {
    	
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(VKAudioItem.AID, item.aid);
        query.put(VKAudioItem.OID, item.owner_id);
        query.put(ACCESS_TOKEN, mAccessToken);
        
        getResponse(composeRequest("/audio.delete.xml", query));

        return true;
    }

    public void addAudio(VKAudioItem item){
    	
    	HashMap<String, String> query = new HashMap<String, String>();
        query.put(VKAudioItem.AID, item.aid);
        query.put(VKAudioItem.OID, item.owner_id);
        query.put(ACCESS_TOKEN, mAccessToken);

        getResponse(composeRequest("/audio.add.xml", query));
    }
	
    private URI composeRequest(String method, Map<String, String> query) {
    	String request = "";
		
		for (Map.Entry<String, String> item : query.entrySet()) {
			request += item.getKey() + "=" + item.getValue() + "&"; 
		}
		
		URI uri = null;
		
		try {
			uri = new URI(SCHEME, AUTHORITY, PATH + method,  request, null);
		} 
		catch (URISyntaxException e) {
		}
		
		return uri;
    }
    
	private URI composeRequest(String method, String[] keys, String[] values) {
		String request = "";
		
		for (int i = 0; i < keys.length; i++) {
			request += keys[i] + "=" + values[i] + "&";
		}
		
		URI uri = null;
		
		try {
			uri = new URI(SCHEME, AUTHORITY, PATH + method,  request, null);
		} catch (URISyntaxException e) {

		}
		
		return uri;
	}

	private String getResponse(URI uri) {
		String res = null;
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) uri.toURL()
					.openConnection();
			connection.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(connection
					.getOutputStream());
			wr.flush();
			wr.close();
			
			InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		    }
		    rd.close();
		    res = response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}

