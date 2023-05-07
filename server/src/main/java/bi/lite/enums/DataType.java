package bi.lite.enums;

import static cn.hutool.core.text.CharSequenceUtil.isEmpty;

import cn.hutool.core.date.DateUtil;
import com.github.sisyphsu.dateparser.DateParserUtils;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * @author lipengpeng
 */
@Slf4j
public enum DataType {
    /**
     * 字符串
     */
    STRING(Names.STRING),
    /**
     * 数字
     */
    NUMBER(Names.NUMBER),
    /**
     * 日期
     */
    DATE(Names.DATE);

    private static final DefaultDataType<String> TEXT = new DefaultDataType<>(null, String.class, "text");
    private static final Map<Class<?>, DataType> MAPPING = new HashMap<>();
    private static final String UNSUPPORTED_DATA_TYPE = "unsupported data type: ";
    private static final LocalDateTime TIME_MIN = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    private static final LocalDateTime TIME_MAX = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    static {
        // NUMBER
        DataType.MAPPING.put(Byte.class, DataType.NUMBER);
        DataType.MAPPING.put(Short.class, DataType.NUMBER);
        DataType.MAPPING.put(Integer.class, DataType.NUMBER);
        DataType.MAPPING.put(Long.class, DataType.NUMBER);
        DataType.MAPPING.put(BigInteger.class, DataType.NUMBER);
        DataType.MAPPING.put(Float.class, DataType.NUMBER);
        DataType.MAPPING.put(Double.class, DataType.NUMBER);
        DataType.MAPPING.put(BigDecimal.class, DataType.NUMBER);
        DataType.MAPPING.put(UByte.class, DataType.NUMBER);
        DataType.MAPPING.put(UShort.class, DataType.NUMBER);
        DataType.MAPPING.put(UInteger.class, DataType.NUMBER);
        DataType.MAPPING.put(ULong.class, DataType.NUMBER);

        // DATE
        DataType.MAPPING.put(Date.class, DataType.DATE);
        DataType.MAPPING.put(Time.class, DataType.DATE);
        DataType.MAPPING.put(Timestamp.class, DataType.DATE);
        DataType.MAPPING.put(LocalDate.class, DataType.DATE);
        DataType.MAPPING.put(LocalTime.class, DataType.DATE);
        DataType.MAPPING.put(LocalDateTime.class, DataType.DATE);
        DataType.MAPPING.put(Instant.class, DataType.DATE);
        DataType.MAPPING.put(OffsetDateTime.class, DataType.DATE);
        DataType.MAPPING.put(OffsetTime.class, DataType.DATE);

        // others are STRING type
        DataType.MAPPING.put(String.class, DataType.STRING);
        DataType.MAPPING.put(Boolean.class, DataType.STRING);
    }

    DataType(final String name) {
        if (!Objects.equals(this.name(), name)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 将 JOOQ的数据类型，转为BI的数据类型
     *
     * @param dataType JOOQ的数据类型
     * @return BI的数据类型
     */
    public static DataType valueOf(final org.jooq.DataType<?> dataType) {
        return DataType.MAPPING.getOrDefault(dataType.getType(), DataType.STRING);
    }

    /**
     * 将源数据转换为BI中的数据格式，转出为null，BigDecimal，String，LocalDateTime这4种类型
     *
     * @param obj  要转的对象，可为null
     * @param type 目标数据类型，不能为空
     * @return 转之后的对象，类型为null，BigDecimal，String，LocalDateTime之一
     */
    @Nullable
    public static Object parse(@Nullable final Object obj, @NonNull final DataType type) {
        if (obj != null) {
            return switch (type) {
                case NUMBER -> DataType.toNum(obj);
                case STRING -> DataType.toStr(obj);
                case DATE -> DataType.standardTime(DataType.toDate(obj));
                default -> throw new UnsupportedOperationException(DataType.UNSUPPORTED_DATA_TYPE + type);
            };
        } else {
            return null;
        }
    }

    private static BigDecimal toNum(final Object obj) {
        if (obj instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        } else {
            return new BigDecimal(String.valueOf(obj));
        }
    }

    private static String toStr(final Object obj) {
        if (obj instanceof String str) {
            return str;
        } else if (obj instanceof BigDecimal) {
            //jooq 查询出来的bigdecimal 是带所有的小数点的，所以在转字符串的时候依然带着小数点后10个零，这里需要转换一下
            DecimalFormat decimalFormat = new DecimalFormat("####################.##########");
            return decimalFormat.format(obj);
        } else {
            return String.valueOf(obj);
        }
    }

    private static LocalDateTime standardTime(final LocalDateTime time) {
        if (null == time) {
            return null;
        }
        if (time.isBefore(DataType.TIME_MIN) || time.isAfter(DataType.TIME_MAX)) {
            return DataType.TIME_MIN;
        }
        return time;
    }

    private static LocalDateTime toDate(final Object obj) {
        if (obj instanceof LocalDateTime ldt) {
            return ldt;
        } else if (obj instanceof OffsetDateTime odt) {
            return odt.toLocalDateTime();
        } else if (obj instanceof ZonedDateTime zdt) {
            return zdt.toLocalDateTime();
        } else if (obj instanceof Instant instant) {
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } else if (obj instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        } else if (obj instanceof java.sql.Date date) {
            return date.toLocalDate().atStartOfDay();
        } else if (obj instanceof java.util.Date date) {
            return new Timestamp(date.getTime()).toLocalDateTime();
        } else if (obj instanceof LocalDate ld) {
            return ld.atStartOfDay();
        } else if (obj instanceof Long l) {
            return new Timestamp(l).toLocalDateTime();
        } else if (obj instanceof String str) {
            if (isEmpty(str)) {
                return null;
            } else {
                return DateParserUtils.parseDateTime((String) obj);
            }
        } else {
            throw new UnsupportedOperationException("unsupported class: " + obj.getClass().getName());
        }
    }

    /**
     * 数据类型转换
     *
     * @param obj      转换的数据，BigDecimal, String, LocalDateTime之一，只接受这3种类型
     * @param fromType 原始类型
     * @param toType   转换后的类型
     * @return 转换后的对象，不能转时返回null
     */
    @Nullable
    public static Object transDataType(@Nullable final Object obj, @NonNull final DataType fromType,
        @NonNull final DataType toType) {
        if (obj != null) {
            return switch (fromType) {
                case NUMBER -> DataType.transNumber(obj, toType);
                case STRING -> DataType.transString(obj, toType);
                case DATE -> DataType.transDate(obj, toType);
                default -> throw new UnsupportedOperationException(DataType.UNSUPPORTED_DATA_TYPE + fromType);
            };
        } else {
            return null;
        }
    }

    private static Serializable transNumber(final Object obj, final DataType toType) {
        if (obj instanceof final BigDecimal bd) {
            return switch (toType) {
                case NUMBER -> bd;
                case STRING -> bd.toString();
                case DATE -> new Timestamp(bd.longValue()).toLocalDateTime();
                default -> throw new UnsupportedOperationException(DataType.UNSUPPORTED_DATA_TYPE + toType);
            };
        } else {
            throw new IllegalArgumentException(obj.toString());
        }
    }

    private static Serializable transString(final Object obj, final DataType toType) {
        if (obj instanceof final String str) {
            switch (toType) {
                case NUMBER -> {
                    try {
                        return new BigDecimal(str);
                    } catch (final NumberFormatException e) {
                        DataType.log.debug("\"{}\" 转换为 Number 失败, 返回预制处理结果 : {}", str, null);
                        return null;
                    }
                }
                case STRING -> {
                    return str;
                }
                case DATE -> {
                    try {
                        return DateParserUtils.parseDateTime(str);
                    } catch (final DateTimeParseException e) {
                        DataType.log.debug("\"{}\" 转换为 DateTime 失败, 返回预制处理结果 : {}", str, null);
                        return null;
                    }
                }
                default -> throw new UnsupportedOperationException(DataType.UNSUPPORTED_DATA_TYPE + toType);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static Object transDate(final Object obj, final DataType toType) {
        if (obj instanceof final LocalDateTime ldt) {
            return switch (toType) {
                case NUMBER -> Timestamp.valueOf(ldt).getTime();
                case STRING -> DateUtil.formatLocalDateTime(ldt);
                case DATE -> ldt;
                default -> throw new UnsupportedOperationException(DataType.UNSUPPORTED_DATA_TYPE + toType);
            };
        } else {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("rawtypes")
    public org.jooq.DataType dataType() {
        return switch (this) {
            case NUMBER -> SQLDataType.DECIMAL(30, 10);
            case STRING -> DataType.TEXT;
            case DATE -> SQLDataType.TIMESTAMPWITHTIMEZONE;
            default -> throw new UnsupportedOperationException("unsupported data type");
        };
    }

    public static class Names {

        public static final String STRING = "STRING";
        public static final String NUMBER = "NUMBER";
        public static final String DATE = "DATE";
        public static final String BOOL = "BOOL";
        public static final String DATE_TIME = "DATE_TIME";

        private Names() {
        }
    }

}
