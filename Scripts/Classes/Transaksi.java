public class Transaksi {
    private int idTransaksi;
    private int jumlahItem;
    private String tanggalPinjam;
    private String tanggalKembali;
    private Client client;
    private Pustakawan pustakawan;

    public Transaksi() {
    }

    public Transaksi(int idTransaksi, int jumlahItem, String tanggalPinjam, String tanggalKembali, Client client, Pustakawan pustakawan) {
        this.idTransaksi = idTransaksi;
        this.jumlahItem = jumlahItem;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.client = client;
        this.pustakawan = pustakawan;
    }

    public int getIdTransaksi() {
        return this.idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getJumlahItem() {
        return this.jumlahItem;
    }

    public void setJumlahItem(int jumlahItem) {
        this.jumlahItem = jumlahItem;
    }

    public String getTanggalPinjam() {
        return this.tanggalPinjam;
    }

    public void setTanggalPinjam(String tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public String getTanggalKembali() {
        return this.tanggalKembali;
    }

    public void setTanggalKembali(String tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Pustakawan getPustakawan() {
        return this.pustakawan;
    }

    public void setPustakawan(Pustakawan pustakawan) {
        this.pustakawan = pustakawan;
    }

}
