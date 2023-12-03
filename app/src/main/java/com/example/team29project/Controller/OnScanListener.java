package com.example.team29project.Controller;

/**
 * Listener interface when it finishes picking options for scanning
 */
public interface OnScanListener {
    void onScannedSerial(String scan);
    void onScannedBarcode(String scan);

}
