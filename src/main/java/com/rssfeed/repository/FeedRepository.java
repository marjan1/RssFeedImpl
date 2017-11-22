package com.rssfeed.repository;

import com.rssfeed.model.FeedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<FeedItem, Short> {

    FeedItem findFeedItemByUri(String uri);

}
