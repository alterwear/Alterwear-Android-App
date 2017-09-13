package lab.ecologies.hybrid.alterwear;

/**
 * Created by Molly on 9/13/2017.
 */

public class Ntag_Get_Version {

    /**
     * Enum for different Products.
     */
    public enum Prod {
        NTAG_I2C_1k(888), NTAG_I2C_2k(1904), NTAG_I2C_1k_T(888), NTAG_I2C_2k_T(1904), NTAG_I2C_1k_V(888), NTAG_I2C_2k_V(1904),
        NTAG_I2C_1k_Plus(888), NTAG_I2C_2k_Plus(1912),
        Unknown(0), MTAG_I2C_1k(720), MTAG_I2C_2k(1440);

        private int mem_size;

        private Prod(int mem_size) {
            this.mem_size = mem_size;
        }

        /**
         * gets the Memsize of a Tag.
         *
         * @return Memsize of a Tag
         */
        public int getMemsize() {
            return mem_size;
        }
    }

    private byte vendor_ID;
    private byte product_type;
    private byte product_subtype;
    private byte major_product_version;
    private byte minor_product_version;
    private byte storage_size;
    private byte protocol_type;

    /**
     * Get version Response of a NTAG_I2C_1K.
     */
    public static final Ntag_Get_Version NTAG_I2C_1k;
    public static final Ntag_Get_Version NTAG_I2C_1k_T;
    public static final Ntag_Get_Version NTAG_I2C_1k_V;

    /**
     * Get version Response of a NTAG_I2C_2K.
     */
    public static final Ntag_Get_Version NTAG_I2C_2k;
    public static final Ntag_Get_Version NTAG_I2C_2k_T;
    public static final Ntag_Get_Version NTAG_I2C_2k_V;

    /**
     * Get version Response for NTAG I2C Plus products.
     */
    public static final Ntag_Get_Version NTAG_I2C_1k_Plus;
    public static final Ntag_Get_Version NTAG_I2C_2k_Plus;

    public static final Ntag_Get_Version MTAG_I2C_1k;
    public static final Ntag_Get_Version MTAG_I2C_2k;
    public static final Ntag_Get_Version TNPI_6230;
    public static final Ntag_Get_Version TNPI_3230;

    static {
        NTAG_I2C_1k = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x01, 0x01, 0x13, 0x03 });
        NTAG_I2C_2k = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x01, 0x01, 0x15, 0x03 });

        NTAG_I2C_1k_V = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x02, 0x00, 0x13, 0x03 });
        NTAG_I2C_2k_V = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x02, 0x00, 0x15, 0x03 });

        NTAG_I2C_1k_T = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x02, 0x01, 0x13, 0x03 });
        NTAG_I2C_2k_T = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x02, 0x01, 0x15, 0x03 });

        NTAG_I2C_1k_Plus = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x02, 0x02, 0x13, 0x03 });
        NTAG_I2C_2k_Plus = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x04, 0x05, 0x02, 0x02, 0x15, 0x03 });

        MTAG_I2C_1k = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x05, 0x07, 0x02, 0x02, 0x13, 0x03 });
        MTAG_I2C_2k = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x05, 0x07, 0x02, 0x02, 0x15, 0x03 });

        TNPI_6230 = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x05, 0x05, 0x01, 0x01, 0x15, 0x03 });
        TNPI_3230 = new Ntag_Get_Version(new byte[] { 0x00, 0x04, 0x05, 0x05, 0x01, 0x01, 0x13, 0x03 });
    }

    /**
     * Returns the Product to which this get Version Response belongs.
     *
     * @return Product
     */
    public Prod Get_Product() {
        if (this.equals(NTAG_I2C_1k))
            return Prod.NTAG_I2C_1k;
        if (this.equals(NTAG_I2C_2k))
            return Prod.NTAG_I2C_2k;
        if (this.equals(NTAG_I2C_1k_T))
            return Prod.NTAG_I2C_1k;
        if (this.equals(NTAG_I2C_2k_T))
            return Prod.NTAG_I2C_2k;
        if (this.equals(NTAG_I2C_1k_V))
            return Prod.NTAG_I2C_1k;
        if (this.equals(NTAG_I2C_2k_V))
            return Prod.NTAG_I2C_2k;
        if (this.equals(NTAG_I2C_1k_Plus))
            return Prod.NTAG_I2C_1k_Plus;
        if (this.equals(NTAG_I2C_2k_Plus))
            return Prod.NTAG_I2C_2k_Plus;
        if (this.equals(MTAG_I2C_1k))
            return Prod.MTAG_I2C_1k;
        if (this.equals(MTAG_I2C_2k))
            return Prod.MTAG_I2C_2k;
        else
            return Prod.Unknown;
    }

    /**
     * Constructor.
     *
     * @param Data
     *            Data from the Get Version Command
     */
    public Ntag_Get_Version(byte[] Data) {
        vendor_ID = Data[1];
        product_type = Data[2];
        product_subtype = Data[3];
        major_product_version = Data[4];
        minor_product_version = Data[5];
        storage_size = Data[6];
        protocol_type = Data[7];
    }
}

