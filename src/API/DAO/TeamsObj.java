package API.DAO;

public class TeamsObj {
    private String teamid;      // 팀 ID
    private String teamname;    // 팀 이름
    private String createDate;  // 생성 날짜

    public TeamsObj(String teamid, String teamname, String createDate) {
        this.teamid = teamid;
        this.teamname = teamname;
        this.createDate = createDate;
    }

    public String getId() {
        return this.teamid;
    }

    public String getName() {
        return this.teamname;
    }

    public String getCreateDate() {
        return this.createDate;
    }
}
