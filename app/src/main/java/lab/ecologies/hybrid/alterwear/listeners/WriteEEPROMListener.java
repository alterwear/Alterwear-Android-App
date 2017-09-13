package lab.ecologies.hybrid.alterwear.listeners;

public interface WriteEEPROMListener {
	/**
	 * It informs the listener about the number of bytes written in the EEPROM
	 * Used to inform about the progress during the SpeedTest
	 * 
	 * @param bytes
	 */
    public abstract void onWriteEEPROM(int bytes);
}
