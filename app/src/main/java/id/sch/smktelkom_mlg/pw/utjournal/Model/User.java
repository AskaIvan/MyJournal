package id.sch.smktelkom_mlg.pw.utjournal.Model;

/**
 * Created by Widya on 1/4/2018.
 */

public class User {

    private String username;
    private String password;
    private String email;
    private String branch;
    private String area;
    private String nrp;

    public User(){

    }

    public User(String username, String password, String email, String branch, String area, String nrp) {
        //this.username = username;
        this.password = password ;
        this.email = email;
        this.branch = branch;
        this.area = area;
        this.nrp = nrp;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getBranch(){
        return branch;
    }

    public void setBranch(String branch){
        this.branch = branch;
    }

    public String getArea(){
        return area;
    }

    public void setArea(String area){
        this.area = area;
    }

    public String getNrp(){
        return nrp;
    }

    public void setNrp(String nrp){
        this.nrp = nrp;
    }

}
