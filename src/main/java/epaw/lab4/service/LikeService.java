package epaw.lab4.service;

import epaw.lab4.repository.LikeRepository;

public class LikeService {

    private static LikeService instance;
    private final LikeRepository likeRepository;

    private LikeService() {
        this.likeRepository = LikeRepository.getInstance();
    }

    public static synchronized LikeService getInstance() {
        if (instance == null) {
            instance = new LikeService();
        }
        return instance;
    }

    /** Toggle the like for a user on a tweet; returns the new like count. */
    public int toggle(Integer userId, Integer tweetId) {
        if (likeRepository.isLiked(userId, tweetId)) {
            likeRepository.remove(userId, tweetId);
        } else {
            likeRepository.add(userId, tweetId);
        }
        return likeRepository.countForTweet(tweetId);
    }
}
