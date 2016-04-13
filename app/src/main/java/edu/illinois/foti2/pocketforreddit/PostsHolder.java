package edu.illinois.foti2.pocketforreddit;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by Chris on 4/12/2016.
 */
public class PostsHolder {
    private final String URL_TEMPLATE =   "http://www.reddit.comSUBREDDIT_NAME/.json?after=AFTER";
    String subreddit;
    String url;
    String after;

    PostsHolder(String sr){
        subreddit = sr;
        after="";
        generateURL();
    }

    private void generateURL(){
        if(subreddit!=null){
            url=URL_TEMPLATE.replace("SUBREDDIT_NAME", "/r/"+subreddit);
        }
        else{
            url=URL_TEMPLATE.replace("SUBREDDIT_NAME", "");
        }
        url = url.replace("AFTER", after);
    }

    List<Post> fetchPosts(){
        String raw = RemoteData.readContents(url);
        List<Post> list = new ArrayList<Post>();
        try{
            JSONObject data = new JSONObject(raw).getJSONObject("data");
            JSONArray children = data.getJSONArray("children");
            after = data.getString("after");
            for(int i=0; i<children.length();i++){
                JSONObject cur = children.getJSONObject(i).getJSONObject("data");
                Post p = new Post();
                p.title = cur.optString("title");
                p.url = cur.optString("url");
                p.numComments = cur.optInt("num_comments");
                p.points = cur.optInt("score");
                p.author = cur.optString("author");
                p.subreddit = cur.optString("subreddit");
                p.permalink = cur.optString("permalink");
                p.domain = cur.optString("domain");
                p.id = cur.optString("id");
                p.thumbnail = cur.optString("thumbnail");
                if(p.title!=null){
                    list.add(p);
                }
            }
        }
        catch(Exception e){
            Log.e("fetchPosts()",e.toString() + ": " + url);
        }
        return list;
    }

    List<Post> fetchMorePosts(){
        generateURL();
        return fetchPosts();
    }


}
