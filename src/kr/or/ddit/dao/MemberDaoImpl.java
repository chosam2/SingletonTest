package kr.or.ddit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.or.ddit.util.DBUtil2;
import kr.or.ddit.vo.MemberVO;

// DB에 접근해서 CRUD하는 역할
public class MemberDaoImpl implements IMemberDao {

	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
//	private static MemberDaoImpl dao;
	private static IMemberDao dao;
	
	private MemberDaoImpl() {} // 생성자
	
//	public static MemberDaoImpl getInstance() {
	public static IMemberDao getInstance() {
		if(dao == null) {
			dao = new MemberDaoImpl();
		}
		return dao; 
	}
	
	@Override
	public int insertMember(MemberVO mv) {

		int cnt = 0; // 등록된 데이터 건수

		try {
			conn = DBUtil2.getConnection();

			String sql = "insert into mymember (mem_id, mem_name, mem_tel, mem_addr)" + " values (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mv.getMem_id());
			pstmt.setString(2, mv.getMem_name());
			pstmt.setString(3, mv.getMem_tel());
			pstmt.setString(4, mv.getMem_addr());

			cnt = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}

	@Override
	public int deleteMember(String memId) {

		int cnt = 0; // 삭제된 데이터 건수

		try {
			conn = DBUtil2.getConnection();

			String sql = "delete from mymember where mem_id = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memId);

			cnt = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(memId + "회원 정보 삭제 실패"); // 회원정보가 없다는 뜻
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}

	@Override
	public int updateMember(MemberVO mv) {

		int cnt = 0; // 수정된 데이터 건수

		try {
			conn = DBUtil2.getConnection();
			String sql = "update mymember " + " set mem_name = ? " + " , mem_tel = ? " + " , mem_addr = ? " + " where mem_id = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mv.getMem_name()); // 물음표의 순서에맞게 Index에 넣어주면됨.
			pstmt.setString(2, mv.getMem_tel());
			pstmt.setString(3, mv.getMem_addr());
			pstmt.setString(4, mv.getMem_id());

			cnt = pstmt.executeUpdate();

		} catch (SQLException e) {

		} finally {
			disConnect();
		}

		return cnt;
	}

	@Override
	public List<MemberVO> getAllMemberList() {

		List<MemberVO> memList = new ArrayList<>();

		try {
			conn = DBUtil2.getConnection();

			stmt = conn.createStatement();

			String sql = "select * from mymember";

			rs = stmt.executeQuery(sql); // select 쿼리이기때문에 executeQuery로 던짐

			while (rs.next()) {

				MemberVO memVO = new MemberVO();

				memVO.setMem_id(rs.getString("mem_id"));
				memVO.setMem_name(rs.getString("mem_name"));
				memVO.setMem_tel(rs.getString("mem_tel"));
				memVO.setMem_addr(rs.getString("mem_addr"));

				memList.add(memVO); // 회원정보 한건 추가하기

			}

			System.out.println("--------------------------------------------------------------------");
			System.out.println("출력작업 끝...");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}

		return memList;
	}

	@Override
	public boolean chkMemberInfo(String memId) {

		boolean isExist = false;

		try {
			conn = DBUtil2.getConnection();

			String sql = "select count(*) as cnt from mymember where mem_id = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memId);

			rs = pstmt.executeQuery(); // select니깐 던져줌.

			int cnt = 0;

			if (rs.next()) { // 한건밖에 안나올테니깐 if로 해도 무방함. 여러건이면 while(rs.next())
				//				rs.getInt(cnt);
				cnt = rs.getInt(1);
			}

			if (cnt > 0) { // 0 이상이면 true로
				isExist = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return isExist;
	}

	/**
	 *  연결 끊을 때 finally에 들어갈 예외처리.
	 *  자원 반납(close)
	 */
	private void disConnect() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException ee) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException ee) {
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException ee) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException ee) {
			}

	}

	@Override
	public List<MemberVO> getSearchMember(MemberVO mv) {
		List<MemberVO> memList = new ArrayList<>();

		try {
			conn = DBUtil2.getConnection();

			String sql = "select * from mymember where 1=1 "; // where 1=1 쓰는거랑 안쓰는거 똑같음. 쓰는 이유는? => and로 이어주기 위해서
			// "select * from mymember where 1=1 and mem_id = ? and mem_name = ? and mem_tel = ? and mem_addr = like '%' || ? || '%' ";

			if (mv.getMem_id() != null && !mv.getMem_id().equals("")) {
				sql += " and mem_id = ? ";
			}
			if (mv.getMem_name() != null && !mv.getMem_name().equals("")) {
				sql += " and mem_name = ? ";
			}
			if (mv.getMem_tel() != null && !mv.getMem_tel().equals("")) {
				sql += " and mem_tel = ? ";
			}
			if (mv.getMem_addr() != null && !mv.getMem_addr().equals("")) {
				sql += " and mem_addr like '%' || ? || '%' "; // ex)'%대전$'
			}

			pstmt = conn.prepareStatement(sql);

			int index = 1;

			if (mv.getMem_id() != null && !mv.getMem_id().equals("")) {
				pstmt.setString(index++, mv.getMem_id());
			}
			if (mv.getMem_name() != null && !mv.getMem_name().equals("")) {
				pstmt.setString(index++, mv.getMem_name());
			}
			if (mv.getMem_tel() != null && !mv.getMem_tel().equals("")) {
				pstmt.setString(index++, mv.getMem_tel());
			}
			if (mv.getMem_addr() != null && !mv.getMem_addr().equals("")) {
				pstmt.setString(index++, mv.getMem_addr());
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {

				MemberVO memVO = new MemberVO();

				memVO.setMem_id(rs.getString("mem_id"));
				memVO.setMem_name(rs.getString("mem_name"));
				memVO.setMem_tel(rs.getString("mem_tel"));
				memVO.setMem_addr(rs.getString("mem_addr"));

				memList.add(memVO); // 회원정보 한건 추가하기

			}

		} catch (SQLException e) {
		} finally {
			disConnect();
		}

		return memList;
	}

}
