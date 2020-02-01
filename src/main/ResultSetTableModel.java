package main;

// A TableModel that supplies ResultSet data to a JTable.

import bean.EventAttribute;
import bean.QueryTo;
import bean.TerrorEvent;
import bean.getEachEventData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dao.EventMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;

// ResultSet rows and columns are counted from 1 and JTable
// rows and columns are counted from 0. When processing
// ResultSet rows or columns for use in a JTable, it is
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is
// ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class ResultSetTableModel extends AbstractTableModel {
    private List<TerrorEvent> events;
    private ArrayList<EventAttribute> attributes;
    private Integer[] pages;
    private Integer prePage;
    private Integer nextPage;
    private long pagenum;
    private int numberOfRows;
    private int columnCount;
    // initialize resultSet and obtain its meta data object;
    // determine number of rows
    public ResultSetTableModel() throws SQLException{
        SqlSession session = getSession();
        try{
            // set query and execute it
            setDefaultQuery(session);
        }
        finally {
           session.close();
        }
    }
    //get pages
    public Integer[] getPages() {
        return pages;
    }

    public long getPagenum() {
        return pagenum;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public ResultSetTableModel(QueryTo search) throws SQLException {
            // set query and execute it
            setSearchQuery(search, 1, 28);
                // notify JTable that model has changed
            fireTableStructureChanged();
    }
    //page method
    public void setSearchQuery(QueryTo search,int pageNum, int pageSize){
        SqlSession session = getSession();
        try{
            EventMapper mapper = session.getMapper(EventMapper.class);
            //page setting
            Page<Object> page = PageHelper.startPage(pageNum,pageSize);
            //execute querying
            events = mapper.getSearchEvent(search);
            //page info
            PageInfo<TerrorEvent> pinfo = new PageInfo<>(events,5);
            //judge previoud page and next page
            if (pageNum==1) {
                this.prePage = 0;
                this.nextPage = pinfo.getNextPage();
            }
            else if (pageNum==pinfo.getPages()){
                this.prePage = pinfo.getPrePage();
                this.nextPage = 0;
            }
            else {
                this.prePage = pinfo.getPrePage();
                this.nextPage = pinfo.getNextPage();
            }
            if(pages==null){
                this.pages = new Integer[pinfo.getPages()];
                for (int p=0;p<pages.length;p++) this.pages[p] = p+1;
                this.pagenum = pinfo.getTotal();
            }
            //get attributes as table columns
            if(attributes==null) attributes = mapper.getAttributes();
            numberOfRows = events.size();  // get row number
        }finally {
            session.close();
        }
    }


    public SqlSession getSession(){
        String resource = "config/Configure.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //SqlSession cannot be shared, you must create new object when you use it every time
        SqlSession session = sqlSessionFactory.openSession();
        return session;
    }
    //继承AbstractTableModel必须实现的方法
    // get number of columns in ResultSet
    public int getColumnCount() throws IllegalStateException
    {
        // determine number of columns
        SqlSession session = getSession();
        try {
            EventMapper mapper = session.getMapper(EventMapper.class);
            ArrayList<EventAttribute> attribute = mapper.getAttributes();
            columnCount = attribute.size();
            return columnCount;
        }
        finally {
            session.close();
        }
    }

    //继承AbstractTableModel必须实现的方法
    // 得到列名 in ResultSet
    public String getColumnName( int column ) throws IllegalStateException
    {
        if(column<attributes.size())
        return attributes.get(column++).getAttribute_name();
        return null;
    }

    //继承AbstractTableModel必须实现的方法
    // return number of rows in ResultSet
    public int getRowCount() throws IllegalStateException
    {
        return numberOfRows;
    }

    //继承AbstractTableModel必须实现的方法,以获得编辑数据。
    // obtain value in particular row and column
    public Object getValueAt( int row, int column )
            throws IllegalStateException {
            // obtain a value at specified ResultSet row and column
        if(row<events.size()&&column<attributes.size()) {
            TerrorEvent oneEvent = events.get(row++);
            return getEachEventData.getData(oneEvent)[column++];
        }
        else return null;
    }

    // set new database query string
    public void setDefaultQuery( SqlSession session)
            throws SQLException, IllegalStateException
    {
        EventMapper mapper = session.getMapper(EventMapper.class);
        events = mapper.getDefaultEvent();
        attributes = mapper.getAttributes();
        numberOfRows = events.size();  // get row number
            // notify JTable that model has changed
            fireTableStructureChanged();
    }
}  // end class ResultSetTableModel




