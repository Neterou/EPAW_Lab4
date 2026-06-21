package epaw.lab4.service;

import java.util.List;

import epaw.lab4.model.Comment;
import epaw.lab4.repository.CommentRepository;

public class CommentService {

    private static CommentService instance;
    private final CommentRepository commentRepository;

    private CommentService() {
        this.commentRepository = CommentRepository.getInstance();
    }

    public static synchronized CommentService getInstance() {
        if (instance == null) {
            instance = new CommentService();
        }
        return instance;
    }

    public void add(Comment comment) {
        commentRepository.save(comment);
    }

    public void delete(Integer id, Integer userId) {
        commentRepository.delete(id, userId);
    }

    public List<Comment> getComments(Integer tweetId) {
        return commentRepository.findByTweet(tweetId).orElse(null);
    }
}
