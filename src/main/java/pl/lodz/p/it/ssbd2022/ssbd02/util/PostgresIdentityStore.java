package pl.lodz.p.it.ssbd2022.ssbd02.util;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:app/jdbc/ssbd02auth",
        callerQuery = "select distinct password from User where login = ?",
        groupsQuery = "select level from AccessLevel where login = ? and active = true",
        hashAlgorithm = PasswordHashImpl.class
)
public class PostgresIdentityStore {
}
