package top.jadeyan.commons.http;

import com.google.common.collect.Iterables;
import top.jadeyan.commons.exception.EncodingRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * http 工具
 *
 * @author yan
 */
public final class HttpUtils {

    private HttpUtils() {
    }

    /**
     * 获取 query 参数
     *
     * @param url url
     * @return query 参数
     */
    public static Map<String, String> getQueryParams(String url) {
        final URI uri = URI.create(url);
        final MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        Map<String, String> result = new LinkedHashMap<>();
        for (String key : queryParams.keySet()) {
            String value = queryParams.getFirst(key);
            if (Objects.nonNull(value)) {
                String decodeValue = decodeURIComponent(value);
                result.put(key, decodeValue);
            }
        }
        return result;
    }

    /**
     * 解码页面元素
     *
     * @param value 值
     * @return 解码页面元素值
     */
    public static String decodeURIComponent(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            String errMsg = "[decodeURIComponent] error. value: " + value;
            throw new EncodingRuntimeException(errMsg, ex);
        }
    }

    /**
     * 编码页面元素
     *
     * @param value 值
     * @return 编码后的值
     */
    public static String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            String errMsg = "[encodeURIComponent] error. value: " + value;
            throw new EncodingRuntimeException(errMsg, ex);
        }
    }

    /**
     * 从header 中获取值
     *
     * @param headers 头
     * @param key     key值
     * @return key 对应value
     */
    public static Optional<String> getValueFromHeaders(Iterable<Map.Entry<String, String>> headers, String key) {
        return getValueFromHeaders(headers, key, true);
    }

    /**
     * 从header 中获取值
     *
     * @param headers       头
     * @param key           key值
     * @param keyIgnoreCase 是否忽略key 大小写
     * @return key 对应value
     */
    @SuppressWarnings("squid:S2301")
    public static Optional<String> getValueFromHeaders(Iterable<Map.Entry<String, String>> headers, String key, boolean keyIgnoreCase) {
        if (Objects.isNull(headers) || Iterables.isEmpty(headers) || StringUtils.isBlank(key)) {
            return Optional.empty();
        }
        for (Map.Entry<String, String> header : headers) {
            if (keyIgnoreCase) {
                if (key.equalsIgnoreCase(header.getKey())) {
                    return Optional.ofNullable(header.getValue());
                }
            } else {
                if (key.equals(header.getKey())) {
                    return Optional.ofNullable(header.getValue());
                }
            }
        }
        return Optional.empty();
    }
}
