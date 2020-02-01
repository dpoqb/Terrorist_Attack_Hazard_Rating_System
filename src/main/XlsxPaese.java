package main;

/**https://commons.apache.org/
 https://commons.apache.org/proper/commons-beanutils/index.html
 */
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class XlsxPaese {
    /**
     *
     * @param filePath excel文件路径
     * @param cellKey excel文件每列对应实体类属性名称数组 如String[] cellKey = {"id","province","city","district","postcode"};
     * @param c 实体类Class
     * @return 根据excel表每行的记录生成的实体类list
     * @throws Exception
     */
    public <T>List<T> getBeanList(String filePath,String[] cellKey,Class<T> c) throws Exception{
        //创建实体类对象容器
        List<T> beanList = new ArrayList<T>();
        //读取excel文件
        POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(filePath));
        @SuppressWarnings("resource")
        HSSFWorkbook xlsFile = new HSSFWorkbook(fs);
        HSSFSheet sheet = xlsFile.getSheetAt(0);
        //判断属性列表同excel文件列是否相符，避免数组范围溢出
        if(cellKey.length!=sheet.getRow(0).getLastCellNum()) {
            throw new RuntimeException("传入列名参数不符合条件！");
        }
        //创建属性map
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        //遍历excel文件
        //========下面在遍历过程需要考虑数据类型========
        for (int r=1;r<=sheet.getLastRowNum();r++) {
            Row row = sheet.getRow(r);
            for(int i=0;i<cellKey.length;i++) {
                if(row.getCell(i) == null){
                    valueMap.put(cellKey[i], null);
                }
                /**NumberToTextConverter.toText() some number should serve as text
                 * for example: filed id,it should be a text,otherwise,will produce a unnormal format
                 * 199801010001 , 1.99801010001E11
                 * */
                else{
                    if(row.getCell(i).getColumnIndex()!=0 && row.getCell(i).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                        valueMap.put(cellKey[i], row.getCell(i).getNumericCellValue());
                    }
                    else if(row.getCell(i).getColumnIndex()==0 && row.getCell(i).getRowIndex()!=0){
                        valueMap.put(cellKey[i],NumberToTextConverter.toText(row.getCell(i).getNumericCellValue()));
                    }
                    else
                        valueMap.put(cellKey[i], row.getCell(i).getStringCellValue());
                }
            }
            T t = c.newInstance();
            //使用BeanUtils将封装的属性注入对象
            BeanUtils.populate(t, valueMap);
            //将对象添加至容器
            beanList.add(t);
        }
        return beanList;
    }
}
