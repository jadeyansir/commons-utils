package top.jadeyan.commons.easyExcel;

import top.jadeyan.commons.excelutils.EasyExcelUtil;
import top.jadeyan.commons.excelutils.ErrorMsg;
import top.jadeyan.commons.excelutils.ExcelImportResult;
import top.jadeyan.commons.exception.ExcelRuntimeException;
import top.jadeyan.commons.model.ExcelTestBO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class easyExcelUtilsTest {


    /**
     * 导入Excel测试，前端传入指定文件
     * 此工具类提供分批处理与校验数据的功能，动态表头与异步暂不支持
     * predicate接收的是位于表达式，可用于校验文件属性值，如果不满足会封装到errorMsg中
     * 注意：此方法不会中断，会排除失败或不满足的，返回结果之后正确的，如果不使用此种方式，建议根据errorMsg进行异常处理
     *
     * @throws IOException IOException
     * @see EasyExcelUtil#importExcel(MultipartFile, Class, Predicate, BiConsumer)
     */
    @Test
    public void testForImport() throws IOException {
        File file = new File("src/excelFile/test1.xlsx");
        MultipartFile fileItem = createFileItem(file);
        ExcelImportResult<ExcelTestBO> result =
                EasyExcelUtil.importExcel(fileItem, ExcelTestBO.class, this::checkBO, this::dealWithExcelData);
        // 满足条件或满足校验的结果
        List<ExcelTestBO> successResult = result.getResult();
        // 校验失败的数据
        ErrorMsg errorMsg = result.getErrorMsg();
        // 有一条失败全部不保存的处理
        if (!CollectionUtils.isEmpty(errorMsg.getMsgInfo())) {
            throw new ExcelRuntimeException("有失败数据，请检查数据");
        }
        // 处理正确数据
        System.out.println(successResult);
        assertNotNull(result.getResult());
    }

    /**
     * 检查字段
     *
     * @param excelTestBO
     * @return
     */
    private Boolean checkBO(ExcelTestBO excelTestBO) {
        if (ObjectUtils.isEmpty(excelTestBO)) {
            //处理某个字段为空抛出异常 ,校验出BusinessException异常，会被捕获，并加到errorMsg中
            throw new ExcelRuntimeException("异常");
        } else if (ObjectUtils.isEmpty(excelTestBO)) {
            // 抛出其他异常会中断操作，注意：如果是分批存储，在dao上加事务，已经存储的可能不会回滚要注意处理
            throw new RuntimeException("异常");
        } else if (ObjectUtils.isEmpty(excelTestBO)) {
            // 如果返回false也会将数据剔除
            return false;
        }
        // 如果返回true，表示是正确数据加载到结果中
        return true;
    }

    /**
     * 处理业务逻辑
     *
     * @param excelTestBOList
     * @param errorInfo
     */
    private void dealWithExcelData(List<ExcelTestBO> excelTestBOList, ErrorMsg errorInfo) {
        // 分批处理的逻辑，可用于防止oom
        assertEquals(2, excelTestBOList.size());
    }

    /**
     * 模拟前端上传文件
     *
     * @param file
     * @return
     */
    private MultipartFile createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonsMultipartFile(item);
    }
}
