package de.smava.prcrawler.crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import de.smava.prcrawler.resource.PullRequest;
import de.smava.prcrawler.resource.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static de.smava.prcrawler.resource.PullRequestSpec.VALID;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

class PaginatedResponseCrawlerTest {

    private BitbucketRestClient client;
    private TypeReference<Response<PullRequest>> pullRequestType = new TypeReference<>() {
    };

    @BeforeEach
    void setUp() {
        client = mock(BitbucketRestClient.class);
    }

    @Test
    void shouldCrawlAllPages() {
        var crawler = new PaginatedResponseCrawler(client, pullRequestType);

        when(client.<PullRequest>getPage(Mockito.any(), Mockito.eq(pullRequestType)))
                .thenReturn(
                        new Response<>(List.of(VALID, VALID), 2, 2, 0, false),
                        new Response<>(List.of(VALID, VALID), 2, 2, 2, false),
                        new Response<>(List.of(VALID), 1, 2, 4, true)
                );

        var pullRequests = crawler.crawl(start -> "start=" + start, -1);

        assertThat(pullRequests, hasSize(5));

        verify(client, times(1)).getPage("start=0", pullRequestType);
        verify(client, times(1)).getPage("start=2", pullRequestType);
        verify(client, times(1)).getPage("start=4", pullRequestType);
    }
}
