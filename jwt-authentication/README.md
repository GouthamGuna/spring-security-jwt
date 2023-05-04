### Configure Spring Security

    security.oauth2.resource.filter-order=3
    
    security.signing-key=MaYzkSjmkzPC57L
    security.encoding-strength=256
    security.security-realm=Spring Boot JWT Example Realm
    
    security.jwt.client-id=testjwtclientid
    security.jwt.client-secret=XY7kmzoNzl100
    security.jwt.grant-type=password
    security.jwt.scope-read=read
    security.jwt.scope-write=write
    security.jwt.resource-ids=testjwtresourceid

## [Study the article](https://betterprogramming.pub/secure-a-spring-boot-rest-api-with-json-web-token-reference-to-angular-integration-e57a25806c50)