package de.smava.prcrawler.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.smava.prcrawler.resource.PullRequest.BranchRef;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class MapperTest {

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new Mapper(new ObjectMapper());
    }

    @Test
    void shouldMapPullRequests() {
        var is = getClass().getResourceAsStream("/pull-requests.json");
        var response = mapper.parse(is, new TypeReference<Response<PullRequest>>() {});
        var pullRequests = response.getValues();

        assertThat(response.isLastPage(), is(true));
        assertThat(pullRequests, hasSize(3));
        assertThat(pullRequests, hasSize(3));
        assertThat(pullRequests.get(0), any(PullRequest.class));
        assertThat(pullRequests.get(0).getId(), is(255L));
        assertThat(pullRequests.get(0).getFromRef(), is(new BranchRef("feature/SME-15129")));
        assertThat(pullRequests.get(0).getToRef(), is(new BranchRef("develop")));
    }

    @Test
    void shouldMapActivities() {
        var is = getClass().getResourceAsStream("/activities.json");
        var activites = mapper.parseActivities(is);

        assertThat(activites, hasSize(3));
        assertThat(activites.get(0), any(Activity.class));
        assertThat(activites.get(0).getAction(), is("APPROVED"));
        assertThat(activites.get(0).getUser().getName(), is("fsokol"));
    }

}
