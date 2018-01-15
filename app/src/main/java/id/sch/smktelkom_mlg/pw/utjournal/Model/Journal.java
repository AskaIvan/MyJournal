package id.sch.smktelkom_mlg.pw.utjournal.Model;

/**
 * Created by selina on 1/7/2018.
 */

public class Journal {

    private String mcodejob;
    private String mcategory;
    private String mcodecategory;
    private String mactivity;
    private String mdescription;
    private String msapsomp;
    private String mmonth;
    private String mstart;
    private String mend;
    private String mhours;
    private String mvenue;
    private String mvendor;
    private String munittype;
    private String mremark;

    public Journal() {
    }

    public Journal(String codejob, String codecategory, String category, String activity, String description, String sapsomp, String month, String start, String end, String hours, String venue, String vendor, String unittype, String remark){
        mcodejob = codejob;
        mcategory = category;
        mcodecategory = codecategory;
        mactivity = activity;
        mdescription = description;
        msapsomp = sapsomp;
        mmonth = month;
        mstart = start;
        mend = end;
        mhours = hours;
        mvenue = venue;
        mvendor = vendor;
        munittype = unittype;
        mremark = remark;
    }

    public String getCodejob(){
        return mcodejob;
    }

    public void setCodejob(String codejob){
        mcodejob = codejob;
    }

    public String getCategory(){
        return mcategory;
    }

    public void setCategory(String category){
        mcategory = category;
    }

    public String getCodecategory(){
        return mcodecategory;
    }

    public void setCodecategory(String codecategory) {
        mcodecategory = codecategory;
    }

    public String getActivity(){
        return mactivity;
    }

    public void setActivity(String activity) {
        mactivity = activity;
    }

    public String getDescription(){
        return mdescription;
    }

    public void setDescription(String description) {
        mdescription = description;
    }

    public String getSapsomp(){
        return msapsomp;
    }

    public void setSapsomp(String sapsomp) {
        msapsomp = sapsomp;
    }

    public String getMonth(){
        return mmonth;
    }

    public void setMonth(String month) {
        mmonth = month;
    }

    public String getStart(){
        return mstart;
    }

    public void setStart(String start) {
        mstart = start;
    }

    public String getEnd(){
        return mend;
    }

    public void setEnd(String end) {
        mend = end;
    }

    public String getHours(){
        return mhours;
    }

    public void setHours(String hours) {
        mhours = hours;
    }

    public String getVenue(){
        return mvenue;
    }

    public void setVenue(String venue) {
        mvenue = venue;
    }

    public String getVendor(){
        return mvendor;
    }

    public void setVendor(String vendor) {
        mvendor = vendor;
    }

    public String getUnittype(){
        return munittype;
    }

    public void setUnittype(String unittype) {
        munittype = unittype;
    }

    public String getRemark(){
        return mremark;
    }

    public void setRemark(String remark) {
        mremark = remark;
    }
}
