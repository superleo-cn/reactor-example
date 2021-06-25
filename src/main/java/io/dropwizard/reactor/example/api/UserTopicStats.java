package io.dropwizard.reactor.example.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserTopicStats {
    @JsonProperty("user")
    private User user;
    @JsonProperty("top_group")
    private Topic topTopic;
    @JsonProperty("count")
    private Long count;

    @JsonCreator
    public UserTopicStats(@JsonProperty("user") User user,
                          @JsonProperty("top_topic") Topic topTopic,
                          @JsonProperty("count") long count) {
        this.user = user;
        this.topTopic = topTopic;
        this.count = count;
    }

    public User getUser() {
        return user;
    }

    public Topic getTopTopic() {
        return topTopic;
    }

    public Long getTopicCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTopicStats that = (UserTopicStats) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(topTopic, that.topTopic) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, topTopic, count);
    }

    @Override
    public String toString() {
        return "UserTopicStats{" +
                "user=" + user +
                ", topTopic=" + topTopic +
                ", count=" + count +
                '}';
    }
}
