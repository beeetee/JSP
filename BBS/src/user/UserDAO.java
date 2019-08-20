package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS&serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Drvier");//MYSQL 접속 매개체 역할
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 실제 로그인 함수
	 */
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);	// 어떠한 정해진 sql 문장을 db에 삽입하는 형식으로 pstmt를 가져 옴
			// sql의 해킹을 방어하는 수단으로 preparedStatement 기법을 사용하여 하나의 문장을 미리 준비해놓고 미리 물음표를 넣어놓고 나중에 물음표에 해당하는 내용으로 userID를 넣어줌
			// userID가 실제로 존재하는지, 존재하면 그 비밀번호가 뭔지 DB로 부터 가져옴
			pstmt.setString(1, userID);					
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).contentEquals(userPassword)) {
					return 1;	// 로그인 성공
				}
				else {
					return 0;	// 비밀번호 불일치
				}
			}
			return -1;	// 아이디가 없음
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -2;// db 오류
	}
}
