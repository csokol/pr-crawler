package de.smava.prcrawler.crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import de.smava.prcrawler.resource.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PaginatedResponseCrawler {
    private BitbucketRestClient bitbucketRestClient;
    private TypeReference typeReference;

    public PaginatedResponseCrawler(BitbucketRestClient bitbucketRestClient, TypeReference typeReference) {
        this.bitbucketRestClient = bitbucketRestClient;
        this.typeReference = typeReference;
    }

    public <T> List<T> crawl(Function<Integer, String> startToUrl, int limit) {
        limit = limit <= 0 ? Integer.MAX_VALUE : limit;
        var allPullRequests = new ArrayList<T>();
        Response<T> batch;
        int start = 0;
        do {
            batch = bitbucketRestClient.getPage(startToUrl.apply(start), typeReference);
            allPullRequests.addAll(batch.getValues());
            start += batch.getSize();
        } while (!batch.isLastPage() && allPullRequests.size() < limit);

        return allPullRequests;
    }
}
