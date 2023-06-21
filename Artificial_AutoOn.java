 byte[] imageBuffer1 = new byte[deviceInfo.imageWidth * deviceInfo.imageHeight];
        while (true) {
            long errorCodee = jsgfpLib.GetImage(imageBuffer1);
            if (errorCodee == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                System.out.println("First fingerprint captured successfully.");
                
            } else if (errorCodee == SGFDxErrorCode.SGFDX_ERROR_WRONG_IMAGE) {
                System.out.println("No finger detected. Place your finger on the sensor.");
            } else {
                System.out.println("Failed to capture first fingerprint.");
                jsgfpLib.CloseDevice();
                return;
            }
        }