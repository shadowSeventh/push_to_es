package com.shadow.push.readFiles;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TableManager {


    @Cacheable(key = "#fileName", value = "esMapping", condition = "#condition")
    public String getMapping(String fileName, Boolean condition) {
        System.out.println(fileName);
        return ReadOssFiles.openFile(fileName);
    }

    void getTableStructure() {


    }
}
