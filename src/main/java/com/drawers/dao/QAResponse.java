package com.drawers.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harshit on 1/5/16.
 */
public class QAResponse {
    public String getHeroImage() {
        return heroImage;
    }

    public void setHeroImage(String heroImage) {
        this.heroImage = heroImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActionableItem getPrimary() {
        return primary;
    }

    public void setPrimary(ActionableItem primary) {
        this.primary = primary;
    }

    public ActionableItem getSecondary() {
        return secondary;
    }

    public void setSecondary(ActionableItem secondary) {
        this.secondary = secondary;
    }

    public ActionableItem getTertiary() {
        return tertiary;
    }

    public void setTertiary(ActionableItem tertiary) {
        this.tertiary = tertiary;
    }

    @SerializedName("h")
    private String heroImage;

    @SerializedName("t")
    private String title;

    // max of 100 chars
    @SerializedName("d")
    private String description;

    @SerializedName("p")
    private ActionableItem primary;

    @SerializedName("s")
    private ActionableItem secondary;

    @SerializedName("r")
    private ActionableItem tertiary;

    public QAResponse(String heroImage, String title, String description, ActionableItem primary,
                      ActionableItem secondary, ActionableItem tertiary) {
        this.heroImage = heroImage;
        this.title = title;
        this.description = getStripDescription(description);
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
    }

    private String getStripDescription(String description) {
        return description.length() < 100 ? description : String.format("%.100s", description) + "...";
    }

    public static class ActionableItem {
        public String getDisplayText() {
            return displayText;
        }

        public void setDisplayText(String displayText) {
            this.displayText = displayText;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getReplyType() {
            return replyType;
        }

        public void setReplyType(String replyType) {
            this.replyType = replyType;
        }

        @SerializedName("l")
        private String displayText;

        @SerializedName("r")
        private String reply;

        @SerializedName("t")
        private String replyType;


        public ActionableItem(String displayText, String reply, String replyType) {
            this.displayText = displayText;
            this.reply = reply;
            this.replyType = replyType;
        }
    }


    public static class ReplyType {
        public static final String WEB = "W";
        public static final String REPLY = "R";
        public static final String NA = "N";
    }
}