import java.text.SimpleDateFormat;
import java.util.Date;

public class Denda {
    private Pengembalian pengembalian;
    private int dendaTelat;
    private int dendaRusak;
    private boolean isBayar;
    final private int HARGATELAT = 3000;
    final private int HARGARUSAK = 6000;

    public Denda() {
    }

    public Denda(Pengembalian pengembalian, boolean isTelat) {
        this.pengembalian = pengembalian;
        if(isTelat){
            String tglKembali = getPengembalian().getPeminjaman().getTransaksi().getTanggalKembali();
            String tglPinjam = getPengembalian().getPeminjaman().getTransaksi().getTanggalPinjam();
            this.dendaTelat = dateDifference(tglKembali, tglPinjam) * HARGATELAT;
        }
        if(pengembalian.getIsRusak()){
            this.dendaRusak = pengembalian.getPeminjaman().getJumlahPinjam() * HARGARUSAK;
        }
        this.isBayar = false;
    }

    public static int dateDifference(String date1, String date2) {
        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        try{ 
            Date tgl1 = obj.parse(date1);
            Date tgl2 = obj.parse(date2);
            long time_difference = tgl1.getTime() - tgl2.getTime(); 
            int days_difference = (int) (time_difference / (1000*60*60*24)) % 365;
            return days_difference;
        } catch(Exception e){
            System.err.println("Something went wrong!");
            return 0;
        }
    }

    public Pengembalian getPengembalian() {
        return this.pengembalian;
    }

    public void setPengembalian(Pengembalian pengembalian) {
        this.pengembalian = pengembalian;
    }

    public int getDendaTelat() {
        return this.dendaTelat;
    }

    public void setDendaTelat(int dendaTelat) {
        this.dendaTelat = dendaTelat;
    }

    public int getDendaRusak() {
        return this.dendaRusak;
    }

    public void setDendaRusak(int dendaRusak) {
        this.dendaRusak = dendaRusak;
    }

    public boolean getIsBayar() {
        return this.isBayar;
    }

    public void setIsBayar(boolean isBayar) {
        this.isBayar = isBayar;
    }

}
