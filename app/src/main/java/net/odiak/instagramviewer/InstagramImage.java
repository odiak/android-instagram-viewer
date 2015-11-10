package net.odiak.instagramviewer;


import org.json.JSONException;
import org.json.JSONObject;

public class InstagramImage {
    String link;
    String imageUrl;
    String thumbnailImageUrl;
    String smallImageUrl;

    public static InstagramImage fromJson(JSONObject jsonObject) throws JSONException {

        return new InstagramImage()
                .setImageUrl(jsonObject
                        .getJSONObject("images")
                        .getJSONObject("standard_resolution")
                        .getString("url"))
                .setLink(jsonObject
                        .getString("link"));
    }

    public String getLink() { return link; }
    public String getImageUrl() { return imageUrl; }

    public InstagramImage setLink(String link) {
        this.link = link;
        return this;
    }

    public InstagramImage setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
