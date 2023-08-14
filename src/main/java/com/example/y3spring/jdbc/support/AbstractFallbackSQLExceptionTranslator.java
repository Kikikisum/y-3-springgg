package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.UncategorizedSQLException;
import com.example.y3spring.jdbc.exception.DataAccessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.sql.SQLException;

public abstract class AbstractFallbackSQLExceptionTranslator implements SQLExceptionTranslator  {

    protected final Log logger = LogFactory.getLog(getClass());

    @Nullable
    private SQLExceptionTranslator fallbackTranslator;

    public void setFallbackTranslator(@Nullable SQLExceptionTranslator fallback) {
        this.fallbackTranslator = fallback;
    }

    @Nullable
    public SQLExceptionTranslator getFallbackTranslator() {
        return this.fallbackTranslator;
    }


    /**
     * 预先检查参数，随后由DataAccessException的doTranslate方法进行翻译
     * @param task
     * @param sql
     * @param ex
     * @return
     */
    @Override
    @NonNull
    public DataAccessException translate(String task, @Nullable String sql, SQLException ex) {
        Assert.notNull(ex, "Cannot translate a null SQLException");
        DataAccessException dae = doTranslate(task, sql, ex);
        if (dae != null) {
            return dae;
        }
        // 回退翻译器
        SQLExceptionTranslator fallback = getFallbackTranslator();
        if (fallback != null) {
            dae = fallback.translate(task, sql, ex);
            if (dae != null) {
                return dae;
            }
        }
        // 如果回调实现不了，就返回异常
        return new UncategorizedSQLException(task, sql, ex);
    }

    /**
     * 由具体异常类来进行继承续写的翻译
     * @param task 正在尝试的任务的可读文本
     * @param sql 导致问题的sql查询或更新
     * @param ex
     * @return
     */
    @Nullable
    protected abstract DataAccessException doTranslate(String task, @Nullable String sql, SQLException ex);


    /**
     * 由具体的异常类生成一条描述异常的语句，照搬的源码
     * @param task
     * @param sql
     * @param ex
     * @return
     */
    protected String buildMessage(String task, @Nullable String sql, SQLException ex) {
        return task + "; " + (sql != null ? ("SQL [" + sql + "]; ") : "") + ex.getMessage();
    }
}
