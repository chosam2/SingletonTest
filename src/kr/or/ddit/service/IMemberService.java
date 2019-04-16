package kr.or.ddit.service;

import java.util.List;

import kr.or.ddit.vo.MemberVO;

/**
 * Service객체는 Dao에 설정된 메서드를 원하는 작업에 맞게 호출하여 결과를 받아오고, 받아온 자료를
 * Controller에게 보내주는 역할을 수행한다. 보통 Dao의 메서드와 같은 구조를 갖는다.
 * @author pc18
 *
 */
public interface IMemberService {
	/**
	 * MemberVO에 담겨진 자료를 DB에 insert하는 메서드
	 * @param mv DB에 insert할 자료가 저장된 MemverVO객체
	 * @return DB작업이 성공하면 1이상의 값이 반환되고 실패하면 0이 반환된
	 */
	public int insertMember(MemberVO mv);

	/**
	 * 회원 ID를 매개변수로 받아서 그 회원 정보를 삭제하는 메서드
	 * @param memId 삭제할 회원 ID
	 * @return 작업성공 : 1, 작업실패 : 0
	 */
	public int deleteMember(String memId);

	/**
	 * 하나의 MemberVO자료를 이요하여 DB를 update하는 메서드
	 * @param mv update할 회원정보가 들어있는 MemberVO객체
	 * @return 작업성공  : 1, 작업실패 : 0
	 */
	public int updateMember(MemberVO mv);

	/**
	 * DB의 mymember테이블의 전체 레코드를 가져와서 List에 담아서 반환하는 메서드
	 * @return MemberVO객체를 담고 있는 List객체
	 */
	public List<MemberVO> getAllMemberList();

	/**
	 * 주어진 회원ID가 존재하는지 여부를 알아내는 메서드
	 * @param memId 검색할 회원 ID
	 * @return 해당 회원ID가 있으면 true, 없으면 false
	 */
	public boolean chkMemberInfo(String memId);

	/**
	 * 주어진 회원정보를 이용하여 회원을 검색해주는 메서드
	 * @param mv 검색할 회원정보
	 * @return MemberVO객체를 담고 있는 List객체
	 */
	public List<MemberVO> getSearchMember(MemberVO mv);
}
