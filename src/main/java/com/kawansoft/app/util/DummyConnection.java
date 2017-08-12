/**
 * 
 */
package com.kawansoft.app.util;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Nicolas de Pomereu
 *
 */
public class DummyConnection implements Connection {

    /**
     * 
     */
    public DummyConnection() {
	// TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }


    /* (non-Javadoc)
     * @see java.sql.Connection#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#close()
     */
    @Override
    public void close() throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#commit()
     */
    @Override
    public void commit() throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
     */
    @Override
    public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createBlob()
     */
    @Override
    public Blob createBlob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createClob()
     */
    @Override
    public Clob createClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createNClob()
     */
    @Override
    public NClob createNClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createSQLXML()
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement()
     */
    @Override
    public Statement createStatement() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement(int, int)
     */
    @Override
    public Statement createStatement(int arg0, int arg1) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    @Override
    public Statement createStatement(int arg0, int arg1, int arg2)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
     */
    @Override
    public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getAutoCommit()
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getCatalog()
     */
    @Override
    public String getCatalog() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getClientInfo()
     */
    @Override
    public Properties getClientInfo() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getClientInfo(java.lang.String)
     */
    @Override
    public String getClientInfo(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getHoldability()
     */
    @Override
    public int getHoldability() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getMetaData()
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getTransactionIsolation()
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getTypeMap()
     */
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#isClosed()
     */
    @Override
    public boolean isClosed() throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#isReadOnly()
     */
    @Override
    public boolean isReadOnly() throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#isValid(int)
     */
    @Override
    public boolean isValid(int arg0) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    @Override
    public String nativeSQL(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    @Override
    public CallableStatement prepareCall(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    @Override
    public CallableStatement prepareCall(String arg0, int arg1, int arg2)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    @Override
    public CallableStatement prepareCall(String arg0, int arg1, int arg2,
	    int arg3) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    @Override
    public PreparedStatement prepareStatement(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int[] arg1)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, String[] arg1)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
	    int arg3) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    @Override
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#rollback()
     */
    @Override
    public void rollback() throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    @Override
    public void rollback(Savepoint arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    @Override
    public void setAutoCommit(boolean arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    @Override
    public void setCatalog(String arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setClientInfo(java.util.Properties)
     */
    @Override
    public void setClientInfo(Properties arg0) throws SQLClientInfoException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
     */
    @Override
    public void setClientInfo(String arg0, String arg1)
	    throws SQLClientInfoException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setHoldability(int)
     */
    @Override
    public void setHoldability(int arg0) throws SQLException {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setSavepoint()
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    @Override
    public Savepoint setSavepoint(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }


    /* (non-Javadoc)
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    @Override
    public void setTransactionIsolation(int arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    @Override
    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
	// TODO Auto-generated method stub

    }

    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
