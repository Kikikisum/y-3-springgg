package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.*;
import org.springframework.lang.Nullable;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SQLStateSQLExceptionTranslator extends AbstractFallbackSQLExceptionTranslator{

    private static final Set<String> BAD_SQL_GRAMMAR_CODES = new HashSet<>(8);

    private static final Set<String> DATA_INTEGRITY_VIOLATION_CODES = new HashSet<>(8);

    private static final Set<String> DATA_ACCESS_RESOURCE_FAILURE_CODES = new HashSet<>(8);

    private static final Set<String> TRANSIENT_DATA_ACCESS_RESOURCE_CODES = new HashSet<>(8);

    private static final Set<String> CONCURRENCY_FAILURE_CODES = new HashSet<>(4);


    static {
        BAD_SQL_GRAMMAR_CODES.add("07");
        BAD_SQL_GRAMMAR_CODES.add("21");
        BAD_SQL_GRAMMAR_CODES.add("2A");
        BAD_SQL_GRAMMAR_CODES.add("37");
        BAD_SQL_GRAMMAR_CODES.add("42");
        BAD_SQL_GRAMMAR_CODES.add("65");

        DATA_INTEGRITY_VIOLATION_CODES.add("01");
        DATA_INTEGRITY_VIOLATION_CODES.add("02");
        DATA_INTEGRITY_VIOLATION_CODES.add("22");
        DATA_INTEGRITY_VIOLATION_CODES.add("23");
        DATA_INTEGRITY_VIOLATION_CODES.add("27");
        DATA_INTEGRITY_VIOLATION_CODES.add("44");

        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("08");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("53");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("54");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("57");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("58");

        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JW");
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JZ");
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("S1");

        CONCURRENCY_FAILURE_CODES.add("40");
        CONCURRENCY_FAILURE_CODES.add("61");
    }

    /**
     * 还未实现
     * @param task 正在尝试的任务的可读文本
     * @param sql 导致问题的sql查询或更新
     * @param ex
     * @return
     */
    @Override
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        String sqlState = getSqlState(ex);
        if (sqlState != null && sqlState.length() >= 2) {
            String classCode = sqlState.substring(0, 2);
            if (logger.isDebugEnabled()) {
                logger.debug("Extracted SQL state class '" + classCode + "' from value '" + sqlState + "'");
            }
            if (BAD_SQL_GRAMMAR_CODES.contains(classCode)) {
                return new BadSqlGrammarException(task, (sql != null ? sql : ""), ex);
            }
            else if (DATA_INTEGRITY_VIOLATION_CODES.contains(classCode)) {
                return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
            }
            else if (DATA_ACCESS_RESOURCE_FAILURE_CODES.contains(classCode)) {
                return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
            }
            else if (TRANSIENT_DATA_ACCESS_RESOURCE_CODES.contains(classCode)) {
                return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
            }
            else if (CONCURRENCY_FAILURE_CODES.contains(classCode)) {
                return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
            }
        }

        return null;
    }

    @Nullable
    private String getSqlState(SQLException ex) {
        String sqlState = ex.getSQLState();
        if (sqlState == null) {
            SQLException nestedEx = ex.getNextException();
            if (nestedEx != null) {
                sqlState = nestedEx.getSQLState();
            }
        }
        return sqlState;
    }
}
