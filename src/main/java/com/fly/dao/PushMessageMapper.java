package com.fly.dao;

import com.fly.domain.PushMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PushMessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table push_message
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table push_message
     *
     * @mbg.generated
     */
    int insert(PushMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table push_message
     *
     * @mbg.generated
     */
    int insertSelective(PushMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table push_message
     *
     * @mbg.generated
     */
    PushMessage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table push_message
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(PushMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table push_message
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(PushMessage record);
}