package de.smava.prcrawler.resource;

import lombok.Data;

@Data
public class Activity {
    private String action;
    private User user;

    @Data
    public static class User {
        private String name;
    }
}
