package com.example.backend.report;


import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReportMapper {
    @Select("SELECT * FROM report WHERE uid = #{uid}")
    List<Report> list(@Param("uid") int uid);

    @Select("SELECT * FROM report WHERE uid = #{uid} AND hid = #{hid}")
    Report list2(@Param("uid") int uid, @Param("hid") int hid);

    // Add methods for inserting and other operations as needed
    @Insert("INSERT INTO report (uid, date, url, result, suggestion) VALUES (#{uid}, #{date}, #{url}, #{result}, #{suggestion})")
    void add(Report report);
}
