import java.util.ArrayList;
public class Client {
    private String idPengenal;
    private String nama;
    private String fakultas;
    private String prodi;
    private String posisi;
    private String noTelp[];
    private char jenisKelamin;
    private String username;
    private String password;

    public Client() {
    }

    public Client(String idPengenal, String nama, String fakultas, String prodi, String posisi, String[] noTelp, char jenisKelamin, String username, String password) {
        this.idPengenal = idPengenal;
        this.nama = nama;
        this.fakultas = fakultas;
        this.prodi = prodi;
        this.posisi = posisi;
        this.noTelp = noTelp;
        this.jenisKelamin = jenisKelamin;
        this.username = username;
        this.password = password;
    }

    public void showDetails(ArrayList<Denda> dendaList, ArrayList<Pembayaran> pembayaranList) {
        int i = 1, totalDenda = 0;
        System.out.println("Your Details:");
        System.out.println(((posisi.matches("Mahasiswa")) ? "NIM: " : "NIK: ") + idPengenal);
        System.out.println("Nama: " + nama);
        System.out.println("Fakultas: " + fakultas);
        System.out.println("Prodi: " + prodi);
        System.out.println("Posisi: " + posisi);
        System.out.println("Nomor Telepon: ");
        for(String telp : noTelp){
            System.out.println(i + ". " + telp);
            i++;
        }
        System.out.println("Jenis Kelamin: " + jenisKelamin);
        if(dendaList.size() > 0){
            for(Denda denda : dendaList){
                if(denda.getPengembalian().getPeminjaman().getTransaksi().getClient() == this){
                    if(!denda.getIsBayar())totalDenda += getDendaSisa(denda, pembayaranList);
                }
            }
        }
        System.out.println("Total Denda: " + totalDenda);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    public static int getDendaSisa(Denda denda, ArrayList<Pembayaran> pembayaranList){
        int dendaSisa = (denda.getDendaRusak() + denda.getDendaTelat());
        for(Pembayaran pembayaran : pembayaranList){
            if(pembayaran.getDenda() == denda){
                dendaSisa -= pembayaran.getJumlahBayar();
            }
        }
        return dendaSisa;
    }

    public boolean checkNull(){
        if(this.password != null){
            return true;
        } else{
            return false;
        }
    }
    
    public String getIdPengenal() {
        return this.idPengenal;
    }

    public void setIdPengenal(String idPengenal) {
        this.idPengenal = idPengenal;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFakultas() {
        return this.fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getProdi() {
        return this.prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getPosisi() {
        return this.posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String[] getNoTelp() {
        return this.noTelp;
    }

    public void setNoTelp(String[] noTelp) {
        this.noTelp = noTelp;
    }

    public char getJenisKelamin() {
        return this.jenisKelamin;
    }

    public void setJenisKelamin(char jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
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
