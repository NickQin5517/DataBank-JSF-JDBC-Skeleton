/******************************************************
 * File:  ListDataDaoImpl.java Course materials (22F) CST8277
 *
 * @author Teddy Yap
 */
package databank.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.sql.DataSource;


@SuppressWarnings("unused")
/**
 * Description:  API for reading list data from the database
 */
@Named
@ApplicationScoped
public class ListDataDaoImpl implements ListDataDao, Serializable {
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static final String DATABANK_DS_JNDI = "java:app/jdbc/databank";
	private static final String READ_ALL_LEVELS = "select * from level";
	private static final String READ_ALL_PROGRAMS = "select * from program";

	@Inject
	protected ExternalContext externalContext;

	private void logMsg(String msg) {
		((ServletContext) externalContext.getContext()).log(msg);
	}

	@Resource(lookup = DATABANK_DS_JNDI)
	protected DataSource databankDS;

	protected Connection conn;
	protected PreparedStatement readAllLevelsPstmt;
	protected PreparedStatement readAllProgramsPstmt;

	@PostConstruct
	protected void buildConnectionAndStatements() {
		try {
			logMsg("building connection and stmts");
			conn = databankDS.getConnection();
			//TODO Initialize PreparedStatements
			readAllLevelsPstmt = conn.prepareStatement(READ_ALL_LEVELS);
			readAllProgramsPstmt = conn.prepareStatement(READ_ALL_PROGRAMS);
		} catch (Exception e) {
			logMsg("something went wrong getting connection from database:  " + e.getLocalizedMessage());
		}
	}

	@PreDestroy
	protected void closeConnectionAndStatements() {
		try {
			logMsg("closing stmts and connection");
			//TODO Close PreparedStatements
			readAllLevelsPstmt.close();
			readAllProgramsPstmt.close();
			conn.close();
		} catch (Exception e) {
			logMsg("something went wrong closing stmts or connection:  " + e.getLocalizedMessage());
		}
	}

	@Override
	public List<String> readAllLevels() {
		logMsg("reading all levels");
		List<String> levels = new ArrayList<>();
		//TODO Complete the retrieval of all levels
		//TODO Be sure to use try-and-catch statement
        try {
        	ResultSet rs = readAllLevelsPstmt.executeQuery();
        	while(rs.next()) {
        		levels.add(rs.getString("name"));       	
            }
            try {
            	rs.close();
            }
            catch (SQLException e) {
                logMsg("something went wrong closing resultSet: " + e.getLocalizedMessage());
            }
        }
        catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
        }

		return levels;

	}

	@Override
	public List<String> readAllPrograms() {
		logMsg("reading all programs");
		List<String> programs = new ArrayList<>();
		//TODO Complete the retrieval of all programs
		//TODO Be sure to use try-and-catch statement
		try {
        	ResultSet rs = readAllProgramsPstmt.executeQuery();
        	while(rs.next()) {
        		programs.add(rs.getString("name"));       	
            }
            try {
            	rs.close();
            }
            catch (SQLException e) {
                logMsg("something went wrong closing resultSet: " + e.getLocalizedMessage());
            }
        }
        catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
        }
		return programs;

	}
	
}
