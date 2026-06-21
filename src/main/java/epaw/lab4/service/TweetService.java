package epaw.lab4.service;

import java.util.List;

import epaw.lab4.model.Tweet;
import epaw.lab4.repository.TweetRepository;

public class TweetService {

    private static TweetService instance;
    private final TweetRepository tweetRepository;

    private TweetService() {
        this.tweetRepository = TweetRepository.getInstance();
    }

    public static synchronized TweetService getInstance() {
        if (instance == null) {
            instance = new TweetService();
        }
        return instance;
    }

    public void add(Tweet tweet) {
        tweetRepository.save(tweet);
    }

    public void delete(Integer id, Integer userId) {
        tweetRepository.delete(id, userId);
    }

    /** Posts for a bubble's feed; {@code viewerId} is used to flag own likes. */
    public List<Tweet> getBubbleFeed(Integer bubbleId, Integer viewerId, Integer start, Integer count) {
        return tweetRepository.findByBubble(bubbleId, viewerId, start, count).orElse(null);
    }

    /** Posts authored by a particular user (for their public profile page). */
    public List<Tweet> getUserPosts(Integer userId, Integer start, Integer count) {
        return tweetRepository.findByUser(userId, start, count).orElse(null);
    }

    /** Personalized timeline: posts from the people {@code viewerId} follows. */
    public List<Tweet> getFollowedTimeline(Integer viewerId, Integer start, Integer count) {
        return tweetRepository.findFollowedTimeline(viewerId, start, count).orElse(null);
    }
}
