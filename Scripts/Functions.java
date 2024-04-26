import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;  
import java.util.Date;   
public class Functions{
    static Scanner input = new Scanner(System.in);
    static ArrayList<Client> clientList = new ArrayList<Client>();
    static ArrayList<Pustakawan> pustakawanList = new ArrayList<Pustakawan>();
    static ArrayList<Transaksi> transaksiList = new ArrayList<Transaksi>();
    static ArrayList<Pembayaran> pembayaranList = new ArrayList<Pembayaran>();
    static ArrayList<Peminjaman> peminjamanList = new ArrayList<Peminjaman>();
    static ArrayList<Pengembalian> pengembalianList = new ArrayList<Pengembalian>();
    static ArrayList<Denda> dendaList = new ArrayList<Denda>();
    static ArrayList<Koleksi> koleksiList = new ArrayList<Koleksi>();
    static int masaPinjamanDosen = 7;
    static int masaPinjamanStaff = 5;
    static int masaPinjamanMahasiswa = 3;
    static final int mataUang = 100;
    static String[] clientNav = new String[]{
        "My Info",
        "My Transaction",
        "Logout"
    };
    static String[] pustakawanNav = new String[]{
        "My Info",
        "Add User",
        "Add Collection",
        "Borrow Collection",
        "Return Collection",
        "Pay Debt",
        "List User",
        "List Koleksi",
        "List Transaksi",
        "Logout"
    };

    public static void navClient(Client client) throws IOException{
        String pilih = enumerator("Please Choose an Action:", clientNav);
        boolean isLoggedin = true;
        switch(pilih){
            case "Logout":
                isLoggedin = false;
                login();
                break;
            case "My Transaction":
                listTransaksi(false, getTransaksiByUser(client));
                break;
            default:
                client.showDetails(dendaList, pembayaranList);
        }
        if(isLoggedin)navClient(client);
    }

    public static void navPustakawan(Pustakawan pustakawan) throws IOException{
        String pilih = enumerator("Please Choose an Action:", pustakawanNav);
        boolean isLoggedin = true;
        switch(pilih){
            case "Logout":
                isLoggedin = false;
                mainMenu();
                break;
            case "Add User":
                addUser();
                break;
            case "Add Collection":
                addKoleksi();
                break;
            case "Borrow Collection":
                pinjamKoleksi(pustakawan);
                break;
            case "Return Collection":
                kembalikanKoleksi(pustakawan);
                break;
            case "List User":
                listUser(true);
                break;
            case "List Koleksi":
                listKoleksi(true);
                break;
            case "List Transaksi":
                listTransaksi(true);
                break;
            case "Pay Debt":
                bayarDenda();
                break;
            default:
                pustakawan.showDetails();
        }
        if(isLoggedin)navPustakawan(pustakawan);
    }

    public static void login() throws IOException{
        System.out.println("\nLogin:");
        String username = inputText("Masukkan Username: ", true);
        String password = inputText("Masukkan Password: ", true);
        String checker = loginCheck(username);
        if(checker.matches("client")){
            Client client = loginClient(username, password);
            if(client.checkNull()){
                System.out.println("\nWelcome, " + client.getNama());
                navClient(client);
            } else{
                System.out.println("\nUsername or Password Invalid!");
                login();
            }
        } else if(checker.matches("pustakawan")){
            Pustakawan pustakawan = loginPustakawan(username, password);
            if(pustakawan.checkNull()){
                System.out.println("\nWelcome, " + pustakawan.getNama());
                navPustakawan(pustakawan);
            } else{
                System.out.println("\nUsername or Password Invalid!");
                login();
            }
        } else{
            System.out.println("\nUsername Not Found!");
            login();
        }
    }

    public static void mainMenu() throws IOException{
        String pilih = enumerator("\nChoose Your Action:", new String[] {"Login", "Exit"});
        switch(pilih){
            case "Login":
                login();
                break;
            case "Exit":
                System.out.println("Bye!");
                System.exit(0);
                break;
        }
        mainMenu();
    }

    public static void listUser(boolean printFile) throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "ID Pengenal",
            "Nama",
            "Fakultas",
            "Prodi",
            "Posisi",
            "Jenis Kelamin",
            "Total Denda",
            "Username",
            "Password"
        };
        Object[][] rowData = dataGenerator(clientList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            int totalDenda = 0;
            Client data = clientList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getIdPengenal();
            rowData[i][2] = data.getNama();
            rowData[i][3] = data.getFakultas();
            rowData[i][4] = data.getProdi();
            rowData[i][5] = data.getPosisi();
            rowData[i][6] = data.getJenisKelamin();
            if(dendaList.size() > 0){
                for(Denda denda : dendaList){
                    if(denda.getPengembalian().getPeminjaman().getTransaksi().getClient() == data){
                        if(!denda.getIsBayar())totalDenda += getDendaSisa(denda);
                    }
                }
            }
            rowData[i][7] = totalDenda;
            rowData[i][8] = data.getUsername();
            rowData[i][9] = data.getPassword();
        }
        generateTable("Clients", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        if(printFile)printTXT("Clients", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Users.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void listKoleksi(boolean printFile) throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "Judul",
            "Tahun Terbit",
            "Kategori",
            "Edisi",
            "Jenis",
            "Stok Tersedia"
        };
        Object[][] rowData = dataGenerator(koleksiList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Koleksi data = koleksiList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getJudul();
            rowData[i][2] = data.getTahunTerbit();
            rowData[i][3] = data.getKategori();
            if(data instanceof BukuMajalah){
                BukuMajalah buku = (BukuMajalah) data;
                rowData[i][4] = buku.getEdisi();
                rowData[i][5] = "Buku/Majalah";
            } else{
                rowData[i][4] = "";
                rowData[i][5] = "CD";
            }
            rowData[i][6] = data.getStokTersedia();
        }
        generateTable("Collections", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        if(printFile)printTXT("Collections", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Collections.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void listKoleksi(boolean printFile, ArrayList<Koleksi> koleksiList) throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "Judul",
            "Tahun Terbit",
            "Kategori",
            "Edisi",
            "Jenis",
            "Total Stok",
            "ISBN/ISSN",
            "Stok Tersedia"
        };
        Object[][] rowData = dataGenerator(koleksiList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Koleksi data = koleksiList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getJudul();
            rowData[i][2] = data.getTahunTerbit();
            rowData[i][3] = data.getKategori();
            if(data instanceof BukuMajalah){
                BukuMajalah buku = (BukuMajalah) data;
                rowData[i][4] = buku.getEdisi();
                rowData[i][5] = "Buku/Majalah";
                rowData[i][7] = buku.getISBN();
            } else{
                rowData[i][4] = "";
                rowData[i][5] = "CD";
                rowData[i][7] = "";
            }
            rowData[i][6] = data.getStok();
            rowData[i][8] = data.getStokTersedia();
        }
        generateTable("Collections", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        if(printFile)printTXT("Collections", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Collections.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void listTransaksi(boolean printFile) throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "ID Transaksi",
            "Jumlah Item",
            "Tanggal Peminjaman",
            "Tanggal Pengembalian",
            "Nama Client",
            "Nama Pustakawan"
        };
        Object[][] rowData = dataGenerator(transaksiList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Transaksi data = transaksiList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getIdTransaksi();
            rowData[i][2] = data.getJumlahItem();
            rowData[i][3] = data.getTanggalPinjam();
            rowData[i][4] = data.getTanggalKembali();
            rowData[i][5] = data.getClient().getNama();
            rowData[i][6] = data.getPustakawan().getNama();
        }
        generateTable("Transactions", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        if(printFile)printTXT("Transactions", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Transactions.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void listTransaksi(boolean printFile, ArrayList<Transaksi> transaksiList) throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "ID Transaksi",
            "Jumlah Item",
            "Tanggal Peminjaman",
            "Tanggal Pengembalian",
            "Nama Client",
            "Nama Pustakawan"
        };
        Object[][] rowData = dataGenerator(transaksiList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Transaksi data = transaksiList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getIdTransaksi();
            rowData[i][2] = data.getJumlahItem();
            rowData[i][3] = data.getTanggalPinjam();
            rowData[i][4] = data.getTanggalKembali();
            rowData[i][5] = data.getClient().getNama();
            rowData[i][6] = data.getPustakawan().getNama();
        }
        generateTable("Transactions", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        if(printFile)printTXT("Transactions", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Transactions.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void listDenda(ArrayList<Denda> dendaList){
        Object[] columnNames = new Object[]{
            "No",
            "ID Transaksi",
            "Denda Telat",
            "Denda Rusak",
            "Total Denda",
            "Denda Sisa",
            "Kali Bayar"
        };
        Object[][] rowData = dataGenerator(dendaList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Denda data = dendaList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getPengembalian().getPeminjaman().getTransaksi().getIdTransaksi();
            rowData[i][2] = data.getDendaTelat();
            rowData[i][3] = data.getDendaRusak();
            rowData[i][4] = (data.getDendaRusak() + data.getDendaTelat());
            rowData[i][5] = getDendaSisa(data);
            rowData[i][6] = getTotalBayar(data) + " Kali Bayar";
        }
        generateTable("Debt Details", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void bayarDenda() throws IOException{
        ArrayList<Denda> dendaNunggak = getDendaNunggak();
        if(dendaNunggak.size() > 0){
            if(clientList.size() > 0){
                int userIndex = 0;
                String[] users = new String[clientList.size()];
                for(int i = 0; i < users.length; i++){
                    users[i] = clientList.get(i).getIdPengenal();
                }
                listUser(false);
                String idUser = enumeratorSilence(users);
                for(int j = 0; j < users.length; j++){
                    if(idUser.matches(users[j])){
                        userIndex = j;
                    }
                }
                Client client = clientList.get(userIndex);
                dendaNunggak = getDendaByUser(true, client);
                int dendaIndex = 0;
                String[] dendas = new String[dendaNunggak.size()];
                for(int i = 0; i < dendas.length; i++){
                    dendas[i] = Integer.toString(i);
                }
                listDenda(dendaNunggak);
                String idDenda = enumeratorSilence(dendas);
                for(int j = 0; j < dendas.length; j++){
                    if(idDenda.matches(dendas[j])){
                        dendaIndex = j;
                    }
                }
                Denda denda = dendaNunggak.get(dendaIndex);
                int dendaSisa = getDendaSisa(denda);
                pembayaranList.add(new Pembayaran(denda, enumerator("Jenis Pembayaran:", new String[]{"Tunai", "QRIS", "Debit", "Kredit"}), inputUang("Jumlah Bayar: ", dendaSisa)));
                if(getDendaSisa(denda) == 0){
                    denda.setIsBayar(true);
                }
            } else{
                System.out.println("No Client Available");
            }
        } else{
            System.out.println("No Debt Available or All Cleared");
        }
    }

    public static int getDendaSisa(Denda denda){
        int dendaSisa = (denda.getDendaRusak() + denda.getDendaTelat());
        for(Pembayaran pembayaran : pembayaranList){
            if(pembayaran.getDenda() == denda){
                dendaSisa -= pembayaran.getJumlahBayar();
            }
        }
        return dendaSisa;
    }

    public static int getTotalBayar(Denda denda){
        int totalBayar = 0;
        for(Pembayaran pembayaran : pembayaranList){
            if(pembayaran.getDenda() == denda){
                totalBayar++;
            }
        }
        return totalBayar;
    }

    public static ArrayList<Denda> getDendaNunggak(){
        ArrayList<Denda> dendaNunggak = new ArrayList<Denda>();
        for(Denda denda : dendaList){
            if(!denda.getIsBayar()){
                dendaNunggak.add(denda);
            }
        }
        return dendaNunggak;
    }

    public static ArrayList<Denda> getDendaByUser(boolean nunggak, Client client){
        ArrayList<Denda> dendaClient = new ArrayList<Denda>();
        for(Denda denda : dendaList){
            if(nunggak){
                if(!denda.getIsBayar() && denda.getPengembalian().getPeminjaman().getTransaksi().getClient() == client){
                    dendaClient.add(denda);
                }
            } else{
                if(denda.getPengembalian().getPeminjaman().getTransaksi().getClient() == client){
                    dendaClient.add(denda);
                }
            }
        }
        return dendaClient;
    }

    public static void kembalikanKoleksi(Pustakawan pustakawan) throws IOException{
        if(clientList.size() > 0){
            int userIndex = 0;
            String[] users = new String[clientList.size()];
            for(int i = 0; i < users.length; i++){
                users[i] = clientList.get(i).getIdPengenal();
            }
            listUser(false);
            String idUser = enumeratorSilence(users);
            for(int j = 0; j < users.length; j++){
                if(idUser.matches(users[j])){
                    userIndex = j;
                }
            }
            Client client = clientList.get(userIndex);
            ArrayList<Transaksi> dataTransaksi = getTransaksiByUser(client);
            if(dataTransaksi.size() > 0){
                String[] transactions = new String[dataTransaksi.size()];
                listTransaksi(false, dataTransaksi);
                for(int i = 0; i < transactions.length; i++){
                    transactions[i] = Integer.toString(dataTransaksi.get(i).getIdTransaksi());
                }
                int pilihanTransaksi = Integer.parseInt(enumeratorSilence(transactions));
                Transaksi transaksi = getTransaksiByID(pilihanTransaksi);
                String tanggalKembali = inputDate("Tanggal Pengembalian");
                transaksi.setTanggalKembali(tanggalKembali);
                transaksi.setStatus("Pengembalian");
                boolean isTelat = false;
                int durasiPinjam = (client.getPosisi().matches("Mahasiswa")) ? masaPinjamanMahasiswa : (client.getPosisi().matches("Dosen")) ? masaPinjamanDosen : masaPinjamanStaff;
                if(dateDifference(dateAdder(tanggalKembali, durasiPinjam), tanggalKembali) > durasiPinjam){
                    isTelat = true;
                }
                ArrayList<Peminjaman> peminjamans = getPeminjamanByTransaksi(transaksi, client);
                for(Peminjaman peminjaman : peminjamans){
                    Koleksi koleksi = peminjaman.getKoleksi();
                    Object[] columnNames = new Object[]{
                        "Nama Koleksi",
                        "Jenis Koleksi",
                        "Jumlah Pinjaman",
                        (koleksi instanceof BukuMajalah) ? "ISBN/ISSN" : "ID Koleksi"
                    };
                    Object[][] rowData = new Object[][]{{
                        koleksi.getJudul(),
                        ((koleksi instanceof BukuMajalah) ? "Buku/Majalah" : "CD"),
                        peminjaman.getJumlahPinjam(),
                        (koleksi instanceof BukuMajalah) ? ((BukuMajalah) koleksi).getISBN() : koleksi.getIdKoleksi()
                    }};
                    generateTable("Collection Details", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
                    boolean isRusak = inputBool("Koleksi Rusak", "Y", "N");
                    pengembalianList.add(new Pengembalian(peminjaman, isRusak, peminjaman.getKoleksi(), (isRusak) ? inputInt("Jumlah Rusak: ", 1, peminjaman.getJumlahPinjam()) : 0));
                    Pengembalian pengembalian = pengembalianList.getLast();
                    koleksi.setStokTersedia(koleksi.getStok() + peminjaman.getJumlahPinjam());
                    if(isRusak || isTelat){
                        dendaList.add(new Denda(pengembalian, isTelat));
                    }
                }
                if(checkDendaByTransaksi(transaksi))transaksi.setStatus("Lunas");
            }
        }
    }

    public static boolean checkDendaByTransaksi(Transaksi transaksi){
        for(Denda denda : dendaList){
            if(!denda.getIsBayar() && denda.getPengembalian().getPeminjaman().getTransaksi() == transaksi)return false;
        }
        return true;
    }

    public static Transaksi getTransaksiByID(int idTransaksi){
        for(Transaksi transaksi : transaksiList){
            if(transaksi.getIdTransaksi() == idTransaksi){
                return transaksi;
            }
        }
        return new Transaksi();
    }

    public static ArrayList<Peminjaman> getPeminjamanByTransaksi(Transaksi transaksi, Client client){
        ArrayList<Peminjaman> listData = new ArrayList<Peminjaman>();
        for(Peminjaman peminjaman : peminjamanList){
            if(peminjaman.getTransaksi().getClient() == client && peminjaman.getTransaksi() == transaksi){
                listData.add(peminjaman);
            }
        }
        return listData;
    }

    public static ArrayList<Transaksi> getTransaksiByUser(Client client){
        ArrayList<Transaksi> listData = new ArrayList<Transaksi>();
        for(Transaksi transaksi : transaksiList){
            if(transaksi.getClient() == client){
                listData.add(transaksi);
            }
        }
        return listData;
    }

    public static void pinjamKoleksi(Pustakawan pustakawan) throws IOException{
        if(koleksiList.size() > 0 && clientList.size() > 0){
            int userIndex = 0;
            String[] users = new String[clientList.size()];
            for(int i = 0; i < users.length; i++){
                users[i] = clientList.get(i).getIdPengenal();
            }
            listUser(false);
            String idUser = enumeratorSilence(users);
            for(int j = 0; j < users.length; j++){
                if(idUser.matches(users[j])){
                    userIndex = j;
                }
            }
            transaksiList.add(new Transaksi(generateID("transaksi"), 1, inputDate("Tanggal Transaksi"), "", clientList.get(userIndex), pustakawan));
            addPinjamKoleksi();
            int jumlahItem = 1;
            while(enumerator("Action:", new String[]{"Pilih Koleksi", "Done"}).matches("Pilih Koleksi")){
                addPinjamKoleksi();
                jumlahItem++;
            }
            transaksiList.getLast().setJumlahItem(jumlahItem);
        } else{
            System.out.println("Collection or Client data not found!");
        }
    }

    public static String[] koleksiChecker(){
        boolean hasBuku = false;
        boolean hasCD = false;
        for(Koleksi koleksi : koleksiList){
            if(koleksi instanceof BukuMajalah)hasBuku = true;
            if(koleksi instanceof CD)hasCD = true;
            if(hasBuku && hasCD)return new String[]{"Buku/Majalah", "CD"};
        }
        if(hasBuku)return new String[]{"Buku/Majalah"};
        if(hasCD)return new String[]{"CD"};
        return new String[]{};
    }

    public static void addPinjamKoleksi() throws IOException{
        int collectionIndex = 0;
        ArrayList<Koleksi> dataList = new ArrayList<Koleksi>();
        String jenisKoleksi = enumerator("Jenis Koleksi:", koleksiChecker());
        String[] collections = new String[koleksiList.size()];
        for(int k = 0; k < collections.length; k++){
            if(jenisKoleksi.matches("CD")){
                if(koleksiList.get(k) instanceof CD && koleksiList.get(k).getStokTersedia() > 0){
                    collections[k] = Integer.toString(koleksiList.get(k).getIdKoleksi());
                    dataList.add(koleksiList.get(k));
                }
            } else if(jenisKoleksi.matches("Buku/Majalah")){
                if(koleksiList.get(k) instanceof BukuMajalah && koleksiList.get(k).getStokTersedia() > 0){
                    collections[k] = Integer.toString(koleksiList.get(k).getIdKoleksi());
                    dataList.add(koleksiList.get(k));
                }
            }
        }
        listKoleksi(false, dataList);
        String idKoleksi = enumeratorSilence(collections);
        for(int j = 0; j < collections.length; j++){
            if(idKoleksi.matches(collections[j])){
                collectionIndex = j;
            }
        }
        int jumlahPinjam = inputInt("Jumlah Pinjaman Koleksi Ini: ", 1, koleksiList.get(collectionIndex).getStokTersedia());
        peminjamanList.add(new Peminjaman(transaksiList.getLast(), jumlahPinjam, koleksiList.get(collectionIndex)));
        koleksiList.get(collectionIndex).setStokTersedia(koleksiList.get(collectionIndex).getStok() - jumlahPinjam);
    }

    public static void addKoleksi(){
        String pilih = enumerator("Jenis Koleksi:", new String[]{"Buku/Majalah", "CD"});
        String[] kategoriBuku = new String[]{
            "Romance",
            "Action"
        };
        String[] kategoriCD = new String[]{
            "Pop",
            "Rock"
        };
        String[] edisi = new String[]{
            "Saku",
            "Jilid"
        };
        switch(pilih){
            case "Buku/Majalah":
                koleksiList.add(new BukuMajalah(generateID("koleksi"), inputInt("Masukkan Stock: "), inputText("Masukkan Judul: ", false), inputInt("Masukkan Tahun Terbit: ", 1900), enumerator("Pilih Genre Buku:", kategoriBuku), enumerator("Pilih Edisi Buku:", edisi), uniqueKeyMaker("ISBN/ISSN")));
                break;
            case "CD":
                koleksiList.add(new CD(generateID("koleksi"), inputInt("Masukkan Stock: "), inputText("Masukkan Judul: ", false), inputInt("Masukkan Tahun Terbit: ", 1900), enumerator("Pilih Genre CD:", kategoriCD)));
                break;
        }
    }

    public static Client loginClient(String username, String password){
        for(Client client : clientList){
            if(client.getUsername().matches(username) && client.getPassword().matches(password)){
                return client;
            }
        }
        return new Client();
    }

    public static Pustakawan loginPustakawan(String username, String password){
        for(Pustakawan pustakawan : pustakawanList){
            if(pustakawan.getUsername().matches(username) && pustakawan.getPassword().matches(password)){
                return pustakawan;
            }
        }
        return new Pustakawan();
    }

    public static String loginCheck(String username){
        if(clientList.size() > 0){
            for(int i = 0; i < clientList.size(); i++){
                if(username.toLowerCase().matches(clientList.get(i).getUsername().toLowerCase())){
                    return "client";
                }
            }
        }
        if(pustakawanList.size() > 0){
            for(int i = 0; i < pustakawanList.size(); i++){
                if(username.toLowerCase().matches(pustakawanList.get(i).getUsername().toLowerCase())){
                    return "pustakawan";
                }
            }
        }
        return "none";
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean userExist(String username){
        if(clientList.size() > 0){
            for(int i = 0; i < clientList.size(); i++){
                if(username.toLowerCase().matches(clientList.get(i).getUsername().toLowerCase())){
                    return false;
                }
            }
        }
        if(pustakawanList.size() > 0){
            for(int i = 0; i < pustakawanList.size(); i++){
                if(username.toLowerCase().matches(pustakawanList.get(i).getUsername().toLowerCase())){
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String userMaker(){
        String username = inputText("Masukkan Username: ", true);
        if(userExist(username)){
            return username;
        } else{
            System.out.println("\nUsername Sudah Ada!");
            return userMaker();
        }
    }

    public static String primaryKeyMaker(String keyType){
        String key = inputTextNum("Masukkan " + keyType + ": ");
        if(primaryKeyCheck(key)){
            return key;
        } else{
            System.out.println("\n" + keyType + " Sudah Terdaftar!");
            return primaryKeyMaker(keyType);
        }
    }

    public static String uniqueKeyMaker(String keyType){
        String key = inputTextNum("Masukkan " + keyType + ": ");
        if(uniqueKeyCheck(key)){
            return key;
        } else{
            System.out.println("\n" + keyType + " Sudah Terdaftar!");
            return uniqueKeyMaker(keyType);
        }
    }

    public static int generateID(String jenisID){
        jenisID = jenisID.toLowerCase();
        int id = 0;
        switch(jenisID){
            case "pustakawan": 
                id = (pustakawanList.size() == 0) ? 0 : pustakawanList.get(pustakawanList.size() - 1).getIdPustakawan() + 1;
                break;
            case "koleksi": 
                id = (koleksiList.size() == 0) ? 0 : koleksiList.get(koleksiList.size() - 1).getIdKoleksi() + 1;
                break;
            case "transaksi": 
                id = (transaksiList.size() == 0) ? 0 : transaksiList.get(transaksiList.size() - 1).getIdTransaksi() + 1;
                break;
        }
        return id;
    }

    public static void addUser(){
        System.out.println("Pendaftaran Client:");
        String posisi = enumerator("Posisi:", new String[] {"Dosen", "Staff", "Mahasiswa"});
        clientList.add(new Client(primaryKeyMaker((posisi.matches("Mahasiswa")) ? "NIM" : "NIK"), toTitle(inputText("Masukkan Nama: ", false)), null, null, posisi, null, jenisKelamin(), "", null));
        int clientIndex = (clientList.size() > 0) ? clientList.size() - 1 : 0;
        String[] fakultasProdi = prodiSelector();
        clientList.get(clientIndex).setFakultas(fakultasProdi[0]);
        clientList.get(clientIndex).setProdi(fakultasProdi[1]);
        int jumlahTelp = inputInt("Jumlah Nomor Telepon: ", 1, 10);
        String[] noTelp = new String[jumlahTelp];
        for(int i = 0; i < jumlahTelp; i++){
            noTelp[i] = inputTextNum("Nomor Telepon " + (i + 1) + ": ");
        }
        clientList.get(clientIndex).setNoTelp(noTelp);
        clientList.get(clientIndex).setUsername(userMaker());
        clientList.get(clientIndex).setPassword(inputText("Masukkan Password: ", true));
    }

    public static void addPustakawan(){
        System.out.println("Pendaftaran Pustakawan:");
        pustakawanList.add(new Pustakawan(generateID("pustakawan"), toTitle(inputText("Masukkan Nama: ", false)), inputEmail(), jenisKelamin(), null, "", null));
        int pustakawanIndex = (pustakawanList.size() > 0) ? pustakawanList.size() - 1 : 0;
        int jumlahTelp = inputInt("Jumlah Nomor Telepon: ", 1, 10);
        String[] noTelp = new String[jumlahTelp];
        for(int i = 0; i < jumlahTelp; i++){
            noTelp[i] = inputTextNum("Nomor Telepon " + (i + 1) + ": ");
        }
        pustakawanList.get(pustakawanIndex).setNoTelp(noTelp);
        pustakawanList.get(pustakawanIndex).setUsername(userMaker());
        pustakawanList.get(pustakawanIndex).setPassword(inputText("Masukkan Password: ", true));
    }

    public static String[] prodiSelector(){
        String[] prodi = new String[] {
            "Manajemen",
            "Informatika",
            "Desain Komunikasi Visual"
        };
        String fakultas = "";

        String pilih = enumerator("Pogram Studi:", prodi);
        switch(pilih){
            case "Manajemen":
                fakultas = "Ekonomi";
                break;
            case "Informatika":
            case "Desain Komunikasi Visual":
                fakultas = "Teknik";
                break;
        }
        return new String[] {fakultas, pilih};
    }

    public static String toTitle(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
    
        StringBuilder converted = new StringBuilder();
    
        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
    
        return converted.toString();
    }

    public static String enumerator(String enumTitle, String[] choices){
        System.out.println(enumTitle);
        int count = 1;
        for(String choice:choices){
            System.out.println(count + ". " + choice);
            count++;
        }
        System.out.print("Masukkan Pilihan: ");
        int pilihan;
        try{
            pilihan = Integer.parseInt(input.nextLine());
            if(pilihan <= choices.length && pilihan > 0){
                return choices[pilihan-1];
            } else{
                System.out.println("\nWrong Input!");
                return enumerator(enumTitle, choices);
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return enumerator(enumTitle, choices);
        }
        // Copyright By Gibek
    }

    public static String enumeratorSilence(String[] choices){
        System.out.print("Masukkan Pilihan: ");
        int pilihan;
        try{
            pilihan = Integer.parseInt(input.nextLine());
            if(pilihan <= choices.length && pilihan > 0){
                return choices[pilihan-1];
            } else{
                System.out.println("\nWrong Input!");
                return enumeratorSilence(choices);
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return enumeratorSilence(choices);
        }
        // Copyright By Gibek
    }

    public static int inputUang(String prompt, int max){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value > max){
                System.out.println("\nYour Input Exeeds The Limit of " + max + "!");
                return inputUang(prompt, max);
            }else if(value % mataUang != 0){
                System.out.println("\nYour Input Isn't Valid");
                return inputUang(prompt, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputUang(prompt, max);
        }
    }

    public static char jenisKelamin(){
        if(inputBool("Jenis Kelamin", "M", "F")){
            return 'M';
        } else{
            return 'F';
        }
    }

    public static String inputDate(String prompt){
        Pattern DATE_PATTERN = Pattern.compile(
      "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$" 
      + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
      + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
      + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
        System.out.print(prompt + " (YYYY-MM-DD): ");
        try{
            String value = input.nextLine();
            if(value.matches("") || !DATE_PATTERN.matcher(value).matches() || value.matches("^[\\s]*$")){
                System.out.println("\nWrong Input!");
                return inputDate(prompt);
            }else {
                int date = Integer.parseInt(value.split("-")[2]);
                int month = Integer.parseInt(value.split("-")[1]);
                int year = Integer.parseInt(value.split("-")[0]);
                boolean lolos = true;
                switch(month){
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                    case 12:
                        if(date > 30){
                            System.out.println("\nWrong Input!");
                            lolos = false;
                        }
                        break;
                    case 2:
                        if(year % 4 == 0 && date > 29){
                            System.out.println("\nWrong Input!");
                            lolos = false;
                        }
                        if(year % 4 != 0 && date > 28){
                            System.out.println("\nWrong Input!");
                            lolos = false;
                        }
                        break;
                }
                return (lolos) ? value : inputDate(prompt);
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputDate(prompt);
        }
    }

    public static String inputText(String prompt, boolean allowNum){
        System.out.print(prompt);
        try{
            String value = input.nextLine();
            if(!allowNum){
                if(value.matches("") || !value.matches("^[a-zA-Z\\s]*$") || value.matches("^[\\s]*$")){
                    System.out.println("\nWrong Input!");
                    return inputText(prompt, allowNum);
                }else {
                    return value;
                }
            } else{
                if(value.matches("") || !value.matches("^[a-zA-Z0-9\\s]*$") || value.matches("^[\\s]*$")){
                    System.out.println("\nWrong Input!");
                    return inputText(prompt, allowNum);
                }else {
                    return value;
                }
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputText(prompt, allowNum);
        }
    }

    public static String inputTextNum(String prompt){
        System.out.print(prompt);
        try{
            String value = input.nextLine();
            if(value.matches("") || !value.matches("^[0-9]*$") || value.matches("^[\\s]*$")){
                System.out.println("\nWrong Input!");
                return inputTextNum(prompt);
            }else {
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputTextNum(prompt);
        }
    }

    public static String inputEmail(){
        System.out.print("Masukkan Email: ");
        try{
            String value = input.nextLine();
            if(value.matches("") || value.matches("^[\\s]*$")){
                System.out.println("\nInput Empty!");
                return inputEmail();
            }else {
                if(isValidEmail(value)){
                    return value;
                } else{
                    System.out.println("\nInvalid Email!");
                    return inputEmail();
                }
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputEmail();
        }
    }

    public static int inputInt(String prompt, int min){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value < min){
                System.out.println("\nYour Input is Less Than " + min + "!");
                return inputInt(prompt, min);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputInt(prompt, min);
        }
    }

    public static int inputIntMax(String prompt, int max){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value > max){
                System.out.println("\nYour Input Exeeds The Limit of " + max + "!");
                return inputIntMax(prompt, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputIntMax(prompt, max);
        }
    }

    public static int inputIntMin(String prompt){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value <= 0){
                System.out.println("\nWrong Input!");
                return inputIntMin(prompt);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputIntMin(prompt);
        }
    }

    public static int inputInt(String prompt, int min, int max){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value < min || value > max){
                System.out.println("\nYour Input Value Doesn't Match The Range!");
                return inputInt(prompt, min, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputInt(prompt, min, max);
        }
    }

    public static int inputInt(String prompt){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            return value;
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputInt(prompt);
        }
    }

    public static float inputFloat(String prompt){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            return value;
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt);
        }
    }

    public static float inputFloat(String prompt, float min){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value < min){
                System.out.println("\nYour Input is Less Than " + min + "!");
                return inputFloat(prompt, min);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt, min);
        }
    }

    public static float inputFloatMin(String prompt){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value <= 0){
                System.out.println("\nWrong Input!");
                return inputFloat(prompt);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt);
        }
    }

    public static float inputFloatMax(String prompt, float max){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value > max){
                System.out.println("\nYour Input Exeeds The Limit of " + max + "!");
                return inputFloatMax(prompt, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloatMax(prompt, max);
        }
    }

    public static float inputFloat(String prompt, float min, float max){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value < min || value > max){
                System.out.println("\nYour Input Value Doesn't Match The Range!");
                return inputFloat(prompt, min, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt, min, max);
        }
    }

    public static boolean inputBool(String prompt, String ifTrue, String ifFalse){
        System.out.print(prompt + " (" + ifTrue + "/" + ifFalse + "): ");
        String jawaban;
        try{
            jawaban = input.nextLine().toLowerCase();
            if(jawaban.matches(ifTrue.toLowerCase())){
                return true;
            } else if(jawaban.matches(ifFalse.toLowerCase())){
                return false;
            } else{
                System.out.println("\nWrong Input!");
                return inputBool(prompt, ifTrue, ifFalse);
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputBool(prompt, ifTrue, ifFalse);
        }
    }

    public static boolean primaryKeyCheck(String key){
        if(clientList.size() > 0){
            for(int i = 0; i < clientList.size(); i++){
                if(key.toLowerCase().matches(clientList.get(i).getIdPengenal())){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean uniqueKeyCheck(String key){
        if(koleksiList.size() > 0){
            for(int i = 0; i < koleksiList.size(); i++){
                if(koleksiList.get(i) instanceof BukuMajalah){
                    if(key.toLowerCase().matches(((BukuMajalah) koleksiList.get(i)).getISBN())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean printTXT(String bannerTitle, String tableFormat, Object[][] rowData, String pathToFile, Object[] columnNames, int dataLengthTotal) throws IOException{
        try{
            PrintWriter txt = new PrintWriter (pathToFile);
            String titleBanner = "+", titlePadding = "";
            if(dataLengthTotal % 2 == 0){
                for(int i=0; i < dataLengthTotal; i++){
                    titleBanner += "-";
                }
            } else{
                for(int i=0; i < dataLengthTotal; i++){
                    titleBanner += "-";
                }
            }
            titleBanner += "+";
            for(int i=0; i < ((dataLengthTotal-bannerTitle.length())/2)-1; i++){
                titlePadding += " ";
            }
            txt.printf(titleBanner + "%n");
            txt.printf("|" + titlePadding + ((bannerTitle.length() % 2 != 0) ? " " : "") + bannerTitle + " " + titlePadding + "|%n");
            txt.printf(titleBanner + "%n");
            txt.printf(tableFormat, columnNames);
            txt.printf(titleBanner + "%n");
            for (int n = 0; n < rowData.length; n++) {
                txt.printf(tableFormat, rowData[n]);
            }
            txt.printf(titleBanner + "%n");
            txt.close();
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public static void generateTable(String bannerTitle, String tableFormat, Object[][] rowData, Object[] columnNames, int dataLengthTotal){
            String titleBanner = "+", titlePadding = "";
            if(dataLengthTotal % 2 == 0){
                for(int i=0; i < dataLengthTotal-1; i++){
                    titleBanner += "-";
                }
            } else{
                for(int i=0; i < dataLengthTotal-1; i++){
                    titleBanner += "-";
                }
            }
            titleBanner += "+";
            for(int i=0; i < ((dataLengthTotal-bannerTitle.length())/2)-1; i++){
                titlePadding += " ";
            }
            System.out.printf(titleBanner + "%n");
            System.out.printf("|" + titlePadding + ((bannerTitle.length() % 2 != 0) ? " " : "") + bannerTitle + " " + titlePadding + "|%n");
            System.out.printf(titleBanner + "%n");
            System.out.printf(tableFormat, columnNames);
            System.out.printf(titleBanner + "%n");
            for (int n = 0; n < rowData.length; n++) {
                System.out.printf(tableFormat, rowData[n]);
            }
            System.out.printf(titleBanner + "%n");
    }

    public static Object[][] dataGenerator(int rowCount, Object[] columnNames){
        Object rowData[][] =  new Object[rowCount][columnNames.length];
        return rowData;
    }

    public static String tableFormatter(Object[][] rowData, Object[] columnNames){
        int[] dataLength = new int[columnNames.length];
        for (int length = 0; length < columnNames.length; length++) {
            if (columnNames[length] != null) {
                int panjangHeader = columnNames[length].toString().length();
                if (panjangHeader > dataLength[length]) {
                    dataLength[length] = panjangHeader;
                }
            }
        }

        for(int i = 0; i < rowData.length; i++){
            for (int l = 0; l < columnNames.length; l++) {
                if (rowData[i][l] != null) {
                    int panjangData = rowData[i][l].toString().length();
                    if (panjangData > dataLength[l]) {
                        dataLength[l] = panjangData;
                    }
                }
            }
        }
        
        String formatTable = "| %";
        int dataLengthTotal = 0;
        for(int a = 0; a < dataLength.length; a++) {
            formatTable += "-" + (dataLength[a] + 2) + "s | %";
            dataLengthTotal += dataLength[a] + 5;
        }
        formatTable += "n";
        return formatTable + "/" + dataLengthTotal;
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

    public static String dateAdder(String date, int days) {
        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        long msCounter = 86400000;
        try{ 
            Date tgl = obj.parse(date);
            Date result = new Date(tgl.getTime() + msCounter * days);
            return obj.format(result);
        } catch(Exception e){
            System.err.println("Something went wrong!");
            return "";
        }
    }
}