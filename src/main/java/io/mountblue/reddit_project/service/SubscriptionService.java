package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.model.Subscription;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository = subscriptionRepository;
    }

    public void saveSubScription(User user, SubReddit subReddit) {
        Subscription subscription = new Subscription();
        subscription.setSubReddit(subReddit);
        subscription.setUser(user);
        subscriptionRepository.save(subscription);
    }

    public void deleteSubScription(User user, SubReddit subReddit) {
        Subscription subscription = subscriptionRepository.findBYUserAndSubReddit(user.getId(),subReddit.getSubRedditId());
        subscriptionRepository.deleteById(subscription.getId());
    }

    public Subscription findSubscription(Long id, Long subRedditId) {
        return subscriptionRepository.findBYUserAndSubReddit(id,subRedditId);
    }

    public List<Long> findAllByUserId(Long id) {
        return subscriptionRepository.findByUserId(id);
    }
}