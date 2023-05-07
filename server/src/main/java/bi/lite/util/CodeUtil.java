package bi.lite.util;

import cn.hutool.core.util.RandomUtil;

/**
 * @author lipengpeng
 */
public class CodeUtil {

    private CodeUtil() {
    }

    /**
     * 租户编码
     * <p>
     * 格式：1位字母 + 4位字母数字，共5位
     * <p>
     * 26 * 36^4 = 43,670,016
     *
     * @return 租户编码
     */
    public static String tenantCode() {
        return RandomUtil.randomString(RandomUtil.BASE_CHAR, 1) + RandomUtil.randomString(4);
    }

    /**
     * 数据源编码，租户内唯一，是租户内所有表名的前缀，尽量短
     * <p>
     * 格式：1 位字母 + 3 位字母数字，共 4 位
     * <p>
     * 26 * 36^3 = 1,213,056
     *
     * @return 租户编码
     */
    public static String sourceCode() {
        return RandomUtil.randomString(RandomUtil.BASE_CHAR, 1) + RandomUtil.randomString(3);
    }
}
