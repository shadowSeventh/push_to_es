package com.shadow.push.mapping;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

@Mapper
public interface UserMapper {

    @Update("UPDATE User SET passWord=#{userName} WHERE id=#{id}")
    int updateName(@Param(value = "userName") String userName, @Param(value = "id") int id);

    @Update("UPDATE User SET passWord=#{passWord} WHERE id=#{id} and userName=#{userName}")
    int updatePassWordByName(@Param(value = "passWord") String passWord, @Param(value = "userName") String userName, @Param(value = "id") int id);

    @Update("UPDATE User SET passWord=#{passWord} WHERE id=#{id} and userName=#{userName} and passWord=#{word}")
    int updatePassWordByNameAndPassword(@Param(value = "passWord") String passWord, @Param(value = "userName") String userName, @Param(value = "word") String word, @Param(value = "id") int id);


    @Delete("DELETE FROM User WHERE id=#{id}")
    int delete(@Param(value = "id") int id);

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
