package com.ec.singleOut.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ecuser on 2015/12/28.
 */
@Component
public abstract class BaseDaoSupport {

    @Resource(name = "sqlSessionBase")
    private SqlSession sqlSessionBase;   //基础库
    @Resource(name = "sqlSessionCrm0")
    private SqlSession sqlSessionCrm0;
    @Resource(name = "sqlSessionCrm1")
    private SqlSession sqlSessionCrm1;
    @Resource(name = "sqlSessionCrm2")
    private SqlSession sqlSessionCrm2;
    @Resource(name = "sqlSessionCrm3")
    private SqlSession sqlSessionCrm3;

    @Resource(name="sqlSessionStatic")
    private SqlSession sqlSessionStatic;  // 统计库


    protected String UPDATE = "update";
    protected String SELECT = "select";

    public SqlSession getSqlSessionCrm0() {
        return sqlSessionCrm0;
    }

    public SqlSession getSqlSessionCrm1() {
        return sqlSessionCrm1;
    }

    public SqlSession getSqlSessionCrm2() {
        return sqlSessionCrm2;
    }

    public SqlSession getSqlSessionCrm3() {
        return sqlSessionCrm3;
    }

    public SqlSession getSqlSessionStatic() {
        return sqlSessionStatic;
    }

    public SqlSession getBaseSqlSession(String operateType, String tableName) {

        return sqlSessionBase;
    }


    public SqlSession getCrmSqlSession(long cropId) {
        return sqlSessionRouter(cropId);
    }

    private SqlSession sqlSessionRouter(long id) {
        SqlSession sqlSession = null;
        int result = (int) (id % 4);

        switch (result) {
            case 0:
                sqlSession = sqlSessionCrm0;
                break;
            case 1:
                sqlSession = sqlSessionCrm1;
                break;
            case 2:
                sqlSession = sqlSessionCrm2;
                break;
            case 3:
                sqlSession = sqlSessionCrm3;
                break;
            default:
                break;
        }
        return sqlSession;
    }
}
