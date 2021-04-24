package com.chesterccw.exceldiffer.util;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.chesterccw.exceldiffer.ExcelDifferAction;
import com.chesterccw.exceldiffer.notify.MyNotifier;
import com.intellij.notification.Notification;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author chesterccw
 * @date 2020/7/27
 */
public class ResolveData {

    private final Vector<String> header = new Vector<>();
    private final Vector<Vector<Object>> rows = new Vector<>();
    private final String filePath;
    private final String title;
    private final String extension;
    MyNotifier notifier = new MyNotifier();

    public ResolveData(VirtualFile data){
        this.filePath = data.getPath();
        this.extension = data.getExtension();
        this.title = data.getName();
    }

    public ResolveData(File data){
        if(!data.exists()){
            Notification notify = notifier.notify("File " + data.getPath() + " not exists!");
        }
        this.filePath = data.getPath();
        this.extension = FileUtilRt.getExtension(data.getName());
        this.title = data.getName();
    }

    public ExcelDifferAction.Data resolve(){
        ExcelDifferAction.Data data = new ExcelDifferAction.Data();
        if(StrUtil.isEmpty(filePath) || StrUtil.isEmpty(title) || StrUtil.isEmpty(extension)){
            return data;
        }
        if(Arrays.asList(Constant.SUFFIX_ARRAY).contains(this.extension)){
            if(filePath.endsWith(Constant.SUFFIX_ARRAY[2])){
                CsvReader reader = CsvUtil.getReader();
                CsvData csvData = reader.read(new File(filePath));
                resolve(header,rows,csvData);
            } else {
                ExcelReader reader = ExcelUtil.getReader(filePath);
                resolve(header,rows,reader);
            }
        }
        data.header = this.header;
        data.rows = this.rows;
        data.title = this.title;
        return data;
    }

    private void resolve(Vector<String> header, Vector<Vector<Object>> rows, CsvData csvData){
        List<CsvRow> allRows = csvData.getRows();
        if(allRows.size() == 0){
            return;
        }
        CsvRow headerRow = allRows.get(0);
        if(headerRow == null){
            return;
        }
        List<String> headerList = headerRow.getRawList();
        for(String s : headerList){
            if(StringUtils.isEmpty(s)){
                continue;
            }
            header.add(s);
        }
        for(int i = 1 ; i < allRows.size() ; i++){
            CsvRow csvRow = allRows.get(i);
            if(csvRow == null){
                continue;
            }
            List<String> list = csvRow.getRawList();
            Vector<Object> vector = new Vector<>(list);
            rows.add(vector);
        }
    }

    private void resolve(Vector<String> header, Vector<Vector<Object>> rows,ExcelReader reader){
        List<Map<String,Object>> readAll = reader.readAll();
        boolean outHeader = true;
        for(Map<String,Object> map : readAll){
            for(Map.Entry<String,Object> entry : map.entrySet()){
                if(outHeader){
                    header.add(entry.getKey());
                }
            }
            outHeader = false;
            Vector<Object> vector = new Vector<>();
            for(Map.Entry<String,Object> entry : map.entrySet()){
                vector.add(entry.getValue());
            }
            rows.add(vector);
        }
    }
}
