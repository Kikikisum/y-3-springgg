package com.example.y3spring.transaction.support;

import com.example.y3spring.transaction.TransactionDefinition;
import org.springframework.core.Constants;
import org.springframework.lang.Nullable;

public class DefaultTransactionDefinition implements TransactionDefinition {
    public static final String PREFIX_PROPAGATION = "PROPAGATION_";

    public static final String PREFIX_ISOLATION = "ISOLATION_";

    public static final String PREFIX_TIMEOUT = "timeout_";

    public static final String READ_ONLY_MARKER = "readOnly";

    static final Constants constants = new Constants(TransactionDefinition.class);

    private int propagationBehavior = PROPAGATION_REQUIRED;

    private int isolationLevel = ISOLATION_DEFAULT;

    private int timeout = TIMEOUT_DEFAULT;

    private boolean readOnly = false;

    @Nullable
    private String name;


    public DefaultTransactionDefinition() {
    }

    public DefaultTransactionDefinition(TransactionDefinition other) {
        this.propagationBehavior = other.getPropagationBehavior();
        this.isolationLevel = other.getIsolationLevel();
        this.timeout = other.getTimeout();
        this.readOnly = other.isReadOnly();
        this.name = other.getName();
    }

    public DefaultTransactionDefinition(int propagationBehavior) {
        this.propagationBehavior = propagationBehavior;
    }

    @Override
    public final int getPropagationBehavior() {
        return this.propagationBehavior;
    }


    @Override
    public final int getIsolationLevel() {
        return this.isolationLevel;
    }

    public final void setTimeout(int timeout) {
        if (timeout < TIMEOUT_DEFAULT) {
            throw new IllegalArgumentException("超时时间不能小于默认时间");
        }
        this.timeout = timeout;
    }

    @Override
    public final int getTimeout() {
        return this.timeout;
    }

    public final void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    public final void setName(String name) {
        this.name = name;
    }

    @Override
    @Nullable
    public final String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return (this == other || (other instanceof TransactionDefinition && toString().equals(other.toString())));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return getDefinitionDescription().toString();
    }

    // 照搬源码
    protected final StringBuilder getDefinitionDescription() {
        StringBuilder result = new StringBuilder();
        result.append(constants.toCode(this.propagationBehavior, PREFIX_PROPAGATION));
        result.append(',');
        result.append(constants.toCode(this.isolationLevel, PREFIX_ISOLATION));
        if (this.timeout != TIMEOUT_DEFAULT) {
            result.append(',');
            result.append(PREFIX_TIMEOUT).append(this.timeout);
        }
        if (this.readOnly) {
            result.append(',');
            result.append(READ_ONLY_MARKER);
        }
        return result;
    }
}
