package pl.lodz.p.it.ssbd2022.ssbd02.security;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:app/jdbc/ssbd02auth",
        callerQuery = "select distinct password from authorizationview where login = ?",
        groupsQuery = "select level from authorizationview where login = ?",
        hashAlgorithm = PasswordHashImpl.class
)
public class PostgresIdentityStore {

}
