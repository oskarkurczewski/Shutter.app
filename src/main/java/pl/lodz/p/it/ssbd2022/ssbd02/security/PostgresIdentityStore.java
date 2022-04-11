package pl.lodz.p.it.ssbd2022.ssbd02.security;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:app/jdbc/ssbd02auth",
        callerQuery = "select distinct password from authorization_view where login = ?",
        groupsQuery = "select access_level from authorization_view where login = ?",
        hashAlgorithm = PasswordHashImpl.class
)
public class PostgresIdentityStore {

}
