package top.jadeyan.commons.object;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * 字符串扩展工具类
 *
 * @author yan
 */
public final class StringExtensionUtils {


    private static final String DOMAIN_PREFIX = "www.";

    public static final String DEFAULT_EMPTY = "--";

    public static final String MEANINGLESS_CHARS = "\\s*|\r|\n|\t";

    private StringExtensionUtils() {
        // hide construct
    }

    /**
     * 获取第一个不为空的字符串
     *
     * @param strings 字符串数组
     * @return 第一个不为空的字符串
     */
    public static Optional<String> getFirstNotBlank(String... strings) {
        if (ArrayUtils.isEmpty(strings)) {
            return Optional.empty();
        }
        for (String string : strings) {
            if (StringUtils.isNotBlank(string)) {
                return Optional.of(string);
            }
        }
        return Optional.empty();
    }

    /**
     * 从url 中获取domain
     * <p>
     * eg: http://www.google.com/test  -> google.com
     * eg: www.google.com/test -> google.com
     * eg: http://192.168.1.1/test -> 192.168.1.1
     *
     * @param url url 地址
     * @return domain 域名
     */
    @SuppressWarnings("squid:S1166")
    public static Optional<String> getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if (StringUtils.isBlank(domain)) {
                // fix relative path(without http) eg: www.google.com
                domain = url;
            }
            domain = domain.startsWith(DOMAIN_PREFIX) ? domain.substring(DOMAIN_PREFIX.length()) : domain;
            return Optional.of(domain);
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * 对象toString
     *
     * @param object 对象
     * @return toSafeString 字符串
     */
    public static String toSafeString(Object object) {
        return toSafeString(object, false);
    }

    /**
     * 对象toString
     *
     * @param object      对象
     * @param nullToEmpty null 变成空
     * @return toSafeString 字符串
     */
    @SuppressWarnings("squid:S2301")
    public static String toSafeString(Object object, boolean nullToEmpty) {
        if (Objects.nonNull(object)) {
            return object.toString();
        }
        if (nullToEmpty) {
            return "";
        } else {
            return null;
        }
    }

    /**
     * 对象toString
     *
     * @param object       对象
     * @param defaultValue 默认值 如果object == null
     * @return toSafeString 字符串
     */
    public static String toSafeString(Object object, String defaultValue) {
        if (Objects.nonNull(object)) {
            return object.toString();
        }
        return defaultValue;
    }

    /**
     * 目标字符串如果为空（包括空格），用defaultValue， 不是直接用target
     *
     * @param target       目标字符串
     * @param defaultValue 默认值
     * @return 目标字符串如果为空（包括空格），用defaultValue，  不是直接用target
     */
    public static String blankToDefault(String target, String defaultValue) {
        if (StringUtils.isBlank(target)) {
            return defaultValue;
        }
        return target;
    }

    /**
     * 目标字符串如果为空（不包括空格），用defaultValue，  不是直接用target
     *
     * @param target       目标字符串
     * @param defaultValue 默认值
     * @return 目标字符串如果为空（不包括空格），用defaultValue，  不是直接用target
     */
    public static String emptyToDefault(String target, String defaultValue) {
        if (StringUtils.isEmpty(target)) {
            return defaultValue;
        }
        return target;
    }

    /**
     * 如果不为空（包括空格）则运行消费者
     *
     * @param target   目标字符串
     * @param consumer 消费者
     */
    public static void ifNotBlank(String target, Consumer<String> consumer) {
        if (StringUtils.isNotBlank(target)) {
            consumer.accept(target);
        }
    }

    /**
     * 如果不为空（包括空格）则运行消费者
     *
     * @param target   目标字符串
     * @param consumer 消费者
     */
    public static void ifNotEmpty(String target, Consumer<String> consumer) {
        if (StringUtils.isNotEmpty(target)) {
            consumer.accept(target);
        }
    }

    /**
     * 多个Object对象join 为空则跳过
     *
     * @param split   连接符
     * @param objects 对象数组
     * @return 拼接结果
     */
    public static String objectJoinWith(String split, Object... objects) {
        if (Objects.isNull(objects)) {
            return null;
        }
        StringJoiner stringJoiner = new StringJoiner(split);
        for (Object object : objects) {
            if (Objects.isNull(object)) {
                continue;
            }
            stringJoiner.add(object.toString());
        }
        return stringJoiner.toString();
    }

    /**
     * 多个字符串有序拼接成一个字符串
     *
     * @param stringList 字符串
     * @return 拼接后字符串
     */
    public static String listToString(List<String> stringList) {
        if (CollectionUtils.isEmpty(stringList)) {
            return "";
        }
        // 排序
        return stringList.stream().sorted().map(String::valueOf).collect(Collectors.joining("|"));
    }

    /**
     * 检查多个字符串是否都不为空 或长度为0 或由空白符构成
     *
     * @param strings 字符串数组
     * @return 是否都不为空
     */
    public static boolean allNotBlank(String... strings) {
        if (ArrayUtils.isEmpty(strings)) {
            return false;
        }
        for (String string : strings) {
            if (StringUtils.isBlank(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将多个key组合为一个key字符串
     *
     * @param delimiter the sequence of characters to be used between each
     *                  element added to the {@code StringJoiner} value
     * @param keys      多个key值
     * @return 组合生成的key字符串
     */
    public static String complexKey(CharSequence delimiter, Object... keys) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (Object key : keys) {
            joiner.add(null == key ? null : key.toString());
        }
        return joiner.toString();
    }

    /**
     * 将字符串中的特殊空格替换掉, 目前主要是中文括号替换成英文括号
     *
     * @param str 待处理文本
     * @return 处理后文本
     */
    public static String replaceNameSpecialChar(String str) {
        if (StringUtils.isBlank(str)) {
            return DEFAULT_EMPTY;
        }
        return StringUtils.replaceEach(str.trim(), new String[]{"（", "）", "【", "】", "，"}, new String[]{"(", ")", "[", "]", ","}).replaceAll(MEANINGLESS_CHARS, EMPTY);
    }

    /**
     * 搜索关键字限制, 页面传入关键字大于100个时候，取前面100个
     */
    private static final int KEYWORD_CHAR_HUNDRED = 100;

    /**
     * 截取前100个字符, 为空返回空字符串
     *
     * @param inputString 输入关键字
     * @return 前100个字符
     */
    public static String cutOutHundredLengthString(String inputString) {
        // 不修改入参，新建一个字符串
        String searchKeyWord = StringUtils.isBlank(inputString) ? "" : inputString.trim();
        if (StringUtils.isNotBlank(searchKeyWord) && searchKeyWord.length() > KEYWORD_CHAR_HUNDRED) {
            searchKeyWord = searchKeyWord.substring(0, KEYWORD_CHAR_HUNDRED);
        }
        return searchKeyWord;
    }

}
