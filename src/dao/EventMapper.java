package dao;

import bean.EventAttribute;
import bean.ExcelInput;
import bean.QueryTo;
import bean.TerrorEvent;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface EventMapper<Model, PK extends Serializable>  {
     public List<TerrorEvent> getDefaultEvent();
     public List<TerrorEvent> getSearchEvent(QueryTo search);

     Integer selectEventCount();
     Integer selectAttrCount();


     int deleteByPrimaryKey(@Param("eventid") PK id);

     int insert(Model record);

     int insertSelective(List<ExcelInput> record);

     TerrorEvent selectByPrimaryKey(PK id);

     int updateByPrimaryKeySelective(Model record);

     int updateByPrimaryKeyWithBLOBs(Model record);


     int updateByPrimaryKey(Model record);

     public ArrayList<EventAttribute> getAttributes();
     public ArrayList<EventAttribute> getAttributesAll();
}
