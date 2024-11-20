package com.satoru.pdfadmin.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import java.sql.Types;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super(DatabaseVersion.make(3, 0));
        registerDefaultKeywords();
//        registerColumnTypes(Types.BLOB, "blob");
//        registerColumnType(Types.VARCHAR, "text");
//        registerColumnType(Types.INTEGER, "integer");
//        registerColumnType(Types.FLOAT, "float");
    }

    @Override
    public Boolean supportsBatchUpdates() {
        return true;
    }

}
