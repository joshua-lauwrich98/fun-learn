package anchovy.net.funlearn.other;

/**
 * Created by DarKnight98 on 7/11/2017.
 */

public class Friend {

    private int totalGames, totalWin, totalLose, totalExercise, pvpBestScore, timeTrialBestScore,
            totalLearn, pvpTotalGames, pvpTotalWin, pvpTotalLose, pvpAnswer, pvpAnswerTrue,
            pvpAnswerFalse, timeTrialTotalGames, timeTrialTotalWin, timeTrialTotalLose,
            timeTrialAnswer, timeTrialAnswerTrue, timeTrialAnswerFalse;
    private String email, username, fullname, type, photo, status, uid;

    public Friend() {
        //required empty constructor
    }

    public Friend(int totalGames, int totalWin, int totalLose, int totalExercise, int pvpBestScore,
                  int timeTrialBestScore, int totalLearn, int pvpTotalGames, int pvpTotalWin,
                  int pvpTotalLose, int pvpAnswer, int pvpAnswerTrue, int pvpAnswerFalse,
                  int timeTrialTotalGames, int timeTrialTotalWin, int timeTrialTotalLose,
                  int timeTrialAnswer, int timeTrialAnswerTrue, int timeTrialAnswerFalse,
                  String email, String username, String fullname, String type, String photo,
                  String status, String uid) {
        this.totalGames = totalGames;
        this.totalWin = totalWin;
        this.totalLose = totalLose;
        this.totalExercise = totalExercise;
        this.pvpBestScore = pvpBestScore;
        this.timeTrialBestScore = timeTrialBestScore;
        this.totalLearn = totalLearn;
        this.pvpTotalGames = pvpTotalGames;
        this.pvpTotalWin = pvpTotalWin;
        this.pvpTotalLose = pvpTotalLose;
        this.pvpAnswer = pvpAnswer;
        this.pvpAnswerTrue = pvpAnswerTrue;
        this.pvpAnswerFalse = pvpAnswerFalse;
        this.timeTrialTotalGames = timeTrialTotalGames;
        this.timeTrialTotalWin = timeTrialTotalWin;
        this.timeTrialTotalLose = timeTrialTotalLose;
        this.timeTrialAnswer = timeTrialAnswer;
        this.timeTrialAnswerTrue = timeTrialAnswerTrue;
        this.timeTrialAnswerFalse = timeTrialAnswerFalse;
        this.email = email;
        this.username = username;
        this.fullname = fullname;
        this.type = type;
        this.photo = photo;
        this.status = status;
        this.uid = uid;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }

    public int getTotalLose() {
        return totalLose;
    }

    public void setTotalLose(int totalLose) {
        this.totalLose = totalLose;
    }

    public int getTotalExercise() {
        return totalExercise;
    }

    public void setTotalExercise(int totalExercise) {
        this.totalExercise = totalExercise;
    }

    public int getPvpBestScore() {
        return pvpBestScore;
    }

    public void setPvpBestScore(int pvpBestScore) {
        this.pvpBestScore = pvpBestScore;
    }

    public int getTimeTrialBestScore() {
        return timeTrialBestScore;
    }

    public void setTimeTrialBestScore(int timeTrialBestScore) {
        this.timeTrialBestScore = timeTrialBestScore;
    }

    public int getTotalLearn() {
        return totalLearn;
    }

    public void setTotalLearn(int totalLearn) {
        this.totalLearn = totalLearn;
    }

    public int getPvpTotalGames() {
        return pvpTotalGames;
    }

    public void setPvpTotalGames(int pvpTotalGames) {
        this.pvpTotalGames = pvpTotalGames;
    }

    public int getPvpTotalWin() {
        return pvpTotalWin;
    }

    public void setPvpTotalWin(int pvpTotalWin) {
        this.pvpTotalWin = pvpTotalWin;
    }

    public int getPvpTotalLose() {
        return pvpTotalLose;
    }

    public void setPvpTotalLose(int pvpTotalLose) {
        this.pvpTotalLose = pvpTotalLose;
    }

    public int getPvpAnswer() {
        return pvpAnswer;
    }

    public void setPvpAnswer(int pvpAnswer) {
        this.pvpAnswer = pvpAnswer;
    }

    public int getPvpAnswerTrue() {
        return pvpAnswerTrue;
    }

    public void setPvpAnswerTrue(int pvpAnswerTrue) {
        this.pvpAnswerTrue = pvpAnswerTrue;
    }

    public int getPvpAnswerFalse() {
        return pvpAnswerFalse;
    }

    public void setPvpAnswerFalse(int pvpAnswerFalse) {
        this.pvpAnswerFalse = pvpAnswerFalse;
    }

    public int getTimeTrialTotalGames() {
        return timeTrialTotalGames;
    }

    public void setTimeTrialTotalGames(int timeTrialTotalGames) {
        this.timeTrialTotalGames = timeTrialTotalGames;
    }

    public int getTimeTrialTotalWin() {
        return timeTrialTotalWin;
    }

    public void setTimeTrialTotalWin(int timeTrialTotalWin) {
        this.timeTrialTotalWin = timeTrialTotalWin;
    }

    public int getTimeTrialTotalLose() {
        return timeTrialTotalLose;
    }

    public void setTimeTrialTotalLose(int timeTrialTotalLose) {
        this.timeTrialTotalLose = timeTrialTotalLose;
    }

    public int getTimeTrialAnswer() {
        return timeTrialAnswer;
    }

    public void setTimeTrialAnswer(int timeTrialAnswer) {
        this.timeTrialAnswer = timeTrialAnswer;
    }

    public int getTimeTrialAnswerTrue() {
        return timeTrialAnswerTrue;
    }

    public void setTimeTrialAnswerTrue(int timeTrialAnswerTrue) {
        this.timeTrialAnswerTrue = timeTrialAnswerTrue;
    }

    public int getTimeTrialAnswerFalse() {
        return timeTrialAnswerFalse;
    }

    public void setTimeTrialAnswerFalse(int timeTrialAnswerFalse) {
        this.timeTrialAnswerFalse = timeTrialAnswerFalse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
