package io.dropwizard.reactor.example.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserTopic {
    @JsonProperty("user")
    private User user;
    @JsonProperty("top_topic")
    private Topic topTopic;

    @JsonCreator
    public UserTopic(@JsonProperty("user") User user,
                     @JsonProperty("top_topic") Topic topTopic) {
        this.user = user;
        this.topTopic = topTopic;
    }

    public User getUser() {
        return user;
    }

    public Topic getTopTopic() {
        return topTopic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTopic userTopic = (UserTopic) o;
        return Objects.equals(user, userTopic.user) &&
                Objects.equals(topTopic, userTopic.topTopic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, topTopic);
    }

    @Override
    public String toString() {
        return "UserTopic{" +
                "user=" + user +
                ", topTopic=" + topTopic +
                '}';
    }
}
