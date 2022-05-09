package top.jadeyan.commons.exception;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

/**
 * @author yan
 **/
@SuppressWarnings("all")
public class ExceptionUtilsTest {

    @Test
    public void testParseHttpServerErrorException1() {
        String errMsg1 = "500 : [{\"timestamp\":1650016726775,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"内部错误:\",\"path\":\"/onshore-im-service/api/im/groups\"}]";
        String errMsg2 = "500 [{\"timestamp\":1650016726775,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"内部错误:\",\"path\":\"/onshore-im-service/api/im/groups\"}]";
        String errMsg3 = "{\"timestamp\":1650016726775,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"内部错误:\",\"path\":\"/onshore-im-service/api/im/groups\"}";
        List<String> testStrings = new ArrayList<>();
        testStrings.add(errMsg1);
        testStrings.add(errMsg2);
        testStrings.add(errMsg3);

        for (String errMsg : testStrings) {
            final Optional<ExceptionModel> exceptionModelOptional = ExceptionUtils.parseHttpServerError(errMsg);
            final ExceptionModel exceptionModel = exceptionModelOptional.get();
            assertEquals(1650016726775L, exceptionModel.getTimestamp().longValue());
            assertEquals(500, exceptionModel.getStatus().intValue());
            assertEquals("Internal Server Error", exceptionModel.getError());
            assertEquals("内部错误:", exceptionModel.getMessage());
            assertEquals("/onshore-im-service/api/im/groups", exceptionModel.getPath());
        }
    }
}
