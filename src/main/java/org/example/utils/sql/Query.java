package org.example.utils.sql;

import java.util.Objects;

public final class Query {
    private final String sql;
    private final QueryType type;

    Query(String sql, QueryType type) {
        this.sql = sql;
        this.type = type;
    }

    public String sql() {
        return sql;
    }

    public QueryType type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Query) obj;
        return Objects.equals(this.sql, that.sql) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sql, type);
    }

    @Override
    public String toString() {
        return sql;
    }
}
