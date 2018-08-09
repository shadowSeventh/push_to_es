package com.shadow.push.mapping;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

@Mapper
public interface UserMapper {

    @UpdateProvider(type = BaseSqlProvider.class, method = "insert")
    int insert(JSONObject input);


    @UpdateProvider(type = BaseSqlProvider.class, method = "update")
    int update(JSONObject input);


    class BaseSqlProvider {

        public String insert(JSONObject map) {
            return new SQL() {
                {
                    INSERT_INTO("User");
                    for (String key : map.keySet()) {
                        VALUES(key, "#{" + key + "}");
                    }
                }
            }.toString();
        }

        public String update(JSONObject map) {
            return new SQL() {
                {
                    UPDATE("User");
                    for (String key : map.keySet()) {
                        if (!"ID".equals(key)) {
                            SET(key + "= #{" + key + "}");
                        }
                    }
                    WHERE("ID = #{ID}");
                }
            }.toString();
        }
    }
}
