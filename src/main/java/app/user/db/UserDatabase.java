package app.user.db;

import auth.AuthUserDetails;
import auth.db.AuthLoginDatabase;
import infra.db.Database;

public interface UserDatabase extends Database<AuthUserDetails>, AuthLoginDatabase {
}
