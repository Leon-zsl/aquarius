package com.leonc.zodiac.aquarius.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DBConnPool
{
	private static Log logger = LogFactory.getLog(DBConnPool.class);
	
	private ConcurrentLinkedQueue<Connection> conns = new ConcurrentLinkedQueue<Connection>();
	

    private String dbDriver = "";
    private String dbUrl = "";
	
	public void start(int initSize, String dbaddr, String dbname, String usrname, String usrpwd) {
        this.dbDriver = "com.mysql.jdbc.Driver";
		this.dbUrl = "jdbc:mysql://" + dbaddr + "/" + dbname + "?" +
			"user=" + usrname + "&password=" + usrpwd;

		for(int i = 0; i < initSize; ++i) {
            Connection conn = createConn();
            conns.add(conn);
        }
	}
	
	public void close() {
		try {
	        while(true) {
	            Connection conn = conns.poll();
	            if(conn == null) break;
	            conn.close();
	        }
	        conns.clear();
		} catch(SQLException ex) {
			logger.error("close conns exception: " + ex);
		}
	}
	
	public DBConn fetch() {
        Connection conn = conns.poll();
        if(conn != null) return new DBConn(conn);

        conn = createConn();
        conns.add(conn);
        return new DBConn(conn);
	}
	
	public void release(DBConn dbconn) {
        if(dbconn == null) return;

        Connection conn = dbconn.getDriverConn();
        try {
        	if(conn.isClosed()) return;
        } catch(SQLException ex) {
        	logger.error("release conn exception: " + ex);
        }
        
        conns.add(conn);
	}
	
	private Connection createConn() {
		try {
			Class.forName(this.dbDriver);
		} catch(ClassNotFoundException ex) {
			logger.error("can not find jdbc driver");
			return null;
		}

		try {
			Connection conn = DriverManager.getConnection(this.dbUrl);
            conn.setAutoCommit(true);
            logger.info("connection count:" + this.conns.size());
			return conn;
		} catch(SQLException ex) {
			logger.error("create db connection failed");
			return null;
		}
	}
}