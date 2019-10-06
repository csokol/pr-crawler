package de.smava.prcrawler.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.smava.prcrawler.resource.PullRequest.BranchRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class PullRequestSpec {

    public static final PullRequest VALID = valid().build();

    public static PullRequest.PullRequestBuilder valid() {
        return PullRequest.builder()
                .fromRef(BranchRef.builder().build())
                .toRef(BranchRef.builder().build())
                .id(1L);
    }

}
