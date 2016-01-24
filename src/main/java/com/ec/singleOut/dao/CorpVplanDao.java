/**
 * @Package com.ec.nsqc.dao.impl
 * @Description: TODO
 * @author ecuser
 * @date 2015年12月10日 下午4:46:19
 */
package com.ec.singleOut.dao;
import com.ec.singleOut.bean.CorpVplan;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: CorpVplanDaoImpl
 * @Description: TODO
 * @author ecuser
 * @date 2015年12月10日 下午4:46:19
 */
@Repository
public class CorpVplanDao extends BaseDaoSupport  {

    private static final String TABLE_NAME = "t_corp_vplan";
    private static final String GET_BY_CORP_ID = "corpVplan.getByCorpId";

    /*
     * <p>Title: getByCorpId</p> <p>Description: </p>
     *
     * @param corpId
     *
     * @return
     *
     * @see com.ec.nsqc.dao.CorpVplanDao#getByCorpId(long)
     */

    public CorpVplan getByCorpId(long corpId) {
        return getBaseSqlSession(SELECT, TABLE_NAME).selectOne(GET_BY_CORP_ID, corpId);
    }

}
