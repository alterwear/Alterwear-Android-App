# Alterwear-Android-App

## Setup Development Environment
0. Download [the latest version of Android Studio](https://developer.android.com/studio/). This README was written with version 3.2.1 (181.5056338-windows).
1. This app was developed for Android ???, v ??. So you may need to install an older SDK.
2. Pull this repo to computer: ```git clone https://github.com/molecule/Alterwear-Android-App.git```
3. Open the app as an "Existing Android Studio project".
4. Test that everything installed correctly by connecting your Android device, and hitting the big green play button, which builds and pushes the app to your phone. You may get a pop-up on the phone asking you to "Allow USB debugging?". Click "Always allow from this computer" to stop this from showing up again.
5. If this is the first time you're developing for this phone, you may need to install the platform specific to your device. Do so.
6. 

## Programming
0. The majority of changes are made to app/src/main/java/lab/ecologies/hybrid/alterwear/MainActivity.java

## Usage
0. Click a button. A dialog box will pop up asking you to tag an NFC tag. Tag an NFC tag and the dialog box will disappear. The phone should chime once it has successfully transferred the data. The data that was transferred is printed to the screen in a textbox that's below all the buttons. A Toast also pops up to indicate how long it took to complete the write.

## Origins
The original source code was [here](https://www.nxp.com/products/identification-and-security/nfc/nfc-tags-for-electronics/ntag-ic-iplus-i-explorer-kit:OM5569-NT322E?&fpsp=1&tab=Design_Tools_Tab), linked from [this thread on the NXP forums](https://community.nxp.com/thread/386404)

Specifically, scroll down and click on NTAG I²C Demo Android App Source code ([picture here](https://www.dropbox.com/s/euky57xeaabv2xb/Screenshot%202018-04-16%2013.45.59.png?dl=0))
