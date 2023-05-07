package bi.lite.util;

import cn.hutool.extra.spring.SpringUtil;
import org.jooq.DSLContext;

/**
 * @author lipengpeng
 */
public class DslUtil {

    private DslUtil() {
    }

    public static DSLContext dslContext() {
        return SpringUtil.getBean(DSLContext.class);
    }
}
