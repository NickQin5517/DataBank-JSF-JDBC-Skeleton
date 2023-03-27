/******************************************************
 * File:  StudentDaoImpl.java Course materials (22F) CST8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.dao;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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


import databank.model.StudentPojo;

@SuppressWarnings("unused")
/**
 * Description:  Implements the C-R-U-D API for the database
 */
//TODO don't forget this object is a managed bean with a application scope
@Named
@ApplicationScoped
public class StudentDaoImpl implements StudentDao, Serializable {
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static final String DATABANK_DS_JNDI = "java:app/jdbc/databank";
	private static final String READ_ALL = "select * from student";
	private static final String READ_STUDENT_BY_ID = "select * from student where id = ?";
	private static final String INSERT_STUDENT = "insert into student(last_name, first_name, email, phone, level, program, created) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_STUDENT_ALL_FIELDS = "update student set last_name = ?, first_name = ?, email = ?, phone = ?, level = ?, program = ? where id = ?";
	private static final String DELETE_STUDENT_BY_ID = "delete from student where id = ?";

	@Inject
	protected ExternalContext externalContext;

	private void logMsg(String msg) {
		((ServletContext) externalContext.getContext()).log(msg);
	}

	@Resource(lookup = DATABANK_DS_JNDI)
	protected DataSource databankDS;

	protected Connection conn;
	protected PreparedStatement readAllPstmt;
	protected PreparedStatement readByIdPstmt;
	protected PreparedStatement createPstmt;
	protected PreparedStatement updatePstmt;
	protected PreparedStatement deleteByIdPstmt;

	@PostConstruct
	protected void buildConnectionAndStatements() {
		try {
			logMsg("building connection and stmts");
			conn = databankDS.getConnection();
			readAllPstmt = conn.prepareStatement(READ_ALL);
			createPstmt = conn.prepareStatement(INSERT_STUDENT, RETURN_GENERATED_KEYS);
			//TODO Initialize other PreparedStatements
			readByIdPstmt = conn.prepareStatement(READ_STUDENT_BY_ID);
			updatePstmt = conn.prepareStatement(UPDATE_STUDENT_ALL_FIELDS);
			deleteByIdPstmt = conn.prepareStatement(DELETE_STUDENT_BY_ID);
		} catch (Exception e) {
			logMsg("something went wrong getting connection from database:  " + e.getLocalizedMessage());
		}
	}

	@PreDestroy
	protected void closeConnectionAndStatements() {
		try {
			logMsg("closing stmts and connection");
			readAllPstmt.close();
			createPstmt.close();
			//TODO Close other PreparedStatements
			readByIdPstmt.close();
			updatePstmt.close();
			deleteByIdPstmt.close();
			conn.close();
		} catch (Exception e) {
			logMsg("something went wrong closing stmts or connection:  " + e.getLocalizedMessage());
		}
	}

	@Override
	public List<StudentPojo> readAllStudents() {
		logMsg("reading all students");
		List<StudentPojo> students = new ArrayList<>();
		try (ResultSet rs = readAllPstmt.executeQuery();) {

			while (rs.next()) {
				StudentPojo newStudent = new StudentPojo();
				newStudent.setId(rs.getInt("id"));
				newStudent.setLastName(rs.getString("last_name"));
				//TODO Complete the student initialization
				newStudent.setFirstName(rs.getString("first_name"));
				newStudent.setEmail(rs.getString("email"));
				newStudent.setPhoneNumber(rs.getString("phone"));
				newStudent.setLevel(rs.getString("level"));
				newStudent.setProgram(rs.getString("program"));
				newStudent.setCreated((LocalDateTime) rs.getObject("created"));
				students.add(newStudent);
			}
			
		} catch (SQLException e) {
			logMsg("something went wrong accessing database:  " + e.getLocalizedMessage());
		}
		
		return students;

	}

	@Override
	public StudentPojo createStudent(StudentPojo student) {
		logMsg("creating a student");
		//TODO Complete the insertion of a new student
		//TODO Be sure to use try-and-catch statement
		try {
			createPstmt.setString(1, student.getLastName());
			createPstmt.setString(2, student.getFirstName());
			createPstmt.setString(3, student.getEmail());
			createPstmt.setString(4, student.getPhoneNumber());
			createPstmt.setString(5, student.getLevel());
			createPstmt.setString(6, student.getProgram());
			createPstmt.setObject(7, student.getCreated());
			createPstmt.execute();
		}
		catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
		}
		
		return null;
	}

	@Override
	public StudentPojo readStudentById(int studentId) {
		logMsg("read a specific student");
		//TODO Complete the retrieval of a specific student by its id
		//TODO Be sure to use try-and-catch statement
		
		StudentPojo student = new StudentPojo();

    	//last_name, first_name, email, phone, level, program,
    	try {
    		readByIdPstmt.setLong(1, studentId);
    		ResultSet rs = readByIdPstmt.executeQuery();
    		if (rs.next()) {
    			student.setId(rs.getInt("id"));
    			student.setLastName(rs.getString("last_name"));
				student.setFirstName(rs.getString("first_name"));
				student.setEmail(rs.getString("email"));
				student.setPhoneNumber(rs.getString("phone"));
				student.setLevel(rs.getString("level"));
				student.setProgram(rs.getString("program"));
    		}
    	}
		catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
		}
    	return student;
	}

	@Override
	public void updateStudent(StudentPojo student) {
		logMsg("updating a specific student");
		//TODO Complete the update of a specific student
		//TODO Be sure to use try-and-catch statement
		try {
			updatePstmt.setString(1, student.getLastName());
			updatePstmt.setString(2, student.getFirstName());
			updatePstmt.setString(3, student.getEmail());
			updatePstmt.setString(4, student.getPhoneNumber());
			updatePstmt.setString(5, student.getLevel());
			updatePstmt.setString(6, student.getProgram());
			updatePstmt.setInt(7, student.getId());
			updatePstmt.execute();
		}
		catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
		}

	}

	@Override
	public void deleteStudentById(int studentId) {
		logMsg("deleting a specific student");
		//TODO Complete the deletion of a specific student
		//TODO Be sure to use try-and-catch statement
		try {
			deleteByIdPstmt.setInt(1, studentId);

			deleteByIdPstmt.execute();
		}
		catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
		}
	}

}