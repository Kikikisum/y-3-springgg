package com.example.y3spring.jdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomSQLExceptionTranslatorRegistry {
    private static final Log logger = LogFactory.getLog(CustomSQLExceptionTranslatorRegistry.class);
    
    private static final CustomSQLExceptionTranslatorRegistry instance = new CustomSQLExceptionTranslatorRegistry();

    private final Map<String, SQLExceptionTranslator> translatorMap = new HashMap<>();

    public static CustomSQLExceptionTranslatorRegistry getInstance() {
        return instance;
    }

    /**
     * 为指定的数据库名称注册一个新的自定义转换器
     * @param dbName
     * @param translator
     */
    public void registerTranslator(String dbName, SQLExceptionTranslator translator) {
        SQLExceptionTranslator replaced = translatorMap.put(dbName, translator);
        if (replaced != null) {
            logger.warn("Replacing custom translator [" + replaced + "] for database '" + dbName +
                    "' with [" + translator + "]");
        }
        else {
            logger.info("Adding custom translator of type [" + translator.getClass().getName() +
                    "] for database '" + dbName + "'");
        }
    }

    /**
     * 为指定的数据库查找自定义转换器
     * @param dbName
     * @return
     */
    @Nullable
    public SQLExceptionTranslator findTranslatorForDatabase(String dbName) {
        return this.translatorMap.get(dbName);
    }
}
