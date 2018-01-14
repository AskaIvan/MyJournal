package id.sch.smktelkom_mlg.pw.utjournal.Model;

/**
 * Created by selina on 1/7/2018.
 */

public class Journal {

    private String codejob;
    private String category;
    private String codecategory;
    private String activity;
    private String description;
    private String sapsomp;
    private String month;
    private String start;
    private String end;
    private String hours;
    private String venue;
    private String vendor;
    private String unittype;
    private String remark;

    public Journal() {
    }
    public Journal(String codejob, String codecategory, String category, String activity, String description, String sapsomp, String month, String start, String end, String hours, String venue, String vendor, String unittype, String remark){
        this.codejob = codejob;
        this.category = category;
        this.codecategory = codecategory;
        this.activity = activity;
        this.description = description;
        this.sapsomp = sapsomp;
        this.month = month;
        this.start = start;
        this.end = end;
        this.hours = hours;
        this.venue = venue;
        this.vendor = vendor;
        this.unittype = unittype;
        this.remark = remark;
    }

    public String getCodejob(){
        return codejob;
    }

    public void setCodejob(String codejob){
        this.codejob = this.codejob;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = this.category;
    }

    public String getCodecategory(){
        return codecategory;
    }

    public void setCodecategory(String codecategory) {
        this.codecategory = codecategory;
    }

    public String getActivity(){
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSapsomp(){
        return sapsomp;
    }

    public void setSapsomp(String sapsomp) {
        this.sapsomp = sapsomp;
    }

    public String getMonth(){
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStart(){
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd(){
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getHours(){
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getVenue(){
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVendor(){
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getUnittype(){
        return unittype;
    }

    public void setUnittype(String unittype) {
        this.unittype = unittype;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
