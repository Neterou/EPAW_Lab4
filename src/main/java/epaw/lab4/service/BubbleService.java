package epaw.lab4.service;

import java.util.List;

import epaw.lab4.model.Bubble;
import epaw.lab4.model.JoinRequest;
import epaw.lab4.repository.BubbleRepository;
import epaw.lab4.repository.MembershipRepository;

public class BubbleService {

    private static BubbleService instance;
    private final BubbleRepository bubbleRepository;
    private final MembershipRepository membershipRepository;

    private BubbleService() {
        this.bubbleRepository = BubbleRepository.getInstance();
        this.membershipRepository = MembershipRepository.getInstance();
    }

    public static synchronized BubbleService getInstance() {
        if (instance == null) {
            instance = new BubbleService();
        }
        return instance;
    }

    public List<Bubble> getAllBubbles() {
        return bubbleRepository.findAll();
    }

    /** All bubbles annotated with the user's membership status (for Discover + map). */
    public List<Bubble> getBubblesForUser(Integer userId) {
        return bubbleRepository.findAllWithMembership(userId);
    }

    public Bubble getBubble(Integer id) {
        return bubbleRepository.findById(id).orElse(null);
    }

    // ---------- membership ----------

    public String membershipStatus(Integer userId, Integer bubbleId) {
        return membershipRepository.getStatus(userId, bubbleId);
    }

    public boolean isApprovedMember(Integer userId, Integer bubbleId) {
        return membershipRepository.isApproved(userId, bubbleId);
    }

    /**
     * Join a bubble. Open bubbles approve immediately; closed bubbles create a
     * PENDING request. Returns the resulting status.
     */
    public String join(Integer userId, Integer bubbleId) {
        Bubble bubble = bubbleRepository.findById(bubbleId).orElse(null);
        if (bubble == null) return null;
        String status = bubble.isOpen() ? "APPROVED" : "PENDING";
        membershipRepository.setStatus(userId, bubbleId, status);
        return status;
    }

    public void leave(Integer userId, Integer bubbleId) {
        membershipRepository.remove(userId, bubbleId);
    }

    // ---------- ownership & join-request approval ----------

    /** Pending join requests for every bubble owned by {@code ownerId}. */
    public List<JoinRequest> getPendingRequests(Integer ownerId) {
        return membershipRepository.findPendingForOwner(ownerId);
    }

    /** True only if {@code ownerId} is the owner of {@code bubbleId}. */
    private boolean owns(Integer ownerId, Integer bubbleId) {
        Bubble bubble = bubbleRepository.findById(bubbleId).orElse(null);
        return bubble != null && bubble.getOwnerId() != null && bubble.getOwnerId().equals(ownerId);
    }

    /** Approve a pending request — only the bubble owner may do this. */
    public boolean approveRequest(Integer ownerId, Integer userId, Integer bubbleId) {
        if (!owns(ownerId, bubbleId)) return false;
        membershipRepository.setStatus(userId, bubbleId, "APPROVED");
        return true;
    }

    /** Reject a pending request — only the bubble owner may do this. */
    public boolean rejectRequest(Integer ownerId, Integer userId, Integer bubbleId) {
        if (!owns(ownerId, bubbleId)) return false;
        membershipRepository.remove(userId, bubbleId);
        return true;
    }
}
