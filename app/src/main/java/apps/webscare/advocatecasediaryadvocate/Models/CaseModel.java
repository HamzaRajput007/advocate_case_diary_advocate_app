package apps.webscare.advocatecasediaryadvocate.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaseModel {
    @SerializedName("Advocate UD")
    @Expose
    private String advocateUD;
    @SerializedName("Advocate name ")
    @Expose
    private String advocateName;
    @SerializedName("Case Act Appllied")
    @Expose
    private String caseActAppllied;
    @SerializedName("Case Filer Address")
    @Expose
    private String caseFilerAddress;
    @SerializedName("Case Filer CNIC")
    @Expose
    private String caseFilerCNIC;
    @SerializedName("Case Filer Name")
    @Expose
    private String caseFilerName;
    @SerializedName("Case Filer Phone")
    @Expose
    private String caseFilerPhone;
    @SerializedName("Case Full Description")
    @Expose
    private String caseFullDescription;
    @SerializedName("Judge Name")
    @Expose
    private String judgeName;
    @SerializedName("Opponent Phone")
    @Expose
    private String opponentPhone;
    @SerializedName("Opponent Address")
    @Expose
    private String opponentAddress;
    @SerializedName("Opponent Name")
    @Expose
    private String opponentName;

    public String getAdvocateUD() {
        return advocateUD;
    }

    public void setAdvocateUD(String advocateUD) {
        this.advocateUD = advocateUD;
    }

    public String getAdvocateName() {
        return advocateName;
    }

    public void setAdvocateName(String advocateName) {
        this.advocateName = advocateName;
    }

    public String getCaseActAppllied() {
        return caseActAppllied;
    }

    public void setCaseActAppllied(String caseActAppllied) {
        this.caseActAppllied = caseActAppllied;
    }

    public String getCaseFilerAddress() {
        return caseFilerAddress;
    }

    public void setCaseFilerAddress(String caseFilerAddress) {
        this.caseFilerAddress = caseFilerAddress;
    }

    public String getCaseFilerCNIC() {
        return caseFilerCNIC;
    }

    public void setCaseFilerCNIC(String caseFilerCNIC) {
        this.caseFilerCNIC = caseFilerCNIC;
    }

    public String getCaseFilerName() {
        return caseFilerName;
    }

    public void setCaseFilerName(String caseFilerName) {
        this.caseFilerName = caseFilerName;
    }

    public String getCaseFilerPhone() {
        return caseFilerPhone;
    }

    public void setCaseFilerPhone(String caseFilerPhone) {
        this.caseFilerPhone = caseFilerPhone;
    }

    public String getCaseFullDescription() {
        return caseFullDescription;
    }

    public void setCaseFullDescription(String caseFullDescription) {
        this.caseFullDescription = caseFullDescription;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public String getOpponentPhone() {
        return opponentPhone;
    }

    public void setOpponentPhone(String opponentPhone) {
        this.opponentPhone = opponentPhone;
    }

    public String getOpponentAddress() {
        return opponentAddress;
    }

    public void setOpponentAddress(String opponentAddress) {
        this.opponentAddress = opponentAddress;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

}
