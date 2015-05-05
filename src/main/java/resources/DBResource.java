package resources;

/**
 * Created by mihanik
 * 23.04.15 11:09
 * Package: resources
 */
@SuppressWarnings("UnusedDeclaration")
public class DBResource implements Resource {
    private String dbURL, dbUser, dbPassword, dbName, dbTestName, dbDialect, dbDriverClassName, showSql, hbm2DdlAuto, flushMode;

    public String getDbURL() {
        return dbURL;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbTestName() {
        return dbTestName;
    }

    public String getDbDialect() {
        return dbDialect;
    }

    public String getDbDriverClassName() {
        return dbDriverClassName;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getHbm2DdlAuto() {
        return hbm2DdlAuto;
    }

    public String getFlushMode() {
        return flushMode;
    }

}
