package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import assets.DBConnectionMgr;

public class LoginService {

	public static void main(String[] args) {
		
		boolean test = loginTest("test", "1234");

		System.out.println("로그인 결과 : " + test);
		
	}

	public static boolean loginTest(String id, String password) {
		boolean flag = false;

		DBConnectionMgr pool = DBConnectionMgr.getInstance();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String getPass = null;

		try {
			con = pool.getConnection();

			// 문장 생성
			sql = "select password from member_test where id=?";

			// 문장연결, 열차준비
			pstmt = con.prepareStatement(sql);

			// 빈칸채워주기

			pstmt.setString(1, id);

			// 실행
			rs = pstmt.executeQuery();

			if (rs.next()) {
				// 패스워드를 읽어온다.
				getPass = rs.getString("password");

				// 데이터베이스에서 읽어온 문자열과 사용자가 입력한 비밀번호가 같을 경우
				// 창을 반환
				if (getPass.equals(password)) {
					System.out.println("받아온 비밀번호 : " + getPass);
					flag = true;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			pool.freeConnection(con, pstmt, rs);

		}

		return flag;
	}
}
