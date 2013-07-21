package com.leonc.zodiac.aquarius.db;

import java.util.HashMap;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Blob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DBConn {
    private static Log logger = LogFactory.getLog(DBConn.class);

    private Connection conn = null;

    public Connection getDriverConn() { return this.conn; }

    public DBConn(Connection conn) {
        this.conn = conn;
    }

    public HashMap<String, Object> queryObject(String sql, Object... params) {
        ArrayList<HashMap<String, Object>> list = queryObjects(sql, params);
        if(list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    public ArrayList<HashMap<String, Object>> queryObjects(String sql, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
        try {
            st = this.conn.prepareStatement(sql);
            for(int i = 0; i < params.length; ++i) {
                st.setObject(i + 1, params[i]);
            }
            
            rs = st.executeQuery();
            while(rs.next()) {
                results.add(this.readRow(rs));
            }
            return results;
        } catch(SQLException ex) {
            logger.error("query object exception:" + ex + sql);
            return null;
        } finally {
        	try {rs.close(); } catch(SQLException e) {}
            try {st.close(); } catch(SQLException e) {}
        }
    }

    public HashMap<Long, HashMap<String, Object>> queryObjectsInUidMap(String sql, String uidColumnName, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        HashMap<Long, HashMap<String, Object>> results = new HashMap<Long, HashMap<String, Object>>();
        try {
            st = this.conn.prepareStatement(sql);
            for(int i = 0; i < params.length; ++i) {
                st.setObject(i + 1, params[i]);
            }
            
            rs = st.executeQuery();
            while(rs.next()) {
                results.put(rs.getLong(uidColumnName), this.readRow(rs));
            }
            return results;
        } catch(SQLException ex) {
            logger.error("query object exception:" + ex + sql);
            return null;
        } finally {
        	try {rs.close(); } catch(SQLException e) {}
            try {st.close(); } catch(SQLException e) {}
        }
    }

    public ArrayList<Long> queryUids(String sql, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        ArrayList<Long> results = new ArrayList<Long>();
        try {
            st = this.conn.prepareStatement(sql);
            for(int i = 0; i < params.length; ++i) {
                st.setObject(i + 1, params[i]);
            }
            
            rs = st.executeQuery();
            while(rs.next()) {
                results.add(rs.getLong(1));
            }
            return results;
        } catch(SQLException ex) {
            logger.error("query object exception:" + ex + sql);
            return null;
        } finally {
        	try {rs.close(); } catch(SQLException e) {}
            try {st.close(); } catch(SQLException e) {}
        }
    }

    public int queryCount(String sql, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = this.conn.prepareStatement(sql);
            for(int i = 0; i < params.length; ++i) {
                st.setObject(i + 1, params[i]);
            }
            
            rs = st.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch(SQLException ex) {
            logger.error("query object count exception:" + ex + sql);
            return 0;
        } finally {
        	try {rs.close(); } catch(SQLException e) {}
            try {st.close(); } catch(SQLException e) {}
        }
    }

    public void update(String sql, Object... params) {
        PreparedStatement st = null;
        try {
            st = this.conn.prepareStatement(sql);
            for(int i = 0; i < params.length; ++i) {
                st.setObject(i + 1, params[i]);
            }
            st.executeUpdate();
        } catch(SQLException ex) {
            logger.error("update exception:" + ex + sql);
        } finally {
        	try {st.close(); } catch(SQLException e) {}
        }
    }

    public long insert(String sql, Object... params) {
        PreparedStatement st = null;
        try {
            st = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < params.length; ++i) {
                st.setObject(i + 1, params[i]);
            }
            if(st.executeUpdate() > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()) {
                    long val = rs.getLong(1);
                    rs.close();
                    return val;
                }
            }
            return 0;
        } catch(SQLException ex) {
            logger.error("insert exception:" + ex + sql);
            return 0;
        } finally {
            try {st.close(); } catch(SQLException e) {}
        }
    }

    private HashMap<String, Object> readRow(ResultSet rs) throws SQLException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		ResultSetMetaData rsmd = rs.getMetaData();
		int nCols = rsmd.getColumnCount();
		for (int i = 1; i <= nCols; ++i) {
			String name = (rsmd.getColumnLabel(i));
			Object value = rs.getObject(i);
			if (value instanceof Blob) {
                logger.error("can not read object from db: " + name);
                continue;
			}

            result.put(name, value);
		}
		return result;
	}
}