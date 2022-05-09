package top.jadeyan.commons.excelutils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.util.ClassUtils;
import top.jadeyan.commons.exception.ExcelRuntimeException;
import top.jadeyan.commons.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * EasyExcel 工具类
 *
 * @author : yan
 * @date : 2021/11/02
 */
public final class EasyExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelUtil.class);
    private static final Integer DEFAULT_EXCEL_NUMBER = 2000;
    /**
     * 一次最多导入数量
     */
    private static final Integer MAX_EXCEL_NUMBER = 10000;
    private static final String EXCEL_MARK = ".";
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";
    private static final String IMPORT_DATA_TOO_MUCH = "导入数量过多";
    private static final String EXCEL_HEAD_ERROR = "表头错误";
    private static final String PARAM_ERROR = "入参定义错误";

    private EasyExcelUtil() {

    }

    /**
     * 获取AnalysisEventListener
     *
     * @param consumer  插入逻辑
     * @param threshold 阈值
     * @param <T>       需要解析的Excel实体类
     * @return AnalysisEventListener
     */
    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer, int threshold) {
        return new AnalysisEventListener<T>() {
            private List<T> dataList = new LinkedList<>();

            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                dataList.add(t);
                if (dataList.size() >= threshold) {
                    consumer.accept(dataList);
                    dataList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (!CollectionUtils.isEmpty(dataList)) {
                    consumer.accept(dataList);
                }
            }
        };
    }

    /**
     * 导入处理
     *
     * @param file               导入的文件
     * @param source             目标class
     * @param checkProperty      校验的函数
     * @param deal               当总数小于batchDealThreshold的数据只会调用一次,如果有则调用，没有则不调用
     *                           当列表的正确的数据超过等于batchDealThreshold时,该方法将在每次读取到正确的batchDealThreshold条数据时触发调用,如果没有则抛异常
     * @param uploadLimitSize    上传数量限制 NOTNULL
     * @param batchDealThreshold 触发批量的阈值 NOTNULL 触发阈值限制不大于 DEFAULT_EXCEL_NUMBER ,大事务可能导致事务堵塞
     * @param <T>                泛型
     * @return 导入结果
     * @throws IOException IO异常
     */
    @SuppressWarnings("squid:S1188")
    public static <T> ExcelImportResult<T> importExcel(MultipartFile file, Class<T> source, Predicate<T> checkProperty,
            BiConsumer<List<T>, ErrorMsg> deal, int uploadLimitSize, int batchDealThreshold) throws IOException {
        List<T> result = new ArrayList<>();
        ErrorMsg errorMsg = new ErrorMsg(0, new ArrayList<>());
        ExcelImportResult<T> excelResult = new ExcelImportResult<>();
        checkFileFormat(file);
        processImport(file.getInputStream(), source, checkProperty, deal, uploadLimitSize, batchDealThreshold, result, errorMsg);
        excelResult.setResult(result);
        excelResult.setErrorMsg(errorMsg);
        return excelResult;
    }


    /**
     * 导入处理
     *
     * @param in                 导入的文件字节流
     * @param source             目标class
     * @param checkProperty      校验的函数
     * @param deal               当总数小于batchDealThreshold的数据只会调用一次,如果有则调用，没有则不调用
     *                           当列表的正确的数据超过等于batchDealThreshold时,该方法将在每次读取到正确的batchDealThreshold条数据时触发调用,如果没有则抛异常
     * @param uploadLimitSize    上传数量限制 NOTNULL
     * @param batchDealThreshold 触发批量的阈值 NOTNULL 触发阈值限制不大于 DEFAULT_EXCEL_NUMBER ,大事务可能导致事务堵塞
     * @param <T>                泛型
     * @return 导入结果
     */
    @SuppressWarnings("squid:S1188")
    public static <T> ExcelImportResult<T> importExcel(InputStream in, Class<T> source, Predicate<T> checkProperty,
                                                       BiConsumer<List<T>, ErrorMsg> deal, int uploadLimitSize,
                                                       int batchDealThreshold) {
        List<T> result = new ArrayList<>();
        ErrorMsg errorMsg = new ErrorMsg(0, new ArrayList<>());
        ExcelImportResult<T> excelResult = new ExcelImportResult<>();
        processImport(in, source, checkProperty, deal, uploadLimitSize, batchDealThreshold, result, errorMsg);
        excelResult.setResult(result);
        excelResult.setErrorMsg(errorMsg);
        return excelResult;
    }

    @SuppressWarnings("squid:S1188")
    private static <T> void processImport(InputStream in, Class<T> source, Predicate<T> checkProperty,
                                          BiConsumer<List<T>, ErrorMsg> deal, int uploadLimitSize, int batchDealThreshold,
                                          List<T> result, ErrorMsg errorMsg) {
        try {
            EasyExcel.read(in, source, new AnalysisEventListener<T>() {

                @Override
                public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                    checkExcelHead(headMap, source);
                    checkExcelLimit(context, errorMsg, uploadLimitSize, batchDealThreshold);
                }

                @Override
                public void invoke(T data, AnalysisContext context) {
                    Field rowNumber = ReflectionUtils.findField(source, "rowNumber");
                    Integer rowIndex = context.readRowHolder().getRowIndex();
                    if (rowNumber != null) {
                        ReflectionUtils.makeAccessible(rowNumber);
                        ReflectionUtils.setField(rowNumber, data, rowIndex);
                    }
                    // 处理自定义判断逻辑
                    try {
                        if (checkProperty != null && !checkProperty.test(data)) {
                            dealErrorMessage(errorMsg, rowIndex, "未满足约定条件！");
                        } else {
                            result.add(data);
                        }
                    } catch (Exception e) {
                        dealErrorMessage(errorMsg, rowIndex, e.getMessage());
                        LOGGER.info(e.getMessage(), e);
                    }
                    // 分批处理防止OOM
                    dealImportData(errorMsg, result, deal, batchDealThreshold);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    checkFileResult(errorMsg, deal, result);
                    LOGGER.info(String.format("所有数据读取完毕，包含异常有：%s", JSON.toJSONString(errorMsg.getMsgInfo())));
                }

                @Override
                public void onException(Exception exception, AnalysisContext context) {
                    if (exception instanceof ExcelRuntimeException) {
                        throw (ExcelRuntimeException) exception;
                    }
                    try {
                        super.onException(exception, context);
                    } catch (Exception ex) {
                        LOGGER.info(String.format("所有数据读取完毕，包含异常有：%s", ex));
                        dealErrorMessage(errorMsg, context.readRowHolder().getRowIndex(), exception.getMessage());
                    }
                }
            })
                    .registerConverter(new ExcelDateConvert())
                    .registerConverter(new ExcelSqlDateConvert())
                    .registerConverter(new StringToSqlDateConvert()).sheet().doRead();
        } catch (ExcelAnalysisException e) {
            LOGGER.info(String.format("捕获所有ExcelAnalysisException异常:%s", e));
            throw new ExcelAnalysisException();
        }
    }

    @SuppressWarnings("squid:CallToDeprecatedMethod")
    private static void checkExcelLimit(AnalysisContext context, ErrorMsg errorMsg, int uploadLimitSize, int batchDealThreshold) {
        if (batchDealThreshold < 0 || batchDealThreshold > DEFAULT_EXCEL_NUMBER || uploadLimitSize < batchDealThreshold) {
            dealErrorMessage(errorMsg, 0, "上传限制值需要大于批量处理触发阈值");
            throw new ExcelRuntimeException(PARAM_ERROR);
        }
        //去除当前第一行的表头
        int totalCount = context.getTotalCount() - 1;
        if (totalCount > uploadLimitSize) {
            dealErrorMessage(errorMsg, 0, "导入数量过多!限制：" + uploadLimitSize + "，文件：" + totalCount);
            throw new ExcelRuntimeException("导入数量过多!限制：" + uploadLimitSize + "，文件：" + totalCount);
        }
    }

    /**
     * 导入处理
     *
     * @param file          导入的文件
     * @param source        目标class
     * @param checkProperty 校验的函数
     * @param deal          当小于2000的数据只会调用一次,如果有则调用，没有则不调用，最大上传限制10000
     *                      当列表的正确的数据超过等于2000时,该方法将在每次读取到正确的2000条数据时触发调用,如果没有则抛异常
     * @param <T>           泛型
     * @return 导入结果
     * @throws IOException IO异常
     */
    @SuppressWarnings("squid:S1188")
    public static <T> ExcelImportResult<T> importExcel(MultipartFile file, Class<T> source, Predicate<T> checkProperty,
                                                       BiConsumer<List<T>, ErrorMsg> deal) throws IOException {
        return importExcel(file, source, checkProperty, deal, MAX_EXCEL_NUMBER, DEFAULT_EXCEL_NUMBER);
    }

    /**
     * 检查数据读取结果
     *
     * @param errorMsg 错误信息
     * @param deal     处理方式
     * @param result   结果集
     */
    private static <T> void checkFileResult(ErrorMsg errorMsg, BiConsumer<List<T>,
            ErrorMsg> deal, List<T> result) {
        if (Objects.nonNull(deal) && !CollectionUtils.isEmpty(result)) {
            deal.accept(result, errorMsg);
            result.clear();
        }
    }

    /**
     * 检查数据读取结果
     *
     * @param headMap  表头
     * @param source   目标Class
     */
    private static <T> void checkExcelHead(Map<Integer, String> headMap, Class<T> source) {
        ClassUtils.declaredFields(source, new ArrayList<>(), true);
        Field[] fields = source.getDeclaredFields();
        Set<String> headSet = Arrays.stream(fields).filter(x -> x.isAnnotationPresent(ExcelProperty.class)).map(x -> {
            ExcelProperty excelProperty = x.getAnnotation(ExcelProperty.class);
            return excelProperty.value()[0];
        }).collect(Collectors.toSet());
        Collection<String> excelHeadList = headMap.values();
        if (headSet.size() != excelHeadList.size()) {
            throw new ExcelRuntimeException(EXCEL_HEAD_ERROR);
        }
        if (!headSet.containsAll(excelHeadList)) {
            throw new ExcelRuntimeException(EXCEL_HEAD_ERROR);
        }
    }

    /**
     * 检查文件格式
     *
     * @param file 文件
     */
    private static void checkFileFormat(MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new ExcelRuntimeException("未能正常接收文件或未传文件");
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf(EXCEL_MARK));
        if (!(EXCEL_XLS.equals(suffix) || EXCEL_XLSX.equals(suffix))) {
            throw new ExcelRuntimeException("文件格式错误，请检测文件格式");
        }
    }

    /**
     * 处理异常信息
     *
     * @param error      错误信息
     * @param lineNumber 错误行数
     * @param errorMsg   错误信息
     */
    private static void dealErrorMessage(ErrorMsg error, Integer lineNumber, String errorMsg) {
        ExcelMsgInfo excelMsgInfo = new ExcelMsgInfo();
        error.setErrorNumber(error.getErrorNumber() + 1);
        excelMsgInfo.setLineNumber(lineNumber);
        excelMsgInfo.setErrorMsg(errorMsg);
        error.getMsgInfo().add(excelMsgInfo);
    }

    /**
     * 分批处理数据
     *
     * @param errorMsg           错误信息
     * @param result             结果数据
     * @param deal               处理方式
     * @param batchDealThreshold 批量处理触发阈值
     * @param <T>                泛型
     */
    private static <T> void dealImportData(
            ErrorMsg errorMsg, List<T> result, BiConsumer<List<T>, ErrorMsg> deal, int batchDealThreshold) {
        if (result.size() == batchDealThreshold) {
            if (Objects.isNull(deal)) {
                dealErrorMessage(errorMsg, 0, "导入数量过多！请减少一次导入量，或者添加分批处理逻辑！");
                throw new ExcelRuntimeException(IMPORT_DATA_TOO_MUCH);
            }
            deal.accept(result, errorMsg);
            result.clear();
        }
    }
}
