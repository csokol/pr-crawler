package de.smava.prcrawler.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequest {
    private Long id;
    private BranchRef fromRef;
    private BranchRef toRef;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class BranchRef {
        private String displayId;
    }
}
