package de.smava.prcrawler;

import com.fasterxml.jackson.core.type.TypeReference;
import de.smava.prcrawler.crawler.BitbucketRestClient;
import de.smava.prcrawler.crawler.PaginatedResponseCrawler;
import de.smava.prcrawler.resource.Activity;
import de.smava.prcrawler.resource.PullRequest;
import de.smava.prcrawler.resource.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class Main {
    private final String hostname;
    private final String repo;
    private final String project;
    private final String token;
    private final int maxPrs;

    public Main() {
        this.hostname = System.getProperty("bitbucket.server.hostname");
        this.project = System.getProperty("bitbucket.server.project");
        this.repo = System.getProperty("bitbucket.server.repo");
        this.token = System.getProperty("bitbucket.server.token");
        this.maxPrs = Integer.parseInt(System.getProperty("bitbucket.server.max-prs", "100"));
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        log.info("Starting crawler...");
        var client = new BitbucketRestClient(token);
        var crawler = new PaginatedResponseCrawler(client, new TypeReference<Response<PullRequest>>() {});
        var activityCrawler = new PaginatedResponseCrawler(client, new TypeReference<Response<Activity>>() {});

        List<PullRequest> pullRequests = crawler.crawl(start ->
                String.format("%s/%s?state=MERGED&start=%d", baseUri(), pullRequestsEndpoint(), start), maxPrs);

        for (PullRequest pr : pullRequests) {
            List<Activity> activities = activityCrawler.crawl(start -> String.format("%s/%s?start=%d", baseUri(), activitiesEndpoint(pr), start), -1);

            var totalComments = (Long) activities.stream()
                    .filter(a -> a.getAction().equals("COMMENTED") && !a.getUser().getName().equals("sonar"))
                    .count();

            log.info("PR {}: {} comments by real users", pr.getId(), totalComments);
        }
    }

    private String baseUri() {
        return String.format("https://%s/rest/api/1.0", this.hostname);
    }

    private String pullRequestsEndpoint() {
        return String.format("projects/%s/repos/%s/pull-requests", this.project, this.repo);
    }

    private String activitiesEndpoint(PullRequest pullRequest) {
        return String.format("projects/%s/repos/%s/pull-requests/%d/activities", this.project, this.repo, pullRequest.getId());
    }
}
