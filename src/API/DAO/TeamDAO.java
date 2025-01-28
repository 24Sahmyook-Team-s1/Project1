package API.DAO;

import java.sql.*;
import javax.naming.NamingException;
import API.Utils.ConnectionPool;

public class TeamDAO {

    /** 
     * 새로운 팀을 데이터베이스에 추가합니다.
     * @param tid 팀 ID
     * @param teamname 팀 이름
     * @return 팀 추가 성공 여부
     */
    public boolean insertNewTeam(String tid, String teamname) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO ProjectTeams(ProjectTeamID, TeamName, CreatedAt) VALUES(?, ?, SYSDATE)";
            conn = ConnectionPool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tid);
            stmt.setString(2, teamname);
            
            int count = stmt.executeUpdate();
            return count == 1; // 성공적으로 추가되면 true 반환
            
        } finally {
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
    
    /** 
     * 팀 멤버를 데이터베이스에 추가합니다.
     * @param projectUserId 사용자 ID
     * @param projectTeamId 팀 ID
     * @return 팀 멤버 추가 성공 여부
     */
    public boolean insertTeamMember(String projectUserId, String projectTeamId) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO TeamMembers(ProjectUserID, ProjectTeamID, CreatedAt) VALUES(?, ?, SYSDATE)";
            conn = ConnectionPool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, projectUserId);
            stmt.setString(2, projectTeamId);
            
            int count = stmt.executeUpdate();
            return count == 1; // 성공적으로 추가되면 true 반환
            
        } finally {
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
    
    /** 
     * 주어진 팀 ID가 데이터베이스에 존재하는지 확인합니다.
     * @param tid 팀 ID
     * @return 팀 존재 여부
     */
    public boolean existsFromID(String tid) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT ProjectTeamID FROM ProjectTeams WHERE ProjectTeamID = ?";
            conn = ConnectionPool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tid);
            
            rs = stmt.executeQuery();
            return rs.next(); // 결과가 존재하면 true 반환
            
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
    
    /** 
     * 주어진 팀 이름이 데이터베이스에 존재하는지 확인합니다.
     * @param teamname 팀 이름
     * @return 팀 존재 여부
     */
    public boolean existsFromTeamName(String teamname) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT ProjectTeamID FROM ProjectTeams WHERE TeamName = ?";
            conn = ConnectionPool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teamname);
            
            rs = stmt.executeQuery();
            return rs.next(); // 결과가 존재하면 true 반환
            
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }

    /** 
     * 주어진 팀 ID에 해당하는 팀과 그 멤버를 삭제합니다.
     * @param tid 팀 ID
     * @return 팀 삭제 성공 여부
     */
    public boolean delete(String tid) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            // 1. 팀 멤버 삭제
            String sqlDeleteMembers = "DELETE FROM TeamMembers WHERE ProjectTeamID = ?";
            conn = ConnectionPool.get();
            stmt1 = conn.prepareStatement(sqlDeleteMembers);
            stmt1.setString(1, tid);
            stmt1.executeUpdate(); // 팀 멤버 삭제
            
            // 2. 팀 삭제
            String sqlDeleteTeam = "DELETE FROM ProjectTeams WHERE ProjectTeamID = ?";
            stmt2 = conn.prepareStatement(sqlDeleteTeam);
            stmt2.setString(1, tid);
            
            int count = stmt2.executeUpdate(); // 팀 삭제
            return count == 1; // 성공적으로 삭제되면 true 반환
            
        } finally {
            if (stmt1 != null) stmt1.close(); 
            if (stmt2 != null) stmt2.close(); 
            if (conn != null) conn.close();
        }
    }
    
    /** 
     * 모든 팀의 목록을 가져옵니다.
     * @return 팀 목록(JSON 형식)
     */
    public String getList() throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM ProjectTeams";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            StringBuilder str = new StringBuilder("[");
            int cnt = 0;
            while (rs.next()) {
                if (cnt++ > 0) str.append(", ");
                str.append(rs.getString("TeamName")); // 팀 이름 추가
            }
            return str.append("]").toString(); // JSON 형식으로 반환
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }

    /** 
     * 주어진 팀 ID에 해당하는 팀 정보를 가져옵니다.
     * @param tid 팀 ID
     * @return 팀 정보(JSON 형식)
     */
    public String get(String tid) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT TeamName FROM ProjectTeams WHERE ProjectTeamID = ?";
            conn = ConnectionPool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tid);
            
            rs = stmt.executeQuery();
            return rs.next() ? rs.getString("TeamName") : "{}"; // 팀 정보 반환, 없으면 빈 JSON 반환
            
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
    
    /** 
     * 주어진 팀 ID의 팀 이름을 업데이트합니다.
     * @param tid 팀 ID
     * @param teamname 새 팀 이름
     * @return 업데이트 성공 여부
     */
    public boolean update(String tid, String teamname) throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE ProjectTeams SET TeamName = ? WHERE ProjectTeamID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teamname);
            stmt.setString(2, tid);
            
            int count = stmt.executeUpdate();
            return count == 1; // 성공적으로 업데이트되면 true 반환
            
        } finally {
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
}
