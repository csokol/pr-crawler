# pr-crawler

Dependencies:
 - maven
 - java 12

Running:
```bash
mvn clean package

java \
    -Dbitbucket.server.hostname=bitbucket.company.com \
    -Dbitbucket.server.project=XXX \
    -Dbitbucket.server.repo=xxx-xxx \
    -Dbitbucket.server.token=xxxxx \
    -Dbitbucket.server.max-prs=200 \
    -jar target/pr-crawler-0.0.1-SNAPSHOT.jar
```

[How do I generate a bitbucket server token?](https://confluence.atlassian.com/bitbucketserver055/personal-access-tokens-940682155.htm)
