public class Pustakawan {
    private int idPustakawan;
    private String nama;
    private String email;
    private char jenisKelamin;
    private String[] noTelp;
    private String username;
    private String password;

    public Pustakawan() {
    }

    public Pustakawan(int idPustakawan, String nama, String email, char jenisKelamin, String[] noTelp, String username, String password) {
        this.idPustakawan = idPustakawan;
        this.nama = nama;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.noTelp = noTelp;
        this.username = username;
        this.password = password;
    }

    public void showDetails(){
        int i = 1;
        System.out.println("Your Details:");
        System.out.println("Nama: " + nama);
        System.out.println("Email: " + email);
        System.out.println("Jenis Kelamin: " + jenisKelamin);
        System.out.println("Nomor Telepon: ");
        for(String telp : noTelp){
            System.out.println(i + ". " + telp);
            i++;
        }
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    public boolean checkNull(){
        if(this.password != null){
            return true;
        } else{
            return false;
        }
    }

    public int getIdPustakawan() {
        return this.idPustakawan;
    }

    public void setIdPustakawan(int idPustakawan) {
        this.idPustakawan = idPustakawan;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char isjenisKelamin() {
        return this.jenisKelamin;
    }

    public char getjenisKelamin() {
        return this.jenisKelamin;
    }

    public void setjenisKelamin(char jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String[] getNoTelp() {
        return this.noTelp;
    }

    public void setNoTelp(String[] noTelp) {
        this.noTelp = noTelp;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
