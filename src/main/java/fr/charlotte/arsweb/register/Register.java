package fr.charlotte.arsweb.register;

public class Register {

    private String username, password, vessel, email, name, scc;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScc() {
        return scc;
    }

    public void setScc(String scc) {
        this.scc = scc;
    }

    public static String translateScc(int scc) {
        return "SCC#" + scc;
    }

    @Override
    public String toString() {
        return "Register{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", vessel='" + vessel + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", scc='" + scc + '\'' +
                '}';
    }
}
